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

package ro.genomeartist.components.popup.test;

import ro.genomeartist.components.popup.CopyPastePopupManager;
import ro.genomeartist.components.textarea.fixedwidth.JFixedWidthTextArea;
import ro.genomeartist.components.textfield.fixedwidth.JFixedWidthTextField;
import ro.genomeartist.components.utils.WindowUtilities;
import java.awt.Dimension;
import java.util.Locale;
import javax.swing.*;

public class CopyPastePopupTestClass extends JPanel {
    private static final int FIELD_COLS = 3;
    private static final int FIELD_WIDTH = 200;
    private static final int LABEL_WIDTH = 100;
    
    public CopyPastePopupTestClass() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        //Panel 1
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
            JLabel label1 = new JLabel("label1");
                label1.setPreferredSize(new Dimension(LABEL_WIDTH, 0));
            JFixedWidthTextField field1 = new JFixedWidthTextField(FIELD_WIDTH);
        panel1.add(label1);
        panel1.add(field1);
        panel1.add(Box.createHorizontalGlue());

        //Panel 2
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
            JLabel label2 = new JLabel("label2");
                label2.setPreferredSize(new Dimension(LABEL_WIDTH, 0));
            JFixedWidthTextArea field2 = new JFixedWidthTextArea(FIELD_COLS,FIELD_WIDTH);
        panel2.add(label2);
        panel2.add(field2);
        panel2.add(Box.createHorizontalGlue());
        
        //Fac layout-ul
        this.add(panel1);
        this.add(panel2);
        this.add(Box.createVerticalGlue());
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Metode statice pentru rularea clasei
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Init look and feel
        WindowUtilities.initLookAndFeel();

        //Create and set up the window.
        JFrame frame = new JFrame("CopyPastePopupTestClass");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //testez clasa
        CopyPastePopupTestClass testClass = new CopyPastePopupTestClass();
        frame.add(testClass);

        //Display the window.
        frame.setSize(500, 500);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    /**
     * Metoda main
     * @param args
     */
    public static void main(String[] args) {
        //Initializez limba
        Locale.setDefault(new Locale("ro", "RO"));

        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
