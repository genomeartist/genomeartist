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

package ro.genomeartist.gui.controller.externalcalls;

import ro.genomeartist.gui.controller.externalcalls.actions.RunSearchCallable;
import ro.genomeartist.gui.controller.externalcalls.actions.RunDataHashingUtilityCallable;
import ro.genomeartist.components.swingworkers.progressworker.AbstractProgressCallable;
import ro.genomeartist.gui.controller.externalcalls.actions.AddSearchFileCallable;
import ro.genomeartist.gui.controller.externalcalls.actions.AddSearchFolderCallable;
import ro.genomeartist.gui.controller.externalcalls.actions.CreateSearchFileCallable;
import ro.genomeartist.gui.controller.externalcalls.actions.DeleteMultipleFilesCallable;
import ro.genomeartist.gui.controller.externalcalls.actions.DeleteSearchFileCallable;
import ro.genomeartist.gui.controller.externalcalls.actions.RunExpansionGenerationCallable;
import ro.genomeartist.gui.controller.externalcalls.actions.StartServerCallable;
import ro.genomeartist.gui.utils.ReadOnlyConfiguration;
import ro.genomeartist.gui.controller.genes.GeneItem;
import ro.genomeartist.gui.controller.genes.GeneVector;
import ro.genomeartist.gui.controller.query.MainResult;
import ro.genomeartist.gui.controller.query.SearchQuery;
import ro.genomeartist.gui.controller.settings.SearchFile;
import ro.genomeartist.gui.controller.settings.SearchFileSequence;
import ro.genomeartist.gui.controller.settings.SearchFolder;
import ro.genomeartist.gui.utils.MyUtils;
import ro.genomeartist.gui.utils.StringUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Clasa ce contine metodele de a executa functii din exterior
 * @author iulian
 */
public class ExternalLink {
    //Constante
    private static final String FASTA_MARKER = ">";
    private static final String FIELD_SEPARATOR = " ; ";
    //Regexurile
    private static final Pattern REGEX_NAME = Pattern.compile("name=([^;]*);");
    private static final Pattern REGEX_ID = Pattern.compile("ID=([^;]*);");
    private static final Pattern REGEX_LOCUS =
            Pattern.compile("loc=[^;:]*:(complement)*[\\(]*(\\d+)\\.{2}(\\d+)[\\)]*;");
    private static final Pattern REGEX_CYTO =Pattern.compile("derived_computed_cyto=([^ ;]+)%");

    //VALORI HARDCODATE
    public static final int PARAM_WINDOW_SIZE = 10;
    public static final int PARAM_EXPANDED_SIZE = 4;

    //Procese importante
    private static Process serverProcess = null;
    private static Process searchProcess = null;

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Operatiuni server
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Obtin operatiunea de pronire server
     * @return
     */
    public static AbstractProgressCallable<Boolean> getStartServerCallable() {
        return new StartServerCallable();
    }
    
    /**
     * Opreste serverul de cautare
     *    - dealoca memoria partajata
     */
    public static void stopServer() {
       //Inchid procesul trecut
       if (serverProcess != null) serverProcess.destroy();
        
       //Rulez cleanerul
       String args[]= new String[1];
       args[0] = ReadOnlyConfiguration.getString("cleanerFile");

       ProcessBuilder pbuild;

        try {
            pbuild = new ProcessBuilder(args);
            Process process = pbuild.start();

            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;

            System.out.printf("Output of running %s is:",
                    Arrays.toString(args));

            while ((line = br.readLine()) != null) {
                System.out.println(line);
                //Consume content
            }
            System.out.println("Server stopped");
        } catch (IOException iOException) {
            System.err.println("Eroare la oprire server");
            iOException.printStackTrace();
        }
    }

