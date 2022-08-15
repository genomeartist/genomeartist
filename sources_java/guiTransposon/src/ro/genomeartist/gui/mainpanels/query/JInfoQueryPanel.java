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

package ro.genomeartist.gui.mainpanels.query;

import ro.genomeartist.gui.interfaces.ILocalManager;
import ro.genomeartist.gui.interfaces.IGlobalManager;
import ro.genomeartist.gui.controller.query.InfoQuery;
import ro.genomeartist.gui.controller.query.MainResult;
import ro.genomeartist.gui.custompaint.DrawingConstants;
import ro.genomeartist.gui.dialogs.JPartialResultSetDialog;
import ro.genomeartist.gui.popups.JTextareaPopup;
import ro.genomeartist.gui.utils.MyUtils;
import ro.genomeartist.gui.utils.StringUtils;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author iulian
 */
public class JInfoQueryPanel extends JPanel {
    private InfoQuery infoQuery;
    private IGlobalManager globalManager;
    private ILocalManager localManager;

    //Constante
    private static final String TEXT_TITLE = "Query information";
    private static final String TEXT_DATE = "Search date";
    private static final String TEXT_TIME = "Time elapsed";
    private static final String TEXT_QUERY = "Query";
    private static final String TEXT_FINAL_RESULTS = "Final results";
    private static final String TEXT_PARTIAL_RESULTS = "Partial alignments";
    private static final String TEXT_OPEN = " Open partial alignments ";
    //Constante pt actiuni
    private static final String ACTION_OPEN = "open";
    //Constante pentru fereastra
    private static final int TEXTAREA_ROWS = 3;
    private static final int TEXTAREA_COLS = 10;
    private static final int LABEL_SIZE = 50;

    //Structura panoului
    JPanel datePane;
        JLabel labelTextDate, labelValueDate;
    JPanel timePane;
        JLabel labelTextTime, labelValueTime;
    JPanel queryHeaderPane;
        JLabel labelTextQueryHeader;
    JPanel queryPane;
        JTextArea textareaValueQuery;
    JPanel finalResultPane;
        JLabel labelTextFinalResults, labelValueFinalResults;
    JPanel partialResultPane;
        JLabel labelTextPartialResults, labelValuePartialResults;
    JPanel buttonPane;
        JButton openButton;
        
    /**
     * Construiesc panoul
     * @param infoQuery
     */
    public JInfoQueryPanel(IGlobalManager globalManager,ILocalManager localManager,
            InfoQuery infoQuery) {
        this.infoQuery = infoQuery;
        this.globalManager = globalManager;
        this.localManager = localManager;

        //Variabile auxiliare
        int auxInt;
        String auxString;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createTitledBorder(TEXT_TITLE));
        //Panoul de data
                labelTextDate = new JLabel(TEXT_DATE);
                MyUtils.setLabelBold(labelTextDate);
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                auxString = dateFormat.format(infoQuery.searchDate);
                labelValueDate = new JLabel(auxString);
                MyUtils.setLabelBold(labelValueDate);
            datePane = createAlignedPane(labelTextDate, labelValueDate);
        this.add(datePane);
        //Panoul cu timpul petrecut
                labelTextTime = new JLabel(TEXT_TIME);
                MyUtils.setLabelBold(labelTextTime);
                auxString = StringUtils.timeToString(infoQuery.timpExecutie);
                labelValueTime = new JLabel(auxString);
                MyUtils.setLabelBold(labelValueTime);
            timePane = createAlignedPane(labelTextTime, labelValueTime);
        this.add(timePane);
        //Panoul cu query header
                labelTextQueryHeader = new JLabel(TEXT_QUERY);
                MyUtils.setLabelBold(labelTextQueryHeader);
                labelTextQueryHeader.setPreferredSize(new Dimension(LABEL_SIZE,0));
            queryHeaderPane = createAlignedPane(labelTextQueryHeader, new JLabel(" "));
        this.add(queryHeaderPane);
        //Panoul cu Jtextarea
                textareaValueQuery = new JTextArea(TEXTAREA_ROWS, TEXTAREA_COLS);
                textareaValueQuery.setEditable(false);
                textareaValueQuery.setLineWrap(true);
                textareaValueQuery.setText(infoQuery.query);
                JTextareaPopup textareaPopup = new JTextareaPopup(textareaValueQuery, true);
                textareaValueQuery.addMouseListener(textareaPopup.getTextareaTrigger());
            JScrollPane scrollPane = new JScrollPane(textareaValueQuery);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.add(scrollPane);
        //Panoul cu nr de rezultate finale
                labelTextFinalResults = new JLabel(TEXT_FINAL_RESULTS);
                MyUtils.setLabelBold(labelTextFinalResults);
                labelValueFinalResults = new JLabel(infoQuery.finalResultNumber+"");
                MyUtils.setLabelBold(labelValueFinalResults);
            finalResultPane = createAlignedPane(labelTextFinalResults, labelValueFinalResults);
        this.add(finalResultPane);
        //Panoul cu nr de rezultate partiale
                labelTextPartialResults = new JLabel(TEXT_PARTIAL_RESULTS);
                MyUtils.setLabelBold(labelTextPartialResults);
                labelValuePartialResults = new JLabel(infoQuery.partialResultNumber+"");
                MyUtils.setLabelBold(labelValuePartialResults);
            partialResultPane = createAlignedPane(labelTextPartialResults, labelValuePartialResults);
        this.add(partialResultPane);
            buttonPane =  new JPanel();
            buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
                openButton = new JButton();
                openButton.setText(TEXT_OPEN);
                openButton.setActionCommand(ACTION_OPEN);
                openButton.addActionListener(buttonListener);
            buttonPane.add(Box.createHorizontalGlue());
            buttonPane.add(openButton);
            buttonPane.add(Box.createHorizontalGlue());
        this.add(buttonPane);
        this.add(Box.createVerticalGlue());
    }

    /*
     * Creeaza un panoul cu un label si o valoare
     */
    private JPanel createAlignedPane(JLabel label1,JLabel label2) {
        JPanel panel;
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(label1);
        panel.add(Box.createHorizontalGlue());
        panel.add(label2);

        label2.setForeground(DrawingConstants.COLOR_GENOM_BORDER);

        return panel;
    }

    /**
     * Ascultatorul pentru butonul de deschidere
     */
    ActionListener buttonListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();

            if (ACTION_OPEN.equals(cmd)) {
                MainResult mainResult = localManager.getMainResult();
                Dialog dialog = new JPartialResultSetDialog(globalManager
                        , TEXT_PARTIAL_RESULTS, true,
                        mainResult.partialResultSet);
                dialog.setVisible(true);
            }
        }
    };

}
