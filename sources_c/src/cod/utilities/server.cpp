#ifdef _WIN32
        #include <fcntl.h>
	#include <io.h>
        #include <windows.h>
#else
	#include <stdio.h>
	#include <sys/types.h>
	#include <sys/mman.h>
        #ifdef __APPLE__
                #include <fcntl.h>
        #else
                #include <mqueue.h>
        #endif
	#include <string.h>
	#include <stdlib.h>
	#include <errno.h>
	#include <unistd.h>
	#include <sys/stat.h>
#define O_BINARY 0
#endif

#include "../../headere/comun.h"
#include "../../headere/gene_retrieval.h"

#define MAX_NUME_FISIER 256

#ifdef _WIN32
	HANDLE mem_h;
	HANDLE mem_acc_h;
#else
	int mem_h;
	int mem_acc_h;
#endif

/*
 *
 * This file is part of Genome Artist.
 *
 * Genome Artist is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Genome Artist is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Genome Artist.  If not, see <http://www.gnu.org/licenses/>.
 *
 */


char *mem;
char *mem_acc;

void test_continut()
{
	unsigned long i,j;
	unsigned long numar_fisiere = *(unsigned long*)mem;
	struct FISIER_DATE *fisier_date;
	int marime_date = 0;
	struct INFORMATII_POZITIONARE *temp_acc;

	printf("----------------------------\n");
	printf("numar fisierere = %lu\n",numar_fisiere);
	fisier_date = (struct FISIER_DATE*)(mem+sizeof(unsigned long));
	for (i=0; i<numar_fisiere; i++)
	{
		printf("Nume fisier = %s\n",fisier_date->nume_fisier);
		printf("offset_start = %lu\n",fisier_date->offset_start);
		printf("marime = %lu\n",fisier_date->marime);
		printf("offset acc = %lu\n",fisier_date->offset_start_acc);
		printf("numar structuri = %lu\n",fisier_date->acc_count);
		printf("Titlu fisier = %s\n",fisier_date->titlu_fisier);
		if (fisier_date->isTransposon)
			printf("Tip fisier = T\n");
		else
			printf("Tip fisier = G\n");
		temp_acc = (struct INFORMATII_POZITIONARE*)(mem_acc + fisier_date->offset_start_acc);
		for (j=0; j<5; j++)
		{
			printf("     %s  ##  %s  ##  %lu  #  %lu\n",temp_acc->nume,temp_acc->cod,temp_acc->offset_start,temp_acc->offset_stop);
			temp_acc += 1;
		}
		printf("-------------------------------------\n");
		marime_date += fisier_date->marime + 1;
		if (i < numar_fisiere-1)
			fisier_date += 1;
	}
	//for (i=0; i<numar_fisiere; i++)
	//{
		/*
		printf("Datele concatenate :\n");
		write(1,mem+sizeof(int)+numar_fisiere*MARIME_STRUCTURA,marime_date);
		printf("\n-----------------------------------\n");
		*/
	//}
}

void release(long long marime_totala, long long marime_totala_acc)
{
	#ifdef _WIN32
		UnmapViewOfFile(mem);
		CloseHandle(mem_h);

		UnmapViewOfFile(mem_acc);
		CloseHandle(mem_acc_h);
	#else
		munmap(mem,marime_totala);
		close(mem_h);

		munmap(mem_acc,marime_totala_acc);
		close(mem_acc_h);
	#endif
}

void delincheaza()
{
	#ifdef _WIN32
	#else
		shm_unlink(NUME_MEMORIE_PARTAJATA);
		shm_unlink(NUME_MEMORIE_PARTAJATA_POZITIONARE);
	#endif
}

