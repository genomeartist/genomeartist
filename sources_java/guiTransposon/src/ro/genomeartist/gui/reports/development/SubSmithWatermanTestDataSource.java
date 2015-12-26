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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.Random;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author iulian
 */
public class SubSmithWatermanTestDataSource implements JRDataSource {
        private Random rand = new Random(System.currentTimeMillis());
        private static final int LIMIT = 10;
        private int counter;
        private Image f1;

        public static final int IMAGE_HEIGHT = 90;

        public SubSmithWatermanTestDataSource() {
            counter = 0;

            f1 = new BufferedImage(555, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = (Graphics2D) f1.getGraphics();

            //Set antialias
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            //Setez parametrii
            g2d.setStroke(new BasicStroke(2));
            g2d.setBackground(Color.WHITE);
            g2d.clearRect(0, 0, 555, IMAGE_HEIGHT);

            g2d.setColor(Color.BLACK);
            g2d.drawLine(0, 0, 555, IMAGE_HEIGHT);
            g2d.drawLine(0, IMAGE_HEIGHT, 555, 0);
            g2d.drawOval(0, 0, 555, IMAGE_HEIGHT);

            //Set antialias
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_OFF);
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

             if ("sub_image".equals(fieldName)) {
                 return f1;
             }  else {
                 System.out.println("fieldName = "+fieldName);
                throw new JRException("No field with specified name");
             }
        }
    }
