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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Clasa reprezinta un panou pentru afisarea proprietatiilor unui obiect
 * @author iulian
 */
public class JPropertiesPanel extends JPanel implements ChangeListener {
    //Constante pentru tipuri de componente
    public static final int TEXTFIELD = 0;
    public static final int TEXTAREA = 1;

    //Constante interne
    private static final int PADDING_HEIGHT = 10;
    private static final int PADDING_TOP = 5;
    private static final int PADDING_LEFT = 5;
    private static final int PADDING_RIGHT = 4;
    private static final int PADDING_BOTTOM = 10;
    private static final int COMPONENTS_SPACER = 3;

    //Componente de baza
    private AbstractPropertiesPanelModel propertiesModel;       //Modelul
    private ArrayList<IPropertiesEditor> propertiesEditorVector;   //Componentele editoare
    private Map<Object,GridBagConstraints> gridBagConstraintsHash;    //Vectorul de preferinte de desenare
    private ArrayList<IEditorChangeListener> editorChangeListeners;    //Vector de ascultatori pe problemele editorilor
    private boolean[] changeTracker;    //Vectorul ce monitorizeaza schimbarile valorilor
    private GridBagConstraints fillConstraint; //Contrainturi pentru umplerea completa a spatiului

    //Structura ferestrei
    private ValidationPanel validationPanel;
        private JPanel innerPanel;


    /**
     * Clasa se construieste pe baza unui model
     * @param propertiesModel
     */
    public JPropertiesPanel(AbstractPropertiesPanelModel propertiesModel) {
        super();
        this.setLayout(new BorderLayout());

        //Creez panoul de validare
        this.validationPanel = new ValidationPanel();
        this.changeTracker = null;

        //Creez panoul cu date
        this.innerPanel = new JPanel();
        innerPanel.setLayout(new GridBagLayout());
        innerPanel.setBorder(BorderFactory.createEmptyBorder(
                PADDING_TOP, PADDING_LEFT, PADDING_BOTTOM, PADDING_RIGHT));

        //Componentele clasei
        this.propertiesModel = propertiesModel;
        this.propertiesEditorVector = new ArrayList<IPropertiesEditor>();
        this.gridBagConstraintsHash = new HashMap<Object, GridBagConstraints>();
        this.editorChangeListeners = new ArrayList<IEditorChangeListener>();
        computeGridBagConstraints();
        
        //Obtin setarile din model
        int labelWidth = propertiesModel.getLabelWidth();        

        //Iterez prin model si creez panoul
        for (int i = 0; i < propertiesModel.getRowCount(); i++) {
            //Incerc sa ii adaug headerul
            if (propertiesModel.getHeaderComponentAt(i) != null) {
                IPropertiesHeader propertiesHeader = propertiesModel.getHeaderComponentAt(i);
                innerPanel.add(propertiesHeader.getContainerComponent(),
                        gridBagConstraintsHash.get(propertiesHeader));
            }

            //Determin componenta activa
            IPropertiesEditor propertiesEditor = propertiesModel.getEditorComponentAt(i);
            Class objectClass = propertiesModel.getClassAt(i);
            Object objectValue = propertiesModel.getValueAt(i);
            int rowHeight = propertiesModel.getRowHeightAt(i);

            //Setez valoarea indicata
            propertiesEditor.setValue(objectClass, objectValue);
            propertiesEditor.setEditorName(propertiesModel.getDescriptionAt(i));

            //Creez si adaug linia de proprietati
            propertiesEditorVector.add(propertiesEditor);
            JPanel rowPanel = getPanelForRow(propertiesModel.getDescriptionAt(i),
                    labelWidth, rowHeight, propertiesEditor.getContainerComponent());
            innerPanel.add(rowPanel,gridBagConstraintsHash.get(propertiesEditor));

            //Leg componenta la sistemul de evenimente
            IEditorChangeListener editorChangeListener = new LocalEditorChangeListener(i);
            propertiesEditor.addEditorChangeListener(editorChangeListener);
            editorChangeListeners.add(editorChangeListener);
        }

        //Make the window expand by filling the remaining space
        innerPanel.add(Box.createVerticalStrut(PADDING_HEIGHT),fillConstraint);

        //Fac structura principala
        validationPanel.setInnerComponent(innerPanel);
        this.add(validationPanel, BorderLayout.CENTER);
    }

