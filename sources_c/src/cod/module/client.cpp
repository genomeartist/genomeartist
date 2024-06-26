#include <stdio.h>
#include <time.h>

#include <stdlib.h>
#include <string.h>
#include <errno.h>

#ifdef _WIN32
#include <fcntl.h>
#include <io.h>
#include <stdio.h>
#include <windows.h>

#else
#define O_BINARY 0
#endif


#include "../../headere/comun.h"
#include "../../headere/utils.h"

#include "../../headere/m1_hash_retrieval.h"
#include "../../headere/m2_reuniune.h"
#include "../../headere/m3_expand.h"
#include "../../headere/m3_stitch.h"
#include "../../headere/m4_pick.h"
#include "../../headere/m4_smith-waterman.h"
#include "../../headere/m5_compunere_rezultate.h"
#include "../../headere/configuration.h"
#include "../../headere/client.h"

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

#define FORWARD 1
#define REVERSE 2

//Pointer to the shared memory
#ifdef _WIN32
HANDLE mem_h;
HANDLE ext_mem_h;
#else
int mem_h;
int ext_mem_h;
#endif

char *mem;
char *ext_mem;

//Size of the shared memory
long long filesize;
long long ext_filesize;

//Tipul de output
int outputType;

//Structura din memorie
unsigned long filesno;		//Nr de fisiere in memorie
struct FISIER_DATE* fisiere;	//Structura cu date

//Ext memory
unsigned long ext_filesno;		//Nr de fisiere in memorie
struct INFORMATII_POZITIONARE* ext_fisiere;	//Structura cu date

//Variabile pentru afisare progress
int progress_start;
int progress_offset;
int progress;
int progress_step;	//Pasul principal
int progress_step_forward; //Pasul pentru catena sens
int progress_step_reverse; //Pasul pentru complementara

//````````````````````````````````````````````````
//    Functii ajutatoare. Utils.
//................................................

/**
 * Reverses a string
 */
char* my_strrev(char *s)
{
       int i=0;
       int n = strlen(s);
       while (i<n/2)
       {
               *(s+n) = *(s+i);       //uses the null character as the temporary storage.
               *(s+i) = *(s + n - i -1);
               *(s+n-i-1) = *(s+n);
               i++;
       }
       *(s+n) = '\0';

       return s;
}

//````````````````````````````````````````````````
//    Functie pentru managementul memoriei partajate
//................................................

/**
 * Link with the shared memory
 */
void link_memory()
{
#ifdef _WIN32
	mem_h = OpenFileMapping(FILE_MAP_READ,false, NUME_MEMORIE_PARTAJATA);
	if (mem_h == NULL)
	{
		fprintf(stderr, "Eroare la accesarea memoriei partajate\n");
		exit(-1);
	}
	mem = (char*)MapViewOfFile(mem_h, FILE_MAP_READ, 0, 0, 0);
	DWORD lastError = GetLastError();
	if (mem == NULL)
	{
		fprintf(stderr, "Eroare la maparea memoriei\n");
		CloseHandle(mem_h);
		exit(-1);
	}
#else
	struct stat buf;
	int  rc;

	//Obtain the file descriptor
	if ((mem_h = shm_open(NUME_MEMORIE_PARTAJATA, O_RDONLY, 0666)) == -1)
	{
		perror("client : ");
		fprintf(stderr, "Eroare la deschiderea FileMapping\n");
		exit(-1);
	}

	//Find the size of the shared memory
	rc = fstat(mem_h, &buf);
	if (rc == -1)
	{
		perror("client : ");
		fprintf(stderr, "Eroare la verificare marime\n");
		exit(-1);
	}
	else {
		filesize = buf.st_size;
	}

	//Load it into process memory
	if ((mem = (char*)mmap(0, filesize, PROT_READ, MAP_SHARED, mem_h, 0)) == NULL)
	{
		fprintf(stderr, "Eroare la maparea memoriei\n");
		close(mem_h);
		exit(-1);
	}
#endif
}

/**
 * Link with the extended
 */
