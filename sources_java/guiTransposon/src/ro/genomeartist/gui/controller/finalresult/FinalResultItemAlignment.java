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
import ro.genomeartist.gui.custompaint.ImageManipulation;
import ro.genomeartist.gui.custompaint.ImageVector;
import ro.genomeartist.gui.interfaces.ICanPaint;
import ro.genomeartist.gui.utils.DNAUtils;
import ro.genomeartist.gui.utils.MyUtils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import javax.swing.JComponent;
import javax.swing.UIManager;
import ro.genomeartist.gui.controller.exporters.FinalResultExporter.FilteredSetWrapper;
import static ro.genomeartist.gui.controller.exporters.FinalResultExporter.filterIntervalMappingSet;
import static ro.genomeartist.gui.controller.exporters.FinalResultExporter.getInsertionPosition;
import ro.genomeartist.gui.controller.settings.GeneralSettings;

/**
 *
 * @author iulian
 */
public class FinalResultItemAlignment implements ICanPaint {
    
     /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *      Atributele clasei
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    private FinalResultItem finalResultItem;
    private Font monospaceFont_gui;
    private Font monospaceFont_report;
    
    //generalSettings data
    GeneralSettings generalSettings;
    
    //###Display offset
    private int offset;

    //Constante pentru export in imagine
    private static final int CHARS_LEFT = 8;
    private static final int CHARS_RIGHT = 8;
    private static final int CHARS_INDEX = 4;

    /**
     * Contruiesc un obiect care imbraca un partialResult cu scopul
     * de a desena alinierile
     * @param finalResultItem
     */
    public FinalResultItemAlignment(FinalResultItem finalResultItem) {
        this.finalResultItem = finalResultItem;

        generalSettings = new GeneralSettings();
        generalSettings.loadFromFile();
        
        //Creez un font monospaced
        Font font = UIManager.getDefaults().getFont("Table.font");
        this.monospaceFont_gui = new Font(Font.MONOSPACED, Font.PLAIN, font.getSize());
        this.monospaceFont_report = new Font(Font.MONOSPACED, Font.PLAIN,
                DrawingConstants.REPORT_MONOSPACE_FONT_SIZE);

        //Obtin offsetul de afisare a pozitiilor ( 0 sau +1)
        offset = Integer.parseInt((String)ReadWriteConfiguration.get("NUMBERING_OFFSET"));
    }

     /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *      Deseneare pe ecran
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

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

        //Spatiul ocupat de text
        int leftTextLength; //Dimensiune scrisului de inceput
        int rigthTextLength; //Dimensiune scrisului de sfarsit
        int centerTextLength;   //Lungimea secventei aliniate
        int centerTextOffset;   //Offsetul textului din centru fata de margini

        //Denesare intervale
        Iterator<IntervalMappingItem> intervale;
        int indexInterval;
        String textIndexInterval;
        IntervalMappingItem intervalMappingItem;
        int markerLeft,intervalLeft;
        int markerDeviationLeft,markerDeviationRight;
        int markerRight,intervalRight;
        int intervalWidth;
        int indexWidth = 0;
        int textMarkerWidth,textMarkerHeight;

        //Culoarea de desenare
        Color colorIntervalBorder;
        Color colorIntervalFill;
        Color colorInsertionPosition;

        //Auxiliare pentru calcule
        String auxString;
        int auxInt,sumInt;
        int stringHalf;

        // ====================
        // Calculez parametrii
        // ====================

        //Setez fontul
        g2d.setFont(monospaceFont_gui);
        fontMetrics = g2d.getFontMetrics();
        lineHeight = fontMetrics.getHeight();

        //Calculez capetele de inceput si sfarsit
        queryLeftEnd = " "+ offset +" ";
        int rightEnd = finalResultItem.getLengthInitialQuery() - 1 + offset;
        queryRightEnd = " "+rightEnd +" ";
        leftTextLength = fontMetrics.stringWidth(queryLeftEnd);
        rigthTextLength = fontMetrics.stringWidth(queryRightEnd);

        //Calculez marimea textului in centru
        centerTextOffset = DrawingConstants.LINEWIDTH;
        centerTextLength = fontMetrics.stringWidth(finalResultItem.getOut1());

        //Setarile pentru intervale
        intervale = finalResultItem.getIntervalMappingSet().iterator();

