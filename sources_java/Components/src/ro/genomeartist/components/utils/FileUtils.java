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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author iulian
 */
public class FileUtils {
    
    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
    
    /**
     * Obtin numele fara extensie
     * @param f
     * @return
     */
    public static String getFilenameNoExt(File f) {
        String base = null;
        String s = f.getPath();
        int pos = s.lastIndexOf('.');
        int min = s.lastIndexOf('/');

        if (pos > min &&  pos < s.length() - 1) {
            base = s.substring(min+1, pos);
        } else return s;

        return base;
    }    
    
    /**
     * Obtin numele fara extensie
     * @param f
     * @return
     */
    public static String getBasename(File f) {
        String base = null;
        String s = f.getPath();
        int pos = s.lastIndexOf('.');
        int min = s.lastIndexOf('/');

        if (pos > min &&  pos < s.length() - 1) {
            base = s.substring(0, pos);
        } else return s;
        
        return base;
    }
    
    /**
    * Copiaza un fisier
    * @param sourceFile       Fisierul sursa
    * @param destinationFile  Fisierul destinatie
    */
    public static void copyFile(File sourceFile, File destinationFile) {
    try{
      InputStream in = new FileInputStream(sourceFile);
      OutputStream out = new FileOutputStream(destinationFile,false);

      byte[] buf = new byte[1024];
      int len;
      while ((len = in.read(buf)) > 0){
        out.write(buf, 0, len);
      }
      in.close();
      out.close();
    }
    catch(FileNotFoundException ex){
      System.out.println(ex.getMessage() + " in the specified directory.");
      System.exit(0);
    }
    catch(IOException e){
      System.out.println(e.getMessage());
    }
    }
}
