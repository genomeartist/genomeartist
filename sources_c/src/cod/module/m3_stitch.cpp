#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "../../headere/m3_stitch.h"
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

///~~~~~~~~~~~~~~~~~~Logica aplicatiei~~~~~~~~~~~~~~~~~~~~~

/**
*   Reuneste un interval cu un pseudo-interval (interval 1 incepe tot timpul mai devreme decat interval 2)
*   Intervalele se vor reuni daca se intersecteaza in genom
*	Intorace:   1 - daca s-au reunit
*		    0 - daca nu s-au reunit
*/
long reunesteInterval(struct EXPAND_RESULT* interval1,struct EXPAND_RESULT* interval2) {
  ulong seq_min_absolute,seq_min_other;
  ulong seq_max_absolute,seq_max_other;
  ulong genom_min_absolute,genom_min_other;
  ulong genom_max_absolute,genom_max_other;
  ulong end1,end2;
  ulong lost_end;
  
  //Conditie obligatorie de reuniune. Ordinea din secventa se pastreaza in genom
  if (interval1->offset2 > interval2->offset2)
    return -1;
  
  //Obtinem minimul pentru secventa
  if (interval1->offset1 < interval2->offset1) {
    seq_min_absolute = interval1->offset1;
    seq_min_other = interval2->offset1;
  } else {
    seq_min_absolute = interval2->offset1;
    seq_min_other = interval1->offset1;
  }
  
  //Obtinem maximul pentru secventa
  end1 = interval1->offset1+interval1->length1;
  end2 = interval2->offset1+interval2->length1;
  if (end1 > end2) {
    seq_max_absolute = end1;
    seq_max_other = end2;
  } else {
    seq_max_absolute = end2;
    seq_max_other = end1;
  }
  
  //Obtinem minimul pentru genom
  if (interval1->offset2 < interval2->offset2) {
    genom_min_absolute = interval1->offset2;
    genom_min_other = interval2->offset2;
  } else {
    genom_min_absolute = interval2->offset2;
    genom_min_other = interval1->offset2;
  }
  
  //Obtinem maximul pentru genom
  end1 = interval1->offset2+interval1->length2;
  end2 = interval2->offset2+interval2->length2;
  if (end1 > end2) {
    genom_max_absolute = end1;
    genom_max_other = end2;
  } else {
    genom_max_absolute = end2;
    genom_max_other = end1;
  }
  
  //Punem conditia de intersectie
  if (genom_max_other < genom_min_other) {
    //Nu afectez intervale
    return -1;
  } else {
    //Modific interval1 astfel incat sa reprezinte reuniunea celor doua
    lost_end = interval1->offset1+interval1->length1;
    interval1->offset1 = seq_min_absolute;
    interval1->length1 = seq_max_absolute - seq_min_absolute;
    interval1->offset2 = genom_min_absolute;
    interval1->length2 = genom_max_absolute - genom_min_absolute;
    return lost_end;
  }
}

/**
*	Incearca reuniunea unui interval cu toate aflate la acea pozitie
*       Daca nu se reuneste, atunci se adauga stand alone
*/
void procesareInterval(struct EXPAND_RESULT* interval_nou, MAPPING_EXPANDED* mexpanded) {
  //Variabile locale
  EXPANDED_LLIST list = *mexpanded->elements[interval_nou->offset1];
  struct EXPAND_RESULT* interval_vechi;
  long lost_end,from,to;
  int shouldAdd = 1;
  long i;

  //iterez prin toate intervalele si incerc sa le reunesc
  while (list != NULL)
  {
	  interval_vechi = list->data;
	  lost_end = reunesteInterval(interval_vechi,interval_nou);
	  if (lost_end >= 0) {
	    shouldAdd = 0;
	    //Adaug in minterval diferenta de interval
	    from = lost_end;
	    to = interval_vechi->offset1+interval_vechi->length1;
	    for ( i = from; i < to ; i++) {
	      mexpanded_addExpanded(interval_vechi, mexpanded, i);
	    }
	  }
	  list = list->next;
  }

  //Daca nu s-a reunit cu nici un interval, trebuie pastrat individual
  if (shouldAdd == 1) {
      from = interval_nou->offset1;
      to = interval_nou->offset1+interval_nou->length1;
      for ( i = from; i < to ; i++) {
	mexpanded_addExpanded(interval_nou, mexpanded, i);
      }
  }
}

/**
*	Proceseaza o pozitie: obtine intervale noi si reuneste cu existente
*/
void proceseazaPozitie(int pozitie,MAPPING_EXPANDED* mauxiliary,MAPPING_EXPANDED* mexpanded) {
  struct EXPAND_RESULT* interval;
  EXPANDED_LLIST list = *mauxiliary->elements[pozitie];
  
  //iterez prin toate intervalele si incerc sa le reunesc
  while (list != NULL)
  {
	  interval = list->data;
	  procesareInterval(interval,mexpanded);
	  list = list->next;
  }
}

/**===============================================
*       REUNESTE INTERVALELE EXPANDED
*=================================================*/
void stitchTheExpanded(VECTOR_EXPANDED* vexpanded, MAPPING_EXPANDED* mexpanded) {
  //  Voi creea o structura MAPPING_EXPANDED intermediara pentru a sorta
  //  structurile expanded dupa pozitia de inceput
  int i;
  MAPPING_EXPANDED* auxiliary_mapping;
  EXPANDED_LLIST lista_expanded; //lista de intervale din interiorul vectorului de extinse
  struct EXPAND_RESULT* expanded_result; //Un rezultat extins singular
  
  //initializari
  auxiliary_mapping = mexpanded_initialize(mexpanded->size);
  
  //   Iterez prin vectorul de expanded si pun intervalele pe pozitia corespunzatoare din
  // maparea auxiliara
  lista_expanded = *(vexpanded->elements);
  while (lista_expanded != NULL)
  {
	  expanded_result = lista_expanded->data;
	  mexpanded_addExpanded(expanded_result, auxiliary_mapping, expanded_result->offset1 );
	  lista_expanded = lista_expanded->next;
  }
  
  // In maparea auxiliara avem sortate intervalele in ordinea pozitiilor de inceput
  // Le vom prelucra asemeanator cu etapa de reuniune
  for ( i=0; i<mexpanded->size; i++) {
    proceseazaPozitie(i,auxiliary_mapping,mexpanded);
  }
  
  //eliberez mappingul auxiliar
  mexpanded_free(auxiliary_mapping);
}
