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

package ro.genomeartist.gui.popups;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * Clasa ce face managementul unui popul.
 * Poate sa activeze sau sa dezactiveze optiuni din mers
 * @author iulian
 */
public class JSimpleMenu extends JMenu {
    //Ascultatorul pe butoanele din meniu
    public ActionListener myPopupListener = null;

    //Hashtable-ul ce retine itemurile din meniu
    Hashtable<String, Component> items;

    /**
     * Constructorul popup-ului pentru selectie simpla
     * @param popupListener Ascultatorul pentru butoane
     */
    public JSimpleMenu(String name, ActionListener popupListener) {
        super(name);
        items = new Hashtable<String, Component>();
        this.myPopupListener = popupListener;
    }

    /**
     * Adauga un item la meniu si in hashtable
     * @param itemName Numele optiunii
     */
    public void addItem(String itemName){
        JMenuItem menuItem;
        menuItem = new JMenuItem(itemName);
        menuItem.setActionCommand(itemName.toLowerCase());
        menuItem.addActionListener(myPopupListener);
        items.put(itemName, menuItem);
        this.add(menuItem);
    }

    /**
     * Activeaza un item
     * @param itemName Numele optiunii
     */
    public void setItemEnabled(String itemName) {
        Component menuItem;
        menuItem = items.get(itemName);
        if (menuItem!=null) menuItem.setEnabled(true);
    }

    /**
     * Dezactiveaza un item (il face gray)
     * @param itemName Numele optiunii
     */
    public void setItemDisabled(String itemName) {
        Component menuItem;
        menuItem = items.get(itemName);
        if (menuItem!=null) menuItem.setEnabled(false);
    }

    /**
     * Afiseaza un item in tabel
     * @param itemName Numele optiunii
     */
    public void setItemVisible(String itemName) {
        Component menuItem;
        menuItem = items.get(itemName);
        if (menuItem!=null) menuItem.setVisible(true);
    }

     /**
     * Ascunde un item din meniu
     * @param itemName Numele optiunii
     */
    public void setItemInvisible(String itemName) {
        Component menuItem;
        menuItem = items.get(itemName);
        if (menuItem!=null) menuItem.setVisible(false);
    }
}
