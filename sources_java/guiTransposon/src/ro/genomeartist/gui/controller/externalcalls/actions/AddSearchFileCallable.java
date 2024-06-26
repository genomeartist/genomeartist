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

package ro.genomeartist.gui.controller.externalcalls.actions;

import ro.genomeartist.components.swingworkers.progressworker.AbstractProgressCallable;
import ro.genomeartist.gui.controller.externalcalls.ExternalLink;
import ro.genomeartist.gui.controller.settings.SearchFile;
import ro.genomeartist.gui.utils.ReadOnlyConfiguration;
import java.io.File;

/**
 * Construiesc o actiune ce va aduaga
 * @author iulian
 */
public class AddSearchFileCallable extends AbstractProgressCallable<Boolean> {
    private SearchFile searchFile;

    /**
     * Construiesc actiunea
     * @param searchFile
     */
    public AddSearchFileCallable(SearchFile searchFile) {
        this.searchFile = searchFile;
    }

    /**
     * Fac actiunea propriu-zis
     * @return
     * @throws Exception
     */
    public Boolean call() throws Exception {
        String baseName = searchFile.fileTitle;

        //    pas 1. Se copiaza fisierul .raw in locatie
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        this.setProgressRange(0, 5);
        this.setProgressInfo("Adding "+baseName);
        String folderRaw = ReadOnlyConfiguration.getString("FOLDER_RAW");
        String numeRawNew = baseName+".raw";
        String pathRaw = folderRaw + numeRawNew;

        File newRaw = new File(pathRaw);
        ExternalLink.copyRawFile(searchFile.rawLocation, newRaw);
        searchFile.rawLocation = newRaw;
        this.setProgressValue(100); // of (0,5)

        //    pas 2. Se genereaza fisierul .hash pentru acest fisier
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        this.setProgressRange(5, 95);
        AbstractProgressCallable runDataHashing =
                ExternalLink.getDataHashingCallable(newRaw);
        runDataHashing.setProgressInfoManager(this);
        runDataHashing.call();

        //    pas 3. Se genereaza fisierul de gene pentru fisier
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        this.setProgressRange(95, 100);
        String folderGene = ReadOnlyConfiguration.getString("FOLDER_GENE");
        String numeGeneNew = baseName+".gene";
        String pathGene = folderGene + numeGeneNew;

        File newGene = new File(pathGene);
        //Daca nu se specifica gene, le setez pe 0
        if (searchFile.geneLocation != null) {
            searchFile.geneNumber = ExternalLink.fastaToGeneFile(searchFile.geneLocation,newGene);
        } else {
            ExternalLink.createEmptyFile(newGene);
            searchFile.geneNumber = 0;
        }
        searchFile.geneLocation = newGene;
        this.setProgressValue(100); // of (95,100)

        
        //Default value
        return Boolean.TRUE;
    }



}
