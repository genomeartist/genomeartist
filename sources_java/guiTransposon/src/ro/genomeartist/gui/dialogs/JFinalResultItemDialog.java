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

package ro.genomeartist.gui.dialogs;
import ro.genomeartist.components.dropdownbutton.JDropDownButton;
import ro.genomeartist.components.glasspane.IDoScreenshot;
import ro.genomeartist.components.glasspane.JGlasspaneMessage;
import ro.genomeartist.gui.controller.exporters.MyImageFilter;
import ro.genomeartist.gui.controller.exporters.MyPdfFilter;
import ro.genomeartist.gui.interfaces.IGlobalManager;
import ro.genomeartist.gui.controller.finalresult.FinalResultItem;
import ro.genomeartist.gui.custompaint.DrawingConstants;
import ro.genomeartist.gui.icons.JToolbarFinalResultIcons;
import ro.genomeartist.gui.interfaces.ILocalManager;
import ro.genomeartist.gui.mainpanels.finalresult.JFinalResultItemPane;
import ro.genomeartist.gui.utils.MyGlobalClasses;
import ro.genomeartist.gui.utils.MyUtils;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.*;

/**
 *  Dialogul wrapper peste panoul cu afisearea unui Partial Result
 * @author iulian
 */
public class JFinalResultItemDialog extends JFrame implements IDoScreenshot {
    //Constante pentru dimensiuni
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;
    private static final int BUTTON_WIDTH = 100;
    //Constante
    private static final String BUTTON_OK = "   Ok   ";
    //Constante pentru actiuni
    private static final String ACTION_OK = "ok";
    private static final String ACTION_SET = "set";
    
    //Managerii
    private IGlobalManager globalManager;
    private ILocalManager localManager;
    private FinalResultItem finalResultItem;

    //~~~~~~~~~ Top level panes ~~~~~~~~~/
    private Container contentPane;  //Containerul principal
    private JLayeredPane layeredPane; // Containerul ce tine meniul si contentPane-ul
    private JGlasspaneMessage glassPane;     //Glass pane pentru interceptat evenimente si login

    //Structura ferestrei
    private JToolBar toolbar;
    private JPanel middlePane;
    private JPanel bottomPane;
        private JButton buttonSet;
        private JButton buttonOk;

    //~~~~~~~~~Icon providerul~~~~~~~~~/
    JToolbarFinalResultIcons iconProvider;

    //~~~~~~~~~Constante pentru actiuni~~~~/
    private final static String NAME_EXPORT = "Export";
    private final static String NAME_EXPORT_IMAGE = "as Image";
    private final static String NAME_EXPORT_PDF = "as Pdf";
    private final static String NAME_PRINT = "Print";
    private final static String NAME_BEST = "Best Result";

    private final static String ACTION_EXPORT = NAME_EXPORT.toLowerCase();
    private final static String ACTION_EXPORT_IMAGE = NAME_EXPORT_IMAGE.toLowerCase();
    private final static String ACTION_EXPORT_PDF = NAME_EXPORT_PDF.toLowerCase();
    private final static String ACTION_PRINT = NAME_PRINT.toLowerCase();
    private final static String ACTION_BEST = NAME_BEST.toLowerCase();

    /**
     * Dialog de afisare partial result
     */
    public JFinalResultItemDialog(IGlobalManager globalManager, ILocalManager localManager,
            String title, FinalResultItem finalResultItem )  {
        //super(globalManager.getTheRootFrame(), title, false);
        super();
        setSize(FRAME_WIDTH,FRAME_HEIGHT);

        //Initialize the dialog
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(windowListener);
        setLayout(new BorderLayout());

        //Setez modul de aranjare a datelor
        layeredPane = this.getLayeredPane();
        contentPane = this.getContentPane();

        //~~~~~~~~~Scriu in GlassPane~~~~~~~~~~~~~/
        glassPane = new JGlasspaneMessage(
                DrawingConstants.EXPORTING_ROW1,
                DrawingConstants.EXPORTING_ROW2,this);
        this.setGlassPane(glassPane);
        glassPane.setVisible(false);

        //Obtin iconProviderul
        iconProvider = (JToolbarFinalResultIcons) MyGlobalClasses
                .get(JToolbarFinalResultIcons.GLOBAL_NAME);

        //Setez managerii
        this.globalManager = globalManager;
        this.localManager = localManager;
        this.finalResultItem = finalResultItem;

        //Initialize the dialog
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setLayout(new BorderLayout());

        //Middle pane
        middlePane = new JFinalResultItemPane(globalManager,finalResultItem);
        this.add(middlePane,BorderLayout.CENTER);

        //Make the toolbar
        toolbar = createToolBar();
        this.add(toolbar,BorderLayout.NORTH);

        //Making the buttons
        bottomPane = new JPanel();
        bottomPane.setLayout(new BoxLayout(bottomPane, BoxLayout.X_AXIS));
        bottomPane.add(Box.createHorizontalGlue());
            buttonOk = new JButton(BUTTON_OK);
            buttonOk.setActionCommand(ACTION_OK);
            buttonOk.addActionListener(buttonListener);
        bottomPane.add(buttonOk);
        bottomPane.add(Box.createHorizontalGlue());
        this.add(bottomPane,BorderLayout.SOUTH);

        //Set it's location
        setLocationRelativeTo(globalManager.getTheRootFrame());
    }