        //Stabilesc culorile
        colorIntervalBorder = DrawingConstants.COLOR_CHENAR_BORDER;
        colorIntervalFill = DrawingConstants.COLOR_CHENAR_FILL;
        colorInsertionPosition = DrawingConstants.COLOR_INSERTION_POSITION;

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
        localY = DrawingConstants.MARGIN_TOP + 3*lineHeight;
        g2d.setColor(colorIntervalBorder);
        g2d.drawString(queryLeftEnd, localX, localY);

        //Capat query dreapta
        localX = DrawingConstants.MARGIN_LEFT + leftTextLength + centerTextLength;
        localY = DrawingConstants.MARGIN_TOP + 3*lineHeight;
        g2d.setColor(colorIntervalBorder);
        g2d.drawString(queryRightEnd, localX, localY);
        // </editor-fold>

        // ~~~~~~~~~~~~~~~~~~Intervale~~~~~~~~~~~~~~~~~~~~~~~

        //Calculez pozitia de insertie
        FilteredSetWrapper filteredSetWrapper = filterIntervalMappingSet(finalResultItem.getIntervalMappingSet(), 5);
        IntervalMappingSet filteredIntervalMappingSet = filteredSetWrapper.intervalSet;
        int filteredIntervalIndex = filteredSetWrapper.interalIndex;
        int[] codifiedInsertionPosition = getInsertionPosition(filteredIntervalMappingSet);
        
        //Iau fiecare interval si il desenez
        indexInterval = 1;
        while (intervale.hasNext()) {
            // <editor-fold defaultstate="collapsed" desc="Setez variabile">
            intervalMappingItem = intervale.next();
            
            //Deviatia markerului datorata alinierii textului
            if (intervalMappingItem.getPozitieStartQuery() != 0)
                markerDeviationLeft = centerTextOffset;
            else markerDeviationLeft = 0;
            if ((intervalMappingItem.getPozitieStartQuery() + intervalMappingItem.getLengthQuery())
                    != finalResultItem.getLengthInitialQuery())
                markerDeviationRight = centerTextOffset;
            else markerDeviationRight = 2*centerTextOffset;

            //Capetele markerului
            markerLeft = (int) ((intervalMappingItem.getOutStringOffset() / (double) finalResultItem.getOutTotalLength()) * centerTextLength);
            intervalLeft = DrawingConstants.MARGIN_LEFT + markerLeft + leftTextLength + markerDeviationLeft;
            markerRight = (int) (((intervalMappingItem.getOutStringOffset() + intervalMappingItem.getOutStringLength())
                    / (double) finalResultItem.getOutTotalLength()) * centerTextLength);
            intervalRight = DrawingConstants.MARGIN_LEFT + markerRight + leftTextLength + markerDeviationRight;
            intervalWidth = intervalRight - intervalLeft;

            //Se stabileste textul pentru indexInterval
            textIndexInterval = "["+indexInterval+"] "+intervalMappingItem.getFisierOrigine();
            indexWidth = fontMetrics.stringWidth(textIndexInterval);
            if (indexWidth > intervalWidth) {
                textIndexInterval = "["+indexInterval+"]";
                indexWidth = fontMetrics.stringWidth(textIndexInterval);
            }
            indexInterval++;

            //Stabilesc culorile
            if (intervalMappingItem.isTransposon()) {
                colorIntervalBorder = DrawingConstants.COLOR_TRANSPOSON_BORDER;
                colorIntervalFill = DrawingConstants.COLOR_TRANSPOSON_FILL;
            } else {
                colorIntervalBorder = DrawingConstants.COLOR_GENOM_BORDER;
                colorIntervalFill = DrawingConstants.COLOR_GENOM_FILL;
            }
            // </editor-fold>

            // ~~~~~~~~~~~~~~~~~~Interval~~~~~~~~~~~~~~~~~~~~~~~

            // <editor-fold defaultstate="collapsed" desc="Desenez intervalul">
            //Marchez pozitia intervalului
            g2d.setStroke(new BasicStroke(DrawingConstants.LINEWIDTH-1));
            localX = intervalLeft;
            localY = DrawingConstants.MARGIN_TOP + lineHeight;
            localWidth = intervalWidth;
            localHeight = 3*lineHeight;
            g2d.setColor(colorIntervalFill);
            g2d.fillRoundRect(localX, localY, localWidth, localHeight,
                    DrawingConstants.ARC_WIDTH, DrawingConstants.ARC_HEIGHT);

            //Chenarul intervalului
            g2d.setColor(colorIntervalBorder);
            g2d.drawRoundRect(localX, localY, localWidth, localHeight,
                    DrawingConstants.ARC_WIDTH, DrawingConstants.ARC_HEIGHT);
            g2d.setStroke(new BasicStroke(DrawingConstants.LINEWIDTH));

            // </editor-fold>

            // ~~~~~~~~~~~~~~~~~~~Sageata~~~~~~~~~~~~~~~~~~~~~~

            // <editor-fold defaultstate="collapsed" desc="Desenez sageata de sens">
            //Marchez sensul intervalului
            if (intervalWidth - 4 * DrawingConstants.LINEWIDTH > DrawingConstants.ARROW_WIDTH) {
                auxInt = (intervalWidth - DrawingConstants.ARROW_WIDTH) / 2;
                localX = intervalLeft + auxInt;
                localY = DrawingConstants.MARGIN_TOP + 2*lineHeight + lineHeight / 2;
                localWidth = DrawingConstants.ARROW_WIDTH;
                localHeight = 0;

                //antialias on
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
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

                //reset antialias
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_DEFAULT);
            }
            // </editor-fold>

