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

package ro.genomeartist.gui.reports.datasources;

import java.awt.Canvas;
import ro.genomeartist.gui.controller.finalresult.FinalResultItem;
import ro.genomeartist.gui.controller.finalresult.FinalResultItemAlignment;
import ro.genomeartist.gui.controller.finalresult.IntervalMappingItem;
import ro.genomeartist.gui.controller.finalresult.IntervalMappingSet;
import ro.genomeartist.gui.controller.genes.GeneItem;
import ro.genomeartist.gui.controller.genes.GeneItemWrapper;
import ro.genomeartist.gui.controller.genes.GeneVector;
import ro.genomeartist.gui.controller.query.InfoQuery;
import ro.genomeartist.gui.controller.query.MainResult;
import ro.genomeartist.gui.custompaint.DrawingConstants;
import ro.genomeartist.gui.custompaint.ImageManipulation;
import ro.genomeartist.gui.custompaint.ImageVector;
import ro.genomeartist.gui.utils.ReadOnlyConfiguration;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author iulian
 */
public class JRDataSourceFinalResult implements JRDataSource {
        private static final int LIMIT = 5;
        private int counter;

        //Variabilele ce stau la baza
        MainResult mainResult;
        FinalResultItem sourceResult;

        //Varibile ce se vor returna
        JRDataSource subreport_query_data;
        JasperReport subreport_query_engine;
        //~~~~~~~~~~~~~
        JRDataSource subreport_mapping_data;
        JasperReport subreport_mapping_engine;
        //~~~~~~~~~~~~~
        JRDataSource subreport_smith_data;
        JasperReport subreport_smith_engine;
        //~~~~~~~~~~~~~
        JRDataSource subreport_positioning_data;
        JasperReport subreport_positioning_engine;
        //~~~~~~~~~~~~~
        JRDataSource subreport_genes_data;
        JasperReport subreport_genes_engine;

        /**
         * Construiesc un data source combinat din mai multe
         * @param mainResult
         * @param sourceResult
         */
        public JRDataSourceFinalResult(MainResult mainResult, FinalResultItem sourceResult) {
            counter = 0;

            //Variabilele de baza
            this.mainResult = mainResult;
            this.sourceResult =sourceResult;
            
            //Incarc rapoartele
            loadQueryReport();
            loadMappingReport();
            loadSmithReport();
            loadPositioningReport();
            loadGenesReport();
        }

      /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *            Incarc subrapoartele
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