void link_ext_memory()
{
#ifdef _WIN32
	ext_mem_h = OpenFileMapping(FILE_MAP_READ, false, NUME_MEMORIE_PARTAJATA_POZITIONARE);
	if (ext_mem_h == NULL)
	{
		fprintf(stderr, "Eroare la accesarea memoriei partajate\n");
		exit(-1);
	}
	ext_mem = (char*)MapViewOfFile(ext_mem_h, FILE_MAP_READ, 0, 0, 0);
	DWORD lastError = GetLastError();
	if (ext_mem == NULL)
	{
		fprintf(stderr, "Eroare la maparea memoriei\n");
		CloseHandle(ext_mem_h);
		exit(-1);
	}
#else
	struct stat buf;
	int  rc;


	//Optain the file descriptor
	if ( (ext_mem_h = shm_open(NUME_MEMORIE_PARTAJATA_POZITIONARE, O_RDONLY , 0666)) == -1 )
	{
		perror("client : ");
		fprintf(stderr,"Eroare la deschiderea FileMapping\n");
		exit (-1);
	}

	//Find the size of the shared memory
	rc = fstat( ext_mem_h , &buf );
	if( rc == -1 )
	{
		perror("client : ");
		fprintf(stderr,"Eroare la verificare marime\n");
		exit (-1);
	} else {
		ext_filesize = buf.st_size;
	}

	//Load it into process memory
	if ( (ext_mem = (char*) mmap(0, ext_filesize, PROT_READ , MAP_SHARED, ext_mem_h, 0)) == NULL )
	{
		fprintf(stderr,"Eroare la maparea memoriei\n");
		close(ext_mem_h);
		exit (-1);
	}
#endif
}

/**
 * Release the shared memory
 */
void release_memory()
{
#ifdef _WIN32
	UnmapViewOfFile(mem);
	CloseHandle(mem_h);
#else
	munmap(mem,filesize);
	close(mem_h);
#endif
	
}

/**
 * Release the shared memory
 */
void release_ext_memory()
{
#ifdef _WIN32
	UnmapViewOfFile(ext_mem);
	CloseHandle(ext_mem_h);
#else
	munmap(ext_mem,ext_filesize);
	close(ext_mem_h);
#endif
	
}


//````````````````````````````````````````````````
//    Functie pentru calculul catenei inverse
//................................................

/**
  Compute the reverse complementary from a specified string
*/
char* revncomp(char* forward_pattern,int start,int stop)
{
      char* reverse_pattern;
      int len,i;
      char c;

      len=stop-start;
      reverse_pattern = (char*)malloc(len+1);
      strncpy(reverse_pattern, forward_pattern+start,len);
      reverse_pattern[len] = '\0';
      my_strrev(reverse_pattern);

      //Compute the reverse pattern
      for (i=0;i<len;i++)
      {
	  c = reverse_pattern[i];
	  reverse_pattern[i] = nucl2compl(c);
      }

      return reverse_pattern;
}

/**
  Compute the reverse complementary
*/
char* revcomp(char* forward_pattern)
{
    int k = strlen(forward_pattern);
    return revncomp(forward_pattern,0,k);
}

