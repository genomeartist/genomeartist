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

import ro.genomeartist.gui.controller.finalresult.FinalResultItem;
import ro.genomeartist.gui.controller.finalresult.FinalResultSet;
import ro.genomeartist.gui.controller.genes.GeneItem;
import ro.genomeartist.gui.controller.finalresult.IntervalMappingItem;
import ro.genomeartist.gui.controller.finalresult.IntervalMappingSet;
import ro.genomeartist.gui.controller.partialresult.PartialResultItem;
import ro.genomeartist.gui.controller.partialresult.PartialResultSet;
import ro.genomeartist.gui.controller.query.InfoQuery;
import ro.genomeartist.gui.controller.query.MainResult;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

/**
 *
 * @author iulian
 */
public class ResultParsing {
    private static final int STRING_CONST = 128;
    private static final int STRING_GENE_CONST = 64;
    private static final byte DIRECTION_FORWARD = 'F';
    private static final byte DIRECTION_COMPLEMENT = 'R';

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Functiile publice
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /**
     * Obtin rezultatul unei cautari
     * @return null in caz de eroare
     */
    public static MainResult readResult(File file) {
        try {
            MainResult mainResult = new MainResult();
            RandomAccessFile resultFile = new RandomAccessFile(file, "r");

            int offsetInfoQuery;
            int offsetFinalResultSet;
            int offsetPartialResultSet;
            int offsetBestResult;
            byte auxByte;
            char auxChar;
            String auxString;

            offsetInfoQuery = littleEndianToBigEndian(resultFile.readInt());
            offsetFinalResultSet = littleEndianToBigEndian(resultFile.readInt());
            offsetPartialResultSet = littleEndianToBigEndian(resultFile.readInt());
            offsetBestResult = littleEndianToBigEndian(resultFile.readInt());

            //Citesc info query-ul
            resultFile.seek(offsetInfoQuery);
            InfoQuery infoQuery = readInfoQuery(resultFile);
            
            //Citesc final result set
            resultFile.seek(offsetFinalResultSet);
            FinalResultSet finalResultSet = readFinalResultSet(
                    resultFile, infoQuery.query.length());
            
            //Citesc partial result
            resultFile.seek(offsetPartialResultSet);
            PartialResultSet partialResultSet = readPartialResultSet(
                    resultFile, infoQuery.query.length());

            //Citesc best result
            resultFile.seek(offsetBestResult);
            int bestResult = littleEndianToBigEndian(resultFile.readInt());
            System.out.println("bestResult="+bestResult);

            //Compun rezultatul final
            mainResult.infoQuery = infoQuery;
            mainResult.finalResultSet = finalResultSet;
            mainResult.partialResultSet = partialResultSet;
            if (bestResult != -1)
                mainResult.bestResult = mainResult.finalResultSet.elementAt(bestResult);
            else mainResult.bestResult = null;

            //Inchidem fisierul
            resultFile.close();

            return mainResult;
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }

    /**
     * Salvez cel mai bun rezultat
     * @param mainResult
     */
    public static void saveBestResult(MainResult mainResult) {
        try {
            File sourceFile = mainResult.backgroundFile;
            int index = mainResult.finalResultSet.indexOf(mainResult.bestResult);

            RandomAccessFile resultFile;
            resultFile = new RandomAccessFile(sourceFile, "rw");

            int offsetInfoQuery;
            int offsetFinalResultSet;
            int offsetPartialResultSet;
            int offsetBestResult;

            offsetInfoQuery = littleEndianToBigEndian(resultFile.readInt());
            offsetFinalResultSet = littleEndianToBigEndian(resultFile.readInt());
            offsetPartialResultSet = littleEndianToBigEndian(resultFile.readInt());
            offsetBestResult = littleEndianToBigEndian(resultFile.readInt());

            resultFile.seek(offsetBestResult);
            resultFile.write(index);

            //Inchidem fisierul
            resultFile.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Citesc Partial Result Set
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /**
     * Citesc un partial result set din fisier
     * @param file
     * @return
     * @throws IOException
     */
    private static PartialResultSet readPartialResultSet(RandomAccessFile file,
            int lengthInitialQuery)
            throws IOException {
        PartialResultSet partialResultSet = new PartialResultSet();

        //Structura fisier
        //int					partialResultsNumber;
        //PartialResultItem[partialResultsNumber]	partialResultSet;

        int partialResultsNumber = littleEndianToBigEndian(file.readInt());
        PartialResultItem partialResultItem;
        for (int i = 0; i < partialResultsNumber; i++) {
            partialResultItem = readPartialResultItem(file,lengthInitialQuery);
            partialResultSet.add(partialResultItem);
        }

        return partialResultSet;
    }

    /**
     * Citesc un partial result item din fisier
     * @param file
     * @return
     * @throws IOException
     */
    private static PartialResultItem readPartialResultItem(RandomAccessFile file,
            int lengthInitialQuery)
            throws IOException {
        PartialResultItem partialResultItem = new PartialResultItem();

        //Structura fisier
        //char[CONST]	fisierOrigine;
        //int 		pozitieQuery;
        //int		pozitieGenom;
        //int		lungimeQuery;
        //int		lungimeGenom;
        //int 		scor;
        //int		outLength;	//Lungimea stringului aliniere
        //char[outLength]	out1;
        //char[outLength]	relation;
        //char[outLength]	out2;
        //char		isTransposon;		//1 daca e transposon, 0 daca nu
        //char		isComplement;		//1 daca e pe catena complementare, 0 daca nu

        partialResultItem.setFisierOrigine(readString(file, STRING_CONST));
        partialResultItem.setPozitieStartQuery(littleEndianToBigEndian(file.readInt()));
        partialResultItem.setPozitieStartGenom(littleEndianToBigEndian(file.readInt()));
        partialResultItem.setLengthQuery(littleEndianToBigEndian(file.readInt()));
        partialResultItem.setLengthGenom(littleEndianToBigEndian(file.readInt()));
        partialResultItem.setScore(littleEndianToBigEndian(file.readInt()));
        partialResultItem.setLength(littleEndianToBigEndian(file.readInt()));
        partialResultItem.setOut1(readString(file, partialResultItem.getLength()));
        partialResultItem.setRelation(readString(file, partialResultItem.getLength()));
        partialResultItem.setOut2(readString(file, partialResultItem.getLength()));
        partialResultItem.setIsTransposon(readBoolean(file));
        partialResultItem.setIsComplement(readBoolean(file));
        partialResultItem.setLengthInitialQuery(lengthInitialQuery);

        return partialResultItem;
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Citesc FInal Result Set
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Citesc un final result set din fisier
     * @param file
     * @return
     */
    private static FinalResultSet readFinalResultSet(RandomAccessFile file,
            int lengthInitialQuery) throws IOException {
        FinalResultSet finalResultSet = new FinalResultSet();
        
        //Structura fisier
        //int					finalResultsNumber;
        //FinalResultItem[finalResultsNumber]	finalResultSet;

        int finalResultNumber = littleEndianToBigEndian(file.readInt());
        FinalResultItem finalResultItem;
        for (int i = 0; i < finalResultNumber; i++) {
            finalResultItem = readFinalResultItem(file, lengthInitialQuery);
            finalResultSet.add(finalResultItem);
        }

        return finalResultSet;
    }

    /**
     * Citesc un final result item din fisier
     * @param file
     * @return
     * @throws IOException
     */
    private static FinalResultItem readFinalResultItem(RandomAccessFile file,
            int lengthInitialQuery) throws IOException {
        FinalResultItem finalResultItem = new FinalResultItem();

        //Structura fisier
        //int						score;		//Scorul obtinut din aliniere
        //int						outTotalLength;	//Marimea stringului de output (aliniere)
        //char[outTotalLength]				out1;
        //char[outTotalLength]				relation;
        //char[outTotalLength]				out2;
        //int						intervalMappingNumber; //Numarul de intervale din care este compus rezultatul
        //IntervalMappingItem[intervalMappingNumber]	intervalMappingSet;

        finalResultItem.setScore(littleEndianToBigEndian(file.readInt()));
        finalResultItem.setOutTotalLength(littleEndianToBigEndian(file.readInt()));
        
        int outTotalLength = finalResultItem.getOutTotalLength();
        finalResultItem.setOut1(readString(file, outTotalLength));
        finalResultItem.setRelation(readString(file, outTotalLength));
        finalResultItem.setOut2(readString(file, outTotalLength));
        finalResultItem.setLengthInitialQuery(lengthInitialQuery);
        int intervalMappingNumber = littleEndianToBigEndian(file.readInt());
        finalResultItem.setIntervalMappingSet(readIntervalMappingSet
                (file, intervalMappingNumber,
                finalResultItem.getLengthInitialQuery(), finalResultItem.getOutTotalLength()));

        return finalResultItem;
    }

    /**
     * Citesc un Interval Mapping set din fisier
     * @param file
     * @param lengthInitialQuery
     * @return
     * @throws IOException
     */
    private static IntervalMappingSet readIntervalMappingSet(RandomAccessFile file,
            int intervalMappingItems,
            int lengthInitialQuery, int outStringTotalLength ) throws IOException {
        IntervalMappingSet intervalMappingSet = new IntervalMappingSet();
        IntervalMappingItem intervalMappingItem;
        for (int i = 0; i < intervalMappingItems; i++) {
            intervalMappingItem = readIntervalMappingItem(file,lengthInitialQuery,
                    outStringTotalLength,intervalMappingSet);
            intervalMappingSet.add(intervalMappingItem);
        }

        return intervalMappingSet;
    }

    /**
     * Citesc un interval mapping item din fisier
     * @param file
     * @param intervalMappingItems
     * @return
     * @throws IOException
     */
    private static IntervalMappingItem readIntervalMappingItem(RandomAccessFile file,
            int lengthInitialQuery,int outStringTotalLength, IntervalMappingSet mappingParinte)
            throws IOException {
        IntervalMappingItem intervalMappingItem =  new IntervalMappingItem(mappingParinte);

        // Structura fisier
        //### INTERVAL MAPPING ITEM ### //Un interval singular
        //char[CONST]	fisierOrigine;
        //int 		pozitieQuery;
        //int		pozitieGenom;
        //int		lungimeQuery;
        //int		lungimeGenom;
        //int		outStringOffset;	//Locatia corespunzatoare in stringul de aliniere
        //int		outStringLength;	//Lungimea corespunzatoare pe aliniere
        //char		isTransposon;		//1 daca e transposon, 0 daca nu
        //char		isComplement;		//1 daca e pe catena complementare, 0 daca nu
        //char		hasInsideGene;
        //GENE ITEM	insideGene;		//doar daca hasInsideGene == 1
        //char 		hasDownstreamGene;
        //GENE ITEM	downstreamGene;		//doar daca hasDownStreamGene == 1
        //char		hasUpstreamGene;
        //GENE ITEM	upstreamGene;		//doar daca hasUpstreamGene == 1
        
        intervalMappingItem.setFisierOrigine(readString(file, STRING_CONST));
        intervalMappingItem.setLengthInitialQuery(lengthInitialQuery);
        intervalMappingItem.setOutStringTotalLength(outStringTotalLength);
        intervalMappingItem.setPozitieQuery(littleEndianToBigEndian(file.readInt()));
        intervalMappingItem.setPozitieStartGenom(littleEndianToBigEndian(file.readInt()));
        intervalMappingItem.setLengthQuery(littleEndianToBigEndian(file.readInt()));
        intervalMappingItem.setLengthGenom(littleEndianToBigEndian(file.readInt()));
        intervalMappingItem.setOutStringOffset(littleEndianToBigEndian(file.readInt()));
        intervalMappingItem.setOutStringLength(littleEndianToBigEndian(file.readInt()));
        intervalMappingItem.setIsTransposon(readBoolean(file));
        intervalMappingItem.setIsComplement(readBoolean(file));
        //Obtin gena inside
        byte insideGeneNo = file.readByte();
        GeneItem insideGene;
        for (int i = 0; i < insideGeneNo; i++) {
            insideGene = readGeneItem(file);
            if (intervalMappingItem.isComplement())
                intervalMappingItem.pushInsideGene(insideGene);
            else intervalMappingItem.addInsideGene(insideGene);
        }
        //Obtin gena downstream
        boolean hasDownstreamGene = readBoolean(file);
        if (hasDownstreamGene) {
            GeneItem downstreamGene = readGeneItem(file);
            intervalMappingItem.setClosestDownstream(downstreamGene);
        } else intervalMappingItem.setClosestDownstream(null);
        //Obtin gena upstream
        boolean hasUpstreamGene = readBoolean(file);
        if (hasUpstreamGene) {
            GeneItem upstreamGene = readGeneItem(file);
            intervalMappingItem.setClosestUpstream(upstreamGene);
        } else intervalMappingItem.setClosestUpstream(null);

        //Intorc intervalul
        return intervalMappingItem;
    }

    /**
     * Citesc o gena din fisier
     * @param file
     * @return
     * @throws IOException
     */
    private static GeneItem readGeneItem(RandomAccessFile file)
            throws IOException {
        GeneItem geneItem = new GeneItem();

        //Structura fisier
        //char[CONST_64]		name;			//Numele genei
        //char[CONST]		fisierOrigine;		//Fisierul in care se afla
        //int			start_position;		//Startul genei
        //int			end_position;		//Sfarsitul genei
        //char[CONST_64]		cyto;			//Cytogenetic map
        //char[CONST_64]		geneId;			//Pentru drosophila, Flybase id
        //int			direction;		//directia : F sau R

        geneItem.setName(readString(file, STRING_GENE_CONST));
        geneItem.setFisierOrigine(readString(file, STRING_CONST));
        geneItem.setLocationStart(littleEndianToBigEndian(file.readInt()));
        geneItem.setLocationEnd(littleEndianToBigEndian(file.readInt()));
        geneItem.setCyto(readString(file, STRING_GENE_CONST));
        geneItem.setGeneId(readString(file, STRING_GENE_CONST));

        int auxInt = littleEndianToBigEndian(file.readInt());
        byte byteChar = (byte)auxInt;
        if (byteChar == DIRECTION_FORWARD)
            geneItem.setIsComplement(false);
        else if (byteChar == DIRECTION_COMPLEMENT)
            geneItem.setIsComplement(true);

        return geneItem;
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Citesc Info Query
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Citesc un info query din fisier
     * @param file
     * @return
     * @throws IOException
     */
    private static InfoQuery readInfoQuery(RandomAccessFile file) throws IOException {
        InfoQuery infoQuery = new InfoQuery();
        int auxInt;

        //FORMAT FISIER
        //char[CONST] 		queryName;
        //int 			querySize;
        //char[querySize] 	query;
        //int			date;
        //int			finalResultsNumber;
        //int			partialResultsNumber;

        infoQuery.queryName = readString(file, STRING_CONST);
        auxInt = littleEndianToBigEndian(file.readInt());
        infoQuery.query = readString(file, auxInt);
        long secondsFromEpoch = ((long)littleEndianToBigEndian(file.readInt()))*1000;
        infoQuery.searchDate = new Date(secondsFromEpoch);
        infoQuery.timpExecutie = littleEndianToBigEndian(file.readInt());
        infoQuery.finalResultNumber = littleEndianToBigEndian(file.readInt());
        infoQuery.partialResultNumber = littleEndianToBigEndian(file.readInt());

        return infoQuery;
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *     Metode de conversie
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /**
     * Converteste un integer little endian in big endian
     * @param i
     * @return
     */
    private static int littleEndianToBigEndian(int i) {
        return((i&0xff)<<24)+((i&0xff00)<<8)+((i&0xff0000)>>8)+((i>>24)&0xff);
    }

    /**
     * Converteste un integer big endian in little endian
     * @param i
     * @return
     */
    private static int bigEndianToLittleEndian(int i) {
        return littleEndianToBigEndian(i);
    }

    /**
     * Citeste un string de lungime constanta
     * @param file
     * @return
     * @throws IOException
     */
    private static String readString(RandomAccessFile file, int size) throws IOException {
        char[] chars = new char[size];
        int length = size;
        boolean stopRead = false;
        char c;

        for (int i = 0; i < size; i++) {
            //Consum streamul
            c = (char) file.readByte();

            //Daca gasesc caracterul terminal nu mai umplu vectorul
            if (c == 0 && stopRead == false) {
                stopRead = true;
                length = i;
            }

            //Scriu doar daca caracterul e valid
            if (stopRead == false) chars[i] = c;
        }

        return new String(chars, 0, length);
    }

    /**
     * Obtin un boolean din fisier
     * @param file
     * @return
     * @throws IOException
     */
    private static boolean readBoolean(RandomAccessFile file) throws IOException {
        byte auxByte;
        auxByte = file.readByte();
        if (auxByte > 0) return true;
        else return false;
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    TESTING
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * The main class
     * @param args
     */
    public static void main(String[] args) {
        File file = new File("../fisier_rezultate_formatat.tress");
        MainResult mainResult = readResult(file);

        if (mainResult == null)
            System.out.println("erorr");
    }
}