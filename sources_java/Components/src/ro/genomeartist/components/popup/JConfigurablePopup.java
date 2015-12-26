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

package ro.genomeartist.components.popup;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

/**
 * Clasa ce face managementul unui popul.
 * Poate sa activeze sau sa dezactiveze optiuni din mers
 * @author iulian
 */
public class JConfigurablePopup extends JPopupMenu {
    //Ascultatorul pe butoanele din meniu
    public ActionListener myPopupListener = null;

    //Hashtable-ul ce retine itemurile din meniu
    Hashtable<String, Component> items;

    /**
     * Constructorul popup-ului pentru selectie simpla
     * @param popupListener Ascultatorul pentru butoane
     */
    public JConfigurablePopup(ActionListener popupListener) {
        super();
        items = new Hashtable<String, Component>();
        this.myPopupListener = popupListener;
    }

    /**
     * Adauga un item la meniu si in hashtable
     * @param itemName Numele optiunii
     */
    public void addItem(String itemName){
        this.addItem(itemName, null);
    }    
    
    /**
     * Adauga un item la meniu si in hashtable
     * @param itemName Numele optiunii
     */
    public void addItem(String itemName, Icon icon){
        this.addItem(itemName, icon, -1, null);
    }
    
    /**
     * Adauga un item la meniu si in hashtable
     * @param itemName Numele optiunii
     */
    public void addItem(String itemName, Icon icon, int mnemonic){
        this.addItem(itemName, icon, mnemonic, null);
    }
    
    /**
     * Adauga un item la meniu si in hashtable
     * @param itemName Numele optiunii
     */
    public void addItem(String itemName, String actionName, Icon icon, int mnemonic){
        this.addItem(itemName, actionName, icon, mnemonic, null);
    }
    
    /**
     * Adauga un item la meniu si in hashtable
     * @param itemName Numele optiunii
     */
    public void addItem(String itemName, Icon icon, KeyStroke accelerator){
        this.addItem(itemName, icon, -1, accelerator);
    }
    
    /**
     * Adauga un item la meniu si in hashtable
     * @param itemName Numele optiunii
     */
    public void addItem(String itemName, String actionName, Icon icon, KeyStroke accelerator){
        this.addItem(itemName, actionName, icon, -1, accelerator);
    }
    
    /**
     * Adauga un item la meniu si in hashtable
     * @param itemName Numele optiunii
     */
    public void addItem(String itemName, int mnemonic, KeyStroke accelerator){
        this.addItem(itemName, null, mnemonic, accelerator);
    }

    /**
     * Adauga un item la meniu si in hashtable
     * @param itemName
     * @param icon
     * @param mnemonic
     * @param accelerator 
     */
    public void addItem(String itemName, Icon icon, 
                int mnemonic, KeyStroke accelerator) {
        this.addItem(itemName, itemName.toLowerCase(), icon, mnemonic, accelerator);
    }
    
    /**
     * Adauga un item la meniu si in hashtable
     * @param itemName Numele optiunii
     */
    public void addItem(String itemName, String actionName, Icon icon, 
            int mnemonic, KeyStroke accelerator){
        //Setez itemul
        JMenuItem menuItem;
        menuItem = new JMenuItem(itemName);
        if (icon != null)
            menuItem.setIcon(icon);
        menuItem.setActionCommand(actionName);
        menuItem.addActionListener(myPopupListener);
        if (mnemonic >= 0)
            menuItem.setMnemonic(mnemonic);
        if (accelerator != null)
            menuItem.setAccelerator(accelerator);
        
        //Il inregistrez
        items.put(itemName, menuItem);
        this.add(menuItem);
    }

    /**
     * Remove item from menu
     * @param itemName
     */
    public void removeItem(String itemName) {
        Component menuItem;
        menuItem = items.get(itemName);
        this.remove(menuItem);
        items.remove(itemName);
    }

    /**
     * Adauga un meniu la popup
     * @param menuName
     * @param menu
     */
    public void addMenu(JMenu menu) {
        items.put(menu.getName(), menu);
        this.add(menu);
    }

   /**
     * Adaug un separator cu nume
     */
    public void addNamedSeparator(String itemName) {
        int componentCount = this.getComponentCount();
        this.addSeparator();
        items.put(itemName, this.getComponent(componentCount));
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
