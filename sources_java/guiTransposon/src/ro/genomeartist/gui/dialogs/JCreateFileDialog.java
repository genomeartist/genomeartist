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
import ro.genomeartist.gui.controller.settings.SearchFileSequence;
import ro.genomeartist.gui.interfaces.IGlobalManager;
import ro.genomeartist.gui.popups.JTextareaPopup;
import ro.genomeartist.gui.utils.DNAUtils;
import ro.genomeartist.gui.utils.JMyBoolean;
import ro.genomeartist.gui.utils.MyUtils;
import ro.genomeartist.gui.utils.StringUtils;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *  Dialogul wrapper peste panoul cu afisearea unui Partial Result
 * @author iulian
 */
public class JCreateFileDialog extends JDialog {
    //Constante pentru dimensiuni
    private static final int FRAME_WIDTH = 300;
    private static final int FRAME_HEIGHT = 200;
    private static final int BUTTON_WIDTH = 100;
    //Constante
    private static final String TEXT_TITLE = "File from sequence";
    private static final String TEXT_NAME = "Name";
    private static final String TEXT_NAME_FIELD = "sequence_001";
    private static final String TEXT_QUERY= "Sequence";
    //Constante pentru fereastra
    private static final int TEXTAREA_ROWS = 10;
    private static final int TEXTAREA_COLS = 10;
    private static final int TEXTFIELD_COLS = 25;
    private static final int LABEL_SIZE = 100;

    //Managerii
    private IGlobalManager globalManager;
    private SearchFileSequence searchFileSequence;
    private JMyBoolean isOk;

    private JPanel middlePane;
        //Structura panoului
        private JPanel namePane;
            private JLabel labelTextName;
            private JTextField textfieldValueName;
        private JPanel queryLabelPane;
            private JLabel labelTextQuery;
        private JPanel queryPane;
            private JTextArea textareaValueQuery;
        private JPanel buttonPane;
            private JButton searchButton;
    private JPanel bottomPane;
        private JButton buttonOk;
        private JButton buttonCancel;

    private static final String BUTTON_OK = "    Ok    ";
    private static final String BUTTON_CANCEL = "  Cancel  ";
    //Constante pentru actiuni
    private static final String ACTION_OK = "ok";
    private static final String ACTION_CANCEL = "apply";

    /**
     * Dialog de afisare partial result
     */
    public JCreateFileDialog(IGlobalManager globalManager, 
            String title, boolean modal , JMyBoolean isOk)  {
        super(globalManager.getTheRootFrame(), title, modal);
        setSize(FRAME_WIDTH,FRAME_HEIGHT);

        //Setez managerii
        this.globalManager = globalManager;
        this.isOk = isOk;

        //Initialize the dialog
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setLayout(new BorderLayout());

        //Middle pane
        middlePane = new JPanel();
        middlePane.setLayout(new BoxLayout(middlePane, BoxLayout.Y_AXIS));
        middlePane.setBorder(BorderFactory.createTitledBorder(TEXT_TITLE));
                labelTextName = new JLabel(TEXT_NAME);
                labelTextName.setPreferredSize(new Dimension(LABEL_SIZE, 0));
                textfieldValueName = new JTextField(TEXTFIELD_COLS);
                textfieldValueName.setText(TEXT_NAME_FIELD);
            namePane = createAlignedPane(labelTextName, textfieldValueName);
        middlePane.add(namePane);
                labelTextQuery = new JLabel(TEXT_QUERY);
                MyUtils.setLabelBold(labelTextQuery);
            queryLabelPane = createAlignedPane(labelTextQuery, new JLabel(""));
        middlePane.add(queryLabelPane);
        //Panoul cu Jtextarea
                textareaValueQuery = new JTextArea(TEXTAREA_ROWS, TEXTAREA_COLS);
                textareaValueQuery.setEditable(true);
                textareaValueQuery.setLineWrap(true);
                JTextareaPopup textareaPopup = new JTextareaPopup(textareaValueQuery, false);
                textareaValueQuery.addMouseListener(textareaPopup.getTextareaTrigger());
            JScrollPane scrollPane = new JScrollPane(textareaValueQuery);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        middlePane.add(scrollPane);
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

        this.pack();

        //Set it's location
        setLocationRelativeTo(globalManager.getTheRootFrame());
    }

    /**
     * Creeaza un panoul cu un label si o valoare
     */
    private JPanel createAlignedPane(JLabel label1,Component component2) {
        JPanel panel;
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(label1);
        panel.add(Box.createHorizontalGlue());
        panel.add(component2);
        return panel;
    }

    /**
     * Obtin file sequence-ul
     * @return
     */
    public SearchFileSequence getSearchFileSequence() {
        return searchFileSequence;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/
    //             Metodele de afisare                      /
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/
    /**
     * Lansez actiunea ACTION_OK
     */
    private void fireActionOk() {
        //Setez numele fisierului
        searchFileSequence = new SearchFileSequence();
        searchFileSequence.name = StringUtils.sanitizeFilename(textfieldValueName.getText());
        searchFileSequence.sequence = DNAUtils.extractCleanSequences(textareaValueQuery.getText()).get(0);
        searchFileSequence.sequence = searchFileSequence.sequence.toUpperCase();

        //Setez ok
        isOk.setTrue();
        JCreateFileDialog.this.dispose();
    }

    /**
     * Lansez actiunea de cancel
     */
    private void fireActionCancel() {
        isOk.setFalse();
        JCreateFileDialog.this.dispose();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/
    //            Ascultatori pe actiuni                    /
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/

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
                if (ACTION_OK.equals(cmd)) {
                    fireActionOk();
                }  else
                if (ACTION_CANCEL.equals(cmd)) {
                    fireActionCancel();
                }
            }
        };
}
