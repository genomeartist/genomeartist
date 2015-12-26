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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author iulian
 */
public class SubGenesTestDataSource implements JRDataSource {
        private Random rand = new Random(System.currentTimeMillis());
        private static final int LIMIT = 10;
        private int counter;
        private String f1,f2,f3,f4,f5,f6,f7;

        public SubGenesTestDataSource() {
            counter = 0;
            f1 = "gene";
            f2 = "dmel";
            f3 = "83b8-7B3B%";
            f4 = "12541123..122151512";
            f5 = "(+)";
            f6 = "FBgn02612451";
        }


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

             if ("sub_genes_name".equals(fieldName)) {
                 return f1+"_"+counter;
             } else
             if ("sub_genes_location".equals(fieldName)) {
                 return f2+"_"+counter;
             } else
             if ("sub_genes_cyto".equals(fieldName)) {
                 return f3+"_"+counter;
             } else
             if ("sub_genes_position".equals(fieldName)) {
                 return f4;
             } else
             if ("sub_genes_strand".equals(fieldName)) {
                 return f5;
             } else
             if ("sub_genes_id".equals(fieldName)) {
                 return f6;
             }  else {
                 System.out.println("fieldName = "+fieldName);
                throw new JRException("No field with specified name");
             }
        }
    }
