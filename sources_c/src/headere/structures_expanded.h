/* 
 * File:   m2_reuniune.h
 * Author: iulian
 *
 * Created on June 20, 2010, 3:39 PM
 */

#ifndef _STRUCTURES_EXPANDED_H
#define	_STRUCTURES_EXPANDED_H
#include "utils.h"

///~~~~~~~~~~~~~~~~~~~Structuri primare~~~~~~~~~~~~~~~~~~~~
//Rezultatul din urma unei extinderi
typedef struct EXPAND_RESULT {
  char* str1;	//Pointer catre locatia de inceput a secventei extinse
  ulong offset1;  //Offsetul acesteia fata de inceputul stringului initial
  ulong length1;  //Lungimea secventei extinse
  char* str2;   //Pointer catre inceputul stringului omolog
  ulong offset2;  //Offsetul fata de initial
  ulong length2;  //Lungimea omologului extins
  //pentru sortare
  int tempScore; //Scor temporar rezultat din extindere = LUNGIMEA_INITIALA*PONDERE_LUNGIME + SCOR_STANGA +SCOR_DREAPTA
  int usedFlag; //Flag ce trebuie setat daca s-a adaugat
} EXPAND_RESULT;

///~~~~~~~~~~~~~~~~~~~Liste inlantuite~~~~~~~~~~~~~~~~~~~~
//Structura listei inlantute
typedef struct EXNODE {
	struct EXPAND_RESULT* data;
	struct EXNODE *next; /* pointer to next element in list */
} EX_LLIST;
typedef EX_LLIST* EXPANDED_LLIST;

///~~~~~~~~~~~~~~~~~~~MAppinguri peste query~~~~~~~~~~~~~~~~~~~~
//Vectorul mapat peste query
typedef struct MAPPING_EXPANDED {
  int size;		//Numarul de Liste Inlantuite
  EXPANDED_LLIST** elements;   //Vectorul de liste inlantuite pentru fiecare pozitie
} MAPPING_EXPANDED;

///~~~~~~~~~~~~~~~~~~~Vectori~~~~~~~~~~~~~~~~~~~~
//Vectorul de intervale, implementat ca lista inlantuita
typedef struct VECTOR_EXPANDED {
  int size;
  EXPANDED_LLIST* elements;
} VECTOR_EXPANDED;

//Vectorul de intervale, implementat ca lista inlantuita
typedef struct SORTED_EXPANDED {
  int maximum_size;
  int actual_size;
  EXPANDED_LLIST* elements;
} SORTED_EXPANDED;

///~~~~~~~~~~~~~~~~~~~Metode pentru structuri~~~~~~~~~~~~~~~~~~~~
//Metodele publice pentru mapare expanded
MAPPING_EXPANDED* mexpanded_initialize(int query_size);
void mexpanded_print(MAPPING_EXPANDED* mexpanded);
void mexpanded_free(MAPPING_EXPANDED* mexpanded);
void mexpanded_addExpanded(struct EXPAND_RESULT* expanded, MAPPING_EXPANDED* mexpanded, int position);

//Metode publice pentru vectorul de expanded
VECTOR_EXPANDED* vexpanded_initialize();
void vexpanded_print(VECTOR_EXPANDED* vexpanded);
void vexpanded_free(VECTOR_EXPANDED* vexpanded);
void vexpanded_free_no_data(VECTOR_EXPANDED* vexpanded);
void vexpanded_addExpanded(struct EXPAND_RESULT* expanded, VECTOR_EXPANDED* vexpanded);

//Metode publice pentru vectorul de expanded
SORTED_EXPANDED* sexpanded_initialize(int maxsize);
void sexpanded_print(SORTED_EXPANDED* sexpanded);
void sexpanded_free(SORTED_EXPANDED* sexpanded);
void sexpanded_addExpanded(struct EXPAND_RESULT* expanded, SORTED_EXPANDED* sexpanded);

#endif	/* _STRUCTURES_EXPANDED_H */

