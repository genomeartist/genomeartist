#ifndef _COMPUNERE_REZULTATE_H
#define	_COMPUNERE_REZULTATE_H

#include "structures_aliniere.h"
#include "comun.h"

#define STARE_T1 0
#define STARE_G 1
#define STARE_T2 2
#define DISTANTA_GENOMICA_MAXIMA 200
#define MODIF_START 10
#define MODIF_LEN 10

/* structura pentru heat_map

typedef struct HIT_MAP_FINAL {
  int size;		//Numarul de Intervale care ating un anumit punct
  //Vector de pointeri catre structurile rezultat
  struct HIT* santinela_start;
  struct HIT* santinela_sfarsit; 
} HIT_MAP_FINAL;

struct HIT{
  ALINIERE* interval_intersectat;
  struct HIT* vecin_dreapta;
};
*/

//cate o structura de acest tip pentru fiecare fisier
typedef struct REZULTATE_FISIER{
	int size;
	//lista inlantuita cu rezultatele din fisier
	struct REZULTAT_COMPUNERE *rezultate_fisier_start;
	struct REZULTAT_COMPUNERE *rezultate_fisier_sfarsit;
} REZULTATE_FISIER;

//cate o structura pentru fiecare solutie gasita
typedef struct REZULTAT_COMPUNERE{
	//numarul de structuri ALINIERE din care este formata aceasta solutie
	int size;
	//distanta adunata pe care aceasta solutie nu acopera query-ul
	int distanta;
	//suma scorurilor nucleelor din care este compusa solutia
	int scorNucleu;
	//fiecare solutie este compusa din uniunea a 2 seturi de solutii, fiecare set corespunzand unui fisier (care poate fi la randul lui direct, sau invers complementat)
	int fisierSursa1;
	bool isFisierSursa1Direct;
	int fisierSursa2;
	bool isFisierSursa2Direct;
	//pointer catre o structura care tine primul obiect ALINIERE din aceasta solutie
	struct BUCATA_REZULTAT *santinela_start;
	//pointer catre o structura care tine ultimul obiect ALINIERE din aceasta solutie
	struct BUCATA_REZULTAT *santinela_sfarsit;
	struct REZULTAT_COMPUNERE *vecin_stanga;	
	//pointer catre urmatoarea solutie din acelasi fisier
	struct REZULTAT_COMPUNERE *vecin_dreapta;
} REZULTAT_COMPUNERE;

//cate o structura pentru fiecare ALINIERE inclusa intr-o solutie
struct BUCATA_REZULTAT{
	struct ALINIERE *interval;
	struct BUCATA_REZULTAT *vecin_stanga;
	struct BUCATA_REZULTAT *vecin_dreapta;
};

//folosita la crearea unui vector de distante de la fiecare segment aliniat pe query la toate celelalte care urmeaza dupa el si
//cu care nu se intersecteaza
typedef struct DISTANTA{
	ALINIERE *punct_final;
	int distanta;
	struct DISTANTA *vecin_dreapta;
} DISTANTA;

typedef struct OUTPUT_COMPUNERE{
	int length;
	char *out1;
	char *relation;
	char *out2;
}OUTPUT_COMPUNERE;

REZULTATE_FISIER* CompuneRezultate(struct VECTOR_REZULTATE_PARTIALE *rezultate_partiale_directe, 
									struct VECTOR_REZULTATE_PARTIALE *rezultate_partiale_complementare, 
									int marime_query, struct FISIER_DATE* fisiere, char *query, int lungime_minima_nucleu,
									int numar_maxim_solutii, bool acorda_bonus, char *mem, char *queryInversComplementat,
									int *contorAlinieriCuNucleuPreaMicDeReturnat);

void PrinteazaRezultate(REZULTATE_FISIER *rezulate, char *query, int query_size, int numar_maxim_solutii);

void scrieInFisier(int fd, REZULTATE_FISIER *rezultate, char *query, int query_size, char *query_name, 
					VECTOR_REZULTATE_PARTIALE *rezultate_partiale_directe, VECTOR_REZULTATE_PARTIALE *rezultate_partiale_complementare,
					struct FISIER_DATE* fisiere, char *ext_mem, int max_cnt, time_t timp_total_executie, int contorAlinieriCuNucleuPreaMic,
					int lungime_minima_nucleu);
					
void elibereazaRezultat(REZULTAT_COMPUNERE *solutie);

#endif
