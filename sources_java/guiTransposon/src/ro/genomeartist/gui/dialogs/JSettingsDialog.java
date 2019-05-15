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

package ro.genomeartist.gui.dialogs;
import ro.genomeartist.components.dialogs.JTwoButtonAbstractDialog;
import ro.genomeartist.components.modalpanel.progresspanel.JProgressPanel;
import ro.genomeartist.components.swingworkers.progressworker.AbstractProgressCallable;
import ro.genomeartist.components.swingworkers.progressworker.JProgressSwingWorker;
import ro.genomeartist.gui.controller.externalcalls.ExternalLink;
import ro.genomeartist.gui.controller.settings.GeneralSettings;
import ro.genomeartist.gui.interfaces.IGlobalManager;
import ro.genomeartist.gui.interfaces.ILocalManager;
import ro.genomeartist.gui.mainpanels.settings.JSettingsPane;

/**
 *  Dialogul wrapper peste panoul cu afisearea unui Partial Result
 * @author iulian
 */
public class JSettingsDialog extends JTwoButtonAbstractDialog {
    //Constante pentru dimensiuni
    private static final int FRAME_WIDTH = 700;
    private static final int FRAME_HEIGHT = 800;

    //Constante
    private static final String BUTTON_OK = "    Ok    ";
    private static final String BUTTON_CANCEL = "  Cancel  ";

    //Managerii
    private IGlobalManager globalManager;
    private ILocalManager localManager;
    private GeneralSettings generalSettings;

    //~~~~~~~~~ Low level panes ~~~~~~~~~/
    private JSettingsPane settingsPane;


    /**
     * Dialog de afisare partial result
     */
    public JSettingsDialog(IGlobalManager globalManager, 
            String title, boolean modal , GeneralSettings generalSettings)  {
        super(globalManager.getTheRootFrame(), title, modal);
        setSize(FRAME_WIDTH,FRAME_HEIGHT);

        //Setez managerii
        this.globalManager = globalManager;
        this.generalSettings = generalSettings;

        //Initialize the dialog
        this.setTextOk(BUTTON_OK);
        this.setTextCancel(BUTTON_CANCEL);
        setResizable(true);

        //Middle pane
        settingsPane = new JSettingsPane(globalManager, generalSettings);
        this.setCenterComponent(settingsPane);

        //Set it's location
        setLocationRelativeTo(globalManager.getTheRootFrame());
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Actiunile asupra Dialogului
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Lansez actiunea OK
     */
    public void fireActionOk() {
        //Fac actiunea in background cu panou de monitorizare
        AplicaSetariCallableAction callable = new AplicaSetariCallableAction();
        JProgressSwingWorker settingsWorker =
                new JProgressSwingWorker(globalManager.getTheRootFrame(),
                "Applying new settings",callable,JProgressPanel.INDETERMINATE);
        settingsWorker.setStandardErrorMessage("Error applying settings !");

        //Fac taskul
        settingsWorker.executeTask();

        JSettingsDialog.this.dispose();
    }

    /**
     * Lansez actiunea de anulare
     */
    public void fireActionCancel() {
        //Fac actiunea in background cu panou de monitorizare
        AplicaSetariCallableAction callable = new AplicaSetariCallableAction(false);
        JProgressSwingWorker settingsWorker =
                new JProgressSwingWorker(globalManager.getTheRootFrame(),
                "Canceling settings",callable,JProgressPanel.INDETERMINATE);
        settingsWorker.setStandardErrorMessage("Error canceling settings !");

        //Fac taskul
        settingsWorker.executeTask();

        JSettingsDialog.this.dispose();
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/
    //            Controllerele pentru actiuni              /
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/

    /**
     * Aplica setarile
     */
    private class AplicaSetariCallableAction extends AbstractProgressCallable<Boolean> {
        boolean applyAlgorithmChanges;

        public AplicaSetariCallableAction() {
            this(true);
        }

        public AplicaSetariCallableAction(boolean applyAlgorithmChanges) {
            this.applyAlgorithmChanges = applyAlgorithmChanges;
        }
        
        
        public Boolean call() throws Exception {
            boolean hasChanged, auxFlag;
            hasChanged = false;

            //Salvez setarile
            if (applyAlgorithmChanges) {
                auxFlag = settingsPane.fireActionApplyAlgorithmParams();
                hasChanged = (hasChanged || auxFlag);
            }
            auxFlag = settingsPane.fireActionApplyFileParams();
            hasChanged = (hasChanged || auxFlag);

            //Daca s-au intamplat modificari trebuie sa restartez serverul
            if (hasChanged) {
                ExternalLink.stopServer();
                
                AbstractProgressCallable startServerCallable = ExternalLink.getStartServerCallable();
                startServerCallable.setProgressInfoManager(this);
                startServerCallable.call();
            }

            return hasChanged;
        }
    };

}
