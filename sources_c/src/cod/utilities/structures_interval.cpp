#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "../../headere/structures_interval.h"
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

///~~~~~~~~~~~~~~~~~~~Functiile pe LISTA DE INTERVALE~~~~~~~~~~~~~~~~~~~~
/**
*    Adauga un element la lista
*/
I_LLIST *i_list_add(I_LLIST **p, struct INTERVAL* interval) {
	if (p == NULL)
		return NULL;

	I_LLIST *n = (I_LLIST *) malloc(sizeof(I_LLIST));
	if (n == NULL)
		return NULL;

	n->next = *p; 				/* the previous element (*p) now becomes the "next" element */
	n->bridge = NULL;
	if (*p != NULL) {
		n->previous = (*p)->previous;
		(*p)->previous = n;
	} else n->previous = NULL;

	*p = n;       				/* add new empty element to the front (head) of the list */
	n->data = interval;

	return *p;
}

/**
 * Inserez un element intr-o lista dublu inlantuita
 * Return: adresa elementului care s-a adaugat
 */
I_LLIST *i_list_insert(I_LLIST **head, struct INTERVAL* interval, I_LLIST *leftNode, I_LLIST *rightNode) {
	if (head == NULL)
		return NULL;

	I_LLIST *n = (I_LLIST *) malloc(sizeof(I_LLIST));
	if (n == NULL)
		return NULL;

	n->next = rightNode; 				/* the previous element (*p) now becomes the "next" element */
	n->previous = leftNode;
	n->bridge = NULL;
	n->data = interval;

	//Update the other nodes
	if (rightNode != NULL) {
		rightNode->previous = n;
	}

	if (leftNode != NULL) {
		leftNode->next = n;
	} else {
		*head = n;
	}

	return n;
}

/**
*    Sterge capatul unei liste
*/
void i_list_remove(I_LLIST **p) {
	if (p != NULL && *p != NULL)
	{
		I_LLIST *n = *p;
		*p = (*p)->next;
		free(n);
	}
}

/**
*    Cauta intr-o lista
*/
I_LLIST **i_list_search(I_LLIST **n, struct INTERVAL* interval) {
	if (n == NULL)
		return NULL;

	while (*n != NULL)
	{
		if ((*n)->data == interval)
		{
			return n;
		}
		n = &(*n)->next;
	}
	return NULL;
}

/**
 * Gaseste elementul care are campul bridge diferit de null
 * Se cauta tot timpul in sus
 * Returns: elementul cu care este bridge-uit
 */
I_LLIST *i_list_find_bridge(I_LLIST *n) {
	I_LLIST *ptr = n;

	while ( ptr != NULL ) {
		if (ptr->bridge != NULL) return ptr->bridge;
		else ptr = ptr->previous;
	}

	return NULL;
}

/**
*      Afiseaza o lista
*/
void i_list_print(I_LLIST *n) {
	if (n == NULL)
	{
		printf("list is empty");
	}
	while (n != NULL)
	{
		//printf("[%p,%p,%p] ", n, n->next, n->data);
		printf("[%3lu,%9lu](%4lu) b:%10p",n->data->pozitieQuery,n->data->pozitieGenom,n->data->length,n->bridge);
		n = n->next;
	}
	printf("\n");
}

/**
*      Afiseaza o lista
*/
void i_list_bridge_print(I_LLIST *n) {
	if (n == NULL)
	{
		printf("list is empty");
	} else {
		printf("%9lu: self:%p -> ",n->data->pozitieGenom,n);
	}

	//Printez toata lista
	while (n != NULL)
	{
		//printf("[%p,%p,%p] ", n, n->next, n->data);
		printf("%p -> ",n->bridge);
		n = n->bridge;
	}
	printf("\n");
}

/**
 * Elibereaza memoria asociata unei liste inlantuite si a datelor din interior
 */
