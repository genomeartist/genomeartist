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

package ro.genomeartist.components.propertiespanel.headers;

import ro.genomeartist.components.propertiespanel.IPropertiesHeader;
import ro.genomeartist.components.utils.ComponentsUtils;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * Construiesc un label cu rol de header
 * @author iulian
 */
public class JLabelPropertiesHeader extends JPanel implements IPropertiesHeader {
    private String title;
    
    //Strunctura panoului
    private JLabel labelTitlu;
    
    //Constante pentru desenare
    private static final int LINE_WIDTH = 1;
    private static final int MARGIN_TOP = 10;
    private static final int MARGIN_LEFT = 0;
    private static final int MARGIN_BOTTOM = 5;
    private static final int MARGIN_RIGHT = 0;

    /**
     * Consturctorul headerului
     * @param title Titlul headerului
     */
    public JLabelPropertiesHeader(String title) {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        //Setez borderul
        Border insideBorder = BorderFactory.createMatteBorder(0, 0, LINE_WIDTH, 0, Color.black);
        Border outsideBorder = BorderFactory.createEmptyBorder(
                MARGIN_TOP, MARGIN_LEFT, MARGIN_BOTTOM, MARGIN_RIGHT);
        this.setBorder(BorderFactory.createCompoundBorder(outsideBorder, insideBorder));

        this.title = title;

        //Fac panoul
        labelTitlu = new JLabel(title);
        ComponentsUtils.setFontBold(labelTitlu);
        this.add(labelTitlu);
        this.add(Box.createHorizontalGlue());
    }

    /**
     * Obtin containerul global al componentei
     * @return
     */
    public JComponent getContainerComponent() {
        return this;
    }

}
