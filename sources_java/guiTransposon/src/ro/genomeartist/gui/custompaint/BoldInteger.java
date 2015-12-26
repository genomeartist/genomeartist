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

package ro.genomeartist.gui.custompaint;

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
 * Clasa wraper pentre un intreg ce i ofera posibilitatea de a se desena diferit
 * @author iulian
 */
public class BoldInteger implements Comparable,ICanPaint {
    private static final int FONT_OFFSET = 2;
    private static final int LINEWIDTH = 2;
    //Recomended Width
    private static final int RECOMMENDED_WIDTH = 300;
    private int integer;

    /**
     * Contruiesc un wrapper peste un intreg
     * @param integer
     */
    public BoldInteger(int integer) {
        this.integer = integer;
    }

    /**
     * Obtin intregul transportat
     * @return
     */
    public int getInteger() {
        return integer;
    }

    /**
     * Metoda de afisarea a intregului intr-un context grafic
     * @param g     Contextul grafic
     * @param width Latimea
     * @param height Inaltimea
     * @param bgColor Culoarea de fundal
     * @param fgColor Culoarea scrisului
     */
    public void paint(Graphics g, int width ,int height, Color bgColor, Color fgColor ) {
        Graphics2D g2d = (Graphics2D) g;

        //Setari pentru font
        Font oldFont = g2d.getFont();
        int oldFontSize = oldFont.getSize();
        FontMetrics fontMetrics;
        g2d.setFont(oldFont.deriveFont(Font.BOLD, oldFontSize+FONT_OFFSET));
        fontMetrics = g2d.getFontMetrics();

        //Parametrii de desenare
        String text = integer+"";
        int textHeight = fontMetrics.getHeight();
        int textWidth = fontMetrics.stringWidth(text);

        //pozitia curenta la care se deseneaza
        int localX,localY;
        int localWidth,localHeight;

        // ====================
        // Incep sa desenez
        // ====================

        //Fundalul pe care se deseneaza
        g2d.setStroke(new BasicStroke(LINEWIDTH));
        g2d.setColor(bgColor);
        g2d.fillRect(0, 0, width, height);

        //Desenez stringul
        localX = (width - textWidth)/2;
        localY = (height+textHeight)/2;
        g2d.setColor(fgColor);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawString(text, localX, localY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_DEFAULT);
    }

    /**
     * Get recomended height
     */
    public int getRecommendedHeight(JComponent component) {
        Font font = UIManager.getDefaults().getFont("Table.font");
        int lineHeight = component.getFontMetrics(font).getHeight();
        int recommendedSize = lineHeight;
        return recommendedSize;
    }

    /**
     * Get recomended qidth
     */
    public int getRecommendedWidth(JComponent component) {
        return RECOMMENDED_WIDTH;
    }

    /**
     * Metoda ce determina ordinea naturala a obiectelor
     * @param o Obiectul cu care se compara
     * @return negativ, zero sau pozitiv daca obiectul este mai mic,
     * egal sau mai mare decat obiectul verificat
     */
    public int compareTo(Object o) {
        if (o instanceof BoldInteger) {
            BoldInteger boldInteger = (BoldInteger) o;
            return integer - boldInteger.getInteger();
        } else throw new ClassCastException("Cannot compare");
    }
}
