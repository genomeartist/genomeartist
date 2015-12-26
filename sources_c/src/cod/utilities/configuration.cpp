#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include "../../headere/configuration.h"

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

//##########################################
//   HASHTABLE cu key si value stringuri
//    - este implementat ca un vector de
//      liste inlantuite
//##########################################

//Nodul din lista de coliziuni
typedef struct _node{
  char *name;
  char *desc;
  struct _node *next;
}node;

#define HASHSIZE 101
static node* hashtab[HASHSIZE];

//Seteaza hashtable-ul pe null
void inithashtab(){
  int i;
  for(i=0;i<HASHSIZE;i++)
    hashtab[i]=NULL;
}

//functia de hash
unsigned int hash(char *s){
  unsigned int h=0;
  for(;*s;s++)
    h=*s+h*31;
  return h%HASHSIZE;
}

//obtine un nod din hash
node* lookup(char *n){
  unsigned int hi=hash(n);
  node* np=hashtab[hi];
  for(;np!=NULL;np=np->next){
    if(!strcmp(np->name,n))
      return np;
  }

  return NULL;
}

//copiaza un string in hash
char* m_strdup(char *o){
  int l=strlen(o)+1;
  char *ns=(char*)malloc(l*sizeof(char));
  strcpy(ns,o);
  if(ns==NULL)
    return NULL;
  else
    return ns;
}

//obtin o valoare din hash
char* get(char* name){
  node* n=lookup(name);
  if(n==NULL)
    return NULL;
  else
    return n->desc;
}

//Adauga o valoare in hash
int install(char* name,char* desc){
  unsigned int hi;
  node* np;
  if((np=lookup(name))==NULL){
    hi=hash(name);
    np=(node*)malloc(sizeof(node));
    if(np==NULL)
      return 0;
    np->name=m_strdup(name);
    if(np->name==NULL) return 0;
    np->next=hashtab[hi];
    hashtab[hi]=np;
  }
  else
    free(np->desc);
  np->desc=m_strdup(desc);
  if(np->desc==NULL) return 0;

  return 1;
}

/* A pretty useless but good debugging function,
   which simply displays the hashtable in (key.value) pairs
*/
void displaytable(){
  int i;
  node *t;
  for(i=0;i<HASHSIZE;i++){
    if(hashtab[i]==NULL)
      printf("()");
    else{
      t=hashtab[i];
      printf("(");
      for(;t!=NULL;t=t->next)
	printf("(%s.%s) ",t->name,t->desc);
      printf(")");
    }
  }
}

//elibereaza memoria pentru hashtable
void cleanup(){
  int i;
  node *np,*t;
  for(i=0;i<HASHSIZE;i++){
    if(hashtab[i]!=NULL){
      np=hashtab[i];
      while(np!=NULL){
	t=np->next;
	free(np->name);
	free(np->desc);
	free(np);
	np=t;
      }
    }
  }
}

//Initializez hashtable-ul
void configurationInit() {
	inithashtab();
}


//Citeste fisierul de configuratie de pe disk
void configurationReadFromFile() {
	FILE* fisier_intrare;
	char linie[MAX_LINIE_SIZE];
	char cheie[MAX_MARIME_STRING];
	char valoare[MAX_MARIME_STRING];
	
	//trebuie sa am un fisier de intrare care poate fi deschis
	fisier_intrare = fopen(CONFIGURATION_FILE, "r");
	if (fisier_intrare == NULL )
	{
		fprintf(stdout,"Fisierul de intrare %s nu poate fi deschis\n",CONFIGURATION_FILE);
		return;
	}

	//iau toate fisierele date
	while (fgets(linie,MAX_LINIE_SIZE,fisier_intrare) != NULL)
	{
		//Se ignora comentariile
		if (linie[0] == '#') continue;
		//sterg ultimul caracter
		linie[strlen(linie)-1] = 0;
		
		//citesc informatiile din linia curenta
		sscanf(linie," %s  =  %s",cheie,valoare);
		//printf("linie citita:%s=%sX\n",cheie,valoare);
		
		//pun informatia in hash
		install(cheie,valoare);
	}

	fclose(fisier_intrare);
}

//Eliberez fisierul de configuratie
bool configurationIsValid() {
	//Parametrii de cautare
	bool isValid = true;

	//~~
	int length_modifier = atoi(configurationGetString((char*)"EXPANSION_LENGTH_MODIFIER"));
	if (length_modifier < 0) {
		isValid = false;
	}

	//~~
	int picking_depth = atoi(configurationGetString((char*)"PICKING_SORT_DEPTH"));
	if (picking_depth <= 0) {
		isValid = false;
	}

	//~~
	int nucleus_size = atoi(configurationGetString((char*)"COMPUNERE_LUNGIME_MINIMA_NUCLEU"));
	if (nucleus_size < 10) {
		isValid = false;
	}

	
	//~~ Error
	return isValid;
}

//Obtin valoarea stocata
char* configurationGetString(char* key) {
	return get(key);
}

//Eliberez fisierul de configuratie
void configurationFree() {
		//Afisez de test
		//displaytable();
	cleanup();
}

