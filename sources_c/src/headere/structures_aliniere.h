#ifndef _STRUCTURES_ALINIERE_H
#define	_STRUCTURES_ALINIERE_H
#include "utils.h"
#include "m5_compunere_rezultate.h"

///~~~~~~~~~~~~~~~~~~~Structuri primare~~~~~~~~~~~~~~~~~~~~
//Structura interval
typedef struct ALINIERE
{
    //Informatii despre pozitionare
    int pozitieQuery;	//offsetul alinieri primei secvente
    int pozitieGenom;	//offsetul alinierii celei de-a doua secvente
    int lengthQuery;	//lungimea care s-a potrivit din query
    int lengthGenom;    //lungimea care s-a potrivit din genom
    //Informatii nucleu
    int pozitieNucleuQuery;	//pozitia nucleului in query
    int pozitieNucleuGenom;	//pozitia nucleului in genom
    int lengthNucleu;	//lungime nucleu
    int scoreNucleu;    //scorul SW al nucleului
    //Informatii de afisare
    int length;		//lungimea secventei rezultat
    int score;		//Scorul obtinut de aliniere
    char* out1;		//primul string aliniat
    char* relation;	//relatia (liniutze)
    char* out2;		//al doilea string aliniat
    //Informatii pentru urmarire originii
    int fisierSursa; //fisierul din care provine aceasta aliniere
    char isTransposon; //daca este parte din transposon
    char isDirect; //daca este pe catena directa sau invers complementata
	struct DISTANTA *legaturi; //alinierile la care se poate ajunge plecand de la aceasta
	int	outStringOffset;	//Locatia corespunzatoare in stringul de aliniere
} ALINIERE;

///~~~~~~~~~~~~~~~~~~~Vectori~~~~~~~~~~~~~~~~~~~~
//Vectorul de alinieri, cate o intrare pentru fiecare aliniere gasita
typedef struct VECTOR_ALINIERE {
  int size;		//Numarul de Intervale
  struct ALINIERE** elements;   //Vector de pointeri catre structurile rezultat
  struct DISTANTA **grafLegaturiInFisier;
} VECTOR_ALINIERE;

//Vectorul de alinieri, cate o intrare pentru fiecare fisier
typedef struct VECTOR_REZULTATE_PARTIALE {
  int size;		//Numarul de fisiere
  VECTOR_ALINIERE** elements;   //Vector de pointeri catre structurile rezultat
} VECTOR_REZULTATE_PARTIALE;

///~~~~~~~~~~~~~~~~~~~Metode pentru structuri~~~~~~~~~~~~~~~~~~~~
//Metodele publice pentru structura ALINIERE
void printSWResult(struct ALINIERE* result);
void invertResult(struct ALINIERE* result);
void complementSWResult(struct ALINIERE* result, int query_size);
void freeSWResult(struct ALINIERE* result);

//Metodele publice pentru VECTORUL de ALINIERI
VECTOR_ALINIERE* valiniere_initialize(int size);
void valiniere_complement(VECTOR_ALINIERE* valiniere, int query_size);
void valiniere_print(VECTOR_ALINIERE* valiniere);
void valiniere_free(VECTOR_ALINIERE* valiniere);

//Metodele publice pentru VECTORUL de REZULTATE PARTIALE
VECTOR_REZULTATE_PARTIALE* vrezpartiale_initialize(int size);
void vrezpartiale_print(VECTOR_REZULTATE_PARTIALE* vrezpartiale);
void vrezpartiale_free(VECTOR_REZULTATE_PARTIALE* vrezpartiale);

#endif	/* _STRUCTURES_ALINIERE_H */

