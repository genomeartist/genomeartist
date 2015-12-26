#include <stdio.h>
#include <stdlib.h>

#include "../../headere/gene_retrieval.h"

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

///~~~~~~~~~~~~~~~~~~~Functiile pe LISTA DE GENE~~~~~~~~~~~~~~~~~~~~
/**
*    Adauga un element la lista
*/
GENE_LLIST *gene_list_add(GENE_LLIST **p, int geneNumber) {
	if (p == NULL)
		return NULL;

	GENE_LLIST *n = (GENE_LLIST *) malloc(sizeof(GENE_LLIST));
	if (n == NULL)
		return NULL;

	n->next = *p; /* the previous element (*p) now becomes the "next" element */
	*p = n;       /* add new empty element to the front (head) of the list */
	n->data = geneNumber;

	return *p;
}

/**
*    Sterge capatul unei liste
*/
void gene_list_remove(GENE_LLIST **p) {
	if (p != NULL && *p != NULL)
	{
		GENE_LLIST *n = *p;
		*p = (*p)->next;
		free(n);
	}
}

/**
*      Afiseaza o lista
*/
void gene_list_print(GENE_LLIST *n) {
	if (n == NULL)
	{
		printf("list is empty");
	}
	while (n != NULL)
	{
		printf("[%2d] ",n->data);
		n = n->next;
	}
	printf("\n");
}

/**
 * Elibereaza memoria asociata unei liste inlantuite si a datelor din interior
 */
void gene_list_free(GENE_LLIST *n) {
	GENE_LLIST* aux_this;
	GENE_LLIST* aux_next;
    aux_this = n;
    while (aux_this != NULL)
    {
        aux_next = aux_this->next;
        aux_this->next = NULL;
        free(aux_this);
        aux_this = aux_next;
    }
}

///~~~~~~~~~~~~~~~~~~~Functiile pe structura CLOSEST_GENES~~~~~~~~~~~~~~~~~~~~

/**
	ALoca memorie pentru o structura closestGenes
*/
CLOSEST_GENES* closestGenesInit(struct INFORMATII_POZITIONARE* geneVector){
    CLOSEST_GENES* closestGenes = (CLOSEST_GENES*) malloc(sizeof(CLOSEST_GENES));
    closestGenes->geneVector = geneVector;
    closestGenes->upstreamGene = -1;
    closestGenes->downstreamGene = -1;
    closestGenes->insideGeneNo = 0;
    closestGenes->insideGenes = (GENELIST*) malloc(sizeof(GENELIST));
	*closestGenes->insideGenes = NULL;
    return closestGenes;
}

/**
	Elibereaza memoria pentru o structura closestGenes
*/
void closestGenesFree(CLOSEST_GENES* closestGenes) {
	gene_list_free(*closestGenes->insideGenes);
	free(closestGenes->insideGenes);
    free(closestGenes);
}

///~~~~~~~~~~~~~~~~~~~Functiile de initializare structura gene din server~~~~~~~~~~~~~~~~~~~

/**
 * Obtine cea mai apropiata gena ce o acopera
 */
int getIndexShadowGene(struct INFORMATII_POZITIONARE* geneVector, int geneNumber,
		int indexCandidat, unsigned long pozitieDeAcoperit) {

	if (indexCandidat == -1) {
		return -1;
	} else {
		if (geneVector[indexCandidat].offset_stop >= pozitieDeAcoperit)
			return indexCandidat;
		else return getIndexShadowGene(geneVector,geneNumber,
				geneVector[indexCandidat].shadowGene, pozitieDeAcoperit);
	}
}

/**
 * Funcite ce initializeaza acoperirea genelor pentru a le prelua usor
 */
void initializeShadowGenes(struct INFORMATII_POZITIONARE* geneVector, int geneNumber) {
	int i;

	for (i=0; i<geneNumber; i++) {
		geneVector[i].shadowGene = getIndexShadowGene(geneVector,geneNumber,
				i-1, geneVector[i].offset_start);
	}
}


