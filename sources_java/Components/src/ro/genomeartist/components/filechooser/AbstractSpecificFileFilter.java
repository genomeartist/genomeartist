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

package ro.genomeartist.components.filechooser;

import ro.genomeartist.components.utils.FileUtils;
import java.io.File;
import javax.swing.filechooser.*;


/**
 * Clasa abstracta ce sta la baza fitrelor
 * @author iulian
 */
public abstract class AbstractSpecificFileFilter extends FileFilter {

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Metode abstracte (getteri)
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Metoda ce specifica daca se accepta directoare
     * @return 
     */
    public abstract boolean isAcceptDirectory();
    
    /**
     * Metoda ce valideaza o anumita extensie
     * @param extension
     * @return 
     */
    public abstract boolean isExtensionValid(String extension);

    
    /**
     * Obtine extensia prefereata pentru salvare
     * @return 
     */
    public abstract String getPrefferedExtension();
    
    /**
     * Metoda ce obtine descrierea acestui filtru
     * @return 
     */
    public abstract String getDescription();
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Implementarea filtrului
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Valideaza fisierele specificate
     * @param f
     * @return 
     */
    public boolean accept(File f) {
        //Cazul in care este director
        if (f.isDirectory()) {
            return isAcceptDirectory();
        }

        //Cazul in care e fisier
        String extension = FileUtils.getExtension(f);
        if (extension != null) {
            return isExtensionValid(extension);
        }

        //Valoarea default
        return false;
    }
    
    /**
     * Obtin un fisier care are extensia preferata de filtru
     * @param sourceFile
     * @return 
     */
    public File getFileWithPrefferedExtension(File sourceFile) {
        File newFile;
        
        //Convertesc extensia
        String basename = FileUtils.getBasename(sourceFile);
        basename += "."+getPrefferedExtension();
        newFile = new File(basename);
        
        //Intorc fisierul nou
        return newFile;
    }
}

