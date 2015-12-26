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

package ro.genomeartist.gui.mainpanels;

import ro.genomeartist.gui.controller.finalresult.FinalResultItem;
import ro.genomeartist.gui.interfaces.ILocalManager;
import ro.genomeartist.gui.interfaces.IGlobalManager;
import ro.genomeartist.gui.controller.query.MainResult;
import ro.genomeartist.gui.controller.exporters.FinalResultExporter;
import ro.genomeartist.gui.mainpanels.finalresult.JFinalResultSetPane;
import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JPanel;
import javax.swing.JSplitPane;


/**
 * Panoul central al fiecarei cautari
 * Contine:
 *      - InfoQuery
 *      - BestResult
 *      - FinalResultSet
 *      - PartialResultSet
 * @author iulian
 */
public class JSearchResultPaneManager extends JPanel implements ILocalManager {
    private MainResult mainResult;
    private IGlobalManager globalManager;

    //Constante
    private static final double VALUE_PERCENTAGE = 0.34;
    private static final double VALUE_DISTRIBUTE = 0.1;

    //Structura panoului
    private JSplitPane splitPane;
        private JSideInfoPane sideInfoPane;
        private JFinalResultSetPane finalResultSetPane;

    /**
     * Construiesc panoul principal
     * @param mainResult
     */
    public JSearchResultPaneManager(IGlobalManager globalManager, MainResult mainResult) {
        this.mainResult = mainResult;
        this.globalManager = globalManager;
        this.setLayout(new BorderLayout());

        splitPane = new JSplitPane();
            sideInfoPane = new JSideInfoPane(globalManager, this,
                    mainResult, mainResult.bestResult);
            finalResultSetPane = new JFinalResultSetPane(globalManager, this,
                    mainResult.finalResultSet);
        splitPane.setLeftComponent(sideInfoPane);
        splitPane.setRightComponent(finalResultSetPane);
        splitPane.setDividerLocation(VALUE_PERCENTAGE);
        splitPane.setResizeWeight(VALUE_DISTRIBUTE);

        this.add(splitPane,BorderLayout.CENTER);
    }

    /**
     * Obtin rezulatul ce sta la baza cautarii
     * @return
     */
    public MainResult getMainResult() {
        return mainResult;
    }

    /**
     * Seteaza cel mai bun rezultat
     * @param bestResult
     */
    public void setBestResult(FinalResultItem bestResult) {
        sideInfoPane.setBestResult(bestResult);
    }

    /**
     * Exporta un rezultatul ca imagine
     * @param exportResult
     */
    public void exportResultAsImage(FinalResultItem exportResult, File destination) {
        FinalResultExporter.exportResultAsImage(exportResult, destination);
    }

    /**
     * Exporta un rezultatul ca imagine
     * @param exportResult
     */
    public void exportResultAsPdf(FinalResultItem exportResult, File destination) {
        FinalResultExporter.exportResultAsPdf(globalManager, destination,
                mainResult, exportResult);
    }

    /**
     * Printeaza rezultatul
     * @param exportResult
     */
    public void printResult(FinalResultItem exportResult) {
        FinalResultExporter.printReport(mainResult, exportResult);
    }
}
