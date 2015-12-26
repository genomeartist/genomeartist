/* 
 * File:   smith-waterman.h
 * Author: iulian
 *
 * Created on June 17, 2010, 8:51 PM
 */

#ifndef _SMITH_WATERMAN_H
#define	_SMITH_WATERMAN_H

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

#include "structures_aliniere.h"

//Alinieaza 2 secvente folosind algoritmul Smith-Waterman
struct ALINIERE* sw_align(char* str1,int offset1, int length1, char* str2, int offset2, int length2);

#endif	/* _SMITH_WATERMAN_H */

