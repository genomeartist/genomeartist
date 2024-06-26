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

package ro.genomeartist.gui.mainpanels.settings;

import ro.genomeartist.components.coloredtable.AbstractColoredTableModel;
import ro.genomeartist.components.coloredtable.JColoredTable;
import ro.genomeartist.components.headerrenderer.MultilineHeaderRenderer;
import ro.genomeartist.components.modalpanel.progresspanel.JProgressPanel;
import ro.genomeartist.components.swingworkers.progressworker.AbstractProgressCallable;
import ro.genomeartist.components.swingworkers.progressworker.JProgressSwingWorker;
import ro.genomeartist.gui.cellrenderers.JTransposonDefaultCellRenderer;
import ro.genomeartist.gui.controller.externalcalls.ExternalLink;
import ro.genomeartist.gui.interfaces.IGlobalManager;
import ro.genomeartist.gui.controller.settings.SearchFile;
import ro.genomeartist.gui.controller.settings.SearchFileSequence;
import ro.genomeartist.gui.controller.settings.SearchFileSet;
import ro.genomeartist.gui.controller.settings.SearchFolder;
import ro.genomeartist.gui.custompaint.DrawingConstants;
import ro.genomeartist.gui.dialogs.JAddFileDialog;
import ro.genomeartist.gui.dialogs.JAddFolderDialog;
import ro.genomeartist.gui.dialogs.JCreateFileDialog;
import ro.genomeartist.gui.icons.JToolbarFisiereIcons;
import ro.genomeartist.gui.utils.MyGlobalClasses;
import ro.genomeartist.gui.utils.JMyBoolean;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * Afiseaza extrasul de materiale
 * @author iulian
 */
public class JSearchFilesPane extends JPanel implements ActionListener {
    private JColoredTable tableSearchFiles;
    private MyTableModel tableModel;
    private int scale = 2;

    //Renderer pentru headere
    MultilineHeaderRenderer multirenderer;
    TableRowSorter<TableModel> sorter;

    //Managerul global
    private IGlobalManager globalManager;
    private SearchFileSet searchFileSet;
    private boolean isTransposon;

    //Constante pentru butoane
    private static final String ACTION_ADD_FOLDER = "add_folder";
    private static final String ACTION_ADD_FILE = "add_node";
    private static final String ACTION_DELETE = "del_node";
    private static final String ACTION_CREATE = "create_node";

    //Butoanele din meniu
    JToolBar toolBar;
        JButton buttonAddFolder;
        JButton buttonAddFile;
        JButton buttonDelete;
        JButton buttonCreate;

    //Providerul de icoane
    JToolbarFisiereIcons iconToolbarProvider;
    boolean hasChanged; //flag daca s-a schimbat ceva


    /**
     * Constructorul clasei
     * @param extrasMateriale Extrasul din spatele Clasei
     */
    public JSearchFilesPane(IGlobalManager globalManager, SearchFileSet searchFileSet,
            boolean isTransposon ) {
        super();
        setLayout(new BorderLayout());
        this.globalManager = globalManager;
        this.isTransposon = isTransposon;
        this.hasChanged = false;

        //Providerul de icoane
        iconToolbarProvider = (JToolbarFisiereIcons)
                MyGlobalClasses.get(JToolbarFisiereIcons.GLOBAL_NAME);

        //          Ma ocup de Tabel
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // <editor-fold defaultstate="collapsed" desc="Setez mecanismul de la tabel">
        this.searchFileSet = searchFileSet;
        this.tableModel = new MyTableModel(searchFileSet);
        this.tableModel.addTableModelListener(tableModelListener);
        tableSearchFiles = new JColoredTable(tableModel);
        tableSearchFiles.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        tableSearchFiles.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        //Setez modul de afisare a celulelor
        tableSearchFiles.getTableHeader().setReorderingAllowed(false);
        tableSearchFiles.setShowHorizontalLines(true);
        tableSearchFiles.setShowVerticalLines(true);

        //Renderul
        tableSearchFiles.setDefaultCellRenderer(new JTransposonDefaultCellRenderer());

        sorter = new TableRowSorter<TableModel>(tableModel);
        tableSearchFiles.setRowSorter(sorter);
        sorter.toggleSortOrder(0);sorter.toggleSortOrder(0);

        //Setez renderul pentru headere multiline
        multirenderer = new MultilineHeaderRenderer();
        this.setPrefferedWidth();
        // </editor-fold>

        //Setez toolbarul
        toolBar = createToolBar();

        //Adaug tree-ul la Panel
        add(new JScrollPane(tableSearchFiles),BorderLayout.CENTER);
        add(toolBar,BorderLayout.NORTH);
    }

