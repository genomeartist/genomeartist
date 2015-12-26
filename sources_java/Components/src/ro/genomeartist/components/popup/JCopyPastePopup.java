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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 *
 * @author iulian
 */
public class JCopyPastePopup {
    private JComponent targetComponent;
    
    //Popup-urile ce se afiseaza
    private JConfigurablePopup configurablePopup; //popup pentru devizul obiect cu selectie simpla

    /**
     * Constructorul principal
     * @param fereastra Fereastra pe care se lanseaza popup-ul
     */
    public JCopyPastePopup() {
        targetComponent = null;
        
        //Creez popup-urile
        configurablePopup = createPopup();
    }

    //~~~~~~~~~~~~~ Creez un Popup pt Deviz Obiect  ~~~~
    /**
     * Creeaza popupul pentru selectie singulara asociat Devizului general
     * @return Popul creat pentru Deviz general
     * @author iulian
     */
    private JConfigurablePopup createPopup() {
        JConfigurablePopup popup;
        popup  = new JConfigurablePopup(popupListener);

        //Adaug un element la meniu
        popup.addItem(CopyPastePopupManager.NAME_CUT,CopyPastePopupManager.getCutIcon(), 
                KeyStroke.getKeyStroke("ctrl X"));
        popup.addItem(CopyPastePopupManager.NAME_COPY,CopyPastePopupManager.getCopyIcon(), 
                KeyStroke.getKeyStroke("ctrl C"));
        popup.addItem(CopyPastePopupManager.NAME_PASTE,CopyPastePopupManager.getPasteIcon(), 
                KeyStroke.getKeyStroke("ctrl V"));
        
        return popup;
    }

    /**
     * Obtin triggerul pentru popup-uri
     * @return
     */
    public MouseListener getPopupTrigger() {
        return popupTrigger;
    }

    //~~~~~~~~~~~~~~~~~~ Controller Deviz Obiect Popup ~~~~~~~~~~~~~~~~//
    /**
     *  <p style="margin-top: 0">
     *  Clasa ce reprezinta un ascultator pe optiuniile din popup-ul
     *      pentru TreeTable
     *  </p>
     * @author iulian
     */
    ActionListener popupListener = new ActionListener() {
        /**
         *  <p style="margin-top: 0">
         *  Invoked when an action occurs.
         *  </p>
         * @author iulian
         */
            public void actionPerformed(ActionEvent e)
            {
                String cmd = e.getActionCommand();
                if (CopyPastePopupManager.recognizeCommand(cmd)) {
                    CopyPastePopupManager.handleEvent(e, targetComponent);
                }
            }
        };

    /**
     *  <p style="margin-top: 0">
     *  Ascultatorul pentru declansarea popup-ului
     *  </p>
     * @author iulian
     */
    MouseListener popupTrigger = new MouseListener() {
        public void mouseClicked(MouseEvent e) {
            maybeShowPopup(e);
        }

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
         * Sincronizeaza popup-ul astfel incat sa aiba enabled
         * doar optiunile necesare
         */
        private void syncronizePopup() {
            //Actualizez si itemurile de copy-paste
            if (CopyPastePopupManager.isCutEnabled(targetComponent))
                configurablePopup.setItemEnabled(CopyPastePopupManager.NAME_CUT);
            else configurablePopup.setItemDisabled(CopyPastePopupManager.NAME_CUT);
            if (CopyPastePopupManager.isCopyEnabled(targetComponent))
                configurablePopup.setItemEnabled(CopyPastePopupManager.NAME_COPY);
            else configurablePopup.setItemDisabled(CopyPastePopupManager.NAME_COPY);
            if (CopyPastePopupManager.isPasteEnabled(targetComponent))
                configurablePopup.setItemEnabled(CopyPastePopupManager.NAME_PASTE);
            else configurablePopup.setItemDisabled(CopyPastePopupManager.NAME_PASTE);
        }

        /**
         * Verifica daca trebuie afisat popupul
         */
        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                if (e.getComponent() instanceof JComponent) {
                    targetComponent = (JComponent) e.getComponent();
                }
                
                //Verific tipul selectiei si activez / dezactivez anumite optiuni din popup
                syncronizePopup();

                //Afisez popup-ul
                configurablePopup.show(e.getComponent(),
                           e.getX(), e.getY());
            }
        }

    };
}
