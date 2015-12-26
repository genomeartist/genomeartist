#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "../../headere/structures_expanded.h"
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

///~~~~~~~~~~~~~~~~~~~Functiile pe LISTA DE EXPANDED~~~~~~~~~~~~~~~~~~~~
/**
*    Adauga un element la lista
*/
EX_LLIST *ex_list_add(EX_LLIST **p, struct EXPAND_RESULT* expanded) {
	if (p == NULL)
		return NULL;

	EX_LLIST *n = (EX_LLIST *) malloc(sizeof(EX_LLIST));
	if (n == NULL)
		return NULL;

	n->next = *p; /* the previous element (*p) now becomes the "next" element */
	*p = n;       /* add new empty element to the front (head) of the list */
	n->data = expanded;

	return *p;
}

/**
*    Sterge capatul unei liste
*/
void ex_list_remove(EX_LLIST **p) {
	if (p != NULL && *p != NULL)
	{
		EX_LLIST *n = *p;
		*p = (*p)->next;
		free(n);
	}
}

/**
*    Cauta intr-o lista
*/
EX_LLIST **ex_list_search(EX_LLIST **n, struct EXPAND_RESULT* expanded) {
	if (n == NULL)
		return NULL;

	while (*n != NULL)
	{
		if ((*n)->data == expanded)
		{
			return n;
		}
		n = &(*n)->next;
	}
	return NULL;
}

/**
*      Afiseaza o lista
*/
void ex_list_print(EX_LLIST *n) {
	if (n == NULL)
	{
		printf("list is empty");
	}
	while (n != NULL)
	{
		//printf("[%p,%p,%p] ", n, n->next, n->data);
		printf("{[%3lu,%3lu]->[%9lu,%9lu]}",n->data->offset1,n->data->offset1+n->data->length1-1,
		       n->data->offset2,n->data->offset2+n->data->length2-1);
		n = n->next;
	}
	printf("\n");
}

/**
 * Elibereaza memoria asociata unei liste inlantuite si a datelor din interior
 */
void ex_list_free_with_data(EX_LLIST *n) {
    EX_LLIST* aux_this;
    EX_LLIST* aux_next;
    aux_this = n;
    while (aux_this != NULL)
    {
        aux_next = aux_this->next;
        aux_this->next = NULL;
        free(aux_this->data);
        free(aux_this);
        aux_this = aux_next;
    }
}

/**
 * Elibereaza memoria asociata unei liste inlantuite fara date din interior
 */
void ex_list_free_no_data(EX_LLIST *n) {
    EX_LLIST* aux_this;
    EX_LLIST* aux_next;
    aux_this = n;
    while (aux_this != NULL)
    {
        aux_next = aux_this->next;
        aux_this->next = NULL;
        free(aux_this);
        aux_this = aux_next;
    }
}

///~~~~~~~~~~~~~~~~~~~Functiile pe vector MAPARE_EXPANDED~~~~~~~~~~~~~~~~~~~~
/**
*	Initializeaza structura
*/
MAPPING_EXPANDED* mexpanded_initialize(int query_size) {
	int i;
	//Creez vectorul 1;
	MAPPING_EXPANDED* mexpanded = (MAPPING_EXPANDED*) malloc(sizeof(MAPPING_EXPANDED));
	mexpanded->size = query_size;
	mexpanded->elements = (EXPANDED_LLIST**) malloc(mexpanded->size*sizeof(EXPANDED_LLIST*));
	for (i=0;i<mexpanded->size;i++) {
	    mexpanded->elements[i] = (EXPANDED_LLIST*) malloc(sizeof(EXPANDED_LLIST));
	    *(mexpanded->elements[i]) = NULL;
	}

	return mexpanded;
}

/**
*	Printeaza vector V1
*/
void mexpanded_print(MAPPING_EXPANDED* mexpanded) {
  int i;
  EXPANDED_LLIST item;
  for (i=0;i<mexpanded->size;i++) {
      item = *(mexpanded->elements[i]);
      ex_list_print(item);
  }
}

/**
 * Elibereaza memoria asociata lui V1
 */
void mexpanded_free(MAPPING_EXPANDED* mexpanded) {
    int i;
    for (i = 0; i < mexpanded->size; i++) {
        ex_list_free_no_data(*mexpanded->elements[i]);
        free(mexpanded->elements[i]);
    }
    free(mexpanded->elements);
    free(mexpanded);
}

/**
*	Adauga un interval la o anumita pozitie
*/
void mexpanded_addExpanded(struct EXPAND_RESULT* expanded, MAPPING_EXPANDED* mexpanded, int position) {
  ex_list_add(mexpanded->elements[position],expanded);
}


///~~~~~~~~~~~~~~~~~~~Functiile pe vector VECTOR_EXPANDED~~~~~~~~~~~~~~~~~~~~
/**
*	Initializez vectorul de expanded
*/
VECTOR_EXPANDED* vexpanded_initialize() {
	//Creez vectorul 2;
	VECTOR_EXPANDED* vexpanded = (VECTOR_EXPANDED*) malloc(sizeof(VECTOR_EXPANDED));
	vexpanded->size = 0;
	vexpanded->elements = (EXPANDED_LLIST*) malloc(sizeof(EXPANDED_LLIST));
	*vexpanded->elements = NULL;

	return vexpanded;
}

