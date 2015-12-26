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
package ro.genomeartist.components.dialogs;

import ro.genomeartist.components.propertiespanel.AbstractPropertiesPanelModel;
import ro.genomeartist.components.propertiespanel.JPropertiesPanel;
import java.awt.Frame;

/**
 *
 * @author iulian
 */
public class JPropertiesContainerDialog extends JTwoButtonAbstractDialog  {
    //Variabile de clasa
    JBooleanWrapper isOk;
    AbstractPropertiesPanelModel propertiesPanelModel;
    private boolean forceUpdate;
    
    //Structura panoului
    JPropertiesPanel propertiesPanel;
    
    public JPropertiesContainerDialog(Frame owner, String title, 
            boolean modal, AbstractPropertiesPanelModel propertiesPanelModel, JBooleanWrapper isOk) {
        super(owner, title, modal);
        
        //Variabilele de clasa
        this.isOk = isOk;
        this.propertiesPanelModel = propertiesPanelModel;
        this.forceUpdate = false;
        
        //Pun un panou de proprietati
        propertiesPanel = new JPropertiesPanel(propertiesPanelModel);

        //Setez panoul central
        this.setCenterComponent(propertiesPanel);

        this.pack();

        //Set it's location
        setLocationRelativeTo(owner);
    }
    
    /**
     * Forteaza updatarea obiectului la sfarsitul dialogului
     * @param forceUpdate
     */
    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }
    
     /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Actiunile asupra Dialogului
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Lansez actiunea OK
     */
    public void fireActionOk() {
        boolean hasChanged = forceUpdate;

        //Fac commit la date
        hasChanged = propertiesPanel.commitChanges() || hasChanged;
        if (hasChanged) {
            dispose();
            isOk.setTrue();
        } else {
            dispose();
            isOk.setFalse();
        }
    }

    /**
     * Lansez actiunea de anulare
     */
    public void fireActionCancel() {
        //inchid dialogul
        dispose();
        isOk.setFalse();
    }
    
}
