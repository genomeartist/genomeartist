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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author iulian
 */
public class JButtonPropertiesEditor extends JPanel
        implements IPropertiesEditor, ActionListener {
    private Vector<IEditorChangeListener> editorChangeListeners;

    //Variabilele clasei
    private JButton mainButton;
    private Runnable runnable;
    private String textfield;

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Constructori
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Creez un editor cu button cu textul specificat
     * @param buttonText 
     */
    public JButtonPropertiesEditor(String buttonText) {
        super();
        JButton button =  new JButton();
        button.setText(buttonText);
        init(button);
    }
    
    /**
     * Construiesc un editor folosind un buton specificat de utilizator
     * @param button 
     */
    public JButtonPropertiesEditor(JButton button) {
        super();
        init(button);
    }
    
    /**
     * Creez un editor cu button cu textul si ascultatorul specificat
     * @param buttonText 
     */
    public JButtonPropertiesEditor(String buttonText, ActionListener listener) {
        super();
        JButton button =  new JButton();
        button.setText(buttonText);
        init(button, listener);
    }
    
    /**
     * Functia de initializare
     * @param button 
     */
    private void init(JButton button) {
        this.setLayout(new BorderLayout());

        //Creez componentele
        mainButton = button;
        runnable = null;
        textfield = "";

        //Setez butonul
        mainButton.setHorizontalAlignment(SwingConstants.CENTER);
        mainButton.addActionListener(this);

        //Setez panoul
        this.add(mainButton, BorderLayout.CENTER);

        //Initializez ascultatorii
        editorChangeListeners = new Vector<IEditorChangeListener>();
    }
    
    /**
     * Initializare cu event listener
     */
    private void init(JButton button, ActionListener listener) {
        this.setLayout(new BorderLayout());

        //Creez componentele
        mainButton = button;
        runnable = null;
        textfield = "";

        //Setez butonul
        mainButton.setHorizontalAlignment(SwingConstants.CENTER);
        
        editorChangeListeners = new Vector<IEditorChangeListener>();
        mainButton.addActionListener(listener);

        //Setez panoul
        this.add(mainButton, BorderLayout.EAST);

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
        return mainButton;
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
        Runnable auxRunnable;

        //Setez obiectul in textfield
        if (Runnable.class == objectClass) {
            auxRunnable = (Runnable) renderedObject;
            runnable = auxRunnable;
        } else if (String.class == objectClass) 
            textfield = (String) renderedObject;
        else
            throw new UnsupportedOperationException(objectClass+" not supported in JColorIcon");
    }

    /**
     * Se obtione valoarea din textfield cu cast la clasa specificata
     * @param objectClass
     * @return
     */
    public Object getValue(Class objectClass) {
        //Obtin obiectul din textfield
        if (Runnable.class == objectClass)
            return runnable;
        else if (String.class == objectClass)
            return textfield;
        else
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
        //Execut actiunea specificata
        if (runnable != null)
            runnable.run();

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
