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

package ro.genomeartist.coloredtable.cellrenderers;

import ro.genomeartist.components.coloredtable.AbstractColoredTableModel;
import ro.genomeartist.components.coloredtable.JColoredTable;
import ro.genomeartist.components.utils.GraphicRenderingUtils;
import ro.genomeartist.components.utils.NumberUtils;
import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author iulian
 */
public abstract class AbstractColoredCellRenderer extends DefaultTableCellRenderer {
    //Surse de date
    private JColoredTable coloredTable;
    private AbstractColoredTableModel coloredTableModel;

    //Variabile booleane puse la dispozitie de catre clasa
    private boolean isSelected;
    private boolean hasSomeFocus;
    private boolean isCellEditable;
    private boolean hasToolTipText;

    //Variabile de stare
    private Icon cellIcon;
    private Color prefferedBackground;
    private Color prefferedForeground;
    private int colorOffset;

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Construierea renderului
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Constructorul default
     */
    public AbstractColoredCellRenderer() {
        super();

        //Backend-ul renderului
        coloredTable = null;
        coloredTableModel = null;
        
        //Initializez default
        isSelected = false;
        hasSomeFocus = false;
        isCellEditable = false;
        hasToolTipText = false;
        
        //~~~~
        cellIcon = null;
        prefferedBackground = null;
        prefferedForeground = null;
        colorOffset = 15;
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Acces la sursele de date
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/    

    /**
     * Obtin colored table-ul caruia se adreseaza renderul
     * @return
     */
    public JColoredTable getColoredTable() {
        return coloredTable;
    }

    /**
     * Obtin modelul pe care se bazeaza tabelul
     * @return
     */
    public AbstractColoredTableModel getColoredTableModel() {
        return coloredTableModel;
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *     Obtinerea variabilelor puse la
     *      dispozitie pentru desenare
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    /**
     * Obtin daca randul este selectat
     * @return
     */
    public boolean isSelected() {
        return isSelected;
    }    

    /**
     * Obtin daca celula are focus in acest moment
     * @return
     */
    public boolean hasSomeFocus() {
        return hasSomeFocus;
    }

    /**
     * Obtin daca celula este editabila
     * @return
     */
    public boolean isCellEditable() {
        return isCellEditable;
    }    

    /**
     * Obtin daca cell rederul are tooltip text
     * @return
     */
    public boolean hasToolTipText() {
        return hasToolTipText;
    }

    /**
     * Obtin iconul pentru aceasta celula
     * @return 
     */
    public Icon getCellIcon() {
        return cellIcon;
    }
    
    /**
     * Obtin backgroundul preferat de model
     * @return
     */
    public Color getPrefferedBackground() {
        return prefferedBackground;
    }

    /**
     * Obtin foregroundul preferat de model
     * @return
     */
    public Color getPrefferedForeground() {
        return prefferedForeground;
    }

    /**
     * Obtin offsetul de culoare intre randuri
     * @return
     */
    public int getColorOffset() {
        return colorOffset;
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *     Setarea variabilelor puse la
     *      dispozitie pentru desenare
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Setez faptul ca acest rand este selectat
     * @param isSelected
     */
    protected void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }    

    /**
     * Setez faptul ca aceasta componenta are focus
     * @param hasSomeFocus
     */
    protected void setHasSomeFocus(boolean hasSomeFocus) {
        this.hasSomeFocus = hasSomeFocus;
    }

    /**
     * Setez faptul ca celula este editabila
     * @param isCellEditable
     */
    protected void setIsCellEditable(boolean isCellEditable) {
        this.isCellEditable = isCellEditable;
    }    

    /**
     * Setez faptul ca are tooltiptext
     * @param hasToolTipText
     */
    protected void setHasToolTipText(boolean hasToolTipText) {
        this.hasToolTipText = hasToolTipText;
    }

    /**
     * Setez culoare preferata de model
     * @param cellIcon
     */
    protected void setCellIcon(Icon cellIcon) {
        this.cellIcon = cellIcon;
    }
    
    /**
     * Setez culoare preferata de model
     * @param prefferedBackground
     */
    protected void setPrefferedBackground(Color prefferedBackground) {
        this.prefferedBackground = prefferedBackground;
    }

    /**
     * Setez culoare preferata de model
     * @param prefferedForeground
     */
    protected void setPrefferedForeground(Color prefferedForeground) {
        this.prefferedForeground = prefferedForeground;
    }

    /**
     * Setez diferenta de culoare intre randuri
     * @param colorOffset
     */
    protected void setColorOffset(int colorOffset) {
        this.colorOffset = colorOffset;
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Construieste componenta ce
     *      va fi trimisa spre desenare
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Obtin componenta ce va fi rendata in celula
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param column
     * @return
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {

        //Apel in parinte pentru a seta el variabilele default
        super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);

        //Surse de date
        coloredTable = (JColoredTable) table;
        coloredTableModel = coloredTable.getModel();

        //Translatez randul in randul de model
        int modelrow = coloredTable.convertRowIndexToModel(row);
        int modelcolumn = coloredTable.convertColumnIndexToModel(column);

        //Variabile boolene de stare
        this.isSelected = isSelected;
        this.hasSomeFocus = hasFocus;
        this.isCellEditable = coloredTableModel.isCellEditable(modelrow, modelcolumn);

        //Textul ce se deseneaza
        if (value instanceof String) {
            this.setText((String)value);
        } else
        if (value instanceof BigDecimal) {
            BigDecimal auxBigDecimal = (BigDecimal) value;
            this.setText(NumberUtils.formatBigDecimal(auxBigDecimal,
                    coloredTableModel.getDecimalScale()));
        } else
        if (value instanceof Double) {
            Double auxDouble = (Double) value;
            this.setText(NumberUtils.formatDouble(auxDouble,
                    coloredTableModel.getDecimalScale()));
        } else
        if (value instanceof Integer) {
            Integer auxInteger = (Integer) value;
            this.setText(NumberUtils.formatInteger(auxInteger));
        }

        //Alinierea textului
        setVerticalAlignment(SwingConstants.TOP);
        if (value instanceof Number) {
            setHorizontalAlignment(SwingConstants.RIGHT);
        } else {
            setHorizontalAlignment(SwingConstants.LEFT);
        }

        //obtin tooltip textul
        String tooltipText;
        tooltipText = coloredTableModel
                .getTooltipText(modelrow,modelcolumn);

        //Setez tooltip textul
        this.setToolTipText(tooltipText);
        if (tooltipText != null) {
            hasToolTipText = true;
        } else {
            hasToolTipText = false;
        }

        //Setez iconul
        cellIcon = coloredTableModel.getCellIcon(modelrow, modelcolumn);
        this.setIcon(cellIcon);
        
        //Setez culorile de desenare
        prefferedBackground = coloredTableModel.getBackground(modelrow, modelcolumn);
        prefferedForeground = coloredTableModel.getForeground(modelrow, modelcolumn);
        //~~~ Sel ~~~
        Color chosenBackground, chosenForeground;
        if (isSelected) {
            //Culoare speciala daca bg este suprascris
            chosenBackground = table.getSelectionBackground();
            chosenForeground = table.getSelectionForeground();
        } else {
            chosenBackground = prefferedBackground;
            chosenForeground = prefferedForeground;
        }

        //Desenez fundalul alternativ
        if (row % 2 == 0)
            setBackground(GraphicRenderingUtils.lightenColor(chosenBackground, colorOffset));
        else
            setBackground(GraphicRenderingUtils.darkenColor(chosenBackground, colorOffset));
        setForeground(chosenForeground);

        //Intorc aceasta componente
        return this;
    }

    /**
     * Aici printez starea la aceasta celula
     * @return
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        //Variabile booleane puse la dispozitie de catre clasa
        stringBuilder.append("isSelected = ").append(isSelected).append('\n');
        stringBuilder.append("hasSomeFocus = ").append(hasSomeFocus).append('\n');
        stringBuilder.append("isCellEditable = ").append(isCellEditable).append('\n');
        stringBuilder.append("hasToolTipText = ").append(hasToolTipText).append('\n');

        //Variabile de stare
        stringBuilder.append("text = ").append(getText()).append('\n');
        stringBuilder.append("background = ").append(getBackground()).append('\n');
        stringBuilder.append("foreground = ").append(getForeground()); stringBuilder.append('\n');
        stringBuilder.append("prefferedBackground = ").append(prefferedBackground); stringBuilder.append('\n');
        stringBuilder.append("prefferedForeground = ").append(prefferedForeground); stringBuilder.append('\n');

        //Intorc stringul compus
        stringBuilder.append("==========================");
        return stringBuilder.toString();
    }
}
