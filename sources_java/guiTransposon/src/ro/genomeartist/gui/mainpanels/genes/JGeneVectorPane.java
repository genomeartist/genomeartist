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

package ro.genomeartist.gui.mainpanels.genes;

import ro.genomeartist.components.coloredtable.AbstractColoredTableModel;
import ro.genomeartist.components.coloredtable.JColoredTable;
import ro.genomeartist.components.headerrenderer.MultilineHeaderRenderer;
import ro.genomeartist.gui.interfaces.IGlobalManager;
import ro.genomeartist.gui.cellrenderers.JGeneWrapperCellRenderer;
import ro.genomeartist.gui.cellrenderers.JTransposonDefaultCellRenderer;
import ro.genomeartist.gui.controller.genes.GeneItem;
import ro.genomeartist.gui.controller.genes.GeneItemWrapper;
import ro.genomeartist.gui.controller.genes.GeneVector;
import ro.genomeartist.gui.custompaint.DrawingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * Afiseaza extrasul de materiale
 * @author iulian
 */
public class JGeneVectorPane extends JPanel {
    private GeneVector geneVector;
    private JColoredTable tableGenes;
    private MyTableModel tableModel;
    private int scale = 2;

    //Renderer pentru headere
    MultilineHeaderRenderer multirenderer;
    TableRowSorter<TableModel> sorter;

    //Managerul global
    IGlobalManager globalManager;

    //Scrollpane-ul ce tine componentele
    JScrollPane scrollPane;

    /**
     * Constructorul clasei
     * @param extrasMateriale Extrasul din spatele Clasei
     */
    public JGeneVectorPane(GeneVector geneVector) {
        super();
        setLayout(new BorderLayout());

        //          Ma ocup de Tabel
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // <editor-fold defaultstate="collapsed" desc="Setez mecanismul de la tabel">
        this.geneVector = geneVector;
        this.tableModel = new MyTableModel(geneVector);
        tableGenes = new JColoredTable(tableModel);
        tableGenes.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);

        //Activez selectia de celule
        tableGenes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Setez modul de afisare a celulelor
        tableGenes.getTableHeader().setReorderingAllowed(false);
        tableGenes.setShowHorizontalLines(true);
        tableGenes.setShowVerticalLines(true);

        //Setez cell renderul pentru coloana de Partial results
        tableGenes.setDefaultCellRenderer(new JTransposonDefaultCellRenderer());
        tableGenes.setDefaultRenderer(GeneItemWrapper.class,
                new JGeneWrapperCellRenderer());

        sorter = new TableRowSorter<TableModel>(tableModel);
        tableGenes.setRowSorter(sorter);
        sorter.toggleSortOrder(0);

        //Setez renderul pentru headere multiline
        multirenderer = new MultilineHeaderRenderer();
        this.setPrefferedWidth();
        // </editor-fold>

        //Adaug tree-ul la Panel
        scrollPane = new JScrollPane(tableGenes);
        add(scrollPane,BorderLayout.CENTER);
    }

    /**
     * Setez proportiile coloanelor
     */
    private void setPrefferedWidth() {
        //Incerc sa definesc latimea coloanelor
        TableColumn column; //coloana pe care lucrez
        TableColumnModel columns = tableGenes.getColumnModel();

        //Cod
        column = columns.getColumn(0);
        column.setHeaderRenderer(multirenderer);
        column.setPreferredWidth(50);
        //Descriere
        column = columns.getColumn(1);
        column.setHeaderRenderer(multirenderer);
        column.setPreferredWidth(50);
        //Cantitate
        column = columns.getColumn(2);
        column.setHeaderRenderer(multirenderer);
        column.setPreferredWidth(70);
        //Cantitate
        column = columns.getColumn(3);
        column.setHeaderRenderer(multirenderer);
        column.setPreferredWidth(130);
        //Cantitate
        column = columns.getColumn(4);
        column.setHeaderRenderer(multirenderer);
        column.setPreferredWidth(40);
        //Cantitate
        column = columns.getColumn(5);
        column.setHeaderRenderer(multirenderer);
        column.setPreferredWidth(100);
    }

    /**
     * Obtin scrollpane-ul tabelului
     * @return
     */
    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    /**
     * Obtin inaltimea indicata pentru acest tabel
     * @return
     */
    public int getSuggestedHeight() {
        //calculez spatiul pentru celula
        int cellSize = 0;
        cellSize += tableGenes.getRowHeight();
        cellSize += tableGenes.getIntercellSpacing().width;
        cellSize += tableGenes.getRowMargin();

        //calculez spatiul pentru header
        int headerSize = 3*cellSize;

        //calculez totalul
        int totalSize = cellSize * geneVector.size() + headerSize;
        return totalSize;
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Definitia de clase interne
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Clasa implementeaza modelul tabelului de Extras Materiale
     */
    private class MyTableModel extends AbstractColoredTableModel {
        private GeneVector dataProvider;

        public MyTableModel(GeneVector geneVector) {
            this.dataProvider = geneVector;
        }

        /**
         * Obtin numarul de coloane
         * @return numarul de coloane
         */
        public int getColumnCount() {
            return 6;
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
                return "Location";
            case 2:
                return "Cytological\nmap";
            case 3:
                return "Position\nin location";
            case 4:
                return "Strand";
            case 5:
                return "Gene Id";
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
            GeneItem geneItem =
                     (GeneItem) dataProvider.elementAt(row);

            switch (col) {
            case 0:
                return geneItem.getName();
            case 1:
                return geneItem.getFisierOrigine();
            case 2:
                return geneItem.getCyto();
            case 3:
                output = geneItem.getLocationStart() + " .. " +
                        geneItem.getLocationEnd();
                return output;
            case 4:
                //Vad sensul catenei
                output = "";
                if (geneItem.isComplement())
                    auxString = DrawingConstants.TEXT_STRAND_COMPLEMENTARY;
                else auxString = DrawingConstants.TEXT_STRAND_FORWARD;

                //Compun rezultatul
                output += " "+auxString;
                return output;
            case 5:
                return geneItem.getGeneId();
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
            case 3:
                return String.class;
            case 4:
                return String.class;
            case 5:
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
            return DrawingConstants.COLOR_TABLE_BG_GREEN;
        }

        @Override
        public Color getForeground(int row, int col) {
            return null;
        }
    }
}
