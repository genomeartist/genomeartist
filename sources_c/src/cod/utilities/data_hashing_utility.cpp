#include <stdio.h>
#include <sys/types.h>
#include <sys/mman.h>
#include <mqueue.h>
#include <string.h>
#include <stdlib.h>
#include <errno.h>
#include <unistd.h>
#include <sys/stat.h>

#include "../../headere/utils.h"
#include "../../headere/comun.h"

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

#define MAX_NUME_FISIER 256
//1 MB
#define BUFFER_SIZE 1048576
#define MASCA_STRING "11111111111111111111111111111111"
#define DIMENSIUNE_MASCA_STRING 32
//secventele ce depasesc PLAFON_NR_APARITII aparitii sunt ignorate
#define PLAFON_NR_APARITII 1000

#define DEBUG 0

int convert(char x)
{
	switch (x){
		case 'A': case 'a': return BINARY_A;
		case 'C': case 'c': return BINARY_C;
		case 'G': case 'g': return BINARY_G;
		case 'T': case 't': return BINARY_T;
		default :
			//fprintf(stdout,"Caracter nepermis gasit in fisier %c\n",x);
			return BINARY_A;
	}
}

void creazaHashFisier(int file_des, int marime_fereastra, STRUCTURA_HASH *schelet_hash, unsigned int masca_anulare_biti_superiori)
{
	unsigned long octeti_cititi;
	char buffer_citire[BUFFER_SIZE];
	bool prima_data = true;
	unsigned int hash_curent = 0;
	unsigned long index_curent = 0;

	unsigned long marime_octeti_totala_fisier = 0;
	unsigned long marime_octeti_procent_increment = 0;
	unsigned long offset_octeti_curent = 0;
	unsigned long offset_octeti_pt_procent_urmator = 0;
	unsigned long procent_curent = 0;
	unsigned long increment_procent = 2;
	unsigned long valoare_procent_start = 0;
	unsigned long valoare_procent_stop = 50;

	marime_octeti_totala_fisier = lseek(file_des, 0, SEEK_END);
	lseek(file_des, 0, SEEK_SET);
	marime_octeti_procent_increment = marime_octeti_totala_fisier / ((valoare_procent_stop-valoare_procent_start) / increment_procent);
	offset_octeti_pt_procent_urmator = marime_octeti_procent_increment;

	//ATENTIE : nu tratez cazul in care in fisier se gasesc mai putine caractere decat lungimea unei ferestre
	//cat timp mai e ceva de citit (in caz ca nu mai sunt octeti de citit returneaza 0) si nu a aparut o eroare (in caza de eroare returneaza -1)
	while ( (octeti_cititi = read(file_des, buffer_citire, BUFFER_SIZE)) > 0 )
	{
		unsigned int i = 0;
		if (DEBUG) printf("--------------------------------------------------\n");
		if (DEBUG) printf("octeti_cititi = %ld\n", octeti_cititi);
		if (DEBUG) printf("prima_data = %d\n", prima_data);
		if (prima_data == true)
		{
			prima_data = false;
			//daca nu avem un numar de caractere macar egal cu marimea ferestrei, nu avem ce procesa
			if (octeti_cititi < (unsigned long)marime_fereastra)
				break;

			//procesam informatia din vectorul citit
			for (; i<(unsigned)marime_fereastra; i++)
			{
				//mutam toti bitii la stanga cu 2 pozitii
				hash_curent = hash_curent<<2;
				//adunam la hash valoarea corespunzatoare nucleotidei curente (fiecare nucleotida este reprezentata pe 2 biti)
				hash_curent += convert(buffer_citire[i]);
				index_curent++;
			}

			schelet_hash[hash_curent].numar_aparitii++;
			schelet_hash[hash_curent].vector_pozitii = (unsigned long*)realloc(schelet_hash[hash_curent].vector_pozitii, schelet_hash[hash_curent].numar_aparitii * sizeof(unsigned long));
			//la prima citire, primul hash creat incepe in fisier de pe pozitia 0
			schelet_hash[hash_curent].vector_pozitii[schelet_hash[hash_curent].numar_aparitii-1] = 0;
		}

		for (; i<octeti_cititi; i++)
		{
			//se muta toti bitii cu 2 pozitii la stanga
			hash_curent = hash_curent << 2;
			//se aduna valoarea care urmeaza acum la final
			hash_curent += convert(buffer_citire[i]);
			//  se pun pe 0 bitii de pe pozitiile (marime_fereastra*2) si (marime_fereastra*2)+1 pe 0 pentru a elimina bitii corespunzatori
			//caracterului care a fost eliminat din fereastra o data cu avansarea
			//  daca marimea ferestrei este 16, oricum bitii superiori dispar la shiftarea la stanga
			if (marime_fereastra < 16)
				hash_curent = hash_curent & masca_anulare_biti_superiori;

			schelet_hash[hash_curent].numar_aparitii++;
			schelet_hash[hash_curent].vector_pozitii = (unsigned long*)realloc(schelet_hash[hash_curent].vector_pozitii, schelet_hash[hash_curent].numar_aparitii * sizeof(unsigned long));
			//hash-ul creat incepe de pe pozitia ( index_curent - (marime_fereastra-1) )
			schelet_hash[hash_curent].vector_pozitii[schelet_hash[hash_curent].numar_aparitii-1] = index_curent - (marime_fereastra-1);

			index_curent++;
		}

		offset_octeti_curent += octeti_cititi;
		if (offset_octeti_curent >= offset_octeti_pt_procent_urmator )
		{
			procent_curent += increment_procent;
			offset_octeti_pt_procent_urmator += marime_octeti_procent_increment;
			printf("@progress:%ld\n",procent_curent);
			fflush(stdout);
		}

		if (DEBUG) getchar();
	}
}

