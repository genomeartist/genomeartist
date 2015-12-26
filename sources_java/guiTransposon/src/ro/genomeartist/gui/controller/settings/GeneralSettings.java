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

package ro.genomeartist.gui.controller.settings;

import java.util.Iterator;

/**
 *
 * @author iulian
 */
public class GeneralSettings {
    //Parametri de algoritm
    public AlgorithmParams algorithmParams;
    //Fisierele in care se cauta
    public SearchFileSet genomFiles;
    public SearchFileSet transposonFiles;


    /**
     * Incarca setarile din fisier
     */
    public void loadFromFile() {
        //Incarc algoritmii
        algorithmParams = new AlgorithmParams();
        algorithmParams.loadFromFile();

        //Incarc fisierele de genom si transposon
        genomFiles = new SearchFileSet();
        transposonFiles = new SearchFileSet();
        
        //Citesc fisierul de index
        SearchFileSet indexFisiere = new SearchFileSet();
        indexFisiere.loadFromFile();
        Iterator <SearchFile> iterator = indexFisiere.iterator();
        SearchFile itemFile;
        while (iterator.hasNext()) {
            itemFile = iterator.next();
            if (itemFile.isTransposon)
                transposonFiles.add(itemFile);
            else genomFiles.add(itemFile);
        }
    }

    /**
     * Salveaza setarile in fisiere
     * NU se reporneste serverul. Decat se face dump la configuratie pe disc
     */
    public void saveToFile() {
        if (algorithmParams != null)
            algorithmParams.saveToFile();

        SearchFileSet indexFisiere = new SearchFileSet(genomFiles);
        indexFisiere.addAll(transposonFiles);
        indexFisiere.saveToFile();
    }
}
