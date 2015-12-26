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

import ro.genomeartist.gui.mainpanels.query.JBestResultPane;
import ro.genomeartist.gui.mainpanels.query.JInfoQueryPanel;
import ro.genomeartist.gui.interfaces.ILocalManager;
import ro.genomeartist.gui.interfaces.IGlobalManager;
import ro.genomeartist.gui.controller.finalresult.FinalResultItem;
import ro.genomeartist.gui.controller.query.InfoQuery;
import ro.genomeartist.gui.controller.query.MainResult;
import java.awt.GridLayout;
import javax.swing.JPanel;

/**
 * Panoul de informatii legate de cautare + cel mai bun rezultat
 * @author iulian
 */
public class JSideInfoPane extends JPanel {
    private MainResult mainResult;
    private InfoQuery infoQuery;
    private FinalResultItem finalResultItem;
    private IGlobalManager globalManager;
    private ILocalManager localManager;

    //Structura ferestrei
    JPanel infoQueryPanel;
    JPanel bestResultPanel;

    /**
     * Construiesc un panou informativ
     * @param infoQuery
     * @param finalResultItem
     */
    public JSideInfoPane(IGlobalManager globalManager, ILocalManager localManager,
            MainResult mainResult, FinalResultItem finalResultItem) {
        this.mainResult = mainResult;
        this.infoQuery = mainResult.infoQuery;
        this.finalResultItem = finalResultItem;
        this.globalManager = globalManager;
        this.localManager = localManager;

        this.setLayout(new GridLayout(2, 1));
            infoQueryPanel = new JInfoQueryPanel(globalManager,localManager,infoQuery);
        this.add(infoQueryPanel);
            bestResultPanel = new JBestResultPane(globalManager,localManager,finalResultItem);
        this.add(bestResultPanel);
    }

    /**
     * Seteaza cel mai bun rezultat
     * @param bestResult
     */
    public void setBestResult(FinalResultItem bestResult) {
        if (bestResult != mainResult.bestResult) {
            //Salvez bestResult in modelul din spate
            mainResult.bestResult = bestResult;
            mainResult.hasBeenModified = true;
            //Afisez vizual
            this.remove(bestResultPanel);
            bestResultPanel = new JBestResultPane(globalManager,localManager,bestResult);
            this.add(bestResultPanel);
            this.revalidate();
        }
    }


}
