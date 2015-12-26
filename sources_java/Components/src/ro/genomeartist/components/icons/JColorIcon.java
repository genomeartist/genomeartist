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

package ro.genomeartist.components.icons;

import java.awt.*;
import java.util.Random;
import javax.swing.*;

/**
 * Icon pentru TreeTable
 *
 * @author iulian
 */
public class JColorIcon implements Icon{
    private static Random rand = new Random(System.currentTimeMillis());
    private int width = 100;
    private int height = 16;
    private Color color;

    /**
     * Constructorul null. Creeaza un icon cu o culoare random
     */
    public JColorIcon() {
        setRandomColor();
    }

    /**
     * Creeaza un icon cu culoare specificata
     */
    public JColorIcon(Color color) {
        this.color = color;
    }

    /**
     * Metoda de desenare a iconului
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(color);
        g2d.fillRect(x, y, width, height);

        g2d.dispose();
    }

    /*
     *     Getter setter
     */
    
    public int getIconWidth() {
        return width;
    }

    public int getIconHeight() {
        return height;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColorForText() {
        return getColorForText(color);
    }

    /**
     * Obtin culoarea cea mai potrivita pentru text
     * @return
     */
    public static Color getColorForText(Color bgColor) {
        int prag = 500;
        int current = bgColor.getRed()+bgColor.getGreen()+bgColor.getBlue();

        if (current < prag) return Color.WHITE;
        else return Color.BLACK;
    }

    /**
     * Seteaza o culoare random
     */
    private void setRandomColor() {
        this.color = getRandomColor();
    }

    /**
     * Obtine o culoare Random
     * @return
     */
    public static Color getRandomColor() {
        float red = rand.nextFloat();
        float green = rand.nextFloat();
        float blue = rand.nextFloat();
        return new Color(red, green, blue);
    }
}