char* obtine_memorie(const char *nume_zona_memorie, long long marime_zona_memorie, int optiune)
{
	char *temp_mem;
	#ifdef _WIN32
		HANDLE temp_mem_h;
		temp_mem_h = CreateFileMapping(INVALID_HANDLE_VALUE,NULL,PAGE_READWRITE,0,marime_zona_memorie,nume_zona_memorie);
		if ( temp_mem_h == INVALID_HANDLE_VALUE )
		{
			fprintf(stderr,"Eroare la crearea memoriei partajate\n");
			exit(-1);
		}
		temp_mem = (char*)MapViewOfFile(temp_mem_h,FILE_MAP_READ|FILE_MAP_WRITE,0,0,0);
		if ( temp_mem == NULL )
		{
			fprintf(stderr,"Eroare la maparea memoriei\n");
			exit(-1);
		}
	#else
		int temp_mem_h;
		//creez cadrul pentru memoria partajata
		if ( (temp_mem_h = shm_open(nume_zona_memorie, O_CREAT|O_EXCL |O_RDWR, 0666)) == -1 )
		{
			fprintf(stderr,"Eroare la crearea memoriei partajate\n");
			fprintf(stdout,"@error: Error creating shared memory\n");
			fflush(stdout);
			exit(-1);
		}
		//ii aloc spatiul necesar
		if ( ftruncate(temp_mem_h,marime_zona_memorie)==-1 )
		{
			fprintf(stderr,"Eroare la redimensionare memorie\n");
			fprintf(stdout,"@error: Loaded files are too large.\n");
			fflush(stdout);
			exit(-1);
		}
		//mapez memoria parajata in spatiul de adresa al acestui proces
		if ( (temp_mem = (char*) mmap(0, marime_zona_memorie, PROT_READ | PROT_WRITE, MAP_SHARED, temp_mem_h, 0)) == NULL )
		{
			fprintf(stderr,"Eroare la maparea memoriei\n");
			fprintf(stdout,"@error: Loaded files are too large.\n");
			fflush(stdout);
			exit(-1);
		}

	#endif
	//memoria alocata este initializata
	memset(temp_mem,0,marime_zona_memorie);
	if (optiune == 0)
		mem_h = temp_mem_h;
	else
		mem_acc_h = temp_mem_h;
	return temp_mem;
}

unsigned long get_file_size(char *fileName)
{
	unsigned long endPos = 0;
	FILE * stream = fopen( fileName, "r" );
	if ( stream == NULL )
	{
		fclose(stream);
		fprintf(stderr,"[1]Eroare la obtinerea informatiilor despre fisierul de date '%s' \n",fileName);
		exit(-1);
	}
	fseek( stream, 0L, SEEK_END );
	endPos = ftell( stream );
	fclose( stream );
	return endPos;
}

