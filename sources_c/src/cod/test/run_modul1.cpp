/* 
 * File:   run_modul1.cpp
 * Author: iulian
 *
 * Created on June 21, 2010, 8:32 PM
 */

#include <stdlib.h>
#include <string.h>
#include "../../headere/m1_hash_retrieval.h"
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

/*
 * 
 */
int main(int argc, char **args)
{
	if (argc < 3)
	{
		fprintf(stdout,"Nu sunt destule argumente : [nume_fisier] [valoare_hash]\n");
		return -1;
	}
	int fd = open(args[1], O_RDONLY);
	if (fd == -1)
	{
		fprintf(stdout,"Eroare la deschiderea fisierului de intrare cu numele %s\n", args[1]);
		return -1;
	}

	uint hashCode = acgt2uint_inv(args[2],strlen(args[2]));
	STRUCTURA_HASH *rezultat = positionsForHash(hashCode, fd);
	if (rezultat == NULL)
		fprintf(stdout,"Nici un rezultat.\n");
	else
	{
		fprintf(stdout,"Am gasit %d rezultate\n", rezultat->numar_aparitii);
		fprintf(stdout,"Apasati o tasta\n");
		getchar();
		for (unsigned int i=0; i<rezultat->numar_aparitii; i++)
			fprintf(stdout,"%lu ", rezultat->vector_pozitii[i]);
		fprintf(stdout,"\nGATA !!!\n");
	}
	close(fd);
}

