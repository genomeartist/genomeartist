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

package ro.genomeartist.gui.controller.genes;

import ro.genomeartist.gui.controller.finalresult.IntervalMappingItem;
import ro.genomeartist.gui.controller.finalresult.IntervalMappingSet;
import ro.genomeartist.gui.custompaint.DrawingConstants;
import ro.genomeartist.gui.interfaces.ICanPaint;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.UIManager;

/**
 * Wrapper peste o gena si un interval
 * Wrapperul are rolul de a oferi datele pentru afisare
 * @author iulian
 */
public class GeneItemWrapper implements ICanPaint {
    private IntervalMappingSet mappingParinte;
    private IntervalMappingItem interval;
    
    
    /**
     * Contruiesc un wrapper pentru a afisa
     * @param interval
     * @param gene
     * @param type
     */
    public GeneItemWrapper(IntervalMappingSet mappingParinte,
            IntervalMappingItem interval) {
        this.mappingParinte = mappingParinte;
        this.interval = interval;
    }

    /**
     * Obtin gena la care se refera wrapperul
     * @return
     */
    public GeneItem getUpstreamGene() {
        if (interval.isComplement())
            return interval.getClosestDownstream();
        else return interval.getClosestUpstream();
    }

    /**
     * Obtin gena la care se refera wrapperul
     * @return
     */
    public GeneItem getDownstreamGene() {
        if (interval.isComplement())
            return interval.getClosestUpstream();
        else return interval.getClosestDownstream();
    }

    /**
     * Obtin un vector intreg de gene
     * @return
     */
    public GeneVector getFullGeneVector() {
        GeneVector geneVector = new GeneVector();

        //Iau genele din interior
        geneVector.addAll(interval.getInsideGenes());

        //Iau celelalte gene
        GeneItem upstreamGene = interval.getClosestUpstream();
        if (upstreamGene != null) geneVector.add(upstreamGene);
        GeneItem downstreamGene = interval.getClosestDownstream();
        if (downstreamGene != null) geneVector.add(downstreamGene);

        return geneVector;
    }

    /**
     * Obtin indexul celei mai reprezentative gene
     * @return
     */
    private int getRepresentativeInsideGeneIndex() {
        GeneVector geneVector = interval.getInsideGenes();
        boolean isLeftSideBetter = false;
        boolean isRightSideBetter = false;

        //Verific daca am gena
        if (mappingParinte.isEmpty())
            return 0;

        //Obtin indexul meu in parinte
        int myIndex = mappingParinte.indexOf(interval);
        int vecinStanga = myIndex-1;
        int vecinDreapta = myIndex+1;
        
        //Verific daca am tranzitie in stanga
        if (vecinStanga >= 0) {
            IntervalMappingItem itemStanga = mappingParinte.get(vecinStanga);
            if (itemStanga.isTransposon() != interval.isTransposon())
                isLeftSideBetter = true;
        }

        //Verific daca am tranzitie in dreapta 
        if (vecinDreapta < mappingParinte.size()) {
            IntervalMappingItem itemStanga = mappingParinte.get(vecinDreapta);
            if (itemStanga.isTransposon() != interval.isTransposon())
                isRightSideBetter = true;
        }

        //Obtin indexul de interes
        if (isLeftSideBetter)
            return 0;
        else
        if (isRightSideBetter)
            return geneVector.size() - 1;
        else
            return 0;
    }

     /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *      Desenarea genelor
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /**
     * Metoda ce deseneaza acest Maparea genelor
     */
    public void paint(Graphics g, int width ,int height, Color bgColor, Color fgColor ) {
        mainPaintMethod(g, width, height, bgColor, fgColor, true, true);
    }

    /**
     * Metoda ce deseneaza acest Maparea genelor
     */
    public void alternativePaint(Graphics g, int width ,int height, Color bgColor, Color fgColor ) {
        mainPaintMethod(g, width, height, bgColor, fgColor, false, true);
    }

