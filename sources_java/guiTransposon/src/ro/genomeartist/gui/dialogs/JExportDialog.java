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
import java.util.ArrayList;
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
 *  Dialogul wrapper peste panoul pentru export
 * @author ghita
 */
public class JExportDialog extends JTwoButtonAbstractDialog {
    //Constante pentru dimensiuni
    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 380;

    //Constante
    private static final String BUTTON_OK = "    Ok    ";
    private static final String BUTTON_CANCEL = "  Cancel  ";

    //Managerii
    private IGlobalManager globalManager;

    private JExportPane exportPane;
    
    private JMyBoolean isOk;
    
    private int[] intCoordinateArray;
    private int lengthExtractSeq;
    private int toleranceExtractSeq;
    private double consensusTreshold;

    /**
     * Dialog de afisare export
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

        exportPane = new JExportPane();
        this.setCenterComponent(exportPane);
        
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
        String coordinateText = exportPane.getGenomeCoordinate();
        ArrayList<String> coordinateList = exportPane.getGenomeCoordinateList();
        if(coordinateList == null)
            coordinateList = new ArrayList<String>();
        String lengthExtractSeqText = exportPane.getLengthExtractSeq();
        String toleranceExtractSeqText = exportPane.getToleranceExtractSeq();
        String consensusTresholdText = exportPane.getConsensusTreshold();
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
        if(exportPane.getIsConsensusButtonSelected()) {
            try {
                consensusTreshold = Integer.parseInt(consensusTresholdText);
            } catch(NumberFormatException e) { 
                JOptionPane.showMessageDialog(JExportDialog.this,
                                "Consensus Treshold must be a percentage","Error",JOptionPane.ERROR_MESSAGE);
                consensusTreshold = -1;
                isOk.setFalse();
            }
        }
        if(exportPane.getIsChooseButtonSelected()) {
            if(!coordinateText.isEmpty())
                coordinateList.add(0, coordinateText);
            intCoordinateArray = new int[coordinateList.size()];
            for(int i = 0; i < coordinateList.size(); i++) {
                try {
                    intCoordinateArray[i] = Integer.parseInt(coordinateList.get(i));
                } catch(NumberFormatException e) { 
                    JOptionPane.showMessageDialog(JExportDialog.this,
                                    "Coordinate Value must be an Integer","Error",JOptionPane.ERROR_MESSAGE);
                    isOk.setFalse();
                    break;
                }
            }
            if(exportPane.getChromosomeFilepath() == null)
                JOptionPane.showMessageDialog(JExportDialog.this,
                                "No Chromosome selected","Error",JOptionPane.ERROR_MESSAGE);
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
        return exportPane.getChromosomeFilepath();
    }
    
    public int[] getGenomeCoordinateArray() {
        return intCoordinateArray;
    }
    
    public int getLengthExtractSeq() {
        return lengthExtractSeq;
    }
    
    public int getToleranceExtractSeq() {
        return toleranceExtractSeq;
    }
    
    public double getConsensusTreshold() {
        return consensusTreshold;
    }
    
    public boolean getIsChooseButtonSelected() {
        return exportPane.getIsChooseButtonSelected();
    }
    
    public boolean getIsTableButtonSelected() {
        return exportPane.getIsTableButtonSelected();
    }
    
    public boolean getIsTSDButtonSelected() {
        return exportPane.getIsTSDButtonSelected();
    }
    
    public boolean getIsFlankingButtonSelected() {
        return exportPane.getIsFlankingButtonSelected();
    }
    
    public boolean getIsTwoFlanksButtonSelected() {
        return exportPane.getIsTwoFlanksButtonSelected();
    }
    
    public boolean getIsConsensusButtonSelected() {
        return exportPane.getIsConsensusButtonSelected();
    }

}

