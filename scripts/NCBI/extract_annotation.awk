#!/usr/bin/awk -f
# Script versiunea 3, sterge simboluri "<" si ">" din pozitie si pune "/gene="
BEGIN {
	#Define separators
	FS=" ";
	OFS=",";
	AUX_FS=":";
	
	# Variabilele de stare
	active="false";		
	active_gene="false";

	#Portiunea de genom curenta
	chromosome="genome";
	
	#Variabile pentru gene
	FORWARD="F";
	COMPLEMENT="R";
	gene_start="";
	gene_end="";
	gene_orientation="";
	gene_id="";
	gene_name="none";
	
	#print "begin";
}
{
	#print ">>",$0;
	
	# Se trece in starea INACTIVE daca se detecteaza sfarsitul secventei
	if ( $1 == "ORIGIN") {
		active="false";
		#printf("\ndeactivated");
	}

	# Se opreste daca se detecteaza sfarsitul secventei
	if ( active == "true" ) {
		if ($1 !~ /^\//) {
			if ($1 != "gene") {
				if (active_gene == "true") {
					#Aici trebuie sa printez rezultatul final
					if (    length(gene_name) > 0 &&
						length(gene_start) > 0 &&
						length(gene_end) > 0 &&
						length(gene_orientation) > 0) {
				
						if (gene_orientation == FORWARD) {
							print chromosome,">" gene_id " type=gene; " "name=" gene_name "; " \
								"ID=" gene_id "; " \
								"loc=" chromosome ":" gene_start ".." gene_end ";";
						} else {
							print chromosome,">" gene_id " type=gene; " "name=" gene_name "; " \
								"ID=" gene_id "; " \
								"loc=" chromosome ":complement(" gene_start ".." gene_end ");";
						}
				
						#resetez valorile
						gene_start="";
						gene_end="";
						gene_orientation="";
						gene_id="none";
						gene_name="";
					}
				}

				#Dezactivez gena
				active_gene="false";
			} else {
				active_gene="true";

				#Obtin directia si locatia genei
				n=split($2,array,AUX_FS);
				if (index($2,"complement") == 1 ) {
					gene_orientation=COMPLEMENT;
					leftb=index($2,"(");
					rightb=index($2,")");
					auxstring=substr($2,leftb+1,rightb-leftb-1);
				} else {
					gene_orientation=FORWARD;
					auxstring=$2;
				}
			
				gsub(/[<>]/, "", auxstring); 
				n=split(auxstring,array,".");
				gene_start=array[1];
				gene_end=array[3];
				#print ">>" gene_orientation,array[1],array[3];
			}
		} else {
			if ( active_gene == "true" ) {
				#print $0
				if (index($1,"/gene=") == 1) {
					#Split
					if (index($1,"\"") > 0) {
						n=split($1,array,"\"");
					} else {
						n=split($1,array,"=");
					}

					#Get data
					gene_name=array[2];
					#print ">>" gene_name;
				} else 
				if (index($1,"/db_xref") == 1) {
					#Split
					if (index($1,"\"") > 0) {
						n=split($1,array,"\"");
					} else {
						n=split($1,array,"=");
					}

					#Get data
					auxstring=array[2]
					n=split(auxstring,array,":");
					gene_id=array[2];
					#print ">>" gene_id;
				}
			}
		}
	}

	# Se trece in starea ACTIVE daca se detecteaza inceputul unei secvente
	if ( $1 == "FEATURES") {
		active="true";
		#printf("activated\n");
	}

	#~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	if ( $1 == "AC" ) {
		separator=":";
		n=split($2,array,AUX_FS);
		chromosome=array[3];
		sequence_start=array[4];
	} else 
	if ( $1 == "FT" ){
		if ( $2 == "gene" ) {
			#verific daca e pe complement
			if (index($3,"complement") == 1 ) {
				gene_orientation=COMPLEMENT;
				leftb=index($3,"(");
				rightb=index($3,")");
				auxstring=substr($3,leftb+1,rightb-leftb-1);
			} else {
				gene_orientation=FORWARD;
				auxstring=$3;
			}
			
			n=split(auxstring,array,".");
			gene_start=array[1];
			gene_end=array[3];
			#print ">>" gene_orientation,array[1],array[3];
		} else {
			if (index($2,"/gene") == 1) {
				if (index($2,"\"") > 0) {
					n=split($2,array,"\"");
					gene_id=array[2];
					#print ">>" gene_id;
				} else {
					n=split($2,array,"=");
					gene_id=array[2];
					#print ">>" gene_id;
				}
			} else 
			if (index($2,"/locus_tag") == 1) {
				if (index($2,"\"") > 0) {
					n=split($2,array,"\"");
					gene_name=array[2];
					#print ">>" gene_name;
				} else {
					n=split($2,array,"=");
					gene_name=array[2];
					#print ">>" gene_name;
				}
				
				#Aici trebuie sa printez rezultatul final
				gene_start = sequence_start + gene_start - 1;
				gene_end = sequence_start + gene_end - 1;
				
				if (	length(chromosome) > 0 &&
					length(gene_name) > 0 &&
					length(gene_id) > 0 &&
					length(gene_start) > 0 &&
					length(gene_end) > 0 &&
					length(gene_orientation) > 0) {
					
					if (gene_orientation == FORWARD) {
						print chromosome,">" gene_id " type=gene; " "name=" gene_name "; " \
							"ID=" gene_id "; " \
							"loc=" chromosome ":" gene_start ".." gene_end ";";
					} else {
						print chromosome,">" gene_id " type=gene; " "name=" gene_name "; " \
							"ID=" gene_id "; " \
							"loc=" chromosome ":complement(" gene_start ".." gene_end ");";
					}
					
					#resetez valorile
					gene_start="";
					gene_end="";
					gene_orientation="";
					gene_id="";
					gene_name="";
				}
			}
		}
	}
}
END {
	#print "end";
}
