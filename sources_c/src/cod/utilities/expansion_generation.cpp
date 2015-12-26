//Include-urile standard
#include <stdio.h>
#include <stdlib.h>
#include <string.h> 

#include "../../headere/expansion_generation.h"
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

/**==============================================
*  Printeaza o structura rezultat
*===============================================*/
void print_result(struct SW_DECISION* result) {
  printf("start_pos = %d, length = %d, score = %d\n",
	 result->start_pos,result->length,result->score);
  switch(result->decision) {
    case DECISION_PUSH_UP:
      printf("PUSH UP\n");
      break;
    case DECISION_PUSH_BOTH:
      printf("PUSH BOTH\n");
      break;
    case DECISION_PUSH_DOWN:
      printf("PUSH DOWN");
      break;
    default:
      break;
  }
  print_string(result->out1,result->length);
  print_string(result->relation,result->length);
  print_string(result->out2,result->length);
  printf("=================================\n");
}

/**==============================================
*  Algoritmul va face matchul invers, pentru a
*    prezenta rezultatul, el trebuie inversat
*===============================================*/
void invertDecisionResult(struct SW_DECISION* result) {
  int i,n;
  char phony;
  n = result->length-1;

  //Inversez stringurile
  for (i=0; i < (result->length/2);i++)
  {
      //out1
      phony = result->out1[i];
      result->out1[i] = result->out1[n-i];
      result->out1[n-i] = phony;
      //relation
      phony = result->relation[i];
      result->relation[i] = result->relation[n-i];
      result->relation[n-i] = phony;
      //relation
      phony = result->out2[i];
      result->out2[i] = result->out2[n-i];
      result->out2[n-i] = phony;
  }
}

/**==========================================================
*  Alinieaza 2 secvente folosind algoritmul Smith-Waterman
*===========================================================*/
struct SW_DECISION* sw_align(char* string1,int len1, char* string2, int len2, int padding) {
    int i,j,k;
    int length1 = len1 + 2*padding;
    int length2 = len2 + 2*padding;
    int rows = length1 + 1;
    int cols = length2 + 1;
    int max,max2,max3,trace; 	//pentru maxim local
    int global_maximum = 0;	//pentru maxim global
    int global_row = 0,global_col = 0;  //pentru trace back
    int item,tip; //algoritmul de traceback

    //Fac noile stringuri cu padding inclus
    char* str1 = (char*) malloc(length1*sizeof(char));
    char* str2 = (char*) malloc(length2*sizeof(char));

    //Matricile de date
    int* vector = (int*) malloc(rows*cols*sizeof(int));
    int* vector_trace = (int*) malloc(rows*cols*sizeof(int));
    int** matrix = (int**) malloc(rows*(sizeof(int*)));
    int** matrix_trace = (int**) malloc(rows*(sizeof(int*)));

    //Adaug paddingul
    for (i=0;i<padding;i++)
    {
      //Str1
      str1[i] = PADDING_CHAR;
      str1[length1-1-i] = PADDING_CHAR;
      //Str2
      str2[i] = PADDING_CHAR;
      str2[length2-1-i] = PADDING_CHAR;
    }

    //Fac completarea structurii
    for (i=0;i<len1;i++)
      str1[i+padding] = string1[i];

    for (i=0;i<len2;i++)
      str2[i+padding] = string2[i];

    //structura pentru rezultate
    struct SW_DECISION* result = (struct SW_DECISION*) malloc(sizeof(struct SW_DECISION));

