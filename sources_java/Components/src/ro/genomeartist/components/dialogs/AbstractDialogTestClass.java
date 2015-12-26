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
package ro.genomeartist.components.dialogs;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * Clasa de test pentru penoul de browse normative
 * @author iulian
 */
public class AbstractDialogTestClass extends JFrame {

    /**
     * Contruiesc un panou de test
     */
    public AbstractDialogTestClass() {
        super("Test Class");
        
        this.setSize(300,300);
        this.setLocation(100, 100);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        
        JButton startButton = new JButton();
            startButton.setText("Show dialog");
            startButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireActionShowDialog();
                }
            });
        
        this.add(startButton);
        this.add(Box.createVerticalGlue());
        //this.pack();
    }
    
    /**
     * Afisez dialogul
     */
    public void fireActionShowDialog() {
        TestDialog testDialog = new TestDialog(this, "titlu", true);
        testDialog.setMinimumSize(new Dimension(100, 100));
        testDialog.setProcentualSize(0, 0);
        testDialog.setLocationRelativeTo(this);
        
        //Afisez panoul
        testDialog.setVisible(true);
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Definitia unui dialog
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /**
     * O implementate a dialogurilor abstracte
     */
    private class TestDialog extends JOneButtonAbstractDialog {
        
        /**
         * Constructorul
         * @param owner
         * @param title
         * @param modal 
         */
        public TestDialog(Frame owner, String title, boolean modal) {
            super(owner, title, modal);
        }
        
        /**
         * Actiunea de ok
         */
        @Override
        public void fireActionOk() {
            TestDialog.this.dispose();
        }
        
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
        initLookAndFeel();

        //Create and set up the window.
        JFrame frame = new AbstractDialogTestClass();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Display the window.
        //frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

     /**
     *  <p style="margin-top: 0">
     *         Initializeaza Look and Feel. Default este cel al sistemului.
     *      </p>
     * @param titlu Titlul ferestrei
     * @author iulian
     */
    public static void initLookAndFeel() {
        String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
