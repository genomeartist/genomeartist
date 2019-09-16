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

package ro.genomeartist.gui.controller.finalresult;

import ro.genomeartist.gui.utils.ReadWriteConfiguration;
import ro.genomeartist.gui.custompaint.DrawingConstants;
import ro.genomeartist.gui.interfaces.ICanPaint;
import ro.genomeartist.gui.controller.exporters.FinalResultExporter;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Iterator;
import javax.swing.JComponent;
import javax.swing.UIManager;

/**
 *
 * @author iulian
 */
public class FinalResultItem implements ICanPaint {

     /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *      Atributele clasei
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    //Informatii generale
    private int score;
    private int lengthInitialQuery; //Lungimea initiala a secventei cautate

    //Informatii despre afisate
    private int outTotalLength;	//lungimea secventei rezultat
    private String out1;		//primul string aliniat
    private String relation;	//relatia (liniutze)
    private String out2;		//al doilea string aliniat

    //Informatii despre intervale
    private IntervalMappingSet intervalMappingSet;

    //###Display offset
    private int offset;

    /**
     * Constructorul default
     */
    public FinalResultItem() {

        //Obtin offsetul de afisare a pozitiilor ( 0 sau +1)
        offset = Integer.parseInt((String)ReadWriteConfiguration.get("NUMBERING_OFFSET"));
    }

     /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *      Painting methods
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Metoda ce se apeleaza implicit la desenarea unui panou
     * @param g         Contextul grafic
     * @param width     latimea
     * @param height    inaltimea
     * @param bgColor   Culoarea de fundal
     * @param fgColor   Culoarea textului
     */
    public void paint(Graphics g, int width ,int height, Color bgColor, Color fgColor ) {
        mainPaintMethod(g, width, height, bgColor, fgColor, true);
    }

    /**
     * Metoda ce deseneaza un context grafic al unui imageBuffer
     * @param g         Contextul grafic
     * @param width     latimea
     * @param height    inaltimea
     * @param bgColor   Culoarea de fundal
     * @param fgColor   Culoarea textului
     */
    public void paintImage(Graphics g, int width ,int height, Color bgColor, Color fgColor ) {
        Graphics2D g2d = (Graphics2D) g;
        
        //Setez parametrii de desenare
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int lineHeight = fontMetrics.getHeight();

        //Desenez textul de scor
        String textScore = " Score: ";
        String textValueScore = this.score+"";
            g2d.setColor(Color.BLACK);
            g2d.drawString(textScore, 0, lineHeight-2);
            Font oldFont = g2d.getFont();
            g2d.setFont(oldFont.deriveFont(Font.BOLD,oldFont.getSize()+1));
            g2d.drawString(textValueScore, fontMetrics.stringWidth(textScore), lineHeight-1);
            g2d.setFont(oldFont);

        //Desenez alinierea
        g.translate(0, lineHeight);
        mainPaintMethod(g, width, height-lineHeight, bgColor, fgColor, false);
        g.translate(0, -lineHeight);
    }

