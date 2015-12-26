#!/usr/bin/awk -f
BEGIN {
	#Define separators
	FS=" ";
	OFS=",";
	AUX_FS=":";
	
	# No newline printed
	ORS="";

	#Portiunea de genom curenta
	active="false";
}
{
	#print ">>",$0;

	# Se trece in starea INACTIVE daca se detecteaza sfarsitul secventei
	if ( $1 == "//") {
		active="false";
		#printf("\ndeactivated");
	}

	# Se opreste daca se detecteaza sfarsitul secventei
	if ( active == "true" ) {
		sequence=$0
		gsub(/\ */,"",sequence);
		gsub(/[0-9]+/,"",sequence);
		print sequence;
	}

	# Se trece in starea ACTIVE daca se detecteaza inceputul unei secvente
	if ( $1 == "ORIGIN") {
		active="true";
		#printf("activated\n");
	}
}
END {
	#print "end";
}
