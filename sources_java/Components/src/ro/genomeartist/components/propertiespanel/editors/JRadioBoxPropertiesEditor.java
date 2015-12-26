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

package ro.genomeartist.components.propertiespanel.editors;

import ro.genomeartist.components.propertiespanel.IEditorChangeListener;
import ro.genomeartist.components.propertiespanel.IPropertiesEditor;
import ro.genomeartist.components.propertiespanel.undecided.UndecidedBoolean;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JRadioButton;

/**
 *
 * @author iulian
 */
public class JRadioBoxPropertiesEditor extends JRadioButton
        implements IPropertiesEditor, ItemListener {
    private Vector<IEditorChangeListener> editorChangeListeners;

    //Suportul pentru boolean undecided
    private boolean isUndecided;
    private String checkBoxText;


    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Se extind toti constructorii
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    public JRadioBoxPropertiesEditor(String text, Icon icon, boolean selected) {
        super(text, icon, selected);
        initialize();
    }

    public JRadioBoxPropertiesEditor(String text, Icon icon) {
        super(text, icon);
        initialize();
    }

    public JRadioBoxPropertiesEditor(String text, boolean selected) {
        super(text, selected);
        initialize();
    }

    public JRadioBoxPropertiesEditor(Action a) {
        super(a);
        initialize();
    }

    public JRadioBoxPropertiesEditor(String text) {
        super(text);
        initialize();
    }

    public JRadioBoxPropertiesEditor(Icon icon, boolean selected) {
        super(icon, selected);
        initialize();
    }

    public JRadioBoxPropertiesEditor(Icon icon) {
        super(icon);
        initialize();
    }

    public JRadioBoxPropertiesEditor() {
        initialize();
    }

    /**
     * Functie de initializare
     */
    private void initialize() {
        editorChangeListeners = new Vector<IEditorChangeListener>();
        this.addItemListener(this);
        this.checkBoxText = this.getText();
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Se implementeaza interfata IPropertiesEditor
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Obtin containerul global al editorului
     * @return
     */
    public JComponent getContainerComponent() {
        return this;
    }

    /**
     * Obtine componenta care realizeaza editarea propriu-zisa
     * @return
     */
    public JComponent getEditorComponent() {
        return this;
    }

    /**
     * Seteaza numele componentei
     * @param name
     */
    @Override
    public void setEditorName(String name) {
        this.setName(name);
    }

    /**
    * Se seteaza valoarea ce se editeaza
    * @param objectClass
    * @param renderedObject
    */
    public void setValue(Class objectClass, Object renderedObject) {
        Boolean auxBoolean;
        UndecidedBoolean auxUndecidedBoolean;

        //Setez obiectul in textfield
        if (Boolean.class == objectClass) {
            auxBoolean = (Boolean) renderedObject;
            this.setSelected(auxBoolean);
        } else
        if (UndecidedBoolean.class == objectClass) {
            auxUndecidedBoolean = (UndecidedBoolean) renderedObject;
            auxBoolean = auxUndecidedBoolean.getBoolean();
            this.setSelected(auxBoolean);

            //Afisez starea
            setUndecided(auxUndecidedBoolean.isUndecided());
        } else
            throw new UnsupportedOperationException(objectClass+" not supported in JRadioButton");
    }

    /**
     * Se obtione valoarea din textfield cu cast la clasa specificata
     * @param objectClass
     * @return
     */
    public Object getValue(Class objectClass) {
        Boolean auxBoolean;

        //Obtin obiectul din textfield
        if (Boolean.class == objectClass) {
            auxBoolean = Boolean.valueOf(this.isSelected());
            return auxBoolean;
        } else
        if (UndecidedBoolean.class == objectClass) {
            auxBoolean = Boolean.valueOf(this.isSelected());
            return new UndecidedBoolean(auxBoolean, isUndecided);
        } else
            throw new UnsupportedOperationException(objectClass+" not supported in JRadioButton");
    }

    /**
     * Seteaza campul undecided
     * @param isUndecided
     */
    private void setUndecided(boolean isUndecided) {
        if (this.isUndecided != isUndecided) {
            this.isUndecided = isUndecided;

            if (isUndecided) {
                this.setText(UndecidedBoolean.UNDECIDED_STRING + " " + checkBoxText );
            } else {
                this.setText(checkBoxText);
            }
        }
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *   Managementul actiunilor
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Aici primesc evenimente de la checkbox
     * @param e
     */
    public void itemStateChanged(ItemEvent e) {
        //Specific faptul ca nu mai este indecisa optiunea
        setUndecided(false);

        //Notific ascultatorii
        Iterator<IEditorChangeListener> iterator = editorChangeListeners.iterator();
        while (iterator.hasNext()) {
            IEditorChangeListener iEditorChangeListener = iterator.next();
            iEditorChangeListener.fireValueChanged();
        }
    }

    /**
     * Adaug un ascultator pentru schimbare valori
     * @param listener
     */
    public void addEditorChangeListener(IEditorChangeListener listener) {
        editorChangeListeners.add(listener);
    }

    /**
     * Deinregistrez un ascultator pentru schimbare valori
     * @param listener
     */
    public void removeEditorChangeListener(IEditorChangeListener listener) {
        editorChangeListeners.remove(listener);
    }
}