///~~~~~~~~~~~~~~~~~~~Functiile de cautare gene~~~~~~~~~~~~~~~~~~~
/**
*	Gasesc regiunea in care se afla intervalul
*	Prin cautare binara.
*/
int findIndexRegion(struct INFORMATII_POZITIONARE* geneVector, 
		    int indexStart, int indexStop, 
		    unsigned long searchValue) {
    int middlePosition;
    unsigned long middleValue;
  
    //Fac cautare binara
    if ( indexStop == indexStart ) {
	//Daca exista decat un singur element
	if (searchValue < geneVector[indexStart].offset_start )
	    return indexStart;
	//Daca este in dreapta atunci intorc -1
	return -1;
    } else
    if ( indexStop - indexStart == 1 ) {
	//Daca este mai mic intorc prima pozitie
	if ( searchValue <=  geneVector[indexStart].offset_start )
	    return indexStart;	
	//Daca este chiar in interval intorc capatul din dreapta
	if ( searchValue <=  geneVector[indexStop].offset_start )
	    return indexStop;
	//Daca este mai mare decat tot vectorul, intorc -1
	return -1;
    } else {
        //Pozitia de mijloc
	middlePosition = indexStart + (indexStop - indexStart) / 2;
	middleValue = geneVector[middlePosition].offset_start;
	
	//Daca pozitia este mai mare decat mijlocul il trimit in jumatatea superioara
	//In caz de egalitate se alege jumatatea din dreapta. intersectia nu este buna
	if (searchValue < middleValue) 
	  return findIndexRegion(geneVector, indexStart, middlePosition, searchValue);
	else
	  return findIndexRegion(geneVector, middlePosition, indexStop, searchValue);
    }
}

/**
 * Obtin toate genele ce acopera o pozitie
 */
void findAllShadowGenes(struct INFORMATII_POZITIONARE* geneVector, int geneNumber,
		int indexCandidat, unsigned long pozitieDeAcoperit, CLOSEST_GENES* closest_genes) {
	if (indexCandidat == -1) {
		return;
	} else {
		//Verifi candidatul
		if (geneVector[indexCandidat].offset_stop >= pozitieDeAcoperit) {
			closest_genes->insideGeneNo ++;
			gene_list_add(closest_genes->insideGenes,indexCandidat);
		}

		//Continui sa urc
		findAllShadowGenes(geneVector,geneNumber,
				geneVector[indexCandidat].shadowGene, pozitieDeAcoperit, closest_genes);
	}
}

/**
*	Obtine genele care flancheaza un interval
*/
CLOSEST_GENES* findClosestGenes(struct INFORMATII_POZITIONARE* geneVector, int geneNumber, 
	unsigned long intervalStart, unsigned long intervalStop, int isComplement) {
    //Structura rezultat
    CLOSEST_GENES* closestGenes = closestGenesInit(geneVector);
    
    //EXCEPTIE 1. Vectorul e gol
    if (geneNumber == 0) return closestGenes;
    
    //Calculez urmatoarea gena care incepe dupa sfarsitul intervalului
    int downstreamGene = findIndexRegion( geneVector, 0, geneNumber-1 , intervalStop );
    
    //Merg inapoi pe vector cautand informatii despre gena upstream
    //Continui pana gasesc o gena care are sfarsitul inainter inceputului intervalului
    int upstreamGene = downstreamGene-1;
    int foundUpstream;
    foundUpstream = NOT_FOUND;
    while (upstreamGene >= 0) {
       //Ma opresc daca am gasit o gena corespunzatoare
       if (geneVector[upstreamGene].offset_stop < intervalStart) {
		  foundUpstream = FOUND;
		  break;
       } else {
		  //Gena se intersecteaza cu intervalul
    	   closestGenes->insideGeneNo ++;
    	   gene_list_add(closestGenes->insideGenes,upstreamGene);
       }
       
       //Continui iteratia
       upstreamGene --;
    }
    
    //Verific daca am gasit upstream
    if ( foundUpstream == NOT_FOUND )
      upstreamGene = -1;
    
    //Verific daca exista alte gene care sa acopere intervalul
	if ( foundUpstream == FOUND ) {
		findAllShadowGenes(geneVector,geneNumber,upstreamGene,
				intervalStart,closestGenes);
	}
    
    //Daca este pe catena complementara, semnificatia de upstream/downstream se inverseaza
    if ( isComplement == TRUE ) {
      closestGenes->upstreamGene = downstreamGene;
      closestGenes->downstreamGene = upstreamGene;
    } else {
      closestGenes->upstreamGene = upstreamGene;
      closestGenes->downstreamGene = downstreamGene;
    }
    
    return closestGenes;
}
