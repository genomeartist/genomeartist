/* 
 * File:   m3_expand.h
 * Author: iulian
 *
 * Created on June 20, 2010, 1:50 PM
 */

#ifndef _M3_EXPAND_H
#define	_M3_EXPAND_H

#define DECISION_PUSH_UP 0
#define DECISION_PUSH_BOTH 1
#define DECISION_PUSH_DOWN 2

//  In configuration file
//	Ponderea lungimii in calcularea scorului intermediar
//  #define SCORE_LUNGIME 4

#include "expansion_generation.h"
#include "structures_expanded.h"

//Extinde un interval
struct EXPAND_RESULT* expandStrings(struct EXP_RESULT_MATRIX* expansion_table,int length_modifier,
				    char* string1,int offset1,int length1,int size1,
				    char* string2,int offset2,int length2,int size2);

#endif	/* _M3_EXPAND_H */

