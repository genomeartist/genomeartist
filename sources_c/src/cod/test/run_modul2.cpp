/* 
 * File:   run_modul2.cpp
 * Author: iulian
 *
 * Created on June 20, 2010, 3:45 PM
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "../../headere/m2_reuniune.h"
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

/*
 * Ruleaza un test pentru modul 2;
 */
int main(int argc,char** argv)
{
	if (argc < 3)
	{
	  printf("Sintaxa este: ./reuniune query fisier_hash\n");
	  exit(0);
	}

	MAPPING_INTERVAL* minterval = minterval_initialize(strlen(argv[1]));
	VECTOR_INTERVAL* vinterval = vinterval_initialize();

	int fd = open(argv[2], O_RDONLY);
	if (fd == -1)
	{
		fprintf(stdout,"Eroare la deschiderea fisierului de intrare cu numele %s\n", argv[2]);
		return -1;
	}

	proceseazaQuery(fd,argv[1],strlen(argv[1]),10,minterval,vinterval);
	minterval_print(minterval);
	printf("===========================\n");
	vinterval_print(vinterval);

        //Eliberez memoria
        minterval_free(minterval);
        vinterval_free(vinterval);

	return 0;
}

