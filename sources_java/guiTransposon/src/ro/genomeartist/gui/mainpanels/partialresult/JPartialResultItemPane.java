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

package ro.genomeartist.gui.mainpanels.partialresult;

import ro.genomeartist.gui.controller.partialresult.PartialResultItem;
import ro.genomeartist.gui.controller.partialresult.PartialResultItemAlignment;
import ro.genomeartist.gui.custompaint.DrawingConstants;
import ro.genomeartist.gui.custompaint.JDefferedPanel;
import ro.genomeartist.gui.utils.MyUtils;
import java.awt.GridLayout;
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
public class JPartialResultItemPane extends JPanel {
    //Constante pentru titluri
    private static final String TITLE_ALIGNMENT = "Alignment";
    private static final String TITLE_MAPPING = "Mapping";
    //Constante pentru label-uri
    private static final String TEXT_SCORE = "Score";
    private static final String TEXT_LENGTH_QUERY = "Length in query";
    private static final String TEXT_LENGTH_LOCATION = "Length in location";
    private static final String TEXT_LOCATION = "Location";
    private static final String TEXT_POSITION_QUERY = "Position in query";
    private static final String TEXT_POSITION_LOCATION = "Position in location";
    private static final String TEXT_STRAND = "Strand";
    private static final String TEXT_STRAND_FORWARD = "forward";
    private static final String TEXT_STRAND_COMPLEMENTARY = "reverse";

    private PartialResultItem partialResultItem;

    //Structura ferestrei
    private JPanel infoPanel;
        private JPanel alignmentPanel;
            //Panoul cu date despre aliniere
            private JPanel scorePanel;
                private JLabel labelTextScore,labelValueScore;
            private JPanel lengthQueryPanel;
                private JLabel labelTextLengthQuery,labelValueLengthQuery;
            private JPanel lengthLocationPanel;
                private JLabel labelTextLengthLocation,labelValueLengthLocation;
        private JPanel mappingPanel;
            //Panoul cu date despre mapare
            private JPanel locationPanel;
                private JLabel labelTextLocation,labelValueLocation;
            private JPanel positionQueryPanel;
                private JLabel labelTextPositionQuery,labelValuePositionQuery;
            private JPanel positionLocationPanel;
                private JLabel labelTextPositionLocation,labelValuePositionLocation;
            private JPanel strandPanel;
                private JLabel labelTextStrand,labelValueStrand;
     private JPanel summaryPanel;
     private JPanel swPanel;


    /**
     * Construieste un panou pentru afisarea unui partial result
     */
    public JPartialResultItemPane(PartialResultItem partialResultItem) {
        super();
        this.partialResultItem = partialResultItem;

        //Setez layout-ul
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // <editor-fold defaultstate="collapsed" desc="Setez info panelul">
        //Creez infoPanel-ul
        infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(1, 2));
            //~~~~~~~Setez alignment Panel-ul~~~~~~
            alignmentPanel = new JPanel();
            alignmentPanel.setLayout(new BoxLayout(alignmentPanel, BoxLayout.Y_AXIS));
            alignmentPanel.setBorder(BorderFactory.createTitledBorder(TITLE_ALIGNMENT));
                //Score panel
                labelTextScore = new JLabel(TEXT_SCORE);
                MyUtils.setLabelBold(labelTextScore);
                labelValueScore = new JLabel(partialResultItem.getScore() + "");
                MyUtils.setLabelBold(labelValueScore);
                scorePanel = createAlignedPane(labelTextScore, labelValueScore);
            alignmentPanel.add(scorePanel);
                //Length query panel
                labelTextLengthQuery = new JLabel(TEXT_LENGTH_QUERY);
                labelValueLengthQuery = new JLabel(partialResultItem.getLengthQuery() + "");
                lengthQueryPanel = createAlignedPane(labelTextLengthQuery, labelValueLengthQuery);
            alignmentPanel.add(lengthQueryPanel);
                //Length location
                labelTextLengthLocation = new JLabel(TEXT_LENGTH_LOCATION);
                labelValueLengthLocation = new JLabel(partialResultItem.getLengthGenom() + "");
                lengthLocationPanel = createAlignedPane(labelTextLengthLocation, labelValueLengthLocation);
            alignmentPanel.add(lengthLocationPanel);
        infoPanel.add(alignmentPanel);
            //~~~~~~~Setez mapping Panel-ul~~~~~~
            mappingPanel = new JPanel();
            mappingPanel.setLayout(new BoxLayout(mappingPanel, BoxLayout.Y_AXIS));
            mappingPanel.setBorder(BorderFactory.createTitledBorder(TITLE_MAPPING));
                //Location panel
                labelTextLocation = new JLabel(TEXT_LOCATION);
                MyUtils.setLabelBold(labelTextLocation);
                labelValueLocation = new JLabel(partialResultItem.getFisierOrigine() + "");
                MyUtils.setLabelBold(labelValueLocation);
                locationPanel = createAlignedPane(labelTextLocation, labelValueLocation);
            mappingPanel.add(locationPanel);
                //Position in query panel
                labelTextPositionQuery = new JLabel(TEXT_POSITION_QUERY);
                labelValuePositionQuery = new JLabel(
                        partialResultItem.getPozitieStartQuery() + " .. "
                        + partialResultItem.getPozitieStopQuery());
                positionQueryPanel = createAlignedPane(labelTextPositionQuery, labelValuePositionQuery);
            mappingPanel.add(positionQueryPanel);
                //Position in location panel
                labelTextPositionLocation = new JLabel(TEXT_POSITION_LOCATION);
                labelValuePositionLocation = new JLabel(
                        partialResultItem.getPozitieStartGenom() + " .. "
                        + partialResultItem.getPozitieStopGenom());
                positionLocationPanel = createAlignedPane(labelTextPositionLocation, labelValuePositionLocation);
            mappingPanel.add(positionLocationPanel);
                //Position in location panel
                labelTextStrand = new JLabel(TEXT_STRAND);
                if (partialResultItem.isComplement()) {
                    labelValueStrand = new JLabel(TEXT_STRAND_COMPLEMENTARY);

                } else {
                    labelValueStrand = new JLabel(TEXT_STRAND_FORWARD);

                }
                strandPanel = createAlignedPane(labelTextStrand, labelValueStrand);
            mappingPanel.add(strandPanel);
        infoPanel.add(mappingPanel);
        this.add(infoPanel);
        // </editor-fold>
        
        summaryPanel = new JDefferedPanel(partialResultItem);
        this.add(summaryPanel);

        swPanel = new JDefferedPanel(
                new PartialResultItemAlignment(partialResultItem));
        this.add(new JScrollPane(swPanel));
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

        //Setez culoarea pentru label-uri
        if (partialResultItem.isTransposon())
            label2.setForeground(DrawingConstants.COLOR_TRANSPOSON_BORDER);
        else label2.setForeground(DrawingConstants.COLOR_GENOM_BORDER);

        return panel;
    }
}
