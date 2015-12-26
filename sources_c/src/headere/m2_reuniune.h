/* 
 * File:   m2_reuniune.h
 * Author: iulian
 *
 * Created on June 20, 2010, 3:39 PM
 */

#ifndef _M2_REUNIUNE_H
#define	_M2_REUNIUNE_H

#include "structures_interval.h"

void proceseazaQuery(int fd, char* query,int length,int windowSize,MAPPING_INTERVAL* minterval,VECTOR_INTERVAL* vinterval);

#endif	/* _M2_REUNIUNE_H */

