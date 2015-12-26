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

package ro.genomeartist.coloredtable.cellrenderers;

import ro.genomeartist.components.utils.GraphicRenderingUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 * Implementarea default a cellRenderului
 * @author iulian
 */
public class DefaultColoredCellRenderer extends AbstractColoredCellRenderer {

    public DefaultColoredCellRenderer() {
        super();
    }

    /** Overridden to combine the expansion border (whose insets determine how
     * much a child tree node is shifted to the right relative to the ancestor
     * root node) with whatever border is set, as a CompoundBorder.  The expansion
     * border is also responsible for drawing the expansion icon.  */
    @Override
    public void setBorder(Border b) {
        super.setBorder(b);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/
    //      Clase de desenare
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/

    @Override
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        //Setez parametrii in parinte
        super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);

        //DEBUG
        //if (hasFocus)
        //    System.out.println(this);


        //Intorc aceasta componenta pentru a-i suprascrie paintul
        return this ;
    }

    /**
     * Suprascriu paintul pentru a putea desena celulele editabile
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();
        int startX,startY;
        int focusMarkerSize = 4;
        int iconWidth = 16;

        //Culorile
        Color actualBgColor, actualFgColor; //Culoarea setata in pregatiri
        actualBgColor = getBackground();
        actualFgColor = getForeground();
        Color prefferedBgColor, prefferedFgColor;
        prefferedBgColor = getPrefferedBackground();
        prefferedFgColor = getPrefferedForeground();

        //Desenez backgroundul
        g2d.setColor(actualBgColor);
        g2d.fillRect(0, 0, width, height);
        setBackground(null);

        //Caz special daca celula este editabila
        if (isCellEditable()) {
            boolean isLeftAligned = getHorizontalAlignment() == SwingConstants.LEFT ||
                    getHorizontalAlignment() == SwingConstants.LEADING;

            //Aleg culorile pentru desenare
            Color colorLeft;
            Color colorRight;
            if (isSelected()) {
                colorLeft = g2d.getColor();
                colorRight = GraphicRenderingUtils.lightenColor(colorLeft, 2*getColorOffset());
            } else {
                colorLeft = g2d.getColor();
                colorRight = Color.WHITE;
            }

            //Fac desenarea propriu-zisa
            GraphicRenderingUtils.drawEditableBackground(g2d,colorLeft,
                    colorRight,width,height,iconWidth,isLeftAligned);

            //Invalidez fundalul
            setBackground(null);
        }

        //Desenez focusul
        if (hasSomeFocus()) {
            g2d.setColor(actualFgColor);

            //Stanga - Sus
            startX = 0;
            startY = 0;
            g2d.drawLine(startX, startY,
                    startX, focusMarkerSize);
            g2d.drawLine(startX, startY,
                    focusMarkerSize, startY);

            //Stanga - Jos
            startX = 0;
            startY = height-1;
            g2d.drawLine(startX, startY,
                    startX, startY-focusMarkerSize);
            g2d.drawLine(startX, startY,
                    focusMarkerSize, startY);

            //Dreapta - Jos
            startX = width-1;
            startY = height-1;
            g2d.drawLine(startX, startY,
                    startX, startY-focusMarkerSize);
            g2d.drawLine(startX, startY,
                    startX-focusMarkerSize, startY);

            //Dreapta - Sus
            startX = width-1;
            startY = 0;
            g2d.drawLine(startX, startY,
                    startX, focusMarkerSize);
            g2d.drawLine(startX, startY,
                    startX-focusMarkerSize, startY);
        }

        super.paint(g);
    }
}
