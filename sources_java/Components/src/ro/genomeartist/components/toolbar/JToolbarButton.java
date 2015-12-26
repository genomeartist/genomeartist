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

package ro.genomeartist.components.toolbar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

/**
 * Wrapper peste butonul normal pentru a-l forta sa nu deseneze focusul
 * @author iulian
 */
public class JToolbarButton extends JButton {

    /**
     * Constructor cu un String
     * @param text
     */
    public JToolbarButton(String text) {
        super(text);
        configureButton();
    }

    /**
     * Constructor vid
     */
    public JToolbarButton() {
        configureButton();
    }

    /**
     * Dezactivez focusul pe butoanele din toolbar
     */
    private void configureButton() {
        //Se reseteaza borderul pentru ca JButton sa onoreze inseturile
        Border defaultBorder = this.getBorder();
        Border emptyBorder = BorderFactory.createEmptyBorder();
        Border compoundBorder = BorderFactory.createCompoundBorder(emptyBorder, defaultBorder);
        this.setBorder(compoundBorder);
        
        //Se fac setarile de afisare
        this.setFocusable(false);
        this.setFocusPainted(false);
        this.setRequestFocusEnabled(false);
        this.setRolloverEnabled(true);
        this.setBorderPainted(false);
    }
}
