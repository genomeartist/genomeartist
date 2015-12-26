/* 
 * File:   run_modul3.cpp
 * Author: iulian
 *
 * Created on June 20, 2010, 1:52 PM
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "../../headere/configuration.h"
#include "../../headere/m3_expand.h"

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
    int length_modifier; //Parametru ce precizeaza importanta lungimii in extindere
    
    if (argc < 8)
    {
      printf("Sintaxa este: ./expand filename string1 offset1 length1 string2 offset2 length2\n");
      exit(0);
    }

    int offset1,length1,offset2,length2;
    char* string1;
    char* string2;

    string1 = argv[2];
    offset1 = atoi(argv[3]);
    length1 = atoi(argv[4]);
    string2 = argv[5];
    offset2 = atoi(argv[6]);
    length2 = atoi(argv[7]);
 
    //Citesc fisierul de configurare
    configurationInit();
    configurationReadFromFile();
    length_modifier = atoi(configurationGetString((char*)"EXPANSION_LENGTH_MODIFIER"));
    printf("length_mod= %d\n",length_modifier);

    //Citesc matricea din fisier
    struct EXP_RESULT_MATRIX* expansion_table = readEXPResultFromFile(argv[1]);
    printf("n= %d\n",expansion_table->n);
    printf("offset1= %d, length1 = %d, size1= %d \n string1 = %s\n",offset1,length1,strlen(string1),string1);
    printf("offset2= %d, length2 = %d, size2= %d \n string1 = %s\n",offset2,length2,strlen(string2),string2);

    struct EXPAND_RESULT* result = expandStrings(expansion_table,length_modifier,
	   string1,offset1,length1,strlen(string1),
	   string2,offset2,length2,strlen(string2));

    printf("offset1= %lu, length1= %lu\n",result->offset1,result->length1);
    printf("offset2= %lu, length2= %lu\n",result->offset2,result->length2);

    //eliberez memoria asociata unui rezultat
    freeEXPResult(expansion_table);
    free(result);

    //Eliberez fisierul de configurare
    configurationFree();
   
    return 0;
}

