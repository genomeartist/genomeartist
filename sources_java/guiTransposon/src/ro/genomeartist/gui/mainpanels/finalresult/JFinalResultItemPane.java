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

package ro.genomeartist.gui.mainpanels.finalresult;

import ro.genomeartist.gui.controller.finalresult.FinalResultItem;
import ro.genomeartist.gui.controller.finalresult.FinalResultItemAlignment;
import ro.genomeartist.gui.custompaint.JDefferedPanel;
import ro.genomeartist.gui.interfaces.IGlobalManager;
import ro.genomeartist.gui.utils.MyUtils;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author iulian
 */
public class JFinalResultItemPane extends JPanel {
    private FinalResultItem finalResultItem;

    //Constante
    private static final String TEXT_SCORE = "Score";

    //Structura ferestrei
     private JPanel scorePanel;
        private JLabel labelTextScore;
        private JLabel labelValueScore;
     private JPanel infoPanel;
     private JPanel summaryPanel;
     private JPanel swPanel;

     //Managerul global
     private IGlobalManager globalManager;

    /**
     * Construieste un panou pentru afisarea unui partial result
     */
    public JFinalResultItemPane(IGlobalManager globalManager, FinalResultItem finalResultItem) {
        super();
        this.globalManager = globalManager;
        this.finalResultItem = finalResultItem;

        //Setez layout-ul
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //Panoul cu scor
        scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.X_AXIS));
        scorePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
        scorePanel.add(Box.createHorizontalGlue());
            labelTextScore = new JLabel(TEXT_SCORE+" ");
            MyUtils.increaseLabelFont(labelTextScore,2);
            labelValueScore = new JLabel(finalResultItem.getScore()+"");
            MyUtils.setLabelBold(labelValueScore);
            MyUtils.increaseLabelFont(labelValueScore,4);
        scorePanel.add(labelTextScore);
        scorePanel.add(labelValueScore);
        scorePanel.add(Box.createHorizontalGlue());
        this.add(scorePanel);

        //Summary panel
        summaryPanel = new JDefferedPanel(finalResultItem);
        this.add(summaryPanel);

        swPanel = new JDefferedPanel(
                new FinalResultItemAlignment(finalResultItem));
        //this.add(swPanel);
        JScrollPane scrollPane = new JScrollPane(swPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER ,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setMinimumSize(swPanel.getMinimumSize());
        this.add(scrollPane);

        //Panourile cu intervalele
        infoPanel = new JIntervalMappingSetPane(globalManager, finalResultItem.getIntervalMappingSet());
        this.add(infoPanel);
    }
}
