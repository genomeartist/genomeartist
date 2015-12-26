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

package ro.genomeartist.components.singleton;

import java.util.Hashtable;

/**
 * SINGLETON
 * Clasa ce retine global anumite clase
 * Nu se poate instantia. Pentru a fi folosita trebuie sa fie extinsa
 * si suprascrisa metoda init();
 * @author iulian
 */
public abstract class GlobalClasses {
    private static Hashtable _registry = new Hashtable();

    //Constanta pentru fisierul de configurare
    private static final String HASHNAME = "_private_hash";

    //Initializare static a hashtable-ului
    static {
      Hashtable <String,IGlobalClass> hashtable = new Hashtable<String, IGlobalClass>();
      _registry.put(HASHNAME,hashtable);
    }

    /**
     * Constructor protected pentru a nu fi initializat
     */
    protected GlobalClasses() {
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
        //Trebuie suprascrisa aceasta metoda pentru a putea incarca
       //clasele
    }

    /**
     * Seteaza un nou parametru
     * @param key Nume setare
     * @param value Valoare
     */
    public static void put(IGlobalClass value) {
        Hashtable <String,IGlobalClass> hash = (Hashtable) _registry.get(HASHNAME) ;
        hash.put(value.getGlobalName(), value);
    }

    /**
     * Obtine valoarea pentru o setare
     * @param key Nume setare
     * @return Valoare
     */
    public static IGlobalClass get(String key) {
        Hashtable <String,IGlobalClass> hash = (Hashtable) _registry.get(HASHNAME) ;
        return hash.get(key);
    }
}