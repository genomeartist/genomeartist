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

import ro.genomeartist.gui.utils.ReadWriteConfiguration;
import ro.genomeartist.gui.custompaint.DrawingConstants;
import ro.genomeartist.gui.interfaces.ICanPaint;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.UIManager;

/**
 * Structura unui rezultat partial
 * Se va folosi ca structura de date
 * @author iulian
 */
public class PartialResultItem implements Comparable,ICanPaint {

     /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *      Atributele clasei
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    //Informatii despre pozitionare
    private String fisierOrigine;
    private int pozitieQuery;	//offsetul alinieri primei secvente
    private int pozitieGenom;	//offsetul alinierii celei de-a doua secvente
    private int lengthQuery;	//lungimea care s-a potrivit din query
    private int lengthGenom;    //lungimea care s-a potrivit din genom
    
    //Informatii de afisare
    private int length;		//lungimea secventei rezultat
    private int score;		//Scorul obtinut de aliniere
    private String out1;		//primul string aliniat
    private String relation;	//relatia (liniutze)
    private String out2;		//al doilea string aliniat

    //Informatii despre provenienta
    private boolean isComplement;    //Daca rezultatul este pe catena complementara
    private boolean isTransposon;    //Daca rezultatul este transposon
    private int lengthInitialQuery; //Lungimea initiala a secventei cautate

    //###Display offset
    private int offset;

    /**
     * Constructorul default
     */
    public PartialResultItem() {

        //Obtin offsetul de afisare a pozitiilor ( 0 sau +1)
        offset = Integer.parseInt((String)ReadWriteConfiguration.get("NUMBERING_OFFSET"));
    }

