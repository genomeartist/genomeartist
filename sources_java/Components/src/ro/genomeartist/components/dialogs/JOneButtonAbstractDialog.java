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

package ro.genomeartist.components.dialogs;

import java.awt.Frame;

/**
 *
 * @author iulian
 */
public abstract class JOneButtonAbstractDialog extends JTwoButtonAbstractDialog {

    /**
     * Contructor secundar
     * @param owner
     * @param title
     * @param modal
     */
    public JOneButtonAbstractDialog(Frame owner, String title, boolean modal) {
        this(owner, title, modal, JTwoButtonAbstractDialog.TEXT_OK);
    }    

    /**
     * Copnstructor principal
     * @param owner
     * @param title
     * @param modal
     * @param text_ok
     */
    public JOneButtonAbstractDialog(Frame owner, String title, boolean modal, String text_ok) {
        super(owner, title, modal, text_ok, JTwoButtonAbstractDialog.TEXT_CANCEL);
        buttonCancel.setVisible(false);
    }

    
    public abstract void fireActionOk();

    @Override
    public void fireActionCancel() {
        fireActionOk();
    }
}