    /**
     * Creeaza butonul pentru actiunea de creare
     * @return
     */
    public JDropDownButton createButtonForExport() {
        JDropDownButton dropDownButton = null;
        JPopupMenu popupMenu  = new JPopupMenu();
        JMenuItem menuItem;

        //Adaug item-uri in meniu
        menuItem = new JMenuItem();
        menuItem.setText(NAME_EXPORT_IMAGE);
        menuItem.setActionCommand(ACTION_EXPORT_IMAGE);
        menuItem.addActionListener(buttonListener);
        menuItem.setIcon(iconProvider.getIcon(JToolbarFinalResultIcons.EXPORT_IMAGE));
        popupMenu.add(menuItem);

        //Adaug item-uri in meniu
        menuItem = new JMenuItem();
        menuItem.setText(NAME_EXPORT_PDF);
        menuItem.setActionCommand(ACTION_EXPORT_PDF);
        menuItem.addActionListener(buttonListener);
        menuItem.setIcon(iconProvider.getIcon(JToolbarFinalResultIcons.EXPORT_PDF));
        popupMenu.add(menuItem);

        dropDownButton = new JDropDownButton(
                NAME_EXPORT,
                iconProvider.getIcon(JToolbarFinalResultIcons.EXPORT),
                popupMenu);

        return dropDownButton;
    }