    /**
     * Metoda ce deseneaza acest Partial Result Item
     */
    public void paint(Graphics g, int width ,int height, Color bgColor, Color fgColor ) {
        Graphics2D g2d = (Graphics2D) g;

        // =========================
        // Parametrii de desenare
        // =========================
        FontMetrics fontMetrics; //Dimensiunile fontului
        int lineHeight; //Dimensiunea unei linii de text
        int leftTextLength; //Dimensiune scrisului de inceput
        int rigthTextLength; //Dimensiune scrisului de sfarsit
        int chenarWidth;     //Dimensiunea chenarului cu secventa

        //pozitia curenta la care se deseneaza
        int localX,localY;
        int localWidth,localHeight;
        
        //Capetele intervalului
        String leftEnd;
        String rightEnd;

        //Capetele intervalului
        int markerLeft,intervalLeft;
        int markerRight,intervalRight;
        int intervalWidth;

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

        //Dimensiunea unei linii de text
        fontMetrics = g2d.getFontMetrics();
        lineHeight = fontMetrics.getHeight();        

        //Capetele intervalului
        leftEnd = new String(" "+offset+" ");
        int auxRightEnd = offset + this.getLengthInitialQuery() -1;
        rightEnd = new String(" "+auxRightEnd+" ");
        leftTextLength = fontMetrics.stringWidth(leftEnd);
        rigthTextLength = fontMetrics.stringWidth(rightEnd);
        chenarWidth = width - DrawingConstants.MARGIN_LEFT -
                DrawingConstants.MARGIN_RIGHT - leftTextLength - rigthTextLength;

        //Capetele markerului
        markerLeft = (int)((this.pozitieQuery/(double)this.getLengthInitialQuery())*chenarWidth);
        intervalLeft = DrawingConstants.MARGIN_LEFT + markerLeft + leftTextLength;
        markerRight = (int)(((this.pozitieQuery+this.getLengthQuery())
                /(double)this.getLengthInitialQuery())*chenarWidth);
        intervalRight = DrawingConstants.MARGIN_LEFT + markerRight + leftTextLength;
        intervalWidth = intervalRight - intervalLeft;

        //Stabilesc culorile
        if (isTransposon) {
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

        //Umplerea secventei
        localX = DrawingConstants.MARGIN_LEFT + leftTextLength;
        localY =DrawingConstants. MARGIN_TOP + lineHeight;
        localWidth = chenarWidth;
        localHeight = lineHeight;
        g2d.setColor(DrawingConstants.COLOR_CHENAR_FILL);
        g2d.fillRoundRect(localX, localY, localWidth, localHeight, 
                DrawingConstants.ARC_WIDTH, DrawingConstants.ARC_HEIGHT);

        //Chenarul secventei
        g2d.setColor(DrawingConstants.COLOR_CHENAR_BORDER);
        g2d.drawRoundRect(localX, localY, localWidth, localHeight, 
                DrawingConstants.ARC_WIDTH, DrawingConstants.ARC_HEIGHT);

        //Pozitionez textul de capete
        localX = DrawingConstants.MARGIN_LEFT;
        localY = DrawingConstants.MARGIN_TOP + 2 * lineHeight -
                DrawingConstants.ARC_HEIGHT;
        g2d.setColor(fgColor);
        g2d.drawString(leftEnd, localX, localY);

        //Pozitionez textul de capete
        localX = width - DrawingConstants.MARGIN_RIGHT - rigthTextLength;
        localY = DrawingConstants.MARGIN_TOP + 2 * lineHeight -
                DrawingConstants.ARC_HEIGHT;
        g2d.setColor(fgColor);
        g2d.drawString(rightEnd, localX, localY);
        // </editor-fold>

        // ~~~~~~~~~~~~~~~~~~Interval~~~~~~~~~~~~~~~~~~~~~~~

        // <editor-fold defaultstate="collapsed" desc="Desenez intervalul">
        //Marchez pozitia intervalului
        localX = intervalLeft;
        localY = DrawingConstants.MARGIN_TOP + lineHeight;
        localWidth = intervalWidth;
        localHeight = lineHeight;
        g2d.setColor(colorIntervalFill);
        g2d.fillRoundRect(localX, localY, localWidth, localHeight, 
                DrawingConstants.ARC_WIDTH, DrawingConstants.ARC_HEIGHT);

        //Chenarul intervalului
        g2d.setColor(colorIntervalBorder);
        g2d.drawRoundRect(localX, localY, localWidth, localHeight, 
                DrawingConstants.ARC_WIDTH, DrawingConstants.ARC_HEIGHT);

        //Marchez pozitia intervalului
        localX = intervalLeft;
        localY = DrawingConstants.MARGIN_TOP + lineHeight;
        localWidth = intervalRight - intervalLeft;
        localHeight = lineHeight;
        g2d.setColor(colorIntervalFill);
        g2d.fillRoundRect(localX, localY, localWidth, localHeight, 
                DrawingConstants.ARC_WIDTH, DrawingConstants.ARC_HEIGHT);

        //Chenarul intervalului
        g2d.setColor(colorIntervalBorder);
        g2d.drawRoundRect(localX, localY, localWidth, localHeight, 
                DrawingConstants.ARC_WIDTH, DrawingConstants.ARC_HEIGHT);

        // </editor-fold>

        // ~~~~~~~~~~~~~~~~~~~Sageata~~~~~~~~~~~~~~~~~~~~~~

        // <editor-fold defaultstate="collapsed" desc="Desenez sageata de sens">
        //Marchez sensul intervalului
        if (intervalWidth - 4 * DrawingConstants.LINEWIDTH > DrawingConstants.ARROW_WIDTH) {
            auxInt = (intervalWidth - DrawingConstants.ARROW_WIDTH) / 2;
            localX = intervalLeft + auxInt;
            localY = DrawingConstants.MARGIN_TOP + lineHeight + lineHeight / 2;
            localWidth = DrawingConstants.ARROW_WIDTH;
            localHeight = 0;

            //antialias on
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(colorIntervalBorder);
            g2d.drawLine(localX, localY, localX + localWidth, localY + localHeight);

            //Desenez varful sagetii
            auxInt = (lineHeight / 2) - 2 * DrawingConstants.LINEWIDTH;
            if (isComplement) {
                localX = localX - DrawingConstants.LINEWIDTH;
                g2d.drawLine(localX, localY, localX + auxInt, localY - auxInt);
                g2d.drawLine(localX, localY, localX + auxInt, localY + auxInt);
            } else {
                localX = localX + localWidth + DrawingConstants.LINEWIDTH;
                g2d.drawLine(localX, localY, localX - auxInt, localY - auxInt);
                g2d.drawLine(localX, localY, localX - auxInt, localY + auxInt);
            }

            //reset antialias
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_DEFAULT);
        }
        // </editor-fold>

        // ~~~~~~~~~~~~~~~~~~~Text~~~~~~~~~~~~~~~~~~~~~~

        // <editor-fold defaultstate="collapsed" desc="Text pozitie secventa">
        //Plasez textul de mapare secventa
        auxString = this.getPozitieStartQuery() + "";
        auxInt = fontMetrics.stringWidth(auxString);
        stringHalf = auxInt / 2;
        localX = intervalLeft - stringHalf;
        localY = DrawingConstants.MARGIN_TOP + lineHeight - DrawingConstants.ARC_HEIGHT;
        g2d.setColor(colorIntervalBorder);
        g2d.drawString(auxString, localX, localY);

        //Plasez textul de mapare secventa
        auxString = this.getPozitieStopQuery() + "";
        auxInt = fontMetrics.stringWidth(auxString);
        if (auxInt < intervalWidth) {
            stringHalf = auxInt / 2;
            localX = intervalRight - stringHalf;
            localY = DrawingConstants.MARGIN_TOP + lineHeight - DrawingConstants.ARC_HEIGHT;
            g2d.setColor(colorIntervalBorder);
            g2d.drawString(auxString, localX, localY);
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Text pozitie genom">
        //Plasez textul de mapare genom
        auxString = this.getPozitieStartGenom() + "";
        auxInt = fontMetrics.stringWidth(auxString);
        stringHalf = auxInt / 2;
        localX = intervalLeft - stringHalf;
        localY = DrawingConstants.MARGIN_TOP + 3 * lineHeight;
        g2d.setColor(colorIntervalBorder);
        g2d.drawString(auxString, localX, localY);

        //Plasez textul de mapare genom
        auxString = this.getPozitieStopGenom() + "";
        auxInt = fontMetrics.stringWidth(auxString);
        if (auxInt < intervalWidth) {
            stringHalf = auxInt / 2;
            localX = intervalRight - stringHalf;
            localY = DrawingConstants.MARGIN_TOP + 3 * lineHeight;
            g2d.setColor(colorIntervalBorder);
            g2d.drawString(auxString, localX, localY);
        }
        // </editor-fold>

        
    }
            
    /**
     * Get recomended height
     */
    public int getRecommendedHeight(JComponent component) {
        Font font = UIManager.getDefaults().getFont("Table.font");
        int lineHeight = component.getFontMetrics(font).getHeight();
        int recommendedSize = DrawingConstants.MARGIN_TOP +
                DrawingConstants.MARGIN_BOTTOM + 3*lineHeight;
        return recommendedSize;
    }

    /**
     * Get recomended qidth
     */
    public int getRecommendedWidth(JComponent component) {
        return DrawingConstants.RECOMMENDED_WIDTH;
    }

    /**
     * Metoda ce determina ordinea naturala a obiectelor
     * Se compara pozitia din query
     * @param o Obiectul cu care se compara
     * @return negativ, zero sau pozitiv daca obiectul este mai mic,
     * egal sau mai mare decat obiectul verificat
     */
    public int compareTo(Object o) {
        if (o instanceof PartialResultItem) {
            PartialResultItem resultItem = (PartialResultItem) o;
            return this.getPozitieStartQuery() - resultItem.getPozitieStartQuery();
        } else throw new ClassCastException("Cannot compare");
    }

     /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *      Getter and setter
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    public String getFisierOrigine() {
        return fisierOrigine;
    }

    public void setFisierOrigine(String fisierOrigine) {
        this.fisierOrigine = fisierOrigine;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public boolean isComplement() {
        return isComplement;
    }

    public void setIsComplement(boolean isComplement) {
        this.isComplement = isComplement;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public boolean isTransposon() {
        return isTransposon;
    }

    public void setIsTransposon(boolean isTransposon) {
        this.isTransposon = isTransposon;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public int getLengthGenom() {
        return lengthGenom;
    }

    public void setLengthGenom(int lengthGenom) {
        this.lengthGenom = lengthGenom;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public int getLengthInitialQuery() {
        return lengthInitialQuery;
    }

    public void setLengthInitialQuery(int lengthInitialQuery) {
        this.lengthInitialQuery = lengthInitialQuery;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public int getLengthQuery() {
        return lengthQuery;
    }

    public void setLengthQuery(int lengthQuery) {
        this.lengthQuery = lengthQuery;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public String getOut1() {
        return out1;
    }

    public void setOut1(String out1) {
        this.out1 = out1;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public String getOut2() {
        return out2;
    }

    public void setOut2(String out2) {
        this.out2 = out2;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public int getPozitieStartGenom() {
        if (isComplement)
            return pozitieGenom + offset +lengthGenom - 1;
        else return pozitieGenom + offset;
    }

    public void setPozitieStartGenom(int pozitieGenom) {
        this.pozitieGenom = pozitieGenom;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public int getPozitieStartQuery() {
        return pozitieQuery + offset;
    }

    public void setPozitieStartQuery(int pozitieQuery) {
        this.pozitieQuery = pozitieQuery;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public int getPozitieStopQuery() {
        return pozitieQuery + offset +lengthQuery - 1;
    }

    public int getPozitieStopGenom() {
        if (isComplement)
            return pozitieGenom + offset;
        else return pozitieGenom + offset +lengthGenom - 1;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