     /**
     *  <p style="margin-top: 0">
     *  Creeaza toolbarul principal
     *  </p>
     * @return Obiectul ce reprezinta toolbarul
     * @author iulian
     */
    private JToolBar createToolBar() {
        JToolBar localToolBar = new JToolBar("Toolbar");

        buttonAddFolder = new JButton("Add folder");
        buttonAddFolder.setActionCommand(ACTION_ADD_FOLDER);
        buttonAddFolder.setToolTipText("Add all files in a folder");
        buttonAddFolder.addActionListener(this);
        buttonAddFolder.setIcon(iconToolbarProvider.getIcon(JToolbarFisiereIcons.ADD_FOLDER));
        buttonAddFolder.setFocusable(false);
        localToolBar.add(buttonAddFolder);

        buttonAddFile = new JButton("Add file");
        buttonAddFile.setActionCommand(ACTION_ADD_FILE);
        buttonAddFile.setToolTipText("Add a new file");
        buttonAddFile.addActionListener(this);
        buttonAddFile.setIcon(iconToolbarProvider.getIcon(JToolbarFisiereIcons.ADD_FILE));
        buttonAddFile.setFocusable(false);
        localToolBar.add(buttonAddFile);

        buttonDelete = new JButton("Delete file");
        buttonDelete.setActionCommand(ACTION_DELETE);
        buttonDelete.setToolTipText("Delete existing file");
        buttonDelete.addActionListener(this);
        buttonDelete.setIcon(iconToolbarProvider.getIcon(JToolbarFisiereIcons.DELETE));
        buttonDelete.setFocusable(false);
        localToolBar.add(buttonDelete);

        localToolBar.addSeparator();

        buttonDelete = new JButton("Create file");
        buttonDelete.setActionCommand(ACTION_CREATE);
        buttonDelete.setToolTipText("Create a new file");
        buttonDelete.addActionListener(this);
        buttonDelete.setIcon(iconToolbarProvider.getIcon(JToolbarFisiereIcons.NEW));
        buttonDelete.setFocusable(false);
        localToolBar.add(buttonDelete);

        localToolBar.setFocusable(false); //nu am nevoie de focus aici
        localToolBar.setFloatable(false); //Daca toolbarul pluteste sau nu
        localToolBar.setRollover(false);
        return localToolBar;
    }

