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
package ro.genomeartist.components.jtable.rowheader;

import java.awt.Color;
import java.util.Vector;

/**
 * Pachetul de date ce se va renda intr-o celula de lista
 * @author iulian
 */
public class RowHeaderCellData {
    private String text;
    private Color backgroundColor;
    private Color foregroundColor;
    private RowAnnotation currentAnnotation;
    
    //variabile private
    private int indexAnotatieCurenta;
    private Vector<RowAnnotation> rowAnnotations;

    /**
     * Construiesc un pachet de date
     * @param text 
     */
    public RowHeaderCellData() {
        this(null);
    }
    
    /**
     * Construiesc un pachet de date
     * @param text 
     */
    public RowHeaderCellData(String text) {
        this.text = text;
        this.backgroundColor = null;
        this.foregroundColor = null;
        
        //Initializez vectorul de annotatii
        this.currentAnnotation = null;
        this.indexAnotatieCurenta = -1;
        rowAnnotations = null;
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *         Getteri
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Obtin textul afisat
     * @return 
     */
    public String getText() {
        return text;
    }
    
    /**
     * Obtin culoarea de fundal
     * @return 
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }   
    
    /**
     * Obtin culoarea scrisului
     * @return 
     */
    public Color getForegroundColor() {
        return foregroundColor;
    }

    /**
     * Obtin anotatia curenta
     * @return 
     */
    public RowAnnotation getCurrentAnnotation() {
        return currentAnnotation;
    }

    /**
     * Obtin toate anotatiile
     * @return 
     */
    public Vector<RowAnnotation> getRowAnnotations() {
        return rowAnnotations;
    }
    
    /**
     * Obine numarul de anotatii
     * @return 
     */
    public int getRowAnnotationCount() {
        if (rowAnnotations != null) {
            return rowAnnotations.size();
        } else {
            return 0;
        }
    }
    
     /**~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *        Setteri
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Setez texxtul afisat
     * @param text 
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Setez culoarea de fundal
     * @param backgroundColor 
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Setez culoarea scrisului
     * @param foregroundColor 
     */
    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    /**
     * Setez vectorul de anotatii
     */
    public void setRowAnnotations(Vector<RowAnnotation> rowAnnotations) {
        this.rowAnnotations = rowAnnotations;
        if (rowAnnotations != null && !rowAnnotations.isEmpty()) {
            indexAnotatieCurenta = 0;
            currentAnnotation = rowAnnotations.elementAt(indexAnotatieCurenta);
        }
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Actiuni pe celula
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Cicleaza printre anotatii
     * @return true daca anotatia s-a schimbat
     */
    public boolean cycleAnnotations() {
        int count = getRowAnnotationCount();
        
        //Operatia are sens doar daca sunt mai multe anotatii
        if (count > 1) {
            indexAnotatieCurenta ++;
            indexAnotatieCurenta %= count;
            currentAnnotation = rowAnnotations.elementAt(indexAnotatieCurenta);
            return true;
        } else {
            return false;
        }
    }
}
