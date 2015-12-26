#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "../../headere/structures_aliniere.h"
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

///~~~~~~~~~~~~~~~~~~~Functiile pe STRUCTURA ALINIERE~~~~~~~~~~~~~~~~~~~~
/**
*  Printeaza o structura rezultat
*/
void printSWResult(struct ALINIERE* result)
{
  printf("pozitieQuery = %d, lengthQuery= %d <==> pozitieGenom = %d, lengthGenom = %d\n",
	 result->pozitieQuery,result->lengthQuery,result->pozitieGenom, result->lengthGenom);
  printf("pozitieNucleuQuery = %d, pozitieNucleuGenom = %d, lungimeNucleuQuery = %d, scoreNucleu = %d\n",
	 result->pozitieNucleuQuery,result->pozitieNucleuGenom,result->lengthNucleu,result->scoreNucleu);
  printf("lungimeRezultat = %d, score = %d\n",
	 result->length,result->score);
  print_string(result->out1,result->length);
  print_string(result->relation,result->length);
  print_string(result->out2,result->length);
  printf("=================================\n");
}

/**
*  Algoritmul va face matchul invers, pentru a
*    prezenta rezultatul, el trebuie inversat
*/
void invertResult(struct ALINIERE* result)
{
  int i,n;
  char phony;
  n = result->length-1;

  //Inversez stringurile
  for (i=0; i < (result->length/2);i++)
  {
      //out1
      phony = result->out1[i];
      result->out1[i] = result->out1[n-i];
      result->out1[n-i] = phony;
      //relation
      phony = result->relation[i];
      result->relation[i] = result->relation[n-i];
      result->relation[n-i] = phony;
      //relation
      phony = result->out2[i];
      result->out2[i] = result->out2[n-i];
      result->out2[n-i] = phony;
  }
}

/**
*  Algoritmul va face matchul invers, pentru a
*    prezenta rezultatul, el trebuie inversat
*/
void complementSWResult(struct ALINIERE* result, int query_size)
{
  int i,n;
  char phony;
  n = result->length;
  
  //Inversez stringul
  invertResult(result);
  
  //Complementez rezultatele
  for (i=0;i<n;i++) {
      phony = result->out1[i];
      result->out1[i] = nucl2compl(phony);
      phony = result->out2[i];
      result->out2[i] = nucl2compl(phony);
  }
  
  //Complementez potitia de inceput in query
  result->pozitieQuery = query_size - result->pozitieQuery - result->lengthQuery;
  result->pozitieNucleuQuery = query_size - result->pozitieNucleuQuery - result->lengthNucleu;
}

/**
 * Eliberez memoria asociata unui rezultat
 */
void freeSWResult(struct ALINIERE* result) {
    free(result->out1);
    free(result->relation);
    free(result->out2);
    DISTANTA *distantaCurenta = result->legaturi;
    while (distantaCurenta != NULL)
    {
    	DISTANTA *distantaUrmatoare = distantaCurenta->vecin_dreapta;
    	free(distantaCurenta);
    	distantaCurenta = distantaUrmatoare;
    }
    free(result);
}

///~~~~~~~~~~~~~~~~~~~Functiile pe VECTOR ALINIERE~~~~~~~~~~~~~~~~~~~~
/**
    Initializez vectorul de alinieri
*/
VECTOR_ALINIERE* valiniere_initialize(int size) {
	int i;
	
	//Creez vectorul 3;
	VECTOR_ALINIERE* valiniere = (VECTOR_ALINIERE*) malloc(sizeof(VECTOR_ALINIERE));
	valiniere->size = size;
	//Aloc vectorul de pointeri catre rezultate
	valiniere->elements = (struct ALINIERE**) malloc(valiniere->size*sizeof(struct ALINIERE*));
	
	//Initializez cu NULL
  	for (i=0 ; i<size ; i++) {
  		valiniere->elements[i] = NULL;
	}
	
	return valiniere;
}

/**
*	Complementeaza rezultatele din vectorul de alinieri
*/
void valiniere_complement(VECTOR_ALINIERE* valiniere,int query_size) {
  int i,n;
  struct ALINIERE* result;
  
  n = valiniere->size;
  for (i=0;i<n;i++) {
  	result = valiniere->elements[i];
  	if (result!=NULL)  complementSWResult(result,query_size);
  }
}

/**
*	Printeaza vector de alinieri
*/
void valiniere_print(VECTOR_ALINIERE* valiniere) {
  int i,n;
  struct ALINIERE* result;
  
  n = valiniere->size;
  for (i=0;i<n;i++) {
  	result = valiniere->elements[i];
  	if (result!=NULL)  printSWResult(result);
  }
}

/**
 * Elibereaza memoria asociata vectorului de aliniere
 */
void valiniere_free(VECTOR_ALINIERE* valiniere) {
  int i,n;
  struct ALINIERE* result;
  
  //Eliberez fiecare structura rezultat
  n = valiniere->size;
  for (i=0;i<n;i++) {
  	result = valiniere->elements[i];
  	if (result != NULL) freeSWResult(result);
  }
  
  //Eliberez vectorul de pointeri
  free(valiniere->grafLegaturiInFisier);
  free(valiniere->elements);
  free(valiniere);
}

///~~~~~~~~~~~~~~~~~~~Functiile pe VECTOR REZULTATE PARTIALE~~~~~~~~~~~~~~~~~~~~
/**
*	Initializez vectorul de rezultate partiale
*/
VECTOR_REZULTATE_PARTIALE* vrezpartiale_initialize(int size) {
	int i;
	
	//Creez vectorul 3;
	VECTOR_REZULTATE_PARTIALE* vrezpartiale = (VECTOR_REZULTATE_PARTIALE*) malloc(sizeof(VECTOR_REZULTATE_PARTIALE));
	vrezpartiale->size = size;
	//Aloc vectorul de pointeri catre rezultate
	vrezpartiale->elements = (VECTOR_ALINIERE**) malloc(vrezpartiale->size*sizeof(VECTOR_ALINIERE*));
	
	//Initializez cu NULL
  	for (i=0 ; i<size ; i++) {
  		vrezpartiale->elements[i] = NULL;
	}
	
	return vrezpartiale;
}

/**
*	Afiseaza un vector de rezultate partiale
*/
void vrezpartiale_print(VECTOR_REZULTATE_PARTIALE* vrezpartiale) {
      int i,n;
      VECTOR_ALINIERE* result;
      
      n = vrezpartiale->size;
      for (i=0;i<n;i++) {
	    result = vrezpartiale->elements[i];
	    if (result!=NULL)  valiniere_print(result);
      }
}

/**
*	Elibereaza un vector de rezultate partiale
*/
void vrezpartiale_free(VECTOR_REZULTATE_PARTIALE* vrezpartiale) {
      int i,n;
      VECTOR_ALINIERE* result;
      
      //Eliberez fiecare structura rezultat
      n = vrezpartiale->size;
      for (i=0;i<n;i++) {
	    result = vrezpartiale->elements[i];
	    if (result != NULL) valiniere_free(result);
      }
      
      //Eliberez vectorul de pointeri
      free(vrezpartiale->elements);
      free(vrezpartiale);
}
