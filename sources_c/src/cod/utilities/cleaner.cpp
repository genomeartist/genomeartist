#ifndef _WIN32
	#include <stdio.h>
	#include <sys/types.h>
	#include <sys/mman.h>
	#include <mqueue.h>
	#include <string.h>
	#include <stdlib.h>
	#include <errno.h>
	#include <unistd.h>
	#include <sys/stat.h>
#else
	
#endif

#include "../../headere/comun.h"

int main()
{
	#ifndef _WIN32
		shm_unlink(NUME_MEMORIE_PARTAJATA);
		shm_unlink(NUME_MEMORIE_PARTAJATA_POZITIONARE);
	#else
	    SharedMemoryKeepAliveService::Stop();
	#endif
	return 0;
}
