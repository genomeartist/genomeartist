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

import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * Clasa ce reprezinta un dialog tip splash care
 * are ca incarcatura un JPanel
 * @author iulian
 */
public abstract class JModalPanel extends JDialog {
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;
    private Frame rootFrame;

    //Variabile ce retine panoul
    private JPanel userPanel = null;

    /**
     * Construiesc un panou cu dimensiuni date
     * @param rootFrame
     */
    public JModalPanel(Frame rootFrame, int width, int height) {
        super(rootFrame, true);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        //Setez panoul central
        this.rootFrame = rootFrame;
        userPanel = buildUserPanel();
        this.add(userPanel);

        //Setez dimensiuniile
        setSize(width, height);

        //Setez locatia
        setLocationRelativeTo(rootFrame);
    }

    /**
     * Construiesc un panou cu dimensiuni standard
     * @param rootFrame
     */
    public JModalPanel(Frame rootFrame) {
        this(rootFrame, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/
        //        Panoul utilizatorului         
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/

    /**
     * Metoda ce trebuie implementata pentru setarea panoului
     * @return
     */
    public abstract JPanel buildUserPanel();

    /**
     * Obtin panoul utilizator
     * @return
     */
    public JPanel getUserPanel() {
        return userPanel;
    }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/
        //       Metode interne
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/

    @Override
    public void setVisible(boolean b) {
        super.setLocationRelativeTo(rootFrame);
        super.setVisible(b);
    }

}
