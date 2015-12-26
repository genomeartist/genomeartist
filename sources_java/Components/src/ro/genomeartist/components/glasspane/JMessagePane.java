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

package ro.genomeartist.components.glasspane;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author iulian
 */
public class JMessagePane extends JPanel {
    private String row1;
    private String row2;
    

    private JLabel label1;
    private JLabel label2;

    /**
     * Construiesc un panou cu mesaj editabil
     * @param row1
     * @param row2
     */
    public JMessagePane(String row1, String row2) {
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));

        this.add(Box.createVerticalGlue());
        JPanel phony = new JPanel();
            phony.setLayout(new BoxLayout(phony, BoxLayout.X_AXIS));
                phony.add(Box.createHorizontalGlue());
                label1 = new JLabel(row1);
                phony.add(label1);
                phony.add(Box.createHorizontalGlue());
        this.add(phony);
            phony = new JPanel();
            phony.setLayout(new BoxLayout(phony, BoxLayout.X_AXIS));
                phony.add(Box.createHorizontalGlue());
                label2 = new JLabel(row2);
                phony.add(label2);
                phony.add(Box.createHorizontalGlue());
        this.add(phony);
        this.add(Box.createVerticalGlue());

        this.setSize( 200, 50);
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
    }

    /**
     * Seteaza mesajul ce va fi afisat
     * @param row1
     * @param row2
     */
    public void setMessage(String row1, String row2) {
        label1.setText(row1);
        label2.setText(row2);
    }

}
