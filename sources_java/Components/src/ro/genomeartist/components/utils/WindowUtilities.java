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

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

/**
 *
 * @author iulian
 */
public class WindowUtilities {
    private static Frame PHONY_FRAME = null;
    
    /**
     * Initialize un phony frame lazy
     * @return 
     */
    private static Frame getPhonyFrame() {
        if (PHONY_FRAME == null) {
            PHONY_FRAME = new Frame("phony");
        }
        
        //Phony frame
        return PHONY_FRAME;
    }
    
    /**
     * Get the root frame of any component
     * @param c
     * @return the top level frame sau un frame phony
     */
    public static Frame getRootFrame(Component c) {
        if (c != null) {
            Window window = SwingUtilities.getWindowAncestor(c);
            if (window instanceof Frame) {
                return (Frame) window;
            } else 
            if (window instanceof Dialog) {
                Dialog dialog = (Dialog) window;
                return getRootFrame(dialog.getOwner());
            }
        }
        
        //Valoare default
         return getPhonyFrame();
    }
    
     /**
     *  <p style="margin-top: 0">
     *         Initializeaza Look and Feel. Default este cel al sistemului.
     *      </p>
     * @param titlu Titlul ferestrei
     * @author iulian
     */
    public static void initLookAndFeel() {
        initLookAndFeel("System");
    }
    
     /**
     *  <p style="margin-top: 0">
     *         Initializeaza Look and Feel. Default este cel al sistemului.
     *      </p>
     * @param titlu Titlul ferestrei
     * @author iulian
     */
    public static void initLookAndFeel(String lafName) {
        initLookAndFeel(lafName, "Ocean");
    }
    
     /**
     *  <p style="margin-top: 0">
     *         Initializeaza Look and Feel. Default este cel al sistemului.
     *      </p>
     * @param titlu Titlul ferestrei
     * @author iulian
     */
    public static void initLookAndFeel(String lafName, String lafTheme) {
        String lookAndFeel = null;
        if (lafName != null) {
            //~~~~ Identifica look and feel-ul ~~~~~/
            if (lafName.equals("Metal")) {
                lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
              //  an alternative way to set the Metal L&F is to replace the
              // previous line with:
              // lookAndFeel = "javax.swing.plaf.metal.MetalLookAndFeel";
            }
            else if (lafName.equals("System")) {
                lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            }
            else if (lafName.equals("Motif")) {
                lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
            }
            else if (lafName.equals("GTK")) {
                lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
            }
            else {
                Logger.getLogger(WindowUtilities.class.getName()).log(Level.SEVERE, 
                        "Unexpected value of LOOKANDFEEL specified: {0}", lafName);
                lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
            }

            //~~~~ Seteaza Look and Feel-ul ~~~~~/
            try {
                UIManager.setLookAndFeel(lookAndFeel);
                // If L&F = "Metal", set the theme
               if (lafName.equals("Metal")) {
                  if (lafTheme.equals("DefaultMetal"))
                     MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
                  else if (lafTheme.equals("Ocean"))
                     MetalLookAndFeel.setCurrentTheme(new OceanTheme());
                  UIManager.setLookAndFeel(new MetalLookAndFeel());
                }
            }
            catch (ClassNotFoundException e) {
                Logger.getLogger(WindowUtilities.class.getName()).log(Level.SEVERE, 
                        "Couldn''t find class for specified look and feel:{0}"+"\n"+
                        "Did you include the L&F library in the class path?"+"\n"+
                        "Using the default look and feel.", lookAndFeel);
            }
            catch (UnsupportedLookAndFeelException e) {
                Logger.getLogger(WindowUtilities.class.getName()).log(Level.SEVERE, 
                        "Can''t use the specified look and feel ({0}) on this platform."+"\n"+
                        "Using the default look and feel.", lookAndFeel);
            }
            catch (Exception e) {
                Logger.getLogger(WindowUtilities.class.getName()).log(Level.SEVERE, 
                        "Couldn't get specified look and feel ("
                                   + lookAndFeel
                                   + "), for some reason."+"\n"+
                        "Using the default look and feel.", e);
            }
        }

        //~~~~~~~ Altereaza default-urile Look and Feel-ului

        //refs #481
        //Seteaza acelasi font pentru JTextField si JTextArea
        Font textfieldFont = UIManager.getFont("TextField.font");
        UIManager.put("TextArea.font", new FontUIResource( textfieldFont ));
        UIManager.put("TextPane.font", new FontUIResource( textfieldFont ));
    }
    
}
