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

package ro.genomeartist.components.propertiespanel.undecided;

import java.awt.Color;

/**
 * Clasa ce reprezinta un big decimal nedecis.
 * @author iulian
 */
public class UndecidedColor {
    private Color backingColor;
    private boolean isUndecided;

    //Constanta pentru valoare indecisa
    public static final String UNDECIDED_STRING = "<valori diferite>";


    /**
     * Construiesc un big Decimal
     * @param integer
     */
    public UndecidedColor(Color backingColor, boolean isUndecided) {
        setValue(backingColor, isUndecided);
    }

    /**
     * Construiesc cu un string
     * @param value
     */
    public UndecidedColor(Color backingColor) {
        setValue(backingColor,false);
    }

    /**~~~~~~~~~~~~~~~~~
     *      Getters
     *~~~~~~~~~~~~~~~~~~*/

    /**
     * Obtin BigDecimalul din spate
     * @return
     */
    public Color getColor() {
        return backingColor;
    }

    /**
     * Specifica daca respectiva valoare este nedecisa
     * @return
     */
    public boolean isUndecided() {
        return isUndecided;
    }

    /**
     * Specifica daca valoarea este decisa
     * @return
     */
    public boolean isDecided() {
        return !isUndecided;
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Metode de comparatie nedecisa
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Compar doua obiecte indecise
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UndecidedColor) {
            UndecidedColor compareInteger = (UndecidedColor) obj;
            if (this.isDecided()) {
                assert backingColor != null : "Is is decided should not be null";

                if (compareInteger.isDecided()) {
                    return backingColor.equals(compareInteger.getColor());
                }
                else return false;
            } else {
                if (compareInteger.isUndecided())
                    return true;
                else return false;
            }
        } else return false;
    }

    /**
     * Hash code
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (this.backingColor != null ? this.backingColor.hashCode() : 0);
        hash = 29 * hash + (this.isUndecided ? 1 : 0);
        return hash;
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Metode de interactiune cu string
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * setez valoarea ca BigDecimal
     * @param value
     */
    public void setValue(Color value, boolean isUndecided) {
        backingColor = value;
        this.isUndecided = isUndecided;
    }

    /**
     * Interfata externa a acestei clase este un string
     * @return
     */
    @Override
    public String toString() {
        if (this.isUndecided()) {
            return UNDECIDED_STRING;
        } else {
            return backingColor.toString();
        }
    }
}