void scrieHash(int fisier_iesire, STRUCTURA_HASH *schelet_hash, unsigned int dimensiune_schelet_hash)
{
	//primul uint din fisier va fi dimensiunea scheletului de hash, pentru a stii cat sa citesc cand deschid fisierul alta data
	write(fisier_iesire, &dimensiune_schelet_hash, sizeof(unsigned int));
	unsigned int offset_curent = (1+dimensiune_schelet_hash)*sizeof(unsigned int);
	unsigned int offset_ZERO = 0;

	unsigned int procent_start = 50;
	unsigned int procent_sfarsit = 60;
	unsigned int procent_curent = procent_start;
	unsigned int increment_procent = 2;
	unsigned int dimensiune_procent = dimensiune_schelet_hash / ((procent_sfarsit-procent_start) / increment_procent);
	unsigned int dimensiune_procent_urmator = dimensiune_procent;

	//setand numarul de aparitii peste plafon, nu se va tine cont de aparitiile secventei formata numai din A
	//(nucleotida A este codata cu 0, deci o succesiune de A, va avea codul 0)
	schelet_hash[0].numar_aparitii = PLAFON_NR_APARITII + 10;
	for (unsigned int i=0; i<dimensiune_schelet_hash; i++)
	{
		//  daca pentru hash-ul curent exista cel putin 1 aparitie, atunci se asociaza pozitia datelor cu offsetul curent
		//si se calculeaza offsetul pentru urmatorul se de date
		//  daca sunt mai mult  de PLAFON_NR_APARITII aparitii, secventa este ignorata
		if (schelet_hash[i].numar_aparitii > 0 && schelet_hash[i].numar_aparitii <= PLAFON_NR_APARITII)
		{
			write(fisier_iesire, &offset_curent, sizeof(unsigned int));
			//  offsetul pentru urmatorul set de date este calculat adunand uint-ul pentru marimea setului curent de date
			//si marimea setului de date curent
			offset_curent += 1 * sizeof(unsigned int) + schelet_hash[i].numar_aparitii * sizeof(unsigned long);
		}
		//daca pentru hashul curent nu exista nici o aparitie, atunci ii este asociat offsetul 0 pentru a indica acest lucru
		else
		{
			write(fisier_iesire, &offset_ZERO, sizeof(unsigned int));
			//printf("Pentru hash-ul %d nu sunt aparitii\n",i);
		}

		if (i > dimensiune_procent_urmator)
		{
			procent_curent += increment_procent;
			dimensiune_procent_urmator += dimensiune_procent;
			printf("@progress:%d\n",procent_curent);
			fflush(stdout);
		}
	}

	procent_start = 60;
	procent_sfarsit = 100;
	procent_curent = procent_start;
	increment_procent = 2;
	dimensiune_procent = dimensiune_schelet_hash / ((procent_sfarsit-procent_start) / increment_procent);
	dimensiune_procent_urmator = dimensiune_procent;

	//parcurg scheletul, scriu datele si in acelasi timp eliberez memoria
	//  offseturile la care se scriu datele vor corespunde cu valorile calculate anterior;
	//au fost scrise date in dimesiune egala cu (1+dimensiune_schelet_hash)*sizeof(unsigned int)
	//si fisierul se afla acum exact in pozitia in care se va scrie primul set de date
	for (unsigned int i=0; i<dimensiune_schelet_hash; i++)
	{
		//se scriu date doar daca pentru hash-ul curent sunt aparitii
		//daca sunt mai mult  de PLAFON_NR_APARITII aparitii, secventa este ignorata
		if (schelet_hash[i].numar_aparitii > 0 && schelet_hash[i].numar_aparitii <= PLAFON_NR_APARITII)
		{
			//se scrie mai intai numarul de aparitii pentru secventa corespunzatoare hash-ului
			write(fisier_iesire, &(schelet_hash[i].numar_aparitii), sizeof(unsigned int));
			//si dupaia pozitiile gasite
			write(fisier_iesire, schelet_hash[i].vector_pozitii, schelet_hash[i].numar_aparitii * sizeof(unsigned long));
		}

		if (schelet_hash[i].vector_pozitii != NULL)
			free(schelet_hash[i].vector_pozitii);

		if (i > dimensiune_procent_urmator)
		{
			procent_curent += increment_procent;
			dimensiune_procent_urmator += dimensiune_procent;
			printf("@progress:%d\n",procent_curent);
			fflush(stdout);
		}
	}

	//pentru a ne asigura ca ajunge la sfarsit
	printf("@progress:%d\n",100);
	fflush(stdout);
}

