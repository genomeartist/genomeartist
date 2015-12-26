/* 
 * File:   configuration.h
 * Author: iulian
 *
 * Created on June 21, 2010, 8:29 PM
 */

#ifndef _CONFIGURATION_H
#define	_CONFIGURATION_H

#define CONFIGURATION_FILE "paramsServer.txt"
#define MAX_MARIME_STRING	128
#define MAX_LINIE_SIZE		512

void configurationInit(); //Initializez hashtable-ul
void configurationReadFromFile(); //Citesc fisierul in hashtable
char* configurationGetString(char* key);	//Obtin valoarea pentru cheie
bool configurationIsValid(); //Verifica integritatea parametrilor
void configurationFree(); //Eliberez fisierul de configuratie


#endif	/* _CONFIGURATION_H */
