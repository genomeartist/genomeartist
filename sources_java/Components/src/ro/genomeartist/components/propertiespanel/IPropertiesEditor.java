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

package ro.genomeartist.components.propertiespanel;

import javax.swing.JComponent;

/**
 * Interfata ce defineste un editor de proprietati
 * @author iulian
 */
public interface IPropertiesEditor {

    /**
     * Obtin containerul global al editorului
     * @return
     */
    public JComponent getContainerComponent();

    /**
     * Obtine componenta care realizeaza editarea propriu-zisa
     * @return
     */
    public JComponent getEditorComponent();

    /**
     * Seteaza numele pentru aceasta componenta
     * @param name
     */
    public void setEditorName(String name);

    /**
     * Seteaza valoarea editata
     * @param objectClass
     * @param renderedObject
     */
    public void setValue(Class objectClass, Object renderedObject);

    /**
     * Obtine valoarea editata facuta cast la clasa corespunzatoare
     * @param objectClass
     * @return
     */
    public Object getValue(Class objectClass);

    /**
     * Adaug un ascultator pentru schimbare valori
     * @param listener
     */
    public void addEditorChangeListener(IEditorChangeListener listener);

    /**
     * Deinregistrez un ascultator pentru schimbare valori
     * @param listener
     */
    public void removeEditorChangeListener(IEditorChangeListener listener);
}
