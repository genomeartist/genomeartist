#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "../../headere/m2_reuniune.h"
#include "../../headere/m1_hash_retrieval.h"
#include "../../headere/utils.h"

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

/**
REUNIUNEA DE INTERVALE
======================
	- V2 - VECTOR_INTERVAL - Tinem o lista de intervale rezultate din reuniune
	- V1 - MAPPING_INTERVAL - Tinem un vector de lungimea query-ului care pe fiecare pozitie are o lista cu referinte
	       la intervalele rezultate din reuniuni care ajung pana acolo
	- Pargurgem secventa query si pentru fiecare pozitie luam intervalul asociat
	  cu fereastra care incepe de la acea pozitie
	- Reuniunea cu un nou interval se face astfel:
		1. Din V1 se obtin intervalele din V2 care ajung pana la pozitia de la care
		   incepe noul interval in query
		2. Se face intersectia cu ele tinand cont de pozitionarea in genom
		3a. La o reuniune se updateaza V1 competandu-se pana la noua pozitie de terminare
	 	    si in V2 se modifica parametrii intervalului
		3b. Daca nu se reuneste se adauga in V1 si in V2 un interval nou
*/


///~~~~~~~~~~~~~~~~~~Logica aplicatiei~~~~~~~~~~~~~~~~~~~~~

/**
*	Proceseaza o pozitie: obtine intervale noi si reuneste cu existente
*/
void proceseazaPozitie(int fd,int pozitie,char* query,int windowSize,MAPPING_INTERVAL* minterval,VECTOR_INTERVAL* vinterval) {
  //uint hashCode = acgt2uint(&query[pozitie],windowSize); DEPRECATED
  //Se foloseste inversat pentru a se potrivi cu hash-ul
  uint hashCode = acgt2uint_inv(&query[pozitie],windowSize); 
  STRUCTURA_HASH* rezultat_hash;	//Structura cu rezultatele din hash
  
  //Variabile merge sort
  struct INTERVAL* interval; //Interval din lista
  INTERVAL_LLIST listPointer;
  INTERVAL_LLIST leftNode,rightNode;
  ulong vectorPointer;
  ulong pozitieGenom,pozitieIntervalExistent;

  //Obtin vectorul de pozitionari din genom
  //WARNING Rezultat hash poate intoarce NULL
  rezultat_hash = positionsForHash(hashCode,fd);
  
  if (rezultat_hash != NULL) {
	//Initializez variabilele de mergesort
	listPointer =*minterval->elements[pozitie];
	vectorPointer = 0;
	//Nodurile vecine
	leftNode = NULL;
	rightNode = NULL;

    //Fac reuniunea cu intervalele existente
    while (vectorPointer < rezultat_hash->numar_aparitii) {
    //while (vectorPointer < 1) {
    	//  MERGESORT
    	// Se tin doi pointeri, unui pe hash si unul pe elementele existente
    	// Se parcurg concomitent. Se verifica intervalele pentru intersectie
    	// Daca se intersecteaza atunci se extinde intervalul existent
    	// Daca nu se intersecteaza se injecteaza intervalul nou la acea pozitie
    	pozitieGenom = rezultat_hash->vector_pozitii[vectorPointer];

    	//DEBUG ONLY
    	//printf("pozitie=%d, pozitieGenom:%lu\n",pozitie,pozitieGenom);

    	//CAZ A.
    	// Lista este goala sau a ajuns la sfarsit
    	//CONCLUZIE: adaug intervalele ramase din vector
    	if (listPointer == NULL) {
			//Creez interval nou
			interval = (struct INTERVAL*) malloc(sizeof(struct INTERVAL));
			interval->pozitieQuery = pozitie;
			interval->pozitieGenom = pozitieGenom;
			interval->length = windowSize;
			vinterval_addInterval(interval,vinterval);

    		//Se adauga nodul la sfarsit
			if (leftNode != NULL) rightNode = leftNode->next;
			else rightNode = NULL;
    		minterval_insertInterval(interval, minterval, pozitie,
    				leftNode, rightNode );

    		//Calculez nodul stanga. Ar trebui sa fie nodul proaspat inserat
    		if (leftNode != NULL) leftNode = leftNode->next;
    		else leftNode = *minterval->elements[pozitie];

    		//Avansez pointerul pe vector
    		vectorPointer++;
    	}
    	//CAZ B.
    	// Lista nu este goala
    	//CONCLUZIE: Caut nodurile intre care se insereaza elementul
    	else {
    		//Obtin intervalul curent
    		interval = listPointer->data;

    		//Calculez pozitia ipotetica
    		pozitieIntervalExistent = (pozitie - interval->pozitieQuery) + interval->pozitieGenom;

    		//Testez intersectia intervalelor
    		if (pozitieGenom < pozitieIntervalExistent) {
    			// CAZ B1.
    			//  pozitia din genom a vectorului este mai mica decat a pozitiei curente
    			// CONCLUZIE: introduc elementul inaintea acestuia
    			leftNode = listPointer->previous;
    			rightNode = listPointer;

				//Creez interval nou
				interval = (struct INTERVAL*) malloc(sizeof(struct INTERVAL));
				interval->pozitieQuery = pozitie;
				interval->pozitieGenom = pozitieGenom;
				interval->length = windowSize;
				vinterval_addInterval(interval,vinterval);

	    		//Se adauga nodul la pozitia gasita
	    		minterval_insertInterval(interval, minterval, pozitie,
	    				leftNode, rightNode );

	    		//Se avanseaza pointerul pe vector
    			vectorPointer++; leftNode = listPointer;
    			//Pointerul pe interval se pastreaza
    		} else {
    			if (pozitieGenom == pozitieIntervalExistent) {
        			// CAZ B2.
        			//  Intervalele se intersecteaza
        			// CONCLUZIE: Extind intervalul curent, avansez ambii pointeri

    				//Updatez intervalul corespunzator
    				interval->length = (pozitie - interval->pozitieQuery) + windowSize;
    	    		minterval_extendInterval(listPointer, minterval, pozitie );

    	    		//Avansez pointerul pe vector si pointerul pe lista
    	    		vectorPointer++;
    	    		leftNode = listPointer;
    	    		listPointer = listPointer->next;
    			} else {
        			// CAZ B3.
        			//  Pozitia din genom a vectorului este mai mare ca a listei
        			// CONCLUZIE: Avansez in lista in cautarea unui element mai mare decat al vectorului
    				leftNode = listPointer;
    	    		listPointer = listPointer->next;
    			}
    		}
    	}

    	//DEBUG ONLY
    	//getchar();
    	//minterval_print(minterval);
    	//fflush(stdout);
    }
    
    //Eliberez STRUCTURA_HASH
#ifdef USE_STD_VECTOR_FOR_HASH_POSITIONS
    delete rezultat_hash;
#else
    free(rezultat_hash->vector_pozitii);
    free(rezultat_hash);
#endif    
  }
}

/**
*	Proceseaza un query pentru obtinerea intervalelor conservate
*/
void proceseazaQuery(int fd,char* query,int length,int windowSize,MAPPING_INTERVAL* minterval,VECTOR_INTERVAL* vinterval) {
  int i;
  int n = length-windowSize;
	
  for (i=0;i<=n;i++) {
    proceseazaPozitie(fd,i,query,windowSize,minterval,vinterval);
  }
}
