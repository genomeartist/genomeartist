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
import ro.genomeartist.gui.RootFrame;
import java.io.IOException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *  Clasa cu functii ajutatoare
 * @author iulian
 */
public class MyUtils {
    public final static String TRANSPOSON_EXT = "ga";
    public final static String RAW_EXT = "raw";
    public final static String GENE_SUFFIX = "_gene.fasta";
    public final static String IMAGE_EXT = "png";
    public final static String PDF_EXT = "pdf";
    public final static String CSV_EXT = "csv";
    public final static String ODS_EXT = "ods";
    public final static String FASTA_EXT = "fasta";
    public final static int COLUMNS_NUMBER = 12;

    //Unitati de masura timp
    private static final int MINUTES_2_SECONDS = 60;
    private static final int HOURS_2_SECONDS = 3600;

      /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *    FIle utils
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

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
    public static String getBasename(File f) {
        String base = null;
        String s = f.getPath();
        int pos = s.lastIndexOf('.');
        int min = s.lastIndexOf('/');
        if (min == -1)
        {
            min = s.lastIndexOf('\\');
        }

        if (pos > min &&  pos < s.length() - 1) {
            base = s.substring(0, pos);
        } else return s;
        
        return base;
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
        if (min == -1)
        {
            min = s.lastIndexOf('\\');
        }

        if (pos > min &&  pos < s.length() - 1) {
            base = s.substring(min+1, pos);
        } else return s;

        return base;
    }

    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *    GUI Components util
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Modifica fontul de la un label si il face bold
     * @param label
     */
    public static void setLabelBold(JLabel label) {
        Font oldFont = label.getFont();
        label.setFont(oldFont.deriveFont(Font.BOLD));
    }

    /**
     * Modifica fontul de la un label si il face mai mare
     * @param label
     */
    public static void increaseLabelFont(JLabel label,int offset) {
        Font oldFont = label.getFont();
        label.setFont(oldFont.deriveFont(oldFont.getStyle(), oldFont.getSize()+offset));
    }

     /**
     *  <p style="margin-top: 0">
     *  Creeaza un button cu proprietatiile date
     *  </p>
     * @param imageName Numele imaginii ce va fi reprezentarea butonului
     * @param actionCommand Stringul ce defineste unic actiunea butonului
     * @param toolTipText Textul ce se va afisa la tooltip
     * @param altText Textul ce se va afisa dac nu se gaseste imaginea
     * @param listener Obiectul ce va fi inregistrat ca ActionListener
     * @return Obiectul ce reprezinta butonul
     * @author iulian
     */
    public static JButton createButton(String imageName,
                                 String actionCommand,
                                 String toolTipText,
                                 String altText,
                                 ActionListener listener) {
        //Look for the image.
        URL imageURL = null;
        String imgLocation = null;
        if (imageName != null) {
            imgLocation = imageName;
            imageURL = RootFrame.class.getResource(imgLocation);
        }

        //Create and initialize the button.
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        button.addActionListener(listener);

        if (imageURL != null) {                      //image found
            button.setIcon(new ImageIcon(imageURL, altText));
            button.setText(altText);
        } else {                                     //no image found
            button.setText(altText);
            if (imageName != null) System.err.println("Resource not found: "
                               + imgLocation);
        }
        
        button.setFocusPainted(false);
        return button;
    }

    /**
     * Creez un obiect imagine pornind de la cale
     * @param imagePath
     * @return null daca calea nu este corecta
     */
    public static Image createImage(String imagePath) {
        //Look for the image.
        Image result = null;
        URL imageURL = null;
        String imgLocation = null;
        if (imagePath != null) {
            imgLocation = imagePath;
            imageURL = RootFrame.class.getResource(imgLocation);
            if (imageURL != null) {
                try {
                    result = ImageIO.read(imageURL);
                } catch (IOException ex) {
                    System.err.println("IO error");
                }
            } else {
                System.err.println("File: "+imagePath + " not found !");
            }


        }
        

        return result;
    }

    private static final Color LINK_COLOR = Color.blue;
    private static final Border LINK_BORDER = BorderFactory.createEmptyBorder(0, 0, 1, 0);
    private static final Border HOVER_BORDER = BorderFactory.createMatteBorder(0, 0, 1, 0, LINK_COLOR);

    private static class LinkMouseListener extends MouseAdapter{
        @Override
        public void mouseEntered(MouseEvent e){
            ((JComponent)e.getComponent()).setBorder(HOVER_BORDER);
        }

        @Override
        public void mouseReleased(MouseEvent e){
            ((JComponent)e.getComponent()).setBorder(LINK_BORDER);
        }

        @Override
        public void mouseExited(MouseEvent e){
            ((JComponent)e.getComponent()).setBorder(LINK_BORDER);
        }
    };

    public static JButton createLinkButton(String imageName,
                                 String actionCommand,
                                 String toolTipText,
                                 String altText,
                                 ActionListener listener) {

        JButton button = createButton(imageName,
                                 actionCommand,
                                 toolTipText,
                                 altText,
                                 listener);

        button.setBorder(LINK_BORDER);
        button.setForeground(LINK_COLOR);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setRequestFocusEnabled(false);
        button.setContentAreaFilled(false);
        button.addMouseListener(new LinkMouseListener());
        return button;
    }

    /*
     * Face expand complet la un arbore
     * expand = true - expandAll else collapseAll
     */
    public static void expandAll(JTree tree, boolean expand) {
        TreeNode root = (TreeNode)tree.getModel().getRoot();
    // Traverse tree from root
        expandAll(tree, new TreePath(root), expand);
    }

     /*
     * Face expand complet la un arbore pornind de la root
     * expand = true - expandAll else collapseAll
     */
    private static void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode)e.nextElement();
                if (!n.isLeaf())
                {
                    TreePath path = parent.pathByAddingChild(n);
                    expandAll(tree, path, expand);
                }
            }
        }
        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }

      /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *    Math stuff
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Obtin un nume de fisier random
     * @return
     */
    public static String getRandomFileName() {
        String charset = "!0123456789abcdefghijklmnopqrstuvwxyz";
        Random rand = new Random(System.currentTimeMillis());
        int length = 10;
        
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int pos = rand.nextInt(charset.length());
            sb.append(charset.charAt(pos));
        }

        String filename = sb.toString();
        filename += "."+TRANSPOSON_EXT;
        return filename;
    }
    
    /**
    * Retrieve current UTC timestamp in ISO 8601 format
    * @return string
    */
    public static String getCurrentUTCTimestampInISO8601Format() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calendar = Calendar.getInstance();
        return dateFormat.format(calendar.getTime());
    }


    /**
     * Converteste un vector de Integer in vector de int
     * @param irri Vectorul de Integer
     * @return vector de int
     */
    public static int[] toPrimitive(Integer[] irri) {
        int []array = new int[irri.length];
        for (int i=0;i<array.length;i++) {
            array[i] = irri[i].intValue();
        }
        return array;
    }

      /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *    Function testing
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public static void main(String[] args) {
        Pattern pattern =
            Pattern.compile("^@(\\w+)[\\s]*:[\\s]*([^:]*)");
        String searchString = "@info:test ytvv yvicv";
        String result = StringUtils.regexFind(pattern, searchString, 2);

        System.out.println(result);
    }
}
