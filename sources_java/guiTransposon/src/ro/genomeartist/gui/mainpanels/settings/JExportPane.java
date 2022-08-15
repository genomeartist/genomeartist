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

package ro.genomeartist.gui.mainpanels.settings;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import ro.genomeartist.gui.controller.externalcalls.RawFileFilter;
import ro.genomeartist.gui.utils.ReadOnlyConfiguration;
import ro.genomeartist.components.autocompletecombobox.AutoCompleteComboBox;

/**
 * Consturiesc panoul de export
 * @author ghita
 */
public class JExportPane extends JPanel {
    private static final String EXPORT_TABLE = "Export Aggregate Data to Table";
    private static final String TSD_INSERTION = "TSD Sequence at Insertion Site";
    private static final String FLANKING_INSERTION = "Flanking Seq. at Insertion Site";
    private static final String FLANKING_TSD = "Flanking Seq. around Virtual TSD";
    private static final String FLANKING_COORDINATE = "Flanking Seq. at Coordinate";
    private static final String CONSENSUS ="Export Consensus Sequence";
    
    private JRadioButton tableButton;
    private JRadioButton insertionTSDButton;
    private JRadioButton insertionFlankingButton;
    private JRadioButton insertionTwoFlanksButton;
    private JRadioButton chooseCoordinateButton;
    private JCheckBox toggleConsensusButton;
    private JTextField coordinateField;
    private ArrayList<String> coordinateList;
    private JTextField numberOfResultsField;
    private JButton browseCoordinateFileButton;
    private JTextField lengthExtractSeqField;
    private JTextField toleranceExtractSeqField;
    private JTextField consensusTresholdField;
    private ButtonGroup buttonGroup;
    private AutoCompleteComboBox chromosomeFilesList;
    private String[] chromosomeFileNames;
    private String[] chromosomeFilePaths;
    