    /**
     * Calculez vectorul de contrainturi
     */
    private void computeGridBagConstraints() {
        //Identitic numarul maxim de coloane
        int maxCol = 1;
        int numCol = 0;
        int[] columnStats = new int[propertiesModel.getRowCount()];

        // <editor-fold defaultstate="collapsed" desc="Calculez numarul de coloane">
        int lastRow = propertiesModel.getRowCount()-1;  //Ultimul rand
        int lastNewline = 0;                            //Ultimul newline
        for (int i = 0; i < propertiesModel.getRowCount(); i++) {
            numCol++;

            //Pastrez maximul de coloane
            if (numCol > maxCol) {
                maxCol = numCol;
            }

            //Verific daca pe acest rand se termina linia
            if ((i != lastRow && propertiesModel.isRowOnNewline(i+1)) ||
                (i == lastRow)) {
                //Pentru linia curenta salvez numarul de coloane
                for( int j = lastNewline; j<=i; j++ )
                    columnStats[j] = numCol;

                numCol = 0;
                lastNewline = i+1;
            }
        }
        // </editor-fold>

        //Compun vectorul de constraint-uri
        GridBagConstraints elementConstraints;
        int countRow = -1; //Tracks the rows
        int countCols = 0;
        for (int i = 0; i < propertiesModel.getRowCount(); i++) {
            if (propertiesModel.isRowOnNewline(i)) {
                countRow++;
                countCols = 0;
            }

            //Iau in considerare cazul in care am header
            if (propertiesModel.getHeaderComponentAt(i) != null) {
                //Contruiesc grid bag contraint-ul
                elementConstraints = new GridBagConstraints();
                elementConstraints.fill = GridBagConstraints.HORIZONTAL;
                elementConstraints.anchor = GridBagConstraints.LINE_START;
                elementConstraints.weightx = 1;
                elementConstraints.gridy = countRow;
                elementConstraints.gridx = 0;
                elementConstraints.gridwidth = maxCol;

                //Adaug la vector
                gridBagConstraintsHash.put(propertiesModel.getHeaderComponentAt(i),elementConstraints);

                //Incrementez randul
                countRow ++;
            }


            //Variabilele pentru dimensiunea celulelor
            int colsOnRow = columnStats[i];
            int gridsForCell = maxCol/colsOnRow;
            int rightLeftover = maxCol % colsOnRow;

            //Contruiesc grid bag contraint-ul
            elementConstraints = new GridBagConstraints();
            elementConstraints.fill = GridBagConstraints.HORIZONTAL;
            if (countCols == 0)
                elementConstraints.anchor = GridBagConstraints.LINE_START;
            else if (countCols == (colsOnRow-1))
                elementConstraints.anchor = GridBagConstraints.LINE_END;
            elementConstraints.weightx = colsOnRow/((double)maxCol);
            elementConstraints.gridy = countRow;
            elementConstraints.gridx = countCols*gridsForCell;
            elementConstraints.gridwidth = gridsForCell;
            if (countCols == (colsOnRow-1))
                elementConstraints.gridwidth += rightLeftover;

            //Adaug la vector
            gridBagConstraintsHash.put(propertiesModel.getEditorComponentAt(i),elementConstraints);

            //Incrementez indicele de coloana
            countCols++;

            //Afisez de test
//            System.out.println("i: "+i+" ## ["+elementConstraints.gridy+","+
//                    elementConstraints.gridx+"]-> "+elementConstraints.gridwidth);
        }

        //Calculez constraintul de umplere spatiu ramas
        countRow ++;
        fillConstraint = new GridBagConstraints();
        fillConstraint.fill = GridBagConstraints.BOTH;
        fillConstraint.anchor = GridBagConstraints.LINE_START;
        fillConstraint.weightx = 1;
        fillConstraint.weighty = 1;
        fillConstraint.gridy = countRow;
        fillConstraint.gridx = 0;
        fillConstraint.gridwidth = maxCol;
    }

    /**
     * Obtin panoul pentru un rand dintr-o linie de proprietati
     * @param descriptionText  Textul pentru label
     * @param labelWidth Diemensiunea labelului
     * @param component  Componenta activa
     * @return Un panel cu aranjarea acestora
     */
    private JPanel getPanelForRow(String descriptionText, 
            int labelWidth, int boxHeight, JComponent component) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        //Inaltime si padding left
        panel.add(Box.createRigidArea(new Dimension(COMPONENTS_SPACER, boxHeight)));

