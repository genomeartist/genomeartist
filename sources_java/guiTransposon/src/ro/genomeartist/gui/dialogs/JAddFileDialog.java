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
import ro.genomeartist.gui.controller.settings.SearchFile;
import ro.genomeartist.gui.interfaces.IGlobalManager;
import ro.genomeartist.gui.utils.JMyBoolean;
import ro.genomeartist.gui.utils.MyUtils;
import ro.genomeartist.gui.utils.StringUtils;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;

/**
 *  Dialogul cu preferinte
 * @author iulian
 */
public class JAddFileDialog extends JDialog {
    private IGlobalManager globalManager;
    private SearchFile searchFileRaw;
    private JMyBoolean isOk;

    //Constante pentru titluri
    private static final String TITLE_FILE = "File information";
    //Constante pentru label-uri
    private static final String TEXT_NAME = "Name*";
    private static final String TEXT_SEQUENCE_LOCATION = "Sequence location (.raw)*";
    private static final String TEXT_GENES_LOCATION = "Genes location (.fasta)";
    private static final String TEXT_HINT = "* required fields";
    //Constante pentru dimensiuni
    private static final int FRAME_WIDTH = 500;
    private static final int FRAME_HEIGHT = 200;
    //Valori constante
    private static final int TEXTFIELD_LENGTH = 10;
    private static final int LABEL_SIZE = 200;
    private static final int HUGE_SIZE = 3000;
    //Constante
    private static final String BUTTON_SEQ = "...";
    private static final String BUTTON_GENE = "...";
    private static final String BUTTON_OK = "    Ok    ";
    private static final String BUTTON_CANCEL = "  Cancel  ";
    //Constante pentru actiuni
    private static final String ACTION_SEQ = "seq_file";
    private static final String ACTION_GENE = "gene_file";
    private static final String ACTION_OK = "ok";
    private static final String ACTION_CANCEL = "apply";

    private JPanel middlePane;
        //Panoul cu date despre aliniere
        private JPanel namePanel;
            private JLabel labelTextName;
            private JTextField textfieldName;
        private JPanel sequencePanel;
            private JLabel labelTextSequence;
            private JTextField textfieldSequence;
            private JButton buttonSequenceFile;
        private JPanel genesPanel;
            private JLabel labelTextGenes;
            private JTextField textfieldGenes;
            private JButton buttonGeneFile;
        private JPanel hintPanel;
            private JLabel labelTextHint;
    private JPanel bottomPane;
        private JButton buttonOk;
        private JButton buttonCancel;

    //~~~~~~~~~Un file chooser~~~~~~~~~/
    private JFileChooser fc;

    /**
     * Dialog de eroare
     */
    public JAddFileDialog(IGlobalManager globalManager, String title, boolean modal,
             JMyBoolean isOk)  {
        super(globalManager.getTheRootFrame(), title, modal);
        setSize(FRAME_WIDTH,FRAME_HEIGHT);

        this.globalManager = globalManager;
        this.searchFileRaw = new SearchFile();
        this.isOk = isOk;

        //File choserul
        fc = new JFileChooser();

        //Initialize the dialog
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setLayout(new BorderLayout());


        middlePane = createMiddlePane();
        this.add(middlePane,BorderLayout.CENTER);

        //Making the buttons
        bottomPane = new JPanel();
        bottomPane.setLayout(new BoxLayout(bottomPane, BoxLayout.X_AXIS));
        bottomPane.add(Box.createHorizontalGlue());
            buttonOk = new JButton(BUTTON_OK);
            buttonOk.setActionCommand(ACTION_OK);
            buttonOk.addActionListener(buttonListener);
        bottomPane.add(buttonOk);
            buttonCancel = new JButton(BUTTON_CANCEL);
            buttonCancel.setActionCommand(ACTION_CANCEL);
            buttonCancel.addActionListener(buttonListener);
        bottomPane.add(buttonCancel);
        bottomPane.add(Box.createHorizontalGlue());
        this.add(bottomPane,BorderLayout.SOUTH);

        //Set it's location
        setLocationRelativeTo(globalManager.getTheRootFrame());
    }

