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

package ro.genomeartist.gui;

import ro.genomeartist.components.glasspane.IDoScreenshot;
import ro.genomeartist.components.glasspane.JGlasspaneMessage;
import ro.genomeartist.components.modalpanel.progresspanel.JProgressPanel;
import ro.genomeartist.components.swingworkers.progressworker.AbstractProgressCallable;
import ro.genomeartist.components.swingworkers.progressworker.JProgressSwingWorker;
import ro.genomeartist.components.tabbedpane.JDragableTabbedPane;
import ro.genomeartist.gui.utils.ReadOnlyConfiguration;
import ro.genomeartist.gui.utils.ReadWriteConfiguration;
import ro.genomeartist.gui.controller.externalcalls.GenomeArtistFileFilter;
import ro.genomeartist.gui.controller.externalcalls.MyTableFilter;
import ro.genomeartist.gui.controller.externalcalls.MyFastaFilter;
import ro.genomeartist.gui.controller.externalcalls.ExternalLink;
import ro.genomeartist.gui.controller.query.MainResult;
import ro.genomeartist.gui.controller.query.SearchQuery;
import ro.genomeartist.gui.controller.settings.GeneralSettings;
import ro.genomeartist.gui.custompaint.DrawingConstants;
import ro.genomeartist.gui.dialogs.JSearchQueryDialog;
import ro.genomeartist.gui.dialogs.JSettingsDialog;
import ro.genomeartist.gui.icons.JBrandingImages;
import ro.genomeartist.gui.interfaces.IGlobalManager;
import ro.genomeartist.gui.icons.JToolbarFereastraIcons;
import ro.genomeartist.gui.mainpanels.JSearchResultPaneManager;
import ro.genomeartist.gui.utils.MyGlobalClasses;
import ro.genomeartist.gui.utils.JMyBoolean;
import ro.genomeartist.gui.utils.MyUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.JCheckBox;
import ro.genomeartist.gui.controller.exporters.FinalResultExporter;
import ro.genomeartist.gui.dialogs.JExportDialog;
import ro.genomeartist.gui.utils.NaturalOrderComparator;

/**
 *
 * @author iulian, ghita
 */
public class RootFrame extends JFrame implements IDoScreenshot,IGlobalManager {

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/
    //      Sectiunea in care definesc variabile            /
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/
    private final static String LOOKANDFEEL = "FlatLaf"; //Look and feel-ul folosit
    private final static String THEME = "Ocean";     // Tema folosita pentru Metal
    private static boolean DARK_THEME = false;

    //~~~~~~~~~ Top level panes ~~~~~~~~~/
    private Container contentPane;  //Containerul principal
    private JLayeredPane layeredPane; // Containerul ce tine meniul si contentPane-ul
    private JGlasspaneMessage glassPane;     //Glass pane pentru interceptat evenimente si login

    //~~~~~~~~~ Top level components ~~~~~~~/
    private JMenuBar menuBar;
    private JToolBar toolBar;
    //~~~~~~~~~~Middle level ~~~~~~~~~~~~/
    private JTabbedPane tabbedpane; //Panoul central de documente
    private JPanel middlePane;

    //~~~~~~~~~Un file chooser~~~~~~~~~/
    private JFileChooser fc;

    //~~~~~~~~~Constante pentru actiuni~~~~/
    private final static String NAME_SEARCH = "New Search";
    private final static String NAME_SAVE = "Save";
    private final static String NAME_LOAD = "Load";
    private final static String NAME_SAVE_AS = "Save As";
    private final static String NAME_SETTINGS = "Settings";
    private final static String NAME_EXPORT = "Export Data to File";
    private final static String NAME_THEME = "Dark Theme";
    private final static String NAME_EXIT = "Exit";

    private final static String ACTION_SEARCH = NAME_SEARCH.toLowerCase();
    private final static String ACTION_SAVE = NAME_SAVE.toLowerCase();
    private final static String ACTION_LOAD = NAME_LOAD.toLowerCase();
    private final static String ACTION_SAVE_AS = NAME_SAVE_AS.toLowerCase();
    private final static String ACTION_SETTINGS = NAME_SETTINGS.toLowerCase();
    private final static String ACTION_EXPORT = NAME_EXPORT.toLowerCase();
    private final static String ACTION_THEME = NAME_THEME.toLowerCase();
    private final static String ACTION_EXIT = NAME_EXIT.toLowerCase();

    //~~~~~~~~~Obiecte proprii~~~~/
    GeneralSettings generalSettings;

