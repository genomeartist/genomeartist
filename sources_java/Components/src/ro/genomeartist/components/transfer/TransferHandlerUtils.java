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

package ro.genomeartist.components.transfer;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 *
 * @author iulian
 */
public class TransferHandlerUtils {

    /**
     * Testeaza daca o componenta este buna pentru copy
     * @param targetComponent
     * @return
     */
    public static boolean isCutEnabled(JComponent targetComponent) {
        TransferHandler transferHandler = targetComponent.getTransferHandler();
        if (transferHandler == null)
            return false;

        int sourceActions = transferHandler.getSourceActions(targetComponent);
        if (sourceActions == TransferHandler.COPY ||
            sourceActions == TransferHandler.COPY_OR_MOVE ||
            sourceActions == TransferHandler.MOVE)
            return true;
        else return false;
    }

    /**
     * Testeaza daca o componenta este buna pentru copy
     * @param targetComponent
     * @return
     */
    public static boolean isCopyEnabled(JComponent targetComponent) {
        TransferHandler transferHandler = targetComponent.getTransferHandler();
        if (transferHandler == null)
            return false;

        int sourceActions = transferHandler.getSourceActions(targetComponent);
        if (sourceActions == TransferHandler.COPY || 
                sourceActions == TransferHandler.COPY_OR_MOVE)
            return true;
        else return false;
    }

    /**
     * Testeaza daca o componenta este buna pentru copy
     * @param targetComponent
     * @return
     */
    public static boolean isPasteEnabled(JComponent targetComponent) {
        Clipboard clipboard = getClipboard(targetComponent);
        Transferable trans = null;
        if (clipboard != null)
            trans = clipboard.getContents(null);

        //Creez suport pentru paste
        TransferHandler.TransferSupport transferSupport =
                new TransferHandler.TransferSupport(targetComponent, trans);
        TransferHandler transferHandler = targetComponent.getTransferHandler();

        //Verific pachetul cu componenta
        if (transferHandler != null && trans != null)
            return targetComponent.getTransferHandler().canImport(transferSupport);
        else return false;
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Utils
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Returns the clipboard to use for cut/copy/paste.
     */
    private static Clipboard getClipboard(JComponent c) {
        return Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    /**
     * Key used in app context to lookup Clipboard to use if access to
     * System clipboard is denied.
     */
    private static Object SandboxClipboardKey = new Object();
}
