/* 
 * File:   expansion_generation.h
 * Author: iulian
 *
 * Created on June 17, 2010, 8:58 PM
 */

#ifndef _EXPANSION_GENERATION_H
#define	_EXPANSION_GENERATION_H

#define MATCH 2
#define MISMATCH -1
#define MISOFFSET 3

#define TRACE_NONE 0
#define TRACE_LEFT 1
#define TRACE_UP   2
#define TRACE_DIAG 3

#define CHAR_MISMATCH ' '
#define CHAR_MINUS '-'
#define CHAR_MATCH '|'

#define DECISION_PUSH_UP 0
#define DECISION_PUSH_BOTH 1
#define DECISION_PUSH_DOWN 2

// From configuration file
//   EXPANSION_OFFSET_ZERO	default -3
//   EXPANSION_SCORE_MATCH	default 2
//   EXPANSION_SCORE_MISMATCH	default 0


#define PADDING_CHAR 'X'

typedef unsigned char uchar;
typedef unsigned int uint;

//Structura rezultat
typedef struct SW_DECISION
{
    int start_pos;	//corespondenta alinieri cu prima secventa
    int active_length;  //lungimea de interes a secventei
    int length;		//lungimea secventei rezultat
    int score;		//Scorul obtinut de aliniere
    int decision;	//Decizia luata
    char* out1;		//primul string aliniat
    char* relation;	//relatia (liniutze)
    char* out2;		//al doilea string aliniat
} SW_DECISION;

//Structura ce va fi retinuta
typedef struct EXPANSION_SCORE {
    char score;		//Scorul alinierii
    uchar decision;	//decizia luata privind alinierea
    uchar phony1;	//aliniez structura la 4
    uchar phony2;	//aliniez structura la 4
} EXPANSION_SCORE;

//Matricea de rezultate
typedef struct EXP_RESULT_MATRIX {
    int rows;  //numarul de randuri
    int cols;  //numarul de coloane
    int n;     //dimensiunea stringurilor ce se testeaza
    struct EXPANSION_SCORE** values; //valorile propriu-zise alocate continuu
} EXP_RESULT_MATRIX;

//Metodele puse la dispozitie
struct EXP_RESULT_MATRIX* initializeEXPResult(int n);
void freeEXPResult(struct EXP_RESULT_MATRIX* result);
void fillEXPResult(struct EXP_RESULT_MATRIX* result,
	int score_offset_zero, int score_match, int score_mismatch);
void writeEXPResult2File(char* filename,struct EXP_RESULT_MATRIX* result);
struct EXP_RESULT_MATRIX* readEXPResultFromFile(char* filename);

#endif	/* _EXPANSION_GENERATION_H */

