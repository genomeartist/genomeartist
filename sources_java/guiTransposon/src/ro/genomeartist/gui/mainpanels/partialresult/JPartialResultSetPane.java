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

package ro.genomeartist.gui.mainpanels.partialresult;

import ro.genomeartist.components.coloredtable.AbstractColoredTableModel;
import ro.genomeartist.components.coloredtable.JColoredTable;
import ro.genomeartist.components.headerrenderer.MultilineHeaderRenderer;
import ro.genomeartist.gui.interfaces.IGlobalManager;
import ro.genomeartist.gui.cellrenderers.JBoldIntegerCellRenderer;
import ro.genomeartist.gui.cellrenderers.JDefferPaintCellRenderer;
import ro.genomeartist.gui.cellrenderers.JTransposonDefaultCellRenderer;
import ro.genomeartist.gui.controller.partialresult.PartialResultItem;
import ro.genomeartist.gui.controller.partialresult.PartialResultSet;
import ro.genomeartist.gui.custompaint.BoldInteger;
import ro.genomeartist.gui.custompaint.DrawingConstants;
import ro.genomeartist.gui.dialogs.JPartialResultItemDialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
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
public class JPartialResultSetPane extends JPanel {
    private PartialResultSet partialResultSet;
    private JColoredTable tablePartialResults;
    private MyTableModel tableModel;
    private int scale = 2;

    //Renderer pentru headere
    MultilineHeaderRenderer multirenderer;
    TableRowSorter<TableModel> sorter;

    //Managerul global
    IGlobalManager globalManager;

    /**
     * Constructorul clasei
     * @param extrasMateriale Extrasul din spatele Clasei
     */
    public JPartialResultSetPane(IGlobalManager globalManager, PartialResultSet partialResultSet ) {
        super();
        setLayout(new BorderLayout());
        this.globalManager = globalManager;

        //          Ma ocup de Tabel
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // <editor-fold defaultstate="collapsed" desc="Setez mecanismul de la tabel">
        this.partialResultSet = partialResultSet;
        this.tableModel = new MyTableModel(partialResultSet);
        this.tableModel.addTableModelListener(tableModelListener);
        tablePartialResults = new JColoredTable(tableModel);
        tablePartialResults.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        tablePartialResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Setez modul de afisare a celulelor
        tablePartialResults.getTableHeader().setReorderingAllowed(false);
        tablePartialResults.setShowHorizontalLines(true);
        tablePartialResults.setShowVerticalLines(true);
        tablePartialResults.setRowHeight(new PartialResultItem().getRecommendedHeight(this));

        //Setez cell renderul pentru coloana de Partial results
        tablePartialResults.setDefaultCellRenderer(new JTransposonDefaultCellRenderer());
        tablePartialResults.setDefaultRenderer(PartialResultItem.class,
                new JDefferPaintCellRenderer());
        tablePartialResults.setDefaultRenderer(BoldInteger.class,
                new JBoldIntegerCellRenderer());

        sorter = new TableRowSorter<TableModel>(tableModel);
        tablePartialResults.setRowSorter(sorter);
        sorter.toggleSortOrder(0);sorter.toggleSortOrder(0);

        tablePartialResults.addMouseListener(clickListener);

        //Setez renderul pentru headere multiline
        multirenderer = new MultilineHeaderRenderer();
        this.setPrefferedWidth();
        // </editor-fold>

        //Adaug tree-ul la Panel
        add(new JScrollPane(tablePartialResults),BorderLayout.CENTER);
    }

    /**
     * Setez proportiile coloanelor
     */
    private void setPrefferedWidth() {
        //Incerc sa definesc latimea coloanelor
        TableColumn column; //coloana pe care lucrez
        TableColumnModel columns = tablePartialResults.getColumnModel();
        //Cod
        column = columns.getColumn(0);
        column.setHeaderRenderer(multirenderer);
        column.setPreferredWidth(10);
        //Descriere
        column = columns.getColumn(1);
        column.setHeaderRenderer(multirenderer);
        column.setPreferredWidth(30);
        //Cantitate
        column = columns.getColumn(2);
        column.setHeaderRenderer(multirenderer);
        column.setPreferredWidth(80);
        //Cantitate
        column = columns.getColumn(3);
        column.setHeaderRenderer(multirenderer);
        column.setPreferredWidth(10);
        //Cantitate
        column = columns.getColumn(4);
        column.setHeaderRenderer(multirenderer);
        column.setPreferredWidth(400);
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
    * Listenerul pentru clickuri la tabel
    */
    private MouseAdapter clickListener = new MouseAdapter(){
            @Override
         public void mouseClicked(MouseEvent e){
          if (e.getClickCount() == 2){
              int row = sorter.convertRowIndexToModel(tablePartialResults.rowAtPoint(e.getPoint()));
              PartialResultItem selectedItem = partialResultSet.elementAt(row);

              Dialog dialog = new JPartialResultItemDialog(globalManager.getTheRootFrame(),
                      "Partial result", true, selectedItem);
              dialog.setVisible(true);

             }
         }
    };    
    
    /**
     * Clasa implementeaza modelul tabelului de Extras Materiale
     */
    private class MyTableModel extends AbstractColoredTableModel {
        private PartialResultSet dataProvider;
        
        public MyTableModel(PartialResultSet dataProvider) {
            this.dataProvider = dataProvider;
        }

        /**
         * Obtin numarul de coloane
         * @return numarul de coloane
         */
        public int getColumnCount() {
            return 5;
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
                return "Score";
            case 1:
                return "Location";
            case 2:
                return "Position";
            case 3:
                return "Length";
            case 4:
                return "Sequence\nMapping";
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
            PartialResultItem partialResultItem =
                     (PartialResultItem) dataProvider.elementAt(row);

            switch (col) {
            case 0:
                return new BoldInteger(partialResultItem.getScore());
            case 1:
                return partialResultItem.getFisierOrigine();
            case 2:
                String complement;
                if (partialResultItem.isComplement())
                    complement = DrawingConstants.TEXT_STRAND_COMPLEMENTARY;
                else
                    complement = DrawingConstants.TEXT_STRAND_FORWARD;
                return partialResultItem.getPozitieStartGenom()+".."+
                                partialResultItem.getPozitieStopGenom()+" "+
                                complement;
            case 3:
                return new Integer(partialResultItem.getLengthQuery());
            case 4:
                return partialResultItem;
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
                return BoldInteger.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return Integer.class;
            case 4:
                return PartialResultItem.class;
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
            return DrawingConstants.COLOR_TABLE_BG_YELLOW;
        }

        @Override
        public Color getForeground(int row, int col) {
            return null;
        }
    }


}
