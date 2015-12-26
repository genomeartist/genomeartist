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

package ro.genomeartist.components.filechooser.filters;

import ro.genomeartist.components.filechooser.AbstractSpecificFileFilter;

/**
 *
 * @author iulian
 */
public class ImageSpecificFileFilter extends AbstractSpecificFileFilter {
    private static final String JPG = "jpg";
    private static final String PNG = "png";
    private static final String GIF = "gif";
    private static final String BMP = "gif";
    
    
    /**
     * Metoda ce specifica daca se accepta directoare
     * @return 
     */
    @Override
    public boolean isAcceptDirectory() {
        return true;
    }

    /**
     * Metoda ce valideaza o anumita extensie
     * @param extension
     * @return 
     */
    @Override
    public boolean isExtensionValid(String extension) {
        if ((JPG.compareToIgnoreCase(extension) == 0) ||
            (PNG.compareToIgnoreCase(extension) == 0) ||
            (GIF.compareToIgnoreCase(extension) == 0) ||
            (BMP.compareToIgnoreCase(extension) == 0)) {
            return true;
        }
        
        //Valoarea default
        return false;
    }
    
    /**
     * Obtine extensia prefereata pentru salvare
     * @return 
     */
    @Override
    public String getPrefferedExtension() {
        return JPG;
    }
    
    /**
     * Metoda ce obtine descrierea acestui filtru
     * @return 
     */
    public String getDescription() {
        StringBuilder stringBuilder = new StringBuilder();
        
        //Compun descrierea
        stringBuilder.append("Imagini (");
        stringBuilder.append(JPG).append(", ");
        stringBuilder.append(PNG).append(", ");
        stringBuilder.append(GIF).append(", ");
        stringBuilder.append(BMP).append(")");
        
        //Intorc stringul
        return stringBuilder.toString();
    }    
}

