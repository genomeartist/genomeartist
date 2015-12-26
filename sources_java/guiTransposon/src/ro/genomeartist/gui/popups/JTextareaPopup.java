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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author iulian
 */
public class JTextareaPopup {
    private JTextArea textarea;
    boolean isReadonly;

    //Variabile de transfer intre apelurile listnerelor
    private DefaultMutableTreeNode selectedOferteTreeNode=null; //este setat la actionare popup oferta

    //Popup-urile ce se afiseaza
    private JSimplePopup textareaPopup;  //pt arborele cu oferte

    //Constante pentru stringuri
    private static final String CUT = "Cut";
    private static final String COPY = "Copy";
    private static final String PASTE = "Paste";

    /**
     * Constructorul principal
     * @param fereastra Fereastra pe care se lanseaza popup-ul
     */
    public JTextareaPopup(JTextArea textarea, boolean isReadonly) {
        this.textarea = textarea;
        this.isReadonly = isReadonly;

        //Creez popup-urile
        textareaPopup = createTextareaPopup();
    }

    /**
     * Constructorul secundar
     * @param fereastra Fereastra pe care se lanseaza popup-ul
     */
    public JTextareaPopup(JTextArea textarea) {
        this(textarea,false);
    }

    //~~~~~~~~~~~~~ Creez un Popup pt Tree Oferte  ~~~~
    /**
     * Creeaza popupul asociat Arborelui de navigatie
     * @return Popul creat pentru Tree
     * @author iulian
     */
    private JSimplePopup createTextareaPopup() {
        JSimplePopup popup;
        popup  = new JSimplePopup(textareaListener);

        //Adaug un element la meniu
        //Specifice pentru noduri
        popup.addItem(CUT);
        popup.addItem(COPY);
        popup.addItem(PASTE);

        return popup;
    }

    /**
     * Obtin triggerul pentru popup-uri
     * @return
     */
    public MouseListener getTextareaTrigger() {
        return textareaPopupTrigger;
    }

    //~~~~~~~~~~~~~~~~~~ Controller TreeTable Deviz Popup ~~~~~~~~~~~~~~~~//
    /**
     *  <p style="margin-top: 0">
     *  Clasa ce reprezinta un ascultator pe optiuniile din popup-ul
     *  pentru Arborele de navigatie
     *  </p>
     * @author iulian
     */
    ActionListener textareaListener = new ActionListener() {
        /**
         *  <p style="margin-top: 0">
         *  Invoked when an action occurs.
         *  </p>
         * @author iulian
         */
            public void actionPerformed(ActionEvent e)
            {
                String cmd = e.getActionCommand();
                if (CUT.toLowerCase().equals(cmd)) {
                    textarea.cut();
                } else
                if (COPY.toLowerCase().equals(cmd)) {
                    textarea.copy();
                } else
                if (PASTE.toLowerCase().equals(cmd)) {
                    textarea.paste();
                }
            }
        };

    /**
     *  <p style="margin-top: 0">
     *  Ascultatorul pentru declansarea popup-ului
     *  </p>
     * @author iulian
     */
    MouseListener textareaPopupTrigger = new MouseListener() {
        public void mouseClicked(MouseEvent e) {
            maybeShowPopup(e);
        }

        /**
         * Updatez informatiile
         */
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        /**
         * Verifica daca trebuie afisat popupul
         */
        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                //Verific selectia textului
                if (textarea.getSelectedText() != null) {
                    textareaPopup.setItemEnabled(CUT);
                    textareaPopup.setItemEnabled(COPY);
                    textareaPopup.setItemEnabled(PASTE);
                } else {
                    textareaPopup.setItemDisabled(CUT);
                    textareaPopup.setItemDisabled(COPY);
                    textareaPopup.setItemEnabled(PASTE);
                }

                //Daca este readonly
                if (isReadonly) {
                    textareaPopup.setItemDisabled(CUT);
                    textareaPopup.setItemDisabled(PASTE);
                }

                //Afisez popup-ul
                textareaPopup.show(e.getComponent(),
                           e.getX(), e.getY());
            }
        }

    };
}
