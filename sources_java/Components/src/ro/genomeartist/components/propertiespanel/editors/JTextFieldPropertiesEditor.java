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
import ro.genomeartist.components.propertiespanel.undecided.UndecidedBigDecimal;
import ro.genomeartist.components.propertiespanel.undecided.UndecidedInteger;
import ro.genomeartist.components.textfield.fixedwidth.JFixedWidthTextField;
import ro.genomeartist.components.utils.NumberUtils;
import ro.genomeartist.components.utils.StringUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author iulian
 */
public class JTextFieldPropertiesEditor extends JPanel
        implements IPropertiesEditor, DocumentListener {
    private JTextField textfield;
    private JLabel endingLabel;
    private Vector<IEditorChangeListener> editorChangeListeners;

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Se extind constructorii
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public JTextFieldPropertiesEditor(int width) {
        this(width, "");
    }

    public JTextFieldPropertiesEditor(int width, String endingText, int labelWidth) {
        super();
        textfield = new JFixedWidthTextField(width);
        endingLabel = new JLabel(endingText);
        endingLabel.setPreferredSize(new Dimension(labelWidth, 0));
        initialize();
    }

    public JTextFieldPropertiesEditor(int width, String endingText) {
        super();
        textfield = new JFixedWidthTextField(width);
        endingLabel = new JLabel(endingText);
        initialize();
    }

    public JTextFieldPropertiesEditor(String endingText, int labelWidth) {
        super();
        textfield = new JFixedWidthTextField();
        endingLabel = new JLabel(endingText);
        endingLabel.setPreferredSize(new Dimension(labelWidth, 0));
        initialize();
    }

    public JTextFieldPropertiesEditor(String endingText) {
        super();
        textfield = new JFixedWidthTextField();
        endingLabel = new JLabel(endingText);
        initialize();
    }

    /**
     * Functie proprie de initializare
     */
    private void initialize() {
        //Fac layout-ul
        this.setLayout(new BorderLayout());
        this.add(textfield, BorderLayout.CENTER);
        this.add(endingLabel, BorderLayout.EAST);

        //Pun o margnie la label
        endingLabel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));

        //Adaug un ascultator de focus
        textfield.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textfield.selectAll();
            }
        });

        //Inregistrez ascultatorii
        editorChangeListeners = new Vector<IEditorChangeListener>();
        textfield.getDocument().addDocumentListener(this);
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Setter/getter pentru ending label
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/    
    /**
     * Obtin textul din label
     * @return
     */
    public String getEndingText() {
        return endingLabel.getText();
    }

    /**
     * Setez textul din label
     * @param endingText
     */
    public void setEndingText(String endingText) {
        this.endingLabel.setText(endingText);
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
        return textfield;
    }

    /**
     * Seteaza numele componentei
     * @param name
     */
    @Override
    public void setEditorName(String name) {
        this.setName(name);
        textfield.setName(name);
    }

    /**
    * Se seteaza valoarea ce se editeaza
    * @param objectClass
    * @param renderedObject
    */
    public void setValue(Class objectClass, Object renderedObject) {
        String auxString;
        Integer auxInteger;
        BigDecimal auxBigDecimal;
        UndecidedBigDecimal auxUndecidedBigDecimal;
        UndecidedInteger auxUndecidedInteger;

        //Setez obiectul in textfield
        if (String.class == objectClass) {
            auxString = (String) renderedObject;
            textfield.setText(auxString);
            textfield.setHorizontalAlignment(JTextField.LEFT);
        } else
        if (Integer.class == objectClass) {
            auxInteger = (Integer) renderedObject;
            textfield.setText(auxInteger.toString());
            textfield.setHorizontalAlignment(JTextField.RIGHT);
        } else
        if (BigDecimal.class == objectClass) {
            auxBigDecimal = (BigDecimal) renderedObject;
            auxBigDecimal = auxBigDecimal.stripTrailingZeros();
            textfield.setText(auxBigDecimal.toPlainString());
            textfield.setHorizontalAlignment(JTextField.RIGHT);
        } else
        if (UndecidedBigDecimal.class == objectClass) {
            auxUndecidedBigDecimal = (UndecidedBigDecimal) renderedObject;
            textfield.setText(auxUndecidedBigDecimal.toString());
            textfield.setHorizontalAlignment(JTextField.RIGHT);
        } else
        if (UndecidedInteger.class == objectClass) {
            auxUndecidedInteger = (UndecidedInteger) renderedObject;
            textfield.setText(auxUndecidedInteger.toString());
            textfield.setHorizontalAlignment(JTextField.RIGHT);
        } else
            throw new UnsupportedOperationException(objectClass+" not supported in JTextField");
    }

    /**
     * Se obtione valoarea din textfield cu cast la clasa specificata
     * @param objectClass
     * @return
     */
    public Object getValue(Class objectClass) {
        String auxString;
        Integer auxInteger;
        BigDecimal auxBigDecimal;
        UndecidedBigDecimal auxUndecidedBigDecimal;
        UndecidedInteger auxUndecidedInteger;

        //Obtin obiectul din textfield
        if (String.class == objectClass) {
            auxString = StringUtilities.removeWindowsCarriageReturn(textfield.getText());
            return auxString;
        } else
        if (Integer.class == objectClass) {
            auxString = textfield.getText();
            
            //Obtin numarul
            auxInteger = NumberUtils.toInteger(auxString);
            if (auxInteger == null) {
                auxInteger = Integer.valueOf(0);
            }

            return auxInteger;
        } else
        if (BigDecimal.class == objectClass) {
            auxString = textfield.getText();

            //Obtin numarul
            auxBigDecimal = NumberUtils.toBigDecimal(auxString);
            if (auxBigDecimal == null) {
                auxBigDecimal = BigDecimal.ZERO;
            }

            return auxBigDecimal;
        } else
        if (UndecidedBigDecimal.class == objectClass) {
            auxString = textfield.getText();
            auxUndecidedBigDecimal = new UndecidedBigDecimal(auxString);
            return auxUndecidedBigDecimal;
        } else
        if (UndecidedBigDecimal.class == objectClass) {
            auxString = textfield.getText();
            auxUndecidedInteger = new UndecidedInteger(auxString);
            return auxUndecidedInteger;
        } else
            throw new UnsupportedOperationException(objectClass+" not supported in JTextField");
    }

    /**
     * Activez/dezactivez textfielul
     * @param enabled
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        //Notific si copii
        textfield.setEnabled(enabled);
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