    /**
     * Metoda ce deseneaza acest Partial Result Item
     */
    public void mainPaintMethod(Graphics g, int width ,int height, Color bgColor, Color fgColor,
            boolean isBackgroundFilled ) {
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
        int nextLocalX;

        //Capetele intervalului
        String leftEnd;
        String rightEnd;

        //Denesare intervale
        Iterator<IntervalMappingItem> intervale;
        int indexInterval;
        IntervalMappingItem intervalMappingItem;
        int markerLeft,intervalLeft;
        int markerRight,intervalRight;
        int intervalWidth;
        int indexWidth = 0;
        int textMarkerWidth,textMarkerHeight;

        //Culoarea de desenare
        Color colorIntervalBorder;
        Color colorIntervalFill;
        Color colorInsertionPosition;

        //Variabile pentru sumar
        String prefixSeparator;
        String suffixSeparator;
        int prefixWidth;
        int suffixWidth;
        int linesUsed,currentLine;

        //Auxiliare pentru calcule
        String auxString;
        int auxInt,sumInt;
        int stringHalf;

        // ====================
        // Calculez parametrii
        // ====================

        //Dimensiunea unei linii de text
        fontMetrics = g2d.getFontMetrics();
        lineHeight = fontMetrics.getHeight();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //Capetele chenarului
        leftEnd = new String(" "+offset+" ");
        int auxRightEnd = offset + this.getLengthInitialQuery() -1;
        rightEnd = new String(" "+auxRightEnd+" ");
        leftTextLength = fontMetrics.stringWidth(leftEnd);
        rigthTextLength = fontMetrics.stringWidth(rightEnd);
        chenarWidth = width - DrawingConstants.MARGIN_LEFT -
                DrawingConstants.MARGIN_RIGHT - leftTextLength - rigthTextLength;

        //iteratorul de itervale
        intervale = intervalMappingSet.iterator();

        //Initializez variabilele pentru legenda
        prefixSeparator = " - ";
        suffixSeparator = "  ";
        prefixWidth = fontMetrics.stringWidth(prefixSeparator);
        suffixWidth = fontMetrics.stringWidth(suffixSeparator);

        // ====================
        // Incep sa desenez
        // ====================

        // ~~~~~~~~~~~~~~~~~Fundal~~~~~~~~~~~~~~~~~~~~~~~~

        // <editor-fold defaultstate="collapsed" desc="Desenez fundalul">
        //Fundalul pe care se deseneaza
        g2d.setStroke(new BasicStroke(DrawingConstants.LINEWIDTH));
        g2d.setColor(bgColor);
        if (isBackgroundFilled) g2d.fillRect(0, 0, width, height);

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

        // ~~~~~~~~~~~~~~~~~~Intervale~~~~~~~~~~~~~~~~~~~~~~~
        
        //Calculez pozitia de insertie
        FinalResultExporter.FilteredSetWrapper filteredStWrapper = FinalResultExporter.filterIntervalMappingSet(this.getIntervalMappingSet(), Integer.MAX_VALUE);
        IntervalMappingSet filteredIntervalMappingSet = filteredStWrapper.intervalSet;
        int filteredSetIndex = filteredStWrapper.interalIndex;
        int[] codifiedInsertionPosition = FinalResultExporter.getInsertionPosition(filteredIntervalMappingSet);

        //Iau fiecare interval si il desenez
        indexInterval = 1;
        while (intervale.hasNext()) {
            // <editor-fold defaultstate="collapsed" desc="Setez variabile">
            intervalMappingItem = intervale.next();
            //Capetele markerului
            markerLeft = (int) ((intervalMappingItem.pozitieQuery /
                    (double) this.lengthInitialQuery) * chenarWidth);
            intervalLeft = DrawingConstants.MARGIN_LEFT + markerLeft + leftTextLength;
            markerRight = (int) (((intervalMappingItem.pozitieQuery+ intervalMappingItem.lengthQuery)
                    / (double) this.getLengthInitialQuery()) * chenarWidth);
            intervalRight = DrawingConstants.MARGIN_LEFT + markerRight + leftTextLength;
            intervalWidth = intervalRight - intervalLeft;

            //Stabilesc culorile
            if (intervalMappingItem.isTransposon()) {
                colorIntervalBorder = DrawingConstants.COLOR_TRANSPOSON_BORDER;
                colorIntervalFill = DrawingConstants.COLOR_TRANSPOSON_FILL;
            } else {
                colorIntervalBorder = DrawingConstants.COLOR_GENOM_BORDER;
                colorIntervalFill = DrawingConstants.COLOR_GENOM_FILL;
            }
            colorInsertionPosition = DrawingConstants.COLOR_INSERTION_POSITION;
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
                g2d.setColor(colorIntervalBorder);
                g2d.drawLine(localX, localY, localX + localWidth, localY + localHeight);

                //Desenez varful sagetii
                auxInt = (lineHeight / 2) - 2 * DrawingConstants.LINEWIDTH;
                if (intervalMappingItem.isComplement()) {
                    localX = localX - DrawingConstants.LINEWIDTH;
                    g2d.drawLine(localX, localY, localX + auxInt, localY - auxInt);
                    g2d.drawLine(localX, localY, localX + auxInt, localY + auxInt);
                } else {
                    localX = localX + localWidth + DrawingConstants.LINEWIDTH;
                    g2d.drawLine(localX, localY, localX - auxInt, localY - auxInt);
                    g2d.drawLine(localX, localY, localX - auxInt, localY + auxInt);
                }
            }
            // </editor-fold>

            // ~~~~~~~~~~~~~~~~~~ Index Interval ~~~~~~~~~~~~
            //Scriu cu bold
            Font oldFont = g2d.getFont();
            g2d.setFont(oldFont.deriveFont(Font.BOLD,oldFont.getSize()+1));

            auxString = "["+indexInterval+"]"; indexInterval++;
            indexWidth = g.getFontMetrics().stringWidth(auxString);
            localX = intervalLeft + intervalWidth/2 - indexWidth/2;
            localY = DrawingConstants.MARGIN_TOP + 3 * lineHeight;
            g2d.setColor(colorIntervalBorder);
            g2d.drawString(auxString, localX, localY);

            g2d.setFont(oldFont);

            // ~~~~~~~~~~~~~~~~~~ Markeri text ~~~~~~~~~~~~~~~
            // <editor-fold defaultstate="collapsed" desc="Markeri text query">
            textMarkerWidth = DrawingConstants.TEXT_MARKER_WIDTH;
            textMarkerHeight = DrawingConstants.TEXT_MARKER_HEIGHT;

            //Markeri pentru text stanga
            localX = intervalLeft;
            localY = DrawingConstants.MARGIN_TOP + lineHeight;
            g2d.setColor(colorIntervalBorder);
            g2d.setStroke(new BasicStroke(DrawingConstants.LINEWIDTH - 1));
            g2d.drawLine(localX, localY, localX, localY - textMarkerHeight);

            localX = intervalLeft;
            localY = DrawingConstants.MARGIN_TOP + lineHeight - textMarkerHeight;
            g2d.setColor(colorIntervalBorder);
            g2d.drawLine(localX, localY, localX + textMarkerWidth, localY);

            //Markeri pentru text dreapta
            localX = intervalRight;
            localY = DrawingConstants.MARGIN_TOP + lineHeight;
            g2d.setColor(colorIntervalBorder);
            g2d.setStroke(new BasicStroke(DrawingConstants.LINEWIDTH - 1));
            g2d.drawLine(localX, localY, localX, localY - textMarkerHeight);

            localX = intervalRight;
            localY = DrawingConstants.MARGIN_TOP + lineHeight - textMarkerHeight;
            g2d.setColor(colorIntervalBorder);
            g2d.drawLine(localX, localY, localX - textMarkerWidth, localY);
            g2d.setStroke(new BasicStroke(DrawingConstants.LINEWIDTH));
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Markeri text genom">
            textMarkerWidth = DrawingConstants.TEXT_MARKER_WIDTH;
            textMarkerHeight = DrawingConstants.TEXT_MARKER_HEIGHT;

            //Markeri pentru text stanga
            localX = intervalLeft;
            localY = DrawingConstants.MARGIN_TOP + 2*lineHeight;
            g2d.setColor(colorIntervalBorder);
            g2d.setStroke(new BasicStroke(DrawingConstants.LINEWIDTH - 1));
            g2d.drawLine(localX, localY, localX, localY + textMarkerHeight);

            localX = intervalLeft;
            localY = DrawingConstants.MARGIN_TOP + 2*lineHeight + textMarkerHeight;
            g2d.setColor(colorIntervalBorder);
            g2d.drawLine(localX, localY, localX + textMarkerWidth, localY);

            //Markeri pentru text dreapta
            localX = intervalRight;
            localY = DrawingConstants.MARGIN_TOP + 2*lineHeight;
            g2d.setColor(colorIntervalBorder);
            g2d.setStroke(new BasicStroke(DrawingConstants.LINEWIDTH - 1));
            g2d.drawLine(localX, localY, localX, localY + textMarkerHeight);

            localX = intervalRight;
            localY = DrawingConstants.MARGIN_TOP + 2*lineHeight + textMarkerHeight;
            g2d.setColor(colorIntervalBorder);
            g2d.drawLine(localX, localY, localX - textMarkerWidth, localY);
            g2d.setStroke(new BasicStroke(DrawingConstants.LINEWIDTH));
            // </editor-fold>

            // ~~~~~~~~~~~~~~~~~~~Text~~~~~~~~~~~~~~~~~~~~~~

            // <editor-fold defaultstate="collapsed" desc="Text pozitie secventa">
            textMarkerWidth = DrawingConstants.TEXT_MARKER_WIDTH + DrawingConstants.LINEWIDTH;

            //Plasez textul de mapare secventa
            auxString = intervalMappingItem.getPozitieStartQuery() + "";
            auxInt = fontMetrics.stringWidth(auxString);
            sumInt = auxInt+2*textMarkerWidth;
                if (sumInt < intervalWidth) {
                localX = intervalLeft + textMarkerWidth;
                localY = DrawingConstants.MARGIN_TOP + lineHeight - DrawingConstants.ARC_HEIGHT;
                g2d.setColor(colorIntervalBorder);
                g2d.drawString(intervalMappingItem.getPozitieStartQuery() + "", localX, localY);
            }

            //Plasez textul de mapare secventa
            auxString = intervalMappingItem.getPozitieStopQuery() + "";
            sumInt = auxInt;
            auxInt = fontMetrics.stringWidth(auxString);
            sumInt += auxInt + 2*textMarkerWidth+ 4 * DrawingConstants.LINEWIDTH;
            if (sumInt < intervalWidth) {
                localX = intervalRight - auxInt - textMarkerWidth;
                localY = DrawingConstants.MARGIN_TOP + lineHeight - DrawingConstants.ARC_HEIGHT;
                g2d.setColor(colorIntervalBorder);
                g2d.drawString(auxString, localX, localY);
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Text pozitie genom">
            textMarkerWidth = DrawingConstants.TEXT_MARKER_WIDTH + DrawingConstants.LINEWIDTH;
            
            //Plasez textul de mapare genom
             auxString = intervalMappingItem.getPozitieStartGenom() + "";
            auxInt = fontMetrics.stringWidth(auxString);
            sumInt = auxInt+textMarkerWidth+indexWidth/2;
            if (sumInt < intervalWidth/2) {
                localX = intervalLeft + textMarkerWidth;
                localY = DrawingConstants.MARGIN_TOP + 3 * lineHeight;
                oldFont = g2d.getFont();
                if((codifiedInsertionPosition[3] != -1 && codifiedInsertionPosition[2] == 1 && indexInterval-2 == filteredSetIndex + codifiedInsertionPosition[1])) {
                    g2d.setColor(colorInsertionPosition);                    
                    g2d.setFont(oldFont.deriveFont(Font.BOLD, oldFont.getSize() + 1));
                }
                else
                    g2d.setColor(colorIntervalBorder);
                g2d.drawString(auxString, localX, localY);
                g2d.setFont(oldFont);
            }

            //Plasez textul de mapare genom
            auxString = intervalMappingItem.getPozitieStopGenom() + "";
            sumInt = auxInt;
            auxInt = fontMetrics.stringWidth(auxString);
            sumInt += auxInt + 2*textMarkerWidth+ 4 * DrawingConstants.LINEWIDTH + indexWidth;
            if (sumInt < intervalWidth) {
                localX = intervalRight - auxInt - textMarkerWidth;
                localY = DrawingConstants.MARGIN_TOP + 3 * lineHeight;
                oldFont = g2d.getFont();                
                if((codifiedInsertionPosition[3] != -1 && codifiedInsertionPosition[2] == 0 && indexInterval-2 == filteredSetIndex + codifiedInsertionPosition[1])) {
                    g2d.setColor(colorInsertionPosition);
                    g2d.setFont(oldFont.deriveFont(Font.BOLD, oldFont.getSize() + 1));
                }
                else
                    g2d.setColor(colorIntervalBorder);
                g2d.drawString(auxString, localX, localY);
                g2d.setFont(oldFont);
            }
            // </editor-fold>
        }

        // ~~~~~~~~~~~~~~~~~~ Legenda ~~~~~~~~~~~~~~~~~~~~~~~
        // <editor-fold defaultstate="collapsed" desc="Verificare dimensiune legenda">
        //Pentru a desena legenda, intai trebuie verificat daca textul incape
        sumInt = 0;
        intervale = intervalMappingSet.iterator();
        while (intervale.hasNext()) {
            sumInt += indexWidth;  //Numarul
            sumInt += prefixWidth; //Prefixul

            intervalMappingItem = intervale.next();
            auxString = intervalMappingItem.getFisierOrigine();
            auxInt = fontMetrics.stringWidth(auxString);
            sumInt += auxInt;

            sumInt += suffixWidth;
        }

        //Fac verificarea daca incape pe o linie
        if (sumInt + DrawingConstants.MARGIN_LEFT + DrawingConstants.MARGIN_RIGHT < width) {
            linesUsed = 1;

        } else {
            linesUsed = 2;

        }
        currentLine = 1;
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Desenez legenda">
        //Desenez un chenar
        localX = 0;
        localY = height - DrawingConstants.MARGIN_BOTTOM - linesUsed * lineHeight;
        localWidth = width;
        localHeight = DrawingConstants.MARGIN_BOTTOM + linesUsed * lineHeight;
        g2d.setColor(bgColor);
        g2d.fillRect(localX, localY, localWidth, localHeight);
        g2d.setColor(DrawingConstants.COLOR_CHENAR_BORDER);
        g2d.drawRect(localX, localY, localWidth, localHeight);

        //Desenez legenda pentru fiecare interval
        indexInterval = 1;
        nextLocalX = DrawingConstants.MARGIN_LEFT;
        localY = height - DrawingConstants.MARGIN_BOTTOM - (linesUsed - 1) * lineHeight;
        intervale = intervalMappingSet.iterator();
        while (intervale.hasNext()) {
            intervalMappingItem = intervale.next();

            //Stabilesc culorile
            if (intervalMappingItem.isTransposon()) {
                colorIntervalBorder = DrawingConstants.COLOR_TRANSPOSON_BORDER;
                colorIntervalFill = DrawingConstants.COLOR_TRANSPOSON_FILL;
            } else {
                colorIntervalBorder = DrawingConstants.COLOR_GENOM_BORDER;
                colorIntervalFill = DrawingConstants.COLOR_GENOM_FILL;
            }

            //Desenez indexMarkerul
            //Scriu cu bold
            Font oldFont = g2d.getFont();
            g2d.setFont(oldFont.deriveFont(Font.BOLD, oldFont.getSize() + 1));

            auxString = "[" + indexInterval + "]";
            indexInterval++;
            auxInt = g.getFontMetrics().stringWidth(auxString);
            if (nextLocalX + auxInt > width - DrawingConstants.MARGIN_RIGHT) {
                localX = DrawingConstants.MARGIN_LEFT;
                localY += lineHeight;
            } else {
                localX = nextLocalX;
            }
            nextLocalX = localX + auxInt;
            g2d.setColor(colorIntervalBorder);
            g2d.drawString(auxString, localX, localY);
            g2d.setFont(oldFont);

            //Desenez prefixul
            auxInt = prefixWidth;
            localX = nextLocalX;
            nextLocalX = localX + auxInt;
            g2d.setColor(colorIntervalBorder);
            g2d.drawString(prefixSeparator, localX, localY);

            //Desenez fisierul de provenienta
            auxString = intervalMappingItem.getFisierOrigine();
            auxInt = fontMetrics.stringWidth(auxString);
            if (nextLocalX + auxInt > width - DrawingConstants.MARGIN_RIGHT) {
                localX = DrawingConstants.MARGIN_LEFT;
                localY += lineHeight;
            } else {
                localX = nextLocalX;
            }
            nextLocalX = localX + auxInt;
            g2d.setColor(colorIntervalBorder);
            g2d.drawString(auxString, localX, localY);

            //Desenez suffixul
            auxInt = suffixWidth;
            if (nextLocalX + auxInt > width - DrawingConstants.MARGIN_RIGHT) {
                localX = DrawingConstants.MARGIN_LEFT;
                localY += lineHeight;
            } else {
                localX = nextLocalX;
            }
            nextLocalX = localX + auxInt;
            g2d.setColor(colorIntervalBorder);
            g2d.drawString(suffixSeparator, localX, localY);

        }
        // </editor-fold>
    }

