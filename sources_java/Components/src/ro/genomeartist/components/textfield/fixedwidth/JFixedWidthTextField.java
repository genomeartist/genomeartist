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
package ro.genomeartist.components.textfield.fixedwidth;

import ro.genomeartist.components.popup.CopyPastePopupManager;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import javax.swing.JTextField;

/**
 *
 * @author iulian
 */
public class JFixedWidthTextField extends JTextField {
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Constructori
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Constructor secundar null
     */
    public JFixedWidthTextField() {
        this(0);
    }     
    
    /**
     * Constructor secundar
     * Constructor ce primeste ca parametru dimensiunea la care sa se ajunga
     * @param width dimensiunea celulei
     */
    public JFixedWidthTextField(int width) {
        this("",width);
    }    
    
    /**
     * Constructorul principal
     * Constructor ce primeste ca parametru dimensiunea la care sa se ajunga
     * @param text Textul ce se va afisa in textfield
     * @param width dimensiunea celulei
     */
    public JFixedWidthTextField(String text,int width) {
        super(text);
        configureFixedWidth(width);
        
        //Activez popup pentru copy-paste
        setCopyPastePopupEnabled(true);
    }

    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Metode publice
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Seteaza dimesiunea acestui textfield
     * @param width 
     */
    public void setFixedWidth(int width) {
        configureFixedWidth(width);
    }
    
    /**
     * Activeaza sau dezactiveaza popup-ul de copy-paste
     * @param isEnabled 
     */
    public void setCopyPastePopupEnabled(boolean shouldEnable) {
        MouseListener popupTrigger = CopyPastePopupManager.getCopyPastePopup().getPopupTrigger();
        
        //Activez sau dezactivez popup-ul
        this.removeMouseListener(popupTrigger);
        if (shouldEnable) {
            this.addMouseListener(popupTrigger);
        }
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Metode private
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /**
     * Seteaza dimesiunea acestui textfield
     * @param width 
     */
    private void configureFixedWidth(int width) {
        if (width > 0) {
            int columnWidth = this.getColumnWidth();
            this.setColumns(width / columnWidth);
           // this.setPreferredSize(new Dimension(width, width));
            this.setMaximumSize(new Dimension(width, width));
        }
    }
}
