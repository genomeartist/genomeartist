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

package ro.genomeartist.components.modalpanel.progresspanel;

import ro.genomeartist.components.modalpanel.JModalPanel;
import java.awt.Frame;
import javax.swing.JPanel;

/**
 *
 * @author iulian
 */
public class JModalProgressPanel extends JModalPanel {
    private static final int DEFAULT_WIDTH = 250;
    private static final int DEFAULT_HEIGHT = 50;

    //Pastrez panoul pentru a-l updata
    private JProgressPanel progressPanel;

    /**
     * Construiesc un panou de progres generic
     * @param rootFrame
     */
    public JModalProgressPanel(Frame rootFrame) {
        super(rootFrame, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        progressPanel = (JProgressPanel) this.getUserPanel();
    }

    @Override
    public JPanel buildUserPanel() {
        return new JProgressPanel();
    }

    /**
     * Setez tipul de afisare al panoului de progress
     * @param title
     * @param mode
     */
    public void setDisplayMode(String title,int mode) {
        progressPanel.setDisplayMode(title, mode);
    }

    /**
     * Setez progresul la panou
     * @param value
     */
    public void setText(String value) {
        progressPanel.setText(value);
    }

    /**
     * Setez progresul la panou
     * @param value
     */
    public void setProgress(int value) {
        progressPanel.setProgress(value);
    }
}