    //Aloc matricea continuu
    matrix[0]=&vector[0];
    matrix_trace[0]=&vector_trace[0];
    for (i=1;i<rows;i++)
    {
      matrix[i] = &(vector[i*cols]);
      matrix_trace[i] = &(vector_trace[i*cols]);
      matrix[i][0] = -i;

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
      matrix[i][0] = -i;
      matrix_trace[i][0] = TRACE_UP;
    }
    for (j=0;j<cols;j++)
    {
      matrix[0][j] = -j;
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

    //Fac trace-back si reconstitui secventa
    //  dimensiunea maxima a stringului este length1+length2
    result->start_pos = 0;
    result->active_length = len1;
    result->out1 = (char*) malloc((length1+length2)*sizeof(char));
    result->relation = (char*) malloc((length1+length2)*sizeof(char));
    result->out2 = (char*) malloc((length1+length2)*sizeof(char));

    //Fac traceback si compun rezultatul
    k = 0; //contorul de litere
    i = global_row;
    j = global_col;
    item = matrix[i][j];
    tip = matrix_trace[i][j];
    do
    {
	  //Obtin noul item
	  item = matrix[i][j];
	  tip = matrix_trace[i][j];
	  if ((i==0) && (j==0)) break;

	  if ( i == padding + 1)
	  {
	    if ( j > i )
	    {
	      result->decision = DECISION_PUSH_DOWN;
	    }
	    else if (j < i)
	    {
	      result->decision = DECISION_PUSH_UP;
	    }
	    else
	    {
	      result->decision = DECISION_PUSH_BOTH;
	    }
	  }

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
	      }
	      else
	      {
		//mismatch
		result->out1[k] = str1[i-1];
		result->relation[k] = CHAR_MISMATCH;
		result->out2[k] = str2[j-1];
		k++;
	      }
	      break;
	    case (TRACE_UP):
	      result->out1[k] = str1[i-1];
	      result->relation[k] = CHAR_MISMATCH;
	      result->out2[k] = CHAR_MINUS;
	      k++;
	      break;
	    case (TRACE_LEFT):
	      result->out1[k] = CHAR_MINUS;
	      result->relation[k] = CHAR_MISMATCH;
	      result->out2[k] = str2[j-1];
	      k++;
	      break;
	    case (TRACE_NONE):
	      if (str1[i-1] == str2[j-1])
	      {
		//match
		result->out1[k] = str1[i-1];
		result->relation[k] = CHAR_MATCH;
		result->out2[k] = str2[j-1];
		k++;
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
	  }
    } while (item !=0 );


    //finalizez structura
    result->start_pos = i;
    result->length = k;
    result->score = global_maximum;
    invertDecisionResult(result);

    //eliberez memoria ocupata de stringuri
    free(str1);
    free(str2);

    //eliberez memoria ocupata cu matricile
    free(vector);
    free(matrix);
    free(vector_trace);
    free(matrix_trace);
    return result;
}

/**==========================================================
*     Obtin rezultatul ce va fi retinut in matrice
*	Scorul se calculeaza in felul urmator
*	  - se incurajeaza potrivirile
*	  - se seteaza un prag de 0.
*	  - se departeaza extremele
* exemplu:
*	12 13 14 15 16 17 18 19 20 21 22 23 24
*      -14 -6 -6 -2 -2  0  0  0  4  4  8  8 16
*===========================================================*/
struct EXPANSION_SCORE getExpansionScore(struct SW_DECISION* decision,
	int score_offset_zero, int score_match, int score_mismatch) {
  struct EXPANSION_SCORE expansionScore;

  //Calculez scorul pentru rezultat;
  int seq_length = decision->active_length*3;
  int maximum = seq_length*2;
  int minimum = maximum - 3*decision->active_length;
  int zero_score = (maximum+minimum)/2 - score_offset_zero;
  int score,offset,mod;

  //Calculez departarea fata de 0
  offset = ((decision->score - zero_score) / 2);

  //score +- 2*(2^mod-1)
  if (offset < 0) {
    mod = -offset;
    score = - 2*((1<<mod)-1) + score_mismatch;
  } else {
    mod = offset;
    score = 2*((1<<mod)-1) + score_match;
  }

  //Compun rezultatul
  expansionScore.score = score;
  expansionScore.decision = decision->decision;
  expansionScore.phony1 = 0;
  expansionScore.phony2 = 0;

  return expansionScore;
}

/**==========================================================
*      Initializeaza structura cu rezultatele precalculate
*          - dimensiunea stringului de cautare
*===========================================================*/
struct EXP_RESULT_MATRIX* initializeEXPResult(int n) {
  struct EXP_RESULT_MATRIX* newMatrix = (struct EXP_RESULT_MATRIX*) malloc(sizeof(struct EXP_RESULT_MATRIX));
  struct EXPANSION_SCORE* vectorBlock;
  int i,offset;

