#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <unistd.h>
#include "../../headere/m5_compunere_rezultate.h"
#include "../../headere/structures_aliniere.h"
#include "../../headere/gene_retrieval.h"
#include "../../headere/m4_smith-waterman.h"

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

int maxResultCount;
REZULTAT_COMPUNERE nuAtasaSolutie;

void printeazaDistante(REZULTATE_FISIER *rezultate_fisier)
{
	REZULTAT_COMPUNERE *r = rezultate_fisier->rezultate_fisier_start;
	while (r != NULL)
	{
		printf("%d ", r->distanta);
		r = r->vecin_dreapta;
	}
	printf("\n");
}

//  Swap:  Swap two items.
void  Swap( REZULTAT_COMPUNERE *v1, REZULTAT_COMPUNERE *v2 , REZULTATE_FISIER *rezultate_fisier)
{
	if (v1 == v2)
		return;

    REZULTAT_COMPUNERE *tmpVal;

	tmpVal = v1->vecin_stanga;
	v1->vecin_stanga = v2->vecin_stanga;
	v2->vecin_stanga = tmpVal;
	//  vectorul trimis la sortare trebuie sa aiba cel putin 2 elemente, deci santinela de inceput va fi 
	//diferite de santinela de sfarsit
	if (v1->vecin_stanga != NULL)
		v1->vecin_stanga->vecin_dreapta = v1;
	else
		rezultate_fisier->rezultate_fisier_start = v1;
	if (v2->vecin_stanga != NULL)
		v2->vecin_stanga->vecin_dreapta = v2;
	else
		rezultate_fisier->rezultate_fisier_start = v2;

	tmpVal = v1->vecin_dreapta;
	v1->vecin_dreapta = v2->vecin_dreapta;
	v2->vecin_dreapta = tmpVal;
	//  vectorul trimis la sortare trebuie sa aiba cel putin 2 elemente, deci santinela de inceput va fi 
	//diferite de santinela de sfarsit
	if (v1->vecin_dreapta != NULL)
		v1->vecin_dreapta->vecin_stanga = v1;
	else
		rezultate_fisier->rezultate_fisier_sfarsit = v1;
	if (v2->vecin_dreapta != NULL)
		v2->vecin_dreapta->vecin_stanga = v2;
	else
		rezultate_fisier->rezultate_fisier_sfarsit = v2;
}

/*
//  Pivot:  Find and return the index of pivot element.
REZULTAT_COMPUNERE *Pivot( REZULTAT_COMPUNERE *first, REZULTAT_COMPUNERE *last , REZULTATE_FISIER *rezultate_fisier) 
{
    REZULTAT_COMPUNERE *pivot = first;
	REZULTAT_COMPUNERE *i = first->vecin_dreapta;
	REZULTAT_COMPUNERE *end = last->vecin_dreapta;

	while (i != end)
	{
		if( i->distanta <= pivot->distanta )
		{
			printf("swap intre distanta %d si distanta %d\n", i->distanta, pivot->distanta);
			Swap(i, pivot, rezultate_fisier);
			printeazaDistante(rezultate_fisier);
			printf("dupa swap; pivot->vecin_dreapta = %p\n",pivot->vecin_dreapta);
			i = pivot->vecin_dreapta;
		}
		else
			i = i->vecin_dreapta;
	}

    return pivot;
}
*/

void Quicksort( REZULTAT_COMPUNERE *first, REZULTAT_COMPUNERE *last , REZULTATE_FISIER *rezultate_fisier)
{
//	printf("distanta first = %d\n",first->distanta);
//	printf("distanta last = %d\n",last->distanta);

    REZULTAT_COMPUNERE *pivot;

    if( first != last ) {

		pivot = last;
		REZULTAT_COMPUNERE *i = first;
		REZULTAT_COMPUNERE *store_place = first;
		REZULTAT_COMPUNERE *end = last;
		REZULTAT_COMPUNERE *next_i;	

		while (i != end)
		{
			if( i->distanta <= pivot->distanta )
			{
				//daca primul element este chiar pozitia de stoare, atunci primul devine elementul 'i', care ii va lua locul
				if (first == store_place)
					first = i;
				else
					//daca primul element este 'i', atunci primul devine pozitia de stoare, care ii va lua locul
					if (first == i)
						first = store_place;

				/*nu mai este nevoie de partea asta, pentru ca pivotul este ultimul si nu este afectat in bucla while
				//daca ultimul element este 'i', atunci ultimul devine pivotul, care va lua locul lui 'i'
				if (i == last)
					last = pivot;
				else
				//daca ultimul element este chiar pivotul, atunci ultimul devine 'i', care va lua locul lui pivotului
				if (pivot == last)
					last = i;
				*/

//				printf("swap intre distanta %d si distanta %d\n", i->distanta, pivot->distanta);
				Swap(i, store_place, rezultate_fisier);
				//  elementul 'i' a fost schimbat cu elementul 'store_place' si urmatorul element verificat va fi cel din
				//dreapta lui 'store_place'
				next_i = store_place->vecin_dreapta;
//				printeazaDistante(rezultate_fisier);
//				printf("dupa swap; pivot->vecin_dreapta = %p\n",pivot->vecin_dreapta);
				//  elementul 'i' a fost schimbat cu elementul 'store_place' si urmatoarea pozitie pentru schimbare este
				//acum cea din dreapta lui 'i'
				store_place = i->vecin_dreapta;
			}
			else
				next_i = i->vecin_dreapta;
			
			i = next_i;
		}

		//pivot este ultimul, deci ultimul devine store_place
		last = store_place;
		//daca store_place era primul, atunci primul devine pivot
		if (store_place == first)
			first = pivot;
		Swap(store_place, pivot, rezultate_fisier);

/*
		printf("distanta first = %d\n",first->distanta);
		printf("distanta last = %d\n",last->distanta);
		printf("pivot->distanta = %d\n",pivot->distanta);
		printeazaDistante(rezultate_fisier);
		printf("----------------------------------------------\n");
*/

//		printf("distanta pivot = %d\n",pivot->distanta);
		if (pivot != first && pivot->vecin_stanga != NULL && pivot->vecin_stanga != first)
		{
//			printf("margine stanga (first) = %p , distanga = %d\n",first, first->distanta);
//			printf("margine dreapta (pivot->vecin_stanga) = %p , distanga = %d\n",pivot->vecin_stanga, pivot->vecin_stanga->distanta);
        	Quicksort( first, pivot->vecin_stanga , rezultate_fisier);
		}
		if (pivot != last && pivot->vecin_dreapta != NULL && pivot->vecin_dreapta != last)
		{
//			printf("margine stanga (pivot->vecin_dreapta) = %p , distanga = %d\n",pivot->vecin_dreapta, pivot->vecin_dreapta->distanta);
//			printf("margine dreapta (last) = %p , distanga = %d\n",last, last->distanta);
        	Quicksort( pivot->vecin_dreapta, last , rezultate_fisier);
		}
    }
}

int calculeazaGapuriInNucleu(ALINIERE *aliniere)
{
	return (aliniere->lengthNucleu*2 - aliniere->scoreNucleu) / 3;
}

int adaugaRezultatLaSfarsitCompunere(REZULTAT_COMPUNERE* rezultat_compunere, ALINIERE *aliniere, DISTANTA *distantaNoua)
{
	int distantaAdaugata = 0;
	rezultat_compunere->size++;
	if (rezultat_compunere->santinela_start == NULL)
	{
		rezultat_compunere->santinela_start = (struct BUCATA_REZULTAT*)calloc(1,sizeof(struct BUCATA_REZULTAT));
		rezultat_compunere->santinela_start->interval = aliniere;
		rezultat_compunere->santinela_start->vecin_dreapta = NULL;
		rezultat_compunere->santinela_start->vecin_stanga = NULL;
		rezultat_compunere->santinela_sfarsit = rezultat_compunere->santinela_start;
		rezultat_compunere->distanta = aliniere->pozitieNucleuQuery + calculeazaGapuriInNucleu(aliniere);
		distantaAdaugata = rezultat_compunere->distanta;
		rezultat_compunere->scorNucleu = aliniere->scoreNucleu;
	}
	else
	{
		struct BUCATA_REZULTAT *element_nou = (struct BUCATA_REZULTAT*)calloc(1,sizeof(struct BUCATA_REZULTAT));
		element_nou->interval = aliniere;
		element_nou->vecin_dreapta = NULL;
		element_nou->vecin_stanga = rezultat_compunere->santinela_sfarsit;
		
		distantaAdaugata = distantaNoua->distanta;
		
		rezultat_compunere->distanta += distantaAdaugata;
		rezultat_compunere->scorNucleu += aliniere->scoreNucleu;

		rezultat_compunere->santinela_sfarsit->vecin_dreapta = element_nou;
		rezultat_compunere->santinela_sfarsit = element_nou;
	}
	return distantaAdaugata;
}

void EliminaUltimulElementDinCompunere(REZULTAT_COMPUNERE *rezultat_compunere, int distantaEliminata)
{
	if (rezultat_compunere->size > 0)
	{
		rezultat_compunere->size--;
		if (rezultat_compunere->size == 0)
		{
			//daca ajunge la marimea 0, atunci santinala de sfarsit = santinela de inceput (pentru ca era un singur element)
			free(rezultat_compunere->santinela_start);
			rezultat_compunere->santinela_start = NULL;
			rezultat_compunere->santinela_sfarsit = NULL;
			rezultat_compunere->distanta = 0;
			rezultat_compunere->scorNucleu = 0;
		}
		else
		{
			//daca nu ajunge la 0, mai raman elemente
			BUCATA_REZULTAT *temp = rezultat_compunere->santinela_sfarsit;
			rezultat_compunere->santinela_sfarsit = rezultat_compunere->santinela_sfarsit->vecin_stanga;
			rezultat_compunere->santinela_sfarsit->vecin_dreapta = NULL;

			rezultat_compunere->distanta -= distantaEliminata;
			rezultat_compunere->scorNucleu -= temp->interval->scoreNucleu;
			free(temp);
		}
	}
}

