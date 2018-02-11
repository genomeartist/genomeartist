#ifdef _WIN32
#include <windows.h>
#endif

#include "../../headere/comun.h"

int main()
{
#ifdef _WIN32
	HANDLE mem_h = OpenFileMapping(FILE_MAP_READ, false, NUME_MEMORIE_PARTAJATA);
	if (mem_h == NULL)
	{
		fprintf(stderr, "Eroare la accesarea memoriei partajate\n");
		exit(-1);
	}
	
	HANDLE ext_mem_h = OpenFileMapping(FILE_MAP_READ, false, NUME_MEMORIE_PARTAJATA_POZITIONARE);
	if (ext_mem_h == NULL)
	{
		fprintf(stderr, "Eroare la accesarea memoriei partajate\n");
		exit(-1);
    }
	
	while (true)
	{
		getchar();
	}

	CloseHandle(mem_h);
	CloseHandle(ext_mem_h);
#else

#endif
	return 0;
}
