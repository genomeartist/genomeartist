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
     * Printez formularul cu Jasper Report
     */
    public static String[] getBestResultsAsTable(MainResult sourceResult) {
        IntervalMappingSet intervalMappingSet = sourceResult.bestResult.getIntervalMappingSet();
        Iterator <IntervalMappingItem> iteratorMapping = intervalMappingSet.iterator();
        String[] returnTable = new String[MyUtils.COLUMNS_NUMBER];
        while (iteratorMapping.hasNext()) {
            IntervalMappingItem mappingItem = iteratorMapping.next();
            if(!mappingItem.isTransposon()) {
                returnTable[0] = mappingItem.getFisierOrigine();
                returnTable[1] = sourceResult.infoQuery.queryName;
                returnTable[2] = Integer.toString(mappingItem.getPozitieStartGenom());
                returnTable[3] = Boolean.toString(mappingItem.isComplement());
                GeneItem auxUpstreamGene = mappingItem.getClosestUpstream();
                returnTable[4] = auxUpstreamGene.getName();
                GeneItem auxDownstreamGene = mappingItem.getClosestDownstream();
                returnTable[5] = auxDownstreamGene.getName();
                GeneVector auxGeneVector = mappingItem.getInsideGenes();
                Iterator <GeneItem> iteratorGenes = auxGeneVector.iterator();
                returnTable[6] = "";
                while(iteratorGenes.hasNext()) {
                    GeneItem auxInsideGene = iteratorGenes.next();
                    returnTable[6] += auxInsideGene.getName() + "; ";
                }
            }
        }
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
