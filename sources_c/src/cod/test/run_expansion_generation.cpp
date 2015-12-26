/* 
 * File:   run_expansion_generation.cpp
 * Author: iulian
 *
 * Created on June 20, 2010, 12:49 PM
 */

#include <stdio.h>
#include <stdlib.h>
#include "../../headere/expansion_generation.h"
#include "../../headere/configuration.h"

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
 * Ruleaza expansion_generation pentru a genera tabela de expansiune
 */
int main(int argc,char** argv)
{ 
    int score_offset_zero; //constanta pentru locul care este considerat 0
    int score_match;	     //Bonus pentru potrivire
    int score_mismatch;

    if (argc < 3)
    {
      printf("Sintaxa este: ./run_expansion_generation filename value\n");
      exit(0);
    }
 
    //Initializez fisierul de configurare
    configurationInit();
    configurationReadFromFile();
    //Citesc parametrii
    score_offset_zero = atoi(configurationGetString((char *)"EXPANSION_OFFSET_ZERO"));
    score_match = atoi(configurationGetString((char *)"EXPANSION_SCORE_MATCH"));
    score_mismatch = atoi(configurationGetString((char *)"EXPANSION_SCORE_MISMATCH"));
    
    //Initializez matricea de expansiune    
    int n = atoi(argv[2]);
    struct EXP_RESULT_MATRIX* bigResult = initializeEXPResult(n);
    fillEXPResult(bigResult,score_offset_zero,score_match,score_mismatch);
    writeEXPResult2File(argv[1],bigResult);
    freeEXPResult(bigResult);

    //Eliberez fisierul de configurare
    configurationFree();

    return 0;
}
