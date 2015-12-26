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

package ro.genomeartist.gui.interfaces;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;

/**
 *
 * @author iulian
 */
public interface ICanPaint {

    /**
     * Metoda ce permite derogarea desenarii
     * @param g
     * @param width
     * @param height
     * @param bgColor
     * @param fgColor
     */
    public void paint(Graphics g, int width ,int height, Color bgColor, Color fgColor );

    /**
     * Metoda ce permite componentei sa sugereze marimea sa
     * @param component Componenta in care se va desena
     * @return
     */
    public int getRecommendedHeight(JComponent component);

    /**
     * Metoda ce permite componentei sa sugereze marimea sa
     * @param component Componenta in care se va desena
     * @return
     */
    public int getRecommendedWidth(JComponent component);
}
