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

package ro.genomeartist.gui.controller.exporters;

import ro.genomeartist.components.dialogs.DialogFactory;
import ro.genomeartist.gui.controller.finalresult.FinalResultItem;
import ro.genomeartist.gui.controller.finalresult.IntervalMappingItem;
import ro.genomeartist.gui.controller.finalresult.IntervalMappingSet;
import ro.genomeartist.gui.controller.genes.GeneItem;
import ro.genomeartist.gui.controller.genes.GeneItemWrapper;
import ro.genomeartist.gui.controller.genes.GeneVector;
import ro.genomeartist.gui.controller.query.MainResult;
import ro.genomeartist.gui.custompaint.DrawingConstants;
import ro.genomeartist.gui.interfaces.IGlobalManager;
import ro.genomeartist.gui.mainpanels.genes.JGeneVectorPane;
import ro.genomeartist.gui.utils.MyUtils;
import ro.genomeartist.gui.utils.ReadOnlyConfiguration;
import ro.genomeartist.gui.reports.datasources.JRDataSourceFinalResult;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ScrollPaneConstants;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Clasa ce face afisarea si exportarea formularelor
 * @author iulian
 */
public class FinalResultExporter {

    /**
     * Exporta un rezultat intr-un fisier png
     * @param sourceResult
     * @param destination
     */
    public static void exportResultAsImage(FinalResultItem sourceResult, File destination ) {
        /**
         * Structura imaginii:
         *      - portiunea de sus: alinierea intervalelor pe query
         *      - iterarea fiecarui interval si afisarea maparii genelor
         *      - in partea finala se pun datele de copyright
         */

        //Variabile pentru dimensiuni
        int resultHeight = sourceResult.getImageHeight(new JLabel());
        int mappingSetHeight = 0;
        int genesHeight = 0;

        //Calculez dimensiuniile componentelor
        LinkedHashSet<GeneItem> geneItemSanitizer = new LinkedHashSet<GeneItem>();
        IntervalMappingSet intervalMappingSet = sourceResult.getIntervalMappingSet();
        Iterator <IntervalMappingItem> iteratorMapping = intervalMappingSet.iterator();
        while (iteratorMapping.hasNext()) {
            IntervalMappingItem mappingItem = iteratorMapping.next();
            GeneItemWrapper geneItemWrapper = mappingItem.getGeneItemWrapper();
            mappingSetHeight += geneItemWrapper.getImageHeight(new JLabel());

            //Pastrez genele pentru desenare ulterioara
            geneItemSanitizer.addAll(geneItemWrapper.getFullGeneVector());
        }
        
        //Tabela de gene
        GeneVector geneVector = new GeneVector();
        geneVector.addAll(geneItemSanitizer);
        JGeneVectorPane geneVectorPane = new JGeneVectorPane(geneVector);
        genesHeight = geneVectorPane.getSuggestedHeight();

        //calculez dimensiunea planselor de desen
        int canvasWidth = DrawingConstants.IMAGE_WIDTH;
        int canvasHeight = resultHeight + mappingSetHeight + genesHeight;

        //Obtin canvasul pe care se va desena
        BufferedImage image = new BufferedImage(canvasWidth, canvasHeight,
             BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) image.getGraphics();

        //~~~~~~~~~Desenez resultatul final~~~~~~~~~~~~~~
        sourceResult.paintImage(g2d, canvasWidth, resultHeight, Color.WHITE, Color.BLACK);
        g2d.translate(0, resultHeight);

        //~~~~~~~~~Desenez pozitionarea genelor pe fiecare interval~~~~~~~~
        iteratorMapping = intervalMappingSet.iterator();
        while (iteratorMapping.hasNext()) {
            IntervalMappingItem mappingItem = iteratorMapping.next();
            GeneItemWrapper geneItemWrapper = mappingItem.getGeneItemWrapper();
            int itemHeight = geneItemWrapper.getImageHeight(new JLabel());

            //Desenez obiectul
            geneItemWrapper.paintImage(g2d, canvasWidth, itemHeight, Color.WHITE, Color.BLACK);
            g2d.translate(0, itemHeight);
        }

        //~~~~~~~~~Desenez un tabel cu genele implicate in aliniere~~~~~~~~
        //Calculez dimensiunea tabelului
        geneVectorPane.setPreferredSize(new Dimension(canvasWidth, genesHeight));
        geneVectorPane.setSize(geneVectorPane.getPreferredSize());

        //Hack: adaug panoul intr-un jframe pentru a-l forta sa faca layout
        JFrame frame = new JFrame();
            geneVectorPane.getScrollPane().setVerticalScrollBarPolicy(
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        frame.getContentPane().add(geneVectorPane);
        frame.pack();
        frame.setSize(canvasWidth, genesHeight);
            geneVectorPane.printAll(g2d);
        frame.getContentPane().removeAll();
        frame.dispose();


        //Scriu imaginea in fisier
        try {
            if (image != null ) ImageIO.write(image, MyUtils.IMAGE_EXT, destination);
        } catch (IOException ex) {
            System.err.println("Could not save");
        }
    }

    /**
     * Printez formularul cu Jasper Report
     */
    public static void exportResultAsPdf(IGlobalManager globalManager, File destination
            ,MainResult mainResult, FinalResultItem sourceResult) {
        JasperPrint jasperPrint = fillResultReport(mainResult, sourceResult);
        try {
            JasperExportManager.exportReportToPdfFile(jasperPrint, destination.getPath());
        } catch (JRException ex) {
            //Show error
            DialogFactory.showErrorDialog(globalManager.getTheRootFrame(), "Error",
                    "Could not export to pdf.");
            System.err.println("Could not export to pdf");
        }
    }
    
    /**
     * Pun datele dorite intr-un tabel de String-uri
     */
    public static ArrayList<String[]> getBestResultsInsertionData(MainResult sourceResult, int lengthSeqExtract, int lengthTolerance) {
        String queryName = sourceResult.infoQuery.queryName;       
        int score = sourceResult.bestResult.getScore();
        ArrayList<String[]> batchResult = new ArrayList<String[]>();
        IntervalMappingSet filteredSet = filterIntervalMappingSet(sourceResult.bestResult.getIntervalMappingSet(), lengthTolerance).intervalSet;
        
        String[] individualResult = getIndividualInsertionData(filteredSet, queryName, score, sourceResult.bestResult, lengthSeqExtract);
        if(individualResult != null)
            batchResult.add(individualResult);
        for(int i = 0; i < sourceResult.finalResultSet.size(); i++) {
            if(!sourceResult.finalResultSet.elementAt(i).equals(sourceResult.bestResult) && sourceResult.finalResultSet.elementAt(i).getScore() == score) {
                filteredSet = filterIntervalMappingSet(sourceResult.finalResultSet.elementAt(i).getIntervalMappingSet(), lengthTolerance).intervalSet;
                individualResult = getIndividualInsertionData(filteredSet, queryName, score, sourceResult.bestResult, lengthSeqExtract);
                if(individualResult != null)
                    batchResult.add(individualResult);
            }
        }
        return batchResult;
    }
    
    /**
     * Filtrez insulele artefact
     */
    public static FilteredSetWrapper filterIntervalMappingSet(IntervalMappingSet inputSet, int lengthTolerance) {
        ArrayList<IntervalMappingSet> intervalClusters = new ArrayList<IntervalMappingSet>();
        int i = 0;
        int maxValue;
        int maxIndex = 0;
        ArrayList<Integer> maxCoordinates = new ArrayList<Integer>();
        // Pune fiecare insula de intervale separate prin max lengthTolerance intr-un element separat din ArrayList-ul intervalClusters
        while(i < inputSet.size()) {
            IntervalMappingSet auxIntervalSet = new IntervalMappingSet();
            auxIntervalSet.add(inputSet.elementAt(i));
            i++;
            // Cazul unei insule izolate pe ultima pozitie
            if(i == inputSet.size()) {
                intervalClusters.add(auxIntervalSet);
                break;
            }
            // Daca pozitiile in query a doua intervale sunt separate de max lengthTolerance, sunt inserate in acelasi Cluster
            while(inputSet.elementAt(i).getPozitieStartQuery() - inputSet.elementAt(i-1).getPozitieStopQuery() -1 <= lengthTolerance) {
                auxIntervalSet.add(inputSet.elementAt(i));
                i++;
                if(i == inputSet.size())
                    break;
            }
            intervalClusters.add(auxIntervalSet);
        }
        // Selectez insula (clusterul) cu valoarea de granita cea mai mare sau egala cu 1
        for(int j = 0; j < intervalClusters.size(); j++) {
            maxValue = 0;
            for(int k = 0; k < intervalClusters.get(j).size(); k++) {               
                if(k != 0) {
                    if(intervalClusters.get(j).elementAt(k).getPozitieStartGenom() == 1)
                        return new FilteredSetWrapper(intervalClusters.get(j), j);
                    if(intervalClusters.get(j).elementAt(k).getPozitieStartGenom() > maxValue)
                        maxValue = intervalClusters.get(j).elementAt(k).getPozitieStartGenom(); 
                }
                if(k != intervalClusters.get(j).size()-1) {
                    if(intervalClusters.get(j).elementAt(k).getPozitieStopGenom() == 1)
                        return new FilteredSetWrapper(intervalClusters.get(j), j);
                    if(intervalClusters.get(j).elementAt(k).getPozitieStopGenom() > maxValue)
                        maxValue = intervalClusters.get(j).elementAt(k).getPozitieStopGenom();                    
                }
            }
            maxCoordinates.add(maxValue);
        }
        maxValue = 0;
        for(int j = 0; j < maxCoordinates.size(); j++) {
            if(maxCoordinates.get(j) > maxValue) {
                maxValue = maxCoordinates.get(j);
                maxIndex = j;
            }               
        }
        return new FilteredSetWrapper(intervalClusters.get(maxIndex), maxIndex);
    }
    
    /**
     * Identific locusul de insertie
     */
    public static int[] getInsertionPosition(IntervalMappingSet intervalMappingSet) {
        String conformation = new String();
        int[] codifiedInsertionPosition = new int[4];
        if(intervalMappingSet.size() == 2) {
            // Cazul transpozon-genom sau genom-stranspozon
            codifiedInsertionPosition[3] = 0;
            if(intervalMappingSet.elementAt(0).isTransposon() && !intervalMappingSet.elementAt(1).isTransposon())
                conformation = "Left Transposon";
            else if(!intervalMappingSet.elementAt(0).isTransposon() && intervalMappingSet.elementAt(1).isTransposon())
                conformation = "Right Transposon";
            // Cazul transpozon-transpozon
            else if(intervalMappingSet.elementAt(0).isTransposon() && intervalMappingSet.elementAt(1).isTransposon()) {
                if(intervalMappingSet.elementAt(0).getPozitieStopGenom() == 1)
                    conformation = "Left Transposon";
                else if(intervalMappingSet.elementAt(1).getPozitieStartGenom() == 1)
                    conformation = "Right Transposon";
                else if(intervalMappingSet.elementAt(0).getPozitieStopGenom() > intervalMappingSet.elementAt(1).getPozitieStartGenom())
                    conformation = "Left Transposon";
                else
                    conformation = "Right Transposon";
            }
            if(conformation.equals("Left Transposon")) {
                codifiedInsertionPosition[0] = 0;//TransposonItem = intervalMappingSet.elementAt(0);
                codifiedInsertionPosition[1] = 1;//GenomeItem = intervalMappingSet.elementAt(1);
                codifiedInsertionPosition[2] = 1;//borderPosition = 1;
            }
            else if(conformation.equals("Right Transposon")) {
                codifiedInsertionPosition[0] = 1;//TransposonItem = intervalMappingSet.elementAt(1);
                codifiedInsertionPosition[1] = 0;//GenomeItem = intervalMappingSet.elementAt(0);
                codifiedInsertionPosition[2] = 0;//borderPosition = 0;
            }                      
        }              
        else if(intervalMappingSet.size() == 3) {
            codifiedInsertionPosition[3] = 1;
            // cazul transpozon-genom-transpozon
            if(intervalMappingSet.elementAt(0).isTransposon() && !intervalMappingSet.elementAt(1).isTransposon() && intervalMappingSet.elementAt(2).isTransposon()) {
                codifiedInsertionPosition[1] = 1;//GenomeItem = intervalMappingSet.elementAt(1);                
                if(intervalMappingSet.elementAt(0).getPozitieStopGenom() == 1)
                    conformation = "Left Transposon";
                else if(intervalMappingSet.elementAt(2).getPozitieStartGenom() == 1)
                    conformation = "Right Transposon";
                else if(intervalMappingSet.elementAt(0).getPozitieStopGenom() > intervalMappingSet.elementAt(2).getPozitieStartGenom())
                    conformation = "Left Transposon";
                else
                    conformation = "Right Transposon";                
            }
            if(intervalMappingSet.elementAt(0).isTransposon() && intervalMappingSet.elementAt(1).isTransposon() && !intervalMappingSet.elementAt(2).isTransposon()) {
                if(intervalMappingSet.elementAt(1).getPozitieStartGenom() == 1)
                    conformation = "Center-left Transposon";
                else if(intervalMappingSet.elementAt(1).getPozitieStopGenom() == 1)
                    conformation = "Center-right Transposon";
                else if(intervalMappingSet.elementAt(1).getPozitieStartGenom() > intervalMappingSet.elementAt(1).getPozitieStopGenom()) {
                    if(intervalMappingSet.elementAt(0).getPozitieStopGenom() > intervalMappingSet.elementAt(1).getPozitieStartGenom()
                    || intervalMappingSet.elementAt(0).getPozitieStopGenom() == 1)
                        conformation = "Left Transposon";
                    else
                        conformation = "Center-left Transposon";
                }
                else
                    conformation = "Center-right Transposon";
            }
            if(!intervalMappingSet.elementAt(0).isTransposon() && intervalMappingSet.elementAt(1).isTransposon() && intervalMappingSet.elementAt(2).isTransposon()) {
                if(intervalMappingSet.elementAt(1).getPozitieStartGenom() == 1)
                    conformation = "Center-left Transposon";
                else if(intervalMappingSet.elementAt(1).getPozitieStopGenom() == 1)
                    conformation = "Center-right Transposon";
                else if(intervalMappingSet.elementAt(1).getPozitieStopGenom() > intervalMappingSet.elementAt(1).getPozitieStartGenom()) {
                    if(intervalMappingSet.elementAt(2).getPozitieStartGenom() > intervalMappingSet.elementAt(1).getPozitieStopGenom()
                    || intervalMappingSet.elementAt(2).getPozitieStartGenom() == 1)
                        conformation = "Right Transposon";
                    else
                        conformation = "Center-right Transposon";
                }
                else
                    conformation = "Center-left Transposon";
            }
            if(conformation.equals("Left Transposon")) {
                codifiedInsertionPosition[0] = 0;//TransposonItem = intervalMappingSet.elementAt(0);
                codifiedInsertionPosition[1] = 1;//GenomeItem = intervalMappingSet.elementAt(1);
                codifiedInsertionPosition[2] = 1;//borderPosition = 1;
            }
            else if(conformation.equals("Right Transposon")) {
                codifiedInsertionPosition[0] = 2;//TransposonItem = intervalMappingSet.elementAt(2);
                codifiedInsertionPosition[1] = 1;//GenomeItem = intervalMappingSet.elementAt(1);
                codifiedInsertionPosition[2] = 0;//borderPosition = 0;
            }
            else if(conformation.equals("Center-right Transposon")) {
                codifiedInsertionPosition[0] = 1;//TransposonItem = intervalMappingSet.elementAt(1);
                codifiedInsertionPosition[1] = 2;//GenomeItem = intervalMappingSet.elementAt(2);
                codifiedInsertionPosition[2] = 1;//borderPosition = 1;
            }
            else if(conformation.equals("Center-left Transposon")) {
                codifiedInsertionPosition[0] = 1;//TransposonItem = intervalMappingSet.elementAt(1);
                codifiedInsertionPosition[1] = 0;//GenomeItem = intervalMappingSet.elementAt(0);
                codifiedInsertionPosition[2] = 0;//borderPosition = 1;
            }
        }
        else if(intervalMappingSet.size() == 1 && !intervalMappingSet.elementAt(0).isTransposon()) {
            codifiedInsertionPosition[0] = -2;
            codifiedInsertionPosition[1] = -1;
            codifiedInsertionPosition[2] = -1;
            codifiedInsertionPosition[3] = -1;
        }
        // Cazul artefact
        else {
            codifiedInsertionPosition[0] = codifiedInsertionPosition[1] = codifiedInsertionPosition[2] = codifiedInsertionPosition[3] = -1;
        }
        return codifiedInsertionPosition;
    }    
    
    /**
     * Creez si returnez un rand de tabel
     */
    public static String[] getIndividualInsertionData(IntervalMappingSet intervalMappingSet, String queryName, int score, FinalResultItem finalResultItem, int lengthTDS) { 
        IntervalMappingItem TransposonItem;
        IntervalMappingItem GenomeItem;
        int borderPosition = 0;
        String tsdRow = "";
        String[] returnTable = new String[MyUtils.COLUMNS_NUMBER];
        
        returnTable[0] = queryName;
        returnTable[8] = Integer.toString(score);
        returnTable[11] = "";
                
        int[] codifiedInsertionPosition = getInsertionPosition(intervalMappingSet);
        if(codifiedInsertionPosition[0] == -2) {
            TransposonItem = null;
            GenomeItem = intervalMappingSet.elementAt(0);
            borderPosition = -2;
            returnTable[2] = "";
        }
        else if(codifiedInsertionPosition[0] != -1) {
            TransposonItem = intervalMappingSet.elementAt(codifiedInsertionPosition[0]);
            GenomeItem = intervalMappingSet.elementAt(codifiedInsertionPosition[1]);
            borderPosition = codifiedInsertionPosition[2];
            returnTable[2] = TransposonItem.getFisierOrigine();
            tsdRow = getTSD(GenomeItem, intervalMappingSet, finalResultItem, codifiedInsertionPosition, lengthTDS, false);
            if(doReverseComplement(TransposonItem.isComplement(), GenomeItem.isComplement()))
                tsdRow = reverseComplement(tsdRow);
            if(isTypeIIorientation(TransposonItem.isComplement(), GenomeItem.isComplement()))
                tsdRow = tsdRow + "(-)";
            else
                tsdRow = tsdRow + "(+)";
        }
        else {              // Cazul artefact
            return null;
        }
        
        returnTable[1] = GenomeItem.getFisierOrigine();
        
        if(borderPosition == 1) {
            returnTable[3] = Integer.toString(GenomeItem.getPozitieStartGenom());
            returnTable[4] = Integer.toString(TransposonItem.getPozitieStopGenom());
            returnTable[9] = tsdRow;
            returnTable[10] = Integer.toString(GenomeItem.getPozitieStopGenom());
            if(GenomeItem.getPozitieStartQuery() - TransposonItem.getPozitieStopQuery() > 1)
                returnTable[11] = "*";
        }
        else if(borderPosition == 0) {
            returnTable[3] = Integer.toString(GenomeItem.getPozitieStopGenom());
            returnTable[4] = Integer.toString(TransposonItem.getPozitieStartGenom());
            returnTable[9] = tsdRow;
            returnTable[10] = Integer.toString(GenomeItem.getPozitieStartGenom());
            if(TransposonItem.getPozitieStartQuery() - GenomeItem.getPozitieStopQuery() > 1)
                returnTable[11] = "*";
        }
        else if(borderPosition == -2) {
            returnTable[3] = Integer.toString(GenomeItem.getPozitieStartGenom());
            returnTable[4] = "";
            returnTable[9] = "";
            returnTable[10] = Integer.toString(GenomeItem.getPozitieStopGenom());
        }
        returnTable[5] = "";
        GeneVector auxGeneVector = GenomeItem.getInsideGenes();
        Iterator <GeneItem> iteratorGenes = auxGeneVector.iterator();
        while(iteratorGenes.hasNext()) {
            GeneItem auxInsideGene = iteratorGenes.next();
            returnTable[5] += auxInsideGene.getName();
            if(iteratorGenes.hasNext())
                returnTable[5] += "; ";
        }
        GeneItem auxUpstreamGene = GenomeItem.getClosestUpstream();
        if(auxUpstreamGene != null)
            returnTable[6] = auxUpstreamGene.getName();
        else
            returnTable[6] = "";
        GeneItem auxDownstreamGene = GenomeItem.getClosestDownstream();
        if(auxDownstreamGene != null)
            returnTable[7] = auxDownstreamGene.getName();
        else
            returnTable[7] = "";
                      
        return returnTable;
    }

    /**
     * Pun datele dorite intr-un fisier fasta
     */
    public static String getBestResultsTSD(MainResult sourceResult, int lengthTDS, int lengthTolerance) {
        IntervalMappingSet filteredSet = filterIntervalMappingSet(sourceResult.bestResult.getIntervalMappingSet(), lengthTolerance).intervalSet;
        int[] codifiedInsertionPosition = getInsertionPosition(filteredSet);
        if(codifiedInsertionPosition[1] == -1)
            return null;
        IntervalMappingItem GenomeItem = filteredSet.elementAt(codifiedInsertionPosition[1]);
        IntervalMappingItem TransposonItem = filteredSet.elementAt(codifiedInsertionPosition[0]);
        String individualResult = getTSD(GenomeItem, sourceResult.bestResult.getIntervalMappingSet(), sourceResult.bestResult, codifiedInsertionPosition, lengthTDS, true);        
        if(individualResult != null) {
            if(doReverseComplement(TransposonItem.isComplement(), GenomeItem.isComplement()))
                individualResult = reverseComplement(individualResult);
            if(isTypeIIorientation(TransposonItem.isComplement(), GenomeItem.isComplement()))
                return "(-)\n" + individualResult;
            else
                return "(+)\n" + individualResult;
        }
        else
            return null;
    }
    
    /**
     * Returnez TDS
     */
    public static String getTSD(IntervalMappingItem GenomeItem, IntervalMappingSet intervalMappingSet, FinalResultItem finalResultItem, int[] codifiedInsertionPosition, int lengthTDS, boolean useInFasta) {
        int gapCount, borderIndex, extractedSequenceIndex, i;
        String output, auxElement;        
        if(codifiedInsertionPosition[2] == 0) {
            borderIndex = GenomeItem.getOutStringOffset() + GenomeItem.getOutStringLength();
            extractedSequenceIndex = borderIndex - lengthTDS;
            if(extractedSequenceIndex < 0)
                extractedSequenceIndex = 0;
            output = finalResultItem.getOut1().substring(extractedSequenceIndex, borderIndex);
            gapCount = output.length() - output.replace("-","").length();
            i = 0;
            while(gapCount > 0 && extractedSequenceIndex > 0) {
		auxElement = finalResultItem.getOut1().substring(extractedSequenceIndex-i-1, extractedSequenceIndex-i);
		if(!auxElement.equals("-"))
			gapCount--;	
                output = auxElement + output;            
                i++;
            }
        }
        else if(codifiedInsertionPosition[2] == 1) {
            borderIndex = GenomeItem.getOutStringOffset();
            extractedSequenceIndex = borderIndex + lengthTDS;
            if(extractedSequenceIndex > finalResultItem.getOut1().length())
                extractedSequenceIndex = finalResultItem.getOut1().length();
            output = finalResultItem.getOut1().substring(borderIndex, extractedSequenceIndex);
            gapCount = output.length() - output.replace("-","").length();
            i = 0;
            while(gapCount > 0 && extractedSequenceIndex < finalResultItem.getOut1().length()) {
		auxElement = finalResultItem.getOut1().substring(extractedSequenceIndex+i, extractedSequenceIndex+i+1);
		if(!auxElement.equals("-"))
			gapCount--;
                output = output + auxElement;               
                i++;
            }
        }
        else
            return null;       
        if(useInFasta) {
            output = output.replace("-","");
        }      
        return output;
    }
    
    /**
     * Pun datele flancatoare intr-un fisier fasta
     */
    public static String getBestResultsFlankingSeq(MainResult sourceResult, boolean useDoubleFlanks, int lengthTSD, int lengthTolerance, String FolderPath) throws IOException {
        IntervalMappingSet filteredSet = filterIntervalMappingSet(sourceResult.bestResult.getIntervalMappingSet(), lengthTolerance).intervalSet;
        String readResult = null;
        int genomePosition = 0, borderPosition;
        int[] codifiedInsertionPosition = getInsertionPosition(filteredSet);
        if(codifiedInsertionPosition[1] == -1)
            return null;
        IntervalMappingItem GenomeItem = filteredSet.elementAt(codifiedInsertionPosition[1]);
        IntervalMappingItem TransposonItem = filteredSet.elementAt(codifiedInsertionPosition[0]);
        borderPosition = codifiedInsertionPosition[2];
            
        if(codifiedInsertionPosition[0] != -1 && codifiedInsertionPosition[0] != -2) {
            if(borderPosition == 1)
                genomePosition = GenomeItem.getPozitieStartGenom();
            else if(borderPosition == 0)
                genomePosition = GenomeItem.getPozitieStopGenom();
        }             
        String genomeFilePath = FolderPath + GenomeItem.getFisierOrigine() + ".raw";
        if((borderPosition == 1 && TransposonItem.isComplement() && !GenomeItem.isComplement())
        || (borderPosition == 0 && !TransposonItem.isComplement() && GenomeItem.isComplement())
        || (borderPosition == 1 && !TransposonItem.isComplement() && !GenomeItem.isComplement())
        || (borderPosition == 0 && TransposonItem.isComplement() && GenomeItem.isComplement()) )
            genomePosition--;
        if(useDoubleFlanks) {
            if((borderPosition == 0 && !GenomeItem.isComplement()) || (borderPosition == 1 && GenomeItem.isComplement()))
                readResult = readFromRawFile(genomeFilePath, genomePosition, 2*lengthTSD, lengthTSD);
            if((borderPosition == 1 && !GenomeItem.isComplement()) || (borderPosition == 0 && GenomeItem.isComplement()))
                readResult = readFromRawFile(genomeFilePath, genomePosition, lengthTSD, 2*lengthTSD);
        }
        else
            readResult = readFromRawFile(genomeFilePath, genomePosition, lengthTSD, lengthTSD);
        if(readResult != null) {
            if(isTypeIIorientation(TransposonItem.isComplement(), GenomeItem.isComplement()))
                return "(-)\n" + reverseComplement(readResult);
            else
                return "(+)\n" + readResult;
        }
        else
            return null;
    }
    
    /*
    * functie pentru citire din fisier .raw
    */
    public static String readFromRawFile(String rawFilePath, int coordinate, int leftLengthExtract, int rightLengthExtract) throws IOException {
        File genomeFile = new File(rawFilePath);
        String outResult = null, textBlock = "";
        int i = 4197;
        try {
            FileReader previousBlock = new FileReader(genomeFile);
            FileReader centerBlock = new FileReader(genomeFile);
            FileReader nextBlock = new FileReader(genomeFile);
            char[] previousChars = new char[4196];
            char[] centerChars = new char[4196];
            char[] nextChars = new char[4196];
            if(nextBlock.read(nextChars) > 0)
                textBlock += new String(nextChars);
            if(nextBlock.read(nextChars) > 0) {
                textBlock += new String(nextChars);
                centerBlock.read(centerChars);
                if(coordinate <= 4196)
                    outResult = textBlock.substring(max(coordinate - leftLengthExtract, 0), coordinate + rightLengthExtract);
            }
            else if(coordinate <= 4196)
                outResult = textBlock.substring(max(coordinate - leftLengthExtract, 0), min(coordinate + rightLengthExtract, textBlock.length()-1));            
            for(int len; (len = nextBlock.read(nextChars)) > 0;) {
                centerBlock.read(centerChars);
                previousBlock.read(previousChars);
                if(i > coordinate - 4196 && i <= coordinate) {
                    textBlock = new String(previousChars) + new String(centerChars) + new String(nextChars);
                    outResult = textBlock.substring(4196 + coordinate%4196 
                            - leftLengthExtract, 4196 + coordinate%4196 + rightLengthExtract);
                    previousBlock.close();
                    centerBlock.close();
                    nextBlock.close();
                    return outResult;
                }
                i += 4196;
            }
            if(i > coordinate - 4196 && i <= coordinate) {
                textBlock = new String(centerChars) + new String(nextChars);
                outResult = textBlock.substring(4196 + coordinate%4196 
                            - leftLengthExtract, min(4196 + coordinate%4196 + rightLengthExtract, textBlock.length()-1));
            }
            previousBlock.close();
            centerBlock.close();
            nextBlock.close();
        } catch (IOException e) {
            throw new IOException("Requires filepath: " + rawFilePath);
        }
        return outResult;
    }
    
    
    
    /*
    * antiparalel
    */
    public static String reverseComplement(String input) {
        String output = "";
	int i;
        for(i = 0; i <= input.length()-1; i++) {
            if(input.substring(i, i+1).equals("A"))
                output += "T";
            if(input.substring(i, i+1).equals("C"))
                output += "G";
            if(input.substring(i, i+1).equals("G"))
                output += "C";
            if(input.substring(i, i+1).equals("T"))
                output += "A";
        }
        StringBuilder outputBuilder = new StringBuilder(output);
        output = outputBuilder.reverse().toString();
        return output;
    }
    
    /**
     * verifica daca este nevoie de antipralel
     */
    public static boolean doReverseComplement(boolean transposonSense, boolean genomeSense) {
        if((transposonSense && genomeSense) || (transposonSense && !genomeSense))
            return true;
        else           
            return false;
    }
    
    /**
     * verifica daca este nevoie de antipralel
     */
    public static boolean isTypeIIorientation(boolean transposonSense, boolean genomeSense) {
        if((transposonSense && !genomeSense) || (!transposonSense && genomeSense))
            return true;
        else           
            return false;
    }
    
    //functii min si max
    public static int min(int a, int b) {
        if(a >= b)
            return b;
        else 
            return a;
    }
    
    public static int max(int a, int b) {
        if(a <= b)
            return b;
        else 
            return a;
    }
    
    public static class FilteredSetWrapper{
        public int interalIndex;
        public IntervalMappingSet intervalSet;
        public FilteredSetWrapper(IntervalMappingSet set, int index) {
            interalIndex = index;
            intervalSet = set;
        }
    }
    
    /**
     * Printez formularul cu Jasper Report
     */
    public static void printReport(MainResult mainResult, FinalResultItem sourceResult) {
        JasperPrint jasperPrint = fillResultReport(mainResult, sourceResult);
        JasperViewer.viewReport(jasperPrint, false);
    }

    /**
     * generez si populez formularul cu date
     * @param mainResult
     * @param sourceResult
     * @return
     */
    private static JasperPrint fillResultReport(MainResult mainResult, FinalResultItem sourceResult) {
        // <editor-fold defaultstate="collapsed" desc="Generez obiectul jasper">
        String compiledFolder, compiledPath;
        JRDataSource dataSource;

        compiledFolder = ReadOnlyConfiguration.getString("JASPER_COMPILED_FOLDER");
        compiledPath = compiledFolder + ReadOnlyConfiguration.getString("JASPER_COMPILED_RESULT_REPORT");

        //Parametrii
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("titlu", "Search Result");
        params.put("header_left", "Search Result");
        params.put("header_right", DrawingConstants.APP_NAME);
        params.put("footer", mainResult.infoQuery.queryName);

        params.put("queryName", mainResult.infoQuery.queryName);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        params.put("searchDate", dateFormat.format(mainResult.infoQuery.searchDate));
        params.put("version", DrawingConstants.APP_VERSION);

        //obtin datasource-ul
        dataSource = new JRDataSourceFinalResult(mainResult, sourceResult);

        //Populez formularul
        try {
            JasperReport jasperReport =(JasperReport) JRLoader.
                        loadObjectFromLocation(compiledPath);

            JasperPrint jasperPrint =
                    JasperFillManager.fillReport(
                    jasperReport, params, dataSource);

            return jasperPrint;
        } catch (JRException ex) {
            ex.printStackTrace();
            return null;
        }
        // </editor-fold>
    }
}

