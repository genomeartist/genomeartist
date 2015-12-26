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

package ro.genomeartist.gui.interfaces;

import ro.genomeartist.gui.controller.finalresult.FinalResultItem;
import ro.genomeartist.gui.controller.query.MainResult;
import java.io.File;

/**
 *
 * @author iulian
 */
public interface ILocalManager {

    /**
     * Obtin rezultatul curent
     * @return
     */
    public MainResult getMainResult();

    /**
     * Seteaza rezultatul ca fiind cel mai bun
     * @param bestResult
     */
    public void setBestResult(FinalResultItem bestResult);


    /**
     * Exporta un rezultat in imagine
     */
     public void exportResultAsImage(FinalResultItem exportResult, File destination);

    /**
     * Exporta un rezultat in pdf
     */
     public void exportResultAsPdf(FinalResultItem exportResult, File destination);

    /**
     * Exporta un rezultat in pdf
     */
     public void printResult(FinalResultItem exportResult);
}
