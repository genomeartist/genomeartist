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

import ro.genomeartist.gui.utils.ReadOnlyConfiguration;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author iulian
 */
public class SearchFileSet extends  Vector <SearchFile> {
    //constante
    private static final String INDICATOR_GENOM = "G";
    private static final String INDICATOR_TRANSPOSON = "T";

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/
    //                   Constructor area                   /
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/

    public SearchFileSet() {
    }


    public SearchFileSet(Collection<? extends SearchFile> c) {
        super(c);
    }


    @Override
    public synchronized boolean add(SearchFile e) {
        if (!super.contains(e))
            return super.add(e);
        else return false;
    }


    @Override
    public synchronized boolean addAll(Collection<? extends SearchFile> c) {
        return super.addAll(c);
    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/
    //                 Serialization/deserialization        
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/
    /**
     * Incarc idexul dintr-un fisier
     */
    public void loadFromFile() {
        try {
            String paramfile = ReadOnlyConfiguration.getString("PARAM_FISIERE");
            BufferedReader fileReader = new BufferedReader(new FileReader(paramfile));
            String line;            //O linie din fisier
            StringTokenizer st,aux; //Tokenizeri
            String token;  //Valoare din stanga si dreapta egalului
            String key,value;             //Valorile ce vor fi retinute
            SearchFile searchFile;
            while ((line = fileReader.readLine()) != null) {
                //Linia ce incepe cu "#" nu va fi interpretata
                if (!line.startsWith("#")){
                    st = new StringTokenizer(line," \t");

                    //Creez un nou fisier
                    searchFile = new SearchFile();
                    searchFile.rawLocation = new File(st.nextToken());
                    searchFile.geneLocation = new File(st.nextToken());
                    searchFile.geneNumber = Integer.parseInt(st.nextToken()); //consum nr de gene
                    searchFile.fileTitle = st.nextToken(); //numele fisierului
                    token = st.nextToken(); //genom sau transposon
                    if (INDICATOR_GENOM.equals(token)) {
                        searchFile.isTransposon = false;
                    } else
                    if (INDICATOR_TRANSPOSON.equals(token)) {
                        searchFile.isTransposon = true;
                    }

                    //Adaug fisierul la set
                    this.add(searchFile);
                }
            }
            fileReader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ResultSet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (java.io.IOException ex) {
            Logger.getLogger(ResultSet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * Salveaza configuratia in fisier
     * @param configFile
     */
    public void saveToFile() {
        try {
            String paramfile = ReadOnlyConfiguration.getString("PARAM_FISIERE");
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(paramfile, false));

            //Scriu headerul
            fileWriter.write("# ");
            fileWriter.write("[SEQ PATH]");
            fileWriter.write("\t");
            fileWriter.write("[GENE PATH]");
            fileWriter.write("\t");
            fileWriter.write("[GENE NUMBER]");
            fileWriter.write("\t");
            fileWriter.write("[FILE TITLE]");
            fileWriter.write("\t");
            fileWriter.write("[GENOM/TRANSPOSON]");
            fileWriter.newLine();

            //Scriu fisierele
            Iterator <SearchFile> iterator = this.iterator();
            SearchFile searchFile;
            while (iterator.hasNext()) {
                searchFile = iterator.next();
                //NAME_OFFSET
                fileWriter.write(searchFile.rawLocation.getPath());
                fileWriter.write("\t");
                fileWriter.write(searchFile.geneLocation.getPath());
                fileWriter.write("\t");
                fileWriter.write(searchFile.geneNumber+"");
                fileWriter.write("\t");
                fileWriter.write(searchFile.fileTitle);
                fileWriter.write("\t");
                if (searchFile.isTransposon)
                    fileWriter.write(INDICATOR_TRANSPOSON);
                else fileWriter.write(INDICATOR_GENOM);
                fileWriter.newLine();
            }

            //END
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(ReadOnlyConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
