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
package ro.genomeartist.components.textfield.validator;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.border.Border;

/**
 *
 * @author iulian
 */
public class ColorizingBorder implements Border {
    private final Border real;
    private Color backgroundColor;

    public ColorizingBorder(Border real, Color color) {
        this.real = real;
        this.backgroundColor = color;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        if (real != null)
            real.paintBorder (c, g, x, y, width, height);
        g.setColor (backgroundColor);
        Graphics2D gg = (Graphics2D) g;
        Composite composite = gg.getComposite();
        AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1F);
        try {
            gg.setComposite(alpha);
            gg.fillRect(x,y,width,height);
        } finally {
            gg.setComposite(composite);
        }
    }

    public Insets getBorderInsets(Component c) {
        if (real != null)
            return real.getBorderInsets(c);
        else return new Insets(0, 0, 0, 0);
    }

    public boolean isBorderOpaque() {
        return false;
    }
}