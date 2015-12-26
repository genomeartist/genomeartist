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

import ro.genomeartist.gui.utils.TestData;
import java.math.BigDecimal;
import java.util.Random;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author iulian
 */
public class SubQueryTestDataSource implements JRDataSource {
        private Random rand = new Random(System.currentTimeMillis());
        private static final int LIMIT = 1;
        private int counter;

        public static final int IMAGE_HEIGHT = 60;

        public SubQueryTestDataSource() {
            counter = 0;
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

             if ("sub_query".equals(fieldName)) {
                 return TestData.getTestInfoQuery().query;
             }  else {
                 System.out.println("fieldName = "+fieldName);
                throw new JRException("No field with specified name");
             }
        }
    }
