/* 
 * File:   newmain.cpp
 * Author: iulian
 *
 * Created on June 20, 2010, 12:47 PM
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
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

/*
 * ruleaza modul4 (Smith-Waterman)
 */
int main(int argc,char** argv)
{

    if (argc < 3)
    {
      printf("Sintaxa este: ./run_modul4 secventa1 secventa2\n");
      exit(0);
    }

    struct ALINIERE* result = sw_align(argv[1],0,strlen(argv[1]),argv[2],0,strlen(argv[2]));
    printSWResult(result);
    freeSWResult(result);

    return 0;
}

