/* 
 * File:   run_configuration.cpp
 * Author: iulian
 *
 * Created on June 20, 2010, 12:49 PM
 */

#include <stdio.h>
#include <stdlib.h>
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
    if (argc < 1)
    {
      printf("Sintaxa este: ./run_configuration\n");
      exit(0);
    }

    configurationInit();
    configurationReadFromFile();
    
    char cheie[128]  = "EXPANSION_LENGTH_MODIFIER";
    printf("valoare pentru %s = %s\n",cheie,configurationGetString(cheie));
    
    configurationFree();

    return 0;
}