            // ~~~~~~~~~~~~~~~~~~ Index Interval ~~~~~~~~~~~~
            // <editor-fold defaultstate="collapsed" desc="Index interval curent">
            //Scriu cu bold
            Font oldFont = g2d.getFont();
            g2d.setFont(oldFont.deriveFont(Font.BOLD, oldFont.getSize() + 1));

            localX = intervalLeft + intervalWidth / 2 - indexWidth / 2;
            localY = DrawingConstants.MARGIN_TOP + 5 * lineHeight;
            g2d.setColor(colorIntervalBorder);
            g2d.drawString(textIndexInterval, localX, localY);

            g2d.setFont(oldFont);
            // </editor-fold>

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
            localY = DrawingConstants.MARGIN_TOP + 4*lineHeight;
            g2d.setColor(colorIntervalBorder);
            g2d.setStroke(new BasicStroke(DrawingConstants.LINEWIDTH - 1));
            g2d.drawLine(localX, localY, localX, localY + textMarkerHeight);

            localX = intervalLeft;
            localY = DrawingConstants.MARGIN_TOP + 4*lineHeight + textMarkerHeight;
            g2d.setColor(colorIntervalBorder);
            g2d.drawLine(localX, localY, localX + textMarkerWidth, localY);

            //Markeri pentru text dreapta
            localX = intervalRight;
            localY = DrawingConstants.MARGIN_TOP + 4*lineHeight;
            g2d.setColor(colorIntervalBorder);
            g2d.setStroke(new BasicStroke(DrawingConstants.LINEWIDTH - 1));
            g2d.drawLine(localX, localY, localX, localY + textMarkerHeight);

