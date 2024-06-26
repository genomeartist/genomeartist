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

package ro.genomeartist.gui.mainpanels.finalresult;

import ro.genomeartist.components.coloredtable.AbstractColoredTableModel;
import ro.genomeartist.components.coloredtable.JColoredTable;
import ro.genomeartist.components.headerrenderer.MultilineHeaderRenderer;
import ro.genomeartist.gui.interfaces.IGlobalManager;
import ro.genomeartist.gui.cellrenderers.JBoldIntegerCellRenderer;
import ro.genomeartist.gui.cellrenderers.JDefferPaintCellRenderer;
import ro.genomeartist.gui.cellrenderers.JTransposonDefaultCellRenderer;
import ro.genomeartist.gui.controller.finalresult.FinalResultItem;
import ro.genomeartist.gui.controller.finalresult.FinalResultSet;
import ro.genomeartist.gui.custompaint.BoldInteger;
import ro.genomeartist.gui.custompaint.DrawingConstants;
import ro.genomeartist.gui.dialogs.JFinalResultItemDialog;
import ro.genomeartist.gui.interfaces.ILocalManager;
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

/**
 * Afiseaza extrasul de materiale
 * @author iulian
 */
public class JFinalResultSetPane extends JPanel {
    private FinalResultSet finalResultSet;
    private JColoredTable tableFinalResults;
    private MyTableModel tableModel;
    private int scale = 2;

    //Constante
    private static final int COL_MAX_WIDTH = 70;

    //Renderer pentru headere
    MultilineHeaderRenderer multirenderer;

    //Managerul global
    IGlobalManager globalManager;
    ILocalManager localManager;

    /**
     * Constructorul clasei
     * @param extrasMateriale Extrasul din spatele Clasei
     */
    public JFinalResultSetPane(IGlobalManager globalManager,ILocalManager localManager,
            FinalResultSet finalResultSet ) {
        super();
        setLayout(new BorderLayout());
        this.globalManager = globalManager;
        this.localManager = localManager;

        //          Ma ocup de Tabel
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // <editor-fold defaultstate="collapsed" desc="Setez mecanismul de la tabel">
        this.finalResultSet = finalResultSet;
        this.tableModel = new MyTableModel(finalResultSet);
        this.tableModel.addTableModelListener(tableModelListener);
        tableFinalResults = new JColoredTable(tableModel);
        tableFinalResults.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        tableFinalResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Setez modul de afisare a celulelor
        tableFinalResults.getTableHeader().setReorderingAllowed(false);
        tableFinalResults.setShowHorizontalLines(true);
        tableFinalResults.setShowVerticalLines(true);
        tableFinalResults.setRowHeight(new FinalResultItem().getRecommendedHeight(this));

        //Setez cell renderul pentru coloana de Final results
        tableFinalResults.setDefaultCellRenderer(new JTransposonDefaultCellRenderer());
        tableFinalResults.setDefaultRenderer(FinalResultItem.class,
                new JDefferPaintCellRenderer());
        tableFinalResults.setDefaultRenderer(BoldInteger.class,
                new JBoldIntegerCellRenderer());

        tableFinalResults.addMouseListener(clickListener);

        //Setez renderul pentru headere multiline
        multirenderer = new MultilineHeaderRenderer();
        this.setPrefferedWidth();
        // </editor-fold>

        //Adaug tree-ul la Panel
        add(new JScrollPane(tableFinalResults),BorderLayout.CENTER);
    }

    /**
     * Setez proportiile coloanelor
     */
    private void setPrefferedWidth() {
        //Incerc sa definesc latimea coloanelor
        TableColumn column; //coloana pe care lucrez
        TableColumnModel columns = tableFinalResults.getColumnModel();
        //Cod
        column = columns.getColumn(0);
        column.setHeaderRenderer(multirenderer);
        column.setPreferredWidth(COL_MAX_WIDTH);
        column.setMaxWidth(COL_MAX_WIDTH);
        
        column = columns.getColumn(1);
        column.setHeaderRenderer(multirenderer);
        column.setPreferredWidth(COL_MAX_WIDTH);
        column.setMaxWidth(COL_MAX_WIDTH);
        //Descriere
        column = columns.getColumn(2);
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
              int row = tableFinalResults.rowAtPoint(e.getPoint());
              FinalResultItem selectedItem = finalResultSet.elementAt(row);
              int index = finalResultSet.indexOf(selectedItem) +1;
              JFrame dialog = new JFinalResultItemDialog(globalManager, localManager,
                      "Result candidate: score "+ selectedItem.getScore(), selectedItem);
              dialog.setTitle("Result candidate: score "+ selectedItem.getScore()+ " no. " + index );
              dialog.setVisible(true);

             }
         }
    };    
    
    /**
     * Clasa implementeaza modelul tabelului de Extras Materiale
     */
    private class MyTableModel extends AbstractColoredTableModel {
        private FinalResultSet dataProvider;
        
        public MyTableModel(FinalResultSet dataProvider) {
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
                return "No.";
            case 1:
                return "Score";
            case 2:
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
            FinalResultItem finalResultItem =
                     (FinalResultItem) dataProvider.elementAt(row);

            switch (col) {
            case 0:
                return new BoldInteger(finalResultSet.indexOf(finalResultItem) +1);
            case 1:
                return new BoldInteger(finalResultItem.getScore());
            case 2:
                return finalResultItem;
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
                return BoldInteger.class;
            case 2:
                return FinalResultItem.class;
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