    /**
     * Creez panoul de mijloc
     * @return
     */
    private JPanel createMiddlePane() {
        JPanel localPane = new JPanel();

        localPane.setLayout(new BoxLayout(localPane, BoxLayout.Y_AXIS));
        localPane.setBorder(BorderFactory.createTitledBorder(TITLE_FILE));
            //Zero offset
            labelTextName = new JLabel(TEXT_NAME);
            textfieldName = new JTextField(TEXTFIELD_LENGTH);
                textfieldName.setText("name");
            namePanel = createAlignedPane(labelTextName, textfieldName, false);
        localPane.add(namePanel);
            //Score match
            labelTextSequence = new JLabel(TEXT_SEQUENCE_LOCATION);
            textfieldSequence = new JTextField(TEXTFIELD_LENGTH);
                textfieldSequence.setEditable(false);
            sequencePanel = createAlignedPane(labelTextSequence, textfieldSequence, false);
                buttonSequenceFile = new JButton();
                buttonSequenceFile.setText(BUTTON_SEQ);
                buttonSequenceFile.setActionCommand(ACTION_SEQ);
                buttonSequenceFile.addActionListener(buttonListener);
            sequencePanel.add(buttonSequenceFile);
         localPane.add(sequencePanel);
            //Score mismatch
            labelTextGenes = new JLabel(TEXT_GENES_LOCATION);
            textfieldGenes = new JTextField(TEXTFIELD_LENGTH);
                textfieldGenes.setEditable(false);
            genesPanel = createAlignedPane(labelTextGenes, textfieldGenes, false);
                buttonGeneFile = new JButton();
                buttonGeneFile.setText(BUTTON_GENE);
                buttonGeneFile.setActionCommand(ACTION_GENE);
                buttonGeneFile.addActionListener(buttonListener);
            genesPanel.add(buttonGeneFile);
        localPane.add(genesPanel);
            labelTextHint = new JLabel(TEXT_HINT);
            MyUtils.increaseLabelFont(labelTextHint, -2);
            labelTextHint.setForeground(Color.blue);
            hintPanel = createAlignedPane(labelTextHint, new JLabel(" "), true);
        localPane.add(hintPanel);
        localPane.add(Box.createVerticalStrut(HUGE_SIZE));
        
        return localPane;
    }

    /**
     * Creeaza un panoul cu un label si o valoare
     */
    private JPanel createAlignedPane(JLabel label1,Component component2,boolean withGap) {
        JPanel panel;
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        label1.setPreferredSize(new Dimension(LABEL_SIZE, 0));
        panel.add(label1);
        if (withGap) panel.add(Box.createHorizontalGlue());
        panel.add(component2);

        return panel;
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Actiuni pe dialog
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /**
     * Obtin fisierul raw care se va creea
     * @return
     */
    public SearchFile getFileRaw() {
        return searchFileRaw;
    }

    /**
     * Updatez panourile sa se potriveasca cu fisierul raw
     */
    private void updatePanelFromRaw() {
        if (searchFileRaw.rawLocation != null) {
            textfieldSequence.setText(searchFileRaw.rawLocation.getPath());
            textfieldSequence.setToolTipText(searchFileRaw.rawLocation.getPath());
        }

        if (searchFileRaw.geneLocation != null) {
            textfieldGenes.setText(searchFileRaw.geneLocation.getPath());
            textfieldGenes.setToolTipText(searchFileRaw.geneLocation.getPath());
        }

        this.repaint();
    }

    /**
     *  <p style="margin-top: 0">
     *  Clasa ce reprezinta un ascultator pe butoanele de ok si Cancel
     *  </p>
     * @author iulian
     */
    ActionListener buttonListener = new ActionListener() {
        /**
         *  <p style="margin-top: 0">
         *  Invoked when an action occurs.
         *  </p>
         * @author iulian
         */
            public void actionPerformed(ActionEvent e)
            {
                String cmd = e.getActionCommand();
                String auxString;

                if (ACTION_SEQ.equals(cmd)) {
                    fc.setCurrentDirectory(new File("."));
                    int returnVal = fc.showOpenDialog(globalManager.getTheRootFrame());
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        searchFileRaw.rawLocation = fc.getSelectedFile();
                        updatePanelFromRaw();
                    }
                }  else
                if (ACTION_GENE.equals(cmd)) {
                    fc.setCurrentDirectory(new File("."));
                    int returnVal = fc.showOpenDialog(globalManager.getTheRootFrame());
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        searchFileRaw.geneLocation = fc.getSelectedFile();
                        updatePanelFromRaw();
                    }
                }  else
                if (ACTION_OK.equals(cmd)) {
                    //Setez numele fisierului
                    auxString = textfieldName.getText();
                    if (auxString.isEmpty()) auxString = "file";
                    searchFileRaw.fileTitle = StringUtils.sanitizeFilename(auxString);

                    //Setez ok
                    isOk.setTrue();
                    JAddFileDialog.this.dispose();
                }  else
                if (ACTION_CANCEL.equals(cmd)) {

                    isOk.setFalse();
                    JAddFileDialog.this.dispose();
                }
            }
        };
}