//````````````````````````````````````````````````
//    Functie pentru realizarea prelucrarilor
//................................................
VECTOR_ALINIERE* searchFile(int hashDescriptor,					//Descripturul fisierului cu hash-uri
		struct EXP_RESULT_MATRIX* expansion_table,      	//Tabela de expansiune
		char* forward_query,int query_size,			//Date despre query
		char* pointerGenom,unsigned long dimensiuneGenom)	//Pointer catre genom
{
	//Variabile pentru prelucrare
	int contor;
	MAPPING_INTERVAL* minterval; //Vectorul mapat pe query
	VECTOR_INTERVAL* vinterval; //Vectorul de intervale
		INTERVAL_LLIST lista_intervale; //lista de intervale din interiorul vectorului de intervale
	VECTOR_EXPANDED* vexpanded; //Vectorul de intervale extinse
		EXPANDED_LLIST lista_expanded; //lista de intervale din interiorul vectorului de extinse
	MAPPING_EXPANDED* mexpanded; //Maparea intervalelor extinse peste query
	VECTOR_EXPANDED* vpicked;
	VECTOR_ALINIERE* valiniere; //Vectorul de intervale aliniate cu Smith-Waterman
	struct INTERVAL* interval;   //Un interval singular
	struct EXPAND_RESULT* expanded_result; //Un rezultat extins singular
	
	//Variabile pentru progress
	double progressClone = progress;
	double progressSecondaryStep = progress_step_forward/5; // 5 - numarul de operatii care se fac

	//Parametrii de cautare
	int length_modifier = atoi(configurationGetString((char*)"EXPANSION_LENGTH_MODIFIER"));
	int picking_depth = atoi(configurationGetString((char*)"PICKING_SORT_DEPTH"));
  
	//Cu fisierul deschis initializez vectorii de intervale
	minterval = minterval_initialize(query_size);
	vinterval = vinterval_initialize();
	
	//NOTE ETAPA 1-2 - procesez query-ul si obtin intervalele reuntie
	proceseazaQuery(hashDescriptor,forward_query,query_size,DIMENSIUNE_FEREASTRA,minterval,vinterval);
	
		//Afisez de test
		//minterval_print(minterval);
		//printf("===========================\n");
		//vinterval_print(vinterval);

	//#Afisez progress
	progressClone += progressSecondaryStep;
	printf("@progress: %d\n",(int)progressClone);
	fflush(stdout);

	//NOTE ETAPA 3 - Extind fiecare interval gasit
	//Vectorul V2 contine lista cu intervalele gasite
	//  iterez prin ele si le trimit la extindere
	//  va rezulta Vectorul V3 de intervale extinse
	vexpanded = vexpanded_initialize();
	lista_intervale = *(vinterval->elements);
	contor = 0;
	while (lista_intervale != NULL)
	{
		//Obtin intervalul din lista
		interval = lista_intervale->data;
		expanded_result =  expandStrings(expansion_table,length_modifier,
			    forward_query,interval->pozitieQuery,interval->length,query_size,
			    pointerGenom,interval->pozitieGenom,interval->length,dimensiuneGenom);
		vexpanded_addExpanded(expanded_result,vexpanded);
		lista_intervale = lista_intervale->next;
		contor++;
	}

		//Afisez de test
		//printf("size = %d\n",vexpanded->size);
		//vexpanded_print(vexpanded);
		
	//Nu mai am nevoie de structurile de lucru cu intervale primare, lucrez cu intervale extinse
	minterval_free(minterval);
	vinterval_free(vinterval);

	//#Afisez progress
	progressClone += progressSecondaryStep;
	printf("@progress: %d\n",(int)progressClone);
	fflush(stdout);
	
	//NOTE ETAPA 4 - Intervalele extinse sunt reunite si remapate pe query
	mexpanded = mexpanded_initialize(query_size);
	stitchTheExpanded(vexpanded, mexpanded);
	
	    //Afisez de test
	    //mexpanded_print(mexpanded);
		//vexpanded_print(vexpanded);
	
	//#Afisez progress
	progressClone += progressSecondaryStep;
	printf("@progress: %d\n",(int)progressClone);
	fflush(stdout);

	//NOTE Etapa 5 - Aleg cele mai bune intervale, char *queryInversComplementat si le sorted
	vpicked = vexpanded_initialize();
	pickBestMatches(vpicked, mexpanded, picking_depth);
	
	    //Afisez de test
	    //vexpanded_print(vpicked);

	//#Afisez progress
	progressClone += progressSecondaryStep;
	printf("@progress: %d\n",(int)progressClone);
	fflush(stdout);
	  
	//Se trimite fiecare element din vectorul V5 la modul4, Smith-Waterman, 
	// Va rezulta un vector de intervale aliniate. (VECTOR6)
	//NOTE Etapa 6 Prelucrez cu smith-waterman
	valiniere = valiniere_initialize(vpicked->size);
	lista_expanded = *(vpicked->elements);
	contor = 0;
	while (lista_expanded != NULL)
	{
		expanded_result = lista_expanded->data;
		valiniere->elements[contor] = sw_align(expanded_result->str1,expanded_result->offset1,expanded_result->length1, 
					    expanded_result->str2,expanded_result->offset2,expanded_result->length2);
		lista_expanded = lista_expanded->next;
		contor++;
	}
	
		//Afisez de test
		//printf("Size = %d\n",valiniere->size);
		//valiniere_print(valiniere);
	
	//#Afisez progress
	progressClone += progressSecondaryStep;
	printf("@progress: %d\n",(int)progressClone);
	fflush(stdout);

	//Eliberez memoria asociata vectorilor de intervale
	mexpanded_free(mexpanded);
	vexpanded_free_no_data(vpicked);
	vexpanded_free(vexpanded);	//Decat aici se eleibereaza datele
	
	//Returnez structura cu aliniere
	return valiniere;
}


//````````````````````````````````````````````````
//   Functia principala. Logica aplicatiei
//................................................

