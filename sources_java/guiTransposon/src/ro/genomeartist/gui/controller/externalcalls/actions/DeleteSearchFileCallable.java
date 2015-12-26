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
import ro.genomeartist.gui.controller.settings.SearchFile;
import java.io.File;

/**
 *
 * @author iulian
 */
public class DeleteSearchFileCallable extends AbstractProgressCallable<Boolean>{
    private SearchFile searchFile;

    /**
     * COnstruiesc actiunea de stergere
     * @param searchFile
     */
    public DeleteSearchFileCallable(SearchFile searchFile) {
        this.searchFile = searchFile;
    }

    /**
     * Fac actiunea propriu-zisa
     * @return
     * @throws Exception
     */
    public Boolean call() throws Exception {
        //Sterg fisierul de gene
        searchFile.geneLocation.delete();

        //Sterg fisierul .hash. Numele este identic cu fisierul .raw
        //doar ca are extensia .hash
        String pathRaw = searchFile.rawLocation.getPath();
        int indexOfPoint = pathRaw.lastIndexOf(".");
        if (indexOfPoint == -1) indexOfPoint = pathRaw.length();
        String pathHash = pathRaw.substring(0, indexOfPoint)+".hash";

        //Ma leg la fisierul .hash
        File hashLocation = new File(pathHash);

        //Sterg fisierele .raw si .hash
        hashLocation.delete();
        searchFile.rawLocation.delete();
        this.setProgressValue(100);

        //Default value
        return Boolean.TRUE;
    }

}
