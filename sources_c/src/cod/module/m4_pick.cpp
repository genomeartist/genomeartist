#include <stdlib.h>
#include <stdio.h>
#include "../../headere/m4_pick.h"
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

void pickBestMatches(VECTOR_EXPANDED* vpicked, MAPPING_EXPANDED* mexpanded, int pickingDepth) {
  SORTED_EXPANDED* sexpanded;
  EXPANDED_LLIST* head;
  EX_LLIST *listIterator;
  struct EXPAND_RESULT* expand_result;
  int i,n = mexpanded->size;
  
  //Pentru fiecare pozitie din query
  for (i=0;i<n;i++) {
      sexpanded = sexpanded_initialize(pickingDepth);
      head = mexpanded->elements[i];
      listIterator = *head;

      //Populez sorted expansion
      while (listIterator != NULL) {
	  expand_result = listIterator->data;
	  sexpanded_addExpanded(expand_result, sexpanded);
	  listIterator = listIterator->next;
      }
    
      //Adaug elementele din sorted in vpicked;
      listIterator = *(sexpanded->elements);
      while (listIterator != NULL) {
	  expand_result = listIterator->data;
	  if (expand_result->usedFlag == FALSE ) {
	    vexpanded_addExpanded(expand_result, vpicked);
	    expand_result->usedFlag = TRUE;
	  }
	  //Continui iteratia
	  listIterator = listIterator->next;
      }
      
      //Eliberez memoria asociata cu sexpanded
      sexpanded_free(sexpanded);
  }
  

}
