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

package ro.genomeartist.gui.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SINGLETON
 * Clasa ce retine global valorile pentru toate configuratiile
 * @author iulian
 */
public class ReadWriteConfiguration {
    private static Hashtable _registry = new Hashtable();

    //Constanta pentru fisierul de configurare
    private static final String HASHNAME = "_readwrite_hash";
    public static final String CONFIGFILE = "paramsInterface.txt";

    //Initializare static a hashtable-ului
    static {
      Hashtable <String,Object> hashtable = new Hashtable<String, Object>();
      _registry.put(HASHNAME,hashtable);
    }

    /**
     * Constructor protected pentru a nu fi initializat
     */
    protected ReadWriteConfiguration() {
    }

    /**
     * Obtin instanta static a clasei de configurare
     * @param byname
     * @return
     */
   public static Hashtable getInstance() {
      return (Hashtable)(_registry.get(HASHNAME));
   }


    /**
     * Initializeaza hashtable-ul la valorile default
     *  sau le citeste dintr-un loc
     */
    public static void init() {
        //Initializarea default
        loadFromFile(CONFIGFILE);
    }

    /**
     * Pune setarile in fisier
     */
    public static void commitSettings() {
        saveToFile(CONFIGFILE);
    }

    /**
     * Incarc setarile dintr-un fisier
     */
    public static void loadFromFile(String configFile) {
        try {
            Hashtable <String,Object> hash = (Hashtable) _registry.get(HASHNAME);
            BufferedReader fileReader = new BufferedReader(new FileReader(configFile));
            String line;            //O linie din fisier
            StringTokenizer st,aux; //Tokenizeri
            String leftToken,rightToken;  //Valoare din stanga si dreapta egalului
            String key,value;             //Valorile ce vor fi retinute
            while ((line = fileReader.readLine()) != null) {
                //Linia ce incepe cu "#" nu va fi interpretata
                if (!line.startsWith("#") && (line.indexOf("=")!=-1)){
                    st = new StringTokenizer(line,"=");
                    leftToken = st.nextToken();
                    rightToken = st.nextToken();
                    
                    //Pregatesc cheia
                    key=new String();
                    aux = new StringTokenizer(leftToken, " \t");
                    if (aux.hasMoreTokens()) key += aux.nextToken();
                    while (aux.hasMoreTokens()) {
                        key += " "+aux.nextToken();
                    }

                    //Pregatesc valoare
                    value=new String();
                    aux = new StringTokenizer(rightToken, " \t");
                    if (aux.hasMoreTokens()) value += aux.nextToken();
                    while (aux.hasMoreTokens()) {
                        value += " "+aux.nextToken();
                    }

                    hash.put(key, value);
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
    public static void saveToFile(String configFile) {
        try {
            Hashtable <String,Object> hash = (Hashtable) _registry.get(HASHNAME);
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(configFile, false));
            Set<Map.Entry<String,Object>> set = hash.entrySet();
            Iterator <Map.Entry<String,Object>> it = set.iterator();
            Map.Entry<String,Object> entry;
            while (it.hasNext()) {
                entry = it.next();
                fileWriter.write(entry.getKey());
                fileWriter.write(" = ");
                fileWriter.write(entry.getValue().toString());
                fileWriter.newLine();
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(ReadWriteConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Seteaza un nou parametru
     * @param key Nume setare
     * @param value Valoare
     */
    public static void put(String key, Object value) {
        Hashtable <String,Object> hash = (Hashtable) _registry.get(HASHNAME) ;
        hash.put(key, value);
    }

    /**
     * Obtine valoarea pentru o setare
     * @param key Nume setare
     * @return Valoare
     */
    public static Object get(String key) {
        Hashtable <String,Object> hash = (Hashtable) _registry.get(HASHNAME) ;
        return hash.get(key);
    }

    /**
     * Obtine valoarea pentru o setare
     * @param key Nume setare
     * @return Valoare
     */
    public static String getString(String key) {
        Hashtable <String,Object> hash = (Hashtable) _registry.get(HASHNAME) ;
        return (String)hash.get(key);
    }

    public static void main(String[] args) {
        ReadWriteConfiguration.init();

        System.out.println(ReadWriteConfiguration.get("NUMBER_OFFSET"));
        ReadWriteConfiguration.put("SCALE", new Integer(4));
        ReadWriteConfiguration.saveToFile("confignew.txt");
    }
}
