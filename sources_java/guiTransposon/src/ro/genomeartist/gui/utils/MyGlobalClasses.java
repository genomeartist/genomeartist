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

import ro.genomeartist.components.singleton.GlobalClasses;
import ro.genomeartist.gui.icons.JBrandingImages;
import ro.genomeartist.gui.icons.JToolbarFereastraIcons;
import ro.genomeartist.gui.icons.JToolbarFinalResultIcons;
import ro.genomeartist.gui.icons.JToolbarFisiereIcons;

/**
 * SINGLETON
 * Clasa ce retine global anumite clase
 * @author iulian
 */
public class MyGlobalClasses extends GlobalClasses {
    /**
     * Constructor protected pentru a nu fi initializat
     */
    protected MyGlobalClasses() {
    }


    /**
     * Initializeaza hashtable-ul la valorile default
     *  sau le citeste dintr-un loc
     */
    public static void init() {
        //Initializarea default (incarc anumite clase)

        //Incarc clasele pentru icone
        put(new JToolbarFereastraIcons());
        put(new JToolbarFisiereIcons());
        put(new JToolbarFinalResultIcons());
        put(new JBrandingImages());
    }

}