int main(int argc,char *args[])
{
	#ifndef _WIN32
		struct stat informatii_fisier;
    #else
	    SharedMemoryKeepAliveService::Stop();
	#endif

	char nume_fisier_date[MAX_NUME_FISIER];
	char nume_fisier_acc[MAX_NUME_FISIER];
	char titlu_fisier[MAX_NUME_FISIER];
	char tip_fisier[5];
	unsigned long nr_inregistrari_acc_total = 0;
	unsigned long nr_inregistrari_acc = 0;
    long long marime_totala = 0;
	long long marime_totala_acc;
	char linie[2*MAX_NUME_FISIER+64];
	FILE* fisier_intrare_acc;
	struct INFORMATII_POZITIONARE *informatii_pozitionare;
	unsigned long offset_curent_acc = 0;

	FILE* fisier_intrare;
	unsigned long numar_fisiere = 0;
	unsigned long contor_fisiere = 0;
	unsigned long progress = 0;
	unsigned long marime_date = 0;
	struct FISIER_DATE *fisier_date;
	unsigned long offset_curent = 0;
	char *date = NULL;

	unsigned long tempSize = 0;

	delincheaza();

	if (argc != 2)
	{
		fprintf(stderr,"@error:Nu s-au dat parametrii necesari !\n");
		return -1;
	}
	//inainte de a aloca spatiul necesar, este nevoie sa aflu de exact cat spatiu am nevoie
	//ca parametru primesc numele unui fisier in care se gasesc numele fisierelor al caror continut trebuie incarcat in memoria partajata
	fisier_intrare = fopen(args[1], "r");
	if (fisier_intrare == NULL )
	{
		fprintf(stderr,"@error:Fisierul de intrare %s nu poate fi deschis\n",args[1]);
		fprintf(stdout,"@error: Error opening configuration file %s\n", args[1]);
		fflush(stdout);
		return -1;
	}
	//iau toate fisierele date
	while (fgets(linie,2*MAX_NUME_FISIER+64,fisier_intrare) != NULL)
	{
		linie[strlen(linie)-1] = 0;
		//diezul la inceput liniei este folosit pentru comentarii
		if (linie[0] != '#')
		{
			sscanf(linie,"%s %s %lu %s %s",nume_fisier_date,nume_fisier_acc,&nr_inregistrari_acc,titlu_fisier,tip_fisier);
			printf("linie citita : %s %s %lu %s %s\n",nume_fisier_date,nume_fisier_acc,nr_inregistrari_acc,titlu_fisier,tip_fisier);
			nr_inregistrari_acc_total += nr_inregistrari_acc;
	
			#ifdef _WIN32
				tempSize = get_file_size(nume_fisier_date);
			#else
				if ( stat(nume_fisier_date,&informatii_fisier) == -1 )
				{
					fclose(fisier_intrare);
					fprintf(stderr,"@error:[1]Eroare la obtinerea informatiilor despre fisierul de date '%s' \n",nume_fisier_date);
					fprintf(stdout,"@error: Error getting info about file %s\n", nume_fisier_date);
					fflush(stdout);
					return -1;
				}
				tempSize = informatii_fisier.st_size;
			#endif
			//imi trebuie marimea totala a datelor si numarul de fisiere
			//dupa datele din fiecare fisier se adauga un 0
			marime_date += tempSize + 1;
			numar_fisiere++;
		}
	}

	printf("nr total inregistrati acc : %lu\n",nr_inregistrari_acc_total);

	//calculez marimea totala necesara pentru memoria partajata
	marime_totala = sizeof(unsigned long) + numar_fisiere * MARIME_STRUCTURA + marime_date;
	marime_totala_acc = sizeof(unsigned long) + MARIME_STRUCTURA_ACC * nr_inregistrari_acc_total;

	mem = obtine_memorie(NUME_MEMORIE_PARTAJATA, marime_totala, 0);
	mem_acc = obtine_memorie(NUME_MEMORIE_PARTAJATA_POZITIONARE, marime_totala_acc, 1);
#ifdef _WIN32
	SharedMemoryKeepAliveService::Start();
#endif

	printf("numar fisiere : %lu\n",numar_fisiere);

	//scriu la inceputul zonei de memorie un integer care da numarul de fisere incarcate in memorie si implicit numarul de structuri cu informatii despre aceste fisiere, care ii urmeaza
	*(unsigned long*)mem = numar_fisiere;
	//scriu la inceputul memoriei partajate pentru informatii acc numarul de structuri acc
	*(unsigned long*)mem_acc = nr_inregistrari_acc_total;

//	printf("numar fisiere : %lu\n",*(unsigned long*)mem);

	//ma pozitionez unde vor fi scrise informatiile despre primul fisier
	fisier_date = (struct FISIER_DATE *)(mem+sizeof(unsigned long));
	//offsetul de la care incep datele incarcate in memoria partajata
	offset_curent = sizeof(unsigned long) + numar_fisiere * MARIME_STRUCTURA;
	//pointer catre pozitia din memoria partajata de la care incept datele
	date = mem + offset_curent;

	//ma pozitionez la locatia din memorie unde va fi scrisa prima structura cu informatii acc
	informatii_pozitionare = (struct INFORMATII_POZITIONARE*)(mem_acc+sizeof(unsigned long));
	//offsetul la care scriu la un moment-dat urmatoarea structura care trebuie scrisa
	offset_curent_acc = sizeof(unsigned long);

	printf("-----------------------------------------\n");

	//resetare pointer de citire la inceput
	rewind(fisier_intrare);
	//iau din nou toate fisierele date - de data asta scriu datele in memorie
	while (fgets(linie,2*MAX_NUME_FISIER+64,fisier_intrare) != NULL)
	{
		if (linie[0] == '#')
			continue;

		int file_des;
		char *fara_cale;
		char *temp;
		linie[strlen(linie)-1] = 0;
		sscanf(linie,"%s %s %lu %s %s",nume_fisier_date,nume_fisier_acc,&nr_inregistrari_acc,titlu_fisier,tip_fisier);
		printf("@info: Loading \"%s\"\n",titlu_fisier);
		printf("incarc : %s %s %lu %s %s\n",nume_fisier_date,nume_fisier_acc,nr_inregistrari_acc,titlu_fisier,tip_fisier);
		file_des = open(nume_fisier_date,O_RDONLY|O_BINARY);
		if (file_des == -1 )
		{
			fprintf(stderr,"Eroare la deschiderea fisierului '%s' \n",nume_fisier_date);
			fprintf(stdout,"@error: Error opening regular file %s\n", nume_fisier_date);
			fflush(stdout);
			return -1;
		}
		fara_cale = nume_fisier_date;
		//ma intereseaza marimea numelor lor si marimea de pe disk
		temp = strrchr (nume_fisier_date, '/');
		//daca exista un caracter / in nume, obtin partea de la el incolo, pentru a retine doar numele fisierului, fara cale
		if ( temp != NULL )
			fara_cale = temp+1;

		#ifdef _WIN32
			//ma pozitionez la sfarsit pentru a afla marimea
			tempSize = lseek(file_des, 0L, SEEK_END);
			//ma pozitionez din nou la inceput pentru ca vreau sa citesc continutul
			lseek(file_des, 0L, SEEK_SET);
		#else
			if ( fstat(file_des,&informatii_fisier) == -1 )
			{
				fprintf(stderr,"Eroare la obtinerea informatiilor despre fisierul de date '%s' \n",nume_fisier_date);
				fprintf(stdout,"@error: Error getting info about file %s\n", nume_fisier_date);
				fflush(stdout);
				return -1;
			}
			tempSize = informatii_fisier.st_size;
		#endif

//		printf("numar fisiere : %lu\n",*(unsigned long*)mem);
		//scriu in structura care este legata cu memoria paratajata numele fisierului
		strcpy(fisier_date->nume_fisier,fara_cale);
		//scriu in structura care este legata cu memoria paratajata calea fisierului
		strcpy(fisier_date->cale_fisier,nume_fisier_date);
		//scriu in structura care este legata cu memoria paratajata titlul fisierului
		strcpy(fisier_date->titlu_fisier,titlu_fisier);
		//scriu offsetul de inceput al fisierului in memoria partajata
		fisier_date->offset_start = offset_curent;
		//scriu marimea fisierului (nu include si octetul despartitor dintre fisiere)
		fisier_date->marime = tempSize;
		//scriu offsetul de la care incep informatiile acc pentru acest fisier
		fisier_date->offset_start_acc = offset_curent_acc;
		//scriu numarul de structuri acc pentru acest fisier
		fisier_date->acc_count = nr_inregistrari_acc;
		//scriu tipul fisierului (transposon sau genom)
		if (tip_fisier[0] == 'T')
			fisier_date->isTransposon = 1;
		else
			fisier_date->isTransposon = 0;
		//scriu in memorie continutul fisierului de pe disc
		if (read(file_des,date,tempSize) == -1)
		{
			fprintf(stderr,"Eroare la citirea datelor din fisierul '%s' \n",nume_fisier_date);
			fprintf(stdout,"@error: Error reading file %s\n", nume_fisier_date);
			fflush(stdout);
			return -1;
		}
//		printf("numar fisiere : %lu\n",*(unsigned long*)mem);

		//pozitionez pointerul pentru a scrie informatiile despre urmatorul fisier
		fisier_date+=1;
		//calculez offsetul din memoria partajata de unde va incepe urmatorul fisier
		printf("    offset date inainte = %lu\n",offset_curent);
		//+1 pentru ca luam in considerare si octetul ce desparte datele din 2 fisiere
		offset_curent += tempSize+1;
		printf("    offset date dupa = %lu\n",offset_curent);
		//adaug zero-ul cu care se termina datele din fiecare fisier
		*(date+tempSize) = 0;
		//pozitionez pointerul de scriere in memoria partajata la pozitia de la care va incepe urmatorul fisier
		date += tempSize + 1;
		close(file_des);
//		printf("numar fisiere : %lu\n",*(unsigned long*)mem);
		//deschid fisierul cu informatii acc corespunzator fisierului cu date procesat curent
		fisier_intrare_acc = fopen(nume_fisier_acc,"r");
		if ( fisier_intrare_acc == NULL )
		{
			fprintf(stderr,"Eroare la deschiderea fisierului cu date acc '%s' \n",nume_fisier_acc);
			fprintf(stdout,"@error: Error opening adnotation file %s\n", nume_fisier_acc);
			fflush(stdout);
			return -1;
		}
		//salvez informatiile acc
		printf("    offset acc inainte = %lu\n",offset_curent_acc);
		int contor = 0;
		struct INFORMATII_POZITIONARE* start_informatii_pozitionare =  informatii_pozitionare;
		while (fgets(linie,2*MAX_NUME_FISIER+64,fisier_intrare_acc) != NULL)
		{
			//le citesc direct in memorie
			char dummyChar;
			sscanf(linie,"%s ; %s ; %lu ; %lu ; %c ; %s",informatii_pozitionare->nume , informatii_pozitionare->cod, &informatii_pozitionare->offset_start, 
						&informatii_pozitionare->offset_stop, &dummyChar, informatii_pozitionare->cyto);
			informatii_pozitionare->direction = dummyChar;
			
			//Informatiile despre acoperire sunt lasate necompletate momentan
			informatii_pozitionare->shadowGene = -1;
			informatii_pozitionare->shadowPosition = -1;
			
			//trec la urmatoarea structura
			informatii_pozitionare += 1;
			contor ++;
			
			//adun la offsetul curent marimea structurii scrise
			offset_curent_acc += MARIME_STRUCTURA_ACC;
		}
		
		initializeShadowGenes(start_informatii_pozitionare, contor);
		
		printf("    offset acc dupa = %lu\n",offset_curent_acc);
		fclose(fisier_intrare_acc);
//		printf("numar fisiere : %lu\n",*(unsigned long*)mem);

		progress = ((contor_fisiere+1)* 100) / numar_fisiere;
		printf("@progress: %lu \n",progress);
		contor_fisiere ++;

	}

	fclose(fisier_intrare);

	//DEBUG ONLY
	//test_continut();

	printf("serverok\n");
	#ifdef _WIN32
		fflush(stdout);
	#endif

	//Sleep ca sa pot da comanda pmap `pidof server`;
	//sleep(60);
	

	release(marime_totala,marime_totala_acc);
	return 0;
}
