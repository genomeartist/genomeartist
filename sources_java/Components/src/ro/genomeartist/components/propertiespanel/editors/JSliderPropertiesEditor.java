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
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Construiesc un panou ce tine si afiseaza un combobox
 * @author iulian
 */
public class JSliderPropertiesEditor extends JPanel 
        implements IPropertiesEditor, ChangeListener {
    //Variabile de clasa
    private Collection backingValues;
    
    //Componente
    private JSlider slider;
    private Vector<IEditorChangeListener> editorChangeListeners;

    public JSliderPropertiesEditor(Collection values) {
        super();

        //Pastrez variabilele de clasa
        backingValues = values;
        
        //Creez componenta de editare
        slider = new JSlider(JSlider.HORIZONTAL, 0, backingValues.size() - 1, 0);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        Hashtable labelTable = new Hashtable();
        int counter = 0;
        for (Object backingValue : backingValues) {
            labelTable.put( counter, new JLabel(backingValue.toString()));
            counter ++;
        }
        slider.setLabelTable( labelTable );
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);

        //initializez
        initialize();
    }

    /**
     * Functie de initializare
     */
    private void initialize() {
        //Fac layout-ul
        this.setLayout(new BorderLayout());
        this.add(slider, BorderLayout.CENTER);

        //Inregistrez ascultatorii
        editorChangeListeners = new Vector<IEditorChangeListener>();
        slider.addChangeListener(this);
    }

    @Override
    public void setEnabled(boolean enabled) {
        slider.setEnabled(enabled);
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
        return slider;
    }

    /**
     * Seteaza numele componentei
     * @param name
     */
    @Override
    public void setEditorName(String name) {
        this.setName(name);
        slider.setName(name);
    }

    /**
    * Se seteaza valoarea ce se editeaza
    * @param objectClass
    * @param renderedObject
    */
    public void setValue(Class objectClass, Object renderedObject) {
        if (backingValues.contains(renderedObject)) {
            int counter = 0;
            int foundPosition = 0;
            for (Object object : backingValues) {
                if (object.equals(renderedObject)) {
                    foundPosition = counter;
                }
                counter ++;
            }
            
            //Setez sliderul la pozitia respectiva
            slider.setValue(foundPosition);
        }
    }

    /**
     * Se obtione valoarea din textfield cu cast la clasa specificata
     * @param objectClass
     * @return
     */
    public Object getValue(Class objectClass) {
        int value = slider.getValue();
        return backingValues.toArray()[value];
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *   Managementul actiunilor
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Aici primesc evenimente de la slider
     * @param e
     */
    public void stateChanged(ChangeEvent e) {
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