        //Labelul de descriere
        if (descriptionText != null) {
            JLabel descriptionLabel = new JLabel(descriptionText);
            descriptionLabel.setPreferredSize(new Dimension(labelWidth, 0));
            descriptionLabel.setMinimumSize(new Dimension(labelWidth, 0));
            panel.add(descriptionLabel);
        }

        //Componenta activa
        panel.add(component);
        return panel;
    }    

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Actiuni asupra panoului
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Obtin modelul din spatele panoului
     * @return
     */
    public AbstractPropertiesPanelModel getModel() {
        return propertiesModel;
    }

    /**
     * Salveaza modificarile in model
     * @return true daca s-a modificat vreo variabila
     */
    public boolean commitChanges() {
        boolean auxBoolean;
        boolean hasChanged = false;

        //Initializez vectorul de schimbari
        changeTracker = new boolean[propertiesModel.getRowCount()];

        //Iterez prin panouri si le fac commit
        for (int i = 0; i < propertiesModel.getRowCount(); i++) {
            Class objectClass = propertiesModel.getClassAt(i);
            Object newValue;

            IPropertiesEditor editorComponent = propertiesEditorVector.get(i);
            newValue = editorComponent.getValue(objectClass);

            //Incerc efectuarea schimbarii
            auxBoolean = propertiesModel.setValueAt(i, newValue);
            changeTracker[i] = auxBoolean;
            hasChanged = auxBoolean || hasChanged;
        }

        //Intorc daca s-a modificat ceva
        return hasChanged;
    }

    /**
     * Reseteaza campurile la valorile lor default
     */
    public void resetToDefaults() {
        //Setez noile valori
        for (int i = 0; i < propertiesEditorVector.size(); i++) {
            Object defaultValue = propertiesModel.getDefaultValueAt(i);
            if (defaultValue != null) {
                IPropertiesEditor propertiesEditor = propertiesEditorVector.get(i);
                propertiesEditor.setValue(defaultValue.getClass()  , defaultValue);
            }
        }
    } 

    /**
     * Verific starea anumitor randuri
     * @param row Randul care se testeaza pentru modificari
     * @return true daca in urma commit-ului acesta s-a schimbat, false daca nu
     */
    public boolean hasRowChanged(int row) {
        if (changeTracker != null)
            return changeTracker[row];
        else return false;
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Actiuni custom la modificare valori
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Dezactivez toti listeneri
     */
    private void silenceAllListeners() {
        Iterator <IEditorChangeListener> iterator = editorChangeListeners.iterator();
        while (iterator.hasNext()) {
            IEditorChangeListener iEditorChangeListener = iterator.next();
            iEditorChangeListener.setEnabled(false);
        }
    }

    /**
     * Dezactivez toti listeneri
     */
    private void wakeupAllListeners() {
        Iterator <IEditorChangeListener> iterator = editorChangeListeners.iterator();
        while (iterator.hasNext()) {
            IEditorChangeListener iEditorChangeListener = iterator.next();
            iEditorChangeListener.setEnabled(true);
        }
    }

    /**
     * Do noting
     * @param ce 
     */
    public void stateChanged(ChangeEvent ce) {
        //do nothing
    }

    /**
     * Clasa ce va asculta de editarile facute
     */
    private class LocalEditorChangeListener implements IEditorChangeListener {
        private int row;
        private boolean isEnabled;
        
        /**
         * Contruiesc un listener si retin randul pe care acesta asculta
         * @param row
         */
        public LocalEditorChangeListener(int row) {
            this.row = row;
            this.isEnabled = true;
        }

        /**
         * Metoda de activare/dezactivare ascultatori
         * @param shouldEnable
         */
        public void setEnabled(boolean shouldEnable) {
            this.isEnabled = shouldEnable;
        }

        /**
         * Propam modificarea in controller
         */
        public void fireValueChanged() {
            if (isEnabled) {
                silenceAllListeners();

                //Fac modificarile fara a reface validarea panourilor
                propertiesModel.fireActionRowChanged(row);
                
                wakeupAllListeners();
            }
        }
    }
}