    /**
     * Metoda ce deseneaza in fisier imagine
     */
    public void paintImage(Graphics g, int width ,int height, Color bgColor, Color fgColor ) {
        mainPaintMethod(g, width, height, bgColor, fgColor, true, false);
    }

    /**
     * Deseneaza panoul cu gene
     * @param g
     * @param width
     * @param height
     * @param bgColor
     * @param fgColor
     * @param paintAll
     */
    private void mainPaintMethod(Graphics g, int width ,int height, Color bgColor, Color fgColor ,
            boolean paintAll, boolean isBackgroundFilled) {
        Graphics2D g2d = (Graphics2D) g;

        // =========================
        // Parametrii de desenare
        // =========================
        // <editor-fold defaultstate="collapsed" desc="Variabile de desenare">
        FontMetrics fontMetrics; //Dimensiunile fontului
        int lineHeight; //Dimensiunea unei linii de text
        int lineWidth; //Dimensiunea liniei cu care se deseneaza
        int availableHeight; //Inaltimea disponibila pt desenare
        int availableWidth;  //Latimea disponibila pentru desenare

        //Variabile locale de desenare
        int localX, localY;
        int localWidth, localHeight;
        int auxInt;
        String auxString;
        GeneItem auxGene;
        double auxDouble;

        //Sageata liniei
        int linieOffset = 5;

        //Parametrii de aliniere secventa
        int reservedTextWidth = 50;    //Spatiul pentru textul de gene
        int reservedGap = 30;           //Spatiu intre text si interval
        int reservedActiveWidth;        //Spatiu activ de desenare intervale
        int actualActiveOffset;         //Locatia spatiului de desenare
        int actualIntervalWidth;        //Dimensiunea propriuzis-a a intervalului
        int actualIntervalOffset;       //Locatia intervalului
        double drawingRatio;            //Rata de desenare

        //parametrii pentru genele din interior
        GeneVector insideGenes;
        int actualGeneWidth;
        int actualGeneOffset;
        boolean trimLeftHead;
        boolean trimRightHead;

        //Culoarea de desenare
        Color colorIntervalBorder;
        Color colorIntervalFill;
        Color colorGeneBorder;
        Color colorGeneFill;
        Color lineColor;
        // </editor-fold>

        // =========================
        // Initializez parametrii
        // =========================

        // <editor-fold defaultstate="collapsed" desc="Initalizari">
        //Dimensiunea unei linii de text
        fontMetrics = g2d.getFontMetrics();
        lineHeight = fontMetrics.getHeight();
        lineWidth = DrawingConstants.LINEWIDTH - 1;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //Initializez dimensiuniile disponibile
        availableWidth = width;
        availableHeight = height;

        //Stabilesc culorile
        lineColor = fgColor;
        colorGeneBorder = DrawingConstants.COLOR_GENE_BORDER;
        colorGeneFill = DrawingConstants.COLOR_GENE_FILL;
        if (interval.isTransposon()) {
            colorIntervalBorder = DrawingConstants.COLOR_TRANSPOSON_BORDER;
            colorIntervalFill = DrawingConstants.COLOR_TRANSPOSON_FILL;
        } else {
            colorIntervalBorder = DrawingConstants.COLOR_GENOM_BORDER;
            colorIntervalFill = DrawingConstants.COLOR_GENOM_FILL;
        }

        //Calculez parametrii de rezervare
        reservedActiveWidth = width
                - DrawingConstants.MARGIN_LEFT
                - DrawingConstants.MARGIN_RIGHT
                - 2 * reservedTextWidth
                - 2 * reservedGap;
        actualActiveOffset = reservedTextWidth + reservedGap;

        //Parametrii intervalului
        drawingRatio = ((double) reservedActiveWidth / (double) interval.lengthInitialQuery);
        actualIntervalWidth = (int) ((double) interval.lengthQuery * drawingRatio);
        actualIntervalOffset = (int) ((double) interval.pozitieQuery * drawingRatio);
        actualIntervalOffset += actualActiveOffset;

        //Pentru genele din interior
        insideGenes = interval.getInsideGenes();
        // </editor-fold>

        // =========================
        // Fac desenarea
        // =========================

        //Fundalul pe care se deseneaza
        g2d.setStroke(new BasicStroke(lineWidth));
        g2d.setColor(bgColor);
        if (isBackgroundFilled) g2d.fillRect(0, 0, width, height);

        //Translatez marginea de sus
        g2d.translate(0, DrawingConstants.MARGIN_TOP);
        availableHeight -= DrawingConstants.MARGIN_TOP;

        //~~~~~~~~~~~~~~~~~Linia orizontala~~~~~~~~~~~~~~~~~~~~~~

        // <editor-fold defaultstate="collapsed" desc="Linia orizontala">
        //Desenez linia
        g2d.setColor(lineColor);
        localY = 3 * lineHeight / 2;
        g2d.drawLine(linieOffset, localY, availableWidth - 2 * linieOffset, localY);

        //Desenez varful sagetii
        auxInt = lineHeight / 4;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        if (interval.isComplement()) {
            localX = linieOffset;
            g2d.drawLine(localX, localY, localX + auxInt, localY - auxInt);
            g2d.drawLine(localX, localY, localX + auxInt, localY + auxInt);
        } else {
            localX = availableWidth - linieOffset - auxInt;
            g2d.drawLine(localX, localY, localX - auxInt, localY - auxInt);
            g2d.drawLine(localX, localY, localX - auxInt, localY + auxInt);
        }
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_DEFAULT);
        // </editor-fold>