     /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *      Sizing methods
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Get recomended height
     */
    public int getRecommendedHeight(JComponent component) {
        Font font = UIManager.getDefaults().getFont("Table.font");
        int lineHeight = component.getFontMetrics(font).getHeight();
        int recommendedSize = DrawingConstants.MARGIN_TOP +
                DrawingConstants.MARGIN_BOTTOM + 5*lineHeight + 5*DrawingConstants.LINEWIDTH;
        return recommendedSize;
    }

    /**
     * Get recomended qidth
     */
    public int getRecommendedWidth(JComponent component) {
        return DrawingConstants.RECOMMENDED_WIDTH;
    }

    /**
     * Get recomended height
     */
    public int getImageHeight(JComponent component) {
        Font font = UIManager.getDefaults().getFont("Table.font");
        int lineHeight = component.getFontMetrics(font).getHeight();
        int recommendedSize = getRecommendedHeight(component) + lineHeight;
        return recommendedSize;
    }

    /**
     * Get recomended qidth
     */
    public int getImageWidth(JComponent component) {
        return DrawingConstants.IMAGE_WIDTH;
    }

     /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *      Getter and setter
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    public IntervalMappingSet getIntervalMappingSet() {
        return intervalMappingSet;
    }

    public void setIntervalMappingSet(IntervalMappingSet intervalMappingSet) {
        this.intervalMappingSet = intervalMappingSet;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public int getLengthInitialQuery() {
        return lengthInitialQuery;
    }

    public void setLengthInitialQuery(int lengthInitialQuery) {
        this.lengthInitialQuery = lengthInitialQuery;
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

    public int getOutTotalLength() {
        return outTotalLength;
    }

    public void setOutTotalLength(int outTotalLength) {
        this.outTotalLength = outTotalLength;
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