void DrumIntreDouaAlinieri(ALINIERE *aliniereStart, ALINIERE *aliniereStop, bool acorda_bonus, int capatTranspozon)
{
	/*
	if (aliniereStart->pozitieQuery == 94 && aliniereStart->lengthQuery == 43 &&
		aliniereStop->pozitieQuery == 138 && aliniereStop->lengthQuery == 146){
		printf("Am gasit!\n");
	}
	*/
	
	//daca unul dintre segmente nu are nucleu, atunci nu poate fi drum intre ele
	if (aliniereStart->lengthNucleu < 0 || aliniereStop->lengthNucleu<0)
	{
		//printf("NU exista drum (nucleu negativ)\n");
		return;
	}
	
	//daca al 2-lea se termina la o pozitie mai mica decat pozitia de terminare a primului
	//sau daca al 2-lea incepe inainte de inceperea primului, nu are sens acest drum
	if (aliniereStart->pozitieNucleuQuery+aliniereStart->lengthNucleu-1 >= aliniereStop->pozitieNucleuQuery+aliniereStop->lengthNucleu-1 ||
	    aliniereStop->pozitieNucleuQuery <= aliniereStart->pozitieNucleuQuery){
		return;
	}

	double patru_pe_zece = (double)4 / (double)10;

	//daca una dintre cele 2 alinieri face parte din genom si cealalta face parte din transposon
	bool conexiuneTranspGenom = false;
	if (  (aliniereStart->isTransposon && !aliniereStop->isTransposon) || 
		  (!aliniereStart->isTransposon && aliniereStop->isTransposon) )
	{
		conexiuneTranspGenom = true;
	}

	bool conexiuneTranspTransp = false;
	//daca este vorba despre o conexiune intre 2 alinieri din transposon
	if ( aliniereStart->isTransposon && aliniereStop->isTransposon )
		conexiuneTranspTransp = true;

	bool conexiuneGenomGenom = false;
	int distantaGenomica = 0;
	if (!conexiuneTranspGenom && !conexiuneTranspTransp)
	{
		conexiuneGenomGenom = true;
		if (aliniereStart->isDirect)
			distantaGenomica = aliniereStop->pozitieGenom - (aliniereStart->pozitieGenom + aliniereStart->lengthGenom-1);
		else
			distantaGenomica = aliniereStart->pozitieGenom - (aliniereStop->pozitieGenom + aliniereStop->lengthGenom-1); 
	}

	//  trebuie respectata pozitionarea genomica, exceptand situatiile in care incercam sa formam:
	//- o legatura de la o aliniere din transposon la o alta aliniere din transposon (capatul care 
	// se va potrivi pe query in dreapta va fi in transposon inaintea capatului care se va potrivi 
	// pe query in stanga, sau invers)
	//- o legatura intre o aliniere din transposon si una din genom sau o legatura intre o aliniere 
	// din genom si una din transposon (caz in care ordinea genomica nu are relevanta, facand parte din entitati diferite)
	if (conexiuneTranspGenom || conexiuneTranspTransp ||
		(conexiuneGenomGenom && distantaGenomica >= 0 && distantaGenomica <= DISTANTA_GENOMICA_MAXIMA))
	{
		int bonus = 0;
		int SFARSIT_TRANSP = capatTranspozon;//10691;
		int DECALAJ_MAXIM_PT_BONUX = 10;
		int VALOARE_BONUS = -500;
		int DISTANTA_MAXIMA_ACORDARE_BONUS = 10;
		
		//- daca alinierile extinse sunt una in continuarea celeilalte, atunci distanta o sa fie 0
		//- daca alinierile extinse se intersecteaza, distanta o sa fie < 0
		int distanta_pe_query = aliniereStop->pozitieQuery - (aliniereStart->pozitieQuery + aliniereStart->lengthQuery-1) - 1;
		
		//  bonusul se acorda daca se specifica acest lucru, conexiunea este transp-genom sau genom-transp si nu cleele sunt unul in continuarea 
		//celuilalt sau se suprapun
		if (acorda_bonus && conexiuneTranspGenom && distanta_pe_query <= DISTANTA_MAXIMA_ACORDARE_BONUS)
		{
			//daca prima aliniere transmisa este transpozon, atunci ii este verificat capatul din dreapta
			if (aliniereStart->isTransposon)
			{
				//daca alinierea este orientata pe direct, atunci capatul din dreapta al alinierii trebuie sa fie cat mai aproape de sfarsitul transpozonului
				if (aliniereStart->isDirect && (SFARSIT_TRANSP - (aliniereStart->pozitieGenom + aliniereStart->lengthGenom-1) < DECALAJ_MAXIM_PT_BONUX)) 
					bonus = VALOARE_BONUS;
				else
				//daca alinierea nu este pe direct, atunci capatul din dreapta al alinierii trebuie sa fie cat mai aproate de inceputul transpozonului
					if (!aliniereStart->isDirect && (aliniereStart->pozitieGenom < DECALAJ_MAXIM_PT_BONUX))
						bonus = VALOARE_BONUS;
			}
			//daca a 2-a aliniere transmisa este transpozon, atunci ii este verificat capatul din stanga
			else
			{
				//daca alinierea este pe direct, atunci capatul sau din stanga trebuie sa fie cat mai aproape de inceputul transpozonului
				if (aliniereStop->isDirect && (aliniereStop->pozitieGenom < DECALAJ_MAXIM_PT_BONUX))
					bonus = VALOARE_BONUS;
				else
				//daca alinierea nu este pe direct, atunci capatul sau din stanga trebuie sa fie cat mai aproate de sfarsitul transpozonului
					if (!aliniereStop->isDirect && (SFARSIT_TRANSP - (aliniereStop->pozitieGenom + aliniereStop->lengthGenom-1) < DECALAJ_MAXIM_PT_BONUX))
						bonus = VALOARE_BONUS;
			}
		}
		
		//daca nu se intersecteaza deloc (nici macar la nivel de segmente extinse)
		if (aliniereStart->pozitieQuery + aliniereStart->lengthQuery-1 < aliniereStop->pozitieQuery)
		{
			DISTANTA *distantaNoua = (DISTANTA*)calloc(1, sizeof(DISTANTA));
			distantaNoua->vecin_dreapta = aliniereStart->legaturi;
			aliniereStart->legaturi = distantaNoua;
			//sigur nu se intersecteaza, deci distanta > 0
			//distanta dintre ele se calculeaza tot in functie de distanta intre nuclee, pentru a pastra unitatea metodei de calcul
			distantaNoua->distanta = aliniereStop->pozitieNucleuQuery - 
									(aliniereStart->pozitieNucleuQuery + aliniereStart->lengthNucleu);
			distantaNoua->distanta += calculeazaGapuriInNucleu(aliniereStop);
			distantaNoua->distanta += bonus;
			distantaNoua->punct_final = aliniereStop;
			//printf("exista drum fara intersectie\n");
			//printf("aliniereStart->pozitieNucleuQuery = %d; aliniereStart->lengthNucleu = %d\n",aliniereStart->pozitieNucleuQuery,aliniereStart->lengthNucleu);
			//printf("aliniereStop->pozitieNucleuQuery = %d\n", aliniereStop->pozitieNucleuQuery);
			return;
		}
		//pentru conexiunile transp-genom sau genom-transp sau transp-transp, segmentele au voie sa se intersecteze cu pana la 40% din nucleu
		else
		{
			//  daca primul e transpozon si al 2-lea nu e, primul va manca toata bucata de intersectie cu acesta,
			//cu specificatia :
			//		- extinderea primului nu are voie sa manance din nucleul celui de-al 2-lea
			//		- nucleul primului poate mananca din nucleul celui de-al 2-lea (daca e cazul)
			if (aliniereStart->isTransposon && !aliniereStop->isTransposon)
			{
				/* metoda asta de calcul a lungimii presupune ca transpozonul primeste toata bucata de intersectie
				 * (extinderea nucleului transpozonului poate manca din nucleul genomului)
				 * 
				//calculam lungimea de intersectie a primului interval extins cu nucleul celui de-al 2-lea 
				//daca primul interval nu se intersecteaza cu nucleul celui de-al 2-lea, va fi negativa
				int lng_intersectie = (aliniereStart->pozitieQuery + aliniereStart->lengthQuery - 1) -
											aliniereStop->pozitieNucleuQuery + 1;
				*/
				
				//calculam lungimea de intersectie a nucleului primului interval cu nucleul celui de-al 2-lea 
				//daca primul nucleu nu se intersecteaza cu nucleul celui de-al 2-lea, va fi negativa
				int lng_intersectie = (aliniereStart->pozitieNucleuQuery + aliniereStart->lengthNucleu - 1) -
											aliniereStop->pozitieNucleuQuery + 1;
				//ca sa existe drum trebuie sa nu fie mancat mai mult de 40% din nucleul celui de-al 2-lea
				//avand in vedere ca primul interval o sa pastreze tot, din nucleul lui nu se ia deloc
				if ( lng_intersectie < aliniereStop->lengthNucleu * patru_pe_zece )
				{
					DISTANTA *distantaNoua = (DISTANTA*)calloc(1, sizeof(DISTANTA));
					distantaNoua->vecin_dreapta = aliniereStart->legaturi;
					aliniereStart->legaturi = distantaNoua;
					//distanta dintre ele se calculeaza tot in functie de distanta intre nuclee, pentru a pastra unitatea metodei de calcul
					distantaNoua->distanta = aliniereStop->pozitieNucleuQuery - 
											(aliniereStart->pozitieNucleuQuery + aliniereStart->lengthNucleu);
					if (distantaNoua->distanta < 0)
						distantaNoua->distanta = 0;
					distantaNoua->distanta += calculeazaGapuriInNucleu(aliniereStop);
					distantaNoua->distanta += bonus;
					distantaNoua->punct_final = aliniereStop;
					return;
				}
			} 
			//  daca al 2-lea este transpozon si primul nu e, al 2-lea va manca toata bucata de intersectie cu acesta,
			//cu specificatia :
			//		- extinderea celui de-al 2-lea nu are voie sa manance din nucleul primului
			//		- nucleul celui de-al 2-lea poate mananca din nucleul primului (daca e cazul)
			else if (!aliniereStart->isTransposon && aliniereStop->isTransposon)
			{
				/* metoda asta de calcul a lungimii presupune ca transpozonul primeste toata bucata de intersectie
				 * (extinderea nucleului transpozonului poate manca din nucleul genomului)
				 * 
				//calculam lungimea de intersectie a nucleului primului interval cu cel de-al 2-lea interval extins
				//daca al 2-lea interval nu se intersecteaza cu nucleul primului, va fi negativa
				int lng_intersectie = (aliniereStart->pozitieNucleuQuery + aliniereStart->lengthNucleu - 1) -
											aliniereStop->pozitieQuery + 1;
				*/
				
				//calculam lungimea de intersectie a nucleului primului interval cu nucleul celui de-al 2-lea interval
				//daca nucleul celui de-al 2-lea interval nu se intersecteaza cu nucleul primului, va fi negativa
				int lng_intersectie = (aliniereStart->pozitieNucleuQuery + aliniereStart->lengthNucleu - 1) -
											aliniereStop->pozitieNucleuQuery + 1;
				
				//ca sa existe drum trebuie sa nu fie mancat mai mult de 40% din nucleul primului
				//avand in vedere ca al 2-lea interval o sa pastreze tot, din nucleul lui nu se ia deloc
				if ( aliniereStart->lengthNucleu * patru_pe_zece > lng_intersectie )
				{
					DISTANTA *distantaNoua = (DISTANTA*)calloc(1, sizeof(DISTANTA));
					distantaNoua->vecin_dreapta = aliniereStart->legaturi;
					aliniereStart->legaturi = distantaNoua;
					//distanta dintre ele se calculeaza tot in functie de distanta intre nuclee, pentru a pastra unitatea metodei de calcul
					distantaNoua->distanta = aliniereStop->pozitieNucleuQuery - 
											(aliniereStart->pozitieNucleuQuery + aliniereStart->lengthNucleu);
					if (distantaNoua->distanta < 0)
						distantaNoua->distanta = 0;
					distantaNoua->distanta += calculeazaGapuriInNucleu(aliniereStop);
					distantaNoua->distanta += bonus;
					distantaNoua->punct_final = aliniereStop;
					return;
				}
			}
			//	daca ajunge aici inseamna ca secventele sunt de acelasi tip (transpozon sau genom)
			//  daca pozitia la care se termina nucleul segmentului curent este mai mica decat pozitia la care incepe nucleul segmentului j,
			//atunci cele 2 nuclee nu se intersecteaza, si exista drum de la segmentul 'i' la segmentul 'j'
			else if (aliniereStart->pozitieNucleuQuery + aliniereStart->lengthNucleu-1 < aliniereStop->pozitieNucleuQuery)
			{
				DISTANTA *distantaNoua = (DISTANTA*)calloc(1, sizeof(DISTANTA));
				distantaNoua->vecin_dreapta = aliniereStart->legaturi;
				aliniereStart->legaturi = distantaNoua;
				//sigur nu se intersecteaza, deci distanta > 0
				distantaNoua->distanta = aliniereStop->pozitieNucleuQuery - 
										(aliniereStart->pozitieNucleuQuery + aliniereStart->lengthNucleu);
				distantaNoua->distanta += calculeazaGapuriInNucleu(aliniereStop);
				distantaNoua->distanta += bonus;
				distantaNoua->punct_final = aliniereStop;
				//printf("exista drum fara intersectie\n");
				//printf("aliniereStart->pozitieNucleuQuery = %d; aliniereStart->lengthNucleu = %d\n",aliniereStart->pozitieNucleuQuery,aliniereStart->lengthNucleu);
				//printf("aliniereStop->pozitieNucleuQuery = %d\n", aliniereStop->pozitieNucleuQuery);
				return;
			}
			else
			{
				//daca cele 2 nuclee se intersecteaza
				if (aliniereStart->pozitieNucleuQuery + aliniereStart->lengthNucleu-1 >= aliniereStop->pozitieNucleuQuery && 
					aliniereStart->pozitieNucleuQuery < aliniereStop->pozitieNucleuQuery)
				{
					int lng_intersectie = (aliniereStart->pozitieNucleuQuery + aliniereStart->lengthNucleu - 1) -
											aliniereStop->pozitieNucleuQuery + 1;
	
					//si intersectia lor nu depaseste 40% din lungimea oricaruia
					//(un procent sub 50% asigura faptul ca segmentul j va acoperi si el o parte din query :
					// max 40% se suprapune cu 'i', eventual inca max 40% cu un segment urmator si ramane minim 20% pe query)
					if ( aliniereStart->lengthNucleu * patru_pe_zece > lng_intersectie &&
						aliniereStop->lengthNucleu * patru_pe_zece > lng_intersectie)
					{
						//atunci, se creeaza un drum de la aliniereStart la aliniereStop
						DISTANTA *distantaNoua = (DISTANTA*)calloc(1, sizeof(DISTANTA));
						distantaNoua->vecin_dreapta = aliniereStart->legaturi;
						aliniereStart->legaturi = distantaNoua;
						//gap-ul lasat intre ele este 0, avand in vedere ca se intersecteaza
						distantaNoua->distanta = 0;
						distantaNoua->distanta += calculeazaGapuriInNucleu(aliniereStop);
						distantaNoua->distanta += bonus;
						distantaNoua->punct_final = aliniereStop;
						//printf("exista drum cu intersectie\n");
						return;
					}
				}
			}
		}
	}

	//printf("NU exista drum\n");
}

void CalculeazaDistanteInFisier(VECTOR_ALINIERE *alinieri, int lungime_minima_nucleu, bool acorda_bonus)
{
	alinieri->grafLegaturiInFisier = (DISTANTA **)calloc(alinieri->size, sizeof(DISTANTA*));

	for (int i=0; i<alinieri->size; i++)
	{
		if ( alinieri->elements[i]->lengthNucleu >= lungime_minima_nucleu)
			for (int j=i+1; j<alinieri->size; j++)
			{
				if ( alinieri->elements[j]->lengthNucleu >= lungime_minima_nucleu)
				{
					//printf("alinieri->size = %d; i = %d; j = %d\n",alinieri->size, i, j);
					//printf("alinieri->elements[i] = %p; alinieri->elements[j] = %p\n",alinieri->elements[i],alinieri->elements[j]);
		
					//daca exista un drum de la alinierea 'i' la alinierea 'j', se creaza o legatura
					//ambele segmente fiind in acelasi fisier, nu conteaza care este capatul (chiar daca ar fi transpozon) 
					DrumIntreDouaAlinieri(alinieri->elements[i], alinieri->elements[j], acorda_bonus, 0);
					alinieri->grafLegaturiInFisier[i] = alinieri->elements[i]->legaturi;
		
					//printf("dupa drum i->j\n");
		
					//daca exista un drum de la alinierea 'j' la alinierea 'i', se creaza o legatura
					//ambele segmente fiind in acelasi fisier, nu conteaza care este capatul (chiar daca ar fi transpozon)
					DrumIntreDouaAlinieri(alinieri->elements[j], alinieri->elements[i], acorda_bonus, 0);
					alinieri->grafLegaturiInFisier[j] = alinieri->elements[j]->legaturi;
					//printf("---------------------\n");
				}
			}
	}
}

void calculeazaDistanteIntreRezultateFisiere(VECTOR_ALINIERE *alinieriFisier1, VECTOR_ALINIERE *alinieriFisier2, int lungime_minima_nucleu, 
											 bool acorda_bonus, int capatTranspozon)
{
	for (int i=0; i<alinieriFisier1->size; i++)
	{
		if ( alinieriFisier1->elements[i]->lengthNucleu >= lungime_minima_nucleu)
			for (int j=0; j<alinieriFisier2->size; j++)
			{
				if ( alinieriFisier2->elements[j]->lengthNucleu >= lungime_minima_nucleu)
				{
					//daca exista un drum de la alinierea 'i' din fisierul 1 la alinierea 'j' din fisierul 2, se creaza o legatura
					DrumIntreDouaAlinieri(alinieriFisier1->elements[i], alinieriFisier2->elements[j], acorda_bonus, capatTranspozon);
					//daca exista un drum de la alinierea 'j' din fisierul 2 la alinierea 'i' din fisierul 1, se creaza o legatura
					DrumIntreDouaAlinieri(alinieriFisier2->elements[j], alinieriFisier1->elements[i], acorda_bonus, capatTranspozon);
				}
			}
	}
}

void EliminaLegaturileIntreFisiere(VECTOR_ALINIERE *alinieriFisier)
{
	for (int i=0; i<alinieriFisier->size; i++)
	{
		//lista inlantuita care porneste de la prima legatura intre 2 alinieri din fisierul 'i'
		DISTANTA *listaDoarFisier = alinieriFisier->grafLegaturiInFisier[i];
		//lista inlantuita care contine in partea de inceput legaturi intre alinieri din fisierul 'i' si un alt fisier 
		DISTANTA *listaCompusa = alinieriFisier->elements[i]->legaturi;

		//distrugem lista compusa pana ajungem la prima legatura intre 2 alinieri din fisierul 'i'
		//(legaturile intre fisiere sunt in partea din fata a 'listaCompusa' si la coada sunt legaturile in interiorul fisierului)
		while (listaCompusa != listaDoarFisier)
		{
			DISTANTA *temp = listaCompusa;
			listaCompusa = listaCompusa->vecin_dreapta;
			free(temp);
		}

		//se retine in aliniere lista cu legaturi doar spre alte alinieri din acelasi fisier
		alinieriFisier->elements[i]->legaturi = listaDoarFisier;
	}
}

ALINIERE *cloneaza(ALINIERE *aliniere)
{
	ALINIERE *clona = (ALINIERE*)calloc(1,sizeof(ALINIERE));
	clona->pozitieQuery = aliniere->pozitieQuery;
	clona->pozitieGenom = aliniere->pozitieGenom;
	clona->lengthQuery = aliniere->lengthQuery;
	clona->lengthGenom = aliniere->lengthGenom;
	clona->pozitieNucleuQuery = aliniere->pozitieNucleuQuery;
	clona->pozitieNucleuGenom = aliniere->pozitieNucleuGenom;
	clona->lengthNucleu = aliniere->lengthNucleu;
	clona->length = aliniere->length;
	clona->score = aliniere->score;
	clona->out1 = (char*)calloc(aliniere->length, sizeof(char));
	clona->relation = (char*)calloc(aliniere->length, sizeof(char));
	clona->out2 = (char*)calloc(aliniere->length, sizeof(char));
	memcpy(clona->out1, aliniere->out1, aliniere->length);
	memcpy(clona->relation, aliniere->relation, aliniere->length);
	memcpy(clona->out2, aliniere->out2, aliniere->length);
	clona->fisierSursa = aliniere->fisierSursa;
	clona->isTransposon = aliniere->isTransposon;
	clona->isDirect = aliniere->isDirect;
	//legaturile nu se cloneaza
	clona->legaturi = aliniere->legaturi;
	clona->scoreNucleu = aliniere->scoreNucleu;
	return clona;
}

