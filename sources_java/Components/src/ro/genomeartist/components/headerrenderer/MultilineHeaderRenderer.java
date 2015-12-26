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

package ro.genomeartist.components.headerrenderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.RowSorter;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author iulian
 */
public class MultilineHeaderRenderer extends JPanel implements TableCellRenderer {
    JList dataList;
    JLabel iconLabel;

    private static Color bgColor = Color.WHITE;
    private static Color fgColor = Color.BLACK;

  public MultilineHeaderRenderer() {
    super();

    dataList = new JList();
    dataList.setOpaque(true);
    dataList.setForeground(fgColor);
    dataList.setBackground(bgColor);
    ListCellRenderer renderer = dataList.getCellRenderer();
    ((JLabel)renderer).setHorizontalAlignment(JLabel.CENTER);
    dataList.setCellRenderer(renderer);

    //Aici se va afisa iconul
    iconLabel = new JLabel();
    iconLabel.setOpaque(true);
    iconLabel.setForeground(fgColor);
    iconLabel.setBackground(bgColor);

    //Setez si JPanelul
    this.setOpaque(true);
    this.setForeground(fgColor);
    this.setBackground(bgColor);

    this.setLayout(new BorderLayout());
    this.setBorder(UIManager.getBorder("TableHeader.cellBorder"));

    this.add(dataList,BorderLayout.CENTER);
    this.add(iconLabel,BorderLayout.EAST);
  }

  public Component getTableCellRendererComponent(JTable table, Object value,
                   boolean isSelected, boolean hasFocus, int row, int column) {
    setFont(table.getFont());
    String str = (value == null) ? "" : value.toString();
    BufferedReader br = new BufferedReader(new StringReader(str));
    String line;
    Vector v = new Vector();
    try {
      while ((line = br.readLine()) != null) {
        v.addElement(line);
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    dataList.setListData(v);

    Icon icon;
    //Check if needs sorting
    boolean isSorted = false;
    if (table.getRowSorter() != null) {
        RowSorter sorter = table.getRowSorter();
        List sortKeys = sorter.getSortKeys();
        Iterator sortIterator = sortKeys.iterator();
        if (sortIterator.hasNext()) {
            RowSorter.SortKey key = (RowSorter.SortKey) sortIterator.next();
            if (key.getColumn() == column) {
                switch (key.getSortOrder()) {
                    case ASCENDING:
                        icon = JHeaderSorterIcons.ASC.getIcon();
                        iconLabel.setIcon(icon);
                        isSorted = true;
                        break;
                    case DESCENDING:
                        icon = JHeaderSorterIcons.DESC.getIcon();
                        iconLabel.setIcon(icon);
                        isSorted = true;
                        break;
                    default:
                        break;
                }
            }
        }
    }
    if (!isSorted) iconLabel.setIcon(null);


    return this;
    
    
  }
}