    /**
     * Setez proportiile coloanelor
     */
    private void setPrefferedWidth() {
        //Incerc sa definesc latimea coloanelor
        TableColumn column; //coloana pe care lucrez
        TableColumnModel columns = tableSearchFiles.getColumnModel();
        //Cod
        column = columns.getColumn(0);
        column.setHeaderRenderer(multirenderer);
        column.setPreferredWidth(100);
        //Descriere
        column = columns.getColumn(1);
        column.setHeaderRenderer(multirenderer);
        column.setPreferredWidth(100);
        //Cantitate
        column = columns.getColumn(2);
        column.setHeaderRenderer(multirenderer);
        column.setPreferredWidth(100);
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *     Handle-ul actiuniilor
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /**
     * Controllerul pentru JPAnel-ul de sus
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (ACTION_ADD_FOLDER.equals(cmd)) {
            fireActionAddFolder();
        } else
        if (ACTION_ADD_FILE.equals(cmd)) {
            fireActionAddFile();
        } else
        if (ACTION_DELETE.equals(cmd)) {
            fireActionDeleteFile();
        } else
        if (ACTION_CREATE.equals(cmd)) {
            fireActionCreateFile();
        }
    }

    /**
     * Lansez actiunea de adaugare fisier
     */
    public void fireActionAddFolder() {
        JMyBoolean isOk = new JMyBoolean();

        JAddFolderDialog dialog = new JAddFolderDialog(globalManager, "Add folder",
                true, isOk);
        dialog.setVisible(true);

        if (isOk.isTrue()) {
            //Trebuie sa adaug fisierul fizic la locatia standard
            final SearchFolder searchFolderRaw = dialog.getFolderRaw();

            //TODO adauga fizic fisierul
            if ((searchFolderRaw.folderLocation == null)) {
                //custom title, error icon
                JOptionPane.showMessageDialog(globalManager.getTheRootFrame(),
                    "Invalid location",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            //Fac actiunea in background cu panou de monitorizare
            AbstractProgressCallable callable = ExternalLink
                    .getAddSearchFolderCallable(searchFolderRaw, isTransposon);
            JProgressSwingWorker addFolderWorker =
                    new JProgressSwingWorker(globalManager.getTheRootFrame(),
                    "Computing",callable,JProgressPanel.DETERMINATE);
            addFolderWorker.setStandardErrorMessage("Error while searching");
            Vector<SearchFile> newSearchFiles =
                    (Vector<SearchFile>) addFolderWorker.executeTask();

            //Actualizez mopdelul tabelului
            Iterator <SearchFile> iteratorNewFiles = newSearchFiles.iterator();
            while (iteratorNewFiles.hasNext()) {
                SearchFile searchFile = iteratorNewFiles.next();
                searchFileSet.add(searchFile);
            }
            tableModel.fireTableDataChanged();

            //Notific ca s-a schimbat configuratia fisierului
            hasChanged = true;
        }
    }

    /**
     * Lansez actiunea de adaugare fisier
     */
    public void fireActionAddFile() {
        JMyBoolean isOk = new JMyBoolean();

        JAddFileDialog dialog = new JAddFileDialog(globalManager, "Add file",
                true, isOk);
        dialog.setVisible(true);

        if (isOk.isTrue()) {
            //Trebuie sa adaug fisierul fizic la locatia standard
            final SearchFile searchFileRaw = dialog.getFileRaw();

            //TODO adauga fizic fisierul
            if ((searchFileRaw.rawLocation == null)) {
                //custom title, error icon
                JOptionPane.showMessageDialog(globalManager.getTheRootFrame(),
                    "Invalid location",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            //Fac actiunea in background cu panou de monitorizare
            AbstractProgressCallable callable = ExternalLink.getAddSearchFileCallable(searchFileRaw);
            JProgressSwingWorker addFileWorker =
                    new JProgressSwingWorker(globalManager.getTheRootFrame(),
                    "Computing",callable,JProgressPanel.DETERMINATE);
            addFileWorker.setStandardErrorMessage("Error while searching");
            addFileWorker.executeTask();

            //Actualizez tabelul
            searchFileRaw.isTransposon = isTransposon;
            searchFileSet.add(searchFileRaw);
            tableModel.fireTableDataChanged();

            //Notific ca s-a schimbat configuratia fisierului
            hasChanged = true;
        }
    }

    /**
     * Lansez actiunea de stergere fisier
     */
    public void fireActionDeleteFile() {
        final int selectedRow = tableSearchFiles.getSelectedRow();
        if (selectedRow == -1) {
            //custom title, error icon
            JOptionPane.showMessageDialog(globalManager.getTheRootFrame(),
                "No file selected",
                "Warning",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        //default icon, custom title
        int n = JOptionPane.showConfirmDialog(
            globalManager.getTheRootFrame(),
            "Are you sure you want to delete the selected files ?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION);

        //S-a confirmat actiunea deci trebuie sters fisierul
        if (n == JOptionPane.YES_OPTION) {
            int[] selectedRows = tableSearchFiles.getSelectedRows();
            int[] positionsToDelete = new int[selectedRows.length];

            //Sterg fisierele selectate
            Vector<SearchFile> vectorDeleteFiles = new Vector<SearchFile>();
            for (int i = selectedRows.length-1; i>=0; i--) {
                int j = selectedRows[i];
                int selectedPosition = tableSearchFiles
                        .convertRowIndexToModel(j);
                SearchFile searchFile = searchFileSet.elementAt(selectedPosition);

                //Sterg fisierul de pe disc
                vectorDeleteFiles.add(searchFile);

                //Adaug fisierul la tabel
                positionsToDelete[i] = selectedPosition;

            }

            //Fac actiunea in background cu panou de monitorizare
            AbstractProgressCallable callable = ExternalLink
                    .getDeleteMultipleFileCallable(vectorDeleteFiles);
            JProgressSwingWorker deleteFilesWorker =
                    new JProgressSwingWorker(globalManager.getTheRootFrame(),
                    "Computing",callable,JProgressPanel.DETERMINATE);
            deleteFilesWorker.setStandardErrorMessage("Error while searching");
            deleteFilesWorker.executeTask();

            //Sortez si apoi sterg
            Arrays.sort(positionsToDelete);
            for (int i = positionsToDelete.length-1; i >= 0; i--) {
                int j = positionsToDelete[i];
                searchFileSet.removeElementAt(j);
            }

            //Adaug fisierul la tabel
            tableModel.fireTableDataChanged();

            //Notific ca s-a schimbat configuratia fisierului
            hasChanged = true;

        }
    }

    /**
     * Lansez actiunea de creeare fisier
     */
    public void fireActionCreateFile() {
        JMyBoolean isOk = new JMyBoolean();

        JCreateFileDialog dialog = new JCreateFileDialog(globalManager, "Create file",
                true, isOk);
        dialog.setVisible(true);

        if (isOk.isTrue()) {
            //Trebuie sa adaug fisierul fizic la locatia standard
            final SearchFileSequence searchFileSequence = dialog.getSearchFileSequence();

            //Fac actiunea in background cu panou de monitorizare
            AbstractProgressCallable callable = ExternalLink.getCreateSearchFileCallable(searchFileSequence);
            JProgressSwingWorker createFileWorker =
                    new JProgressSwingWorker(globalManager.getTheRootFrame(),
                    "Computing",callable,JProgressPanel.DETERMINATE);
            createFileWorker.setStandardErrorMessage("Error while searching");
            SearchFile searchFileRaw = (SearchFile) createFileWorker.executeTask();

             //Actualizez modelul tabelului
            searchFileRaw.isTransposon = isTransposon;
            searchFileSet.add(searchFileRaw);
            tableModel.fireTableDataChanged();

            //Notific ca s-a schimbat configuratia fisierului
            hasChanged = true;
        }
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Metode de accesare
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /**
     * Obtin setul din spate
     * @return
     */
     public SearchFileSet getSearchFileSet() {
        return searchFileSet;
    }   
    
    /**
     * Notific daca s-a schimbat ceva in configuratie
     * @return
     */
    public boolean hasChanged() {
        return hasChanged;
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Definitia de clase interne
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Ascultator pentru modificarea celulelor din tabel
     */
    private TableModelListener tableModelListener = new TableModelListener() {
        /**
         * Metoda apelata in momentul in care se produce o schimbare in tabel
         */
        public void tableChanged(TableModelEvent e) {
             if ((e.getType() == TableModelEvent.UPDATE) &&
                 (e.getColumn() > 0)) {
                   repaint();
             }
        }
    };

    /**
     * Clasa implementeaza modelul tabelului de Extras Materiale
     */
    private class MyTableModel extends AbstractColoredTableModel {
        private SearchFileSet dataProvider;
        
        public MyTableModel(SearchFileSet dataProvider) {
            this.dataProvider = dataProvider;
        }

        /**
         * Obtin numarul de coloane
         * @return numarul de coloane
         */
        public int getColumnCount() {
            return 3;
        }

        /**
         * Obtin numarul de randuri
         * @return Numarul de randuri
         */
        public int getRowCount() {
            return dataProvider.size();
        }

        /**
         * Obtin numele fiecarei coloane
         * @param column Coloana pentru care vreau sa obtin numele
         * @return NUmele coloanei
         */
        @Override
        public String getColumnName(int column) {
            switch (column) {
            case 0:
                return "Name";
            case 1:
                return "Sequence\nlocation";
            case 2:
                return "Genes\nlocation";
            default:
                assert false;
            }
            return null;
        }

        /**
         * Obtin valoarea pentru fiecare celula
         * @param row Randul
         * @param col Coloana
         * @return Valoarea celulei formatata la Clasa coloanei
         */
        public Object getValueAt(int row, int col) {
            Number result= new Double(0);
            String output =new String();
            BigDecimal aux = new BigDecimal(1);
            int auxInt;
            SearchFile searchFile =
                     (SearchFile) dataProvider.elementAt(row);

            switch (col) {
            case 0:
                return searchFile.fileTitle;
            case 1:
                return searchFile.rawLocation.getPath();
            case 2:
                return searchFile.geneLocation.getPath();
            default:
                assert false;
            }

            return null;
        }

        /**
         * Specifica Clasa pentru fiecare coloana
         * @param column Coloana pentru care vreau tipul clasei
         * @return Clasa coloanei
         */
        @Override
        public Class getColumnClass(int column) {
            switch (column) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            default:
                assert false;
            }
            return null;
        }

        /**
         * Specific daca o anumita celula este editabila
         * @param row Randul
         * @param col Coloana
         * @return True daca e aditabila, False altfel
         */
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }

        /**
         * Setez valoarea pentru o anumita celula
         * @param value Valoarea ce se modifica
         * @param row Randul celulei
         * @param col Coloana celulei
         */
        @Override
        public void setValueAt(Object value, int row, int col) {
            //Nu este editabile
        }

        @Override
        public Color getBackground(int row, int col) {
            if (isTransposon)
                return DrawingConstants.COLOR_TABLE_BG_RED;
            else return DrawingConstants.COLOR_TABLE_BG_BLUE;
        }

        @Override
        public Color getForeground(int row, int col) {
            return null;
        }
    }


}
