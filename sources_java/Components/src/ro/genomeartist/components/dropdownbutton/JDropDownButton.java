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

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 *
 * @author iulian
 */
public class JDropDownButton extends JToggleButton {
    private static final Icon i = new MenuArrowIcon();
    protected JPopupMenu pop;

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Constructori
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Constructor secundar
     * @param popup 
     */
    public JDropDownButton(JPopupMenu popup) {
        this("", null,popup);
    }
    
    /**
     * Constructor secundar
     * @param icon
     * @param popup 
     */
    public JDropDownButton(Icon icon,JPopupMenu popup) {
        this("", icon,popup);
    }
    
    /**
     * Constructor secundar
     * @param text
     * @param popup 
     */
    public JDropDownButton(String text,JPopupMenu popup) {
        this(text, null,popup);
    }
    
    /**
     * Contructorul principal.
     * Construieste un meniu ce lanseaza un popup de selectie.
     * @param text
     * @param icon
     * @param popup 
     */
    public JDropDownButton(String text, Icon icon, JPopupMenu popup) {
        super();

        //Seteaza popupul
        setPopupMenu(popup);

        Action a = new AbstractAction(text) {
            public void actionPerformed(ActionEvent ae) {
                JDropDownButton b = (JDropDownButton)ae.getSource();
                if(pop!=null) pop.show(b, 0, b.getHeight());
            }
        };
        a.putValue(Action.SMALL_ICON, icon);
        setAction(a);
        
        //Setez iconita ca si border
        Border defaultBorder = getBorder();
        Border arrowBorder = new MenuArrowBorder();
        Border compoundBorder = BorderFactory.createCompoundBorder(arrowBorder, defaultBorder);
        this.setBorder(compoundBorder);
        
        //Configurez butonul
        this.setFocusable(false);
        this.setFocusPainted(false);
        this.setRequestFocusEnabled(false);
        this.setRolloverEnabled(true);
       // this.setBorderPainted(false);
    }

    /**
     * Seteaza popup-ul lansat de catre buton
     * @param pop 
     */
    private void setPopupMenu(JPopupMenu pop) {
        this.pop = pop;
        pop.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuCanceled(PopupMenuEvent e) {}
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                setSelected(false);
            }
        });
    }
}