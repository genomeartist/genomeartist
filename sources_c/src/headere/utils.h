/* 
 * File:   utils.h
 * Author: iulian
 *
 * Created on June 17, 2010, 8:59 PM
 */

#ifndef _UTILS_H
#define	_UTILS_H

//macro
#define TRUE 1
#define FALSE 0
#define INFINITE 9999999

//Contante pentru tipurile de date
typedef unsigned char uchar;
typedef unsigned int uint;
typedef unsigned long ulong;
#define UCHAR_SIZE 4
#define UINT_SIZE 16

//Constante pentru valori
#define BINARY_A 0
#define BINARY_C 1
#define BINARY_G 2
#define BINARY_T 3

//functii de printare
void print_mat(int** mat,int rows,int cols);
void print_string(char* string,int length);

//Conversia din string in binar
uint acgt2uint(char* seq,int length);
uint acgt2uint_inv(char* seq,int length);	//ordinea bitilor inversata
uchar acgt2uchar(char* seq,int length);

//Conversia din binar in string
char* uint2acgt(uint value, int length);
char* uchar2acgt(uchar value, int length);

//Conversia query-ului in uppercase
char* acgt2upper(char* string,int length);

//Conversia unei nucleotide in complement
char nucl2compl(char nucleotide);

//functie de transformare a unui string ce contine un numar in binar, intr-un int in baza 10
int bin2dec(char *bin);

//Conversie cale pentru hash
char *nume_fisier_hash(char *nume_original);

#endif	/* _UTILS_H */

