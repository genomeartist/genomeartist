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
package ro.genomeartist.components.utils;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Vector;

/**
 *
 * @author iulian
 */
public class CommandLineArgumentParser {
    //Variabile de clasa
    private Vector<String> vectorParams;
    private Hashtable<String,String> hashtableOptions;
    
    /**
     * Construiesc un parser de argumente in linie de comanda
     * @param args 
     */
    public CommandLineArgumentParser(String[] args) {
        //Initializez variabilele
        vectorParams = new Vector<String>();
        hashtableOptions = new Hashtable<String,String>();
        
        //Construiesc parametrii
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-") || args[i].startsWith("/")) {
                int loc = args[i].indexOf("=");
                String key = (loc > 0) ? args[i].substring(1, loc) : args[i].substring(1);
                String value = (loc > 0) ? args[i].substring(loc+1) : "";
                hashtableOptions.put(key.toLowerCase(), value);
            }
            else {
                vectorParams.addElement(args[i]);
            }
        }
    }

    /**
     * Intreb daca exista optiunea opt
     * @param opt
     * @return 
     */
    public boolean hasOption(String opt) {
        return hashtableOptions.containsKey(opt.toLowerCase());
    }

    /**
     * Obtine valoare pentru optiunea opt
     * @param opt
     * @return 
     */
    public String getOption(String opt) {
        return hashtableOptions.get(opt.toLowerCase());
    }
    
    /**
     * Itereaza prin parametrii
     * @return 
     */
    public Iterator<String> getParametersIterator() {
        return vectorParams.iterator();
    }

    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Utils
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Metoda pentru afisare in consola
     * @return 
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        
        //Afisez optiunile
        stringBuilder.append("Number of options = ").append(hashtableOptions.size());
        stringBuilder.append("\n");
        for (Entry<String,String> hashEntry : hashtableOptions.entrySet()) {
            stringBuilder.append(hashEntry.getKey()).append("=").append(hashEntry.getValue());
            stringBuilder.append("\n");
        }
        
        //Afisez parametrii
        stringBuilder.append("Number of paremeters = ").append(vectorParams.size());
        stringBuilder.append("\n");
        for (String param : vectorParams) {
            stringBuilder.append(param);
            stringBuilder.append("\n");
        }
        
        return stringBuilder.toString();
    }    
}