    /**
     * Contructorul pentru fereastra principala
     */
    public RootFrame() {
        super ( DrawingConstants.APP_NAME);

        //Citesc fisierele de configurare
        ReadOnlyConfiguration.init();
        ReadWriteConfiguration.init();
        MyGlobalClasses.init();
        
        DARK_THEME = Boolean.parseBoolean((String)ReadWriteConfiguration.get("THEME"));

        //Incarc configurarile la server
        generalSettings = new GeneralSettings();
        generalSettings.loadFromFile();

        //Initializez fereastra
        initLookAndFeel();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        this.setSize(dimension);
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.setUndecorated(false);
        initLogoImages();

        //Setez modul de aranjare a datelor
        layeredPane = this.getLayeredPane();
        contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());

        //~~~~~~~~~Scriu in GlassPane~~~~~~~~~~~~~/
        glassPane = new JGlasspaneMessage(DrawingConstants.TEXT_QUERY_ROW1,
                DrawingConstants.TEXT_QUERY_ROW2,this);
        this.setGlassPane(glassPane);
        glassPane.setVisible(false);

        //~~~~~~~~~~~Setez meniul si toolbarul~~~~~~~~/
        //Creez meniurile de sus
        menuBar = createMenuBar();
        menuBar.setName("menubar");
        this.setJMenuBar(menuBar);

        //Creez toolbarul
        toolBar = createToolBar();
        toolBar.setName("toolbar");
        contentPane.add(toolBar, BorderLayout.NORTH);

        //~~~~~~~~~Scriu in ContentPane~~~~~~~~~~~~~/
        middlePane = createMiddle();
        contentPane.add(middlePane);

        //~~~~~~~~~Fac ultimele setari~~~~~~~~~~~~~/

