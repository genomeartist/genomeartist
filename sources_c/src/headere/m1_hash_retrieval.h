/* 
 * File:   m1_hash_retrieval.h
 * Author: iulian
 *
 * Created on June 21, 2010, 8:29 PM
 */

#ifndef _M1_HASH_RETRIEVAL_H
#define	_M1_HASH_RETRIEVAL_H

#include "comun.h"

#ifdef _WIN32
#include <io.h>
#else
#include <mqueue.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <unistd.h>
#endif

STRUCTURA_HASH* positionsForHash(unsigned int hashVal, int fd);


#endif	/* _M1_HASH_RETRIEVAL_H */