bool acceasiSolutie(REZULTAT_COMPUNERE *elementCurent, REZULTAT_COMPUNERE *solutieNoua)
{
	//daca nu contine acelasi numar de alinieri, nu are cum sa fie aceeasi
	if (elementCurent->size != solutieNoua->size)
		return false;
	
	//daca elementCurent are unul dintre seturile de rezultate sursa identic cu setul de rezultate sursa nr 1 din solutieNoua, atunci este 'true'
	bool setSursa1Duplicat = false;
	if ( (solutieNoua->fisierSursa1 == elementCurent->fisierSursa1 && solutieNoua->isFisierSursa1Direct == elementCurent->isFisierSursa1Direct) ||
		 (solutieNoua->fisierSursa1 == elementCurent->fisierSursa2 && solutieNoua->isFisierSursa1Direct == elementCurent->isFisierSursa2Direct) )
	{
		setSursa1Duplicat = true;
	}
	
	//daca elementCurent are unul dintre seturile de rezultate sursa identic cu setul de rezultate sursa nr 2 din solutieNoua, atunci este 'true'
	bool setSursa2Duplicat = false;
	if ( (solutieNoua->fisierSursa2 == elementCurent->fisierSursa1 && solutieNoua->isFisierSursa2Direct == elementCurent->isFisierSursa1Direct) ||
		 (solutieNoua->fisierSursa2 == elementCurent->fisierSursa2 && solutieNoua->isFisierSursa2Direct == elementCurent->isFisierSursa2Direct) )
	{
		setSursa2Duplicat = true;
	}
		
	//trebuie sa corespunda doar unul dintre seturile de rezultate sursa
	//daca sunt amandoua diferite, nu poate sa fie aceaesi solutie
	//daca corespund amandoua, nu are cum sa fie aceeasi solutie, datorita modului de calcul
	if ( (setSursa1Duplicat && setSursa2Duplicat) ||
		 (!setSursa1Duplicat && !setSursa2Duplicat)    )
	{
		 return false;
	}
	
	BUCATA_REZULTAT *bucataCurentaElementCurent = elementCurent->santinela_start;
	BUCATA_REZULTAT *bucataCurentaSolutieNoua = solutieNoua->santinela_start;
	
	while (bucataCurentaElementCurent != NULL && bucataCurentaSolutieNoua != NULL)
	{
		//solutiile duplicate contin alinieri ce provin dintr-un singur set de rezultate
		
		//se verifica mai intai ca cele 2 alinieri comparate sa fie din acelasi set de rezultate
		//daca alinierile care se compara acum sunt din seturi de rezultate diferite, nu este solutie duplicat
		if ( !(bucataCurentaSolutieNoua->interval->fisierSursa == bucataCurentaElementCurent->interval->fisierSursa &&
			   bucataCurentaSolutieNoua->interval->isDirect == bucataCurentaElementCurent->interval->isDirect) )
			return false;
			
		//daca ajunge aici, ambele alinieri sunt din acelasi set de rezultate, si se fac verificari doar pe alinierea din noua solutie
		
		//daca corespunde setul de rezultate sursa 1 intre cele 2 solutii si gasesc o aliniere din alt set de rezultate sursa, nu are cum sa fie aceeasi solutie
		if (setSursa1Duplicat && 
			!(bucataCurentaSolutieNoua->interval->fisierSursa == solutieNoua->fisierSursa1 && bucataCurentaSolutieNoua->interval->isDirect == solutieNoua->isFisierSursa1Direct) )
		{
			return false;
		}
		
		//daca corespunde setul de rezultate sursa 2 intre cele 2 solutii si gasesc o aliniere din alt set de rezultate sursa, nu are cum sa fie aceeasi solutie
		if (setSursa2Duplicat && 
			!(bucataCurentaSolutieNoua->interval->fisierSursa == solutieNoua->fisierSursa2 && bucataCurentaSolutieNoua->interval->isDirect == solutieNoua->isFisierSursa2Direct) )
		{
			return false;
		}
		
		//  daca ajunge aici inseamna ca alinierile bucataCurentaElementCurent, bucataCurentaSolutieNoua sunt amandoua
		//din acelasi set de rezultate sursa
		//  mai ramane sa verific daca reprezinta exact aceeasi aliniere
		if ( bucataCurentaElementCurent->interval->pozitieQuery != bucataCurentaSolutieNoua->interval->pozitieQuery || 
		     bucataCurentaElementCurent->interval->pozitieGenom != bucataCurentaSolutieNoua->interval->pozitieGenom ||
		     bucataCurentaElementCurent->interval->lengthQuery != bucataCurentaSolutieNoua->interval->lengthQuery ||
		     bucataCurentaElementCurent->interval->lengthGenom != bucataCurentaSolutieNoua->interval->lengthGenom ||
		     bucataCurentaElementCurent->interval->pozitieNucleuQuery != bucataCurentaSolutieNoua->interval->pozitieNucleuQuery ||
		     bucataCurentaElementCurent->interval->pozitieNucleuGenom != bucataCurentaSolutieNoua->interval->pozitieNucleuGenom ||
		     bucataCurentaElementCurent->interval->lengthNucleu != bucataCurentaSolutieNoua->interval->lengthNucleu ||
		     bucataCurentaElementCurent->interval->length != bucataCurentaSolutieNoua->interval->length ||
		     bucataCurentaElementCurent->interval->score != bucataCurentaSolutieNoua->interval->score ||
		     bucataCurentaElementCurent->interval->scoreNucleu != bucataCurentaSolutieNoua->interval->scoreNucleu)
		{
			return false;
		}
		
		bucataCurentaElementCurent = bucataCurentaElementCurent->vecin_dreapta;
		bucataCurentaSolutieNoua = bucataCurentaSolutieNoua->vecin_dreapta;
	}
	return true;
}

/**
 *	Functia va returna locatia de atasare a solutiei date ca parametru.
 *  Aceasta va fi atasata in stanga elementului intors de functie.
 *  Daca este intors NULL, atunci inseamna ca noua solutie va fi atasata la sfarsitul listei. In afara de situatia
 * in care este solutia cu cea mai mare distanta de pana acum, mai intoarce NULL si in cazul in care este prima solutie
 * adaugata in lista. 
*/
REZULTAT_COMPUNERE *gasesteLocatieAtasare(REZULTATE_FISIER *rezultate, int distantaTotala, REZULTAT_COMPUNERE *o_solutie)
{
	REZULTAT_COMPUNERE *locAtasare = rezultate->rezultate_fisier_start;
	while (locAtasare!=NULL && locAtasare->distanta < distantaTotala)
		locAtasare = locAtasare->vecin_dreapta;
	
	REZULTAT_COMPUNERE *elementCurent = locAtasare;
	//de la pozitia gasita, verificam toate elementele cu acceasi distanta totala pentru a testa unicitatea noii solutii
	while (elementCurent != NULL && elementCurent->distanta == distantaTotala)
	{
		if (acceasiSolutie(elementCurent, o_solutie))
		{
			locAtasare = &nuAtasaSolutie;
			break;
		}
		elementCurent = elementCurent->vecin_dreapta;
	}
	
	return locAtasare;
}

void atasazaLaListaRezultateFisier(REZULTATE_FISIER *rezultate, REZULTAT_COMPUNERE *locAtasare, REZULTAT_COMPUNERE *copie_solutie)
{
	rezultate->size++;
	//in cazul in care este null, se pune pe ultima pozitie
	//poate sa fie si primul element adaugat
	if (locAtasare == NULL)
	{
		//atunci este ultimul din lista
		if (rezultate->rezultate_fisier_sfarsit != NULL)
		{
			rezultate->rezultate_fisier_sfarsit->vecin_dreapta = copie_solutie;
			copie_solutie->vecin_stanga = rezultate->rezultate_fisier_sfarsit;
			copie_solutie->vecin_dreapta = NULL;
			rezultate->rezultate_fisier_sfarsit = copie_solutie;
		}
		//atunci este primul adagat in lista (in acest moment lista este goala)
		else
		{
			rezultate->rezultate_fisier_sfarsit = copie_solutie;
			rezultate->rezultate_fisier_start = copie_solutie;
			copie_solutie->vecin_stanga = NULL;
			copie_solutie->vecin_dreapta = NULL;
		}
	}
	//daca locul este diferit de NULL, se adauga in stanga lui
	else
	{
		//daca vecinul stanga este NULL, atunci se adauga pe prima pozitie si devine santinela de inceput
		if (locAtasare->vecin_stanga == NULL)
		{
			copie_solutie->vecin_stanga = NULL;
			copie_solutie->vecin_dreapta = rezultate->rezultate_fisier_start;
			rezultate->rezultate_fisier_start->vecin_stanga = copie_solutie;
			rezultate->rezultate_fisier_start = copie_solutie;
		}
		else
		//daca vecinul stanga este diferit de NULL, atunci se adauga intre el si locAtasare
		{
			//se face legatura intre noua solutie si vecinul din stanga
			locAtasare->vecin_stanga->vecin_dreapta = copie_solutie;
			copie_solutie->vecin_stanga = locAtasare->vecin_stanga;
			//se face legatura intre noua solutie si locAtasare
			locAtasare->vecin_stanga = copie_solutie;
			copie_solutie->vecin_dreapta = locAtasare; 
		}
	}
}

void elibereazaAliniere(ALINIERE *aliniere)
{
	free(aliniere->out1);
	free(aliniere->out2);
	free(aliniere->relation);
	free(aliniere);
}

void elibereazaRezultat(REZULTAT_COMPUNERE *solutie)
{
	BUCATA_REZULTAT *bucata_rezultat_curenta = solutie->santinela_start;
	while (bucata_rezultat_curenta != NULL)
	{
		BUCATA_REZULTAT *bucata_veche = bucata_rezultat_curenta;
		bucata_rezultat_curenta = bucata_rezultat_curenta->vecin_dreapta;
		elibereazaAliniere(bucata_veche->interval);
		free(bucata_veche);
	}
	free(solutie);
}

void eliminaUltimulRezultat(REZULTATE_FISIER *rezultate)
{
	//daca lista este goala, nu se face nimic
	if (rezultate->size == 0)
		return;
		
	rezultate->size--;
	//daca lista are un singur element, ramane goala
	if (rezultate->rezultate_fisier_start == rezultate->rezultate_fisier_sfarsit)
	{
		rezultate->rezultate_fisier_start->vecin_stanga = NULL;
		rezultate->rezultate_fisier_start->vecin_dreapta = NULL;
		elibereazaRezultat(rezultate->rezultate_fisier_start);
		rezultate->rezultate_fisier_start = NULL;
		rezultate->rezultate_fisier_sfarsit = NULL;
	}
	//daca lista avea mai multe elemente, se scoate ultimul
	else
	{
		REZULTAT_COMPUNERE *ultimul_element = rezultate->rezultate_fisier_sfarsit;
		rezultate->rezultate_fisier_sfarsit = rezultate->rezultate_fisier_sfarsit->vecin_stanga;
		rezultate->rezultate_fisier_sfarsit->vecin_dreapta = NULL;
		ultimul_element->vecin_stanga = NULL;
		elibereazaRezultat(ultimul_element);
	}
}

void adaugaRezultatLaSfarsitCompunere2(REZULTAT_COMPUNERE* rezultat_compunere, ALINIERE *aliniere)
{
	rezultat_compunere->size++;
	if (rezultat_compunere->santinela_start == NULL)
	{
		rezultat_compunere->santinela_start = (struct BUCATA_REZULTAT*)calloc(1,sizeof(struct BUCATA_REZULTAT));
		rezultat_compunere->santinela_start->interval = aliniere;
		rezultat_compunere->santinela_start->vecin_dreapta = NULL;
		rezultat_compunere->santinela_start->vecin_stanga = NULL;
		rezultat_compunere->santinela_sfarsit = rezultat_compunere->santinela_start;
		rezultat_compunere->scorNucleu = aliniere->scoreNucleu;
	}
	else
	{
		struct BUCATA_REZULTAT *element_nou = (struct BUCATA_REZULTAT*)calloc(1,sizeof(struct BUCATA_REZULTAT));
		element_nou->interval = aliniere;
		element_nou->vecin_dreapta = NULL;
		element_nou->vecin_stanga = rezultat_compunere->santinela_sfarsit;

		rezultat_compunere->scorNucleu += aliniere->scoreNucleu;

		rezultat_compunere->santinela_sfarsit->vecin_dreapta = element_nou;
		rezultat_compunere->santinela_sfarsit = element_nou;
	}
}

void adaugaLaRezultateFisier(REZULTATE_FISIER *rezultate, REZULTAT_COMPUNERE *o_solutie, int query_size)
{
	int distantaInPlus = query_size - (o_solutie->santinela_sfarsit->interval->pozitieNucleuQuery +
										o_solutie->santinela_sfarsit->interval->lengthNucleu);
	int distantaTotala = o_solutie->distanta + distantaInPlus;
	
	REZULTAT_COMPUNERE *locAtasare = NULL;
	bool okToAdd = false;
	bool eliminaUltimul = false;
	//daca numarul de solutii este mai mic decat numarul maxim de solutii dorite, atunci adaugarea se face indiferent de scor
	if (rezultate->size < maxResultCount)
	{
		//daca distanta este mai mica sau egala, noua solutie va fi introdusa in interiorul listei
		if (rezultate->rezultate_fisier_sfarsit != NULL && distantaTotala <= rezultate->rezultate_fisier_sfarsit->distanta)
		{
			locAtasare = gasesteLocatieAtasare(rezultate, distantaTotala, o_solutie);
			//folosim un element rezultat dummy care daca este returnat indica faptul ca solutia nu este unica
			//daca solutia nu este unica, nu este salvata
			if (locAtasare != &nuAtasaSolutie)
				okToAdd = true;
		}
		//daca distanta este mai mare sau daca este primul adaugat, atunci noua solutie va fi introdusa la sfarsit
		//solutia este sigur unica, avand in vedere ca are distanta mai mare decat restul solutiilor salvate pana acum 
		else
		{
			locAtasare = NULL;
			okToAdd = true;
		}
	}
	//  daca numarul de solutii este mai mic sau egal, atunci solutia este introdusa decat daca distanta este mai mica decat distanta
	//ultimului element din lista
	//  Ca rezultat al conditiei, daca avem mai multe rezultate cu acelasi scor si se depaseste numarul de solutii dorit, se vor pastra
	//doar primele gasite
	else if (distantaTotala < rezultate->rezultate_fisier_sfarsit->distanta)
	{
		locAtasare = gasesteLocatieAtasare(rezultate, distantaTotala, o_solutie);
		//folosim un element rezultat dummy care daca este returnat indica faptul ca solutia nu este unica
		//daca solutia nu este unica, nu este salvata
		if (locAtasare != &nuAtasaSolutie)
		{
			okToAdd = true;
			//  deja s-au strans numarul de solutii dorite; acum adaugam un rezultat mai bun decat cel cu cel mai prost scor si il elimian pe 
			//acela
			eliminaUltimul = true;
		}
	}
	
	if (okToAdd)
	{
		//creez o copie a solutiei gasite
		REZULTAT_COMPUNERE *copie_solutie = (REZULTAT_COMPUNERE*)calloc(1, sizeof(REZULTAT_COMPUNERE));
		BUCATA_REZULTAT *segment_curent = o_solutie->santinela_start;
		while (segment_curent != NULL)
		{
			adaugaRezultatLaSfarsitCompunere2(copie_solutie, cloneaza(segment_curent->interval));
			segment_curent = segment_curent->vecin_dreapta;
		}

		//  la sfarsit se adauga distanta care a mai ramas intre sfarsitul ultimului interval din solutie si sfarsitul
		//query-ului
		copie_solutie->distanta = o_solutie->distanta;
		copie_solutie->distanta += distantaInPlus;
	
		copie_solutie->fisierSursa1 = o_solutie->fisierSursa1;
		copie_solutie->isFisierSursa1Direct = o_solutie->isFisierSursa1Direct;
		copie_solutie->fisierSursa2 = o_solutie->fisierSursa2;
		copie_solutie->isFisierSursa2Direct = o_solutie->isFisierSursa2Direct;
	
		atasazaLaListaRezultateFisier(rezultate, locAtasare, copie_solutie);
		//  daca prin adaugarea elementului anterior s-a depasit numarul maxim de solutii dorite, se elimina cea mai
		//proasta solutie (ultima din lista inlantuita)
		if (eliminaUltimul)
			eliminaUltimulRezultat(rezultate);
	}
}

