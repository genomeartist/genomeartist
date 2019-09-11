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
import javax.swing.JOptionPane;
import ro.genomeartist.components.dialogs.JTwoButtonAbstractDialog;
import ro.genomeartist.components.modalpanel.progresspanel.JProgressPanel;
import ro.genomeartist.components.swingworkers.progressworker.AbstractProgressCallable;
import ro.genomeartist.components.swingworkers.progressworker.JProgressSwingWorker;
import ro.genomeartist.gui.controller.externalcalls.ExternalLink;
import ro.genomeartist.gui.controller.settings.GeneralSettings;
import ro.genomeartist.gui.interfaces.IGlobalManager;
import ro.genomeartist.gui.interfaces.ILocalManager;
import ro.genomeartist.gui.mainpanels.settings.JExportPane;
import ro.genomeartist.gui.mainpanels.settings.JSettingsPane;
import ro.genomeartist.gui.utils.JMyBoolean;

/**
 *  Dialogul wrapper peste panoul cu afisearea unui Partial Result
 * @author iulian
 */
public class JExportDialog extends JTwoButtonAbstractDialog {
    //Constante pentru dimensiuni
    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 350;

    //Constante
    private static final String BUTTON_OK = "    Ok    ";
    private static final String BUTTON_CANCEL = "  Cancel  ";

    //Managerii
    private IGlobalManager globalManager;

    private JExportPane flankingSequencesPane;
    
    private JMyBoolean isOk;
    
    private int genomeCoordinate;
    private int lengthExtractSeq;
    private int toleranceExtractSeq;
    private boolean useBorderCoordinate;
    private String chromosomeFilename;

    /**
     * Dialog de afisare partial result
     */
    public JExportDialog(IGlobalManager globalManager, 
            String title, boolean modal, JMyBoolean isOk)  {
        super(globalManager.getTheRootFrame(), title, modal);
        setSize(FRAME_WIDTH,FRAME_HEIGHT);

        //Setez managerii
        this.globalManager = globalManager;
        this.isOk = isOk;

        //Initialize the dialog
        this.setTextOk(BUTTON_OK);
        this.setTextCancel(BUTTON_CANCEL);
        setResizable(true);

        flankingSequencesPane = new JExportPane();
        this.setCenterComponent(flankingSequencesPane);
        
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
        String coordinateText = flankingSequencesPane.getGenomeCoordinate();
        String lengthExtractSeqText = flankingSequencesPane.getLengthExtractSeq();
        String toleranceExtractSeqText = flankingSequencesPane.getToleranceExtractSeq();
        try {
            lengthExtractSeq = Integer.parseInt(lengthExtractSeqText);
        } catch(NumberFormatException e) { 
            JOptionPane.showMessageDialog(JExportDialog.this,
                            "Length of extracted sequence must be an Integer","Error",JOptionPane.ERROR_MESSAGE);
            lengthExtractSeq = -1;
            isOk.setFalse();
        }
        try {
            toleranceExtractSeq = Integer.parseInt(toleranceExtractSeqText);
        } catch(NumberFormatException e) { 
            JOptionPane.showMessageDialog(JExportDialog.this,
                            "Tolerance must be an Integer","Error",JOptionPane.ERROR_MESSAGE);
            toleranceExtractSeq = -1;
            isOk.setFalse();
        }
        if(flankingSequencesPane.getIsChooseButtonSelected()) {
            useBorderCoordinate = false;           
            try {
                genomeCoordinate = Integer.parseInt(coordinateText);
            } catch(NumberFormatException e) { 
                JOptionPane.showMessageDialog(JExportDialog.this,
                                "Coordinate Value must be an Integer","Error",JOptionPane.ERROR_MESSAGE);
                genomeCoordinate = -1;
                isOk.setFalse();
            }            
        }
        else {
            useBorderCoordinate = true;            
        }            
        JExportDialog.this.dispose();
        isOk.setTrue();
    }

    /**
     * Lansez actiunea de anulare
     */
    public void fireActionCancel() {
        JExportDialog.this.dispose();
    }
    
    public String getChromosomeFilePath() {
        return flankingSequencesPane.getChromosomeFilepath();
    }
    
    public int getGenomeCoordinate() {
        return Integer.parseInt(flankingSequencesPane.getGenomeCoordinate());
    }
    
    public int getLengthExtractSeq() {
        return Integer.parseInt(flankingSequencesPane.getLengthExtractSeq());
    }
    
    public int getToleranceExtractSeq() {
        return toleranceExtractSeq;
    }
    
    public boolean getIsChooseButtonSelected() {
        return flankingSequencesPane.getIsChooseButtonSelected();
    }
    
    public boolean getIsTableButtonSelected() {
        return flankingSequencesPane.getIsTableButtonSelected();
    }
    
    public boolean getIsTSDButtonSelected() {
        return flankingSequencesPane.getIsTSDButtonSelected();
    }
    
    public boolean getIsFlankingButtonSelected() {
        return flankingSequencesPane.getIsFlankingButtonSelected();
    }
    
    public boolean getIsTwoFlanksButtonSelected() {
        return flankingSequencesPane.getIsTwoFlanksButtonSelected();
    }

}

