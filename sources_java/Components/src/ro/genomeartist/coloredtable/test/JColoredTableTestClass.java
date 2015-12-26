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

package ro.genomeartist.coloredtable.test;

import ro.genomeartist.components.jtable.viewportcontainer.JScrollableBottomSpacingContainer;
import ro.genomeartist.components.coloredtable.AbstractColoredTableModel;
import ro.genomeartist.components.coloredtable.JColoredTable;
import ro.genomeartist.components.headerrenderer.JHeaderSorterIcons;
import ro.genomeartist.components.headerrenderer.MultilineHeaderRenderer;
import ro.genomeartist.components.utils.WindowUtilities;
import java.awt.Color;
import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class JColoredTableTestClass {
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Init look and feel
        WindowUtilities.initLookAndFeel();

        //Create and set up the window.
        JFrame frame = new JFrame("TableSelectionDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        AbstractColoredTableModel tableModel = new AbstractColoredTableModel() {
            
                @Override
                public Color getBackground(int row, int col) {
                    if (col > 0)
                        return Color.RED;
                    return Color.CYAN;
                }

                @Override
                public Color getForeground(int row, int col) {
                    return null;
                }

                public int getRowCount() {
                    return 30;
                }

                public int getColumnCount() {
                    return 3;
                }

                @Override
                public String getColumnName(int column) {
                    if (column >= 0)
                        return "column "+column;
                    else return null;
                }

                public Object getValueAt(int rowIndex, int columnIndex) {
                    return "["+rowIndex+","+columnIndex+"]";
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

                @Override
                public boolean isCellEditable(int row, int col) {
                    switch (col) {
                        case 1:case 2:
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public String getTooltipText(int row, int col) {
                    switch (col) {
                        case 0:case 1:
                            return "test";
                        default:
                            return null;
                    }
                }

                @Override
                public Icon getCellIcon(int row, int col) {
                    switch (col) {
                        case 1:
                            return JHeaderSorterIcons.ASC.getIcon();
                        default:
                            return null;
                    }
                }
                
            };
        JColoredTable coloredTable = new JColoredTable(tableModel);
        coloredTable.setRowHeight(coloredTable.getRowHeight());
        coloredTable.setFillsViewportHeight(true);
        
        //Sorterul
        TableRowSorter tableSorter = new TableRowSorter<TableModel>(tableModel);
        coloredTable.setRowSorter(tableSorter);
        tableSorter.toggleSortOrder(0);

        //Incerc sa definesc latimea coloanelor
        MultilineHeaderRenderer multirenderer = new MultilineHeaderRenderer();
        TableColumn column; //coloana pe care lucrez
        TableColumnModel columns = coloredTable.getColumnModel();
        //Cod
        column = columns.getColumn(0);
        column.setHeaderRenderer(multirenderer);
        column.setPreferredWidth(100);
        //Descriere
        column = columns.getColumn(1);
        column.setHeaderRenderer(multirenderer);
        column.setPreferredWidth(200);
        //Cantitate
        column = columns.getColumn(2);
        column.setHeaderRenderer(multirenderer);
        column.setPreferredWidth(10);

        JScrollableBottomSpacingContainer containerPanel = 
                new JScrollableBottomSpacingContainer(coloredTable);
        JScrollPane scrollPane = new JScrollPane(containerPanel);
        
        frame.add(scrollPane);

        //Display the window.
        frame.setSize(500, 500);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
