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

package ro.genomeartist.gui.controller.query;

import ro.genomeartist.gui.controller.finalresult.FinalResultItem;
import ro.genomeartist.gui.controller.finalresult.FinalResultSet;
import ro.genomeartist.gui.controller.partialresult.PartialResultSet;
import java.io.File;

/**
 * Clasa ce compune raspunsul la un query
 * @author iulian
 */
public class MainResult {
    //Fisierul in care se afla
    public File backgroundFile;     //Fisierul corespunzator de pe disk
    public boolean isSaved;         //Daca a fost salvat vreodata
    public boolean hasBeenModified; //Daca cel mai bun rezultat a fost modificat

    //Parametrii de rezultat
    public InfoQuery infoQuery;
    public FinalResultSet finalResultSet;
    public PartialResultSet partialResultSet;
    public FinalResultItem bestResult;
}