        //~~~~~~~~~~~~~~~~~Linia 1~~~~~~~~~~~~~~~~~~~~~~

        // <editor-fold defaultstate="collapsed" desc="Chenarul cu intervalul">
        //Translatez marginea din stanga
        g2d.translate(DrawingConstants.MARGIN_LEFT, 0);
        availableWidth = availableWidth - DrawingConstants.MARGIN_LEFT
                - DrawingConstants.MARGIN_RIGHT;

        //Desenez gena upstream
        g2d.setColor(fgColor);
        auxGene = interval.getClosestUpstream();
        if (auxGene != null) {
            auxString = auxGene.getName();

        } else {
            auxString = "";

        }
        g2d.drawString(auxString, 0, lineHeight - 2 * lineWidth);

        //Desenez gena downstream
        g2d.setColor(fgColor);
        auxGene = interval.getClosestDownstream();
        if (auxGene != null) {
            auxString = auxGene.getName();

        } else {
            auxString = "";

        }
        localX = availableWidth - fontMetrics.stringWidth(auxString);
        g2d.drawString(auxString, localX, lineHeight - 2 * lineWidth);

        //Umplerea secventei
        g2d.translate(actualIntervalOffset, 0);
        g2d.setColor(colorIntervalFill);
        g2d.fillRoundRect(0, lineWidth, actualIntervalWidth, lineHeight - lineWidth,
                DrawingConstants.ARC_WIDTH, DrawingConstants.ARC_HEIGHT);

        //Chenarul secventei
        g2d.setColor(colorIntervalBorder);
        g2d.drawRoundRect(0, lineWidth, actualIntervalWidth, lineHeight - lineWidth,
                DrawingConstants.ARC_WIDTH, DrawingConstants.ARC_HEIGHT);
        g2d.translate(-actualIntervalOffset, 0);

        //Numarul intervalului
        auxString = (interval.getIntervalIndex() + 1) + "";
        auxInt = fontMetrics.stringWidth(auxString);
        localX = actualIntervalOffset + (actualIntervalWidth - auxInt) / 2;
        g2d.setColor(colorIntervalBorder);
        g2d.drawString(auxString, localX, lineHeight - 2 * lineWidth);
        // </editor-fold>

        //~~~~~~~~~~~~~~~~~Linia 2~~~~~~~~~~~~~~~~~~~~~~

        //Translatez marginea de sus
        g2d.translate(0, lineHeight);
        availableHeight -= lineHeight;

