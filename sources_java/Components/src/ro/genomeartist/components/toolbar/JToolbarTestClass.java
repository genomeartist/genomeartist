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

import ro.genomeartist.components.dropdownbutton.JDropDownButton;
import ro.genomeartist.components.headerrenderer.JHeaderSorterIcons;
import ro.genomeartist.components.utils.WindowUtilities;
import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

/**
 *
 * @author iulian
 */
public class JToolbarTestClass {
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Init look and feel
        WindowUtilities.initLookAndFeel();

        //Create and set up the window.
        JFrame frame = new JFrame("TableSelectionDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        
        JToolBar toolbar = new JConfiguredToolbar();
        JButton button;
        JDropDownButton dropDownButton;
        JPopupMenu popupMenu;
        JMenuItem menuItem;
        
        //button
        button = new JToolbarButton("Toolbar button 1");
            button.setIcon(JHeaderSorterIcons.DESC.getIcon());
        toolbar.add(button);
        
        //button
        popupMenu = new JPopupMenu();
            menuItem = new JMenuItem();
            menuItem.setText("menu item 1");
            menuItem.setIcon(JHeaderSorterIcons.DESC.getIcon());
        popupMenu.add(menuItem);
            menuItem = new JMenuItem();
            menuItem.setText("menu item 2");
            menuItem.setIcon(JHeaderSorterIcons.DESC.getIcon());
        popupMenu.add(menuItem);
            menuItem = new JMenuItem();
            menuItem.setText("menu item 3");
            menuItem.setIcon(JHeaderSorterIcons.DESC.getIcon());
        popupMenu.add(menuItem);
        dropDownButton = new JDropDownButton(
            "Drow down menu",
            JHeaderSorterIcons.DESC.getIcon(),
            popupMenu);
        toolbar.add(dropDownButton);   
        
        //button
        button = new JToolbarButton("Toolbar button 3");
            button.setIcon(JHeaderSorterIcons.DESC.getIcon());
        toolbar.add(button);  
        
        //Pun toolbarul
        contentPane.add(toolbar,BorderLayout.NORTH); 
        
        //Centru
        contentPane.add(new JLabel("Test"), BorderLayout.CENTER);
        

        //Display the window.
        frame.setSize(500, 500);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
