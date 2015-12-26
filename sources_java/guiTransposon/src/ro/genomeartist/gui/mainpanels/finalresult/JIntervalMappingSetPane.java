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
import ro.genomeartist.gui.cellrenderers.JGeneWrapperCellRenderer;
import ro.genomeartist.gui.cellrenderers.JTransposonDefaultCellRenderer;
import ro.genomeartist.gui.controller.genes.GeneItem;
import ro.genomeartist.gui.controller.genes.GeneItemWrapper;
import ro.genomeartist.gui.controller.finalresult.IntervalMappingItem;
import ro.genomeartist.gui.controller.finalresult.IntervalMappingSet;
import ro.genomeartist.gui.custompaint.DrawingConstants;
import ro.genomeartist.gui.dialogs.JGeneMappingDialog;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * Afiseaza extrasul de materiale
 * @author iulian
 */
public class JIntervalMappingSetPane extends JPanel {
    private IntervalMappingSet intervalMappingSet;
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
    public JIntervalMappingSetPane(IGlobalManager globalManager, IntervalMappingSet intervalMappingSet ) {
        super();
        setLayout(new BorderLayout());
        this.globalManager = globalManager;

        //          Ma ocup de Tabel
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // <editor-fold defaultstate="collapsed" desc="Setez mecanismul de la tabel">
        this.intervalMappingSet = intervalMappingSet;
        this.tableModel = new MyTableModel(intervalMappingSet);
        tablePartialResults = new JColoredTable(tableModel);
        tablePartialResults.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);

        //Activez selectia de celule
        tablePartialResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablePartialResults.setCellSelectionEnabled(true);

        //Setez modul de afisare a celulelor
        tablePartialResults.getTableHeader().setReorderingAllowed(false);
        tablePartialResults.setShowHorizontalLines(true);
        tablePartialResults.setShowVerticalLines(true);
        tablePartialResults.setRowHeight(new IntervalMappingItem().getRecommendedHeight(this));

        //Setez cell renderul pentru coloana de Partial results
        tablePartialResults.setDefaultCellRenderer(new JTransposonDefaultCellRenderer());
        tablePartialResults.setDefaultRenderer(GeneItemWrapper.class,
                new JGeneWrapperCellRenderer());

        //Double click listener
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
        column.setPreferredWidth(100);
        //Cantitate
        column = columns.getColumn(2);
        column.setHeaderRenderer(multirenderer);
        column.setPreferredWidth(100);
        //Cantitate
        column = columns.getColumn(3);
        column.setHeaderRenderer(multirenderer);
        column.setPreferredWidth(150);
        //Cantitate
        column = columns.getColumn(4);
        column.setHeaderRenderer(multirenderer);
        column.setPreferredWidth(400);
    }


    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Definitia de clase interne
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
    * Listenerul pentru clickuri la tabel
    */
    private MouseAdapter clickListener = new MouseAdapter(){
         @Override
         public void mouseClicked(MouseEvent e){
          if (e.getClickCount() == 2){
              int row = tablePartialResults.rowAtPoint(e.getPoint());
              int col = tablePartialResults.columnAtPoint(e.getPoint());

              //Deschid gene respectiva
              Object selectedObject = tablePartialResults.getValueAt(row, col);
              if (selectedObject instanceof GeneItemWrapper) {
                  GeneItemWrapper geneItemWrapper = (GeneItemWrapper) selectedObject;

                  Dialog dialog = new JGeneMappingDialog(globalManager,
                  "Gene map", true, geneItemWrapper);
                  dialog.setVisible(true);
              }
             }
         }
    };


    /**
     * Clasa implementeaza modelul tabelului de Extras Materiale
     */
    private class MyTableModel extends AbstractColoredTableModel {
        private IntervalMappingSet dataProvider;

        public MyTableModel(IntervalMappingSet dataProvider) {
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
                return "\\";
            case 1:
                return "Location";
            case 2:
                return "Position\nin query";
            case 3:
                return "Coordinates";
            case 4:
                return "Annotations";
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
            GeneItem auxGene;
            String output =new String();
            String auxString;
            IntervalMappingItem intervalMappingItem =
                     (IntervalMappingItem) dataProvider.elementAt(row);

            switch (col) {
            case 0:
                return new Integer(row+1);
            case 1:
                return intervalMappingItem.getFisierOrigine();
            case 2:
                output = intervalMappingItem.getPozitieStartQuery() + " .. " +
                        intervalMappingItem.getPozitieStopQuery();
                return output;
            case 3:
                output = intervalMappingItem.getPozitieStartGenom() + " .. " +
                        intervalMappingItem.getPozitieStopGenom();
                //Vad sensul catenei
                if (intervalMappingItem.isComplement())
                    auxString = DrawingConstants.TEXT_STRAND_COMPLEMENTARY;
                else auxString = DrawingConstants.TEXT_STRAND_FORWARD;

                //Compun rezultatul
                output += " "+auxString;
                return output;
            case 4:
                return intervalMappingItem.getGeneItemWrapper();
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
                return Integer.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return String.class;
            case 4:
                return GeneItemWrapper.class;
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
