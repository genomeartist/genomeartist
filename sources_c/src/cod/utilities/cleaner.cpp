#ifndef _WIN32
#ifdef __APPLE__
        #include <fcntl.h>
#else
        #include <mqueue.h>
#endif
	#include <stdio.h>
	#include <sys/types.h>
	#include <sys/mman.h>
	#include <string.h>
	#include <stdlib.h>
	#include <errno.h>
	#include <unistd.h>
	#include <sys/stat.h>
#else
	
#endif

#include "../../headere/comun.h"

int main(int argc,char *args[])
{
	#ifndef _WIN32
		char * nume_memorie;
		memset(nume_memorie, 0, sizeof(NUME_MEMORIE_PARTAJATA)+sizeof(args[1]));

		sprintf(nume_memorie,"%s_%s",NUME_MEMORIE_PARTAJATA,args[1]);
		printf("CLEANER: NUME_MEMORIE_PARTAJATA este %s", nume_memorie);

		shm_unlink(NUME_MEMORIE_PARTAJATA);
		shm_unlink(NUME_MEMORIE_PARTAJATA_POZITIONARE);
	#else
	    SharedMemoryKeepAliveService::Stop();
	#endif
	return 0;
}