/**
	Se foloseste graful orientat cu legaturile intre alinieri pentru a cauta toate drumurile pana la frunze (alinierile
care nu au legaturi catre nici o alta aliniere). La formarea grafului au fost puse deja anumite restrictii 'locale' (care
tine cont doar de succesiunea a 2 alinieri) (vezi descrierea functiei 'CalculeazaDistanteInFisier').
	In afara de restrictiile 'locale' impuse la formarea grafului orientat, se mai impun acum si anumite restrictii 'globale' (care
tin cont de toata succesiunea de alinieri care formeaza solutia) :
- orice aliniere din genom adaugata la solutie trebuie sa respecte pozitionarea genomica fata de ultima aliniere genomica adaugata
 la solutie
- pentru alinierile din transposon se permite o singura anomalie in pozitionarea in transposon per solutie; dupa aceasta, orice
 aliniere din transposon adaugata trebuie sa respecte pozitionarea din transposon fata de ultima aliniere din transposon adaugata;
 Consideram ca aceasta anomalie de pozitionare are loc intre T1 si T2. Daca exista si T1 si T2, atunci anomalia de pozitionare
 intre ele este obligatorie (datorita tehnologiei folosite pentru secventiere/taiere).
- portiunile de transposon si genom folosite la acoperirea query-ului trebuie sa fie continue; se foloseste o masina cu 3 stari :
 	> porneste din starea T1 (transposon 1 - eventuala bucata de transposon ce acopera inceputul query-ului)
	> cand selecteaza prima bucata de genom trece in starea G (genom)
	> aflandu-se in starea G, cand selecteaza o aliniere din transposon, trece in starea T2 (tranposon 2 - eventuala bucata de 
	 transposon de la sfarsit)
	> starea T2 este finala si in aceasta poate selecta doar alinieri din transposon

	In momentul in care se ajunge la o frunza, drumul salvat pana la acel punct este adaugat la solutiile gasite pentru 
procesarea (fisierul genom + fisierul transposon) curenta.

@param aliniereCurenta
		Alinierea ce va fi luata ca punct de plecare la acest apel.
@param solutieCurenta
		Aici se construieste solutia. La fiecare apel recursiv al functiei 'cauta' este adaugata o noua aliniere.
@param rezultate
		Retine toate solutiile gasite pana acum pentru (fisierul genom + fisierul transposon) in curs de procesare.
@param query_size
		Marimea query-ului introdus de utilizator.
@param stare
		Indicator stare masina folosita pentru a asigura o acoperire TGT (transposon-genom-transposon) cu bucatile
	T si G continue (in T nu se gasesc alinieri din genom si in G nu se gasesc alinieri din transposon).
		Are 3 stari posibile : STARE_T1, STARE_G si STARE_T2. Vezi explicatia de mai sus pentru a intelege modul de
	functionare.
*/
void cauta(ALINIERE* aliniereCurenta, REZULTAT_COMPUNERE *solutieCurenta, REZULTATE_FISIER *rezultate, int query_size, int stare)
{
	//o aliniere pana la care "exista un drum"
	//se selecteaza initial prima destinatie din lista
	DISTANTA *destinatieUrmatoare = aliniereCurenta->legaturi;

	//daca 'destinatieUrmatoare' este NULL, atunci am ajuns la sfarsitul drumului pentru aceasta solutie si o notam
	if (destinatieUrmatoare == NULL)
	{
		adaugaLaRezultateFisier(rezultate, solutieCurenta, query_size);
	}
	else
	{
		//flag care specifica daca de la alinierea curenta s-a continuat pe cel putin un drum
		bool continuat = false;

		//daca destinatieUrmatoare este nenul, atunci se merge pe toate ramurile posibile
		while (destinatieUrmatoare != NULL)
		{
			ALINIERE *aliniereDestinatieUrmatoare = destinatieUrmatoare->punct_final;
			bool destinatieOK = true;
			int stareUrmatoare = -1;

			switch (stare)
			{
				//  se pot selecta alinieri si pe transposon, si pe genom
				//  daca se selecteaza o aliniere pe transposon, trebuie sa respecte ordinea in transposon fata
				//de ultima aliniere pe transposon adaugata la solutie
				case STARE_T1:
					if (aliniereDestinatieUrmatoare->isTransposon)
					{
						/*
						//daca nu respecta ordinea in transposon, destinatia este respinsa
						//daca se gaseste in T1, alinierea curenta este sigur pe transposon
						if (aliniereCurenta->pozitieNucleuGenom + aliniereCurenta->lengthNucleu - 1 >=
							aliniereDestinatieUrmatoare->pozitieNucleuGenom)
						{
							destinatieOK = false;
						}
						else
						{*/
							stareUrmatoare = STARE_T1;
							continuat = true;
						//}
					}
					else
					{
						//daca urmatoarea aliniere este pe genom, se trece in starea G
						stareUrmatoare = STARE_G;
						continuat = true;
					}
					break;
				//  se pot selecta alinieri pe genom si pe transposon
				//  alinierea pe genom trebuie sa respecte ordinea in genom fata de ultima aliniere pe genom adaugata
				//  alinierea pe transposon trebuie sa NU respecte ordinea in transposon fata de prima aliniere pe transposon adaugata
				case STARE_G:
					if (aliniereDestinatieUrmatoare->isTransposon)
					{
						//restrictii eliminate pentru ca transposonul se poate insera direct sau invers si nu avem de unde sa stim
						//in ce forma trebuie pusa restrictia
						/*
						ALINIERE *primaAliniereDinSolutie = solutieCurenta->santinela_start->interval;
						//  daca la inceputul query-ului avem o bucata de transposon, atunci se aplica restrictia legata de ordinea
						//in transposon
						if (primaAliniereDinSolutie->isTransposon)
						{
							//  nu se verifica cu sfarsitul alinierii destinatie pentru ca prin extindere este posibil sa apara
							//intersectii pe transposon intre primul interval din transposon ce acopera query-ul si ultimul 
							//interval din transposon ce acopera query-ul
							if (primaAliniereDinSolutie->pozitieNucleuGenom <= aliniereDestinatieUrmatoare->pozitieNucleuGenom)
							{
								destinatieOK = false;
							}
							else
							{
								stareUrmatoare = STARE_T2;
								continuat = true;
							}
						}
						//daca nu avem transposon la inceputul query-ului, nu se aplica restrictia (nu exista baza de comparatie)
						else
						{
							stareUrmatoare = STARE_T2;
							continuat = true;
						}
						*/
						stareUrmatoare = STARE_T2;
						continuat = true;
					}
					else
					{
						/*
						//daca nu respecta ordinea in genom, destinatia este respinsa
						//daca se gaseste in G, alinierea curenta este sigur pe genom
						if (aliniereCurenta->pozitieNucleuGenom + aliniereCurenta->lengthNucleu - 1 >=
							aliniereDestinatieUrmatoare->pozitieNucleuGenom)
						{
							destinatieOK = false;
						}
						else
						{
							stareUrmatoare = STARE_G;
							continuat = true;
						}
						*/
						
						//nu mai verific ordinea sau distanta genomica, deoarece sunt verificate atunci cand se construiesc drumurile intre alinieri
						stareUrmatoare = STARE_G;
						continuat = true;
					}
					break;
				//  se pot selecta doar alinieri pe transposon, care trebuie sa repecte ordinea in transposon fata de 
				//ultima aliniere pe transposon adaugata la solutie si inversiunea de pozitie fata de prima aliniere
				//de pe transposon din T1 (daca exista)
				case STARE_T2:
					if (aliniereDestinatieUrmatoare->isTransposon)
					{
						/*
						//daca nu respecta ordinea in transposon, destinatia este respinsa
						if (aliniereCurenta->pozitieNucleuGenom + aliniereCurenta->lengthNucleu - 1 >=
							aliniereDestinatieUrmatoare->pozitieNucleuGenom)
						{
							destinatieOK = false;
						}
						else
						{
						*/
							//  restrictii eliminate pentru ca transposonul se poate insera direct sau invers si nu avem de unde sa stim
							//in ce forma trebuie pusa restrictia
							/*
							ALINIERE *primaAliniereDinSolutie = solutieCurenta->santinela_start->interval;
							//  daca la inceputul query-ului avem o bucata de transposon, atunci se aplica restrictia legata 
							//de ordinea in transposon
							if (primaAliniereDinSolutie->isTransposon)
							{
								//  nu se verifica cu sfarsitul alinierii destinatie pentru ca prin extindere este posibil sa apara
								//intersectii pe transposon intre primul interval din transposon ce acopera query-ul si ultimul 
								//interval din transposon ce acopera query-ul
								if (primaAliniereDinSolutie->pozitieNucleuGenom <= aliniereDestinatieUrmatoare->pozitieNucleuGenom)
									destinatieOK = false;
								else
								{
									stareUrmatoare = STARE_T2;
									continuat = true;
								}
							}
							//daca nu avem transposon la inceputul query-ului, nu se aplica restrictia (nu exista baza de comparatie)
							else
							{
								stareUrmatoare = STARE_T2;
								continuat = true;
							}*/
							stareUrmatoare = STARE_T2;
							continuat = true;
						//}
					}
					else
						//in aceasta stare nu se mai accepta alinieri pe genom
						destinatieOK = false;
					break;
			}

			if (destinatieOK)
			{
				//intervalul urmator este adaugat inainte de a merge mai adanc
				//alinierea urmatoare este acum si ultima aliniere din solutia curenta
				int distantaAdaugata = adaugaRezultatLaSfarsitCompunere(solutieCurenta, destinatieUrmatoare->punct_final, destinatieUrmatoare);
				cauta(destinatieUrmatoare->punct_final, solutieCurenta, rezultate, query_size, stareUrmatoare);
				//dupa ce s-a procesat aceasta ramura, intervalul este eliminat din solutie
				EliminaUltimulElementDinCompunere(solutieCurenta, distantaAdaugata);
			}

			destinatieUrmatoare = destinatieUrmatoare->vecin_dreapta;
		}

		//  daca toate alinierile pana la care exista drum au fost respinse, alinierea curenta se considera frunza si 
		//solutia construita pana acum este adaugata la lista de solutii
		if (!continuat)
			adaugaLaRezultateFisier(rezultate, solutieCurenta, query_size);
	}

}

void printeazaInfoAliniere(ALINIERE *aliniere)
{
	char tip;
	if (aliniere->isTransposon)
		tip = 'T';
	else
		tip = 'G';

	printf("pozitieQuery = %d  pozitieGenom = %d  lengthQuery = %d  lengthGenom = %d  score = %d tip = %c\n",
					aliniere->pozitieQuery,
					aliniere->pozitieGenom,
					aliniere->lengthQuery,
					aliniere->lengthGenom,
					aliniere->score, tip);
}

OUTPUT_COMPUNERE* calculeazaOutputCompunere(REZULTAT_COMPUNERE *solutie_curenta, char *query, int query_size)
{
	char* linia_query = (char*)calloc(1,sizeof(char));
	char* linia_legatura = (char*)calloc(1,sizeof(char));
	char* linia_genom = (char*)calloc(1,sizeof(char));
	
	BUCATA_REZULTAT *bucata_curenta = solutie_curenta->santinela_start;
	//ultima pozitie din query printata
	int pozitie_curenta_printata = -1;
	int marime_curenta = 1;

	while (bucata_curenta != NULL)
	{
		ALINIERE *aliniere_curenta = bucata_curenta->interval;

		if (aliniere_curenta->pozitieQuery > pozitie_curenta_printata+1)
		{
			int marime_in_plus = aliniere_curenta->pozitieQuery - (pozitie_curenta_printata+1);
			linia_query = (char*)realloc(linia_query, (marime_curenta+marime_in_plus)*sizeof(char));
			linia_legatura = (char*)realloc(linia_legatura, (marime_curenta+marime_in_plus)*sizeof(char));
			linia_genom = (char*)realloc(linia_genom, (marime_curenta+marime_in_plus)*sizeof(char));

			strncat(linia_query, &(query[pozitie_curenta_printata+1]), marime_in_plus);
			memset(&(linia_legatura[marime_curenta-1]), ' ', marime_in_plus);
			memset(&(linia_genom[marime_curenta-1]), ' ', marime_in_plus);

			marime_curenta += marime_in_plus;
			pozitie_curenta_printata = aliniere_curenta->pozitieQuery - 1;

			linia_query[marime_curenta-1] = 0;
			linia_legatura[marime_curenta-1] = 0;
			linia_genom[marime_curenta-1] = 0;
		}

		linia_query = (char*)realloc(linia_query, (marime_curenta+aliniere_curenta->length)*sizeof(char));
		linia_legatura = (char*)realloc(linia_legatura, (marime_curenta+aliniere_curenta->length)*sizeof(char));
		linia_genom = (char*)realloc(linia_genom, (marime_curenta+aliniere_curenta->length)*sizeof(char));

		memcpy(&(linia_query[marime_curenta-1]), aliniere_curenta->out1, aliniere_curenta->length);
		memcpy(&(linia_legatura[marime_curenta-1]), aliniere_curenta->relation, aliniere_curenta->length);
		memcpy(&(linia_genom[marime_curenta-1]), aliniere_curenta->out2, aliniere_curenta->length);
		
		aliniere_curenta->outStringOffset = marime_curenta - 1;

		marime_curenta += aliniere_curenta->length;
		linia_query[marime_curenta-1] = 0;
		linia_legatura[marime_curenta-1] = 0;
		linia_genom[marime_curenta-1] = 0;

		pozitie_curenta_printata = aliniere_curenta->pozitieQuery + aliniere_curenta->lengthQuery - 1;
		bucata_curenta = bucata_curenta->vecin_dreapta;
	}

	//daca ultima aliniere nu ajunge pana la sfarsitul query-ului, atunci este completata linia de query
	if (pozitie_curenta_printata < query_size-1)
	{
		int marime_in_plus = query_size - pozitie_curenta_printata - 1;
		linia_query = (char*)realloc(linia_query, (marime_curenta+marime_in_plus)*sizeof(char));
		linia_legatura = (char*)realloc(linia_legatura, (marime_curenta+marime_in_plus)*sizeof(char));
		linia_genom = (char*)realloc(linia_genom, (marime_curenta+marime_in_plus)*sizeof(char));

		strncat(linia_query, &(query[pozitie_curenta_printata+1]), marime_in_plus);
		memset(&(linia_legatura[marime_curenta-1]), ' ', marime_in_plus);
		memset(&(linia_genom[marime_curenta-1]), ' ', marime_in_plus);

		marime_curenta += marime_in_plus;
		pozitie_curenta_printata = query_size-1;

		linia_query[marime_curenta-1] = 0;
		linia_legatura[marime_curenta-1] = 0;
		linia_genom[marime_curenta-1] = 0;
	}

	OUTPUT_COMPUNERE *output_compunere = (OUTPUT_COMPUNERE*)calloc(1, sizeof(OUTPUT_COMPUNERE));
	output_compunere->length = marime_curenta-1;
	output_compunere->out1 = linia_query;
	output_compunere->relation = linia_legatura;
	output_compunere->out2 = linia_genom;
	
	return output_compunere;
}

void PrinteazaRezultate(REZULTATE_FISIER *rezultate, char *query, int query_size, int numar_maxim_solutii)
{
	printf("--------------------------------------------------------------\n");
	printf("-------------------------Solutii (%d)-------------------------\n", rezultate->size);
	printf("--------------------------------------------------------------\n");
	printf("lungime query = %d\n",query_size);
	printf("--------------------------------------------------------------\n");

	REZULTAT_COMPUNERE *solutie_curenta = rezultate->rezultate_fisier_start;
	int max_cnt = numar_maxim_solutii;
	int current_cnt = 0;
	while (solutie_curenta != NULL && current_cnt < max_cnt)
	{
		current_cnt++;
		printf("distanta = %d\n",solutie_curenta->distanta);
		printf("pointer solutie = %p\n",solutie_curenta);
		if (solutie_curenta->isFisierSursa1Direct)
			printf("fisier genom sursa = %d (direct)\n", solutie_curenta->fisierSursa1);
		else
			printf("fisier genom sursa = %d (invers complementat)\n", solutie_curenta->fisierSursa1);

		if (solutie_curenta->isFisierSursa2Direct)
			printf("fisier transposon sursa = %d (direct)\n", solutie_curenta->fisierSursa2);
		else
			printf("fisier transposon sursa = %d (invers complementat)\n", solutie_curenta->fisierSursa2);
		BUCATA_REZULTAT *bucata_curenta = solutie_curenta->santinela_start;
		while (bucata_curenta != NULL)
		{	
			printeazaInfoAliniere(bucata_curenta->interval);
			
			bucata_curenta = bucata_curenta->vecin_dreapta;
		}

		OUTPUT_COMPUNERE* output_compunere = calculeazaOutputCompunere(solutie_curenta, query, query_size);

		printf("%s\n", output_compunere->out1);
		printf("%s\n", output_compunere->relation);
		printf("%s\n", output_compunere->out2); 
		free(output_compunere->out1);
		free(output_compunere->relation);
		free(output_compunere->out2);
		free(output_compunere);

		printf("--------------------------------------------------------------\n");
		solutie_curenta = solutie_curenta->vecin_dreapta;
	}
}

void writeGeneInfo(int fd, int geneIndex, struct INFORMATII_POZITIONARE* geneVector, struct FISIER_DATE* fisiere, int fisier_sursa)
{
		write(fd, geneVector[geneIndex].nume, MAX_INF_STR_LNG * sizeof(char));				//Numele genei
		write(fd, fisiere[fisier_sursa].titlu_fisier, MARIME_CONST_STRING * sizeof(char));	//Fisierul in care se afla
		write(fd, &geneVector[geneIndex].offset_start, sizeof(int));						//Startul genei
		write(fd, &geneVector[geneIndex].offset_stop, sizeof(int));							//Sfarsitul genei
		write(fd, geneVector[geneIndex].cyto, MAX_INF_STR_LNG * sizeof(char));				//Cytogenetic map
		write(fd, geneVector[geneIndex].cod, MAX_INF_STR_LNG * sizeof(char));				//Pentru drosophila, Flybase id
		write(fd, &geneVector[geneIndex].direction, sizeof(int));							//directia : F(+) sau R(-)
}

int getCurrentFilePos(int fd)
{
	return lseek(fd, 0, SEEK_CUR);
}

void setFilePos(int fd, int pos)
{
	lseek(fd, pos, SEEK_SET);
}