    private class EnableListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(chooseCoordinateButton.isSelected()) {
                coordinateField.setEnabled(true);
                chromosomeFilesList.setEnabled(true);
                browseCoordinateFileButton.setEnabled(true);
            }
            if(insertionTSDButton.isSelected() || insertionFlankingButton.isSelected() 
                || tableButton.isSelected() || insertionTwoFlanksButton.isSelected()) {
                coordinateField.setEnabled(false);
                chromosomeFilesList.setEnabled(false);
                browseCoordinateFileButton.setEnabled(false);
            }
        }
    }
    
    public JExportPane() {
        tableButton = new JRadioButton(EXPORT_TABLE, true);
        tableButton.setSelected(true);
        insertionTSDButton = new JRadioButton(TSD_INSERTION, true);
        insertionFlankingButton = new JRadioButton(FLANKING_INSERTION, true);
        insertionTwoFlanksButton = new JRadioButton(FLANKING_TSD, true);
        chooseCoordinateButton = new JRadioButton(FLANKING_COORDINATE, true);
        toggleConsensusButton = new JCheckBox(CONSENSUS);
        tableButton.addActionListener(new EnableListener());
        chooseCoordinateButton.addActionListener(new EnableListener());
        insertionTSDButton.addActionListener(new EnableListener());
        insertionFlankingButton.addActionListener(new EnableListener());
        insertionTwoFlanksButton.addActionListener(new EnableListener());
        toggleConsensusButton.addActionListener(new EnableListener());
        coordinateField = new JTextField(15);
        coordinateField.setEnabled(false);   
        browseCoordinateFileButton = new JButton("Browse");
        browseCoordinateFileButton.setEnabled(false);
        browseCoordinateFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireActionLoadCoordinates();
            }
        });
        
        //... Create a button group and add the buttons.
        buttonGroup = new ButtonGroup();
        buttonGroup.add(tableButton);
        buttonGroup.add(insertionTSDButton);
        buttonGroup.add(insertionFlankingButton);
        buttonGroup.add(insertionTwoFlanksButton);
        buttonGroup.add(chooseCoordinateButton);
        
        numberOfResultsField = new JTextField(15);
        numberOfResultsField.setEditable(true);
        numberOfResultsField.setText("0");
        lengthExtractSeqField = new JTextField(15);
        lengthExtractSeqField.setEditable(true);
        lengthExtractSeqField.setText("8");
        toleranceExtractSeqField = new JTextField(15);
        toleranceExtractSeqField.setEditable(true);
        toleranceExtractSeqField.setText("0");
        consensusTresholdField = new JTextField(15);
        consensusTresholdField.setEditable(true);
        consensusTresholdField.setText("20");
        
        String folderRawString = ReadOnlyConfiguration.getString("FOLDER_RAW");
        File folderRaw = new File(folderRawString);
        File[] chromosomeFileList = folderRaw.listFiles(new RawFileFilter());
        if(chromosomeFileList != null) {
            chromosomeFileNames = new String[chromosomeFileList.length];
            chromosomeFilePaths = new String[chromosomeFileList.length];
            for(int i = 0; i < chromosomeFileList.length; i++) {
                chromosomeFileNames[i] = chromosomeFileList[i].getName();
                chromosomeFilePaths[i] = chromosomeFileList[i].getAbsolutePath();
            }
            chromosomeFilesList = new AutoCompleteComboBox(chromosomeFileNames);
        }
        else
            chromosomeFilesList = new AutoCompleteComboBox();
        chromosomeFilesList.setEnabled(false);
        chromosomeFilesList.setPreferredSize(new Dimension(15, 30));
        
        GridBagConstraints gbc;        
        this.setLayout(new GridBagLayout());
        gbc = makeGbc(0, 0, 1, 1, new Insets(5,5,5,5));
        this.add(tableButton, gbc);
        gbc = makeGbc(1, 0, 1, 1, new Insets(5,5,5,5));
        this.add(insertionTSDButton, gbc);
        gbc = makeGbc(0, 1, 1, 1, new Insets(5,5,5,5));
        this.add(chooseCoordinateButton, gbc);
        gbc = makeGbc(1, 1, 1, 1, new Insets(5,5,5,5));
        this.add(insertionTwoFlanksButton, gbc);
        gbc = makeGbc(0, 2, 1, 1, new Insets(5,5,5,5));
        this.add(insertionFlankingButton, gbc);
        gbc = makeGbc(1, 2, 1, 1, new Insets(5,5,5,5));
        this.add(toggleConsensusButton, gbc);
        gbc = makeGbc(0, 3, 1, 1, new Insets(5,5,5,5));
        this.add(new JLabel("Chromosome"), gbc);
        gbc = makeGbc(1, 3, 1, 1, new Insets(5,5,5,5));
        this.add(chromosomeFilesList, gbc);  
        gbc = makeGbc(0, 4, 1, 1, new Insets(5,5,5,5));
        this.add(new JLabel("Coordinate"), gbc);
        gbc = makeGbc(1, 4, 1, 1, new Insets(5,5,5,5));
        this.add(coordinateField, gbc);
        gbc = makeGbc(2, 4, 1, 1, new Insets(5,5,5,5));
        this.add(browseCoordinateFileButton, gbc);
        gbc = makeGbc(0, 5, 1, 1, new Insets(5,5,5,5));
        this.add(new JLabel("Num. Exported Results (def. best score)"), gbc);
        gbc = makeGbc(1, 5, 1, 1, new Insets(5,5,5,5));
        this.add(numberOfResultsField, gbc);
        gbc = makeGbc(0, 6, 1, 1, new Insets(5,5,5,5));
        this.add(new JLabel("Length of TSD/Flank"), gbc);
        gbc = makeGbc(1, 6, 1, 1, new Insets(5,5,5,5));
        this.add(lengthExtractSeqField, gbc);
        gbc = makeGbc(0, 7, 1, 1, new Insets(5,5,5,5));
        this.add(new JLabel("Tolerance Length"), gbc);
        gbc = makeGbc(1, 7, 1, 1, new Insets(5,5,5,5));
        this.add(toleranceExtractSeqField, gbc);
        gbc = makeGbc(0, 8, 1, 1, new Insets(5,5,5,5));
        this.add(new JLabel("Consensus Treshold (%)"), gbc);
        gbc = makeGbc(1, 8, 1, 1, new Insets(5,5,5,5));
        this.add(consensusTresholdField, gbc);
    }
    
    private GridBagConstraints makeGbc(int x, int y, 
            int width, int height, Insets insets) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;       
        gbc.insets = insets;
        gbc.anchor = (x == 0) ? GridBagConstraints.LINE_START : GridBagConstraints.LINE_END;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }
    
    public void fireActionLoadCoordinates() {
        coordinateList = new ArrayList<String>();
        JFileChooser fc = new JFileChooser();
        int result = fc.showOpenDialog(this);
        coordinateList.clear();
        if(result == JFileChooser.APPROVE_OPTION) {
            try {                
                final File file = fc.getSelectedFile();
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String line;  
                while((line = br.readLine()) != null) {
                    coordinateList.add(line);
                }
            } catch(Exception e) {}
        }
    }

    // Get-eri    
    public boolean getIsChooseButtonSelected() {
        return chooseCoordinateButton.isSelected();
    }
    
    public boolean getIsTableButtonSelected() {
        return tableButton.isSelected();
    }
    
    public boolean getIsTSDButtonSelected() {
        return insertionTSDButton.isSelected();
    }
    
    public boolean getIsFlankingButtonSelected() {
        return insertionFlankingButton.isSelected();
    }
    
    public boolean getIsTwoFlanksButtonSelected() {
        return insertionTwoFlanksButton.isSelected();
    }
    
    public boolean getIsConsensusButtonSelected() {
        return toggleConsensusButton.isSelected();
    }
    
    public String getGenomeCoordinate() {
        return coordinateField.getText();
    }
    
    public String getNumberOfResults() {
        return numberOfResultsField.getText();
    }
    
    public ArrayList<String> getGenomeCoordinateList() {
        return coordinateList;
    }
    
    public String getLengthExtractSeq() {
        return lengthExtractSeqField.getText();
    }
    
    public String getToleranceExtractSeq() {
        return toleranceExtractSeqField.getText();
    }
    
    public String getConsensusTreshold() {
        return consensusTresholdField.getText();
    }
    
    public String getChromosomeFilepath() {
        for(int i = 0; i < chromosomeFilesList.getItemCount(); i++) {
            if(chromosomeFilesList.getSelectedItem().toString().equals(chromosomeFileNames[i]))
                return chromosomeFilePaths[i];
        }
        return null;
    }
}