    /**
    *  <p style="margin-top: 0">
    *  Creeaza toolbarul principal
    *  </p>
    * @return Obiectul ce reprezinta toolbarul
    * @author iulian
    */
    private JToolBar createToolBar() {
        JToolBar localToolBar = new JToolBar("Still draggable");
        String iconPath,iconFolder;

        //Add the buttons
        JButton button = null;
        JDropDownButton dropButton = createButtonForExport();
        dropButton.setToolTipText("Export the result");
        localToolBar.add(dropButton);

        button = new JButton(NAME_PRINT);
            button.setActionCommand(ACTION_PRINT);
            button.addActionListener(buttonListener);
            button.setToolTipText("Save a result");
            button.setIcon(iconProvider.getIcon(JToolbarFinalResultIcons.PRINT));
            button.setFocusable(false);
        localToolBar.add(button);       
        
        localToolBar.addSeparator();

        button = new JButton(NAME_BEST);
            button.setActionCommand(ACTION_BEST);
            button.addActionListener(buttonListener);
            button.setToolTipText("Load a result");
            button.setIcon(iconProvider.getIcon(JToolbarFinalResultIcons.BEST_RESULT));
            button.setFocusable(false);
        localToolBar.add(button);
        
        //Setari pentru toolbar
        localToolBar.setFocusable(false); //nu am nevoie de focus aici
        localToolBar.setFloatable(false); //Daca toolbarul pluteste sau nu
        localToolBar.setRollover(false);
        return localToolBar;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/
    //             Metode ajutatoare   (Utils)              /
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/

    /**
     * Creeaza un screenshot dupa LayeredPane
     * @return Imagine LayeredPane-ului
     */
    public BufferedImage takeScreenshot() {
        BufferedImage image = new BufferedImage(getWidth(),getHeight(),
                            BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        layeredPane.paint(g2d);
        return image;
    }

    /**
     * Declanseaza layout managerul pentru layeredPane
     */
    public void refresh() {
        layeredPane.revalidate();
    }

    /**
     * Aprind sau sting glasspane-ul
     * @param flag
     */
    public void turnOnGlasspane(boolean flag) {
        if (flag == true) {
            glassPane.showGlassPane();
        } else {
            glassPane.setVisible(false);
        }
    }

    /**
     * Listenerul ce previne apasarea butonului de close
     */
    WindowListener windowListener = new WindowListener() {

        public void windowOpened(WindowEvent e) {
        }

        public void windowClosing(WindowEvent e) {
            if (!glassPane.isVisible())
                JFinalResultItemDialog.this.dispose();
        }

        public void windowClosed(WindowEvent e) {
        }

        public void windowIconified(WindowEvent e) {
        }

        public void windowDeiconified(WindowEvent e) {
        }

        public void windowActivated(WindowEvent e) {
        }

        public void windowDeactivated(WindowEvent e) {
        }
    };

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/
    //             Actiuniile inregistrate                  /
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/

    /**
     *  <p style="margin-top: 0">
     *  Clasa ce reprezinta un ascultator pe butoanele de ok si Cancel
     *  </p>
     * @author iulian
     */
    ActionListener buttonListener = new ActionListener() {
        /**
         *  <p style="margin-top: 0">
         *  Invoked when an action occurs.
         *  </p>
         * @author iulian
         */
            public void actionPerformed(ActionEvent e)
            {
                String cmd = e.getActionCommand();
                if (ACTION_OK.equals(cmd)) {
                    fireActionOk();
                } else
                if (ACTION_EXPORT_IMAGE.equals(cmd)) {
                    fireActionExportAsImage();
                } else
                if (ACTION_EXPORT_PDF.equals(cmd)) {
                    fireActionExportAsPdf();
                } else
                if (ACTION_PRINT.equals(cmd)) {
                    fireActionPrintResult();
                } else
                if (ACTION_BEST.equals(cmd)) {
                    fireActionBestResult();
                }
            }
        };

    /**
     * Actiunea de inchidere fereastra
     */
     public void fireActionOk() {
         JFinalResultItemDialog.this.dispose();
     }

     /**
      * Seteaza rezultatul curent ca find best result
      */
     public void fireActionExportAsImage() {
        JFileChooser fc;

        //Configureaz un file chooser
        fc = new JFileChooser();
        fc.addChoosableFileFilter(new MyImageFilter());
        fc.setAcceptAllFileFilterUsed(false);

        //Daca am ce sa salvez
        int returnVal = fc.showSaveDialog(globalManager.getTheRootFrame());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();

            //Compun numele fisierului
            String basename = MyUtils.getBasename(selectedFile);
            basename += "."+MyUtils.IMAGE_EXT;
            final File destination = new File(basename);
            final FinalResultItem sourceResult = finalResultItem;

            //Activez glasspane-ul
            turnOnGlasspane(true);

            //Creez si salvez imaginea pe un alt thread
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    localManager.exportResultAsImage(sourceResult, destination);

                    //Dezactivez glasspane-ul
                    turnOnGlasspane(false);
                }
            });
        }
     }

     /**
      * Seteaza rezultatul curent ca find best result
      */
     public void fireActionExportAsPdf() {
        JFileChooser fc;

        //Configureaz un file chooser
        fc = new JFileChooser();
        fc.addChoosableFileFilter(new MyPdfFilter());
        fc.setAcceptAllFileFilterUsed(false);

        //Daca am ce sa salvez
        int returnVal = fc.showSaveDialog(globalManager.getTheRootFrame());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();

            //Compun numele fisierului
            String basename = MyUtils.getBasename(selectedFile);
            basename += "."+MyUtils.PDF_EXT;
            final File destination = new File(basename);
            final FinalResultItem sourceResult = finalResultItem;

            //Activez glasspane-ul
            turnOnGlasspane(true);

            //Creez si salvez imaginea pe un alt thread
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    localManager.exportResultAsPdf(finalResultItem, destination);

                    //Dezactivez glasspane-ul
                    turnOnGlasspane(false);
                }
            });
        }
     }

     /**
      * Seteaza rezultatul curent ca find best result
      */
     public void fireActionPrintResult() {
        final FinalResultItem sourceResult = finalResultItem;

        //Activez glasspane-ul
        turnOnGlasspane(true);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               localManager.printResult(sourceResult);

                //Dezactivez glasspane-ul
                turnOnGlasspane(false);
            }
        });
     }

     /**
      * Seteaza rezultatul curent ca find best result
      */
     public void fireActionBestResult() {
         localManager.setBestResult(finalResultItem);
     }
}
