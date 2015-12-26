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

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 * Un buton ce simuleaza un buton nativ de toolbar
 * @author iulian
 */
public class JSimulatedToolbarButton extends JButton {

    /**
     * Constructor cu string
     * @param text
     */
    public JSimulatedToolbarButton(String text) {
        super(text);
        initButton();
    }

    /**
     * Constructor default
     */
    public JSimulatedToolbarButton() {
        initButton();
    }

    /**
     * Initializarea butonului
     */
    private void initButton() {
        //Il fac transparent
        this.setContentAreaFilled(false);

        //Nu are nevoie de focus
        setFocusable(false);
        setFocusPainted(false);
        setRequestFocusEnabled(false);

        //Pun un border manual
        setBorder(BorderFactory.createEtchedBorder());
        setBorderPainted(false);

        //Facem efectul de mouse-over manual
        addMouseListener(buttonMouseListener);
        setRolloverEnabled(true);
    }

    /**
     * Clasa ce implementeaza efectul de mouse-over asociat butonului
     */
    private final static MouseListener buttonMouseListener = new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }
    };

}