    /**
     * Incerc oprirea clientului
     */
    public static void stopClient() {
        if (searchProcess != null) searchProcess.destroy();
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *     Operatiuni client
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Obtin operatiunea de cautare
     * @return
     */
    public static AbstractProgressCallable<Boolean> getSearchCallable(
            SearchQuery searchQuery, File outputFile) {
        return new RunSearchCallable(searchQuery, outputFile);
    }

    /**
     * Setez procesul de cautare pentru a stii sa il inchid
     * @param newProcess
     */
    public static void setSearchProcess(Process newProcess) {
        searchProcess = newProcess;
    }

    /**
     * Setez procesul de cautare pentru a stii sa il inchid
     * @param newProcess
     */
    public static void setServerProcess(Process newProcess) {
        serverProcess = newProcess;
    }
    
    /**
     * Citesc fisierul de rezultate
     * @param file
     * @return
     */
    public static MainResult readResultFile(File file) {
        return ResultParsing.readResult(file);
    }

    /**
     * Salveaza cel mai bun rezultat
     * @param mainResult
     */
    public static void saveBestResult(MainResult mainResult) {
        ResultParsing.saveBestResult(mainResult);
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *     Operatiuni pe utilitare
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Obtin operatiunea de hashing
     * @return
     */
    public static AbstractProgressCallable<Boolean> getDataHashingCallable(
            File rawFile) {
        return new RunDataHashingUtilityCallable(rawFile);
    }

    /**
     * Rulez expansion generation-ul
     */
    public static AbstractProgressCallable<Boolean> getExpansionGenerationCallable() {
        return new RunExpansionGenerationCallable();
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *     Propagarea schimbarilor
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Creez un fisier corespunzator cu un query
     * Acesta este cazul la transposoni, dar nu neaparat
     * @param name
     * @param query
     * @return
     */

    public static AbstractProgressCallable<SearchFile>
            getCreateSearchFileCallable(SearchFileSequence searchFileSequence) {
        return new CreateSearchFileCallable(searchFileSequence);
    }

    /**
     * Adaug fisierul in intrarile curente
     * @param searchFile
     */
    public static AbstractProgressCallable<Boolean>
            getAddSearchFileCallable(SearchFile searchFile) {
        return new AddSearchFileCallable(searchFile);
    }

    /**
     * Adaug fisierul in intrarile curente
     * @param searchFile
     */
    public static AbstractProgressCallable<Vector<SearchFile>>
            getAddSearchFolderCallable(SearchFolder searchFolderRaw, boolean isTransposon) {
        return new AddSearchFolderCallable(searchFolderRaw, isTransposon);
    }

    /**
     * Sterge intrarile asociate acestui fisier
     * @param searchFile
     */
    public static AbstractProgressCallable<Boolean>
            getDeleteSearchFileCallable(SearchFile searchFile) {
        return new DeleteSearchFileCallable(searchFile);
    }

    /**
     * Sterge intrarile asociate acestui fisier
     * @param searchFile
     */
    public static AbstractProgressCallable<Boolean>
            getDeleteMultipleFileCallable(Vector<SearchFile> searchFiles) {
        return new DeleteMultipleFilesCallable(searchFiles);
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *     Operatii pe folder
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

     /**
      * Ontin un vector de fisiere de secventa dintr-un folder
      * @param folder
      * @return
      */
     public static Vector<File> getSequencesFromFolder(File folder) {
        Vector<File> fileVector = new Vector<File>();

        FileFilter rawFilter = new RawFileFilter();
        File[] templateFiles = folder.listFiles(rawFilter);
        for (int i = 0; i < templateFiles.length; i++) {
            File file = templateFiles[i];
            fileVector.add(file);
        }

        return fileVector;
     }

     /**
      * Obtin fisierul de gene asociat unei secvente raw
      * @param rawFile
      * @return
      */
     public static File getGeneFileForRaw(File rawFile) {
         String basename = MyUtils.getBasename(rawFile);

         File assumedFile = new File(basename+MyUtils.GENE_SUFFIX);
         if (assumedFile.exists())
            return assumedFile;
         else return null;
     }


    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *     Operatii pe fisiere
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

     /**
      * Parsez fisierul fasta si il scriu intr-un fisier
      * @param fastaFile
      * @param geneFile
      */
     public static int fastaToGeneFile(File fastaFile,File geneFile) {
       int counter = 0; //contorul de inregistrari
         
       try{
          BufferedReader in = new BufferedReader(new FileReader(fastaFile));
          BufferedWriter out = new BufferedWriter(new FileWriter(geneFile, false));

          //Variabile ce trebuiesc completate
          String name;
          String id;    //Flybase id
          String start_locus;
          String end_locus;
          String strand;
          String cyto;

          //~~~~~~~~~ populez vectorul de gene ~~~~~~~~~~
          String line;
          GeneVector geneVector = new GeneVector();
          GeneItem geneItem;
          while ((line = in.readLine()) != null) {
            if (line.startsWith(FASTA_MARKER)) {
                name = StringUtils.regexFind(REGEX_NAME, line, 1);
                if (name == null) continue;
                id = StringUtils.regexFind(REGEX_ID, line, 1);
                if (id == null) id = "none";
                start_locus = StringUtils.regexFind(REGEX_LOCUS, line, 2);
                if (start_locus == null) continue;
                end_locus = StringUtils.regexFind(REGEX_LOCUS, line, 3);
                if (end_locus == null) continue;
                strand = StringUtils.regexFind(REGEX_LOCUS, line, 1);
                cyto = StringUtils.regexFind(REGEX_CYTO, line, 1);
                if (cyto == null) cyto = "none";

                //Creez o gena din rezultate
                geneItem = new GeneItem();
                geneItem.setName(name);
                geneItem.setGeneId(id);
                geneItem.locationStart = Integer.parseInt(start_locus) - 1;
                geneItem.locationEnd = Integer.parseInt(end_locus) - 1;
                geneItem.setCyto(cyto);
                if (strand == null) geneItem.setIsComplement(false);
                    else geneItem.setIsComplement(true);

                //Adaug in vector
                geneVector.add(geneItem);
            }
          }

          //Sortez vectorul
          Collections.sort(geneVector, new Comparator<GeneItem>() {

                public int compare(GeneItem o1, GeneItem o2) {
                    return o1.getLocationStart() - o2.getLocationStart();
                }
            });

          //Scriu vectorul in fisier
          Iterator <GeneItem> iterator = geneVector.iterator();
          while(iterator.hasNext()) {
            geneItem = iterator.next();

            //Write gene to file
            out.write(geneItem.getName());
            out.write(FIELD_SEPARATOR);
            out.write(geneItem.getGeneId());
            out.write(FIELD_SEPARATOR);
            out.write(geneItem.locationStart+"");
            out.write(FIELD_SEPARATOR);
            out.write(geneItem.locationEnd+"");
            out.write(FIELD_SEPARATOR);
            if (geneItem.isComplement()) out.write("R");
            else out.write("F");
            out.write(FIELD_SEPARATOR);
            out.write(geneItem.getCyto());
            out.newLine();
          }

          //Actualizez contorul
          counter = geneVector.size();

          //Inchid streamurile
          in.close();
          out.close();
        }
        catch(FileNotFoundException ex){
          System.out.println(ex.getMessage() + " in the specified directory.");
        }
        catch(IOException e){
          System.out.println(e.getMessage());
        }

       //Intorc numarul de inregistrari
        return counter;
     }
     
     /**
      * Incarca fisier fasta
      * @param fastaFile 
      */
     public static String readFastaFile(File fastaFile) {
        String line, output = "";
        try {
            BufferedReader in = new BufferedReader(new FileReader(fastaFile));
            while ((line = in.readLine()) != null) {
                output += line + "\n";
            }
        }
        catch(FileNotFoundException ex){
          System.out.println(ex.getMessage() + " in the specified directory.");
        }
        catch(IOException e){
          System.out.println(e.getMessage());
        }
        return output;
     }

     /**
      * Copiaza un fisier
      * @param fastaFile       Fisierul sursa
      * @param geneFile  Fisierul destinatie
      */
     public static void copyRawFile(File sourceFile, File destinationFile) {
        try{
          InputStream in = new FileInputStream(sourceFile);
          OutputStream out = new FileOutputStream(destinationFile);

          byte[] buf = new byte[1024];
          int len;
          while ((len = in.read(buf)) > 0){
            //Convertesc literele in uppercase
            for (int i = 0; i < buf.length; i++) {
                  byte b = buf[i];
                  buf[i] = byteToUppercase(b);
              }
            out.write(buf, 0, len);
          }
          in.close();
          out.close();
        }
        catch(FileNotFoundException ex){
          System.out.println(ex.getMessage() + " in the specified directory.");
          System.exit(0);
        }
        catch(IOException e){
          System.out.println(e.getMessage());
        }
     }

     /**
      * Convertesc o nucleotida in uppercase
      * @param b
      * @return
      */
     public static byte byteToUppercase(byte b) {
         switch (b) {
             case 'a':case 'A':
                 return 'A';
             case 'c':case 'C':
                 return 'C';
             case 'g':case 'G':
                 return 'G';
             case 't':case 'T':
                 return 'T';
             default:
                 return b;
         }
     }

     /**
      * Copiaza un fisier
      * @param fastaFile       Fisierul sursa
      * @param geneFile  Fisierul destinatie
      */
     public static void copyFile(File sourceFile, File destinationFile) {
        try{
          InputStream in = new FileInputStream(sourceFile);
          OutputStream out = new FileOutputStream(destinationFile);

          byte[] buf = new byte[1024];
          int len;
          while ((len = in.read(buf)) > 0){
            out.write(buf, 0, len);
          }
          in.close();
          out.close();
        }
        catch(FileNotFoundException ex){
          System.out.println(ex.getMessage() + " in the specified directory.");
          System.exit(0);
        }
        catch(IOException e){
          System.out.println(e.getMessage());
        }
     }

     /**
      * Creeaza un fisier gol
      * @param newFile
      */
     public static void createEmptyFile(File newFile) {
        BufferedWriter out;
        try {
            out = new BufferedWriter(new FileWriter(newFile, false));
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(ExternalLink.class.getName()).log(Level.SEVERE, null, ex);
        }
     }

     /**
      * Scriu un fisier ce contine doar un string
      * @param newFile
      * @param string
      */
     public static void writeStringToFile(File newFile, String string, boolean append) {
        BufferedWriter out;
        try {
            out = new BufferedWriter(new FileWriter(newFile, append));
            out.write(string);
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(ExternalLink.class.getName()).log(Level.SEVERE, null, ex);
        }
     }

     /**
      * JUST FOR TESTING
      * @param args
      */
     public static void main(String[] args) {
        String inputFasta =
                ">FBgn0032620 type=gene; " +
                "loc=2L:complement(16900535..16902189);" +
                " ID=FBgn0032620; " +
                "name=CG12288; " +
                "dbxref=FlyBase:FBgn0032620,FlyBase:FBan0012288,FlyBase_Annotation_IDs:CG12288,GB_protein:AAF53587,GB:AA801811,GB:AY118842,GB_protein:AAM50702,UniProt/TrEMBL:Q8MSG1,UniProt/TrEMBL:Q9VJG2,INTERPRO:IPR000504,INTERPRO:IPR012677,EntrezGene:35027,DroID:FBgn0032620,DRSC:FBgn0032620,FlyAtlas:CG12288-RA,FlyMine:FBgn0032620,GenomeRNAi_gene:35027,modMine:FBgn0032620; " +
                "derived_computed_cyto=36B4-36B4%3B Limits computationally determined from genome sequence between @P{lacW}Mhc<up>k10423</up>@%26@P{lacW}Cas<up>k03902</up>@ and @P{lacW}Aac11<up>k06710</up>@; " +
                "gbunit=AE014134; " +
                "MD5=d1bba921ea9542c7b890ba22cc2203f5; " +
                "length=1655; " +
                "release=r5.26; " +
                "species=Dmel; ";

        String alternateFasta =
                ">FBgn0011823 type=gene; " +
                "loc=2L:10056906..10060097; " +
                "ID=FBgn0011823; " +
                "name=Pen; " +
                "dbxref=FlyBase:FBgn0011823,FlyBase:FBan0004799,FlyBase_Annotation_IDs:CG4799,GB_protein:AAF52853,FlyBase:FBgn0014274,FlyBase:FBgn0026803,FlyBase:FBgn0044610,GB:AA695740,GB:AA803647,GB:AA820973,GB:AA942615,GB:AC005734,GB:AC009744,GB:AI109333,GB:AI257091,GB:AI259659,GB:AI455357,GB:AI944500,GB:AI945098,GB:AI945110,GB:AI945973,GB:AI946350,GB:AI946675,GB:AI946703,GB:AI946866,GB:AI947113,GB:AQ026078,GB:AW944011,GB:AX154909,GB:AY061543,GB_protein:AAL29091,GB:BT003258,GB_protein:AAO25015,GB:CL610233,GB:CZ469793,GB:CZ475481,GB:U12269,GB_protein:AAA85260,GB:X85752,GB_protein:CAA59753,UniProt/Swiss-Prot:P52295,INTERPRO:IPR000225,INTERPRO:IPR002652,INTERPRO:IPR011989,INTERPRO:IPR016024,EntrezGene:34338,BIOGRID:60430,DroID:FBgn0011823,DRSC:FBgn0011823,FlyAtlas:CG4799-RA,FlyMine:FBgn0011823,GenomeRNAi_gene:34338,modMine:FBgn0011823; " +
                "derived_computed_cyto=31A1-31A2%3B Limits computationally determined from genome sequence between @P{EP}CG4747<up>EP594</up>@%26@P{lacW}l(2)k13305<up>k13305</up>@ and @P{lacW}l(2)k06324<up>k06324</up>@; " +
                "gbunit=AE014134; " +
                "MD5=095aafb4ea812db94f9e94f330f35050; " +
                "length=3192; " +
                "release=r5.26; " +
                "species=Dmel;";

        String problemQuery = ">FBgn0041004 type=gene; " +
                "loc=2L:22736958..22742972; " +
                "ID=FBgn0041004; " +
                "name=CG17715; " +
                "dbxref=FlyBase:FBgn0041004,FlyBase:FBan0017715,FlyBase_Annotation_IDs:CG17715,GB_protein:EAA46033,GB_protein:EAA46029,GB_protein:EAA46031,GB_protein:EAA46032,GB_protein:EAA46028,GB_protein:EAA46030,FlyBase:FBgn0041003,FlyBase:FBgn0047070,FlyBase:FBgn0063069,GB:AABU01002768,GB_protein:EAA46028,GB_protein:EAA46029,GB_protein:EAA46030,GB_protein:EAA46031,GB_protein:EAA46032,GB_protein:EAA46033,GB:AY071582,GB_protein:AAL49204,GB:AY113392,GB_protein:AAM29397,GB:BI166931,GB:BI375866,UniProt/TrEMBL:Q5LJR0,UniProt/TrEMBL:Q5LJR2,UniProt/TrEMBL:Q8SYF7,INTERPRO:IPR019537,EntrezGene:3355129,DRSC:FBgn0041004,FlyAtlas:CG17715-RD,FlyMine:FBgn0041004,modMine:FBgn0041004; " +
                "MD5=d09e16c5f0bdeac27291109cbb4dd079; " +
                "length=6015; " +
                "release=r5.26; " +
                "species=Dmel; ";

        Pattern pattern = Pattern.compile("derived_computed_cyto=([^ ;]+)%");
        //String match = MyUtils.regexFind(pattern, inputFasta, 1);
        String match = StringUtils.regexFind(pattern, alternateFasta, 1);
        System.out.println(match);
     }
}
