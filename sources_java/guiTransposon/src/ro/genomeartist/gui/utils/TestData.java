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

package ro.genomeartist.gui.utils;

import ro.genomeartist.gui.controller.finalresult.FinalResultItem;
import ro.genomeartist.gui.controller.finalresult.FinalResultSet;
import ro.genomeartist.gui.controller.genes.GeneItem;
import ro.genomeartist.gui.controller.finalresult.IntervalMappingItem;
import ro.genomeartist.gui.controller.finalresult.IntervalMappingSet;
import ro.genomeartist.gui.controller.partialresult.PartialResultItem;
import ro.genomeartist.gui.controller.partialresult.PartialResultSet;
import ro.genomeartist.gui.controller.query.InfoQuery;
import ro.genomeartist.gui.controller.query.MainResult;
import ro.genomeartist.gui.controller.settings.AlgorithmParams;
import ro.genomeartist.gui.controller.settings.GeneralSettings;
import ro.genomeartist.gui.controller.settings.SearchFile;
import ro.genomeartist.gui.controller.settings.SearchFileSet;
import java.io.File;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author iulian
 */
public class TestData {
    private static final String charset = "!0123456789abcdefghijklmnopqrstuvwxyz";
    private static final String nucleotide = "ACGT-";
    private static final String relations = "| ";
    public static final int QUERY_LENGTH = 400;
    public static final int PARTIAL_RESULTS = 30;
    public static final int SEARCH_FILES = 30;
    public static final int FINAL_RESULTS = 20;
    public static final int GENOME_OFFSET = 10000;
    private static final int DATE_YEAR = 200;
    private static final int DATE_MONTH = 11;
    private static final int DATE_DAY = 20;

    private static Random rand = new Random(System.currentTimeMillis());

     /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *      TEST METHODS Obtin setari de test
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Obtin un obiect GeneralSettings cu setari generale
     * @return
     */
    public static GeneralSettings getTestGeneralSettings() {
        GeneralSettings generalSettings = new GeneralSettings();

        //Parametrii de algoritm
        generalSettings.algorithmParams = getTestAlgorithmParams();

        //Pachetele de fisiere
        generalSettings.genomFiles = getTestSearchFileSet(false);
        generalSettings.transposonFiles = getTestSearchFileSet(true);

        return generalSettings;
    }

    public static AlgorithmParams getTestAlgorithmParams() {
        AlgorithmParams algorithmParams = new AlgorithmParams();

        //Parametrii de algoritmi
        algorithmParams.setOffsetZero(rand.nextInt(SEARCH_FILES));
        algorithmParams.setScoreMatch(rand.nextInt(SEARCH_FILES));
        algorithmParams.setScoreMismatch(rand.nextInt(SEARCH_FILES));
        algorithmParams.setLengthModifier(rand.nextInt(SEARCH_FILES));
        algorithmParams.setPickingDepth(rand.nextInt(SEARCH_FILES));

        return algorithmParams;
    }

    /**
     * Obtin un obiect SearchFileSet de test
     * @return
     */
    public static SearchFileSet getTestSearchFileSet(boolean isTransposon) {
        SearchFileSet searchFileSet;
        int n = rand.nextInt(SEARCH_FILES);
        searchFileSet = new SearchFileSet();

        //Creez un set de rezultate partiale
        for (int i = 0; i < n; i++) {
            searchFileSet.add(getTestSearchFile(isTransposon));
        }

        return searchFileSet;
    }

    /**
     * Obtin un obiect SearchFile de test
     * @return
     */
    public static SearchFile getTestSearchFile(boolean isTransposon) {
        SearchFile searchFile = new SearchFile();

        searchFile.fileTitle = "Title_"+getRandomString(rand, 3);
        searchFile.rawLocation = new File("./resurse/"+getRandomString(rand, 3)+".raw");
        searchFile.geneLocation = new File("./resurse/"+getRandomString(rand, 3)+".gene");
        searchFile.isTransposon = isTransposon;

        return searchFile;
    }

     /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *      TEST METHODS Obtin informatii despre query
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Obtin un obiect MainResult de test
     * @return
     */
    public static MainResult getTestMainResult() {
        MainResult mainResult = new MainResult();

        mainResult.infoQuery = getTestInfoQuery();
        mainResult.bestResult = getTestFinalResultItem();
        mainResult.finalResultSet = getTestFinalResultSet();
        mainResult.partialResultSet = getTestPartialResultSet();

        return mainResult;
    }

