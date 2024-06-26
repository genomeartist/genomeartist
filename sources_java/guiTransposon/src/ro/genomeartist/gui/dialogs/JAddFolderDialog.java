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
import ro.genomeartist.gui.controller.settings.SearchFolder;
import ro.genomeartist.gui.interfaces.IGlobalManager;
import ro.genomeartist.gui.utils.JMyBoolean;
import ro.genomeartist.gui.utils.MyUtils;
import ro.genomeartist.gui.utils.StringUtils;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.*;

/**
 *  Dialogul cu preferinte
 * @author iulian
 */
public class JAddFolderDialog extends JDialog {
    private IGlobalManager globalManager;
    private SearchFolder searchFolderRaw;
    private JMyBoolean isOk;
    private boolean isFolder;

    //Constante pentru titluri
    private static final String TITLE_FILE = "File information";
    //Constante pentru label-uri
    private static final String TEXT_NAME = "Name*";
    private static final String TEXT_SEQUENCE_LOCATION = "Folder with raw sequences*";
    private static final String TEXT_HINT = "* required fields";
    //Constante pentru dimensiuni
    private static final int FRAME_WIDTH = 500;
    private static final int FRAME_HEIGHT = 200;
    //Valori constante
    private static final int TEXTFIELD_LENGTH = 10;
    private static final int LABEL_SIZE = 200;
    private static final int HUGE_SIZE = 3000;
    //Constante
    private static final String BUTTON_FOLDER = "...";
    private static final String BUTTON_OK = "    Ok    ";
    private static final String BUTTON_CANCEL = "  Cancel  ";
    //Constante pentru actiuni
    private static final String ACTION_FOLDER = "seq_file";
    private static final String ACTION_OK = "ok";
    private static final String ACTION_CANCEL = "apply";
    //Constante pentru actiuni
    private static final String ACTION_FILES = "seq_files";

    private JPanel middlePane;
        //Panoul cu date despre aliniere
        private JPanel namePanel;
            private JLabel labelTextName;
            private JTextField textfieldName;
        private JPanel folderPanel;
            private JLabel labelTextFolder;
            private JTextField textfieldFolder;
            private JButton buttonFolderFile;
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
    public JAddFolderDialog(IGlobalManager globalManager, String title, boolean modal,
             JMyBoolean isOk, boolean isFolder)  {
        super(globalManager.getTheRootFrame(), title, modal);
        setSize(FRAME_WIDTH,FRAME_HEIGHT);

        this.globalManager = globalManager;
        this.searchFolderRaw = new SearchFolder();
        this.isOk = isOk;
        this.isFolder = isFolder;

        //File choserul
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

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
            labelTextFolder = new JLabel(TEXT_SEQUENCE_LOCATION);
            textfieldFolder = new JTextField(TEXTFIELD_LENGTH);
                textfieldFolder.setEditable(false);
            folderPanel = createAlignedPane(labelTextFolder, textfieldFolder, false);
                buttonFolderFile = new JButton();
                buttonFolderFile.setText(BUTTON_FOLDER);
                if (isFolder) {
                    buttonFolderFile.setActionCommand(ACTION_FOLDER);
                }
                else {
                    buttonFolderFile.setActionCommand(ACTION_FILES);
                }
                buttonFolderFile.addActionListener(buttonListener);
            folderPanel.add(buttonFolderFile);
         localPane.add(folderPanel);
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
    public SearchFolder getFolderRaw() {
        return searchFolderRaw;
    }

    /**
     * Updatez panourile sa se potriveasca cu fisierul raw
     */
    private void updatePanelFromRaw() {
        if (searchFolderRaw.folderLocation != null) {
            textfieldFolder.setText(searchFolderRaw.folderLocation.getPath());
            textfieldFolder.setToolTipText(searchFolderRaw.folderLocation.getPath());
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

                if (ACTION_FOLDER.equals(cmd)) {
                    fc.setCurrentDirectory(new File("."));
                    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int returnVal = fc.showOpenDialog(globalManager.getTheRootFrame());
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        searchFolderRaw.folderLocation = fc.getSelectedFile();
                        updatePanelFromRaw();
                    }
                }  else
                if (ACTION_FILES.equals(cmd)) {
                    fc.setCurrentDirectory(new File("."));
                    fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    fc.setMultiSelectionEnabled(true);
                    int returnVal = fc.showOpenDialog(globalManager.getTheRootFrame());

                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        searchFolderRaw.selectedFiles = fc.getSelectedFiles();
                        searchFolderRaw.folderLocation = fc.getCurrentDirectory();
                        updatePanelFromRaw();
                    }
                }  else
                if (ACTION_OK.equals(cmd)) {
                    //Setez numele fisierului
                    auxString = textfieldName.getText();
                    if (auxString.isEmpty()) auxString = "file";
                    searchFolderRaw.fileTitle = StringUtils.sanitizeFilename(auxString);

                    //Setez ok
                    isOk.setTrue();
                    JAddFolderDialog.this.dispose();
                }  else
                if (ACTION_CANCEL.equals(cmd)) {

                    isOk.setFalse();
                    JAddFolderDialog.this.dispose();
                }
            }
        };
}