void scrieInFisier(int fd, REZULTATE_FISIER *rezultate, char *query, int query_size, char *query_name, 
					VECTOR_REZULTATE_PARTIALE *rezultate_partiale_directe, VECTOR_REZULTATE_PARTIALE *rezultate_partiale_complementare,
					struct FISIER_DATE* fisiere, char *ext_mem, int max_cnt, time_t timp_total_executie, int contorAlinieriCuNucleuPreaMic,
					int lungime_minima_nucleu)
{	
	//-----------------------------------------------------------------------------------------------
	//--------------Scriere pointeri catre zonele din fisier-----------------------------------------
	//-----------------------------------------------------------------------------------------------
	int ptrInfoQuery;			//Pointer catre InfoQuery
	int ptrFinalResultSet;  	//Pointer catre setul de rezultate finale
	int ptrPartialResultSet; 	//Pointer catre setul de rezultate partiale
	int ptrBestResult;			//Pointer catre indexul celui mai bun rezultat
	
	ptrInfoQuery = 0;
	ptrFinalResultSet = 0;
	ptrPartialResultSet = 0;
	ptrBestResult = 0;
	
	write(fd, &ptrInfoQuery, sizeof(int));
	write(fd, &ptrFinalResultSet, sizeof(int));
	write(fd, &ptrPartialResultSet, sizeof(int));
	write(fd, &ptrBestResult, sizeof(int));
	
	ptrInfoQuery = getCurrentFilePos(fd);
	//-----------------------------------------------------------------------------------------------
	//--------------Scriere informatii despre query--------------------------------------------------
	//-----------------------------------------------------------------------------------------------
	
	write(fd, query_name, MARIME_CONST_STRING * sizeof(char));	//nume query
	write(fd, &query_size, sizeof(int));						//marimea query-ului
	write(fd, query, query_size * sizeof(char));				//query-ul
	time_t timp = time(NULL);
	write(fd, &timp, sizeof(int));								//data la care s-a efectuat query-ul
	write(fd, &timp_total_executie, sizeof(int));				//numarul de secunde cat a durat executia
	int max_cnt_to_write = max_cnt;
	if (max_cnt < rezultate->size)
	{
		write(fd, &max_cnt, sizeof(int));						//numarul de rezultate finale
		max_cnt_to_write = max_cnt;
	}
	else
	{
		write(fd, &rezultate->size, sizeof(int));				//numarul de rezultate finale
		max_cnt_to_write = rezultate->size;
	}
	
	int total_rezultate_partiale_directe = 0;
	for (int i=0; i<rezultate_partiale_directe->size; i++)
	{
		VECTOR_ALINIERE *vector_alinieri_fisier_curent = rezultate_partiale_directe->elements[i];
		if (vector_alinieri_fisier_curent != NULL)
			total_rezultate_partiale_directe += vector_alinieri_fisier_curent->size;
	}
	
	int total_rezultate_partiale_complementare = 0;
	for (int i=0; i<rezultate_partiale_complementare->size; i++)
	{
		VECTOR_ALINIERE *vector_alinieri_fisier_curent = rezultate_partiale_complementare->elements[i];
		if (vector_alinieri_fisier_curent != NULL)
			total_rezultate_partiale_complementare += vector_alinieri_fisier_curent->size;
	}
	
	int total_rezultate_partiale = total_rezultate_partiale_directe + total_rezultate_partiale_complementare - contorAlinieriCuNucleuPreaMic;
	write(fd, &total_rezultate_partiale, sizeof(int));		//numarul de rezultate partiale
	
	ptrFinalResultSet = getCurrentFilePos(fd);
	//-----------------------------------------------------------------------------------------------
	//--------------Scriere FINAL RESULT SET---------------------------------------------------------
	//-----------------------------------------------------------------------------------------------
	
	write(fd, &max_cnt_to_write, sizeof(int));	//numarul de rezultate finale
	REZULTAT_COMPUNERE *solutie_curenta = rezultate->rezultate_fisier_start;
	int current_cnt = 0;
	while (solutie_curenta != NULL && current_cnt < max_cnt)
	{
		current_cnt++;
		solutie_curenta->distanta = query_size - solutie_curenta->distanta;
		write(fd, &solutie_curenta->distanta, sizeof(int));//Scorul obtinut din aliniere
		OUTPUT_COMPUNERE *output_compunere = calculeazaOutputCompunere(solutie_curenta, query, query_size);
		write(fd, &output_compunere->length, sizeof(int));//Marimea stringului de output (aliniere)
		write(fd, output_compunere->out1, output_compunere->length * sizeof(char));//partea de query
		write(fd, output_compunere->relation, output_compunere->length * sizeof(char));//relatia
		write(fd, output_compunere->out2, output_compunere->length * sizeof(char)); //partea de genom
		free(output_compunere->out1);
		free(output_compunere->relation);
		free(output_compunere->out2);
		free(output_compunere);
		write(fd, &solutie_curenta->size, sizeof(int));//Numarul de intervale din care este compus rezultatul
		BUCATA_REZULTAT *bucata_curenta = solutie_curenta->santinela_start;
		while (bucata_curenta != NULL)
		{
			struct ALINIERE *aliniere_curenta = bucata_curenta->interval;
			char *nume_fisier;
			bool isComplement;
			int fisier_sursa;
			
			fisier_sursa = aliniere_curenta->fisierSursa;
			nume_fisier = fisiere[aliniere_curenta->fisierSursa].titlu_fisier;
			isComplement = !aliniere_curenta->isDirect;
			
			write(fd, nume_fisier, MARIME_CONST_STRING * sizeof(char));//numele fisierului de origine
			write(fd, &aliniere_curenta->pozitieQuery, sizeof(int));//pozitia de start in query
			write(fd, &aliniere_curenta->pozitieGenom, sizeof(int));//pozitia de start in genom
			write(fd, &aliniere_curenta->lengthQuery, sizeof(int));//lungimea acoperita pe query
			write(fd, &aliniere_curenta->lengthGenom, sizeof(int));//lungimea acoperita pe genom
			write(fd, &aliniere_curenta->outStringOffset, sizeof(int));//Locatia corespunzatoare in stringul de aliniere
			write(fd, &aliniere_curenta->length, sizeof(int));//Lungimea corespunzatoare pe aliniere
			write(fd, &aliniere_curenta->isTransposon, sizeof(char));//1 daca e transposon, 0 daca nu
			write(fd, &isComplement, sizeof(bool));//1 daca e pe catena complementare, 0 daca nu
			
			//caut inside genes, upstream gene si downstream gene
			CLOSEST_GENES* closest_genes = findClosestGenes((struct INFORMATII_POZITIONARE*)(ext_mem+fisiere[fisier_sursa].offset_start_acc), 
																fisiere[fisier_sursa].acc_count, aliniere_curenta->pozitieGenom, 
																aliniere_curenta->pozitieGenom + aliniere_curenta->lengthGenom - 1,
																(int)isComplement);
			
			write(fd, &closest_genes->insideGeneNo, sizeof(char));//numarul de gene interioare gasite
			GENE_LLIST* geneList = (*closest_genes->insideGenes);
			//scriere informatii despre genele interioare
			while (geneList != NULL)
			{
				writeGeneInfo(fd, geneList->data, closest_genes->geneVector, fisiere, fisier_sursa);
				geneList = geneList->next;
			}
			
			char hasGene;
			if (closest_genes->downstreamGene != -1)
			{
				//are gena downstream
				hasGene = 1;
				write(fd, &hasGene, sizeof(char));
				//scrie informatiile despre gena downstream
				writeGeneInfo(fd, closest_genes->downstreamGene, closest_genes->geneVector, fisiere, fisier_sursa);
			}
			else
			{
				//nu are gena downstream
				hasGene = 0;
				write(fd, &hasGene, sizeof(char));
			}
			
			if (closest_genes->upstreamGene != -1)
			{
				//are gena upstream
				hasGene = 1;
				write(fd, &hasGene, sizeof(char));
				//scrie informatiile despre gena upstream
				writeGeneInfo(fd, closest_genes->upstreamGene, closest_genes->geneVector, fisiere, fisier_sursa);
			}
			else
			{
				//nu are gena upstream
				hasGene = 0;
				write(fd, &hasGene, sizeof(char));
			}
			closestGenesFree(closest_genes);
			bucata_curenta = bucata_curenta->vecin_dreapta;
		}
		solutie_curenta = solutie_curenta->vecin_dreapta;
	}
	
	ptrPartialResultSet = getCurrentFilePos(fd);
	//-----------------------------------------------------------------------------------------------
	//--------------Scriere PARTIAL RESULT SET-------------------------------------------------------
	//-----------------------------------------------------------------------------------------------
	
	write(fd, &total_rezultate_partiale, sizeof(int));		//numarul total de rezultate partiale
	//scriere rezultate partiale directe
	for (int i=0; i<rezultate_partiale_directe->size; i++)
	{
		VECTOR_ALINIERE *vector_alinieri_fisier_curent = rezultate_partiale_directe->elements[i];
		if (vector_alinieri_fisier_curent == NULL)
			continue;
			
		for (int j=0; j<vector_alinieri_fisier_curent->size; j++)
		{
			ALINIERE *aliniere_curenta = vector_alinieri_fisier_curent->elements[j];
			if (aliniere_curenta->lengthNucleu < lungime_minima_nucleu){
				continue;
			}
			write(fd, fisiere[i].titlu_fisier, MARIME_CONST_STRING * sizeof(char));			//nume fisier origine
			write(fd, &aliniere_curenta->pozitieQuery, sizeof(int));						//pozitia in query
			write(fd, &aliniere_curenta->pozitieGenom, sizeof(int));						//pozitia in genom
			write(fd, &aliniere_curenta->lengthQuery, sizeof(int));							//lungimea acoperita pe query
			write(fd, &aliniere_curenta->lengthGenom, sizeof(int));							//lungimea acoperita pe genom
			write(fd, &aliniere_curenta->score, sizeof(int));								//scorul
			write(fd, &aliniere_curenta->length, sizeof(int));								//Lungimea stringului aliniere
			write(fd, aliniere_curenta->out1, aliniere_curenta->length*sizeof(char));		//alinierea (partea de query)
			write(fd, aliniere_curenta->relation, aliniere_curenta->length*sizeof(char));	//relatia
			write(fd, aliniere_curenta->out2, aliniere_curenta->length*sizeof(char));		//alinierea (partea de genom)
			write(fd, &aliniere_curenta->isTransposon, sizeof(char));						//1 daca e transposon, 0 daca nu
			//scriere rezultate directe, deci nu este pe complementara
			bool isComplement = false;
			write(fd, &isComplement, sizeof(bool));											//1 daca e pe catena complementare, 0 daca nu
		}
	}
	//scriere rezultate partiale din fisierele inverse complementate
	for (int i=0; i<rezultate_partiale_complementare->size; i++)
	{
		VECTOR_ALINIERE *vector_alinieri_fisier_curent = rezultate_partiale_complementare->elements[i];
		if (vector_alinieri_fisier_curent == NULL)
			continue;
		
		for (int j=0; j<vector_alinieri_fisier_curent->size; j++)
		{
			ALINIERE *aliniere_curenta = vector_alinieri_fisier_curent->elements[j];
			if (aliniere_curenta->lengthNucleu < lungime_minima_nucleu){
				continue;
			}
			write(fd, fisiere[i].titlu_fisier, MARIME_CONST_STRING * sizeof(char));			//nume fisier origine
			write(fd, &aliniere_curenta->pozitieQuery, sizeof(int));						//pozitia in query
			write(fd, &aliniere_curenta->pozitieGenom, sizeof(int));						//pozitia in genom
			write(fd, &aliniere_curenta->lengthQuery, sizeof(int));							//lungimea acoperita pe query
			write(fd, &aliniere_curenta->lengthGenom, sizeof(int));							//lungimea acoperita pe genom
			write(fd, &aliniere_curenta->score, sizeof(int));								//scorul
			write(fd, &aliniere_curenta->length, sizeof(int));								//Lungimea stringului aliniere
			write(fd, aliniere_curenta->out1, aliniere_curenta->length*sizeof(char));		//alinierea (partea de query)
			write(fd, aliniere_curenta->relation, aliniere_curenta->length*sizeof(char));	//relatia
			write(fd, aliniere_curenta->out2, aliniere_curenta->length*sizeof(char));		//alinierea (partea de genom)
			write(fd, &aliniere_curenta->isTransposon, sizeof(char));						//1 daca e transposon, 0 daca nu
			//scriere rezultate din fisierele inverse complementate, deci sunt pe complementara
			bool isComplement = true;
			write(fd, &isComplement, sizeof(bool));											//1 daca e pe catena complementare, 0 daca nu
		}
	}
	
	ptrBestResult = getCurrentFilePos(fd);
	//-----------------------------------------------------------------------------------------------
	//--------------Scriere BEST RESULT--------------------------------------------------------------
	//-----------------------------------------------------------------------------------------------
	int zero = 0;
	if (max_cnt_to_write == 0)
	  zero = -1;
	write(fd, &zero, sizeof(int));
	
	//-----------------------------------------------------------------------------------------------
	//--------------Scriere pointeri catre zonele din fisier-----------------------------------------
	//valorile au fost obtinute pe masura ce am scris informatiile in fisier
	//-----------------------------------------------------------------------------------------------
	
	setFilePos(fd, 0);
	write(fd, &ptrInfoQuery, sizeof(int));
	write(fd, &ptrFinalResultSet, sizeof(int));
	write(fd, &ptrPartialResultSet, sizeof(int));
	write(fd, &ptrBestResult, sizeof(int));
}

void cautaFaraTransposoni(VECTOR_REZULTATE_PARTIALE *sursa_fisiere_genom, bool is_fisier_sursa_direct,
							REZULTATE_FISIER *rezultate_fisiere, int marime_query, int lungime_minima_nucleu)
{
	REZULTAT_COMPUNERE o_solutie;
	o_solutie.distanta = 0;
	o_solutie.size = 0;
	o_solutie.santinela_start = NULL;
	o_solutie.santinela_sfarsit = NULL;
	o_solutie.vecin_stanga = NULL;
	o_solutie.vecin_dreapta = NULL;
		
	o_solutie.isFisierSursa1Direct = is_fisier_sursa_direct;
	o_solutie.isFisierSursa2Direct = is_fisier_sursa_direct;
		
	for (int i=0; i<sursa_fisiere_genom->size; i++)
	{
		VECTOR_ALINIERE *rezultateFisierGenom = sursa_fisiere_genom->elements[i];
		if (rezultateFisierGenom == NULL)
			continue;
		
		o_solutie.fisierSursa1 = i;
		//nu are transposon sursa, pentru ca nu este incarcat nici un transposon
		o_solutie.fisierSursa2 = -1;
		
		for (int k=0; k<rezultateFisierGenom->size; k++)
			if (rezultateFisierGenom->elements[k]->lengthNucleu >= lungime_minima_nucleu)
			{
				//intervalul curent este adaugat inainte de a merge mai adanc
				int distantaAdaugata = adaugaRezultatLaSfarsitCompunere(&o_solutie, rezultateFisierGenom->elements[k], NULL);
				//daca prima aliniere selectata este pe genom, se ia ca stare de pornire G (si se considera ca nu exista o 
				//bucata de transposon la inceputul query-ului)
				cauta(rezultateFisierGenom->elements[k], &o_solutie, rezultate_fisiere, marime_query, STARE_G);
				//dupa ce s-a procesat aceasta ramura, intervalul este eliminat din solutie
				EliminaUltimulElementDinCompunere(&o_solutie, distantaAdaugata);
			}
	}
}

void combinaSiCauta(VECTOR_REZULTATE_PARTIALE *sursa1_fisiere, bool is_fisier_sursa1_direct,
					VECTOR_REZULTATE_PARTIALE *sursa2_fisiere, bool is_fisier_sursa2_direct,
					int startFisiereSursa1, int stopFisiereSursa1, int startFisiereSursa2, int stopFisiereSursa2,
					REZULTATE_FISIER *rezultate_fisiere, int marime_query, int lungime_minima_nucleu, bool acorda_bonus,
					struct FISIER_DATE* fisiere)
{
	REZULTAT_COMPUNERE o_solutie;
	o_solutie.distanta = 0;
	o_solutie.scorNucleu = 0;
	o_solutie.size = 0;
	o_solutie.santinela_start = NULL;
	o_solutie.santinela_sfarsit = NULL;
	o_solutie.vecin_stanga = NULL;
	o_solutie.vecin_dreapta = NULL;
	
	o_solutie.isFisierSursa1Direct = is_fisier_sursa1_direct;
	o_solutie.isFisierSursa2Direct = is_fisier_sursa2_direct;

