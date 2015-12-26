#include <stdio.h>
#include <stdlib.h>
#include <string.h> 

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

//=============================================================
//   O nucleotida se va converti in echivalentul ei in binar:
//        a = 00
//        c = 01
//        g = 10
//        t = 11
//============================================================

/**==============================================
*  Printeaza o matrice de dimensiune rows*cols
*===============================================*/
void print_mat(int** mat,int rows,int cols)
{
    int i,j;

    for (i=0;i<rows;i++)
    {
      for (j=0;j<cols;j++)
      {
	printf("%3d ",mat[i][j]);
      }
      printf("\n");
    }
    printf("=================================\n");
}

/**==============================================
*  Printeaza un string de lungime fata
*===============================================*/
void print_string(char* string,int length)
{
  int i;
  for (i=0;i<length;i++)
    putchar(string[i]);
  printf("\n");
}

/**===============================================
*  Converteste o secventa de nucleotide intr-un uint
*=================================================*/
uint acgt2uint(char* seq,int length)
{
  uint result;
  int i,min,offset;
  char c;

  //Initializam variabila rezultat
  result = 0;

  if (length < UINT_SIZE) min = length;
    else min  = UINT_SIZE;

  //Prelucrez fiecare litera in parte
  for (i=0;i<min;i++) {
      c = seq[i];
      offset = i*2;

      switch (c) {
	case 'a':case 'A':
	  result = result | (BINARY_A << offset);
	  break;
	case 'c':case 'C':
	case 'n':case 'N':
	  result = result | (BINARY_C << offset);
	  break;
	case 'g':case 'G':
	  result = result | (BINARY_G << offset);
	  break;
	case 't':case 'T':
	  result = result | (BINARY_T << offset);
	  break;
	default:
	  break;
      }
  }

  return result;
}

/**===============================================
*  Converteste o secventa de nucleotide intr-un uint
*   NOTA: Ordinea bitilor este inversata
*=================================================*/
uint acgt2uint_inv(char* seq,int length)
{
  uint result;
  int i,min,offset;
  char c;

  //Initializam variabila rezultat
  result = 0;

  if (length < UINT_SIZE) min = length;
    else min  = UINT_SIZE;

  //Prelucrez fiecare litera in parte
  for (i=0;i<min;i++) {
      c = seq[i];
      offset = (min-i-1)*2;

      switch (c) {
	case 'a':case 'A':
	  result = result | (BINARY_A << offset);
	  break;
	case 'c':case 'C':
	case 'n':case 'N':
	  result = result | (BINARY_C << offset);
	  break;
	case 'g':case 'G':
	  result = result | (BINARY_G << offset);
	  break;
	case 't':case 'T':
	  result = result | (BINARY_T << offset);
	  break;
	default:
	  break;
      }
  }

  return result;
}

/**===============================================
*  Converteste o secventa de nucleotide intr-un uchar
*=================================================*/
uchar acgt2uchar(char* seq,int length) {
    uint result = acgt2uint(seq,length);

    return (uchar) result;
}

/**======================================================
*  Converteste un uint intr-o secventa de nucleotide
*========================================================*/
char* uint2acgt(uint value, int length) {
  char* result;
  int i,min,offset;
  uint mask,item;

  if (length < UINT_SIZE) min = length;
      else min  = UINT_SIZE;

  result = (char*) malloc(min*sizeof(char));
  for(i=0;i<min;i++) {
      offset = i*2;
      mask = 3 << offset;
      item = (value & mask) >> offset;

      switch(item) {
	case BINARY_A:
	  result[i] = 'a';
	  break;
	case BINARY_C:
	  result[i] = 'c';
	  break;
	case BINARY_G:
	  result[i] = 'g';
	  break;
	case BINARY_T:
	  result[i] = 't';
	  break;
	default:
	  result[i] = 'x';
	  break;
      }
  }

  return result;
}

