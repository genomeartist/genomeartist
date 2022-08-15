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
import java.io.IOException;
import java.util.ArrayList;
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
     * Exporta cele mai bune rezultate ca tabel
     * @param sourceResult
     * @param lengthSeqExtract
     * @param lengthTolerance
     * @return 
     */
    public ArrayList<String[]> getResultsInsertionData(MainResult sourceResult, int lengthSeqExtract, int lengthTolerance, int numberOfResults) {
        return FinalResultExporter.getResultsInsertionData(sourceResult, lengthSeqExtract, lengthTolerance, numberOfResults);
    }
    
    /**
     * Exporta cele mai bune rezultate flancatoare ca fasta
     * @param sourceResult
     * @param useDoubleFlanks
     * @param lengthTolerance
     * @param lengthSeqExtract
     * @param folderPath
     * @param numberOfResults
     * @param toggleConsensus
     * @return 
     */
    public ArrayList<String> getResultsFlankingSeq(MainResult sourceResult, boolean useDoubleFlanks, int lengthSeqExtract, int lengthTolerance, int numberOfResults, boolean toggleConsensus, String folderPath) throws IOException {
        return FinalResultExporter.getResultsFlankingSeq(sourceResult, useDoubleFlanks, lengthSeqExtract, lengthTolerance, numberOfResults, toggleConsensus, folderPath);
    }
    
    /**
     * Exporta cele mai bune rezultate TDS ca fasta
     * @param sourceResult
     * @param lengthSeqExtract
     * @param lengthTolerance
     * @param numberOfResults
     * @param toggleConsensus
     * @return 
     */
    public ArrayList<String> getResultsTSD(MainResult sourceResult, int lengthSeqExtract, int lengthTolerance, int numberOfResults, boolean toggleConsensus) {
        return FinalResultExporter.getResultsTSD(sourceResult, lengthSeqExtract, lengthTolerance, numberOfResults, toggleConsensus);
    }

    /**
     * Printeaza rezultatul
     * @param exportResult
     */
    public void printResult(FinalResultItem exportResult) {
        FinalResultExporter.printReport(mainResult, exportResult);
    }
}

