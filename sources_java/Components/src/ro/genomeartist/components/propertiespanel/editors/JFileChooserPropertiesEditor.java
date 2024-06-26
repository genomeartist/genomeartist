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
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author iulian
 */
public class JFileChooserPropertiesEditor extends JPanel
        implements IPropertiesEditor, DocumentListener {
    private final Frame rootFrame;
    private JTextField textfield;
    private JButton selectButton;
    private JFileChooser fileChooser;
    private Vector<IEditorChangeListener> editorChangeListeners;
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Se extind constructorii
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /**
     * Constructor secundar
     * @param rootFrame
     * @param columns
     */
    public JFileChooserPropertiesEditor(Frame rootFrame, int columns) {
        this(rootFrame, columns, null);
    }

    /**
     * Constructorul principal
     * @param rootFrame
     * @param columns
     * @param fileFilter
     */
    public JFileChooserPropertiesEditor(Frame rootFrame, int columns, FileFilter fileFilter) {
        super();
        this.setLayout(new BorderLayout());

        this.rootFrame = rootFrame;
        this.textfield = new JTextField(columns);
        this.textfield.setEditable(false);
        this.selectButton = new JButton("..");

        //Fac layout-ul
        this.add(textfield, BorderLayout.CENTER);
        this.add(selectButton, BorderLayout.EAST);

        //Fac chooserul
        fileChooser = new JFileChooser();
        if (fileFilter != null) {
            fileChooser.setFileFilter(fileFilter);
            fileChooser.setAcceptAllFileFilterUsed(false);
        }

        //Adaug actiunea de deschidere panou de selectie
        this.selectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Obtin fisierul indicat
                fileChooser.setCurrentDirectory(new File("."));
                int returnVal = fileChooser.showOpenDialog(JFileChooserPropertiesEditor.this.rootFrame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        Image imageFile = ImageIO.read(file);
                        textfield.setText(file.getAbsolutePath());
                    } catch (IOException ex) {
                    }
                }
            }
        });

        //Initializez ascultatorii
        editorChangeListeners = new Vector<IEditorChangeListener>();
        textfield.getDocument().addDocumentListener(this);
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
        File auxFile;

        //Setez obiectul in textfield
        if (File.class == objectClass) {
            auxFile = (File) renderedObject;
            textfield.setText(auxFile.getPath());
            textfield.setHorizontalAlignment(JTextField.LEFT);
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

        //Obtin obiectul din textfield
        if (File.class == objectClass) {
            auxString = textfield.getText();
            return new File(auxString);
        } else
            throw new UnsupportedOperationException(objectClass+" not supported in JTextField");
    }


    /**
     * Activare/Dezactivare editor
     * @param enabled
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        //Propag si in copii
        textfield.setEnabled(enabled);
        selectButton.setEnabled(enabled);
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
