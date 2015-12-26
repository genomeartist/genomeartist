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

package ro.genomeartist.gui.mainpanels.settings;

import ro.genomeartist.gui.controller.settings.GeneralSettings;
import ro.genomeartist.gui.controller.settings.SearchFileSet;
import ro.genomeartist.gui.interfaces.IGlobalManager;
import javax.swing.JTabbedPane;

/**
 * Consturiesc panoul de setari
 * @author iulian
 */
public class JSettingsPane extends JTabbedPane {
    private GeneralSettings generalSettings;

    //Constante
    private static final String TEXT_PARAMS = "Parameters";
    private static final String TEXT_GENOM = "Genome files";
    private static final String TEXT_TRANSPOSON = "Transposon files";

    //Structura panoului
    JAlgorithmParametersPane algorithmParamsPane;
    JSearchFilesPane genomFilesPane;
    JSearchFilesPane transposonFilesPane;

    //Managerul global
    IGlobalManager globalManager;

    /**
     * Construiesc panoul de setari
     * @param generalSettings
     */
    public JSettingsPane(IGlobalManager globalManager,
            GeneralSettings generalSettings) {
        this.generalSettings = generalSettings;
        this.globalManager = globalManager;

            genomFilesPane = new JSearchFilesPane(globalManager, 
                    generalSettings.genomFiles, false);
        this.add(TEXT_GENOM,genomFilesPane);
            transposonFilesPane = new JSearchFilesPane(globalManager, 
                    generalSettings.transposonFiles, true);
        this.add(TEXT_TRANSPOSON,transposonFilesPane);
            algorithmParamsPane = new JAlgorithmParametersPane(generalSettings.algorithmParams);
        this.add(TEXT_PARAMS,algorithmParamsPane);
    }

    /**
     * Lansez actiunea de aplicare a schimbarilor de algoritmi
     * @return
     */
    public boolean fireActionApplyAlgorithmParams() {
        return algorithmParamsPane.commitChanges();
    }

    /**
     * Lansez actiunea de aplicare a schimbarilor de algoritmi
     * @return
     */
    public boolean fireActionApplyFileParams() {
        if (genomFilesPane.hasChanged() ||
                transposonFilesPane.hasChanged()) {
            SearchFileSet genomSet = genomFilesPane.getSearchFileSet();
            SearchFileSet transposonSet = transposonFilesPane.getSearchFileSet();
            SearchFileSet indexFisiere = new SearchFileSet(genomSet);
            indexFisiere.addAll(transposonSet);

            indexFisiere.saveToFile();

            return true;
        } else return false;
    }
    
}