/**======================================================
*  Converteste un uchar intr-o secventa de nucleotide
*========================================================*/
char* uchar2acgt(uchar value, int length) {
    return uint2acgt((uint)value,length);
}


/**======================================================
*  Converteste o secventa de nucleotide in uppercase
*========================================================*/
char* acgt2upper(char* string,int n) {
  char* result = (char*) malloc((n+1)*sizeof(char));
  int i;

  for (i=0;i<n;i++) {
  	switch (string[i]) {
	  	case 'a': case 'A':
	  		result[i] = 'A';
	  		break;
	  	case 'c': case 'C':
	  		result[i] = 'C';
	  		break;	
	  	case 'g': case 'G':
	  		result[i] = 'G';
	  		break;	
	  	case 't': case 'T':
	  		result[i] = 'T';
	  		break;	
	  	case 'n': case 'N':
	  		result[i] = 'N';
	  		break;
	  	default:
	  		result[i] = 'A';
	  		break;		
  	}
  }
  
  result[n] = '\0';
  return result;
}

/**======================================================
*  Converteste o nucleotida in complementul sau
*========================================================*/
char nucl2compl(char nucleotide) {
      char c = nucleotide;
      switch (c) {
	case 'a':case 'A':
	    c = 'T';
	    break;
	case 't':case 'T':
	    c = 'A';
	    break;
	case 'c':case 'C':
	    c = 'G';
	    break;
	case 'g':case 'G':
	    c = 'C';
	    break;
	case 'n':case 'N':
	    c = 'N';
	    break;
	case ']':
	    c = '[';
	    break;
	case '[':
	    c = ']';
	    break;
	default:
	    break;
      }
      
      return c;
}


/**======================================================
*  Functie de transformare a unui string ce contine
*    un numar in binar, intr-un int in baza 10
*========================================================*/
int bin2dec(char *bin)
{
	int b, k, m, n;
	int len, sum = 0;

	len = strlen(bin) - 1;

	for(k = 0; k <= len; k++)
	{
		n = (bin[k] - '0'); // char to numeric value
		if ((n > 1) || (n < 0))
		{
			puts("\n\n ERROR! BINARY has only 1 and 0!\n");
			return (0);
		}
		for(b = 1, m = len; m > k; m--)
		{
			// 1 2 4 8 16 32 64 ... place-values, reversed here
			b *= 2;
		}
		// sum it up
		sum = sum + n * b;
		//printf("%d*%d + ",n,b); // uncomment to show the way this works
	}

	return(sum);
}

/**======================================================
*  Functie de transformare a caii unui fisier
*    din cale .raw se obtine calea .hash
*========================================================*/
char *nume_fisier_hash(char *nume_original)
{
	char *pozitie_slash = strrchr(nume_original,'/');
	char *pozitie_punct = strrchr(nume_original,'.');
	char *str_rez;
	//daca nu are punct deloc, se adauga terminatia .hash si atat
	if (pozitie_punct == NULL)
	{
		str_rez = (char*)calloc(strlen(nume_original)+strlen(".hash")+1, sizeof(char));
		strcpy(str_rez,nume_original);
		strcat(str_rez,".hash");
	}
	else
	{
		//daca punctul este inainte de slash, se adauga terminatia .hash si atat
		if (pozitie_slash != NULL && pozitie_punct < pozitie_slash)
		{
			str_rez = (char*)calloc(strlen(nume_original)+strlen(".hash")+1, sizeof(char));
			strcpy(str_rez,nume_original);
			strcat(str_rez,".hash");
		}
		else
		{
			//pe ramura asta are punct si e dupa slash, deci se taie ce e dupa punct si se inlocuieste cu .hash
			str_rez = (char*)calloc( strlen(nume_original) - strlen(pozitie_punct) + strlen(".hash") + 1, sizeof(char) );
			strncpy(str_rez, nume_original, strlen(nume_original) - strlen(pozitie_punct));
			strcat(str_rez,".hash");
		}
	}
	return str_rez;
}
