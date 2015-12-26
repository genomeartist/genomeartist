#ifndef _GENE_RETRIEVAL_H
#define	_GENE_RETRIEVAL_H

#include "comun.h"
#include "utils.h"

///~~~~~~~~~~~~~~~~~~~Structuri primare~~~~~~~~~~~~~~~~~~~~
//Lista de gene
typedef struct GENENODE {
	int data;
	struct GENENODE *next; /* pointer to next element in list */
} GENE_LLIST;
typedef GENE_LLIST* GENELIST;

//Rezultatul din urma unei extinderi
typedef struct CLOSEST_GENES {
  struct INFORMATII_POZITIONARE* geneVector;	//Pointer catre vectorul de gene
  int upstreamGene;	    //indexul genei upstream sau -1 in caz contrar
  int downstreamGene;	//indexul genei downstream sau -1 in caz contrar
  int insideGeneNo;	    //nr de gene din interior
  GENELIST* insideGenes; //lista de gene din interior
} CLOSEST_GENES;

///~~~~~~~~~~~~~~~~~~~Operatii pe memoria primara~~~~~~~~~~~~~~~~~~~
CLOSEST_GENES* closestGenesInit(struct INFORMATII_POZITIONARE* geneVector);	//ALoca memorie pentru o structura closestGenes
void closestGenesFree(CLOSEST_GENES* closestGenes);					//Elibereaza memoria

///~~~~~~~~~~~~~~~~~~~Functiile de initializare structura gene din server~~~~~~~~~~~~~~~~~~~
void initializeShadowGenes(struct INFORMATII_POZITIONARE* geneVector, int geneNumber);
int getIndexShadowGene(struct INFORMATII_POZITIONARE* geneVector, int geneNumber,
		int indexCandidat, unsigned long pozitieDeAcoperit);

///~~~~~~~~~~~~~~~~~~~Cautarea de gene~~~~~~~~~~~~~~~~~~~
#define FOUND 1
#define NOT_FOUND 0

//Gaseste cele mai apropiate gene
CLOSEST_GENES* findClosestGenes(struct INFORMATII_POZITIONARE* geneVector, int geneNumber, 
	unsigned long intervalStart, unsigned long intervalStop, int isComplement);

#endif	/* _GENE_RETRIEVAL_H */