        //~~~~~~~~~~~~~~~~~Linia 3~~~~~~~~~~~~~~~~~~~~~~
        // <editor-fold defaultstate="collapsed" desc="Desenez genele">
        int loopStart;
        int loopCount;
        if (paintAll) {
            loopStart = 0;
            loopCount = insideGenes.size();
        } else {
            if (insideGenes.isEmpty()) {
                loopStart = 0;
                loopCount = 0;
            } else {
                loopStart = getRepresentativeInsideGeneIndex();
                loopCount = 1;
            }
        }

        for (int i = loopStart; i < loopStart+loopCount; i++) {
            auxGene = insideGenes.elementAt(i);

            //Translatez marginea de sus
            g2d.translate(0, lineHeight);
            availableHeight -= lineHeight;

            //Obtin prima gena
            if (auxGene != null) {
                if (interval.isComplement()) {
                    auxInt = interval.pozitieGenom + interval.lengthGenom - 1
                            - auxGene.locationEnd;
                    actualGeneOffset = actualIntervalOffset + (int) ((double) auxInt * drawingRatio);
                    auxInt = auxGene.locationEnd - auxGene.locationStart + 1;
                    actualGeneWidth = (int) ((double) auxInt * drawingRatio);
                } else {
                    auxInt = auxGene.locationStart - interval.pozitieGenom;
                    actualGeneOffset = actualIntervalOffset + (int) ((double) auxInt * drawingRatio);
                    auxInt = auxGene.locationEnd - auxGene.locationStart + 1;
                    actualGeneWidth = (int) ((double) auxInt * drawingRatio);
                }
                
                //Faca verificarea parametriilor
                trimLeftHead = false;
                trimRightHead = false;
                if (actualGeneOffset < 0) {
                    trimLeftHead = true;
                    actualGeneWidth += actualGeneOffset;
                    actualGeneOffset = 0;
                }
                if (actualGeneOffset + actualGeneWidth > availableWidth) {
                    trimRightHead = true;
                    actualGeneWidth += availableWidth - actualGeneWidth;
                }

                //Umplerea secventei
                g2d.setColor(colorGeneFill);
                g2d.fillRoundRect(actualGeneOffset, lineWidth, actualGeneWidth, lineHeight - 2 * lineWidth,
                        DrawingConstants.ARC_WIDTH, DrawingConstants.ARC_HEIGHT);

                //Chenarul secventei
                g2d.setColor(colorGeneBorder);
                g2d.drawRoundRect(actualGeneOffset, lineWidth, actualGeneWidth, lineHeight - 2 * lineWidth,
                        DrawingConstants.ARC_WIDTH, DrawingConstants.ARC_HEIGHT);

                //Fac taierea capetelor
                g2d.setColor(bgColor);
                Composite orig = g2d.getComposite();
                if (trimLeftHead) {
                    localX = -lineWidth;
                    localWidth = DrawingConstants.ARC_WIDTH + lineWidth;
                    //Sterg fundalul la 0
                    g2d.setComposite(AlphaComposite.Clear);
                    g2d.fillRect(localX, 0, localWidth, lineHeight);
                    g2d.setComposite(orig);
                    //Desenez daca trebuie fundalul original
                    if (isBackgroundFilled) g2d.fillRect(localX, 0, localWidth, lineHeight);
                    //Updatez capatul din stanga
                    actualGeneOffset = 0;
                    actualGeneWidth -= localWidth;
                }
                if (trimRightHead) {
                    localX = availableWidth - DrawingConstants.ARC_WIDTH;
                    localWidth = DrawingConstants.ARC_WIDTH + lineWidth;
                    //Sterg fundalul la 0
                    g2d.setComposite(AlphaComposite.Clear);
                    g2d.fillRect(localX, 0, localWidth, lineHeight);
                    g2d.setComposite(orig);
                    //Desenez daca trebuie fundalul original
                    if (isBackgroundFilled) g2d.fillRect(localX, 0, localWidth, lineHeight);
                    //Updatez capatul din dreapta
                    actualGeneWidth -= localWidth;
                }

                //Desenez pe gena numele ei
                auxString = auxGene.getName();
                auxInt = fontMetrics.stringWidth(auxString);
                localX = actualGeneOffset + (actualGeneWidth - auxInt) / 2;
                g2d.setColor(fgColor);
                g2d.drawString(auxString, localX, lineHeight - 3 * lineWidth);
            }
        }
        // </editor-fold>

