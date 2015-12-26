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

package ro.genomeartist.gui.reports.compile;

import java.io.File;
import java.io.FileFilter;
import java.util.Iterator;
import java.util.Vector;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;

/**
 * Compilez toate rapoartele in fisier
 * @author iulian
 */
public class JCompileAllReportsByFolder {
    private static final String JRXML = "jrxml";
    private static final String JASPER = "jasper";

    /**
     * Compilez toate raportele
     */
    public void compileAllReports(String folderSursa,String folderDestinatie) {
        //Fac o structura de tip sursa-destinatie
        Vector <Node> rapoarte = new Vector<Node>();

        //Obtin folderele
        File templateFolder = new File(folderSursa);
        File[] templateFiles = templateFolder.listFiles(fileFilter);
        for (int i = 0; i < templateFiles.length; i++) {
            File file = templateFiles[i];
            String basename = getBasename(file.getName());
            String path = folderDestinatie+"/"+basename+"."+JASPER;
            rapoarte.add(new Node(file.getPath(), path));
        }
        
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // Iterez prin noduri si le generez
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Iterator <Node> iterator = rapoarte.iterator();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            try {
                System.out.println("COMPILING: "+node.sursa);
                JasperCompileManager.compileReportToFile(node.sursa, node.destinatie);
            } catch (JRException ex) {
                System.err.println("Could not compile "+node.sursa);
                ex.printStackTrace();
            }
        }

        //Inchei compilarea
        System.out.println("DONE");
    }

    /**
     * Obtin numele fara extensie
     * @param f
     * @return
     */
    public static String getBasename(String name) {
        String base = null;
        String s = name;
        int pos = s.lastIndexOf('.');
        int min = s.lastIndexOf('/');

        if (pos > min &&  pos < s.length() - 1) {
            base = s.substring(0, pos);
        } else return s;

        return base;
    }

    /**
     * Clasa cu un nod
     */
    private static class Node {
        public String sursa;
        public String destinatie;

        public Node(String sursa, String destinatie) {
            this.sursa = sursa;
            this.destinatie = destinatie;
        }
    }    

    /**
     * Clasa ce reprezinta un file filter
     * @author iulian
     */
    FileFilter fileFilter = new  FileFilter() {
        //Accept specified files
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return false;
            }

            String extension = getExtension(f);
            if (extension != null) {
                if (extension.equals(JRXML)) {
                        return true;
                } else {
                    return false;
                }
            }

            return false;
        }

        //The description of this filter
        public String getDescription() {
            return "Doar devize";
        }

        /*
         * Get the extension of a file.
         */
        public String getExtension(File f) {
            String ext = null;
            String s = f.getName();
            int i = s.lastIndexOf('.');

            if (i > 0 &&  i < s.length() - 1) {
                ext = s.substring(i+1).toLowerCase();
            }
            return ext;
        }
    };

    /**
     * Compilez toate rapoartele in fisier
     * @param args
     */
    public static void main(String[] args) {
        String folderSursa;
        String folderDestinatie;

        //Verific parametrii
        if (args.length < 2) {
            folderSursa = "./report/templates";
            folderDestinatie = "./report/compiled";
        } else {
            folderSursa = args[0];
            folderDestinatie = args[1];
        }

        //Compilez toate rapoartele
        JCompileAllReportsByFolder compiler = new JCompileAllReportsByFolder();
        compiler.compileAllReports(folderSursa,folderDestinatie);
    }
}


