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
import ro.genomeartist.components.textarea.fixedwidth.JFixedWidthTextArea;
import ro.genomeartist.components.utils.StringUtilities;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author iulian
 */
public class JTextAreaPropertiesEditor extends JScrollPane 
        implements IPropertiesEditor, DocumentListener {
    private JTextArea textarea;
    private Vector<IEditorChangeListener> editorChangeListeners;

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Se extind toti constructorii
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    public JTextAreaPropertiesEditor(int rows, int cols) {
        super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textarea = new JFixedWidthTextArea(rows,cols);
        initialize();
    }

    public JTextAreaPropertiesEditor() {
        super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textarea = new JFixedWidthTextArea();
        initialize();
    }

    /**
     * Functie proprie de initializare
     */
    @SuppressWarnings("UseOfObsoleteCollectionType")
    private void initialize() {
        this.setViewportView(textarea);

        //No focus listener because te tab key will erase the values
        textarea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textarea.selectAll();
            }
        });

        //Inregistrez ascultatorii
        editorChangeListeners = new Vector<IEditorChangeListener>();
        textarea.getDocument().addDocumentListener(this);
        
        //Setez focusul
        registerLocalKeyBindings();
    }

    /**
     * Inregistrez key bindingurile in treetable
     */
    private void registerLocalKeyBindings() {

        //   Maparea actiunilor pe nume
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        ActionMap map = textarea.getActionMap();
        
        //Actiunea de schimbare focus
        Action actionReleaseFocus = new AbstractAction("release focus") {
            public void actionPerformed(ActionEvent evt) {
                ((Component)evt.getSource()).transferFocus();
            }
        };
        map.put(actionReleaseFocus.getValue(Action.NAME),
                actionReleaseFocus);


        //   Maparea numelor pe taste
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        InputMap imap = textarea.getInputMap(JComponent.WHEN_FOCUSED);
        imap.put(KeyStroke.getKeyStroke("TAB"),
            actionReleaseFocus.getValue(Action.NAME));
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
        return textarea;
    }

    /**
     * Seteaza numele componentei
     * @param name
     */
    @Override
    public void setEditorName(String name) {
        textarea.setName(name);
    }

    /**
    * Se seteaza valoarea ce se editeaza
    * @param objectClass
    * @param renderedObject
    */
    public void setValue(Class objectClass, Object renderedObject) {
        String auxString;

        //Setez obiectul in textfield
        if (String.class == objectClass) {
            auxString = (String) renderedObject;
            textarea.setText(auxString);
        } else
            throw new UnsupportedOperationException(objectClass+" not supported in JTextArea");
    }

    /**
     * Se obtione valoarea din textarea cu cast la clasa specificata
     * @param objectClass
     * @return
     */
    public Object getValue(Class objectClass) {
        String auxString;

        //Obtin obiectul din textfield
        if (String.class == objectClass) {
            auxString = StringUtilities.removeWindowsCarriageReturn(textarea.getText());
            return auxString;
        } else
            throw new UnsupportedOperationException(objectClass+" not supported in JTextArea");
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *   Managementul actiunilor
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Aici primesc evenimente de la checkbox
     * @param e
     */
    public void insertUpdate(DocumentEvent e) {
        notifyAllListeners();
    }
    public void removeUpdate(DocumentEvent e) {
        notifyAllListeners();
    }
    public void changedUpdate(DocumentEvent e) {
        notifyAllListeners();
    }

    /**
     * Notific toti ascultatorii
     */
    private void notifyAllListeners() {
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
