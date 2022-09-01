#!/bin/bash

echo "Genomic flanking length"
read LengthFL

#Imparte fisierul cu contiguri si preia numele lor in denumirea fisierului.
gawk -F ">| " '/^>/ {s=$2".fa"}; {print > s}' *.fasta

#Pastreaza .fa al caror nume se gaseste in tabelul exportat de GA, coloana Genomic Reference Sequence.
cat 1IR_DOWNSTREAM.txt | while read genomic gstart gend qstart qend
do
for i in *.fa
do
if [[ $genomic == *"_${i%.fa}" ]]
then
echo $genomic $gstart $gend $qstart $qend > $gstart.${i%.fa}.txt
cat *.${i%.fa}.txt > ${i%.fa}.txt
mkdir ${i%.fa}/
fi
done
done

for i in *.fa
do
mv $i ${i%.fa}/
mv ${i%.fa}.txt ${i%.fa}/
done
rm -r *.fa *.txt


ls -d */ |
while read dirname 
do
 cd $dirname

#Afla catena in functie de coordonatele genomice.
cat *.txt | while read genomic gstart gend qstart qend
do 
if [ $gstart -gt $gend ]
then 
echo $genomic $gstart $gend minus $qstart $qend > $gstart.coord.txt
else 
echo $genomic $gstart $gend plus $qstart $qend > $gstart.coord.txt 
fi 
done

cat *.coord.txt > coord.txt
rm -r *.coord.txt

#Transforma in singleline.
for i in *.fa
do
gawk '{if(NR==1) {print $0} else {if($0 ~ /^>/) {print "\n"$0} else {printf $0}}}' $i > "$i.singleline"
done

#Elimina header.
for i in *.singleline
do
tail -n +2 $i > ${i%.fa.singleline}".utile"
done

#Calculez cat o sa scad sau adaug in total. Genomic FL + Lungime TE.
Genomic_FL_TE=$( expr $LengthFL + $Qlength )

#CALCULEAZA COORDONATELE DE EXTRAGERE.
cat coord.txt | while read genomic coordinate1 coordinate2 strand qstart qend
do
 
  if [ $strand = plus ]
then

START=1
if ! [ $coordinate1 -le $Genomic_FL_TE ]
then
START=$( expr $coordinate1 - $Genomic_FL_TE )
fi
STOP=$coordinate2
TE_Length=$( expr $qend - $qstart + 1 )

#Extragere.
for i in *.utile
do
Contig_Length=$( cat $i | wc -c )
cut -c$START-$STOP $i > ${i%.utile}.$coordinate1".flanking"
done

for i in *.$coordinate1.flanking
do
#Adauga cate o linie noua inainte de fiecare motiv extras
gsed -e 's/^/\n/' -i $i
#Adauga semnul de header la liniile noi
#Ai pus aici 1 inainte de s asa ca adauga semnul > doar pe prima linie
gsed -i -e "1s/^$/>/g" $i
#Adauga numele fisierului in header
gsed -i -e "s/>/>${i%.$coordinate1.flanking}-ins/" $i
Length=$( gsed -e '1d' $i | wc -c )
Length1=$( expr $Length - 1 )
STOPh=$( expr $START + $Length1 - 1 )
#Modifica antetul fisierului ce contine secventa de jonctiune prin adaugarea coordonatei de intere, catenei, intervalului de extragere, lungimii totale.-i= editeaza fisierul pe loc(fara output standard); -e= script; s/= substituie; /g= substitutia se aplica pt toate match-urile.
gsed -i -e "s/-ins/\-ins_only_IR3_TG. Contig length: $Contig_Length. Coordinates: $coordinate1-$coordinate2 (TE length: $TE_Length). Strand: $strand. Range: $START:$STOPh. Total length: $Length1. /g" $i

#Daca insertia are lungimea integrala a TE.
if [ $TE_Length -lt $Qlength ]
then
gsed -i -e "s/TE length: $TE_Length/\TE length: $TE_Length < RefTE/g" $i
elif [ $TE_Length -gt $Qlength ]
then
gsed -i -e "s/TE length: $TE_Length/\TE length: $TE_Length > RefTE/g" $i
fi

#A extras "Genomic flanking length" mentionat?
if [ $coordinate1 -le $LengthFL ]
then
gsed -i -e "s/-ins_only_IR3_TG./\-ins_only_IR3_TG. ALERT!/g" $i
fi

done
fi

#Orientarea II.
if [ $strand = minus ]
then
  START=$coordinate2
  STOP=$( expr $coordinate1 + $Genomic_FL_TE )
TE_Length=$( expr $qend - $qstart + 1 )  

#Extragere.
for i in *.utile
do
Contig_Length=$( cat $i | wc -c )
cut -c$START-$STOP $i > ${i%.utile}.$coordinate2".flanking.minus"
done

#Reverse complement.
for i in *.$coordinate2.flanking.minus
do
cat $i | tr ACGTacgt TGCAtgca | rev > ${i%.flanking.minus}".minus.flanking"
done

for i in *.$coordinate2.minus.flanking
do
#Adauga cate o linie noua inainte de fiecare motiv extras
gsed -e 's/^/\n/' -i $i
#Adauga semnul de header la liniile noi
#Ai pus aici 1 inainte de s asa ca adauga semnul > doar pe prima linie
gsed -i -e "1s/^$/>/g" $i
#Adauga numele fisierului in header
gsed -i -e "s/>/>${i%.$coordinate2.minus.flanking}-ins/" $i
Length=$( gsed -e '1d' $i | wc -c )
Length1=$( expr $Length - 1 )
STOPh=$( expr $START + $Length1 - 1 )
#Modifica antetul fisierului ce contine secventa de jonctiune prin adaugarea coordonatei de interes catenei, intervalului de extragere, lungimii totale.-i= editeaza fisierul pe loc(fara output standard); -e= script; s/= substituie; /g= substitutia se aplica pt toate match-urile.
gsed -i -e "s/-ins/\-ins_only_IR3_TG. Contig length: $Contig_Length. Coordinates: $coordinate1-$coordinate2 (TE length: $TE_Length). Strand: $strand. Range: $START:$STOPh. Total length: $Length1. /g" $i

#Daca insertia are lungimea integrala a TE.
if [ $TE_Length -lt $Qlength ]
then
gsed -i -e "s/TE length: $TE_Length/\TE length: $TE_Length < RefTE/g" $i
elif [ $TE_Length -gt $Qlength ]
then
gsed -i -e "s/TE length: $TE_Length/\TE length: $TE_Length > RefTE/g" $i
fi

#A extras "Genomic flanking length" mentionat?
if [[ $STOP -gt $STOPh ]]
then
gsed -i -e "s/-ins_only_IR3_TG./\-ins_only_IR3_TG. ALERT!/g" $i
fi

done
fi
done
rm -r *.singleline *.utile
rm -r *.minus *.txt
cd -
done

