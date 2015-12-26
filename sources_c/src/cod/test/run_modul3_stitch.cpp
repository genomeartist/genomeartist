/* 
 * File:   run_modul3.cpp
 * Author: iulian
 *
 * Created on June 20, 2010, 1:52 PM
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "../../headere/m3_stitch.h"

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
 * Ruleaza modulul 3. Algoritmul de extindere
 */
int main(int argc,char** argv)
{

    if (argc < 1)
    {
      printf("Sintaxa este: ./stitch\n");
      exit(0);
    }

    VECTOR_EXPANDED* vexpanded; //Vector de intervale extinse
    MAPPING_EXPANDED* mexpanded; //Maparea intervalelor unificate pe query
    EXPAND_RESULT* expanded_result; //Un rezultat extins;
    
    //Initializam structurile
    /*Recreez structura:
	query = 10;
	[1 ][1,2 ][1,2 ][1,2 ][2 ][3 ][3 ][3 ][3 ][ ]
    */
    vexpanded = vexpanded_initialize();
    mexpanded = mexpanded_initialize(10);
    expanded_result = (EXPAND_RESULT*) malloc(sizeof(EXPAND_RESULT));
      expanded_result->offset1 = 0;expanded_result->length1 = 5;
      expanded_result->offset2 = 10;expanded_result->length2 = 5;
    vexpanded_addExpanded(expanded_result,vexpanded);
    expanded_result = (EXPAND_RESULT*) malloc(sizeof(EXPAND_RESULT));
      expanded_result->offset1 = 4;expanded_result->length1 = 5;
      expanded_result->offset2 = 16;expanded_result->length2 = 5;
    vexpanded_addExpanded(expanded_result,vexpanded);
    
    //Efectuez prelucrarea asupra vectorului de expanded
    stitchTheExpanded(vexpanded,mexpanded);
    
    //Afisez rezultatele
    vexpanded_print(vexpanded);
    printf("=====================\n");
    mexpanded_print(mexpanded);

    //Eliberez memorie
    mexpanded_free(mexpanded);
    vexpanded_free(vexpanded);
    return 0;
}