	for (int i=startFisiereSursa1; i<=stopFisiereSursa1; i++)
	{
		VECTOR_ALINIERE *rezultateFisier1 = sursa1_fisiere->elements[i];
		if (rezultateFisier1 == NULL)
			continue;
		
		for (int j=startFisiereSursa2; j<=stopFisiereSursa2; j++)
		{
			VECTOR_ALINIERE *rezultateFisier2 = sursa2_fisiere->elements[j];
			if (rezultateFisier2 == NULL)
				continue;

			int capatTranspozon = 0;
			if (fisiere[i].isTransposon)
				capatTranspozon = fisiere[i].marime;
			else
				if (fisiere[j].isTransposon)
					capatTranspozon = fisiere[j].marime;
					
			calculeazaDistanteIntreRezultateFisiere(rezultateFisier1, rezultateFisier2, lungime_minima_nucleu, acorda_bonus, capatTranspozon);

			o_solutie.fisierSursa1 = i;
			o_solutie.fisierSursa2 = j;
			
			for (int k=0; k<rezultateFisier1->size; k++)
				if (rezultateFisier1->elements[k]->lengthNucleu >= lungime_minima_nucleu)
				{
					//daca prima aliniere selectata este pe genom, se ia ca stare de pornire G (si se considera ca nu exista o 
					//bucata de transposon la inceputul query-ului)
					//daca prima aliniere selectata este pe transposon, se ia ca stare de pornire T1
					int starePornire;
					if ( rezultateFisier1->elements[k]->isTransposon )
						starePornire = STARE_T1;
					else
						starePornire = STARE_G;
						
					//intervalul curent este adaugat inainte de a merge mai adanc
					int distantaAdaugata = adaugaRezultatLaSfarsitCompunere(&o_solutie, rezultateFisier1->elements[k], NULL);
					cauta(rezultateFisier1->elements[k], &o_solutie, rezultate_fisiere, marime_query, starePornire);
					//dupa ce s-a procesat aceasta ramura, intervalul este eliminat din solutie
					EliminaUltimulElementDinCompunere(&o_solutie, distantaAdaugata);
				}

			for (int k=0; k<rezultateFisier2->size; k++)
				if (rezultateFisier2->elements[k]->lengthNucleu >= lungime_minima_nucleu)
				{
					//daca prima aliniere selectata este pe genom, se ia ca stare de pornire G (si se considera ca nu exista o 
					//bucata de transposon la inceputul query-ului)
					//daca prima aliniere selectata este pe transposon, se ia ca stare de pornire T1
					int starePornire;
					if ( rezultateFisier2->elements[k]->isTransposon )
						starePornire = STARE_T1;
					else
						starePornire = STARE_G;
						
					//intervalul curent este adaugat inainte de a merge mai adanc
					int distantaAdaugata = adaugaRezultatLaSfarsitCompunere(&o_solutie, rezultateFisier2->elements[k], NULL);
					cauta(rezultateFisier2->elements[k], &o_solutie, rezultate_fisiere, marime_query, starePornire);
					//dupa ce s-a procesat aceasta ramura, intervalul este eliminat din solutie
					EliminaUltimulElementDinCompunere(&o_solutie, distantaAdaugata);
				}

			EliminaLegaturileIntreFisiere(rezultateFisier1);
			EliminaLegaturileIntreFisiere(rezultateFisier2);
		}
	}
}

void combinaSiCautaTransp(	VECTOR_REZULTATE_PARTIALE *sursa_fisiere_transposon, bool is_fisier_sursa_direct,
							int startFisiereTransposon, int stopFisiereTransposon,
							REZULTATE_FISIER *rezultate_fisiere, int marime_query, int lungime_minima_nucleu,
							bool acorda_bonus)
{
	REZULTAT_COMPUNERE o_solutie;
	o_solutie.distanta = 0;
	o_solutie.size = 0;
	o_solutie.santinela_start = NULL;
	o_solutie.santinela_sfarsit = NULL;
	o_solutie.vecin_stanga = NULL;
	o_solutie.vecin_dreapta = NULL;
	
	o_solutie.isFisierSursa1Direct = is_fisier_sursa_direct;
	o_solutie.isFisierSursa2Direct = is_fisier_sursa_direct;

	for (int i=startFisiereTransposon; i<=stopFisiereTransposon; i++)
	{
		VECTOR_ALINIERE *rezultateFisierTransposon1 = sursa_fisiere_transposon->elements[i];
		if (rezultateFisierTransposon1 == NULL)
			continue;
		
		for (int j=i+1; j<=stopFisiereTransposon; j++)
		{
			VECTOR_ALINIERE *rezultateFisierTransposon2 = sursa_fisiere_transposon->elements[j];
			if (rezultateFisierTransposon2 == NULL)
				continue;

			calculeazaDistanteIntreRezultateFisiere(rezultateFisierTransposon1, rezultateFisierTransposon2, lungime_minima_nucleu, acorda_bonus, 0);

			o_solutie.fisierSursa1 = i;
			o_solutie.fisierSursa2 = j;
			
			for (int k=0; k<rezultateFisierTransposon1->size; k++)
				if (rezultateFisierTransposon1->elements[k]->lengthNucleu >= lungime_minima_nucleu)
				{
					//intervalul curent este adaugat inainte de a merge mai adanc
					int distantaAdaugata = adaugaRezultatLaSfarsitCompunere(&o_solutie, rezultateFisierTransposon1->elements[k], NULL);
					//daca prima aliniere selectata este pe transposon, se ia ca stare de pornire T1
					cauta(rezultateFisierTransposon1->elements[k], &o_solutie, rezultate_fisiere, marime_query, STARE_T1);
					//dupa ce s-a procesat aceasta ramura, intervalul este eliminat din solutie
					EliminaUltimulElementDinCompunere(&o_solutie, distantaAdaugata);
				}

			for (int k=0; k<rezultateFisierTransposon2->size; k++)
				if (rezultateFisierTransposon2->elements[k]->lengthNucleu >= lungime_minima_nucleu)
				{
					//intervalul curent este adaugat inainte de a merge mai adanc
					int distantaAdaugata = adaugaRezultatLaSfarsitCompunere(&o_solutie, rezultateFisierTransposon2->elements[k], NULL);
					//daca prima aliniere selectata este pe transposon, se ia ca stare de pornire T1
					cauta(rezultateFisierTransposon2->elements[k], &o_solutie, rezultate_fisiere, marime_query, STARE_T1);
					//dupa ce s-a procesat aceasta ramura, intervalul este eliminat din solutie
					EliminaUltimulElementDinCompunere(&o_solutie, distantaAdaugata);
				}

			EliminaLegaturileIntreFisiere(rezultateFisierTransposon1);
			EliminaLegaturileIntreFisiere(rezultateFisierTransposon2);
		}
	}
}

/**
	Ajusteaza membrii alinierii pentru a scurta din partea dreapta cu cntScurtareQuery nucleotide.
	ATENTIE : NU se ocupa si de pozitieNucleuQuery, pozitieNucleuGenom, lengthNucleu.
*/
void scurteazaDinDreapta(ALINIERE *aliniere, int cntScurtareQuery, char* query, bool alinierePeDirect)
{
	char *pozCurentaOutQuery = &(aliniere->out1[aliniere->length-1]);
	char *pozCurentaOutGenom = &(aliniere->out2[aliniere->length-1]);
	//numarul de nucleotide scoase din aliniere pe genom nu este neaparat egal cu cele scoase din aliniere pe query
	int cntScurtareGenom = 0;
	
	//se incepe cu ultima pozitie a alinierii extinse
	//se opreste la prima pozitie taiata
	for (int i = aliniere->pozitieQuery + aliniere->lengthQuery - 1;
			i >= aliniere->pozitieQuery + aliniere->lengthQuery - cntScurtareQuery; i--)
	{
		//se ia nucleotida curenta din query
		char nucleotidaCurenta = query[i];
		
		while (nucleotidaCurenta != *pozCurentaOutQuery)
		{
			//daca nu este egala, este nepotrivire sau gap si adun 1 la scor (eliminand nepotrivirea, scorul creste);
			aliniere->score += 1;
			//lungimea scade cu 1
			aliniere->length -= 1;
			//ma deplasez la stanga in stringul alinierii pe query
			pozCurentaOutQuery--;
			//daca la pozitia oglindita in out2 se gaseste o nucleotida, creste numarul nucleotidelor scoase din alinierea pe genom
			if (*pozCurentaOutGenom != '-')
				cntScurtareGenom++;
			//ma deplasez la stanga in stringul alinierii pe genom
			pozCurentaOutGenom--;
		}
		//am gasit nucleotida si o elimin
		//daca a fost nepotrivire, se aduna 1
		if (aliniere->relation[aliniere->length-1] == ' ')
			aliniere->score += 1;
		else//daca a fost potrivire, se scade 2
			aliniere->score -= 2;
		aliniere->length -= 1;
		pozCurentaOutQuery--;
		//daca la pozitia oglindita in out2 se gaseste o nucleotida, creste numarul nucleotidelor scoase din alinierea pe genom
		if (*pozCurentaOutGenom != '-')
			cntScurtareGenom++;
		//ma deplasez la stanga in stringul alinierii pe genom
		pozCurentaOutGenom--;
	}
	
	//dupa ce au fost taiate toate nucleotidele cerute din partea dreapta, continui taierea pana intalnesc pe aliniere o nucleotida (adica elimin spre stanga toate '-')
	while (*pozCurentaOutQuery == '-')
	{
		//daca este gap, adun 1 la scor (eliminand nepotrivirea, scorul creste);
		aliniere->score += 1;
		//lungimea scade cu 1
		aliniere->length -= 1;
		//ma deplasez la stanga in stringul alinierii pe query
		pozCurentaOutQuery--;
		//daca la pozitia oglindita in out2 se gaseste o nucleotida, creste numarul nucleotidelor scoase din alinierea pe genom
		if (*pozCurentaOutGenom != '-')
			cntScurtareGenom++;
		//ma deplasez la stanga in stringul alinierii pe genom
		pozCurentaOutGenom--;
	}
	
	//aliniere->out1[aliniere->length] = 0;
	//aliniere->relation[aliniere->length] = 0;
	//aliniere->out2[aliniere->length] = 0;
	//se modifica si lengthQuery si lengthGenom
	aliniere->lengthQuery -= cntScurtareQuery;
	aliniere->lengthGenom -= cntScurtareGenom;
	//daca alinierea este pe catena complementara inversa, pe genom se scurteaza de fapt din stanga
	//trebuie mutata si pozitia de start a alinierii pe genom
	if (!alinierePeDirect)
		aliniere->pozitieGenom += cntScurtareGenom;
}

/**
	Ajusteaza membrii alinierii pentru a scurta din partea stanga cu cntScurtareQuery nucleotide.
	ATENTIE : NU se ocupa si de pozitieNucleuQuery, pozitieNucleuGenom, lengthNucleu.
*/
void scurteazaDinStanga(ALINIERE *aliniere, int cntScurtareQuery, char* query, bool alinierePeDirect)
{
	//printf("query = %s\n",query);
	//printf("query size = %d\n",strlen(query));
	//printf("aliniere->out1 = %s\n",aliniere->out1);
	//printf("cntScurtareQuery = %d\n",cntScurtareQuery);
	//pornim 'scurtarea' de la inceput
	char *pozCurentaOutQuery = aliniere->out1;
	char *pozCurentaRelation = aliniere->relation;
	char *pozCurentaOutGenom = aliniere->out2;
	//numarul de nucleotide scoase din aliniere pe genom nu este neaparat egal cu cele scoase din aliniere pe query
	int cntScurtareGenom = 0;
	int cntScurtareOutput = 0;
	//tot pentru 'aliniere' trebuie ajustat si intervalul extins
	for (int i=aliniere->pozitieQuery; i<aliniere->pozitieQuery + cntScurtareQuery; i++)
	{
		//se ia nucleotida curenta din query
		char nucleotidaCurenta = query[i];

		//se avanseaza pana se gaseste pe alinierea pe query nucleotida egala cu nucleotida urmatoare din query
		while (nucleotidaCurenta != *pozCurentaOutQuery)
		{
			//daca nu este egala, este nepotrivire si adun 1 la scor (eliminand nepotrivirea, scorul creste);
			aliniere->score += 1;
			//lungimea scade cu 1
			aliniere->length -= 1;
			//ma deplasez la dreapta in stringul de aliniere pe query
			pozCurentaOutQuery++;
			//ma deplasez la dreapta in stringul de legatura intre alinierile pe query si pe genom
			pozCurentaRelation++;
			//outputul este scurtat cu inca o nucleotida
			cntScurtareOutput++;
			//daca la pozitia oglindita in out2 se gaseste o nucleotida, creste numarul nucleotidelor scoase din alinierea pe genom
			if (*pozCurentaOutGenom != '-')
				cntScurtareGenom++;
			//ma deplasez la dreapta in stringul alinierii pe genom
			pozCurentaOutGenom++;
		}
		//am gasit nucleotida si o elimin
		//daca a fost nepotrivire, se aduna 1
		if (*pozCurentaRelation == ' ')
			aliniere->score += 1;
		else//daca a fost potrivire, se scade 2
			aliniere->score -= 2;
		aliniere->length -= 1;
		pozCurentaOutQuery++;
		pozCurentaRelation++;
		cntScurtareOutput++;
		//daca la pozitia oglindita in out2 se gaseste o nucleotida, creste numarul nucleotidelor scoase din alinierea pe genom
		if (*pozCurentaOutGenom != '-')
			cntScurtareGenom++;
		//ma deplasez la dreapta in stringul alinierii pe genom
		pozCurentaOutGenom++;
	}
	
	//dupa ce au fost taiate toate nucleotidele cerute din partea stanga, continui taierea pana intalnesc pe aliniere o nucleotida (adica elimin spre dreapta toate '-')
	while (*pozCurentaOutQuery == '-')
	{
		//daca este gap, adun 1 la scor (eliminand nepotrivirea, scorul creste)
		aliniere->score += 1;
		//lungimea scade cu 1
		aliniere->length -= 1;
		//ma deplasez la dreapta in stringul de aliniere pe query
		pozCurentaOutQuery++;
		//ma deplasez la dreapta in stringul de legatura intre alinierile pe query si pe genom
		pozCurentaRelation++;
		//outputul este scurtat cu inca o nucleotida
		cntScurtareOutput++;
		//daca la pozitia oglindita in out2 se gaseste o nucleotida, creste numarul nucleotidelor scoase din alinierea pe genom
		if (*pozCurentaOutGenom != '-')
			cntScurtareGenom++;
		//ma deplasez la dreapta in stringul alinierii pe genom
		pozCurentaOutGenom++;
	}

	//printf("cntScurtareOutput = %d\n",cntScurtareOutput);
	//printf("aliniere->length = %d\n",aliniere->length);
	memmove(aliniere->out1, &(aliniere->out1[cntScurtareOutput]), aliniere->length);
	memmove(aliniere->relation, &(aliniere->relation[cntScurtareOutput]), aliniere->length);
	memmove(aliniere->out2, &(aliniere->out2[cntScurtareOutput]), aliniere->length);
	//aliniere->out1[aliniere->length] = 0;
	//aliniere->relation[aliniere->length] = 0;
	//aliniere->out2[aliniere->length] = 0;

	aliniere->lengthQuery -= cntScurtareQuery;
	aliniere->lengthGenom -= cntScurtareGenom;
	aliniere->pozitieQuery += cntScurtareQuery;
	//  daca alinierea este pe catena complementara inversa, pe genom se scurteaza de fapt din dreapta, si nu se mai muta pozitia
	//de inceput a alinierii pe genom
	if (alinierePeDirect)
		aliniere->pozitieGenom += cntScurtareGenom;
}

