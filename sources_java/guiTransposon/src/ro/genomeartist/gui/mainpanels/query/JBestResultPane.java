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
import ro.genomeartist.gui.controller.finalresult.FinalResultItem;
import ro.genomeartist.gui.custompaint.DrawingConstants;
import ro.genomeartist.gui.custompaint.JDefferedPanel;
import ro.genomeartist.gui.dialogs.JFinalResultItemDialog;
import ro.genomeartist.gui.utils.MyUtils;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
/**
 *
 * @author iulian
 */
public class JBestResultPane extends JPanel {
    private FinalResultItem finalResultItem;
    private IGlobalManager globalManager;
    private ILocalManager localManager;

    //Constante
    private static final String TEXT_TITLE = "Best result";
    private static final String TEXT_SCORE = "Score";
    private static final String TEXT_OPEN = " Open result ";
    //Constante pt actiuni
    private static final String ACTION_OPEN = "open";
    //Constante afisare
    private static final int STRUT_HEIGHT = 800;

    //Structura panoului
    JPanel scorePane;
        JLabel labelTextScore;
        JLabel labelValueScore;
    JPanel summaryPane;
    JPanel buttonPane;
        JButton openButton;

    /**
     * Contructorul pentru rezultat
     * @param finalResultItem
     */
    public JBestResultPane(IGlobalManager globalManager, ILocalManager localManager,
            FinalResultItem finalResultItem) {
        this.finalResultItem = finalResultItem;
        this.globalManager = globalManager;
        this.localManager = localManager;

        if (finalResultItem != null) {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.setBorder(BorderFactory.createTitledBorder(TEXT_TITLE));
                    labelTextScore = new JLabel(TEXT_SCORE);
                    MyUtils.setLabelBold(labelTextScore);
                    labelValueScore = new JLabel(finalResultItem.getScore()+"");
                    MyUtils.setLabelBold(labelValueScore);
                scorePane = createAlignedPane(labelTextScore, labelValueScore);
            this.add(scorePane);
                summaryPane = new JDefferedPanel(finalResultItem);
            this.add(summaryPane);
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
            this.add(Box.createVerticalStrut(STRUT_HEIGHT));
        }

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
                JFrame dialog = new JFinalResultItemDialog(globalManager, localManager,
                      "Best result", finalResultItem);
                dialog.setTitle("Best result");
                dialog.setVisible(true);
            }
        }
    };

}
