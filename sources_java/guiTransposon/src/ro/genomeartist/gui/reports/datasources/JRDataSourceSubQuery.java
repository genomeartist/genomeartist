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

import ro.genomeartist.gui.controller.query.InfoQuery;
import java.math.BigDecimal;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author iulian
 */
public class JRDataSourceSubQuery implements JRDataSource {
        private InfoQuery infoQuery;
        boolean hasNext;

        public JRDataSourceSubQuery(InfoQuery infoQuery) {
            this.infoQuery = infoQuery;
            hasNext = true;
        }


        public boolean next() throws JRException {
            if (hasNext) {
                hasNext = false;
                return true;
            } else return false;
        }

        public Object getFieldValue(JRField jrField) throws JRException {
            String fieldName = jrField.getName();
            BigDecimal aux;
            int scale = 2;

             if ("sub_query".equals(fieldName)) {
                 return infoQuery.query;
             } else
                     {
                 System.out.println("fieldName = "+fieldName);
                throw new JRException("No field with specified name");
             }
        }
    }
