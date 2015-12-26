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
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author iulian
 */
public class DeleteMultipleFilesCallable extends AbstractProgressCallable<Boolean>{
    private Vector<SearchFile> deleteFilesVector;

    /**
     * Construiesc actiunea
     * @param deleteFilesVector
     */
    public DeleteMultipleFilesCallable(Vector<SearchFile> deleteFilesVector) {
        this.deleteFilesVector = deleteFilesVector;
    }


    /**
     * Fac actiunea propriu-zisa
     * @return
     * @throws Exception
     */
    public Boolean call() throws Exception {
        int filesNumber = deleteFilesVector.size();
        int step = 100/filesNumber;
        int infLimit = 0;
        int supLimit = step;

        Iterator<SearchFile> iteratorFisiere = deleteFilesVector.iterator();
        while (iteratorFisiere.hasNext()) {
            SearchFile searchFile = iteratorFisiere.next();

            this.setProgressRange(infLimit, supLimit);
            AbstractProgressCallable deleteIndividualFile =
                    ExternalLink.getDeleteSearchFileCallable(searchFile);
            deleteIndividualFile.setProgressInfoManager(this);
            deleteIndividualFile.call();
            infLimit = supLimit;
            supLimit += step;
        }

        //Default value
        return Boolean.TRUE;
    }

}