        //~~~~~~~~~~~~~~~~~Linia 4~~~~~~~~~~~~~~~~~~~~~~

        // <editor-fold defaultstate="collapsed" desc="Linia de informatii">
        if (!paintAll) {
            //Translatez marginea de sus
            g2d.translate(0, lineHeight);
            availableHeight -= lineHeight;
            auxInt = insideGenes.size();
            if (auxInt > 1) {
                //Compun testul de afisat
                if (auxInt == 2) {
                    auxString = "and " + (auxInt - 1) + " other gene..";

                } else {
                    auxString = "and " + (auxInt - 1) + " other genes..";

                    //Desenez stringul

                }
                auxInt = fontMetrics.stringWidth(auxString);
                localX = (availableWidth - auxInt) / 2;
                g2d.setColor(fgColor);
                g2d.drawString(auxString, localX, lineHeight - lineWidth);
            }
        }
        // </editor-fold>

        //~~~~~~~~~~~~~~~~~Liniile veriticale~~~~~~~~~~~~~~~~~~~~~~
        g2d.translate(0, availableHeight-height);
        g2d.setColor(colorIntervalBorder);
        float[] dash = { 2f, 0f, 2f };
        g2d.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_BUTT,
             BasicStroke.JOIN_ROUND, 1.0f, dash, 2f ));
        g2d.translate(actualIntervalOffset, 0);
        g2d.drawLine(0, height, 0, 0);
        g2d.translate(actualIntervalWidth, 0);
        g2d.drawLine(0, height, 0, 0);
        g2d.translate(-actualIntervalOffset - actualIntervalWidth, 0);

        //~~~~~~~~~~~~~~~~~Refac setarea initiala
        g2d.translate(-DrawingConstants.MARGIN_LEFT, 0);
        g2d.setStroke(new BasicStroke(lineWidth));
    }

     /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *      Calcularea dimensiuniilor
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Get recomended height
     */
    public int getRecommendedHeight(JComponent component) {
        Font font = UIManager.getDefaults().getFont("Table.font");
        int lineHeight = component.getFontMetrics(font).getHeight();
        int recommendedSize = DrawingConstants.MARGIN_TOP +
                DrawingConstants.MARGIN_BOTTOM + 3*lineHeight +
                interval.getInsideGenes().size()*lineHeight;
        return recommendedSize;
    }

    /**
     * Get recomended qidth
     */
    public int getRecommendedWidth(JComponent component) {
        return DrawingConstants.RECOMMENDED_WIDTH;
    }

    /**
     * Get alternative recomended height
     *  - folosit pentru rendarea in tabel
     */
    public int getAltRecommendedHeight(JComponent component) {
        Font font = UIManager.getDefaults().getFont("Table.font");
        int lineHeight = component.getFontMetrics(font).getHeight();
        int recommendedSize = DrawingConstants.MARGIN_TOP +
                DrawingConstants.MARGIN_BOTTOM + 4*lineHeight;
        return recommendedSize;
    }

    /**
     * Get alternative recomended qidth
     *  - folosit pentru rendarea in tabel
     */
    public int getAltRecommendedWidth(JComponent component) {
        return DrawingConstants.RECOMMENDED_WIDTH;
    }

    /**
     * Get image recomended height
     *  - folosit pentru rendarea in imagine
     */
    public int getImageHeight(JComponent component) {
        return getRecommendedHeight(component);
    }

    /**
     * Get image recomended width
     *  - folosit pentru rendarea in imagine
     */
    public int getImageWidth(JComponent component) {
        return DrawingConstants.IMAGE_WIDTH;
    }
}