  newMatrix->n = n;
  newMatrix->rows = 1<<(2*n);
  newMatrix->cols = 1<<(2*n);

  //Aloc memoria continuu;
  vectorBlock = (struct EXPANSION_SCORE*) malloc(newMatrix->rows*newMatrix->cols*sizeof(struct EXPANSION_SCORE));
  newMatrix->values = (struct EXPANSION_SCORE**) malloc(newMatrix->rows*sizeof(struct EXPANSION_SCORE*));
  for ( i = 0 ; i < newMatrix->rows; i++ ) {
      offset = i*newMatrix->cols;
      newMatrix->values[i] = &vectorBlock[offset];
  }

  return newMatrix;
}

/**
 * Eliberez memoria asociata unui rezultat
 */
void freeEXPResult(struct EXP_RESULT_MATRIX* result) {
    struct EXPANSION_SCORE* vectorBlock =
            (struct EXPANSION_SCORE*) &result->values[0][0];
    
    free(vectorBlock);
    free(result->values);
    free(result);
}

/**============================================================
*      Umplu structura rezultat cu datele furnizate de cautari
*==============================================================*/
void fillEXPResult(struct EXP_RESULT_MATRIX* result,
	int score_offset_zero, int score_match, int score_mismatch) {
  struct SW_DECISION* decisionItem;
  char* str1;
  char* str2;
  int n = result->n;
  int i,j;

  for (i=0;i<result->rows;i++) {
      for (j=0;j<result->cols;j++) {
	str1 = uchar2acgt(i,n);
	str2 = uchar2acgt(j,n);
	decisionItem = sw_align(str1,n,str2,n,n);
	result->values[i][j] = getExpansionScore(decisionItem,score_offset_zero,score_match,score_mismatch);

        //Nu mai am nevoie de stringuri
        free(str1);
        free(str2);
        
        //nu mai am nevoie de memoria tinuta de decisionItem
        free(decisionItem->out1);
        free(decisionItem->out2);
        free(decisionItem->relation);
        free(decisionItem); 
      }
  }
}

/**============================================================
*    Scriu matricea intr-un fisier
*	Formatul fisierului:
*	  int - marimea stringurilor de comparatie
*	  struct EXPANSION_SCORE** values - valorile
*==============================================================*/
void writeEXPResult2File(char* filename,struct EXP_RESULT_MATRIX* result) {
  FILE * pFile;
  struct EXPANSION_SCORE* array;
  int array_size;
  pFile = fopen ( filename , "wb" );

  //Scriu marimea stringului
  fwrite ( &result->n , 1 , sizeof(int) , pFile );

  //Scriu matricea
  array = (struct EXPANSION_SCORE*) &result->values[0][0];
  array_size = result->rows*result->cols;
  fwrite (array , array_size , sizeof(struct EXPANSION_SCORE) , pFile );

  fclose (pFile);
}

/**============================================================
*    Scriu matricea dintr-un fisier
*	Formatul fisierului:
*	  int - marimea stringurilor de comparatie
*	  struct EXPANSION_SCORE** values - valorile
*==============================================================*/
struct EXP_RESULT_MATRIX* readEXPResultFromFile(char* filename) {
  FILE * pFile;
  int n;
  size_t error_check;
  struct EXP_RESULT_MATRIX* result;
  struct EXPANSION_SCORE* array;

  size_t array_size;
  pFile = fopen ( filename , "rb" );

  //Scriu marimea stringului
  error_check = fread ( &n , sizeof(int) , 1 , pFile );
  if (error_check != 1) {
      fputs ("Reading error (int)",stderr);
      exit (3);
  }

  //Initializez matricea
  result = initializeEXPResult(n);
  array = (struct EXPANSION_SCORE*) &result->values[0][0];
  array_size = (size_t) result->rows*result->cols;
  error_check = fread (array , sizeof(struct EXPANSION_SCORE), array_size , pFile );
  if (error_check != array_size) {
      fputs ("Reading error (array)",stderr);
      exit (3);
  }

  fclose (pFile);

  return result;
}
