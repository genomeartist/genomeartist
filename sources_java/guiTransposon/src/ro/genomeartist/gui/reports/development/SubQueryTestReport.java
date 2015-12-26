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
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author iulian
 */
public class SubQueryTestReport {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       String templateFolder,templatePath;

        //Citesc configuratia config.txt
        ReadOnlyConfiguration.init();
        
        templateFolder = ReadOnlyConfiguration.getString("JASPER_TEMPLATE_FOLDER");
        templatePath = templateFolder+ReadOnlyConfiguration
                .getString("JASPER_TEMPLATE_SUB_QUERY");

        Map<String, Object> params = new HashMap<String, Object>();
        //params.put("sub_param_nume", "Cheltuieli directe");
        try
        {
            JasperReport jasperReport =
                JasperCompileManager.compileReport(templatePath);


            JasperPrint jasperPrint =
                JasperFillManager.fillReport(
                    jasperReport, params, new SubQueryTestDataSource());

            JasperViewer.viewReport(jasperPrint);
        }

        catch (JRException ex)
        {
            ex.printStackTrace();
        }
    }
}
