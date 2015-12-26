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

import ro.genomeartist.gui.controller.genes.GeneItem;
import ro.genomeartist.gui.controller.genes.GeneVector;
import ro.genomeartist.gui.custompaint.DrawingConstants;
import java.math.BigDecimal;
import java.util.Iterator;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author iulian
 */
public class JRDataSourceSubGenes implements JRDataSource {
        private GeneVector geneVector;
        private Iterator<GeneItem> iterator;
        private GeneItem geneItem;

        public JRDataSourceSubGenes(GeneVector geneVector) {
            this.geneVector = geneVector;
            iterator = geneVector.iterator();
        }


        public boolean next() throws JRException {
            if (iterator.hasNext()) {
                geneItem = iterator.next();
                return true;
            } else return false;
        }

        public Object getFieldValue(JRField jrField) throws JRException {
            String fieldName = jrField.getName();
            BigDecimal aux;
            int scale = 2;

             if ("sub_genes_name".equals(fieldName)) {
                 return geneItem.getName();
             } else
             if ("sub_genes_location".equals(fieldName)) {
                 return geneItem.getFisierOrigine();
             } else
             if ("sub_genes_cyto".equals(fieldName)) {
                 return geneItem.getCyto();
             } else
             if ("sub_genes_position".equals(fieldName)) {
                 return geneItem.getLocationStart()+".."+geneItem.getLocationEnd();
             } else
             if ("sub_genes_strand".equals(fieldName)) {
                 if (geneItem.isComplement())
                     return DrawingConstants.TEXT_STRAND_COMPLEMENTARY;
                 else return DrawingConstants.TEXT_STRAND_FORWARD;
             } else
             if ("sub_genes_id".equals(fieldName)) {
                 return geneItem.getGeneId();
             }  else {
                 System.out.println("fieldName = "+fieldName);
                throw new JRException("No field with specified name");
             }
        }
    }