int calculeazaMargineaDeTaiere(ALINIERE *primulInterval, ALINIERE *urmatorulInterval, char *query)
{
	//pozitia de pe query unde incepe intersectia; nu am voie sa mananc din nucleul alinierii'primulInterval'
	int pozitieStartIntersectie;
	if (primulInterval->pozitieNucleuQuery+primulInterval->lengthNucleu-1 < urmatorulInterval->pozitieQuery)
		pozitieStartIntersectie = urmatorulInterval->pozitieQuery;
	else
		pozitieStartIntersectie = primulInterval->pozitieNucleuQuery+primulInterval->lengthNucleu;

	//pozitia de pe query unde se termina intersectia; nu am voie sa mananc din nucleul alinierii'urmatorulInterval'
	int pozitieStopIntersectie;
	if (primulInterval->pozitieQuery+primulInterval->lengthQuery-1 < urmatorulInterval->pozitieNucleuQuery)
		pozitieStopIntersectie = primulInterval->pozitieQuery+primulInterval->lengthQuery-1;
	else
		pozitieStopIntersectie = urmatorulInterval->pozitieNucleuQuery - 1;

	//daca se intersecteaza pe un singur element, aceea este pozitia de taiere
	if (pozitieStartIntersectie == pozitieStopIntersectie)
		return pozitieStartIntersectie;
	//  daca inceputul este dupa sfarsit, inseamna ca 
	if (pozitieStartIntersectie > pozitieStopIntersectie)
	{
		//daca primul interval are pozitia de sfarsit a query-ului egala cu pozitia de sfarsit a nucleului,
		//taierea se face incepana de la (indexStopNucleu), primind toate nucleotidele si inclusiv pozitia
		//de taiere 
		if (primulInterval->pozitieQuery+primulInterval->lengthQuery-1 == 
				primulInterval->pozitieNucleuQuery+primulInterval->lengthNucleu-1)
			return primulInterval->pozitieQuery+primulInterval->lengthQuery-1;
		//daca intervalul al 2-lea are pozitia de start a query-ului egala cu pozitia de start a nucleului,
		//taierea se face de la nucleotida (indexStartNucleu - 1) - pentru ca primul interval va primi
		//toate nucleotidele pana la si inclusiv punctul de taiere
		if (urmatorulInterval->pozitieQuery == urmatorulInterval->pozitieNucleuQuery)
			return urmatorulInterval->pozitieQuery-1;
			
		//daca nucleele sunt unul in continuarea celuilalt, fara spatii intere ele, pozitia de taiere este sfarsitul
		//primului nucleu
		if (primulInterval->pozitieNucleuQuery + primulInterval->lengthNucleu == urmatorulInterval->pozitieNucleuQuery)
			return primulInterval->pozitieNucleuQuery + primulInterval->lengthNucleu - 1;
			
		printf("NU AM PRINS UN CAZ\n");
	}

	//pentru primul interval se porneste de la sfarsit
	char *pozCurentaOutputQueryPrimulInterval = &(primulInterval->out1[primulInterval->length-1]);
	char *pozCurentaOutputRelationPrimulInterval = &(primulInterval->relation[primulInterval->length-1]);
	int scorCurentPrimulInterval = primulInterval->score;
	//se scurteaza din partea dreapta pana se ajunge la pozitia de stop a intersectiei, calculandu-se scorul in acel punct
	//nu pot sa aleg un punct mai in dreapta pentru ca posibia de stop a intersectiei poate sa fie chiar ultima pozitie din primulInterval
	for (int i=primulInterval->pozitieQuery+primulInterval->lengthQuery-1; i>pozitieStopIntersectie; i--)
	{
		char nucleotidaCurenta = query[i];
		while (nucleotidaCurenta != *pozCurentaOutputQueryPrimulInterval)
		{
			//daca nu este egala, atunci este greseala si eliminand greseala, scorul creste
			scorCurentPrimulInterval += 1;
			pozCurentaOutputQueryPrimulInterval--;
			pozCurentaOutputRelationPrimulInterval--;
		}
		//in caz de mismatch, scorul creste cu 1
		if (*pozCurentaOutputRelationPrimulInterval == ' ')
			scorCurentPrimulInterval += 1;
		else//in caz de match, eliminand nucleotida, scorul scade cu 2
			scorCurentPrimulInterval -= 2;
		pozCurentaOutputQueryPrimulInterval--;
		pozCurentaOutputRelationPrimulInterval--;
	}

	//pentru urmatorul interval se porneste de la inceput
	char *pozCurentaOutputQueryUrmatorulInterval = urmatorulInterval->out1;
	char *pozCurentaOutputRelationUrmatorulInterval = urmatorulInterval->relation;
	int scorCurentUrmatorulInterval = urmatorulInterval->score;
	//se scurteaza din partea stanga pana se ajunge la pozitia de stop a intersectiei, calculandu-se scorul in acel punct
	for (int i = urmatorulInterval->pozitieQuery; i <= pozitieStopIntersectie; i++)
	{
		char nucleotidaCurenta = query[i];
		while (nucleotidaCurenta != *pozCurentaOutputQueryUrmatorulInterval)
		{
			//daca nu este egala, atunci este greseala si eliminand greseala, scorul creste
			scorCurentUrmatorulInterval += 1;
			pozCurentaOutputQueryUrmatorulInterval++;
			pozCurentaOutputRelationUrmatorulInterval++;
		}
		//in caz de mismatch, scorul creste cu 1
		if (*pozCurentaOutputRelationUrmatorulInterval == ' ')
			scorCurentUrmatorulInterval += 1;
		else//in caz de match, eliminand nucleotida, scorul scade cu 2
			scorCurentUrmatorulInterval -= 2;
		pozCurentaOutputQueryUrmatorulInterval++;
		pozCurentaOutputRelationUrmatorulInterval++;
	}
	
	int maxScore = -1;
	int margineaDeTaiere = -1;
	//avem calculate scorurile :
	//	- pentru primul interval scorul de la inceputul sau pana la pozitia de sfarsit a intersectiei (inclusiv)
	//	- pentru urmatorul interval scorul de la pozitia de sfarsit a intersectiei + 1 pana la sfarsitul sau
	//se parcurge toata lungimea intersectiei de la sfarsit la inceput si se selecteaza punctul in care scorul 
	//adunat este cel mai mare
	for (int i=pozitieStopIntersectie; i>=pozitieStartIntersectie; i--)
	{
		//verificam scorul si salvam maximul
		if (maxScore < scorCurentPrimulInterval + scorCurentUrmatorulInterval)
		{
			maxScore = scorCurentPrimulInterval + scorCurentUrmatorulInterval;
			margineaDeTaiere = i;
		}

		char nucleotidaCurenta = query[i];

		//pentru primul interval, mai elimin o nucleotida
		while (nucleotidaCurenta != *pozCurentaOutputQueryPrimulInterval)
		{
			//daca nu este egala, atunci este greseala si eliminand greseala, scorul creste
			scorCurentPrimulInterval += 1;
			pozCurentaOutputQueryPrimulInterval--;
			pozCurentaOutputRelationPrimulInterval--;
		}
		//in caz de mismatch, scorul creste cu 1
		if (*pozCurentaOutputRelationPrimulInterval == ' ')
			scorCurentPrimulInterval += 1;
		else//in caz de match, eliminand nucleotida, scorul scade cu 2
			scorCurentPrimulInterval -= 2;
		pozCurentaOutputQueryPrimulInterval--;
		pozCurentaOutputRelationPrimulInterval--;

		//pentru urmatorul interval, mai adaug o nucleotida
		while (nucleotidaCurenta != *pozCurentaOutputQueryUrmatorulInterval)
		{
			//daca nu este egala, atunci este greseala si eliminand greseala, scorul creste
			scorCurentUrmatorulInterval += 1;
			pozCurentaOutputQueryUrmatorulInterval--;
			pozCurentaOutputRelationUrmatorulInterval--;
		}
		//in caz de mismatch, scorul creste cu 1
		if (*pozCurentaOutputRelationUrmatorulInterval == ' ')
			scorCurentUrmatorulInterval += 1;
		else//in caz de match, eliminand nucleotida, scorul scade cu 2
			scorCurentUrmatorulInterval -= 2;
		pozCurentaOutputQueryUrmatorulInterval--;
		pozCurentaOutputRelationUrmatorulInterval--;
	}

	if (margineaDeTaiere < pozitieStartIntersectie || pozitieStopIntersectie < margineaDeTaiere )
	{
		printf("MEGA FAIL\n");
		printf("pozitieStartIntersectie = %d\n", pozitieStartIntersectie);
		printf("pozitieStopIntersectie = %d\n", pozitieStopIntersectie);
		printf("margineaDeTaiere = %d\n", margineaDeTaiere);
	}
	return margineaDeTaiere;
}

void calculeazaMargineaIntersectie(ALINIERE *primaAliniere, ALINIERE *urmatoareaAliniere, char *query,
									bool isPrimaAliniereDirecta, bool isUrmatoareaAliniereDirecta)
{
	//  mai intai se verifica daca nucleele se suprapun, caz in care toate nucleotidele din suprapunere sunt atribuite
	//alinierii care este transpozon
	if (urmatoareaAliniere->pozitieNucleuQuery <= primaAliniere->pozitieNucleuQuery + primaAliniere->lengthNucleu - 1)
	{
		int lngIntersectieNucleu = primaAliniere->pozitieNucleuQuery + primaAliniere->lengthNucleu - urmatoareaAliniere->pozitieNucleuQuery;
		//toate nucleotidele din suprapunere merg catre intervalul cu scorul cel mai mare
		//if (primaAliniere->score >= urmatoareaAliniere->score)
		//toate nucleotidele din suprapunere merg catre transposon
		if (primaAliniere->isTransposon)
		{
			//alinierea 'primaAliniere' pastreaza toate nucleotidele nucleului, dar se taie tot ce urmeaza in partea dreapta
			int cntScurtareQuery = (primaAliniere->pozitieQuery + primaAliniere->lengthQuery - 1) -
								   (primaAliniere->pozitieNucleuQuery + primaAliniere->lengthNucleu - 1);
			scurteazaDinDreapta(primaAliniere, cntScurtareQuery, query, isPrimaAliniereDirecta);

			//---------alinierea 'urmatoareaAliniere' pierde 'lngIntersectieNucleu' nucleotide din partea stanga----------------
			//(pe query pozitia de start a nucleului se muta oricum - doar pe genom se pune problema, in functie de orientare)
			urmatoareaAliniere->pozitieNucleuQuery += lngIntersectieNucleu;
			urmatoareaAliniere->lengthNucleu -= lngIntersectieNucleu;
			//din scorul nucleului se scade ca si cum in nucleu ar fi doar matchuri
			urmatoareaAliniere->scoreNucleu -= 2*lngIntersectieNucleu;
			//daca este pe catena directa, nucleul se scurteaza din partea stanga si se muta pozitia de inceput a acestuia mai la dreapta
			if ( isUrmatoareaAliniereDirecta )
			{
				urmatoareaAliniere->pozitieNucleuGenom += lngIntersectieNucleu;
			}

			//  va fi scurtat din partea stanga pana va ajunge la noua pozitie de start a nucleului, care va conincide cu
			//noua pozitie de start a alinierii extinse
			cntScurtareQuery = urmatoareaAliniere->pozitieNucleuQuery - urmatoareaAliniere->pozitieQuery;
			scurteazaDinStanga(urmatoareaAliniere, cntScurtareQuery, query, isUrmatoareaAliniereDirecta);
		}
		else
		{
			//alinierea 'primaAliniere' pierde in partea dreapta a nucleului nucleotidele aferente intersectiei
			//starturile nucleelor pe genom si query raman neschimbate; scade length-ul
			primaAliniere->lengthNucleu -= lngIntersectieNucleu;
			//din scorul nucleului se scade ca si cum in nucleu ar fi doar matchuri
			primaAliniere->scoreNucleu -= 2*lngIntersectieNucleu;
			//daca este pe catena inversa complementara, pierde din partea stanga, nu din dreapta si se muta pozitia de inceput pe genom
			if ( !isPrimaAliniereDirecta )
			{
				primaAliniere->pozitieNucleuGenom += lngIntersectieNucleu;
			}
			//pierde toate nucleotidele din dreapta noului sfarsit de nucleu
			int cntScurtareQuery = (primaAliniere->pozitieQuery + primaAliniere->lengthQuery - 1) -
								   (primaAliniere->pozitieNucleuQuery + primaAliniere->lengthNucleu - 1);
			scurteazaDinDreapta(primaAliniere, cntScurtareQuery, query, isPrimaAliniereDirecta);

			//----------alinierea 'urmatoareaAliniere' isi pastreaza nucleul intact, dar pierde toate nucleotidele din
			//----------partea stanga a acestuia

			//  va fi scurtat din partea stanga pana va ajunge la pozitia de start a nucleului, care va conincide cu
			//noua pozitie de start a alinierii extinse
			cntScurtareQuery = urmatoareaAliniere->pozitieNucleuQuery - urmatoareaAliniere->pozitieQuery;
			scurteazaDinStanga(urmatoareaAliniere, cntScurtareQuery, query, isUrmatoareaAliniereDirecta);
		}
	}
	else
	{
		//daca nucleele nu se suprapun, se verifica daca alinierile extinse se suprapun
		if (urmatoareaAliniere->pozitieQuery <= primaAliniere->pozitieQuery + primaAliniere->lengthQuery - 1)
		{
			/*
			printf("---------------------------------------\n");
			printf("info prima aliniere :\n");
			printeazaInfoAliniere(primaAliniere);
			printf("%s\n",primaAliniere->out1);
			printf("%s\n",primaAliniere->relation);
			printf("%s\n",primaAliniere->out2);
			printf("info urmatoarea aliniere :\n");
			printeazaInfoAliniere(urmatoareaAliniere);
			printf("%s\n",urmatoareaAliniere->out1);
			printf("%s\n",urmatoareaAliniere->relation);
			printf("%s\n",urmatoareaAliniere->out2);
			*/
			
			int pozitieQueryMargineTaiere;
			//daca primul este transpozon si urmatorul genom, atunci primul primeste tot, dar nu taie din nucleul celui de-al 2-lea
			if (primaAliniere->isTransposon && !urmatoareaAliniere->isTransposon)
			{
				/*   ce e aici comentat presupunea ca primul primeste tot-tot (ca extinderea nucleului sau poate sa taie din nucleul
				 * celui de-al 2-lea) 
				 * 
				 * 
				//daca se va manca din nucleul urmatoarei alinieri, atunci trebuie mutata pozitia de inceput si lungimea acestuia
				if ( primaAliniere->pozitieQuery + primaAliniere->lengthQuery - 1 >= urmatoareaAliniere->pozitieNucleuQuery )
				{
					int lngScurtareNucleu = primaAliniere->pozitieQuery + primaAliniere->lengthQuery - 1 - urmatoareaAliniere->pozitieNucleuQuery + 1;
					urmatoareaAliniere->pozitieNucleuQuery += lngScurtareNucleu;
					urmatoareaAliniere->lengthNucleu -= lngScurtareNucleu;
					if ( isUrmatoareaAliniereDirecta )
					{
						//daca este pe catena directa, nucleul pe genom se scurteaza din stanga si in afara de faptul ca se modifica lungimea, trebuie
						//mutat inceputul nucleului la dreapta
						urmatoareaAliniere->pozitieNucleuGenom += lngScurtareNucleu;
					}
					else {
						//daca este pe catena inversa complementara, nucleul pe genom se scurteaza din dreapta si se modifica doar lunigimea, nu si 
						//pozitia de inceput
					}
				}
				*/
				
				//daca primul se extinde peste nucleul celui de-al 2-lea, se restrictioneaza
				//cand se face taierea, prima aliniere pierde toate nucleotidele din dreapta marginii (nu pierde marginea)
				if ( primaAliniere->pozitieQuery + primaAliniere->lengthQuery - 1 >= urmatoareaAliniere->pozitieNucleuQuery )
					//setam marginea pe pozitia dinaintea nucleului celui de-al 2-lea
					pozitieQueryMargineTaiere = urmatoareaAliniere->pozitieNucleuQuery - 1;
				else
					//pentru a pastra toate nucleotidele, setam marginea pe pozitia de sfarsit
					pozitieQueryMargineTaiere = primaAliniere->pozitieQuery + primaAliniere->lengthQuery - 1;
			} 
			//daca urmatorul este transpozon si primul genom, atunci urmatorul primeste tot, dar nu taie din nucleul primului
			else if (!primaAliniere->isTransposon && urmatoareaAliniere->isTransposon)
			{
				/*   ce e aici comentat presupunea ca al 2-lea primeste tot-tot (extinderea nucleului celui de-al 2-lea poate
				 * sa taie din nucleul primului)
				 * 
				 * 
				//daca se va manca din nucleul primei alinieri, atunci trebuie modificata lungimea nucleului acesteia
				if (primaAliniere->pozitieNucleuQuery + primaAliniere->lengthNucleu - 1 >= urmatoareaAliniere->pozitieQuery )
				{
					int lngScurtareNucleu = primaAliniere->pozitieNucleuQuery + primaAliniere->lengthNucleu - 1 - urmatoareaAliniere->pozitieQuery + 1;
					primaAliniere->lengthNucleu -= lngScurtareNucleu;
					if ( isPrimaAliniereDirecta )
					{
						//daca prima aliniere se gaseste pe catena directa, atunci nucleul pe genom se va scurta din partea dreapta si modificam doar
						//lungimea acestuia
					}
					else
					{
						//daca prima aliniere se gaseste pe catena inversa complementara, atunci nucleul pe genom se va scurta din partea stanga, si in
						//afara de modificarea lungimii se muta si pozitia de inceput la dreapta
						primaAliniere->pozitieNucleuGenom += lngScurtareNucleu;
					}
				}
				*/
				
				//daca cea de-a 2-a se extinde peste nucleul primeia, se restrictioneaza
				//cand se face taierea, urmatoarea aliniere pierde toate nucleotidele din stanga marginii (inclusiv marginea)
				if (primaAliniere->pozitieNucleuQuery + primaAliniere->lengthNucleu - 1 >= urmatoareaAliniere->pozitieQuery )
					pozitieQueryMargineTaiere = primaAliniere->pozitieNucleuQuery + primaAliniere->lengthNucleu - 1;
				else
					//pentru a pastra a 2-a toate nucleotidele, setam marginea cu o unitate mai la stanga inceputului ei
					pozitieQueryMargineTaiere = urmatoareaAliniere->pozitieQuery - 1;
			}
			else
			{
				//calculam marginea de taiere intre cele 2 intervale
				//aceasta nu va taia din nucleele alinierilor
				pozitieQueryMargineTaiere = calculeazaMargineaDeTaiere(primaAliniere, urmatoareaAliniere, query);
			}
			//printf("pozitieQueryMargineTaiere = %d :\n", pozitieQueryMargineTaiere);
			if (pozitieQueryMargineTaiere == -1)
			{
				printf("FAIL\n");
			}

			//alinierea 'primaAliniere' pierde toate nucleotidele de dupa marginea de taiere (marginea de taiere ramane la ea)
			int cntScurtareQuery = (primaAliniere->pozitieQuery + primaAliniere->lengthQuery - 1) -
								   pozitieQueryMargineTaiere;
			//printf("cntScurtareQuery1 = %d :\n", cntScurtareQuery);
			scurteazaDinDreapta(primaAliniere, cntScurtareQuery, query, isPrimaAliniereDirecta);

			//alinierea 'urmatoareaAliniere' pierde toate nucleotidele dinainte de marginea de taiere (pierde si marginea de taiere)
			cntScurtareQuery = pozitieQueryMargineTaiere - urmatoareaAliniere->pozitieQuery + 1;
			//printf("cntScurtareQuery2 = %d :\n", cntScurtareQuery);
			scurteazaDinStanga(urmatoareaAliniere, cntScurtareQuery, query, isUrmatoareaAliniereDirecta);
		}
		else
		//daca nici acestea nu se suprapun, nu se face nimic, nefiind necesara stabilirea altor delimitari
		{
			return;
		}
	}
}