        //Initializez ascultatorii
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(windowListener);
    }

    /**
     * Initializez pachetele de logo
     */
    private void initLogoImages() {
        //Iconita din stanga sus
        String iconsFolder,iconsPath;
        Image image;
        Vector<Image> imagePack = new Vector<Image>();
        iconsFolder = ReadOnlyConfiguration.getString("BRANDING_FOLDER");

        //Load image
        iconsPath = iconsFolder + ReadOnlyConfiguration.getString("LOGO_24");
        image = MyUtils.createImage(iconsPath);
        imagePack.add(image);

        //Load image
        iconsPath = iconsFolder + ReadOnlyConfiguration.getString("LOGO_36");
        image = MyUtils.createImage(iconsPath);
        imagePack.add(image);

        //Load image
        iconsPath = iconsFolder + ReadOnlyConfiguration.getString("LOGO_48");
        image = MyUtils.createImage(iconsPath);
        imagePack.add(image);

        //Load image
        iconsPath = iconsFolder + ReadOnlyConfiguration.getString("LOGO_64");
        image = MyUtils.createImage(iconsPath);
        imagePack.add(image);

        //Load image
        iconsPath = iconsFolder + ReadOnlyConfiguration.getString("ARTIST_24");
        image = MyUtils.createImage(iconsPath);
        imagePack.add(image);

        //Load image
        iconsPath = iconsFolder + ReadOnlyConfiguration.getString("ARTIST_36");
        image = MyUtils.createImage(iconsPath);
        imagePack.add(image);

        //Setez pachetul de imagini
        this.setIconImages(imagePack);
    }
    
     /**
     *  <p style="margin-top: 0">
     *         Initializeaza Look and Feel. Default este cel al sistemului.
     *      </p>
     * @param titlu Titlul ferestrei
     * @author iulian
     */
    private void initLookAndFeel() {
        String lookAndFeel = null;
        if (LOOKANDFEEL != null) {
            if (LOOKANDFEEL.equals("FlatLaf")) {
                lookAndFeel = "FlatLaf";
            }
            else if (LOOKANDFEEL.equals("Metal")) {
                lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
              //  an alternative way to set the Metal L&F is to replace the
              // previous line with:
              // lookAndFeel = "javax.swing.plaf.metal.MetalLookAndFeel";
            }
            else if (LOOKANDFEEL.equals("System")) {
                lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            }
            else if (LOOKANDFEEL.equals("Motif")) {
                lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
            }
            else if (LOOKANDFEEL.equals("GTK")) {
                lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
            }
            else {
                System.err.println("Unexpected value of LOOKANDFEEL specified: "
                                   + LOOKANDFEEL);
                lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
            }
            
            try {
               if (LOOKANDFEEL.equals("FlatLaf")) {
                   if (!DARK_THEME) 
                        UIManager.setLookAndFeel(new FlatLightLaf());
                   else if (DARK_THEME)
                        UIManager.setLookAndFeel(new FlatDarkLaf());
               }
                // If L&F = "Metal", set the theme
               else if (LOOKANDFEEL.equals("Metal")) {
                  if (THEME.equals("DefaultMetal"))
                     MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
                  else if (THEME.equals("Ocean"))
                     MetalLookAndFeel.setCurrentTheme(new OceanTheme());
                  UIManager.setLookAndFeel(new MetalLookAndFeel());
                }
            }

//            catch (ClassNotFoundException e) {
//                System.err.println("Couldn't find class for specified look and feel:"
//                                   + lookAndFeel);
//                System.err.println("Did you include the L&F library in the class path?");
//                System.err.println("Using the default look and feel.");
//            }

            catch (UnsupportedLookAndFeelException e) {
                System.err.println("Can't use the specified look and feel ("
                                   + lookAndFeel
                                   + ") on this platform.");
                System.err.println("Using the default look and feel.");
            }

            catch (Exception e) {
                System.err.println("Couldn't get specified look and feel ("
                                   + lookAndFeel
                                   + "), for some reason.");
                System.err.println("Using the default look and feel.");
                e.printStackTrace();
            }
        }
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/
    //        Sectiunea de initializare publica           /
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/

    /**
     * Dupa incarcarea completa a ferestrei se apeleaza aceasta metoda
     * pentru a continua procesul de initializare:
     *    - logare utilizator
     *    - incarcare preferinte
     *    - incarcare root
     */
    public void pornesteInitializare() {
        //Fac actiunea in background cu panou de monitorizare
        AbstractProgressCallable startServerCallable = ExternalLink.getStartServerCallable();
        JProgressSwingWorker startServerWorker =
                new JProgressSwingWorker(this,
                "Loading genomes",startServerCallable,JProgressPanel.INDETERMINATE);

        //Fac taskul
        startServerWorker.executeTask();
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/
    //             Metodele getteri                     /
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/
    
    /**
     * Returneaza frame-ul principal
     * @return
     */
    public Frame getTheRootFrame() {
        return this;
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
     * Aprind sau sting glasspane-ul
     * @param flag
     */
    public void setGlasspaneMessage(String row1, String row2) {
        glassPane.setMessage(row1, row2);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/
    //             Metodele de afisare                      /
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/

    /**
    *  <p style="margin-top: 0">
    *  Creeaza meniul de sus
    *  </p>
    * @return Clasa ce reprezinta bara de meniuri
    * @author iulian
    */
    private JMenuBar createMenuBar() {
        JMenuItem item;
        JRadioButtonMenuItem radio;
        JMenuBar mb;
        JMenu menu1;
        //Menu class

        mb=new JMenuBar();
        menu1 = new JMenu("File");

        //Adaug un element la meniu
        item= new JMenuItem(NAME_SEARCH);
            item.addActionListener(menuListener);
            item.setActionCommand(ACTION_SEARCH);
        menu1.add(item);

        //Adaug un element la meniu
        item= new JMenuItem(NAME_SETTINGS);
            item.addActionListener(menuListener);
            item.setActionCommand(ACTION_SETTINGS);
        menu1.add(item);
        
        menu1.addSeparator();
        
        //Adaug un element la meniu
        item= new JMenuItem(NAME_LOAD);
            item.addActionListener(menuListener);
            item.setActionCommand(ACTION_LOAD);
        menu1.add(item);        
        
        //Adaug un element la meniu
        item= new JMenuItem(NAME_SAVE);
            item.addActionListener(menuListener);
            item.setActionCommand(ACTION_SAVE);
        menu1.add(item);

        //Adaug un element la meniu
        item= new JMenuItem(NAME_SAVE_AS);
            item.addActionListener(menuListener);
            item.setActionCommand(ACTION_SAVE_AS);
        menu1.add(item);
        
        //Adaug un element la meniu
        item= new JMenuItem(NAME_EXPORT);
            item.addActionListener(menuListener);
            item.setActionCommand(ACTION_EXPORT);
        menu1.add(item);
        
        item= new JMenuItem(NAME_THEME);
            item.addActionListener(menuListener);
            item.setActionCommand(ACTION_THEME);
        menu1.add(item);

        menu1.addSeparator();

        //Adaug un element la meniu
        item= new JMenuItem(NAME_EXIT);
            item.addActionListener(menuListener);
            item.setActionCommand(ACTION_EXIT);
        menu1.add(item);

        //Adaug meniul la menu bar
        mb.add(menu1);
        return mb;
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
        JToolbarFereastraIcons iconProvider;
        String iconPath,iconFolder;

        //Obtin iconProviderul
        iconProvider = (JToolbarFereastraIcons) MyGlobalClasses
                .get(JToolbarFereastraIcons.GLOBAL_NAME);

        //Configurez filechooserul
        fc = new JFileChooser();
        fc.addChoosableFileFilter(new GenomeArtistFileFilter());
        fc.setAcceptAllFileFilterUsed(false);

        //Add the buttons
        JButton button = null;
        button = new JButton(NAME_SEARCH);
            button.setActionCommand(ACTION_SEARCH);
            button.addActionListener(menuListener);
            button.setToolTipText("Start a new search");
            button.setIcon(iconProvider.getIcon(JToolbarFereastraIcons.SEARCH));
            button.setFocusable(false);
        localToolBar.add(button);

        button = new JButton(NAME_SETTINGS);
            button.setActionCommand(ACTION_SETTINGS);
            button.addActionListener(menuListener);
            button.setToolTipText("Open settings");
            button.setIcon(iconProvider.getIcon(JToolbarFereastraIcons.SETTINGS));
            button.setFocusable(false);
        localToolBar.add(button);

        localToolBar.addSeparator();

        button = new JButton(NAME_LOAD);
            button.setActionCommand(ACTION_LOAD);
            button.addActionListener(menuListener);
            button.setToolTipText("Load a result");
            button.setIcon(iconProvider.getIcon(JToolbarFereastraIcons.LOAD));
            button.setFocusable(false);
        localToolBar.add(button);

        button = new JButton(NAME_SAVE);
            button.setActionCommand(ACTION_SAVE);
            button.addActionListener(menuListener);
            button.setToolTipText("Save a result");
            button.setIcon(iconProvider.getIcon(JToolbarFereastraIcons.SAVE));
            button.setFocusable(false);
        localToolBar.add(button);

        button = new JButton(NAME_SAVE_AS);
            button.setActionCommand(ACTION_SAVE_AS);
            button.addActionListener(menuListener);
            button.setToolTipText("Save a result");
            button.setIcon(iconProvider.getIcon(JToolbarFereastraIcons.SAVEAS));
            button.setFocusable(false);
        localToolBar.add(button);
        
        button = new JButton(NAME_EXPORT);
            button.setActionCommand(ACTION_EXPORT);
            button.addActionListener(menuListener);
            button.setToolTipText("Export Data to File");
            button.setIcon(iconProvider.getIcon(JToolbarFereastraIcons.EXPORT));
            button.setFocusable(false);
        localToolBar.add(button);
        
        localToolBar.addSeparator();
        
        JCheckBox checkbox = new JCheckBox(NAME_THEME, DARK_THEME);
            checkbox.setActionCommand(ACTION_THEME);
            checkbox.addActionListener(menuListener);
            checkbox.setToolTipText("Toggle Dark/Light Theme");
            checkbox.setFocusable(false);
        localToolBar.add(checkbox); 

        //Adaug sigla la sfarsit
        JBrandingImages brandingImages = (JBrandingImages) MyGlobalClasses
                .get(JBrandingImages.GLOBAL_NAME);
        localToolBar.add(Box.createHorizontalGlue());
        JLabel sigla = new JLabel();
        sigla.setIcon(brandingImages.getIcon(JBrandingImages.TOOLBAR));
        localToolBar.add(sigla);

        //Setari pentru toolbar
        localToolBar.setFocusable(false); //nu am nevoie de focus aici
        localToolBar.setFloatable(false); //Daca toolbarul pluteste sau nu
        localToolBar.setRollover(false);
        return localToolBar;
    }

    /**
     *  Creez Containerul din centru si adaug meniurile laterale la acesta
     * @return Obiectul ce va reprezenta mijlocul ecranului
     * @author iulian
     */
    private JPanel createMiddle() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        //Creez tabbed pane-ul
        tabbedpane = createTabbedPane();
        //tabbedpane.add("Center",new JMiddlePane(this, TestData.getTestMainResult()));

        panel.add(tabbedpane);

        return panel;
    }

    //~~~~~~~~~~~~~ Creez zona de afisare al documentelor ~~~~
    /**
     * Creeaza Panoul cu taburi din partea centrala
     * @return TabbedPane-ul cu documentele
     * @author iulian
     */
    private JTabbedPane createTabbedPane() {
        JTabbedPane tpane;
        //tpane = new JMyTabbedPane();
        tpane = new JDragableTabbedPane();        
        tpane.addChangeListener(tpaneListener);
        //tpane.setTabPlacement(JTabbedPane.LEFT);
        return tpane;
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

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/
    //             Actiuniile facute de utilizator          /
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/
    /**
     * Lansez actiunea de cautare
     */
    public void fireActionSearch() {
        JMyBoolean isOk = new JMyBoolean();
        final JSearchQueryDialog searchDialog = new JSearchQueryDialog(
                RootFrame.this, "New Search", true, isOk);
        searchDialog.setVisible(true);

        if(isOk.isTrue()) {
            for (SearchQuery searchQuery : searchDialog.getSearchQueries()) {
                performIndividualSearch(searchQuery);                
            }
        }
        if(tabbedpane.getTabCount() > 5)
            tabbedpane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        else
            tabbedpane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
    }

    /**
     * Lansez actiunea de importare (deschidere)
     */
    public void fireActionLoad() {        
        fc.setMultiSelectionEnabled(true);
        int returnVal = fc.showOpenDialog(RootFrame.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            final File[] files = fc.getSelectedFiles();

            //Afisez mesajul de cautare
            glassPane.setMessage(DrawingConstants.TEXT_OPEN_ROW1,
                    DrawingConstants.TEXT_OPEN_ROW2);
            glassPane.showGlassPane();

            //Lansez cautarea propriu zisa
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    for(int i = 0; i < files.length; i++)
                    {
                        MainResult mainResult = ExternalLink.readResultFile(files[i]);
                        if (mainResult == null) {
                            //custom title, error icon
                            JOptionPane.showMessageDialog(RootFrame.this,
                                "Error opening file",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                            glassPane.setVisible(false);
                            return;
                        } else {
                            mainResult.backgroundFile = files[i];
                            mainResult.isSaved = true;
                            mainResult.hasBeenModified = false;

                            JPanel resultPane = new JSearchResultPaneManager(RootFrame.this,
                                    mainResult);
                            tabbedpane.add(mainResult.infoQuery.queryName,
                                    resultPane);
                            tabbedpane.setSelectedComponent(resultPane);
                            if(tabbedpane.getTabCount() > 100)
                                tabbedpane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
                            else
                                tabbedpane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
                            glassPane.setVisible(false);
                        }
                    }
                }
            });
        }
        fc.setMultiSelectionEnabled(false);
    }

    /**
     * Lansez actiunea de Save
     */
    public void fireActionSave() {
        //Verific daca este ceva selectat
        final JSearchResultPaneManager selectedComponent =
                (JSearchResultPaneManager) tabbedpane.getSelectedComponent();

        //Daca am ce sa salvez
        if (selectedComponent != null) {
            //Vad despre ce este vorba
            final MainResult mainResult = selectedComponent.getMainResult();

            //Daca nu a fost salvat niciodata, atunci lansez actiunea de Save As
            if (!mainResult.isSaved) {
                fireActionSaveAs();
                return;
            } else {
                //Salvez decat best result-ul
                //Afisez mesajul de cautare
                glassPane.setMessage(DrawingConstants.TEXT_SAVING_ROW1,
                        DrawingConstants.TEXT_SAVING_ROW2);
                glassPane.showGlassPane();

                //Copiez fisierul
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        //Copiez fisierul in locul corespunzator
                        File source = mainResult.backgroundFile;
                        ExternalLink.saveBestResult(mainResult);
                        mainResult.isSaved = true;
                        mainResult.hasBeenModified = false;

                        //Scot glasspane-ul
                        glassPane.setVisible(false);
                    }
                });


            }
        }
    }

    /**
     * Lansez actiunea de save-as
     */
    public void fireActionSaveAs() {
        //Verific daca este ceva selectat
        final JSearchResultPaneManager selectedComponent =
                (JSearchResultPaneManager) tabbedpane.getSelectedComponent();

        //Daca am ce sa salvez
        if (selectedComponent != null) {
            int returnVal = fc.showSaveDialog(RootFrame.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fc.getSelectedFile();

                //Compun numele fisierului
                String basename = MyUtils.getBasename(selectedFile);
                basename += "."+MyUtils.TRANSPOSON_EXT;
                System.out.println(basename);
                final File destination = new File(basename);

                //Afisez mesajul de cautare
                glassPane.setMessage(DrawingConstants.TEXT_SAVING_ROW1,
                        DrawingConstants.TEXT_SAVING_ROW2);
                glassPane.showGlassPane();

                //Copiez fisierul
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        //Copiez fisierul in locul corespunzator
                        MainResult mainResult = selectedComponent.getMainResult();
                        File source = mainResult.backgroundFile;
                        //Salvez fizic rezultatul si apoi il copiez
                        ExternalLink.saveBestResult(mainResult);
                        ExternalLink.copyFile(source, destination);
                        mainResult.backgroundFile = destination;
                        mainResult.isSaved = true;
                        mainResult.hasBeenModified = false;

                        //Scot glasspane-ul
                        glassPane.setVisible(false);
                    }
                });
            }
        }
    }
       
    
    /**
     * Lansez actiunea de exportare a TSD sau Flanking Sequences in Fasta
     */
    public void fireActionExport() {
        JMyBoolean isOk = new JMyBoolean();
        String chromosomeFilePath;
        int[] genomeCoordinateArray;
        int lengthExtractSeq;
        int toleranceExtractSeq;
        int numberOfResults;
        final JExportDialog exportDialog = new JExportDialog(
                RootFrame.this, "Export to FASTA", true, isOk);
        exportDialog.setVisible(true);

        if(isOk.isTrue()) {
            lengthExtractSeq = exportDialog.getLengthExtractSeq();
            toleranceExtractSeq = exportDialog.getToleranceExtractSeq();
            numberOfResults = exportDialog.getNumberOfResults();
            boolean toggleConsensus = exportDialog.getIsConsensusButtonSelected();
            double consensusTreshold = exportDialog.getConsensusTreshold();
            if(exportDialog.getIsChooseButtonSelected()) {
                chromosomeFilePath = exportDialog.getChromosomeFilePath();
                genomeCoordinateArray = exportDialog.getGenomeCoordinateArray();
                exportSequenceAtCoordinate(chromosomeFilePath, genomeCoordinateArray, lengthExtractSeq);
            }
            if(exportDialog.getIsTSDButtonSelected())
                exportSequencesAtBorder(true, false, toggleConsensus, consensusTreshold, lengthExtractSeq, toleranceExtractSeq, numberOfResults);
            if(exportDialog.getIsFlankingButtonSelected())
                exportSequencesAtBorder(false, false, toggleConsensus, consensusTreshold, lengthExtractSeq, toleranceExtractSeq, numberOfResults);
            if(exportDialog.getIsTwoFlanksButtonSelected())
                exportSequencesAtBorder(false, true, toggleConsensus, consensusTreshold, lengthExtractSeq, toleranceExtractSeq, numberOfResults);
            if(exportDialog.getIsTableButtonSelected())
                exportTable(lengthExtractSeq, toleranceExtractSeq, numberOfResults);
        }
    }
    
    /**
     * Lansez actiunea de schimbare tema
     */
    public void fireActionChangeTheme() {
        if (DARK_THEME)
            DARK_THEME = false;                      
        else if (!DARK_THEME)
            DARK_THEME = true;         
        ReadWriteConfiguration.put("THEME", DARK_THEME);
        ReadWriteConfiguration.commitSettings();
        initLookAndFeel();
        SwingUtilities.updateComponentTreeUI(RootFrame.this);
    }
    
    /**
     * Lansez actiunea de deschidere panou de setari
     */
    public void fireActionShowSettings() {
        Dialog settingsDialog = new JSettingsDialog(
                RootFrame.this, "Settings", true, generalSettings);
        settingsDialog.setVisible(true);
    }

    /**
     * Lansez actiunea de iesire din program
     */
    public void fireActionExit() {
        ExternalLink.stopClient();
        ExternalLink.stopServer();
        System.exit(0);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/
    //            Action helpers       /
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/
    
    private void performIndividualSearch(SearchQuery searchQuery) {
        MainResult mainResult;
        //Compun calea catre rezultat
        String outputPath = ReadOnlyConfiguration.getString("FOLDER_OUTPUTS");
        outputPath += searchQuery.getQueryName() + "_" + 
                MyUtils.getCurrentUTCTimestampInISO8601Format() + ".ga";
        File outputFile = new File(outputPath);
        //Fac actiunea in background cu panou de monitorizare
        AbstractProgressCallable callable = ExternalLink.getSearchCallable(searchQuery, outputFile);
        JProgressSwingWorker<Boolean> searchWorker =
                new JProgressSwingWorker<Boolean>(this.getTheRootFrame(),
                "Computing",callable,JProgressPanel.DETERMINATE);
        searchWorker.setStandardErrorMessage("Error while searching");
        //Fac taskul
        Boolean hasSucceded = searchWorker.executeTask();
        //Obtin rezultatul
        if (hasSucceded != null && hasSucceded == true) {
            mainResult = ExternalLink.readResultFile(outputFile);
            mainResult.backgroundFile = outputFile;
            mainResult.isSaved = false;
            mainResult.hasBeenModified = true;
            
            //Afisez tabul cu rezultatul
            JPanel resultPane = new JSearchResultPaneManager(RootFrame.this,
                    mainResult);
            tabbedpane.add(mainResult.infoQuery.queryName,
                    resultPane);
            tabbedpane.setSelectedComponent(resultPane);
        }
    }
    
    /**
     * fuctie petru salvare in fisier fasta sau csv
     * @param filetype
     * @param tableData
     * @param fastaData
     */
    public void saveToFile(String filetype, ArrayList<String[]> tableData, ArrayList<String> fastaData) {
        JFileChooser auxfc = new JFileChooser();
        if(filetype.equals(MyUtils.CSV_EXT))
            auxfc.addChoosableFileFilter(new MyTableFilter());
        if(filetype.equals(MyUtils.FASTA_EXT))
            auxfc.addChoosableFileFilter(new MyFastaFilter());
        auxfc.setSelectedFile(new File("."+ filetype));
        int returnVal = auxfc.showSaveDialog(RootFrame.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File destination = auxfc.getSelectedFile();
            if (destination.exists())
                destination.delete();
            try {
                destination.createNewFile();
            }
            catch(IOException e) {}                    
            if(filetype.equals(MyUtils.CSV_EXT)) {
                for(int i = 0; i < tableData.size(); i++) {
                    for(int j = 0; j < MyUtils.COLUMNS_NUMBER; j++) {
                        ExternalLink.writeStringToFile(destination, tableData.get(i)[j]+" , ", true);
                    }
                ExternalLink.writeStringToFile(destination, "\n", true);
                }
            }
            if(filetype.equals(MyUtils.FASTA_EXT)) {
                ArrayList<String> sortedFasta = MyUtils.sortBySymbol(fastaData, "(-)");
                for(int i = 0; i < sortedFasta.size(); i++)
                    ExternalLink.writeStringToFile(destination, sortedFasta.get(i), true);     
            }
        }
    }
    
    /**
     * Lansez actiunea de exportare rezultatele cele mai bune
     * @param lengthTSD
     * @param lengthTolerance
     */
    public void exportTable(int lengthTSD, int lengthTolerance, int numberOfResults) {
        ArrayList<String[]> bestResultData = new ArrayList<String[]>();
        ArrayList<String[]> bestResultsBatch;
        bestResultData.add(MyUtils.TABLE_HEADER);
        
        for(int i = 0; i < tabbedpane.getTabCount(); i++) {            
            JSearchResultPaneManager auxComponent = (JSearchResultPaneManager) tabbedpane.getComponentAt(i);            
            MainResult auxMainResult = auxComponent.getMainResult();
            if(auxMainResult.bestResult != null) {
                bestResultsBatch = auxComponent.getResultsInsertionData(auxMainResult, lengthTSD, lengthTolerance, numberOfResults);
                bestResultData.addAll(bestResultsBatch);
            }
        }
        saveToFile(MyUtils.CSV_EXT, bestResultData, null);
    }
    
    /*
    / functie pt entragerea flanking sequeces in fisier .raw de la border-ulfiecarui Best Result din taburi
    */
    private void exportSequencesAtBorder(boolean useTSD, boolean useDoubleFlanks, boolean toggleConsensus, double consensusTreshold, int lengthSeqExtract, int lengthTolerance, int numberOfResults) {
        ArrayList<String> resultData = new ArrayList<String>();
        ArrayList<String> result;
        String folderRaw = ReadOnlyConfiguration.getString("FOLDER_RAW");
        
        for(int i = 0; i < tabbedpane.getTabCount(); i++) {            
            JSearchResultPaneManager auxComponent = (JSearchResultPaneManager) tabbedpane.getComponentAt(i);            
            MainResult auxMainResult = auxComponent.getMainResult();
            if(auxMainResult.bestResult != null) {
                if(useTSD)
                    result = auxComponent.getResultsTSD(auxMainResult, lengthSeqExtract, lengthTolerance, numberOfResults, toggleConsensus);
                else {
                    try {
                        result = auxComponent.getResultsFlankingSeq(auxMainResult, useDoubleFlanks, lengthSeqExtract, lengthTolerance, numberOfResults, toggleConsensus, folderRaw);
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(RootFrame.this,
                                    "Raw file not found" + e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                        result = null;
                        return;
                    }
                }
                if(result != null && !result.isEmpty())
                    resultData.addAll(result);
            }
        }
        if(resultData.isEmpty())
            JOptionPane.showMessageDialog(RootFrame.this,
                        "No jonction border detected","Error",JOptionPane.ERROR_MESSAGE);
        if(toggleConsensus) {
            saveToFile(MyUtils.FASTA_EXT, null, MyUtils.getConsensus(resultData, consensusTreshold));
        }
        else
            saveToFile(MyUtils.FASTA_EXT, null, resultData);
    }
    
    /*
    / functie pentru extragerea flanking sequences din fisier .raw la coordonata specificata
    */
    private void exportSequenceAtCoordinate(String rawFilePath, int[] coordinates, int sideLengthExtract) {
        ArrayList<String> containerList = new ArrayList<String>();
        String readSequence;
        for(int i = 0; i < coordinates.length; i++) {
            try {
                readSequence = FinalResultExporter.readFromRawFile(rawFilePath, coordinates[i], sideLengthExtract, sideLengthExtract);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(RootFrame.this,
                                    "Error","Error",JOptionPane.ERROR_MESSAGE);
                readSequence = null;
                return;   
            }
            if(readSequence == null)
                JOptionPane.showMessageDialog(RootFrame.this,
                                "Coordinate larger than file size","Error",JOptionPane.ERROR_MESSAGE);               
            readSequence = ">" + rawFilePath + "-" + Integer.toString(coordinates[i]) + " (+)\n" + readSequence + "\n";            
            containerList.add(readSequence);
        }
        saveToFile(MyUtils.FASTA_EXT, null, containerList);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/
    //            Controllerele pentru actiuni              /
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/

    /**
     *  <p style="margin-top: 0">
     *  Clasa ce reprezinta un ascultator pe butoanele din meniu si toolbar
     *  </p>
     * @author iulian
     */
    ActionListener menuListener = new ActionListener() {
        /**
         *  <p style="margin-top: 0">
         *  Invoked when an action occurs.
         *  </p>
         * @author iulian
         */
            public void actionPerformed(ActionEvent e)
            {
                String cmd = e.getActionCommand();

                if (ACTION_SEARCH.equals(cmd)) {
                    fireActionSearch();
                } else
                if (ACTION_SAVE.equals(cmd)) {
                   fireActionSave();
                } else
                if (ACTION_LOAD.equals(cmd)) {
                    fireActionLoad();
                } else
                if (ACTION_SAVE_AS.equals(cmd)) {
                    fireActionSaveAs();
                } else
                if (ACTION_SETTINGS.equals(cmd)) {
                    fireActionShowSettings();
                } else
                if (ACTION_EXPORT.equals(cmd)) {
                    fireActionExport();
                } else
                if (ACTION_THEME.equals(cmd)) {
                    fireActionChangeTheme();
                } else
                if (ACTION_EXIT.equals(cmd)) {
                    fireActionExit();
                }
            }
        };

     /**
      * Ascultator pe schimbarea taburilor in Tabbed Pane
      */
    ChangeListener tpaneListener = new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
            JTabbedPane tpane = (JTabbedPane) e.getSource();
            Component component = tpane.getSelectedComponent();
            /*if (component instanceof JDevizView) {
            JDevizView devizView = (JDevizView) component;
            stateManager.setSelectedDeviz(devizView);
            } else
            if (component instanceof JInvestitieView) {
            JInvestitieView investitieView = (JInvestitieView) component;
            stateManager.setSelectedInvestitie(investitieView);
            } else
            if (component instanceof JObiectView) {
            JObiectView obiectView = (JObiectView) component;
            stateManager.setSelectedObiect(obiectView);
            }*/
        }
    };

    /**
     *  <p style="margin-top: 0">
     *  Clasa ce reprezinta un ascultator pe Fereastra principala
     *  </p>
     * @author iulian
     */
    WindowListener windowListener = new WindowListener() {

      public void windowClosing(WindowEvent e) {
            fireActionExit();
      }

      public void windowOpened(WindowEvent e) {
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

}
