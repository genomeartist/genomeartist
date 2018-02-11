/* 
 * File:   comun.h
 * Author: iulian
 *
 * Created on June 21, 2010, 7:52 PM
 */
#ifndef _COMUN_H
#define	_COMUN_H

#define NUME_MEMORIE_PARTAJATA "/mem_partajata"
#define NUME_MEMORIE_PARTAJATA_POZITIONARE "/mem_partajata_pozitionare"
#define SHARED_MEMORY_KEEP_ALIVE_MODULE_NAME "GA_support_service"

//marimea maxima a unui string din structura cu informatii despre gene
#define MAX_INF_STR_LNG 64
//marimea maxima pentru numele unui fisier
#define MARIME_CONST_STRING 128

#define MARIME_STRUCTURA (sizeof(FISIER_DATE))
#define MARIME_STRUCTURA_ACC (sizeof(INFORMATII_POZITIONARE))

#define DIMENSIUNE_FEREASTRA 10
#define USE_STD_VECTOR_FOR_HASH_POSITIONS

#ifdef USE_STD_VECTOR_FOR_HASH_POSITIONS
#include <vector>
#endif


struct FISIER_DATE{
	char nume_fisier[MARIME_CONST_STRING];
	char cale_fisier[2*MARIME_CONST_STRING];
	char titlu_fisier[MARIME_CONST_STRING];
	unsigned long offset_start;
	unsigned long marime;
	unsigned long offset_start_acc;
	unsigned long acc_count;
	int isTransposon;
};

struct INFORMATII_POZITIONARE{
	char nume[MAX_INF_STR_LNG];
	char cod[MAX_INF_STR_LNG];
	unsigned long offset_start;
	unsigned long offset_stop;
	char cyto[MAX_INF_STR_LNG];
	int direction;
	unsigned long shadowPosition;
	int shadowGene;
};

/*
 * - un schelet hash va fi format dintr-un vector de astfel de structuri
 * - pentru o fereastra de X nucleotide, marimea scheletului va fi de 2^(2X) elemente
 * - numarul maxim de nucleotide pentru o fereastra este de 16 (o nucleotida se reprezinta pe 2 biti si int este pe 32 de biti)
 * - pentru a accesa informatiile stocate pentru o anumite secventa de nucleotide, aceasta se transforma in codificarea cu 2 biti/nucleotida
 *  , se obtine o valoare numerica P, si se acceseaza elementul de la pozitia P din scheletul hash-ului
 */
struct STRUCTURA_HASH{
	STRUCTURA_HASH() : numar_aparitii(0)
	{
	}
	unsigned int				numar_aparitii;
#ifdef USE_STD_VECTOR_FOR_HASH_POSITIONS 
	std::vector<unsigned long>	vector_pozitii;
#else
        unsigned long *vector_pozitii;
#endif
};

#ifdef _WIN32

class SharedMemoryKeepAliveService
{
public:
	static void Start();
	static void Stop();
};

#endif

#endif	/* _COMUN_H */

