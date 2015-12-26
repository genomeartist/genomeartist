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

package ro.genomeartist.components.modalpanel;

import ro.genomeartist.components.modalpanel.progresspanel.JModalProgressPanel;
import ro.genomeartist.components.modalpanel.progresspanel.JProgressPanel;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 * @author iulian
 */
public class TestModal {
    /**
     * Metoda de testare
     * @param args
     */
    public static void main(String[] args) {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JButton button = new JButton(new AbstractAction("press") {
            public void actionPerformed(ActionEvent e) {
                JModalProgressPanel progressPanel = new JModalProgressPanel(frame);
                progressPanel.setDisplayMode("titlu", JProgressPanel.DETERMINATE);
                progressPanel.setProgress(50);
                progressPanel.setVisible(true);
            }
        });

        frame.add(button);

        frame.setSize(100,100);
        frame.setVisible(true);
    }
}