        /**
         * Incarc folrmularul Smith-Waterman
         */
        private void loadQueryReport() {
            String subreportFolder, subreportPath;
            subreportFolder = ReadOnlyConfiguration
                    .getString("JASPER_COMPILED_FOLDER");
            subreportPath = subreportFolder+
                    ReadOnlyConfiguration.getString("JASPER_COMPILED_SUB_QUERY");
            try {
                subreport_query_data = getDataSourceQuery(mainResult.infoQuery);
                subreport_query_engine = (JasperReport) JRLoader.
                        loadObjectFromLocation(subreportPath);
            } catch (JRException ex) {
                Logger.getLogger(JRDataSourceFinalResult.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }

        /**
         * Incarc folrmularul Smith-Waterman
         */
        private void loadMappingReport() {
            String subreportFolder, subreportPath;
            subreportFolder = ReadOnlyConfiguration
                    .getString("JASPER_COMPILED_FOLDER");
            subreportPath = subreportFolder+
                    ReadOnlyConfiguration.getString("JASPER_COMPILED_SUB_MAPPING");
            try {
                subreport_mapping_data = getDataSourceMapping(sourceResult);
                subreport_mapping_engine = (JasperReport) JRLoader.
                        loadObjectFromLocation(subreportPath);
            } catch (JRException ex) {
                Logger.getLogger(JRDataSourceFinalResult.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }

        /**
         * Incarc folrmularul Smith-Waterman
         */
        private void loadSmithReport() {
            String subreportFolder, subreportPath;
            subreportFolder = ReadOnlyConfiguration
                    .getString("JASPER_COMPILED_FOLDER");
            subreportPath = subreportFolder+
                    ReadOnlyConfiguration.getString("JASPER_COMPILED_SUB_SMITH_WATERMAN");
            try {
                subreport_smith_data = getDataSourceSmithWaterman(sourceResult);
                subreport_smith_engine = (JasperReport) JRLoader.
                        loadObjectFromLocation(subreportPath);
            } catch (JRException ex) {
                Logger.getLogger(JRDataSourceFinalResult.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }

        /**
         * Incarc folrmularul Smith-Waterman
         */
        private void loadPositioningReport() {
            String subreportFolder, subreportPath;
            subreportFolder = ReadOnlyConfiguration
                    .getString("JASPER_COMPILED_FOLDER");
            subreportPath = subreportFolder+
                    ReadOnlyConfiguration.getString("JASPER_COMPILED_SUB_POSITIONING");
            try {
                subreport_positioning_data = getDataSourcePositioning(sourceResult);
                subreport_positioning_engine = (JasperReport) JRLoader.
                        loadObjectFromLocation(subreportPath);
            } catch (JRException ex) {
                Logger.getLogger(JRDataSourceFinalResult.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }

        /**
         * Incarc folrmularul Smith-Waterman
         */
        private void loadGenesReport() {
            String subreportFolder, subreportPath;
            subreportFolder = ReadOnlyConfiguration
                    .getString("JASPER_COMPILED_FOLDER");
            subreportPath = subreportFolder+
                    ReadOnlyConfiguration.getString("JASPER_COMPILED_SUB_GENES");
            try {
                subreport_genes_data = getDataSourceGenes(sourceResult);
                subreport_genes_engine = (JasperReport) JRLoader.
                        loadObjectFromLocation(subreportPath);
            } catch (JRException ex) {
                Logger.getLogger(JRDataSourceFinalResult.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }

     /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *      Obtinerea independenta a surselor de date
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
        /**
         * Obtin data source-ul pentru tabelul de gene
         * @param sourceResult
         * @return
         */
        public static JRDataSource getDataSourceQuery(InfoQuery infoQuery) {
            //Fac un data source
            return new JRDataSourceSubQuery(infoQuery);
        }

        /**
         * Obtin data source-ul pentru tabelul de gene
         * @param sourceResult
         * @return
         */
        public static JRDataSource getDataSourceMapping(FinalResultItem sourceResult) {
            int imageWidth = DrawingConstants.REPORT_WIDTH;
            int imageHeight = sourceResult.getImageHeight(new JLabel());

            //Obtin canvasul pe care se va desena
            BufferedImage mappedImage = new BufferedImage(imageWidth, imageHeight,
                 BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = (Graphics2D) mappedImage.getGraphics();

            //~~~~~~~~~Desenez resultatul final~~~~~~~~~~~~~~
            sourceResult.paintImage(g2d, imageWidth, imageHeight, Color.WHITE, Color.BLACK);

            //Convertesc vectorul de imagni intr-o singura imagine
            ImageVector splitedImages = ImageManipulation.splitImage(mappedImage,
                    DrawingConstants.REPORT_MAPPING_CELL_HEIGHT);

            //Fac un data source
            return new JRDataSourceImageVector(splitedImages);
        }

        /**
         * Obtin data source-ul pentru tabelul de gene
         * @param sourceResult
         * @return
         */
        public static JRDataSource getDataSourceSmithWaterman(FinalResultItem sourceResult) {
            //Convertesc vectorul de imagni intr-o singura imagine
            FinalResultItemAlignment alignment = new FinalResultItemAlignment(sourceResult);
            BufferedImage alignmentImage = alignment.exportAsImage(DrawingConstants.REPORT_WIDTH,
                    Color.WHITE, Color.BLACK);
            ImageVector splitedImages = ImageManipulation.splitImage(alignmentImage,
                    DrawingConstants.REPORT_SW_CELL_HEIGHT);

            //Fac un data source
            return new JRDataSourceImageVector(splitedImages);
        }

        /**
         * Obtin data source-ul pentru tabelul de gene
         * @param sourceResult
         * @return
         */
        public static JRDataSource getDataSourcePositioning(FinalResultItem sourceResult) {
            IntervalMappingSet intervalMappingSet = sourceResult.getIntervalMappingSet();
            Iterator <IntervalMappingItem> iteratorMapping = intervalMappingSet.iterator();

            //Iterez prin toate pozitionarile de gene si construiesc un vector de imagini
            iteratorMapping = intervalMappingSet.iterator();
            ImageVector images = new ImageVector();
            while (iteratorMapping.hasNext()) {
                IntervalMappingItem mappingItem = iteratorMapping.next();
                GeneItemWrapper geneItemWrapper = mappingItem.getGeneItemWrapper();
                int itemHeight = geneItemWrapper.getImageHeight(new JLabel());

                //Desenez obiectul
                BufferedImage bufferedImage = new BufferedImage(DrawingConstants.REPORT_WIDTH,
                        itemHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();
                geneItemWrapper.paintImage(g2d, DrawingConstants.REPORT_WIDTH, itemHeight,
                        Color.WHITE, Color.BLACK);

                //Adaug in vectoru de imagini
                images.add(bufferedImage);
            }

            //Convertesc vectorul de imagni intr-o singura imagine
            BufferedImage mergedImage = ImageManipulation.joinImages(images);
            ImageVector splitedImages = ImageManipulation.splitImage(mergedImage,
                    DrawingConstants.REPORT_POSITIONING_CELL_HEIGHT);

            //DEBUG ONLY Scriu imaginea in fisier
            /*File destination = new File("/home/iulian/Desktop/genetica/draw.png");
            try {
            if (mergedImage != null ) ImageIO.write(mergedImage, MyUtils.IMAGE_EXT, destination);
            } catch (IOException ex) {
            System.err.println("Could not save");
            }*/
            //END DEBUG

            //Fac un data source
            return new JRDataSourceImageVector(splitedImages);
        }

        /**
         * Obtin data source-ul pentru tabelul de gene
         * @param sourceResult
         * @return
         */
        public static JRDataSource getDataSourceGenes(FinalResultItem sourceResult) {
            //Calculez dimensiuniile componentelor
            LinkedHashSet<GeneItem> geneItemSanitizer = new LinkedHashSet<GeneItem>();
            IntervalMappingSet intervalMappingSet = sourceResult.getIntervalMappingSet();
            Iterator <IntervalMappingItem> iteratorMapping = intervalMappingSet.iterator();
            while (iteratorMapping.hasNext()) {
                IntervalMappingItem mappingItem = iteratorMapping.next();
                GeneItemWrapper geneItemWrapper = mappingItem.getGeneItemWrapper();

                //Pastrez genele pentru desenare ulterioara
                geneItemSanitizer.addAll(geneItemWrapper.getFullGeneVector());
            }

            //Vectorul de gene
            GeneVector geneVector = new GeneVector();
            geneVector.addAll(geneItemSanitizer);
            Collections.sort(geneVector);

            //Fac un data source
            return new JRDataSourceSubGenes(geneVector);
        }        
        
      /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *            Obtinerea valorilor
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

        /**
         * Se va rula pentru fiecare subformular
         * counter = 0, LIMIT = 5
         * @return
         * @throws JRException
         */
        public boolean next() throws JRException {
            if (counter < LIMIT) {
                counter++;
                return true;
            } else return false;
        }

        public Object getFieldValue(JRField jrField) throws JRException {
            String fieldName = jrField.getName();
            BigDecimal aux;
            int scale = 2;

            switch (counter) {
                case 1:
                     if ("subreport_data".equals(fieldName)) {
                         return subreport_query_data;
                     } else
                     if ("subreport_engine".equals(fieldName)) {
                         return subreport_query_engine;
                     } else
                        throw new JRException("No field with specified name");
                case 2:
                     if ("subreport_data".equals(fieldName)) {
                         return subreport_mapping_data;
                     } else
                     if ("subreport_engine".equals(fieldName)) {
                         return subreport_mapping_engine;
                     } else
                        throw new JRException("No field with specified name");
                case 3:
                     if ("subreport_data".equals(fieldName)) {
                         return subreport_smith_data;
                     } else
                     if ("subreport_engine".equals(fieldName)) {
                         return subreport_smith_engine;
                     } else
                        throw new JRException("No field with specified name");
                case 4:
                     if ("subreport_data".equals(fieldName)) {
                         return subreport_positioning_data;
                     } else
                     if ("subreport_engine".equals(fieldName)) {
                         return subreport_positioning_engine;
                     } else
                        throw new JRException("No field with specified name");
                case 5:
                     if ("subreport_data".equals(fieldName)) {
                         return subreport_genes_data;
                     } else
                     if ("subreport_engine".equals(fieldName)) {
                         return subreport_genes_engine;
                     } else
                        throw new JRException("No field with specified name");
                default:
                    return null;
            }
        }
    }
