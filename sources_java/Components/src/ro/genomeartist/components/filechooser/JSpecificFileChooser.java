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

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * Un file chooser care expune minimul de metode
 * @author iulian
 */
public class JSpecificFileChooser {
    private JFileChooser fileChooser;

    /**
     * Creez un file chooser specific
     */
    public JSpecificFileChooser() {
        fileChooser = new JFileChooser();
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Getteri
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Returns a list of selected files if the file chooser is
     * set to allow multiple selection.
     */
    public File[] getSelectedFiles() {
        return fileChooser.getSelectedFiles();
    }

    /**
     * Returns the selected file. This can be set either by the
     * programmer via <code>setFile</code> or by a user action, such as
     * either typing the filename into the UI or selecting the
     * file from a list in the UI.
     * 
     * @see #setSelectedFile
     * @return the selected file
     */
    public File getSelectedFile() {
        return getSelectedFile(false);
    }

    /**
     * Returns the selected file. This can be set either by the
     * programmer via <code>setFile</code> or by a user action, such as
     * either typing the filename into the UI or selecting the
     * file from a list in the UI.
     * 
     * @see #setSelectedFile
     * @return the selected file
     */
    public File getSelectedFile(boolean useFilterExtension) {
        File file = fileChooser.getSelectedFile();
        
        //Modific extensia
        if (useFilterExtension) {
            FileFilter fileFilter = fileChooser.getFileFilter();
            if (fileFilter != null && fileFilter instanceof AbstractSpecificFileFilter) {
                AbstractSpecificFileFilter specificFileFilter = (AbstractSpecificFileFilter) fileFilter;
                File newFile = specificFileFilter.getFileWithPrefferedExtension(file);
                file = newFile;
            }
            
        }
        
        //Intorc fisierul 
        return file;
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Preagtirea chooserului
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
   /**
     * Setez daca utilizatorul poate vedea toate fisierele sau doar cele filtrate
     * @param b 
     */
    public void setAcceptAllFileFilterUsed(boolean b) {
        fileChooser.setAcceptAllFileFilterUsed(b);
    }    
    
    /**
     * Adaug un filtru din care se poate alege
     * @param filter 
     */
    public void addChoosableFileFilter(AbstractSpecificFileFilter filter) {
        fileChooser.addChoosableFileFilter(filter);
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Actiuniile de lansare chooser
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Lansez un panou de salvare fisier
     * @param parent
     * @return
     * @throws HeadlessException 
     */
    public int showSaveDialog(Component parent) throws HeadlessException {
        return fileChooser.showSaveDialog(parent);
    }

    /**
     * Lansez un panou de deschidere fisier
     * @param parent
     * @return
     * @throws HeadlessException 
     */
    public int showOpenDialog(Component parent) throws HeadlessException {
        return fileChooser.showOpenDialog(parent);
    }
}
