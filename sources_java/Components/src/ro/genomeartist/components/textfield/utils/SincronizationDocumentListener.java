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

package ro.genomeartist.components.textfield.utils;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

/**
 * Clasa se implementeaza generic un ascultator pe editare campuri
 * @author iulian
 */
public abstract class SincronizationDocumentListener implements DocumentListener {
    public void insertUpdate(DocumentEvent e) {
        try {
            String str = e.getDocument().getText(0,
                    e.getDocument().getLength());
            sinc(str);
        } catch (BadLocationException ex) {
        }
    }
    public void removeUpdate(DocumentEvent e) {
        try {
            String str = e.getDocument().getText(0,
                    e.getDocument().getLength());
            sinc(str);
        } catch (BadLocationException ex) {
        }
    }
    public void changedUpdate(DocumentEvent e) {
        try {
            String str = e.getDocument().getText(0, 
                    e.getDocument().getLength());
            sinc(str);
        } catch (BadLocationException ex) {
        }
    }

    /**
     * Metoda apelata pentru sincronizare
     * @param value
     */
    public abstract void sinc(String value);
};
