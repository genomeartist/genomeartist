#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "../../headere/m4_smith-waterman.h"
#include "../../headere/structures_aliniere.h"
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

/**==========================================================
*  Alinieaza 2 secvente folosind algoritmul Smith-Waterman
*===========================================================*/
struct ALINIERE* sw_align(char* str1,int offset1, int length1, char* str2, int offset2, int length2)
{
    int i,j,k,k_seq1,k_seq2;
    int rows = length1 + 1;
    int cols = length2 + 1;
    int max,max2,max3,trace; 	//pentru maxim local
    int global_maximum = 0;	//pentru maxim global
    int global_row = 0,global_col = 0;  //pentru trace back
    int item,tip; //algoritmul de traceback
    int nucl_count, nucl_start, nucl_query, nucl_genom; //identificarea nucleului
    int nucl_score_start; //scorul pozitiei cele mai mari a nucleului

    //Matricile de date
    int* vector = (int*) malloc(rows*cols*sizeof(int));
    int* vector_trace = (int*) malloc(rows*cols*sizeof(int));
    int** matrix = (int**) malloc(rows*(sizeof(int*)));
    int** matrix_trace = (int**) malloc(rows*(sizeof(int*)));

    //structura pentru rezultate
    struct ALINIERE* result = (struct ALINIERE*) malloc(sizeof(struct ALINIERE));

	      //Afisez de test
	      //printf("offset1: %d, length1: %d, string1 :",offset1,length1);
	      //print_string(str1,length1);
	      //printf("offset2: %d, length2: %d, string2 :",offset2,length2);
	      //print_string(str2,length2);
    
    //Aloc matricea continuu
    matrix[0]=&vector[0];
    matrix_trace[0]=&vector_trace[0];
    for (i=1;i<rows;i++)
    {
      matrix[i] = &(vector[i*cols]);
      matrix_trace[i] = &(vector_trace[i*cols]);
     
      //initializez si valorile unde 2 = match, -1 mismatch
      for (j=1;j<cols;j++)
      {
	if (str1[i-1] == str2[j-1])
	    matrix[i][j] = MATCH;
	else matrix[i][j] = MISMATCH;
      }
    }

    //Initializez prima linie si coloana
    for (i=0;i<rows;i++)
    {
      matrix[i][0] = 0;
      matrix_trace[i][0] = TRACE_UP;
    }
    for (j=0;j<cols;j++)
    {
      matrix[0][j] = 0;
      matrix_trace[0][j] = TRACE_LEFT;
    }

    //Calculam scorurile folosind programarea dinamica
    for (i=1;i<rows;i++)
    {
      for (j=1;j<cols;j++)
      {
	//Calculez optiunile
	trace = TRACE_DIAG;
	max = matrix[i][j] + matrix[i-1][j-1];
	max2 = matrix[i][j-1] + MISMATCH;
	max3 = matrix[i-1][j] + MISMATCH;
	if ( max < max2 )
	{
	    max = max2;
	    trace = TRACE_LEFT;
	}
	if ( max < max3 )
	{
	    max = max3;
	    trace = TRACE_UP;
	}
	//Verific cu 0
	if ( max > 0 )
	{
	  matrix[i][j] = max;
	  if (max >= global_maximum)
	  {
	    global_maximum = max;
	    global_row = i;
	    global_col = j;
	  }
	}
	else
	{
	  matrix[i][j] = 0;
	  trace = TRACE_NONE;
	}

	//Pastrez referinta inapoi
	matrix_trace[i][j] = trace;
      }
    }

      //Afisez de test
      //print_mat(matrix,rows,cols);

    //Fac trace-back si reconstitui secventa
    //  dimensiunea maxima a stringului este length1+length2
    result->pozitieQuery = 0;
    result->pozitieGenom = 0;
    result->out1 = (char*) malloc((length1+length2)*sizeof(char));
    result->relation = (char*) malloc((length1+length2)*sizeof(char));
    result->out2 = (char*) malloc((length1+length2)*sizeof(char));