void proceseazaFisier(char *nume_fisier_date, unsigned int dimensiune_schelet_hash, int marime_fereastra, unsigned int masca_anulare_biti_superiori)
{
	STRUCTURA_HASH *schelet_hash;
	int file_des;
	int fisier_iesire;

	//alocam scheletul hash-ului
	schelet_hash = (STRUCTURA_HASH*)calloc(dimensiune_schelet_hash, sizeof(STRUCTURA_HASH));
	if (schelet_hash == NULL)
	{
		fprintf(stdout,"Scheletul hash-ului nu poate fi alocat\n");
		exit(-1);
	}

	file_des = open(nume_fisier_date,O_RDONLY);
	if (file_des == -1 )
	{
		fprintf(stdout,"Eroare la deschiderea fisierului de date '%s' \n",nume_fisier_date);
		exit(-1);
	}

	printf("Incep crearea hash-ului pentru fisierul = %s\n",nume_fisier_date);
	//printf("@info:Processing file \"%s\"\n",nume_fisier_date);
	printf("@progress:%d\n",0);
	fflush(stdout);
	creazaHashFisier(file_des, marime_fereastra, schelet_hash, masca_anulare_biti_superiori);
	close(file_des);
	
	char *nume_iesire = nume_fisier_hash(nume_fisier_date);
	fisier_iesire = open(nume_iesire, O_CREAT | O_TRUNC | O_RDWR, 0666);
	if (fisier_iesire == -1)
	{
		fprintf(stdout,"Eroare la deschiderea fisierului de iesire cu numele %s, pentru stocarea hash-ului\n", nume_iesire);
		fprintf(stderr,"Error operning file \"%s\"\n", nume_iesire);
		exit(-1);
	}
	printf("Incep scrierea in fisierul = %s\n",nume_iesire);

	scrieHash(fisier_iesire, schelet_hash, dimensiune_schelet_hash);

	close(fisier_iesire);
	free(schelet_hash);
}

