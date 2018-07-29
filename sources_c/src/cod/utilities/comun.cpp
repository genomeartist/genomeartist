#ifdef _WIN32
#include <windows.h>
#include <psapi.h>
#include <string>
#include "../../headere/comun.h"
void SharedMemoryKeepAliveService::Start()
{
	char cmd[MAX_PATH];
	STARTUPINFO si;
	memset(&si, 0, sizeof(si));
	si.cb = sizeof(si);
	PROCESS_INFORMATION pi;
	if (!CreateProcess(SHARED_MEMORY_KEEP_ALIVE_MODULE_NAME, cmd, NULL, NULL, FALSE, CREATE_NO_WINDOW, NULL, NULL, &si, &pi))
	{
		fprintf(stderr, "Error creating shared memory keep alive process\n");
		exit(-1);
	}

	WaitForInputIdle(pi.hProcess,INFINITE);

	Sleep(100);

	if (pi.hProcess)
	{
		CloseHandle(pi.hProcess);
	}

	if (pi.hThread)
	{
		CloseHandle(pi.hThread);
	}
}

void SharedMemoryKeepAliveService::Stop()
{
	unsigned long processes[1024];
	unsigned long neededBytesCount;

	if (!EnumProcesses(processes, sizeof(processes), &neededBytesCount))
	{
		fprintf(stderr, "Process enumeration error\n");
		exit(-1);
	}

	unsigned long numberOfProcesses(neededBytesCount / sizeof(unsigned long));
	for (unsigned int i = 0; i < numberOfProcesses; ++i)
	{
		if (processes[i] == 0)
			continue;

		HANDLE hProcess = OpenProcess(PROCESS_QUERY_INFORMATION | PROCESS_VM_READ | PROCESS_TERMINATE, 0, processes[i]);
		TCHAR szProcessName[MAX_PATH];

		const std::string targetProcessName(SHARED_MEMORY_KEEP_ALIVE_MODULE_NAME);

		if (NULL != hProcess)
		{
			HMODULE hMod;
			DWORD cbNeeded;

			typedef BOOL(WINAPI *EnumProcessModulesExFunctionPtr)(HANDLE, HMODULE*, DWORD, LPDWORD, DWORD);

			EnumProcessModulesExFunctionPtr EnumProcessModulesExFromDLL = NULL;

			HMODULE hLoadMe = LoadLibrary("psapi.dll");

			if (hLoadMe == NULL)
			{
				fprintf(stderr, "Couldn't load psapi.dll\n");
				exit(-1);
			}

			EnumProcessModulesExFromDLL = (EnumProcessModulesExFunctionPtr)GetProcAddress(hLoadMe, "EnumProcessModulesEx");

			if (EnumProcessModulesExFromDLL == NULL)
			{
				fprintf(stderr, "Couldn't retrieve function EnumProcessModulesEx\n");
				exit(-1);
			}

			if (EnumProcessModulesExFromDLL(hProcess, &hMod, sizeof(hMod), &cbNeeded, LIST_MODULES_ALL))

			{
				GetModuleFileNameEx(hProcess, hMod, szProcessName,
					sizeof(szProcessName) / sizeof(char));
			}
			std::string currentProcessName(szProcessName);
			currentProcessName = currentProcessName.substr(currentProcessName.find_last_of("\\/") + 1);
			if (currentProcessName == targetProcessName)
			{
				TerminateProcess(hProcess, 0);
			}
			CloseHandle(hProcess);
		}
	}
}
#endif