    //Fac traceback si compun rezultatul
    k = 0; //contorul de nucleotide din stringul rezultat
    k_seq1 = 0;	//contorul de nucleotide folosite din primul string
    k_seq2 = 0;	//contorul de nucleotide folosite din al doilea string
    //Identific si nucleul in timp ce fac traceback
    nucl_count = 0;		//nr de nucleotide continue
    nucl_start = 0;      	//pozitia maxima de la care s-a gasit minimul de nucleotide continue
    nucl_query = global_row;	//minimul pe query
    nucl_genom = global_col;    //minimul pe genom
    nucl_score_start = 0;			//scorul SW a pozitiei maxime din nucleu
    i = global_row;
    j = global_col;
    item = matrix[i][j];
    tip = matrix_trace[i][j];
    do
    {
	  //Obtin noul item
	  item = matrix[i][j];
	  tip = matrix_trace[i][j];
	  if ((i==0) || (j==0)) break;

	  //Scriu in rezultat
	  switch(tip) {
	    case (TRACE_DIAG):
	      if (str1[i-1] == str2[j-1])
	      {
			//match
			result->out1[k] = str1[i-1];
			result->relation[k] = CHAR_MATCH;
			result->out2[k] = str2[j-1];
			k++;
			k_seq1 ++;
			k_seq2 ++;
			  //count nucleus
			  nucl_count++;
			  if (nucl_count >= DIMENSIUNE_FEREASTRA) {
				//setez startul
				if (nucl_start == 0) {
					nucl_start = i + nucl_count;
					nucl_score_start = item + (nucl_count-1)*MATCH;
				}
				//setez end-ul
				nucl_query = i;
				nucl_genom = j;
			  }
	      } else {
			//mismatch
			result->out1[k] = str1[i-1];
			result->relation[k] = CHAR_MISMATCH;
			result->out2[k] = str2[j-1];
			k++;
			k_seq1 ++;
			k_seq2 ++;
			nucl_count = 0; //reset nucleus counts
	      }
	      break;
	    case (TRACE_UP):
	      result->out1[k] = str1[i-1];
	      result->relation[k] = CHAR_MISMATCH;
	      result->out2[k] = CHAR_MINUS;
	      k++;
	      k_seq1 ++;
	      nucl_count = 0; //reset nucleus counts
	      break;
	    case (TRACE_LEFT):
	      result->out1[k] = CHAR_MINUS;
	      result->relation[k] = CHAR_MISMATCH;
	      result->out2[k] = str2[j-1];
	      k++;
	      k_seq2 ++;
	      nucl_count = 0; //reset nucleus counts
	      break;
	    case (TRACE_NONE):
	      if (str1[i-1] == str2[j-1])
	      {
			//match
			result->out1[k] = str1[i-1];
			result->relation[k] = CHAR_MATCH;
			result->out2[k] = str2[j-1];
			k++;
			k_seq1 ++;
			k_seq2 ++;
			  //count nucleus
			  nucl_count++;
			  if (nucl_count >= DIMENSIUNE_FEREASTRA) {
				//setez startul
				if (nucl_start == 0) {
					nucl_start = i + nucl_count;
					nucl_score_start = item + (nucl_count-1)*MATCH;
				}
				//setez end-ul
				nucl_query = i;
				nucl_genom = j;
			  }
	      }
	      break;
	  }

	  //Calculez urmatoarea pozitie
	  switch (matrix_trace[i][j]) {
	    case (TRACE_DIAG):
		i--;
		j--;
		tip = TRACE_DIAG;
		break;
	    case (TRACE_UP):
		i--;
		tip = TRACE_UP;
		break;
	    case (TRACE_LEFT):
		j--;
		tip = TRACE_LEFT;
		break;
	    case (TRACE_NONE):
	        if (str1[i-1] == str2[j-1])
		{
		  //Incrementez doar daca a fost match
		  i--;
		  j--;
		}
		tip = TRACE_NONE;
		break;
	  }
    } while (item !=0 );

    //printf("row=%d,col=%d",global_row,global_col);
    //printf("nucl_start=%d,nucl_query=%d\n",nucl_start,nucl_query);

    //finalizez structura
    result->pozitieQuery = offset1 + i;
    result->pozitieGenom = offset2 + j;
    result->length = k;
    result->lengthQuery = k_seq1;
    result->lengthGenom = k_seq2;
    result->score = global_maximum;
    result->pozitieNucleuQuery = offset1 + nucl_query - 1;
    result->pozitieNucleuGenom = offset2 + nucl_genom - 1;
    result->lengthNucleu = nucl_start - nucl_query;	//lungime nucleu
    result->scoreNucleu = nucl_score_start - matrix[nucl_query][nucl_genom] + MATCH;
    result->isTransposon = 0;
    invertResult(result);

		//Metode de test
		//printSWResult(result);

    //eliberez memoria ocupata cu matricile
    free(vector);
    free(matrix);
    free(vector_trace);
    free(matrix_trace);
    return result;
}
