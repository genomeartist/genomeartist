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
package ro.genomeartist.components.jtable.rowheader.implementation;

import ro.genomeartist.components.jtable.rowheader.RowAnnotation;
import ro.genomeartist.components.jtable.rowheader.RowHeaderCellData;
import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

/**
 * Renderul pentru o celula din RowHeader
 * @author iulian
 */
public class JRowHeaderCellRenderer extends DefaultListCellRenderer {
    private static final int RIGHT_PADDING = 2;
    private static final int LEFT_PADDING = 2;
    
    //Border
    private static final Border STANDARD_BORDER 
            = BorderFactory.createEmptyBorder(0, RIGHT_PADDING, 0, LEFT_PADDING);
    private static final Border MULTIPLE_ANNOTATIONS_BORDER 
            = new MultipleAnnotationArrowBorder();
    
    /**
     * Construiesc rendeul
     */
    public JRowHeaderCellRenderer() {
        this.setBorder(null);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, 
        int index, boolean isSelected, boolean cellHasFocus) {
        
        //Call in parinte pentru initializare
        super.getListCellRendererComponent(list, value, index, 
                isSelected, cellHasFocus);
        
        //Aliniez la dreapta
        this.setHorizontalAlignment(SwingConstants.RIGHT);
        
        //Fac desenarea custom
        if (value instanceof RowHeaderCellData) {
            RowHeaderCellData listCellData = (RowHeaderCellData) value;
            
            //Setez borderul
            int annotationsCount = listCellData.getRowAnnotationCount();
            if (annotationsCount > 1) {
                this.setBorder(MULTIPLE_ANNOTATIONS_BORDER);
            } else {
                this.setBorder(STANDARD_BORDER);
            }
            
            //Afisez anotatia curenta
            RowAnnotation currentAnnotation = listCellData.getCurrentAnnotation();
            if (currentAnnotation != null) {
                Icon icon = currentAnnotation.getIcon();
                if (icon != null) {
                    this.setText(null);
                    this.setIcon(icon);
                    this.setToolTipText(currentAnnotation.getTooltipText());  
                } else {
                    this.setText(listCellData.getText());
                    this.setIcon(null);
                    this.setToolTipText(currentAnnotation.getTooltipText()); 
                }
            } else {
                this.setText(listCellData.getText());
                this.setToolTipText(null);
            }
            
            //Setez fundalul
            Color bgColor = listCellData.getBackgroundColor();
            if (bgColor == null) 
                bgColor = list.getBackground();
            this.setBackground(bgColor);
            this.setForeground(listCellData.getForegroundColor());
        } else {
            //Setez borderul
            this.setBorder(STANDARD_BORDER);
            
            //Setez textul
            this.setText(value.toString());
        }
        
        //Intorc aceasta instanta
        return this;
    }
    
}
