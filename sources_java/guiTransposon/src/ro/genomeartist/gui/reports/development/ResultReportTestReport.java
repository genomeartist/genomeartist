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
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author iulian
 */
public class ResultReportTestReport {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       String templateFolder,templatePath;
       String compiledFolder, compiledPath;

        //Citesc configuratia config.txt
        ReadOnlyConfiguration.init();

        Integer test = new Integer(1);

        //Master report
        templateFolder = ReadOnlyConfiguration.getString("JASPER_TEMPLATE_FOLDER");
        templatePath = templateFolder+ReadOnlyConfiguration.getString("JASPER_TEMPLATE_RESULT_REPORT");
        compiledFolder = ReadOnlyConfiguration.getString("JASPER_COMPILED_FOLDER");
        compiledPath = compiledFolder+ReadOnlyConfiguration.getString("JASPER_COMPILED_RESULT_REPORT");

        //Compilez subrapoartele
        compileQueryReport();
        compileMapping();
        compileSmithReport();
        compilePositioning();
        compileGenes();
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("titlu", "Search Result");
        params.put("header_left", "header_left");
        params.put("header_right", "header_right");
        params.put("footer", "footer");

        params.put("queryName", "My Query");
        params.put("searchDate", "31 sept 2e14");
        params.put("version", "software ver1.32");

        //Compilez si populez raportul master
        try
        {
            JasperCompileManager.compileReportToFile(
                    templatePath,
                    compiledPath);

            JasperReport jasperReport =(JasperReport) JRLoader.
                        loadObjectFromLocation(compiledPath);

            JasperPrint jasperPrint =
                JasperFillManager.fillReport(
                    jasperReport, params, new ResultReportDataSource());

            JasperViewer.viewReport(jasperPrint);
        }

        catch (JRException ex)
        {
            ex.printStackTrace();
        }
    }

      /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *   Compilez subrapoartele
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Compilez raportul pentru query
     */
    public static void compileQueryReport() {
       String subreportFolder,subreportPath;
       String compiledSubFolder, compiledSubPath;

        //Subreport
        subreportFolder = ReadOnlyConfiguration.getString("JASPER_TEMPLATE_FOLDER");
        subreportPath = subreportFolder+ReadOnlyConfiguration.getString("JASPER_TEMPLATE_SUB_QUERY");
        compiledSubFolder = ReadOnlyConfiguration.getString("JASPER_COMPILED_FOLDER");
        compiledSubPath = compiledSubFolder+ReadOnlyConfiguration.getString("JASPER_COMPILED_SUB_QUERY");

        //Compilez si populez subreportul
        try
        {
            JasperCompileManager.compileReportToFile(
                    subreportPath,
                    compiledSubPath);
        }
        catch (JRException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Compilez raportul Mapare
     */
    public static void compileMapping() {
       String subreportFolder,subreportPath;
       String compiledSubFolder, compiledSubPath;

        //Subreport
        subreportFolder = ReadOnlyConfiguration.getString("JASPER_TEMPLATE_FOLDER");
        subreportPath = subreportFolder+ReadOnlyConfiguration.getString("JASPER_TEMPLATE_SUB_MAPPING");
        compiledSubFolder = ReadOnlyConfiguration.getString("JASPER_COMPILED_FOLDER");
        compiledSubPath = compiledSubFolder+ReadOnlyConfiguration.getString("JASPER_COMPILED_SUB_MAPPING");

        //Compilez si populez subreportul
        try
        {
            JasperCompileManager.compileReportToFile(
                    subreportPath,
                    compiledSubPath);
        }
        catch (JRException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Compilez raportul Smith-Waterman
     */
    public static void compileSmithReport() {
       String subreportFolder,subreportPath;
       String compiledSubFolder, compiledSubPath;

        //Subreport
        subreportFolder = ReadOnlyConfiguration.getString("JASPER_TEMPLATE_FOLDER");
        subreportPath = subreportFolder+ReadOnlyConfiguration.getString("JASPER_TEMPLATE_SUB_SMITH_WATERMAN");
        compiledSubFolder = ReadOnlyConfiguration.getString("JASPER_COMPILED_FOLDER");
        compiledSubPath = compiledSubFolder+ReadOnlyConfiguration.getString("JASPER_COMPILED_SUB_SMITH_WATERMAN");

        //Compilez si populez subreportul
        try
        {
            JasperCompileManager.compileReportToFile(
                    subreportPath,
                    compiledSubPath);
        }
        catch (JRException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Compilez raportul Smith-Waterman
     */
    public static void compilePositioning() {
       String subreportFolder,subreportPath;
       String compiledSubFolder, compiledSubPath;

        //Subreport
        subreportFolder = ReadOnlyConfiguration.getString("JASPER_TEMPLATE_FOLDER");
        subreportPath = subreportFolder+ReadOnlyConfiguration.getString("JASPER_TEMPLATE_SUB_POSITIONING");
        compiledSubFolder = ReadOnlyConfiguration.getString("JASPER_COMPILED_FOLDER");
        compiledSubPath = compiledSubFolder+ReadOnlyConfiguration.getString("JASPER_COMPILED_SUB_POSITIONING");

        //Compilez si populez subreportul
        try
        {
            JasperCompileManager.compileReportToFile(
                    subreportPath,
                    compiledSubPath);
        }
        catch (JRException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Compilez raportul Smith-Waterman
     */
    public static void compileGenes() {
       String subreportFolder,subreportPath;
       String compiledSubFolder, compiledSubPath;

        //Subreport
        subreportFolder = ReadOnlyConfiguration.getString("JASPER_TEMPLATE_FOLDER");
        subreportPath = subreportFolder+ReadOnlyConfiguration.getString("JASPER_TEMPLATE_SUB_GENES");
        compiledSubFolder = ReadOnlyConfiguration.getString("JASPER_COMPILED_FOLDER");
        compiledSubPath = compiledSubFolder+ReadOnlyConfiguration.getString("JASPER_COMPILED_SUB_GENES");

        //Compilez si populez subreportul
        try
        {
            JasperCompileManager.compileReportToFile(
                    subreportPath,
                    compiledSubPath);
        }
        catch (JRException ex)
        {
            ex.printStackTrace();
        }
    }


}