void copiazaDateRecalculate(ALINIERE *aliniereRecalculata, ALINIERE *aliniereSursa)
{
	aliniereSursa->pozitieQuery = aliniereRecalculata->pozitieQuery;	//offsetul alinieri primei secvente
	aliniereSursa->pozitieGenom = aliniereRecalculata->pozitieGenom;	//offsetul alinierii celei de-a doua secvente
	aliniereSursa->lengthQuery = aliniereRecalculata->lengthQuery;	//lungimea care s-a potrivit din query
	aliniereSursa->lengthGenom = aliniereRecalculata->lengthGenom;    //lungimea care s-a potrivit din genom
	//Informatii nucleu
	aliniereSursa->pozitieNucleuQuery = aliniereRecalculata->pozitieNucleuQuery;	//pozitia nucleului in query
	aliniereSursa->pozitieNucleuGenom = aliniereRecalculata->pozitieNucleuGenom;	//pozitia nucleului in genom
	aliniereSursa->lengthNucleu = aliniereRecalculata->lengthNucleu;	//lungime nucleu
	aliniereSursa->scoreNucleu = aliniereRecalculata->scoreNucleu;    //scorul SW al nucleului
	//Informatii de afisare
	aliniereSursa->length = aliniereRecalculata->length;		//lungimea secventei rezultat
	aliniereSursa->score = aliniereRecalculata->score;		//Scorul obtinut de aliniere
	aliniereSursa->out1 = aliniereRecalculata->out1;		//primul string aliniat
	aliniereSursa->relation = aliniereRecalculata->relation;	//relatia (liniutze)
	aliniereSursa->out2 = aliniereRecalculata->out2;		//al doilea string aliniat
}

/**
@param rezultate_partiale_directe
		Pointer catre o structura ce contine rezultatele partiale pe catena directa pentru toate fisierele.
@param rezultate_partiale_inverse
		Pointer catre o structura ce contine rezultatele partiale pe catena inversa complementata pentru toate fisierele.
@param marime_query
		Marimea query-ului introdus de utilizator.
@param fisiere
		Vector de structuri cu informatii despre fisierele procesate.
*/
REZULTATE_FISIER* CompuneRezultate(VECTOR_REZULTATE_PARTIALE *rezultate_partiale_directe, VECTOR_REZULTATE_PARTIALE *rezultate_partiale_complementare, 
									int marime_query, struct FISIER_DATE* fisiere, char *query, int lungime_minima_nucleu, int numar_maxim_solutii,
									bool acorda_bonus, char *mem, char *queryInversComplementat, int *contorAlinieriCuNucleuPreaMicDeReturnat)
{
	int i;
	int startFisiereTransposon = -1;
	maxResultCount = numar_maxim_solutii;
	//o structura care va retine toate solutiile din toate fisierele combinate
	REZULTATE_FISIER *rezultate_fisiere = (REZULTATE_FISIER *)calloc(1, sizeof(REZULTATE_FISIER));

	//caut indexul de start al fisierelor cu transposoni
	for (i=0; i<rezultate_partiale_directe->size; i++)
		if (fisiere[i].isTransposon)
		{
			startFisiereTransposon = i;
			break;
		}

	int contorAlinieriCuNucleuPreaMic = 0;
	//pentru fiecare fisiere calculez legaturile intre alinierile de pe catena directa
	for (i=0; i<rezultate_partiale_directe->size; i++)
	{
		//pentru rezultatele din transposoni, setez in alinieri flag-ul care indica acest lucru
		if ( i >= startFisiereTransposon && startFisiereTransposon != -1 )
		{
			VECTOR_ALINIERE *fisierCurent = rezultate_partiale_directe->elements[i];
			if (fisierCurent != NULL)
				for (int j=0; j < fisierCurent->size; j++)
				{
					fisierCurent->elements[j]->fisierSursa = i;
					fisierCurent->elements[j]->legaturi = NULL;
					fisierCurent->elements[j]->isTransposon = true;
					fisierCurent->elements[j]->isDirect = true;
					if (fisierCurent->elements[j]->lengthNucleu < lungime_minima_nucleu){
						contorAlinieriCuNucleuPreaMic++;
					}
				}
		}
		else
		{
			VECTOR_ALINIERE *fisierCurent = rezultate_partiale_directe->elements[i];
			if (fisierCurent != NULL)
				for (int j=0; j < fisierCurent->size; j++)
				{
					fisierCurent->elements[j]->fisierSursa = i;
					fisierCurent->elements[j]->legaturi = NULL;
					fisierCurent->elements[j]->isTransposon = false;
					fisierCurent->elements[j]->isDirect = true;
					if (fisierCurent->elements[j]->lengthNucleu < lungime_minima_nucleu){
						contorAlinieriCuNucleuPreaMic++;
					}
				}
		}
		//indecsii din 'grafLegaturiInFisier' corespund cu indecsii din 'rezultate_partiale_directe->elements[i]->elements'
		//( adica pe pozitia X in 'grafLegaturiInFisier' se va gasi o lista inlantuita cu legaturile pentru alinierea de la
		//  pozitia X din 'rezultate_partiale_directe->elements[i]->elements' )
		if (rezultate_partiale_directe->elements[i] != NULL)
			CalculeazaDistanteInFisier(rezultate_partiale_directe->elements[i],lungime_minima_nucleu, acorda_bonus);
	}

	//printf("dupa calcul legaturi in rezultatele de pe catena directa\n");

	//pentru fiecare fisiere calculez legaturile intre alinierile de pe catena inversa complementata
	for (i=0; i<rezultate_partiale_complementare->size; i++)
	{
		//pentru rezultatele din transposoni, setez in alinieri flag-ul care indica acest lucru
		if ( i >= startFisiereTransposon && startFisiereTransposon != -1 )
		{
			VECTOR_ALINIERE *fisierCurent = rezultate_partiale_complementare->elements[i];
			if (fisierCurent != NULL)
				for (int j=0; j < fisierCurent->size; j++)
				{
					fisierCurent->elements[j]->fisierSursa = i;
					fisierCurent->elements[j]->legaturi = NULL;
					fisierCurent->elements[j]->isTransposon = true;
					fisierCurent->elements[j]->isDirect = false;
					if (fisierCurent->elements[j]->lengthNucleu < lungime_minima_nucleu){
						contorAlinieriCuNucleuPreaMic++;
					}
				}
		}
		else
		{
			VECTOR_ALINIERE *fisierCurent = rezultate_partiale_complementare->elements[i];
			if (fisierCurent != NULL)
				for (int j=0; j < fisierCurent->size; j++)
				{
					fisierCurent->elements[j]->fisierSursa = i;
					fisierCurent->elements[j]->legaturi = NULL;
					fisierCurent->elements[j]->isTransposon = false;
					fisierCurent->elements[j]->isDirect = false;
					if (fisierCurent->elements[j]->lengthNucleu < lungime_minima_nucleu){
						contorAlinieriCuNucleuPreaMic++;
					}
				}
		}
		//indecsii din 'grafLegaturiInFisier' corespund cu indecsii din 'rezultate_partiale_inverse->elements[i]->elements'
		//( adica pe pozitia X in 'grafLegaturiInFisier' se va gasi o lista inlantuita cu legaturile pentru alinierea de la
		//  pozitia X din 'rezultate_partiale_inverse->elements[i]->elements' )
		if (rezultate_partiale_complementare->elements[i] != NULL)
			CalculeazaDistanteInFisier(rezultate_partiale_complementare->elements[i],lungime_minima_nucleu, acorda_bonus);
	}
	*contorAlinieriCuNucleuPreaMicDeReturnat = contorAlinieriCuNucleuPreaMic;
	//daca exista fisiere transpozon, atunci se fac cautarile, cupland 2 cate 2 rezultatele pe genom cu rezultatele pe transpozon
	//si mai luam si fisierele transpozon 2 cate 2
	if (startFisiereTransposon != -1)
	{
		//genom-transposon
		combinaSiCauta(rezultate_partiale_directe, true, rezultate_partiale_directe, true, 
						0, startFisiereTransposon-1, startFisiereTransposon, rezultate_partiale_directe->size-1,
						rezultate_fisiere, marime_query, lungime_minima_nucleu, acorda_bonus, fisiere);
		combinaSiCauta(rezultate_partiale_directe, true, rezultate_partiale_complementare, false, 
						0, startFisiereTransposon-1, startFisiereTransposon, rezultate_partiale_complementare->size-1, 
						rezultate_fisiere, marime_query, lungime_minima_nucleu, acorda_bonus, fisiere);
		combinaSiCauta(rezultate_partiale_complementare, false, rezultate_partiale_directe, true, 
						0, startFisiereTransposon-1, startFisiereTransposon, rezultate_partiale_directe->size-1, 
						rezultate_fisiere, marime_query, lungime_minima_nucleu, acorda_bonus, fisiere);
		combinaSiCauta(rezultate_partiale_complementare, false, rezultate_partiale_complementare, false,
						0, startFisiereTransposon-1, startFisiereTransposon, rezultate_partiale_complementare->size-1, 
						rezultate_fisiere, marime_query, lungime_minima_nucleu, acorda_bonus, fisiere);
						
		//transposon direct - transposon invers complementar
		combinaSiCauta(rezultate_partiale_directe, true, rezultate_partiale_complementare, false, 
						startFisiereTransposon, rezultate_partiale_directe->size-1, startFisiereTransposon, rezultate_partiale_complementare->size-1, 
						rezultate_fisiere, marime_query, lungime_minima_nucleu, acorda_bonus, fisiere);
		
		//transposon direct - transposon direct				
		combinaSiCautaTransp(rezultate_partiale_directe, true, 
							startFisiereTransposon, rezultate_partiale_directe->size-1,
							rezultate_fisiere, marime_query, lungime_minima_nucleu, acorda_bonus);
		//transposon invers complementar - transposon invers complementar
		combinaSiCautaTransp(rezultate_partiale_complementare, false,  
							startFisiereTransposon, rezultate_partiale_complementare->size-1,
							rezultate_fisiere, marime_query, lungime_minima_nucleu, acorda_bonus);
		
	}
	//daca nu exista transpozoni, se cauta direct in genom
	else
	{
		//ca idee, aici nu au cum sa apara solutii finale duplicate
		cautaFaraTransposoni(rezultate_partiale_directe, true, rezultate_fisiere, marime_query, lungime_minima_nucleu);
		cautaFaraTransposoni(rezultate_partiale_complementare, false, rezultate_fisiere, marime_query, lungime_minima_nucleu);
	}

	
	REZULTAT_COMPUNERE *solutie_curenta = rezultate_fisiere->rezultate_fisier_start;
	bool o_data = false;
	while (solutie_curenta != NULL)
	{
		BUCATA_REZULTAT *aliniere1 = solutie_curenta->santinela_start;
		BUCATA_REZULTAT *aliniere2 = aliniere1->vecin_dreapta;
		while (aliniere2 != NULL)
		{		
			calculeazaMargineaIntersectie(aliniere1->interval, aliniere2->interval, query, 
												aliniere1->interval->isDirect, aliniere2->interval->isDirect);
			if (o_data){
				printf("aliniere1->pozitieQuery = %d\n", aliniere1->interval->pozitieQuery);
				printf("aliniere1->lengthQuery = %d\n", aliniere1->interval->lengthQuery);
				o_data = false;

				char * pointerGenom = (char *) mem+fisiere[aliniere1->interval->fisierSursa].offset_start;
				int i;
				for( i = aliniere1->interval->pozitieGenom; i< aliniere1->interval->pozitieGenom+ aliniere1->interval->lengthGenom ; i++ ) {
					printf("%c",pointerGenom[i]);
				}
				printf("\n");
				printf("aliniere1->interval->fisierSursa = %d\n",aliniere1->interval->fisierSursa);
				printf("fisiere[aliniere1->interval->fisierSursa].nume_fisier = %s\n",fisiere[aliniere1->interval->fisierSursa].nume_fisier);
			}
			char *queryAliniere1 = query;
			if (!aliniere1->interval->isDirect){
				queryAliniere1 = queryInversComplementat;
				complementSWResult(aliniere1->interval, marime_query);
			}
			int modifStartAliniere1 = MODIF_START;
			if (aliniere1->interval->pozitieGenom < modifStartAliniere1)
				modifStartAliniere1 = aliniere1->interval->pozitieGenom;
			int modifLenAliniere1 = MODIF_LEN;
			if (aliniere1->interval->pozitieGenom + aliniere1->interval->lengthGenom+modifLenAliniere1 >= fisiere[aliniere1->interval->fisierSursa].marime)
				modifLenAliniere1 = fisiere[aliniere1->interval->fisierSursa].marime - (aliniere1->interval->pozitieGenom + aliniere1->interval->lengthGenom);
			ALINIERE *rezultat1 = sw_align(queryAliniere1 + aliniere1->interval->pozitieQuery,
											aliniere1->interval->pozitieQuery,
											aliniere1->interval->lengthQuery,
								  	  	   	(char*)(mem + fisiere[aliniere1->interval->fisierSursa].offset_start + (aliniere1->interval->pozitieGenom-modifStartAliniere1)),
								  	  	    aliniere1->interval->pozitieGenom-modifStartAliniere1,
								  	  	    aliniere1->interval->lengthGenom+modifLenAliniere1+modifStartAliniere1);
			if (!aliniere1->interval->isDirect){
				complementSWResult(rezultat1, marime_query);
			}
			copiazaDateRecalculate(rezultat1, aliniere1->interval);
			if (o_data){
				printf("aliniere2->pozitieQuery = %d\n", aliniere2->interval->pozitieQuery);
				printf("aliniere2->lengthQuery = %d\n", aliniere2->interval->lengthQuery);
				o_data = false;

				printf("aliniere2->interval->fisierSursa = %d\n",aliniere2->interval->fisierSursa);
				printf("fisiere[aliniere2->interval->fisierSursa].nume_fisier = %s\n",fisiere[aliniere2->interval->fisierSursa].nume_fisier);
			}

			char *queryAliniere2 = query;
			if (!aliniere2->interval->isDirect){
				queryAliniere2 = queryInversComplementat;
				complementSWResult(aliniere2->interval, marime_query);
			}
			int modifStartAliniere2 = MODIF_START;
			if (aliniere2->interval->pozitieGenom < modifStartAliniere2)
				modifStartAliniere2 = aliniere2->interval->pozitieGenom;
			int modifLenAliniere2 = MODIF_LEN;
			if (aliniere2->interval->pozitieGenom + aliniere2->interval->lengthGenom + modifLenAliniere2 >= fisiere[aliniere2->interval->fisierSursa].marime)
				modifLenAliniere2 = fisiere[aliniere2->interval->fisierSursa].marime - (aliniere2->interval->pozitieGenom + aliniere2->interval->lengthGenom);
			ALINIERE *rezultat2 = sw_align(queryAliniere2 + aliniere2->interval->pozitieQuery,
											aliniere2->interval->pozitieQuery,
											aliniere2->interval->lengthQuery,
											(char*)(mem+fisiere[aliniere2->interval->fisierSursa].offset_start+(aliniere2->interval->pozitieGenom-modifStartAliniere2)),
											aliniere2->interval->pozitieGenom-modifStartAliniere2,
											aliniere2->interval->lengthGenom+modifLenAliniere2+modifStartAliniere2);
			if (!aliniere2->interval->isDirect){
				complementSWResult(rezultat2, marime_query);
			}
			copiazaDateRecalculate(rezultat2, aliniere2->interval);

			aliniere1 = aliniere2;
			aliniere2 = aliniere2->vecin_dreapta;
		}
		solutie_curenta = solutie_curenta->vecin_dreapta;
	}

	//printf("inainte de sortare rezultate\n");
	//printeazaDistante(rezultate_fisiere);
	//printf("----------------------------------------------\n");
	//if (rezultate_fisiere->size > 0)
	//	Quicksort( rezultate_fisiere->rezultate_fisier_start, rezultate_fisiere->rezultate_fisier_sfarsit, rezultate_fisiere);
	//printf("dupa sortare rezultate\n");
	//printeazaDistante(rezultate_fisiere);
	//printf("----------------------------------------------\n");
	//printf("----------------------------------------------\n");

	return rezultate_fisiere;
}
