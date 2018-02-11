#include "../../headere/m1_hash_retrieval.h"

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

STRUCTURA_HASH* positionsForHash(unsigned int hashVal, int fd)
{
	//se pozitioneaza in fisier la locatia unde se gaseste valoarea offsetului la care sunt pozitiile
	//hashVal+1 pentru ca in afara de offseturi, in fisier se gaseste pe prima pozitie dimensiunea hash-ului
	//se inmulteste cu sizeof(unsigned int) pentru ca nu ne referim la octeti si la unsigned int
	if ( lseek(fd, (hashVal+1) * sizeof(unsigned int), SEEK_SET) == (off_t)-1)
		return NULL;
	unsigned int offsetValue = 0;
	//citesc offsetul de la care incep datele
	if ( read(fd, &offsetValue, sizeof(unsigned int)) == -1 )
		return NULL;
	//daca offsetul este 0, atunci pentru hash-ul respectiv nu se gaseste nimic in fisier
	if (offsetValue != 0)
	{
		//ma pozitionez la inceputul datelor; valoarea offsetului reprezinta pozitia absoluta in fisier unde se gasesc acestea
		if ( lseek(fd, offsetValue, SEEK_SET) == (off_t)-1 )
			return NULL;
		unsigned int numarValori = 0;
		if ( read(fd, &numarValori, sizeof(unsigned int)) == -1 )
			return NULL;
                
#ifdef USE_STD_VECTOR_FOR_HASH_POSITIONS
		STRUCTURA_HASH *structura = new STRUCTURA_HASH();
		structura->numar_aparitii = numarValori;
		unsigned long* positionsArray = new unsigned long[numarValori];
		if ( read(fd, positionsArray, numarValori * sizeof(unsigned long)) == -1 )
		{
			delete positionsArray;
			delete structura;
			return NULL;
		}
		structura->vector_pozitii.assign(positionsArray, positionsArray+numarValori);
		delete positionsArray;
#else
        STRUCTURA_HASH *structura = (STRUCTURA_HASH*)calloc(1, sizeof(STRUCTURA_HASH));
		if (structura == NULL)
			return NULL;
		structura->numar_aparitii = numarValori;
		structura->vector_pozitii = (unsigned long*)calloc(numarValori, sizeof(unsigned long));
		if ( structura->vector_pozitii == NULL )
		{
			free(structura);
			return NULL;
		}
		if ( read(fd, structura->vector_pozitii, numarValori * sizeof(unsigned long)) == -1 )
		{
			free(structura->vector_pozitii);
			free(structura);
			return NULL;
		}
#endif
                return structura;
	}
	return NULL;
}