    /**
     * Obtin un obiect Info Query de test
     * @return
     */
    public static InfoQuery getTestInfoQuery() {
        InfoQuery infoQuery = new InfoQuery();

        infoQuery.queryName = "Query_"+getRandomString(rand, 3);
        infoQuery.query = getRandomNucleotide(rand,QUERY_LENGTH);
        infoQuery.searchDate = new Date(
                rand.nextInt(DATE_YEAR),
                rand.nextInt(DATE_MONTH),
                rand.nextInt(DATE_DAY));
        infoQuery.timpExecutie = rand.nextInt(FINAL_RESULTS);
        infoQuery.finalResultNumber = rand.nextInt(FINAL_RESULTS);
        infoQuery.partialResultNumber = rand.nextInt(PARTIAL_RESULTS);

        return infoQuery;
    }

     /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *      TEST METHODS Obtin rezultate partiale
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Obtin un set de rezultate partiale de test
     * @return
     */
    public static PartialResultSet getTestPartialResultSet() {
        PartialResultSet partialResultSet;
        int n = rand.nextInt(PARTIAL_RESULTS);
        partialResultSet = new PartialResultSet();

        //Creez un set de rezultate partiale
        for (int i = 0; i < n; i++) {
            partialResultSet.add(getTestPartialResultItem());
        }

        return partialResultSet;
    }

    /**
     * Obtin un ITem de Rezultat partial
     * @return
     */
    public static PartialResultItem getTestPartialResultItem() {
        PartialResultItem partialResultItem;

        //Creez un item de test
        partialResultItem = new PartialResultItem();
        partialResultItem.setPozitieStartQuery(rand.nextInt(QUERY_LENGTH));
        partialResultItem.setPozitieStartGenom(GENOME_OFFSET+rand.nextInt(GENOME_OFFSET));
        partialResultItem.setLengthQuery(rand.nextInt(QUERY_LENGTH - partialResultItem.getPozitieStartQuery()));
        partialResultItem.setLengthGenom(rand.nextInt(QUERY_LENGTH));

        //Informatii de afisare
        partialResultItem.setLength(rand.nextInt(QUERY_LENGTH));
        partialResultItem.setScore(rand.nextInt(QUERY_LENGTH));

        //Relatia intre ele
        partialResultItem.setOut1(getRandomNucleotide(rand, partialResultItem.getLength()));
        partialResultItem.setRelation(getRandomRelation(rand, partialResultItem.getLength()));
        partialResultItem.setOut2(getRandomNucleotide(rand, partialResultItem.getLength()));

        //Informatii despre provenienta
        partialResultItem.setFisierOrigine("Arm_"+getRandomString(rand, 2));
        partialResultItem.setIsComplement(getRandomBoolean(rand));
        partialResultItem.setIsTransposon(getRandomBoolean(rand));
        partialResultItem.setLengthInitialQuery(QUERY_LENGTH);

        return partialResultItem;
    }

      /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *      TEST METHODS Obtin rezultate finale
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Obtin un set de rezultate finale
     * @return
     */
    public static FinalResultSet getTestFinalResultSet() {
        FinalResultSet finalResultSet =  new FinalResultSet();
        int n = rand.nextInt(FINAL_RESULTS);

        //Creez un set de rezultate partiale
        for (int i = 0; i < n; i++) {
            FinalResultItem finalResultItem = getTestFinalResultItem();
            finalResultSet.add(finalResultItem);
        }

        return finalResultSet;
    }

    /**
     * Obtin un item rezultat de test
     * @return
     */
    public static FinalResultItem getTestFinalResultItem() {
        FinalResultItem finalResultItem = new FinalResultItem();

        finalResultItem.setScore(rand.nextInt(QUERY_LENGTH));
        finalResultItem.setLengthInitialQuery(QUERY_LENGTH);

        //Informatii despre afisate
        int outTotalLength = finalResultItem.getOutTotalLength();
        finalResultItem.setOutTotalLength(QUERY_LENGTH);
        finalResultItem.setOut1(getRandomNucleotide(rand, outTotalLength));
        finalResultItem.setRelation(getRandomRelation(rand, outTotalLength));
        finalResultItem.setOut2(getRandomNucleotide(rand, outTotalLength));

        finalResultItem.setIntervalMappingSet(getTestIntervalMappingSet(
                finalResultItem.getLengthInitialQuery(), finalResultItem.getOutTotalLength()));

        return finalResultItem;
    }

