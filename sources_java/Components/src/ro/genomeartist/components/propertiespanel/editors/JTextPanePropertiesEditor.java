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
import ro.genomeartist.components.utils.StringUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author iulian
 */
public class JTextPanePropertiesEditor extends JScrollPane 
        implements IPropertiesEditor, DocumentListener {
    private JTextPane textpane;
    private Vector<IEditorChangeListener> editorChangeListeners;

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Se extind toti constructorii
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public JTextPanePropertiesEditor(int width, int height) {
        super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textpane = new JTextPane();
        textpane.setPreferredSize(new Dimension(width,height));
        textpane.setMinimumSize(new Dimension(width,height));
        
        initialize();
    }


    public JTextPanePropertiesEditor() {
        super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textpane = new JTextPane();

        initialize();
    }

    /**
     * Functie proprie de initializare
     */
    private void initialize() {
        this.setViewportView(textpane);
        textpane.setBorder(BorderFactory.createLoweredBevelBorder());
        textpane.setBackground(Color.WHITE);

        StyledDocument styleDocument = new DefaultStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        styleDocument.setParagraphAttributes(0, styleDocument.getLength(), center, false);
        textpane.setDocument(styleDocument);

        //No focus listener because te tab key will erase the values
//        textarea.addFocusListener(new FocusAdapter() {
//            @Override
//            public void focusGained(FocusEvent e) {
//                textarea.selectAll();
//            }
//        });

        //Inregistrez ascultatorii
        editorChangeListeners = new Vector<IEditorChangeListener>();
        textpane.getDocument().addDocumentListener(this);
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
        return textpane;
    }

    /**
     * Seteaza numele componentei
     * @param name
     */
    @Override
    public void setEditorName(String name) {
        textpane.setName(name);
    }

    /**
    * Se seteaza valoarea ce se editeaza
    * @param objectClass
    * @param renderedObject
    */
    public void setValue(Class objectClass, Object renderedObject) {
        String auxString;

        //Setez obiectul in textfield
        try {
            if (String.class == objectClass) {
                auxString = (String) renderedObject;
                StyledDocument styleDocument = new DefaultStyledDocument();
                AttributeSet attributeSet = textpane.getParagraphAttributes();

                styleDocument.insertString(0, auxString, null);
                styleDocument.setParagraphAttributes(0, styleDocument.getLength(),
                        attributeSet, false);
                textpane.setDocument(styleDocument);
            } else
            throw new UnsupportedOperationException(objectClass+" not supported in JTextArea");
        } catch (BadLocationException ex) {
            Logger.getLogger(JTextPanePropertiesEditor.class.getName()).log(Level.WARNING, 
                    "bad location", ex);
        }
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
            auxString = StringUtilities.removeWindowsCarriageReturn(textpane.getText());
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
