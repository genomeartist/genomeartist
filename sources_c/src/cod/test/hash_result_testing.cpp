#include <sys/types.h>
#include <unistd.h>
#include <stdio.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdlib.h>

#include "../../headere/utils.h"
#include "../../headere/comun.h"

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

int main(int argc, char **args)
{
	int fd = open(args[1], O_RDONLY);
	if (fd == -1)
	{
		fprintf(stdout,"Fisierul dat ca parametru nu poate fi deschis\n");
		return -1;
	}
	
	int dimensiuneScheletHash;
	read(fd, &dimensiuneScheletHash, sizeof(unsigned int));
	
	int *aparitii = (int *)calloc(dimensiuneScheletHash, sizeof(unsigned int));
	//mai intai citesc offseturile informatiilor
	read(fd, aparitii, dimensiuneScheletHash*sizeof(unsigned int));
	for (int i=0; i< dimensiuneScheletHash; i++)
	{
		if (aparitii[i] == 0)
			continue;
			
		//ma pozitionez in fisier la inceputul informatiile despre combinatia curenta
		lseek(fd, aparitii[i], SEEK_SET);
		//suprascriu offsetul informatiilor despre combinatia curenta cu numarul de aparitii al combinatiei
		read(fd, &aparitii[i], sizeof(unsigned int));
	}
	close(fd);
	
	FILE *fisierIesire = fopen(args[2], "w");
	for (int i=0; i<dimensiuneScheletHash; i++)
	{
		char *secventa = uint2acgt(i, 10);
		fprintf(fisierIesire,"%s %d\n",secventa, aparitii[i]);
	}
	fclose(fisierIesire);
}