            localX = intervalRight;
            localY = DrawingConstants.MARGIN_TOP + 4*lineHeight + textMarkerHeight;
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
                localY = DrawingConstants.MARGIN_TOP + 5 * lineHeight;  
                oldFont = g2d.getFont();
                if((codifiedInsertionPosition[3] != -1 && codifiedInsertionPosition[2] == 1 && indexInterval-2 == filteredIntervalIndex + codifiedInsertionPosition[1])) {
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
                localY = DrawingConstants.MARGIN_TOP + 5 * lineHeight;
                oldFont = g2d.getFont();                
                if((codifiedInsertionPosition[3] != -1 && codifiedInsertionPosition[2] == 0 && indexInterval-2 == codifiedInsertionPosition[1])) {
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

        // <editor-fold defaultstate="collapsed" desc="Desenez alinierea">
        //Secventa out1
        localX = DrawingConstants.MARGIN_LEFT + leftTextLength + centerTextOffset;
        localY = DrawingConstants.MARGIN_TOP + 2*lineHeight - centerTextOffset;
        g2d.setColor(fgColor);
        g2d.drawString(finalResultItem.getOut1(), localX, localY);

        //Secventa relation
        localX = DrawingConstants.MARGIN_LEFT + leftTextLength + centerTextOffset;
        localY = DrawingConstants.MARGIN_TOP + 3* lineHeight - centerTextOffset;
        g2d.setColor(fgColor);
        g2d.drawString(finalResultItem.getRelation(), localX, localY);

        //Secventa out2
        localX = DrawingConstants.MARGIN_LEFT + leftTextLength + centerTextOffset;
        localY = DrawingConstants.MARGIN_TOP + 4*lineHeight - centerTextOffset;
        g2d.setColor(fgColor);
        g2d.drawString(finalResultItem.getOut2(), localX, localY);
        // </editor-fold>
    }

    /**
     * Get recomended height
     */
    public int getRecommendedHeight(JComponent component) {
        int lineHeight = component.getFontMetrics(monospaceFont_gui).getHeight();
        int recommendedSize = DrawingConstants.MARGIN_TOP +
                DrawingConstants.MARGIN_BOTTOM + 7*lineHeight;
        return recommendedSize;
    }

    /**
     * Get recomended qidth
     */
    public int getRecommendedWidth(JComponent component) {
        FontMetrics fontMetrics = component.getFontMetrics(monospaceFont_gui);
        int recommendedSize = fontMetrics.stringWidth(finalResultItem.getOut1());
        recommendedSize += DrawingConstants.MARGIN_LEFT +
                DrawingConstants.MARGIN_RIGHT +
                2*fontMetrics.stringWidth("AAAAAAAAAAAA");
        return recommendedSize;
    }

     /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *      Desenare in imagine
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Obtin imaginea cu aceasta aliniere
     * @return
     */
    public BufferedImage exportAsImage(int width, Color bgColor, Color fgColor) {
        ImageVector imageVector = new ImageVector();
        BufferedImage resultImage;

        //Parametrii
        int startQuery = Integer.parseInt((String)ReadWriteConfiguration
                .get("NUMBERING_OFFSET"));
        int lengthQuery;
        int offsetOut,lengthOut;
        int cols = getPartialImageCols(width);
        String referenceString = finalResultItem.getOut1();

        //Parametrii locali
        int localWidth = width;
        int localHeight = getPartialImageHeight();

        //Impart aliniaerea in bucati de dimensiune fixa
        int pieces = finalResultItem.getOutTotalLength()/cols;
        for (int i = 0; i < pieces; i++) {
            //Calculez parametrii bucatii
            //i*cols -> (i+1)*cols-1;
            offsetOut = i*cols;
            lengthOut = cols;
            lengthQuery = countNucleotides(referenceString, offsetOut, lengthOut);

            //Creez o imagine in care voi desena
            BufferedImage imageItem = new BufferedImage(localWidth, localHeight,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics g = imageItem.getGraphics();
            this.paintImage(g, localWidth, localHeight, bgColor, fgColor, 
                    startQuery, lengthQuery, offsetOut, lengthOut);
            imageVector.add(imageItem);

            //Incrementez pozitia de inceput
            startQuery += lengthQuery;
        }

        //Calculez ultima bucata ramasa
        int leftover = finalResultItem.getOutTotalLength() % cols;
        if (leftover != 0) {
            //pieces*cols -> totalLength-1
            offsetOut = pieces*cols;
            lengthOut = leftover;
            lengthQuery = countNucleotides(referenceString, offsetOut, leftover);

            //Creez o imagine in care voi desena
            BufferedImage imageItem = new BufferedImage(localWidth, localHeight,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics g = imageItem.getGraphics();
            this.paintImage(g, localWidth, localHeight, bgColor, fgColor,
                    startQuery, lengthQuery, offsetOut, lengthOut);
            imageVector.add(imageItem);
        }

        //Din vectorul de imagini compun o imagine mare
        resultImage = ImageManipulation.joinImages(imageVector);
        return resultImage;
    }

    /**
     * Metoda ce deseneaza alinierea unui Partial Result Item
     */
    public void paintImage(Graphics g, int width, int height, Color bgColor, Color fgColor,
            int startQuery,int lengthQuery, int startOut, int lengthOut) {
        Graphics2D g2d = (Graphics2D) g;

        // =========================
        // Parametrii de desenare
        // =========================
        FontMetrics fontMetrics; //Dimensiunile fontului
        int lineHeight; //Dimensiunea unei linii de text
        int charWidth;

        //pozitia curenta la care se deseneaza
        int localX,localY;
        int localWidth,localHeight;

        //Text pentru capetele intervalelor
        String queryLeftEnd,queryRightEnd;

        //Spatiul ocupat de text
        int leftTextLength; //Dimensiune scrisului de inceput
        int rigthTextLength; //Dimensiune scrisului de sfarsit
        int centerTextLength;   //Lungimea secventei aliniate
        int centerTextOffset;   //Offsetul textului din centru fata de margini

        //Textul care se va desena
        String stringOut1;
        String stringRelation;
        String stringOut2;

        //Denesare intervale
        Iterator<IntervalMappingItem> intervale;
        int indexInterval;
        String textIndexInterval;
        IntervalMappingItem intervalMappingItem;
        int markerLeft,intervalLeft;
        int markerDeviationLeft,markerDeviationRight;
        int markerRight,intervalRight;
        int intervalWidth;
        int indexWidth = 0;
        int textMarkerWidth,textMarkerHeight;
        boolean drawLeftMarker;
        boolean drawRightMarker;
        boolean drawIndexInterval;
        boolean drawInterval;

        //Culoarea de desenare
        Color colorIntervalBorder;
        Color colorIntervalFill;

        //Auxiliare pentru calcule
        String auxString;
        int auxInt,sumInt;
        int stringHalf;

        // ====================
        // Calculez parametrii
        // ====================

        //Setez fontul
        g2d.setFont(monospaceFont_report);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        fontMetrics = g2d.getFontMetrics();
        lineHeight = fontMetrics.getHeight();
        charWidth = fontMetrics.charWidth('G');

        //Calculez capetele de inceput si sfarsit
        queryLeftEnd = "  "+ startQuery +"  ";
        int rightEnd = startQuery + lengthQuery - 1;
        queryRightEnd = "  "+rightEnd +"  ";
        leftTextLength = CHARS_LEFT*charWidth;
        rigthTextLength = CHARS_RIGHT*charWidth;

        //Obtin stringurile de desenare
        stringOut1 = finalResultItem.getOut1().substring(startOut, startOut+lengthOut);
        stringRelation = finalResultItem.getRelation().substring(startOut, startOut+lengthOut);
        stringOut2 = finalResultItem.getOut2().substring(startOut, startOut+lengthOut);

        //Calculez marimea textului in centru
        centerTextOffset = DrawingConstants.LINEWIDTH;
        centerTextLength = fontMetrics.stringWidth(stringOut1);

        //Setarile pentru intervale
        intervale = finalResultItem.getIntervalMappingSet().iterator();

        //Stabilesc culorile
        colorIntervalBorder = DrawingConstants.COLOR_CHENAR_BORDER;
        colorIntervalFill = DrawingConstants.COLOR_CHENAR_FILL;

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
        localX = 0;
        localY = DrawingConstants.MARGIN_TOP + 3*lineHeight;
        g2d.setColor(colorIntervalBorder);
        g2d.drawString(queryLeftEnd, localX, localY);

        //Capat query dreapta
        localX = leftTextLength + centerTextLength;
        localY = DrawingConstants.MARGIN_TOP + 3*lineHeight;
        g2d.setColor(colorIntervalBorder);
        g2d.drawString(queryRightEnd, localX, localY);
        // </editor-fold>

        // ~~~~~~~~~~~~~~~~~~Intervale~~~~~~~~~~~~~~~~~~~~~~~

        //Iau fiecare interval si il desenez
        indexInterval = 1;
        while (intervale.hasNext()) {
            // <editor-fold defaultstate="collapsed" desc="Setez variabile">
            intervalMappingItem = intervale.next();

            //Deviatia markerului datorata alinierii textului
            if (intervalMappingItem.getPozitieStartQuery() != startQuery)
                markerDeviationLeft = centerTextOffset;
            else markerDeviationLeft = 0;
            if ((intervalMappingItem.getPozitieStartQuery() + intervalMappingItem.getLengthQuery())
                    != (startQuery+lengthQuery))
                markerDeviationRight = centerTextOffset;
            else markerDeviationRight = 2*centerTextOffset;

            //Capetele markerului
                //Left
            auxInt = intervalMappingItem.getOutStringOffset() - startOut;
            markerLeft = (int) ((auxInt/ (double) lengthOut) * centerTextLength);
                if ((markerLeft >= 0) && (markerLeft < centerTextLength)) {
                    drawLeftMarker = true;
                } else {
                    //Determin motivul pentru care nu e bun
                    drawLeftMarker = false;
                    if (markerLeft < 0)
                        markerLeft = 0;
                    if (markerLeft >= centerTextLength)
                        markerLeft = centerTextLength;
                }
            intervalLeft = markerLeft + leftTextLength + markerDeviationLeft;
                //Right
            auxInt = intervalMappingItem.getOutStringOffset() - startOut + intervalMappingItem.getOutStringLength();
            markerRight = (int) ((auxInt / (double) lengthOut) * centerTextLength);
                if ((markerRight > 0) && (markerRight <= centerTextLength)) {
                    drawRightMarker = true;
                } else {
                    //Stabilesc motivul pentru care nu s-a putut desena
                    drawRightMarker = false;
                    if (markerRight <= 0)
                        markerRight = 0;
                    if (markerRight > centerTextLength)
                        markerRight = centerTextLength;
                }
            intervalRight = markerRight + leftTextLength + markerDeviationRight;
            intervalWidth = intervalRight - intervalLeft;

            //Se calculeaza daca textul poate fi afisat
            if (markerLeft == markerRight) drawInterval = false;
            else drawInterval = true;
            if ((markerRight-markerLeft) <= (CHARS_INDEX*charWidth))
                    drawIndexInterval = false;
            else drawIndexInterval = true;

            //Se stabileste textul pentru indexInterval
            textIndexInterval = "["+indexInterval+"] "+intervalMappingItem.getFisierOrigine();
            indexWidth = fontMetrics.stringWidth(textIndexInterval);
            if (indexWidth > intervalWidth) {
                textIndexInterval = "["+indexInterval+"]";
                indexWidth = fontMetrics.stringWidth(textIndexInterval);
            }
            indexInterval++;

            //Stabilesc culorile
            if (intervalMappingItem.isTransposon()) {
                colorIntervalBorder = DrawingConstants.COLOR_TRANSPOSON_BORDER;
                colorIntervalFill = DrawingConstants.COLOR_TRANSPOSON_FILL;
            } else {
                colorIntervalBorder = DrawingConstants.COLOR_GENOM_BORDER;
                colorIntervalFill = DrawingConstants.COLOR_GENOM_FILL;
            }
            // </editor-fold>

            // ~~~~~~~~~~~~~~~~~~Interval~~~~~~~~~~~~~~~~~~~~~~~

            // <editor-fold defaultstate="collapsed" desc="Desenez intervalul">
            //Marchez pozitia intervalului
            if (drawInterval) {
                g2d.setStroke(new BasicStroke(DrawingConstants.LINEWIDTH-1));
                localX = intervalLeft;
                localY = DrawingConstants.MARGIN_TOP + lineHeight;
                localWidth = intervalWidth;
                localHeight = 3*lineHeight;
                g2d.setColor(colorIntervalFill);
                g2d.fillRoundRect(localX, localY, localWidth, localHeight,
                        DrawingConstants.ARC_WIDTH, DrawingConstants.ARC_HEIGHT);

                //Chenarul intervalului
                g2d.setColor(colorIntervalBorder);
                g2d.drawRoundRect(localX, localY, localWidth, localHeight,
                        DrawingConstants.ARC_WIDTH, DrawingConstants.ARC_HEIGHT);
                g2d.setStroke(new BasicStroke(DrawingConstants.LINEWIDTH));
            }
            // </editor-fold>

            // ~~~~~~~~~~~~~~~~~~~Sageata~~~~~~~~~~~~~~~~~~~~~~

            // <editor-fold defaultstate="collapsed" desc="Desenez sageata de sens">
            //Marchez sensul intervalului
            if (intervalWidth - 4 * DrawingConstants.LINEWIDTH > DrawingConstants.ARROW_WIDTH) {
                auxInt = (intervalWidth - DrawingConstants.ARROW_WIDTH) / 2;
                localX = intervalLeft + auxInt;
                localY = DrawingConstants.MARGIN_TOP + 2*lineHeight + lineHeight / 2;
                localWidth = DrawingConstants.ARROW_WIDTH;
                localHeight = 0;

                //antialias on
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
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

                //reset antialias
                //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                //        RenderingHints.VALUE_ANTIALIAS_DEFAULT);
            }
            // </editor-fold>

            // ~~~~~~~~~~~~~~~~~~ Index Interval ~~~~~~~~~~~~
            // <editor-fold defaultstate="collapsed" desc="Index interval curent">
            //Scriu cu bold
            if (drawIndexInterval) {
                Font oldFont = g2d.getFont();
                g2d.setFont(oldFont.deriveFont(Font.BOLD, oldFont.getSize() + 1));

                localX = intervalLeft + intervalWidth / 2 - indexWidth / 2;
                localY = DrawingConstants.MARGIN_TOP + 5 * lineHeight;
                g2d.setColor(colorIntervalBorder);
                g2d.drawString(textIndexInterval, localX, localY);

                g2d.setFont(oldFont);
            }
            // </editor-fold>

            // ~~~~~~~~~~~~~~~~~~ Markeri text ~~~~~~~~~~~~~~~
            // <editor-fold defaultstate="collapsed" desc="Markeri text query">
            textMarkerWidth = DrawingConstants.TEXT_MARKER_WIDTH;
            textMarkerHeight = DrawingConstants.TEXT_MARKER_HEIGHT;

            //Markeri pentru text stanga
            if (drawLeftMarker) {
                localX = intervalLeft;
                localY = DrawingConstants.MARGIN_TOP + lineHeight;
                g2d.setColor(colorIntervalBorder);
                g2d.setStroke(new BasicStroke(DrawingConstants.LINEWIDTH - 1));
                g2d.drawLine(localX, localY, localX, localY - textMarkerHeight);

                localX = intervalLeft;
                localY = DrawingConstants.MARGIN_TOP + lineHeight - textMarkerHeight;
                g2d.setColor(colorIntervalBorder);
                g2d.drawLine(localX, localY, localX + textMarkerWidth, localY);
            }

            //Markeri pentru text dreapta
            if (drawRightMarker) {
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
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Markeri text genom">
            textMarkerWidth = DrawingConstants.TEXT_MARKER_WIDTH;
            textMarkerHeight = DrawingConstants.TEXT_MARKER_HEIGHT;

            //Markeri pentru text stanga
            if (drawLeftMarker) {
                localX = intervalLeft;
                localY = DrawingConstants.MARGIN_TOP + 4*lineHeight;
                g2d.setColor(colorIntervalBorder);
                g2d.setStroke(new BasicStroke(DrawingConstants.LINEWIDTH - 1));
                g2d.drawLine(localX, localY, localX, localY + textMarkerHeight);

                localX = intervalLeft;
                localY = DrawingConstants.MARGIN_TOP + 4*lineHeight + textMarkerHeight;
                g2d.setColor(colorIntervalBorder);
                g2d.drawLine(localX, localY, localX + textMarkerWidth, localY);
            }

            //Markeri pentru text dreapta
            if (drawRightMarker) {
                localX = intervalRight;
                localY = DrawingConstants.MARGIN_TOP + 4*lineHeight;
                g2d.setColor(colorIntervalBorder);
                g2d.setStroke(new BasicStroke(DrawingConstants.LINEWIDTH - 1));
                g2d.drawLine(localX, localY, localX, localY + textMarkerHeight);

                localX = intervalRight;
                localY = DrawingConstants.MARGIN_TOP + 4*lineHeight + textMarkerHeight;
                g2d.setColor(colorIntervalBorder);
                g2d.drawLine(localX, localY, localX - textMarkerWidth, localY);
                g2d.setStroke(new BasicStroke(DrawingConstants.LINEWIDTH));
            }
            // </editor-fold>

            // ~~~~~~~~~~~~~~~~~~~Text~~~~~~~~~~~~~~~~~~~~~~

            // <editor-fold defaultstate="collapsed" desc="Text pozitie secventa">
            textMarkerWidth = DrawingConstants.TEXT_MARKER_WIDTH + DrawingConstants.LINEWIDTH;

            //Plasez textul de mapare secventa
            if (drawLeftMarker) {
                auxString = intervalMappingItem.getPozitieStartQuery() + "";
                auxInt = fontMetrics.stringWidth(auxString);
                sumInt = auxInt+2*textMarkerWidth;
                    if (sumInt < intervalWidth) {
                    localX = intervalLeft + textMarkerWidth;
                    localY = DrawingConstants.MARGIN_TOP + lineHeight - DrawingConstants.ARC_HEIGHT;
                    g2d.setColor(colorIntervalBorder);
                    g2d.drawString(intervalMappingItem.getPozitieStartQuery() + "", localX, localY);
                }
            }

            //Plasez textul de mapare secventa
            if (drawRightMarker) {
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
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Text pozitie genom">
            textMarkerWidth = DrawingConstants.TEXT_MARKER_WIDTH + DrawingConstants.LINEWIDTH;

            //Plasez textul de mapare genom
            if (drawLeftMarker) {
                auxString = intervalMappingItem.getPozitieStartGenom() + "";
                auxInt = fontMetrics.stringWidth(auxString);
                sumInt = auxInt+textMarkerWidth+indexWidth/2;
                if (sumInt < intervalWidth/2) {
                    localX = intervalLeft + textMarkerWidth;
                    localY = DrawingConstants.MARGIN_TOP + 5 * lineHeight;
                    g2d.setColor(colorIntervalBorder);
                    g2d.drawString(auxString, localX, localY);
                }
            }

            //Plasez textul de mapare genom
            if (drawRightMarker) {
                auxString = intervalMappingItem.getPozitieStopGenom() + "";
                sumInt = auxInt;
                auxInt = fontMetrics.stringWidth(auxString);
                sumInt += auxInt + 2*textMarkerWidth+ 4 * DrawingConstants.LINEWIDTH + indexWidth;
                if (sumInt < intervalWidth) {
                    localX = intervalRight - auxInt - textMarkerWidth;
                    localY = DrawingConstants.MARGIN_TOP + 5 * lineHeight;
                    g2d.setColor(colorIntervalBorder);
                    g2d.drawString(auxString, localX, localY);
                }
            }
            // </editor-fold>
        }

        // <editor-fold defaultstate="collapsed" desc="Desenez alinierea">
        //Secventa out1
        localX = leftTextLength + centerTextOffset;
        localY = DrawingConstants.MARGIN_TOP + 2*lineHeight - centerTextOffset;
        g2d.setColor(fgColor);
        g2d.drawString(stringOut1, localX, localY);

        //Secventa relation
        localX = leftTextLength + centerTextOffset;
        localY = DrawingConstants.MARGIN_TOP + 3* lineHeight - centerTextOffset;
        g2d.setColor(fgColor);
        g2d.drawString(stringRelation, localX, localY);

        //Secventa out2
        localX = leftTextLength + centerTextOffset;
        localY = DrawingConstants.MARGIN_TOP + 4*lineHeight - centerTextOffset;
        g2d.setColor(fgColor);
        g2d.drawString(stringOut2, localX, localY);
        // </editor-fold>
    }

    /**
     * Obtin dimensiunea unei imaginii rendate
     * @return
     */
    private int getPartialImageHeight() {
        BufferedImage phonyImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) phonyImage.getGraphics();
        g2d.setFont(monospaceFont_gui);
        FontMetrics fontMetrics = g2d.getFontMetrics();

        int lineHeight = fontMetrics.getHeight();
        int recommendedSize = DrawingConstants.MARGIN_TOP + 5*lineHeight;
        return recommendedSize;
    }

    /**
     * Obtin numarulde nucleotide pe rand sugerat
     * @return
     */
    private int getPartialImageCols(int width) {
        BufferedImage phonyImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) phonyImage.getGraphics();
        g2d.setFont(monospaceFont_report);
        FontMetrics fontMetrics = g2d.getFontMetrics();

        int charWidth = fontMetrics.charWidth('G');
        int recommendedCols = width/charWidth  - CHARS_LEFT - CHARS_RIGHT;
        return recommendedCols;
    }

    /**
     * Numar nucleotidele dintr-un string
     * @param outString
     * @param offset
     * @param length
     * @return
     */
    public int countNucleotides(String outString, int offset, int length) {
        int matches = 0;
        for (int i = offset; i < offset+length; i++) {
            if (DNAUtils.isNucleotide(outString.charAt(i)))
                    matches++;
        }

        //Intorc umarul de nucleotide existente
        return matches;
    }

}

