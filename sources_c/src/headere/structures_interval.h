/* 
 * File:   m2_reuniune.h
 * Author: iulian
 *
 * Created on June 20, 2010, 3:39 PM
 */

#ifndef _STRUCTURES_INTERVAL_H
#define	_STRUCTURES_INTERVAL_H
#include "utils.h"

///~~~~~~~~~~~~~~~~~~~Structuri primare~~~~~~~~~~~~~~~~~~~~
//Structura interval
typedef struct INTERVAL {
  ulong pozitieQuery;	//Pozitia intervalului in query
  ulong pozitieGenom;   //Pozitia intervalului in genom
  ulong length;		//Lungimea secventei
} INTERVAL;

///~~~~~~~~~~~~~~~~~~~Liste inlantuite~~~~~~~~~~~~~~~~~~~~
//Structura listei inlantute de intervale
typedef struct INODE {
	struct INTERVAL* data;
	struct INODE *next; /* pointer to next element in list */
	struct INODE *previous; /* pointer to the previous element in list */
	struct INODE *bridge; /* pointer to correspunding element in another list */
} I_LLIST;
typedef I_LLIST* INTERVAL_LLIST;

///~~~~~~~~~~~~~~~~~~~MAppinguri peste query~~~~~~~~~~~~~~~~~~~~
//Vectorul mapat peste query
typedef struct MAPPING_INTERVAL {
  int size;		//Numarul de Liste Inlantuite
  INTERVAL_LLIST** elements;   //Vectorul de liste inlantuite pentru fiecare pozitie
} MAPPING_INTERVAL;

///~~~~~~~~~~~~~~~~~~~Vectori~~~~~~~~~~~~~~~~~~~~
//Vectorul de intervale, implementat ca lista inlantuita
typedef struct VECTOR_INTERVAL {
  int size;
  INTERVAL_LLIST* elements;
} VECTOR_INTERVAL;

///~~~~~~~~~~~~~~~~~~~Metode pentru structuri~~~~~~~~~~~~~~~~~~~~
//Metodele publice pentru mapare interval
MAPPING_INTERVAL* minterval_initialize(int query_size);
void minterval_print(MAPPING_INTERVAL* minterval);
void minterval_free(MAPPING_INTERVAL* minterval);
void minterval_addInterval(struct INTERVAL* interval, MAPPING_INTERVAL* minterval, int position);
void minterval_insertInterval(struct INTERVAL* interval, MAPPING_INTERVAL* minterval, int position,
		I_LLIST *leftNode, I_LLIST *rightNode );
void minterval_extendInterval(I_LLIST *intervalNode, MAPPING_INTERVAL* minterval, int position);

//Metodele publice pentru vector interval
VECTOR_INTERVAL* vinterval_initialize();
void vinterval_print(VECTOR_INTERVAL* vinterval);
void vinterval_free(VECTOR_INTERVAL* vinterval);
void vinterval_addInterval(struct INTERVAL* interval, VECTOR_INTERVAL* vinterval);

#endif	/* _STRUCTURES_INTERVAL_H */

