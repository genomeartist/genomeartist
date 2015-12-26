#include <stdio.h>
#include <stdlib.h>
#include "../../headere/m3_expand.h"
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

/**==========================================================
*   Inverseaza stringurile pentru a le parcurge la stanga
*===========================================================*/
char* reverseString(char* string,int n) {
  char* result = (char*) malloc(n*sizeof(char));
  int i;

  for (i=0;i<n;i++)
    result[i] = string[n-i-1];

  return result;
}

/**============================================================
*    Extind stringurile folosindu-ma de matricea de extindere
*==============================================================*/
struct EXPAND_RESULT* expandStrings(struct EXP_RESULT_MATRIX* expansion_table,int length_modifier,
				    char* string1,int offset1,int length1,int size1,
				    char* string2,int offset2,int length2,int size2) {
    //Variabile comune
    int n = expansion_table->n;
    struct EXPAND_RESULT* result;
    struct EXPANSION_SCORE* decisionItem;
    int shouldBreak = 0;

    //Variabile extindere dreapta
    int right_pointer1,right_pointer2;
    int right_score;
    char* right_window1;
    char* right_window2;
    uint conversion1,conversion2;

    //Setez pointeri
    right_pointer1 = offset1+length1;
    right_pointer2 = offset2+length2;
    right_score = length_modifier*length1;

    //Variabile extindere stanga
    int left_pointer1,left_pointer2;
    int left_score;
    char* left_window1;
    char* left_window2;

    left_pointer1 = offset1;
    left_pointer2 = offset2;
    left_score = length_modifier*length1;

    //BEGIN Pornesc extinderea in dreapta
    shouldBreak = 0;
    while (TRUE) {
      //Daca se depaseste vreun interval, ies direct
      if (right_pointer1 + n > size1) {
	right_pointer1 = size1-n;
	shouldBreak = 1;
      }
      if (right_pointer2 + n > size2) {
	right_pointer2 = size2-n;
	shouldBreak = 1;
      }
      if (shouldBreak == 1) break;
    
      //Calculez scorul curent
      right_window1 = &string1[right_pointer1];
      right_window2 = &string2[right_pointer2];
      conversion1 = acgt2uint(right_window1,n);
      conversion2 = acgt2uint(right_window2,n);

      decisionItem = &expansion_table->values[conversion1][conversion2];
      right_score += decisionItem->score;

      //Ies daca ating scorul scade sub 0
      if (right_score <= 0) break;

      //Continui sa misc ferestrele
      switch(decisionItem->decision) {
	case DECISION_PUSH_UP:
	  right_pointer1++;
	  break;
	case DECISION_PUSH_BOTH:
	  right_pointer1++;
	  right_pointer2++;
	  break;
	case DECISION_PUSH_DOWN:
	  right_pointer2++;
	  break;
	default:
	  break;
      }
    }
    //END extindere dreapta
    //printf("RIGHT EXPAND score= %d, pointer1 = %d, pointer2 = %d\n",right_score,right_pointer1,right_pointer2);

    //BEGIN Pornesc extinderea in stanga
    shouldBreak = 0;
    while (TRUE) {
      //Daca se depaseste vreun interval, ies direct
      if (left_pointer1 - n < 0) {
	left_pointer1 = n;
	shouldBreak = 1;
      }
      if (left_pointer2 - n< 0) {
	left_pointer2 = n;
	shouldBreak = 1;
      }
      if (shouldBreak == 1) break;
    
      //Calculez scorul curent
      left_window1 = reverseString(&string1[left_pointer1-n],n);
      left_window2 = reverseString(&string2[left_pointer2-n],n);
      conversion1 = acgt2uint(left_window1,n);
      conversion2 = acgt2uint(left_window2,n);

      decisionItem = &expansion_table->values[conversion1][conversion2];
      left_score += decisionItem->score;

      //Ies daca ating scorul scade sub 0
      if (left_score <= 0) {
        free(left_window1);
        free(left_window2);
      	break;
      }

      //Continui sa misc ferestrele
      switch(decisionItem->decision) {
	case DECISION_PUSH_UP:
	  left_pointer1--;
	  break;
	case DECISION_PUSH_BOTH:
	  left_pointer1--;
	  left_pointer2--;
	  break;
	case DECISION_PUSH_DOWN:
	  left_pointer2--;
	  break;
	default:
	  break;
      }

      free(left_window1);
      free(left_window2);
    }
    //END extindere stanga
    //printf("LEFT EXPAND score= %d, pointer1 = %d, pointer2 = %d\n",left_score,left_pointer1,left_pointer2);

    //Compun rezultatul
    right_pointer1 = right_pointer1 + n;
    right_pointer2 = right_pointer2 + n;
    left_pointer1 = left_pointer1 - n;
    left_pointer2 = left_pointer2 - n;

    result = (struct EXPAND_RESULT*) malloc(sizeof(struct EXPAND_RESULT));
    result->str1 = &string1[left_pointer1];		//Pointer catre locatia de inceput a secventei extinse
    result->offset1 = left_pointer1;  			//Offsetul acesteia fata de inceputul stringului initial
    result->length1 = right_pointer1-left_pointer1;  	//Lungimea secventei extinse
    result->str2 = &string2[left_pointer2];   		//Pointer catre inceputul stringului omolog
    result->offset2 = left_pointer2;  			//Offsetul fata de initial
    result->length2 = right_pointer2-left_pointer2;  	//Lungimea omologului extins
    result->tempScore = length_modifier*length1 + left_score + right_score; //Scorul temporar
    result->usedFlag = FALSE;				//Nu este folosit acest expanded
    return result;
}
