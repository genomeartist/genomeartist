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
import java.io.IOException;
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
    public static ArrayList<String[]> getBestResultsInsertionData(MainResult sourceResult) {
        String queryName = sourceResult.infoQuery.queryName;       
        int score = sourceResult.bestResult.getScore();
        ArrayList<String[]> batchResult = new ArrayList<String[]>();
        IntervalMappingSet filteredSet = filterIntervalMappingSet(sourceResult.bestResult.getIntervalMappingSet());
        
        String[] individualResult = FinalResultExporter.getIndividualInsertionData(filteredSet, queryName, score);
        batchResult.add(individualResult);
        for(int i = 0; i < sourceResult.finalResultSet.size(); i++) {
            if(!sourceResult.finalResultSet.elementAt(i).equals(sourceResult.bestResult) && sourceResult.finalResultSet.elementAt(i).getScore() == score) {
                filteredSet = filterIntervalMappingSet(sourceResult.finalResultSet.elementAt(i).getIntervalMappingSet());
                batchResult.add(FinalResultExporter.getIndividualInsertionData(filteredSet, queryName, score));
            }
        }
        return batchResult;
    }
    
    /**
     * Filtrez insulele artefact
     */
    public static IntervalMappingSet filterIntervalMappingSet(IntervalMappingSet inputSet) {
        ArrayList<IntervalMappingSet> intervalClusters = new ArrayList<IntervalMappingSet>();
        int i = 0;
        int maxValue;
        int maxIndex = 0;
        ArrayList<Integer> maxCoordinates = new ArrayList<Integer>();
        // Pune fiecare insula de intervale continue intr-un element separat din ArrayList-ul intervalClusters
        while(i < inputSet.size()) {
            IntervalMappingSet auxIntervalSet = new IntervalMappingSet();
            auxIntervalSet.add(inputSet.elementAt(i));
            i++;
            // Cazul unei insule izolate pe ultima pozitie
            if(i == inputSet.size()) {
                intervalClusters.add(auxIntervalSet);
                break;
            }
            // Daca pozitiile in query a doua intervale sunt cosnecutive, sunt inserate in acelasi Cluster
            while(inputSet.elementAt(i-1).getPozitieStopQuery() + 1 == inputSet.elementAt(i).getPozitieStartQuery()) {
                auxIntervalSet.add(inputSet.elementAt(i));
                i++;
                if(i == inputSet.size()) {
                    intervalClusters.add(auxIntervalSet);
                    break;
                }
            }
            intervalClusters.add(auxIntervalSet);
        }
        // Selectez insula (clusterul) de lungimea cea mai mare
        for(int j = 0; j < intervalClusters.size(); j++) {
            maxValue = 0;
            for(int k = 0; k < intervalClusters.get(j).size(); k++) {               
                if(k != 0) {
                    if(intervalClusters.get(j).elementAt(k).getPozitieStartGenom() == 1)
                        return intervalClusters.get(j);
                    if(intervalClusters.get(j).elementAt(k).getPozitieStartGenom() > maxValue)
                        maxValue = intervalClusters.get(j).elementAt(k).getPozitieStartGenom(); 
                }
                if(k != intervalClusters.get(j).size()-1) {
                    if(intervalClusters.get(j).elementAt(k).getPozitieStopGenom() == 1)
                        return intervalClusters.get(j);
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
        return intervalClusters.get(maxIndex);
    }
    
    /**
     * Identific locusul de insertie
     */
    public static String[] getIndividualInsertionData(IntervalMappingSet intervalMappingSet, String queryName, int score) { 
        IntervalMappingItem TransposonItem = null;
        IntervalMappingItem GenomeItem = null;
        String conformation = new String();
        int borderPosition = 0;
        String[] returnTable = new String[MyUtils.COLUMNS_NUMBER];
        returnTable[0] = queryName;
        returnTable[8] = Integer.toString(score);
                
        if(intervalMappingSet.size() == 2) {
            // Cazul transpozon-genom sau genom-stranspozon
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
                TransposonItem = intervalMappingSet.elementAt(0);
                GenomeItem = intervalMappingSet.elementAt(1);
                borderPosition = 1;
            }
            else if(conformation.equals("Right Transposon")) {
                TransposonItem = intervalMappingSet.elementAt(1);
                GenomeItem = intervalMappingSet.elementAt(0);
                borderPosition = 0;
            }                      
        }       
        // Cazul transpozon-genom-transpozon
        else if(intervalMappingSet.size() == 3 && intervalMappingSet.elementAt(0).isTransposon() && !intervalMappingSet.elementAt(1).isTransposon() && intervalMappingSet.elementAt(2).isTransposon()) {
            GenomeItem = intervalMappingSet.elementAt(1);
            if(intervalMappingSet.elementAt(0).getPozitieStopQuery() == 1)
                conformation = "Left Transposon";
            else if(intervalMappingSet.elementAt(2).getPozitieStartQuery() == 1)
                conformation = "Right Transposon";
            else if(intervalMappingSet.elementAt(0).getPozitieStopQuery() > intervalMappingSet.elementAt(2).getPozitieStartQuery())
                conformation = "Left Transposon";
            else
                conformation = "Right Transposon";
            if(conformation.equals("Left Transposon")) {
                TransposonItem = intervalMappingSet.elementAt(0);
                GenomeItem = intervalMappingSet.elementAt(1);
                borderPosition = 1;
            }
            else if(conformation.equals("Right Transposon")) {
                TransposonItem = intervalMappingSet.elementAt(2);
                GenomeItem = intervalMappingSet.elementAt(1);
                borderPosition = 0;
            }
        }
        // Cazul artefact
        else {
            IntervalMappingItem mappingItem = intervalMappingSet.elementAt(0);
            returnTable[1] = mappingItem.getFisierOrigine();
            returnTable[2] = "";
            returnTable[3] = Integer.toString(mappingItem.getPozitieStartGenom());
            returnTable[4] = "";
            returnTable[5] = "";
            returnTable[6] = "";
            returnTable[7] = "";
            return returnTable;
        }
        
        returnTable[1] = GenomeItem.getFisierOrigine();
        returnTable[2] = TransposonItem.getFisierOrigine();
        if(borderPosition == 1) {
            returnTable[3] = Integer.toString(GenomeItem.getPozitieStartGenom());
            returnTable[4] = Integer.toString(TransposonItem.getPozitieStopGenom());
        }
        else if(borderPosition == 0) {
            returnTable[3] = Integer.toString(GenomeItem.getPozitieStopGenom());
            returnTable[4] = Integer.toString(TransposonItem.getPozitieStartGenom());
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
