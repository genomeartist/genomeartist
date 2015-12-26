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

import ro.genomeartist.components.transfer.TransferHandlerUtils;
import java.awt.event.ActionEvent;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 * Managerul pentru operatiile de undo redo
 * @author iulian
 */
public class CopyPastePopupManager {
    //Numele actiunilor (vor aparea pe popup)
    public static final String NAME_CUT = "Cut";
    public static final String NAME_COPY = "Copy";
    public static final String NAME_PASTE = "Paste";

    //Numele actiuniilor
    public static final String ACTION_CUT = NAME_CUT.toLowerCase();
    public static final String ACTION_COPY = NAME_COPY.toLowerCase();
    public static final String ACTION_PASTE = NAME_PASTE.toLowerCase();

    //Singleton pentru popup
    private static JCopyPastePopup copyPastePopup = null;

    /**
     * No constructor
     */
    private CopyPastePopupManager() {
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *   Obiecte singleton
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Static initialization
     * @return
     */
    public static JCopyPastePopup getCopyPastePopup() {
        if (copyPastePopup == null)
            copyPastePopup = new JCopyPastePopup();
        
        return copyPastePopup;
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Obtinerea icoanelor
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Obtin iconul pentru cut
     * @return
     */
    public static Icon getCutIcon() {
        return JCopyPasteIcons.CUT.getIcon();
    }

    /**
     * Obtin iconul pentru cut
     * @return
     */
    public static Icon getCopyIcon() {
        return JCopyPasteIcons.COPY.getIcon();
    }

    /**
     * Obtin iconul pentru cut
     * @return
     */
    public static Icon getPasteIcon() {
        return JCopyPasteIcons.PASTE.getIcon();
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Actualizarea butoanelor
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Testeaza daca o componenta este buna pentru copy
     * @param targetComponent
     * @return
     */
    public static boolean isCutEnabled(JComponent targetComponent) {
        return TransferHandlerUtils.isCutEnabled(targetComponent);
    }

    /**
     * Testeaza daca o componenta este buna pentru copy
     * @param targetComponent
     * @return
     */
    public static boolean isCopyEnabled(JComponent targetComponent) {
        return TransferHandlerUtils.isCopyEnabled(targetComponent);
    }

    /**
     * Testeaza daca o componenta este buna pentru copy
     * @param targetComponent
     * @return
     */
    public static boolean isPasteEnabled(JComponent targetComponent) {
        return TransferHandlerUtils.isPasteEnabled(targetComponent);
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Prelucrarea evenimentelor
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Functie ce va face recunoasterea unei comenzi Dnd
     */
    public static boolean recognizeCommand(String cmd) {
        if (ACTION_COPY.equals(cmd) || ACTION_PASTE.equals(cmd) || ACTION_CUT.equals(cmd))
            return true;
        else return false;
    }

    /**
     * Actionez asupra evenimentului forwardat catre manager
     */
    public static void handleEvent(ActionEvent e, JComponent targetComponent) {
        String cmd = e.getActionCommand();
        if (ACTION_CUT.equals(cmd)) {
            //Copy event
            ActionEvent copyEvent = new ActionEvent(targetComponent, 1,"cut");
            TransferHandler.getCutAction().actionPerformed(copyEvent);
        } else
        if (ACTION_COPY.equals(cmd)) {
            //Copy event
            ActionEvent copyEvent = new ActionEvent(targetComponent, 1,"copy");
            TransferHandler.getCopyAction().actionPerformed(copyEvent);
        } else
        if (ACTION_PASTE.equals(cmd)) {
            //Paste event
            ActionEvent pasteEvent = new ActionEvent(targetComponent, 1,"paste");
            TransferHandler.getPasteAction().actionPerformed(pasteEvent);
        }
    }

}
