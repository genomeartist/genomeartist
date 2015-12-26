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

import ro.genomeartist.components.icons.JColorIcon;
import ro.genomeartist.components.propertiespanel.IEditorChangeListener;
import ro.genomeartist.components.propertiespanel.IPropertiesEditor;
import ro.genomeartist.components.propertiespanel.undecided.UndecidedColor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author iulian
 */
public class JColorChooserPropertiesEditor extends JPanel
        implements IPropertiesEditor, ActionListener {
    private Vector<IEditorChangeListener> editorChangeListeners;

    //Variabilele clasei
    private JButton colorButton;
    private JColorIcon colorIcon;
    private JColorChooser colorChooser;

    //Suport pentru culoare nedefinita
    private boolean isUndecided = false;

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Se extind toti constructorii
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    public JColorChooserPropertiesEditor() {
        super();
        this.setLayout(new BorderLayout());

        //Creez componentele
        colorButton = new JButton();
        colorIcon = new JColorIcon(Color.black);
        colorChooser = new JColorChooser();

        //Setez butonul
        colorButton.setIcon(colorIcon);
        colorButton.setHorizontalAlignment(SwingConstants.CENTER);
        colorButton.setBorderPainted(false);
        colorButton.addActionListener(new ActionListener() {
            /**
             * Actiunea efectuata la deschiderea panoului
             */
            public void actionPerformed(ActionEvent e) {
                colorChooser.setColor(colorIcon.getColor());

                //Deschid dialogul de selectie
                JDialog dialog = JColorChooser.createDialog(JColorChooserPropertiesEditor.this,
                            "Pick a Color",
                            true,  //modal
                            colorChooser,
                            JColorChooserPropertiesEditor.this,  //OK button handler
                            null); //no CANCEL button handler
                dialog.setVisible(true);
            }
        });

        //Setez panoul
        this.add(colorButton, BorderLayout.CENTER);

        //Initializez ascultatorii
        editorChangeListeners = new Vector<IEditorChangeListener>();
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
        return colorButton;
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
        Color auxColor;
        UndecidedColor auxUndecidedColor;

        //Setez obiectul in textfield
        if (Color.class == objectClass) {
            auxColor = (Color) renderedObject;
            colorIcon.setColor(auxColor);
        } else
        if (UndecidedColor.class == objectClass) {
            auxUndecidedColor = (UndecidedColor) renderedObject;
            if (auxUndecidedColor.isDecided()) {
                auxColor = auxUndecidedColor.getColor();
            } else {
                auxColor = Color.BLACK;
                setUndecided(true);
            }

            //Setez culoarea
            colorIcon.setColor(auxColor);
        } else
            throw new UnsupportedOperationException(objectClass+" not supported in JColorIcon");
    }


    /**
     * Setez starea de undecided
     */
    private void setUndecided(boolean isUndecided) {
        if (this.isUndecided != isUndecided) {
            this.isUndecided = isUndecided;

            //Setez textul de undecided
            if (this.isUndecided == true) {
                colorButton.setIcon(null);
                colorButton.setText(UndecidedColor.UNDECIDED_STRING);
            } else {
                colorButton.setIcon(colorIcon);
                colorButton.setText("");
            }
        }
    }

    /**
     * Se obtione valoarea din textfield cu cast la clasa specificata
     * @param objectClass
     * @return
     */
    public Object getValue(Class objectClass) {
        Color auxColor;

        //Obtin obiectul din textfield
        if (Color.class == objectClass) {
            auxColor = colorIcon.getColor();
            return auxColor;
        } else
        if (UndecidedColor.class == objectClass) {
            auxColor = colorIcon.getColor();
            return new UndecidedColor(auxColor, isUndecided);
        } else
            throw new UnsupportedOperationException(objectClass+" not supported in JColorIcon");
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *   Managementul actiunilor
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Aici primesc evenimente de la checkbox
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        //Setez noua culoare
        colorIcon.setColor(colorChooser.getColor());
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