void i_list_free_with_data(I_LLIST *n) {
    I_LLIST* aux_this;
    I_LLIST* aux_next;
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
void i_list_free_no_data(I_LLIST *n) {
    I_LLIST* aux_this;
    I_LLIST* aux_next;
    aux_this = n;
    while (aux_this != NULL)
    {
        aux_next = aux_this->next;
        aux_this->next = NULL;
        free(aux_this);
        aux_this = aux_next;
    }
}

///~~~~~~~~~~~~~~~~~~~Functiile pe vector MAPARE_INTERVAL~~~~~~~~~~~~~~~~~~~~
/**
*	Initializeaza vectorul V1
*/
MAPPING_INTERVAL* minterval_initialize(int query_size) {
	int i;
	//Creez vectorul 1;
	MAPPING_INTERVAL* minterval = (MAPPING_INTERVAL*) malloc(sizeof(MAPPING_INTERVAL));
	minterval->size = query_size;
	minterval->elements = (INTERVAL_LLIST**) malloc(minterval->size*sizeof(INTERVAL_LLIST*));
	for (i=0;i<minterval->size;i++) {
	    minterval->elements[i] = (INTERVAL_LLIST*) malloc(sizeof(INTERVAL_LLIST));
	    *(minterval->elements[i]) = NULL;
	}

	return minterval;
}

/**
*	Printeaza vector V1
*/
void minterval_print(MAPPING_INTERVAL* minterval) {
  int i;
  INTERVAL_LLIST item;
  for (i=0;i<minterval->size;i++) {
      item = *(minterval->elements[i]);
      printf("pozitie: %4d #",i);
      i_list_print(item);
  }
}

/**
 * Elibereaza memoria asociata lui V1
 */
void minterval_free(MAPPING_INTERVAL* minterval) {
    int i;
    for (i = 0; i < minterval->size; i++) {
        i_list_free_no_data(*minterval->elements[i]);
        free(minterval->elements[i]);
    }
    free(minterval->elements);
    free(minterval);
}

/**
*	Adauga un interval pe o anumita pozitie
*/
void minterval_addInterval(struct INTERVAL* interval, MAPPING_INTERVAL* minterval, int position) {
  i_list_add(minterval->elements[position],interval);
}

/**
 * Inserez un interval nou pe o anumita pozitie intre anumite intervale specifice
 * Se urmareste mentinerea ordinii in structura
 */
void minterval_insertInterval(struct INTERVAL* interval, MAPPING_INTERVAL* minterval, int position,
		I_LLIST *leftNode, I_LLIST *rightNode ) {
	//Variabile pentru procesare
	int i,n;
	I_LLIST *newNode,*previousNode,*bridgeNode;

	//Initializez bridge-ul
	if (leftNode != NULL)
		bridgeNode = leftNode->bridge;
	else bridgeNode = NULL;

	//Initialize parametrii de bucla
	n = position + interval->length;
	newNode = NULL;
	for (i = position; i < n; i++) {
		previousNode = newNode;

		//Inserez elementul la pozitia identificata
		newNode = i_list_insert(minterval->elements[i], interval, leftNode, rightNode);
		if (previousNode != NULL)
			previousNode->bridge = newNode;

		//Caut urmatorul element de legatura catre celalalt brat
		if (bridgeNode == NULL)
			bridgeNode = i_list_find_bridge(newNode);
		if (bridgeNode == NULL) {
			leftNode = NULL;
			if ( i+1 < n )
				rightNode = *(minterval->elements[i+1]);
			else
				rightNode = NULL;
		} else {
			leftNode = bridgeNode;
			rightNode = bridgeNode->next;
			bridgeNode = bridgeNode->bridge;
		}
	}
}

/**
 * Extind un interval cu o anumita lungime
 */
void minterval_extendInterval(I_LLIST *intervalNode, MAPPING_INTERVAL* minterval, int position) {
	//Variabile pentru identificare
	I_LLIST * ptrInterval = intervalNode;
	struct INTERVAL* interval = intervalNode->data;

	//Variabile pentru procesare
	int i,n;
	int startPosition = position;
	I_LLIST *newNode,*previousNode,*bridgeNode;
	I_LLIST *leftNode,*rightNode;

	//Ajung pana la capatul intervalului
	while (ptrInterval->bridge != NULL) {
		ptrInterval = ptrInterval->bridge;
		startPosition ++;
	}

	//Initializez variabilele pentru adaugare
	n = interval->pozitieQuery + interval->length;
	newNode = ptrInterval;
	bridgeNode = NULL;
	for (i = startPosition+1; i < n; i++) {
		//Caut urmatorul element de legatura catre celalalt brat
		if(bridgeNode == NULL)
			bridgeNode = i_list_find_bridge(newNode);

		//Daca tot nu s-a gasit bridge
		if (bridgeNode == NULL) {
			//Daca nu s-a gasit bridge se adauga chiar la inceput
			leftNode = NULL;
			if (minterval->elements[i] != NULL) rightNode = *(minterval->elements[i]);
			else rightNode = NULL;
		} else {
			leftNode = bridgeNode;
			rightNode = bridgeNode->next;
			bridgeNode = bridgeNode->bridge;
		}

		//Salvez ultimul element
		previousNode = newNode;

		//Inserez elementul la pozitia identificata
		newNode = i_list_insert(minterval->elements[i], interval, leftNode, rightNode);
		if (previousNode != NULL)
			previousNode->bridge = newNode;
	}
}

///~~~~~~~~~~~~~~~~~~~Functiile pe vector VECTOR_INTERVAL~~~~~~~~~~~~~~~~~~~~
/**
*	Initializez vectorul V2
*/
VECTOR_INTERVAL* vinterval_initialize() {
	//Creez vectorul 2;
	VECTOR_INTERVAL* vinterval = (VECTOR_INTERVAL*) malloc(sizeof(VECTOR_INTERVAL));
	vinterval->size = 0;
	vinterval->elements = (INTERVAL_LLIST*) malloc(sizeof(INTERVAL_LLIST));
	*vinterval->elements = NULL;

	return vinterval;
}

/**
*	Printeaza vector V2
*/
void vinterval_print(VECTOR_INTERVAL* vinterval) {
  INTERVAL_LLIST item;
  item = *(vinterval->elements);
  i_list_print(item);
}

/**
 * Elibereaza memoria asociata lui V1
 */
void vinterval_free(VECTOR_INTERVAL* vinterval) {
    i_list_free_with_data(*vinterval->elements);
    free(vinterval->elements);
    free(vinterval);
}

/**
*	Adauga un interval un vector
*/
void vinterval_addInterval(struct INTERVAL* interval, VECTOR_INTERVAL* vinterval) {
    i_list_add(vinterval->elements,interval);
    vinterval->size ++;
}
