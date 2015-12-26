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

package ro.genomeartist.gui.reports.development;

import ro.genomeartist.gui.utils.ReadOnlyConfiguration;
import java.math.BigDecimal;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author iulian
 */
public class ResultReportDataSource implements JRDataSource {
        private Random rand = new Random(System.currentTimeMillis());
        private static final int LIMIT = 5;
        private int counter;

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

        public ResultReportDataSource() {
            counter = 0;

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
        public void loadQueryReport() {
            String subreportFolder, subreportPath;
            subreportFolder = ReadOnlyConfiguration
                    .getString("JASPER_COMPILED_FOLDER");
            subreportPath = subreportFolder+
                    ReadOnlyConfiguration.getString("JASPER_COMPILED_SUB_QUERY");
            try {
                subreport_query_data = new SubQueryTestDataSource();
                subreport_query_engine = (JasperReport) JRLoader.
                        loadObjectFromLocation(subreportPath);
            } catch (JRException ex) {
                Logger.getLogger(ResultReportDataSource.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }

        /**
         * Incarc folrmularul Smith-Waterman
         */
        public void loadMappingReport() {
            String subreportFolder, subreportPath;
            subreportFolder = ReadOnlyConfiguration
                    .getString("JASPER_COMPILED_FOLDER");
            subreportPath = subreportFolder+
                    ReadOnlyConfiguration.getString("JASPER_COMPILED_SUB_MAPPING");
            try {
                subreport_mapping_data = new SubMappingTestDataSource();
                subreport_mapping_engine = (JasperReport) JRLoader.
                        loadObjectFromLocation(subreportPath);
            } catch (JRException ex) {
                Logger.getLogger(ResultReportDataSource.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }

        /**
         * Incarc folrmularul Smith-Waterman
         */
        public void loadSmithReport() {
            String subreportFolder, subreportPath;
            subreportFolder = ReadOnlyConfiguration
                    .getString("JASPER_COMPILED_FOLDER");
            subreportPath = subreportFolder+
                    ReadOnlyConfiguration.getString("JASPER_COMPILED_SUB_SMITH_WATERMAN");
            try {
                subreport_smith_data = new SubSmithWatermanTestDataSource();
                subreport_smith_engine = (JasperReport) JRLoader.
                        loadObjectFromLocation(subreportPath);
            } catch (JRException ex) {
                Logger.getLogger(ResultReportDataSource.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }

        /**
         * Incarc folrmularul Smith-Waterman
         */
        public void loadPositioningReport() {
            String subreportFolder, subreportPath;
            subreportFolder = ReadOnlyConfiguration
                    .getString("JASPER_COMPILED_FOLDER");
            subreportPath = subreportFolder+
                    ReadOnlyConfiguration.getString("JASPER_COMPILED_SUB_POSITIONING");
            try {
                subreport_positioning_data = new SubPositioningTestDataSource();
                subreport_positioning_engine = (JasperReport) JRLoader.
                        loadObjectFromLocation(subreportPath);
            } catch (JRException ex) {
                Logger.getLogger(ResultReportDataSource.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }

        /**
         * Incarc folrmularul Smith-Waterman
         */
        public void loadGenesReport() {
            String subreportFolder, subreportPath;
            subreportFolder = ReadOnlyConfiguration
                    .getString("JASPER_COMPILED_FOLDER");
            subreportPath = subreportFolder+
                    ReadOnlyConfiguration.getString("JASPER_COMPILED_SUB_GENES");
            try {
                subreport_genes_data = new SubGenesTestDataSource();
                subreport_genes_engine = (JasperReport) JRLoader.
                        loadObjectFromLocation(subreportPath);
            } catch (JRException ex) {
                Logger.getLogger(ResultReportDataSource.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
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