int main(int argc, char **args)
{
	int marime_fereastra;
	FILE* fisier_intrare;
	char linie[2*MAX_NUME_FISIER+64];
	char nume_fisier_date[MAX_NUME_FISIER];
	char nume_fisier_acc[MAX_NUME_FISIER];
	unsigned long nr_inregistrari_acc = 0;

	unsigned int masca_anulare_biti_superiori = 0;
	unsigned int dimensiune_schelet_hash = 0;
	char masca_string[DIMENSIUNE_MASCA_STRING];

	if (argc < 4)
	{
		fprintf(stdout,"Nu s-au dat toti parametrii\n");
		fprintf(stdout,"folosire : \n");
		fprintf(stdout,"data_hashing_utility [nume_fisier_intrare] [marime_fereastra] [bulk/single]\n");
		return -1;
	}

	marime_fereastra = atoi(args[2]);
	if (marime_fereastra < 1 || marime_fereastra > 16)
	{
		fprintf(stdout,"marimea ferestrei trebuie sa fie intre 1 si 16 baze\n");
		return -1;
	}

	//dimensiunea scheletului hash-ului este egala cu 2^(marime_fereastra*2)
	//marimea ferestrei este inmultita cu 2 pt ca fiecare caracter din fereastra se codifica cu 2 biti
	dimensiune_schelet_hash = 1;
	dimensiune_schelet_hash = dimensiune_schelet_hash << (marime_fereastra*2);

	strncpy(masca_string, MASCA_STRING, DIMENSIUNE_MASCA_STRING);
	//marimea maxima a ferestrei este de 16, pentru care ar trebui o masca de 32 de biti;
	//dar in cazul acesta nu mai este nevoie de aplicarea mastii, pentru ca ultimii biti oricum se pierd la shiftare
	if (marime_fereastra < 16 )
	{
		masca_string[DIMENSIUNE_MASCA_STRING - (marime_fereastra*2) -1] = '0';
		masca_string[DIMENSIUNE_MASCA_STRING - (marime_fereastra*2) -2] = '0';
		masca_anulare_biti_superiori = bin2dec(masca_string);
	}

	if (DEBUG) printf("marime_fereastra = %d\n", marime_fereastra);
	if (DEBUG) printf("masca_string = %s\n", masca_string);
	if (DEBUG) printf("masca_anulare_biti_superiori = %u\n", masca_anulare_biti_superiori);
	if (DEBUG) printf("dimensiune_schelet_hash = %u\n", dimensiune_schelet_hash);

	//daca avem un fisier cu informatii despre mai multe fisiere pentru care este necesara hash-uirea
	if (strcmp(args[3], "bulk") == 0)
	{
		//trebuie sa am un fisier de intrare care poate fi deschis
		fisier_intrare = fopen(args[1], "r");
		if (fisier_intrare == NULL )
		{
			fprintf(stdout,"Fisierul de intrare %s nu poate fi deschis\n",args[1]);
			return -1;
		}
	
		//iau toate fisierele date
		while (fgets(linie,2*MAX_NUME_FISIER+64,fisier_intrare) != NULL)
		{
			//sterg ultimul caracter
			linie[strlen(linie)-1] = 0;
			//citesc informatiile din linia curenta
			sscanf(linie,"%s %s %lu",nume_fisier_date,nume_fisier_acc,&nr_inregistrari_acc);
			printf("linie citita : %s %s %lu\n",nume_fisier_date,nume_fisier_acc,nr_inregistrari_acc);
	
			proceseazaFisier(nume_fisier_date, dimensiune_schelet_hash, marime_fereastra, masca_anulare_biti_superiori);
		}
	
		fclose(fisier_intrare);
	}
	else
	{
		proceseazaFisier(args[1], dimensiune_schelet_hash, marime_fereastra, masca_anulare_biti_superiori);
	}
}