/**
*	Printeaza vector de expanded
*/
void vexpanded_print(VECTOR_EXPANDED* vexpanded) {
  EXPANDED_LLIST item;
  item = *(vexpanded->elements);
  ex_list_print(item);
}

/**
 * Elibereaza memoria asociata vectorului de expanded
 */
void vexpanded_free(VECTOR_EXPANDED* vexpanded) {
    ex_list_free_with_data(*vexpanded->elements);
    free(vexpanded->elements);
    free(vexpanded);
}

/**
 * Elibereaza memoria asociata vectorului de expanded fara sa elibereze datele
 */
void vexpanded_free_no_data(VECTOR_EXPANDED* vexpanded) {
    ex_list_free_no_data(*vexpanded->elements);
    free(vexpanded->elements);
    free(vexpanded);
}
  
/**
*	Adauga un interval un vector
*/
void vexpanded_addExpanded(struct EXPAND_RESULT* expanded, VECTOR_EXPANDED* vexpanded) {
    ex_list_add(vexpanded->elements,expanded);
    vexpanded->size ++;
}

///~~~~~~~~~~~~~~~~~~~Functiile pe vector SORTED_EXPANDED~~~~~~~~~~~~~~~~~~~~
/**
*	Initializez vectorul de expanded
*/
SORTED_EXPANDED* sexpanded_initialize(int maxsize) {
	//Creez vectorul 2;
	SORTED_EXPANDED* sexpanded = (SORTED_EXPANDED*) malloc(sizeof(SORTED_EXPANDED));
	sexpanded->actual_size = 0;
	sexpanded->maximum_size = maxsize;
	sexpanded->elements = (EXPANDED_LLIST*) malloc(sizeof(EXPANDED_LLIST));
	*sexpanded->elements = NULL;

	return sexpanded;
}

/**
*	Printeaza vector de expanded
*/
void sexpanded_print(SORTED_EXPANDED* sexpanded) {
  EXPANDED_LLIST item;
  item = *(sexpanded->elements);
  ex_list_print(item);
}

/**
 * Elibereaza memoria asociata vectorului de expanded
 */
void sexpanded_free(SORTED_EXPANDED* sexpanded) {
    ex_list_free_no_data(*sexpanded->elements);
    free(sexpanded->elements);
    free(sexpanded);
}

/**
*	Adauga un interval un vector
*/
void sexpanded_addExpanded(struct EXPAND_RESULT* expanded, SORTED_EXPANDED* sexpanded) {
    EX_LLIST **p = sexpanded->elements;
    EX_LLIST *k = *p;
    EX_LLIST *t = NULL;
    int adaug = FALSE;
    
    //Daca nu am head atunci este eroare
    if (p == NULL)
	    return;

    //Verific daca il adaug sau nu
    if (sexpanded->actual_size < sexpanded->maximum_size) 
    {
	adaug = TRUE;
    } 
    else 
    {
	//Daca elementul nou este mai lung decat cel mai mic element
	if ( expanded->length1 > k->data->length1 ) 
	{
	  adaug = TRUE;
	}
	else 
	{
	  //Continditie de departajare in caz de egalitate
	  if ( expanded->length1 == k->data->length1 ) 
	  {
	    if ( expanded->tempScore > k->data->tempScore ) 
	    {
	      adaug = TRUE;
	    } 
	    else 
	    {
	      adaug = FALSE;
	    }
	  } 
	  else 
	  {
	    adaug = FALSE;
	  }
	}
    }
    
    //Gasesc locul in care se adauga
    if (adaug == TRUE) {
	while ( k != NULL ) {
	    if ( expanded->length1 > k->data->length1 ) 
	    {
	      t = k;
	      k = k->next;
	    } 
	    else 
	    {
	      if ( expanded->length1 == k->data->length1 ) 
	      {
		if ( expanded->tempScore > k->data->tempScore ) 
		{
		  t = k;
		  k = k->next;
		} 
		else 
		{
		  //Am gasit locul de insertie => Ies
		  break;
		}
	      } 
	      else 
	      {
		//Am gasit locul de insertie => Ies
		break;
	      }
	    }
	}
	
	//Caz special cand t == NULL, inseamna ca adaug la inceput si nu sterg nici un nod
	EX_LLIST *n = (EX_LLIST *) malloc(sizeof(EX_LLIST));
	if ( t == NULL ) {
	  n->next = k; /* the previous element (*p) now becomes the "next" element */
	  n->data = expanded;
	  *p = n;       /* elementul devine capatul listei*/
	  sexpanded->actual_size ++;
	} else {
	  n->next = k; /* the previous element (*p) now becomes the "next" element */
	  n->data = expanded;
	  t->next = n;
	  sexpanded->actual_size ++;
	  
	  //Fac verificarea de marime
	  if (sexpanded->actual_size > sexpanded->maximum_size) {
	    //Sterg primul element
	    t = *p;
	    *p = t->next;
	    
	    t->next = NULL;
	    free(t);
	    sexpanded->actual_size --;
	  }
	}    
    }
}
