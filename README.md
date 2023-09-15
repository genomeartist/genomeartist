# genomeartist
Genome ARTIST (ARtificial Transposon Insertion Site Tracker)
Is a new bioinformatics tool originally developed in order to allow a rapid detection of insertional mutations generated in the genome of Drosophila melanogaster by means of artificial P element derivatives. 

Aside from the large gene disruption projects (BDGP, http://www.fruitfly.org; FlyBase, http://www.flybase.org), many fly laboratories run small scale transposon mutagenesis screenings. Basically, mobilization with a transposase source of artificial molecular constructs (derived from a natural P mobile element or from other transposons) induces insertional mutations in the germline. Many different mutant strains are derived from affected parents using classical genetic crosses and, in the end, their putative useful mutations are analyzed by inverse PCR and sequencing. The sequencing product is a mixture of information, where part of it pertains to the fruit fly canonical genome and the rest of it belongs to a specific artificial element. The most critical aspect of sequence analysis is to detect the exact border between the genomic and transposon DNA, equivalent with identification of the insertion site at the nucleotide level. Sequencing products are not always perfect and a few artifact bases mismatches may impair a fluent insertion mapping. Most commonly, the sequences of interest are aligned with BLAST (http://blast.ncbi.nlm.nih.gov) or BLAT (http://www.genome.ucsc.edu) against D. melanogaster official genome, which do not contain neither natural nor artificial P transposons. Often, additional manual sequence annotation is needed in order to finish an accurate insertion mapping and here is when Genome ARTIST enters the scene and offers a bit of help. The query sequence is simultaneously compared offline against both the D. melanogaster genome and the specific transposon sequence, partial sequence alignments are matched to each other, relative scores of alignments are calculated and the best mixed sequence with the genomic and transposon coordinates is offered to the user. Different colors are used for genomic versus transposon fragments, and an intuitive list of results and details is also depicted. One may easy observe the absolute site of insertion, the gene affected by the transposon insertion, and also the genes located in the close vicinity of the insertion. Special biological conditions occurring during mutagenesis experiments, as transposon reinsertions into the original mobile element copy, are not usually detected with other searching algorithms, therefore Genome ARTIST is designed to reveal and to interpret such events. 

To some extent, Genome ARTIST is an alternative for the classical alignment algorithms and may be exploited for checking the specificity of short sequences as primers or probes. Last but not the least, aficionados of different model organisms may use the abilities of Genome ARTIST by loading other genomes and/or specific transposons. The performances of Genome ARTIST were tested on the genomes of Saccharomyces cerevisiae, Caenorhabditis elegans, Drosophila pseudoobscura, Ciona intestinalis, Danio rerio and Arabidopsis thaliana and offers similar results to those obtained on D. melanogaster. Additionally, pairwise comparative alignments may be performed among sequences pertaining to various species aside from D. melanogaster, allowing the identification of structural orthologous. 


Genome ARTIST is the result of an interdisciplinary collaboration between researchers from the Department of Genetics, University of Bucharest, and computer engineers, graduates of "Politehnica" University of Bucharest, PUB. The authors of the software are Alexandru Al. Ecovoiu, Iulian Constantin Ghionoiu, Andrei Mihai Ciuca, Iulian Cristian Ghita and Attila Cristian Ratiu.

Genome ARTIST pre-compiled versions are available and can be donwloaded at [https://genomeartist.ro/](https://genomeartist.ro/). 


## INSTRUCTIONS FOR COMPILATION Genome ARTIST_v2 LINUX

To compile the Genome ARTIST software, you need jdk-8. To install this version, and to replace the current one, you need to run the following commands:

1. Install jdk-8:

        sudo apt-get install openjdk-8-jdk

2. List installed versions of java

        update-alternatives --list java

3. Configure java installation:

        sudo update-alternatives --install /usr/bin/java java /usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java 1

4. Display installed versions of java and configure the version you want to use:

        update-alternatives --display java

        sudo update-alternatives --config java

Choose jdk-8 entering the corresponding number.

5. Check the current version:

        java -version

6. Next, download the uncompiled package of Genome ARTIST from GitHub: https://github.com/genomeartist/genomeartist. Afterwards, install the compilation dependencies by running the following commands:

        sudo apt install make
        sudo apt install ant
        sudo apt install build-essential

7. In the sources_c directory run the following command:
        
        make

8. In the ant_build directory run the following command:
        
        ant genomeless

Compilation generates the genomeless directory where you find the Genome-ARTIST.sh file. Before accessing it, make sure it is executable.



### INSTRUCTIONS FOR COMPILATION Genome ARTIST_v2 MacOS (tested on High Sierra 10.13)

1. Download the uncompiled package of Genome ARTIST_v2 from GitHub.

2. Replace the makefile from sources_c directory with the makefile from sources_c/MacOS, and run *make* command in the terminal (in the sources_c directory).

3. In the ant_build directory run the *ant genomeless* command (if MacOS version is too old and *brew install ant* is failing, you have to manual download the latest version of ant (from https://dlcdn.apache.org//ant/binaries/apache-ant-1.10.12-bin.tar.gz), uncompress it and run ant directly from the bin folder.

4. In the final step, copy and replace the following files: cleaner, client, data_hashing_utility, run_expansion_generation and server; from sources_c directory to GA_v2 installation directory (genomeless).



### INSTRUCTIONS FOR COMPILATION Genome ARTIST_v2 Windows

1. Download the uncompiled package of Genome ARTIST_v2 from GitHub (https://github.com/genomeartist/genomeartist) and the compiled package for Linux from genomeartist.ro (https://genomeartist.ro/download.html).

2. Download and install **Visual Studio Community 2017**. Open Visual Studio Community 2017 and:
 
      2.1. *Select File -> Open -> /Project/Solution*
           Pick ***genomeartist-master\sources_c\Windows\GenomeArtist.sln***
       
      2.2. *Go to Build -> Configuration Manager*
           Active solution configuration: pick **Release**
           Active solution platform: pick **x64**
       
      2.3. *Go to Build, click Rebuild Solution*.
  
3. Copy the following files from uncompiled ***genomeartist-master\sources_c\Windows\x64\Release*** in the compiled GenomeARTIST directory:
       
      - client.exe as **client**;
      - cleaner.exe as **cleaner**;
      - data_hashing_utility.exe as **data_hashing_utility**;
      - GA_support_service.exe as **GA_support_service**;
      - run_expansion_generation.exe as **run_expansion_generation**;
      - server.exe as **server**.

4. Run **genomeARTIST.jar**.