int main(int argc,char *argv[])
{
	char* forward_query;		//Query-ul pentru cautarea directa
	char* reverse_query;		//Query-ul pentru cautarea inversa
	int query_size;			//Dimensiunea query-ului
	struct EXP_RESULT_MATRIX* expansion_table; //Expansion table-ul pentru extindere

	if (argc < 5)
	{
		fprintf(stderr,"Sintaxa este ./client expresie locatie_expansion query_name fisier_iesire\n");
		exit(-1);
	}

	time_t timpStart = time(NULL);

	//Citesc parametrii de configuratie
	configurationInit();
	configurationReadFromFile();
	if (!configurationIsValid())
	{
		fprintf(stderr,"Valorile parametriilor nu sunt corecte\n");
		fprintf(stdout,"@error: Incorrect value found in search parameters\n");
		fflush(stdout);
		return -1;
	}


	//Link with the shared memory
	link_memory();
	link_ext_memory();

	//Compute the search patterns
	query_size = strlen(argv[1]);
	forward_query = acgt2upper(argv[1],query_size);
	reverse_query = revncomp(forward_query,0,query_size);

	printf("forward: %s\n",forward_query);
	printf("reverse: %s\n",reverse_query);

	//Sleep ca sa pot da comanda pmap `pidof client`;
	//sleep(60);

	//Read the index from the memory
	filesno = *(unsigned long*)mem;
	//printf("filesno: %lu\n",filesno);
	fisiere = ((struct FISIER_DATE*)(mem+sizeof(unsigned long)));

	//Read the index from the memory
	ext_filesno = *(unsigned long*)ext_mem;
	//printf("filesno: %lu\n",filesno);
	ext_fisiere = ((struct INFORMATII_POZITIONARE*)(ext_mem+sizeof(unsigned long)));

	//TODO mutat expansion_table-ul in memoria partajata
    	expansion_table = readEXPResultFromFile(argv[2]);
	
	//===========================================
	//    Implementarea algoritmului de cautare
	//===========================================
	
	//Iau fiecare fisier din structura FISIER_DATE si ii deschid fisierul de hash
	int i;
	char* cale_fisier;
	char* pointerGenom;
	unsigned long dimensiuneGenom;
	int hashDescriptor;   //File descriptorul

	//Variabile pentru afisare progress
	progress_start = 1;
	progress_offset = 5;
	progress = progress_start;
	progress_step = (100-progress_offset-progress_start) / filesno;
	progress_step_forward = progress_step/2;
	progress_step_reverse = progress_step-progress_step_forward;

	//Variabile pentru rezultate
	VECTOR_REZULTATE_PARTIALE* vrez_forward; //Vector de rezultate pentru fisiere -> direct
	VECTOR_REZULTATE_PARTIALE* vrez_reverse; //Vector de rezultate pentru fisiere -> reverse
		VECTOR_ALINIERE* valiniere;	//vector aliniere auxiliar (componenta rez partiale)
	
	//Initializez vectorii de rezultate partiale
	vrez_forward = vrezpartiale_initialize(filesno);
	vrez_reverse = vrezpartiale_initialize(filesno);
	
	//Caut in fiecare fisier din baza de date
	for (i=0;i< (int)filesno;i++) {	//Caut in tot
		//if (i!=2 && i!=6 && i!=7) decat 3L, X, P{LacW}
		//	continue;
	
		//Obtin numele fisierului	
		cale_fisier = nume_fisier_hash(fisiere[i].cale_fisier);
		printf("@info: Searching \"%s\"\n",fisiere[i].titlu_fisier);
		printf("@progress: %d\n",progress);
		printf("fisier %d = %s\n",i,cale_fisier);
		fflush(stdout);
		
		//Deschid fisierul
		hashDescriptor = open(cale_fisier, O_RDONLY|O_BINARY);
		if (hashDescriptor == -1)
		{
			fprintf(stderr,"Eroare la deschiderea fisierului de intrare cu numele %s\n", cale_fisier);
			fprintf(stdout,"@error: Error opening file %s\n", cale_fisier);
			fflush(stdout);
			return -1;
		}
		
		//Obtin pointerul care genom din structura si marimea
		pointerGenom = (char*)(mem+fisiere[i].offset_start);
		dimensiuneGenom = fisiere[i].marime;
		
		//BEGIN Caut pe catena sens
		//Obtin rezultatele din respectivul fisier
		valiniere = searchFile( hashDescriptor, 			//Desciptorul pentru hash
			    expansion_table,			//Pointer catre tabela de expansiune
			    forward_query, query_size, 		//Query-ul
			    pointerGenom,dimensiuneGenom);	//Pointer catre genom
		
		      //Afisez de test
		      //printf("Size = %d\n",valiniere->size);
		      //valiniere_print(valiniere);
		
		//Adaug in vectorul de rezultat
		vrez_forward->elements[i] = valiniere;
		//END Caut pe catena sens

		//Afisez progress
		progress += progress_step_forward;
		printf("@progress: %d\n",progress);
		fflush(stdout);
	
		//BEGIN Caut pe catena complementara
		//Obtin rezultatele din respectivul fisier
		valiniere = searchFile( hashDescriptor, 			//Desciptorul pentru hash
			    expansion_table,			//Pointer catre tabela de expansiune
			    reverse_query, query_size, 		//Query-ul
			    pointerGenom,dimensiuneGenom);	//Pointer catre genom
		
		//Complementez rezultatele deoarece sunt pe invers
		valiniere_complement(valiniere, query_size);
		
		      //Afisez de test
		      //printf("Reverse Size = %d\n",valiniere->size);
		      //valiniere_print(valiniere);
		
		//Adaug in vectorul de rezultat
		vrez_reverse->elements[i] = valiniere;
		//END Caut pe catena complementara
	
		//Inchid fisierul cu Hash-ul
		free(cale_fisier);
		close(hashDescriptor);

		//Afisez progress
		progress += progress_step_reverse;
		printf("@progress: %d\n",progress);
		fflush(stdout);
	}
	
	//actiuni aferente modulului 5
	
	int lungime_minima_nucleu = atoi(configurationGetString((char*)"COMPUNERE_LUNGIME_MINIMA_NUCLEU"));
	int numar_maxim_solutii = atoi(configurationGetString((char*)"COMPUNERE_NUMAR_MAXIM_SOLUTII"));
	bool acorda_bonus = atoi(configurationGetString((char*)"COMPUNERE_GENOM_TRANSPOSON_BONUS"));
	
	printf("inainte de compunere\n");
	printf("@info: Assembling results\n");
	fflush(stdout);
	int contorAlinieriCuNucleuPreaMic = 0;
	REZULTATE_FISIER* rezultate_compunere = CompuneRezultate(vrez_forward, vrez_reverse, query_size, fisiere, forward_query, 
																lungime_minima_nucleu, numar_maxim_solutii, acorda_bonus, mem, reverse_query,
																&contorAlinieriCuNucleuPreaMic);
	printf("inainte de printare\n");
	printf("@info: Done searching\n");
	printf("@progress: %d\n",100);
	fflush(stdout);
	time_t timpStop = time(NULL);
	
	int out_fd = open(argv[4], O_CREAT|O_TRUNC|O_RDWR|O_BINARY,0666);
	char nume_query[MARIME_CONST_STRING];
	strcpy(nume_query, argv[3]); 
	scrieInFisier(out_fd, rezultate_compunere, forward_query, query_size, nume_query, 
				  vrez_forward, vrez_reverse, fisiere, ext_mem, numar_maxim_solutii, 
				  timpStop-timpStart, contorAlinieriCuNucleuPreaMic, lungime_minima_nucleu);
	close(out_fd);
	
	//PrinteazaRezultate(rezultate_compunere, forward_query, query_size, numar_maxim_solutii);
	
	printf("dupa printare\n");
	
	//eliberez memoria folosita de rezultate
	//obtinem primul rezultat obtinut
	REZULTAT_COMPUNERE *solutie_curenta = rezultate_compunere->rezultate_fisier_start;
	while (solutie_curenta != NULL)
	{	
		REZULTAT_COMPUNERE *urmatoarea_solutie = solutie_curenta->vecin_dreapta;
		elibereazaRezultat(solutie_curenta);
		solutie_curenta = urmatoarea_solutie;
	}
	free(rezultate_compunere);


	//Eliberez structurile rezultat
	vrezpartiale_free(vrez_forward);
	vrezpartiale_free(vrez_reverse);

	//TODO mutat expansion_table-ul in memoria partajata
	freeEXPResult(expansion_table);

	//Eliberez stringurile
	free(forward_query);
	free(reverse_query);

	//Eliberez memoria partajata
	release_memory();
	release_ext_memory();

	//Eliberez fisierul de configuratie
	configurationFree();
	
	//End
	fprintf(stdout,"clientok\n");

	return 0;
}
