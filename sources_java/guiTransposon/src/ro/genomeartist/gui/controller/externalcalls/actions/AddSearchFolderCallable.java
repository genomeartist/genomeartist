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
import ro.genomeartist.gui.controller.settings.SearchFolder;
import ro.genomeartist.gui.utils.MyUtils;
import java.io.File;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author iulian
 */
public class AddSearchFolderCallable extends AbstractProgressCallable<Vector<SearchFile>> {
    private SearchFolder searchFolderRaw;
    private boolean isTransposon;

    /**
     * Pastrez parametrii
     * @param searchFolderRaw
     * @param isTransposon
     */
    public AddSearchFolderCallable(SearchFolder searchFolderRaw, boolean isTransposon) {
        this.searchFolderRaw = searchFolderRaw;
        this.isTransposon = isTransposon;
    }


    /**
     * Fac actiunea propriu-zis
     * @return
     * @throws Exception
     */
    public Vector<SearchFile> call() throws Exception {
        Vector<SearchFile> newSearchFiles = new Vector<SearchFile>();

        //parse the folder
        Vector<File> rawFiles = ExternalLink.getSequencesFromFolder(searchFolderRaw.folderLocation);
        int numberOfSeqFile = rawFiles.size();
        int step = 100/numberOfSeqFile;
        int infLimit = 0;
        int supLimit = step;

        Iterator<File> fileIterator = rawFiles.iterator();
        while (fileIterator.hasNext()) {
            File rawFile = fileIterator.next();
            File geneFile = ExternalLink.getGeneFileForRaw(rawFile);

            //Creez un fisier nou
            SearchFile newSearchFile = new SearchFile();
            newSearchFile.fileTitle = searchFolderRaw.fileTitle+"_"+
                    MyUtils.getFilenameNoExt(rawFile);
            newSearchFile.rawLocation = rawFile;
            newSearchFile.geneLocation = geneFile;
            newSearchFile.isTransposon = isTransposon;

            //LAnsez actiunea de adaugare fisier
            this.setProgressRange(infLimit, supLimit);
            AbstractProgressCallable runDataHashing =
                    ExternalLink.getAddSearchFileCallable(newSearchFile);
            runDataHashing.setProgressInfoManager(this);
            runDataHashing.call();
            infLimit = supLimit;
            supLimit += step;

            //Adaug fisierul la tabel
            newSearchFiles.add(newSearchFile);
        }

        //intorc vectorul
        return newSearchFiles;
    }

}
