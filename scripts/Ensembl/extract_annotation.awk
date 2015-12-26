#!/usr/bin/awk -f
BEGIN {
	#Define separators
	FS=" ";
	OFS=",";
	AUX_FS=":";
	
	#Portiunea de genom curenta
	chromosome="";
	sequence_start="";
	
	#Variabile pentru gene
	FORWARD="F";
	COMPLEMENT="R";
	gene_start="";
	gene_end="";
	gene_orientation="";
	gene_id="";
	gene_name="";
	
	#print "begin";
}
{
	#print ">>",$0;

	# $1 - reprezinta tipul randului
	#   SV - The accession.version pair which gives the exact reference to a particular sequence
	#   FT - data field
	
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
