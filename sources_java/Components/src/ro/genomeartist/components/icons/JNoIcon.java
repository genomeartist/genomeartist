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
import javax.swing.*;

/**
 * Icon pentru TreeTable
 *
 * @author iulian
 */
public class JNoIcon implements Icon {
    //Constante statice pentu cele mai folosite cazuri
    public static final JNoIcon ICON16 = new JNoIcon(16);
    public static final JNoIcon ICON24 = new JNoIcon(24);
    
    //Variabile de clasa
    private int width;
    private int height;

    /**
     * Contruiesc iconul default
     */
    public JNoIcon() {
        this(16);
    }

    /**
     * Construiesc un icon la dimensiunile presetate
     * @param size 
     */
    public JNoIcon(int size) {
        this.width = size;
        this.height = size;
    }

    //Pentru desenare
    private BasicStroke stroke = new BasicStroke(2);

    /**
     * Fac desenarea iconului
     * @param c
     * @param g
     * @param x
     * @param y 
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(Color.RED);
        g2d.setStroke(stroke);

        g2d.drawLine(x, y, x + width, y + height);
        g2d.drawLine(x, y + height, x + width, y);

        g2d.dispose();
    }

    /**
     * Obtine dimensiunea iconului
     * @return 
     */
    public int getIconWidth() {
        return width;
    }

    /**
     * Obtine dimensiunea iconului
     * @return 
     */
    public int getIconHeight() {
        return height;
    }
}