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
package ro.genomeartist.components.dropdownbutton;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.Icon;
import javax.swing.border.Border;

/**
 * Creez un border ce rendeaza o mica iconita pentru a marca meniul de dropdown
 * @author iulian
 */
public class MenuArrowBorder implements Border {
    private static final Icon ARROW_ICON = new MenuArrowIcon();;
    
    /**
     * COnstructor null
     */
    public MenuArrowBorder() {
    }

    /**
     * Metoda ce deseneaza efectiv borderul
     * @param c
     * @param g
     * @param x
     * @param y
     * @param width
     * @param height 
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        //Compute the position
        int icon_x = x + width-ARROW_ICON.getIconWidth()-4;
        int icon_y = y + (height-ARROW_ICON.getIconHeight())/2;
        
        //Desenez iconul
        ARROW_ICON.paintIcon(c, g, icon_x, icon_y);
    }

    /**
     * Obtin dimensiuniile adaugate de acest border
     * @param c
     * @return 
     */
    public Insets getBorderInsets(Component c) {
        return new Insets(0, 0, 0, ARROW_ICON.getIconWidth());
    }

    /**
     * Specifica daca borderul suprascrie setarile
     * @return 
     */
    public boolean isBorderOpaque() {
        return false;
    }
}