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

import ro.genomeartist.components.popup.JCopyPasteIcons;
import ro.genomeartist.components.toolbar.JConfiguredToolbar;
import ro.genomeartist.components.toolbar.JToolbarButton;
import ro.genomeartist.components.utils.WindowUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

/**
 * Clasa de test buton de dropdown
 * @author Iulian
 */
public class DropDownButtonTestClass extends JPanel {
    protected JToolBar toolBar;
        JButton buttonSterge;
        JDropDownButton buttonAdauga;
        JButton buttonProprietati;
    
    /**
     * Constructorul clasei
     */
    public DropDownButtonTestClass() {
        super();
        this.setLayout(new BorderLayout());

        //Fac labelul
        JLabel label = new JLabel("ole");
        label.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        
        //Fac butoanele
        toolBar = createToolBar();
        
        //Fac layout-ul
        add(toolBar,BorderLayout.NORTH);
        add(label,BorderLayout.CENTER);
    }
    
    /**
     *  <p style="margin-top: 0">
     *  Creeaza toolbarul principal
     *  </p>
     * @return Obiectul ce reprezinta toolbarul
     * @author iulian
     */
    private JToolBar createToolBar() {
        JToolBar localToolBar = new JConfiguredToolbar("Toolbar");
        
        buttonSterge = new JToolbarButton("Sterge");
        buttonSterge.setIcon(JCopyPasteIcons.COPY.getIcon());
        localToolBar.add(buttonSterge);        
        
        //Creez butonul de drop down
        JPopupMenu popupMenu  = new JPopupMenu();
        JMenuItem menuItem;

        //Adaug item-uri in meniu
        menuItem = new JMenuItem();
        menuItem.setText("Rapid");
        menuItem.setIcon(JCopyPasteIcons.PASTE.getIcon());
        menuItem.setAccelerator(KeyStroke.getKeyStroke("F2"));
        popupMenu.add(menuItem);

        //Adaug item-uri in meniu
        menuItem = new JMenuItem();
        menuItem.setText("Standard");
        menuItem.setIcon(JCopyPasteIcons.PASTE.getIcon());
        menuItem.setAccelerator(KeyStroke.getKeyStroke("F3"));
        popupMenu.add(menuItem);

        buttonAdauga = new JDropDownButton(
                "Adauga",
                JCopyPasteIcons.COPY.getIcon(),
                popupMenu);
        localToolBar.add(buttonAdauga);

        localToolBar.addSeparator();

        buttonProprietati = new JToolbarButton("Proprietati");
        buttonProprietati.setIcon(JCopyPasteIcons.CUT.getIcon());
        localToolBar.add(buttonProprietati);

        return localToolBar;
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
        JFrame frame = new JFrame(DropDownButtonTestClass.class.getSimpleName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //testez clasa
        DropDownButtonTestClass testClass = new DropDownButtonTestClass();
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
