/* 
 * File:   m1_hash_retrieval.h
 * Author: iulian
 *
 * Created on June 21, 2010, 8:29 PM
 */

#ifndef _M1_HASH_RETRIEVAL_H
#define	_M1_HASH_RETRIEVAL_H

#include "comun.h"
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>

STRUCTURA_HASH* positionsForHash(unsigned int hashVal, int fd);


#endif	/* _M1_HASH_RETRIEVAL_H */

