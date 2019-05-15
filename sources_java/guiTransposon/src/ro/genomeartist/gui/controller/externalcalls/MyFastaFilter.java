/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.genomeartist.gui.controller.externalcalls;

import ro.genomeartist.gui.utils.MyUtils;
import java.io.File;
import javax.swing.filechooser.*;

/**
 *
 * @author ghita
 */
public class MyFastaFilter extends FileFilter {

    //Accept specified files
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = MyUtils.getExtension(f);
        if (extension != null) {
            if (extension.equals(MyUtils.FASTA_EXT)) {
                    return true;
            } else {
                return false;
            }
        }

        return false;
    }

    //The description of this filter
    public String getDescription() {
        return "Fasta (.fasta)";
    }
}
