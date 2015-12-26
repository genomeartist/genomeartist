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

package ro.genomeartist.gui.controller.partialresult;

import ro.genomeartist.gui.custompaint.DrawingConstants;
import ro.genomeartist.gui.interfaces.ICanPaint;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import javax.swing.UIManager;

/**
 *
 * @author iulian
 */
public class PartialResultItemAlignment implements ICanPaint {
    
     /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *      Atributele clasei
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    private PartialResultItem partialResultItem;
    private Font monospaceFont;

    /**
     * Contruiesc un obiect care imbraca un partialResult cu scopul
     * de a desena alinierile
     * @param partialResultItem
     */
    public PartialResultItemAlignment(PartialResultItem partialResultItem) {
        this.partialResultItem = partialResultItem;

        //Creez un font monospaced
        Font font = UIManager.getDefaults().getFont("Table.font");
        this.monospaceFont = new Font(Font.MONOSPACED, Font.PLAIN, font.getSize());
    }

    /**
     * Metoda ce deseneaza alinierea unui Partial Result Item
     */
    public void paint(Graphics g, int width, int height, Color bgColor, Color fgColor) {
        Graphics2D g2d = (Graphics2D) g;

        // =========================
        // Parametrii de desenare
        // =========================
        FontMetrics fontMetrics; //Dimensiunile fontului
        int lineHeight; //Dimensiunea unei linii de text

        //pozitia curenta la care se deseneaza
        int localX,localY;
        int localWidth,localHeight;

        //Text pentru capetele intervalelor
        String queryLeftEnd,queryRightEnd;
        String genomLeftEnd,genomRightEnd;

        //Spatiul ocupat de text
        int leftTextLength; //Dimensiune scrisului de inceput
        int rigthTextLength; //Dimensiune scrisului de sfarsit
        int centerTextLength;

        //Culoarea de desenare
        Color colorIntervalBorder;
        Color colorIntervalFill;

        //Auxiliare pentru calcule
        String auxString;
        int auxInt;
        int stringHalf;

        // ====================
        // Calculez parametrii
        // ====================

        //Setez fontul
        g2d.setFont(monospaceFont);
        fontMetrics = g2d.getFontMetrics();
        lineHeight = fontMetrics.getHeight();

        //Calculez capetele de inceput si sfarsit
        queryLeftEnd = " "+partialResultItem.getPozitieStartQuery()+" ";
        queryRightEnd = " "+partialResultItem.getPozitieStopQuery()+" ";
        genomLeftEnd = " "+partialResultItem.getPozitieStartGenom()+" ";
        genomRightEnd = " "+partialResultItem.getPozitieStopGenom()+" ";
        
        //Calculez maximul de text in capete
        leftTextLength = fontMetrics.stringWidth(queryLeftEnd);
        auxInt = fontMetrics.stringWidth(genomLeftEnd);
        if (auxInt > leftTextLength) leftTextLength = auxInt;
        rigthTextLength = fontMetrics.stringWidth(queryRightEnd);
        auxInt = fontMetrics.stringWidth(genomRightEnd);
        if (auxInt > rigthTextLength) rigthTextLength = auxInt;

        //Calculez marimea textului in centru
        centerTextLength = fontMetrics.stringWidth(partialResultItem.getOut1());

        //Stabilesc culorile
        if (partialResultItem.isTransposon()) {
            colorIntervalBorder = DrawingConstants.COLOR_TRANSPOSON_BORDER;
            colorIntervalFill = DrawingConstants.COLOR_TRANSPOSON_FILL;
        } else {
            colorIntervalBorder = DrawingConstants.COLOR_GENOM_BORDER;
            colorIntervalFill = DrawingConstants.COLOR_GENOM_FILL;
        }

        // ====================
        // Incep sa desenez
        // ====================

        // ~~~~~~~~~~~~~~~~~Fundal~~~~~~~~~~~~~~~~~~~~~~~~

        // <editor-fold defaultstate="collapsed" desc="Desenez fundalul">
        //Fundalul pe care se deseneaza
        g2d.setStroke(new BasicStroke(DrawingConstants.LINEWIDTH));
        g2d.setColor(bgColor);
        g2d.fillRect(0, 0, width, height);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Desenez capetele">
        //Capat query stanga
        localX = DrawingConstants.MARGIN_LEFT;
        localY = DrawingConstants.MARGIN_TOP + lineHeight;
        g2d.setColor(colorIntervalBorder);
        g2d.drawString(queryLeftEnd, localX, localY);

        //Capat query dreapta
        localX = DrawingConstants.MARGIN_LEFT + leftTextLength + centerTextLength;
        localY = DrawingConstants.MARGIN_TOP + lineHeight;
        g2d.setColor(colorIntervalBorder);
        g2d.drawString(queryRightEnd, localX, localY);

        //Capat genom stanga
        localX = DrawingConstants.MARGIN_LEFT;
        localY = DrawingConstants.MARGIN_TOP + 3 * lineHeight;
        g2d.setColor(colorIntervalBorder);
        g2d.drawString(genomLeftEnd, localX, localY);

        //Capat genom dreapta
        localX = DrawingConstants.MARGIN_LEFT + leftTextLength + centerTextLength;
        localY = DrawingConstants.MARGIN_TOP + 3 * lineHeight;
        g2d.setColor(colorIntervalBorder);
        g2d.drawString(genomRightEnd, localX, localY);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Desenez alinierea">
        //Secventa out1
        localX = DrawingConstants.MARGIN_LEFT + leftTextLength;
        localY = DrawingConstants.MARGIN_TOP + lineHeight;
        g2d.setColor(fgColor);
        g2d.drawString(partialResultItem.getOut1(), localX, localY);

        //Secventa relation
        localX = DrawingConstants.MARGIN_LEFT + leftTextLength;
        localY = DrawingConstants.MARGIN_TOP + 2 * lineHeight;
        g2d.setColor(fgColor);
        g2d.drawString(partialResultItem.getRelation(), localX, localY);

        //Secventa out2
        localX = DrawingConstants.MARGIN_LEFT + leftTextLength;
        localY = DrawingConstants.MARGIN_TOP + 3 * lineHeight;
        g2d.setColor(fgColor);
        g2d.drawString(partialResultItem.getOut2(), localX, localY);
        // </editor-fold>
    }

    /**
     * Get recomended height
     */
    public int getRecommendedHeight(JComponent component) {
        int lineHeight = component.getFontMetrics(monospaceFont).getHeight();
        int recommendedSize = DrawingConstants.MARGIN_TOP +
                DrawingConstants.MARGIN_BOTTOM + 5*lineHeight;
        return recommendedSize;
    }

    /**
     * Get recomended qidth
     */
    public int getRecommendedWidth(JComponent component) {
        FontMetrics fontMetrics = component.getFontMetrics(monospaceFont);
        int recommendedSize = fontMetrics.stringWidth(partialResultItem.getOut1());
        recommendedSize += DrawingConstants.MARGIN_LEFT +
                DrawingConstants.MARGIN_RIGHT +
                2*fontMetrics.stringWidth("AAAAAAAAAAAA");
        return recommendedSize;
    }

}
