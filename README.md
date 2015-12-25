# genomeartist
Genome ARTIST (ARtificial Transposon Insertion Site Tracker)
Is a new bioinformatics tool originally developed in order to allow a rapid detection of insertional mutations generated in the genome of Drosophila melanogaster by means of artificial P element derivatives. 

Aside from the large gene disruption projects (BDGP, http://www.fruitfly.org; FlyBase, http://www.flybase.org), many fly laboratories run small scale transposon mutagenesis screenings. Basically, mobilization with a transposase source of artificial molecular constructs (derived from a natural P mobile element or from other transposons) induces insertional mutations in the germline. Many different mutant strains are derived from affected parents using classical genetic crosses and, in the end, their putative useful mutations are analyzed by inverse PCR and sequencing. The sequencing product is a mixture of information, where part of it pertains to the fruit fly canonical genome and the rest of it belongs to a specific artificial element. The most critical aspect of sequence analysis is to detect the exact border between the genomic and transposon DNA, equivalent with identification of the insertion site at the nucleotide level. Sequencing products are not always perfect and a few artifact bases mismatches may impair a fluent insertion mapping. Most commonly, the sequences of interest are aligned with BLAST (http://blast.ncbi.nlm.nih.gov) or BLAT (http://www.genome.ucsc.edu) against D. melanogaster official genome, which do not contain neither natural nor artificial P transposons. Often, additional manual sequence annotation is needed in order to finish an accurate insertion mapping and here is when Genome ARTIST enters the scene and offers a bit of help. The query sequence is simultaneously compared offline against both the D. melanogaster genome and the specific transposon sequence, partial sequence alignments are matched to each other, relative scores of alignments are calculated and the best mixed sequence with the genomic and transposon coordinates is offered to the user. Different colors are used for genomic versus transposon fragments, and an intuitive list of results and details is also depicted. One may easy observe the absolute site of insertion, the gene affected by the transposon insertion, and also the genes located in the close vicinity of the insertion. Special biological conditions occurring during mutagenesis experiments, as transposon reinsertions into the original mobile element copy, are not usually detected with other searching algorithms, therefore Genome ARTIST is designed to reveal and to interpret such events. 

To some extent, Genome ARTIST is an alternative for the classical alignment algorithms and may be exploited for checking the specificity of short sequences as primers or probes. Last but not the least, aficionados of different model organisms may use the abilities of Genome ARTIST by loading other genomes and/or specific transposons. The performances of Genome ARTIST were tested on the genomes of Saccharomyces cerevisiae, Caenorhabditis elegans, Drosophila pseudoobscura, Ciona intestinalis, Danio rerio and Arabidopsis thaliana and offers similar results to those obtained on D. melanogaster. Additionally, pairwise comparative alignments may be performed among sequences pertaining to various species aside from D. melanogaster, allowing the identification of structural orthologous. 


Genome ARTIST is the result of an interdisciplinary collaboration between researchers from the Department of Genetics, University of Bucharest, and computer engineers, graduates of "Politehnica" University of Bucharest, PUB. The authors of the software are Alexandru Al. Ecovoiu, Iulian Constantin Ghionoiu, Andrei Mihai Ciuca and Attila Cristian Ratiu.


~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   Building Genome ARTIST from sources
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

~~~~ Requirements ~~~~

To be able to build Genome ARTIST under a UNIX system you need the following tools:

1. The package "build-essential" for building sources

  $ sudo apt-get install build-essential

2. If you have a 64 bit system you will need the following packages:

  $ sudo apt-get install ia32-libs g++-multilib libc6-i386 libc6-dev-i386 lib32gcc1 lib32stdc++6

3. You need to have Java Development Kit installed, 1.6 or higher

  $ sudo apt-get install openjdk-6-jdk

4. For the build script you will need at least Apache Ant version 1.8.0

  $ sudo apt-get install ant

~~~~ Steps to a complete build ~~~~

1. Go to folder "$PROJECT_ROOT/sources_c" and issue the command:

  $ make

2. You then go to the folder "$PROJECT_ROOT/ant_build" and issue the command:

  $ ant genomeless

3. Results

A functional version of Genome ARTIST has been built in the folder "genomeless". It has no genomes nor transposons loaded.
You can use the "genomeless" version and configure it with your own genomes. 
For more information about loading genomes, please reffer to the Manual.

~~~~ Starting and configuring Genome ARTIST ~~~

For information about using Genome ARTIST please check the Manual.pdf located in "$PROJECT_ROOT/documentation/Manual.pdf"
