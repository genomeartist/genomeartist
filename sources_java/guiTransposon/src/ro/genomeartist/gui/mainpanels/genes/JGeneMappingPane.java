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

package ro.genomeartist.gui.mainpanels.genes;

import ro.genomeartist.gui.controller.genes.GeneItemWrapper;
import ro.genomeartist.gui.custompaint.JDefferedPanel;
import ro.genomeartist.gui.interfaces.IGlobalManager;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

/**
 *
 * @author iulian
 */
public class JGeneMappingPane extends JPanel {
    private IGlobalManager globalManager;
    private GeneItemWrapper geneItemWrapper;

    //Structura ferestrei
    private JSplitPane splitPane;
        private JDefferedPanel mappingPanel;
        private JGeneVectorPane geneSetPane;

    //Constante de afisare
    private static final int DIVIDER_LOCATION = 120;


    public JGeneMappingPane(IGlobalManager globalManager, GeneItemWrapper geneItemWrapper) {
        this.globalManager = globalManager;
        this.geneItemWrapper = geneItemWrapper;

        //Setez layout-ul
        setLayout(new BorderLayout());

        //Setez splitpane-ul
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(DIVIDER_LOCATION);

        //Panou cu mapare gene
        mappingPanel = new JDefferedPanel(geneItemWrapper);
        splitPane.setTopComponent(new JScrollPane(mappingPanel));

        //Tabelul cu gene
        geneSetPane = new JGeneVectorPane(geneItemWrapper.getFullGeneVector());
        splitPane.setBottomComponent(geneSetPane);

        //Adaug splitpane-ul
        this.add(splitPane,BorderLayout.CENTER);
    }



}