    /**
     * Obtin o maparea ca unor intervale pe secventa
     * @param lengthInitialQuery
     * @param outStringTotalLength
     * @return
     */
    public static IntervalMappingSet getTestIntervalMappingSet(
            int lengthInitialQuery, int outStringTotalLength) {
        //Clase rezultat
        IntervalMappingSet intervalMappingSet;
        IntervalMappingItem intervalMappingItem;
        //Variabile auxiliare
        int x,y,length;
        
        //Initializez setul de intervale
        intervalMappingSet = new IntervalMappingSet();

        //Creez un set de rezultate partiale
        x = 0; y = 0;
        do {
            //calculez intervalul
            x = y;
            y = x + rand.nextInt(lengthInitialQuery);
            if (y >= lengthInitialQuery)
                y = lengthInitialQuery;

            //Adaug intervalul
            length = y-x;
            intervalMappingItem = getTestIntervalMappingItem(x, length, lengthInitialQuery,
                    x, length, outStringTotalLength, intervalMappingSet);
            intervalMappingSet.add(intervalMappingItem);

        } while ( y < lengthInitialQuery );

        return intervalMappingSet;
    }

    /**
     * Obtin un interval Mapping item de test
     * @return
     */
    public static IntervalMappingItem getTestIntervalMappingItem(
            int offsetQuery, int lengthQuery, int lengthInitialQuery,
            int outStringOffset, int outStringLength, int outStringTotalLength,
            IntervalMappingSet mappingParinte) {
        IntervalMappingItem intervalMappingItem =  new IntervalMappingItem(mappingParinte);

        //Informatii pozitionare
        intervalMappingItem.setFisierOrigine("Arm_"+getRandomString(rand, 2));
        intervalMappingItem.setPozitieQuery(offsetQuery);
        intervalMappingItem.setPozitieStartGenom(GENOME_OFFSET+rand.nextInt(GENOME_OFFSET));
        intervalMappingItem.setLengthQuery(lengthQuery);
        intervalMappingItem.setLengthGenom(rand.nextInt(lengthInitialQuery));
        intervalMappingItem.setLengthInitialQuery(lengthInitialQuery);

        //Legatura cu genele
        intervalMappingItem.setClosestUpstream(getTestGeneItem());
        intervalMappingItem.setClosestDownstream(getTestGeneItem());
        intervalMappingItem.addInsideGene(getTestGeneItem());

        //Informatii afisare
        intervalMappingItem.setOutStringOffset(outStringOffset);
        intervalMappingItem.setOutStringLength(outStringLength);
        intervalMappingItem.setOutStringTotalLength(outStringTotalLength);

        //Informatii despre tipul de aliniere
        intervalMappingItem.setIsComplement(getRandomBoolean(rand));
        intervalMappingItem.setIsTransposon(getRandomBoolean(rand));

        return intervalMappingItem;
    }

    /**
     * Obtin o gena de test
     * @return
     */
    public static GeneItem getTestGeneItem() {
        GeneItem geneItem;
        geneItem = new GeneItem();

        geneItem.setName("Gene_"+getRandomString(rand, 3));
        geneItem.setFisierOrigine("Arm_"+getRandomString(rand, 2));
        geneItem.setLocationStart(rand.nextInt(GENOME_OFFSET));
        geneItem.setLocationEnd(rand.nextInt(GENOME_OFFSET));
        geneItem.setIsComplement(getRandomBoolean(rand));
        geneItem.setCyto("Cyto-"+getRandomString(rand, 4));
        geneItem.setGeneId("FbID_"+getRandomString(rand, 4));

        return geneItem;
    }

     /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *      HELPER METHODS
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

     /**
      * Generez stringuri random
      */
     public static String getRandomString(Random rand,int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int pos = rand.nextInt(charset.length());
            sb.append(charset.charAt(pos));
        }
        return sb.toString();
    }

     /**
      * Generez stringuri de nucleotide
      */
     public static String getRandomNucleotide(Random rand,int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int pos = rand.nextInt(nucleotide.length());
            sb.append(nucleotide.charAt(pos));
        }
        return sb.toString();
    }

     /**
      * Generez relatia intre nucleotide
      */
     public static String getRandomRelation(Random rand,int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int pos = rand.nextInt(relations.length());
            sb.append(relations.charAt(pos));
        }
        return sb.toString();
    }

     /**
      * Generez un boolean
      */
     public static boolean getRandomBoolean(Random rand) {
        int randint = rand.nextInt();
        if (randint % 2 == 0) return true;
        else return false;
    }
}
