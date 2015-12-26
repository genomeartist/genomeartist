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

import ro.genomeartist.components.utils.NumberUtils;
import java.math.BigDecimal;

/**
 * Clasa ce reprezinta un big decimal nedecis.
 * @author iulian
 */
public class UndecidedBigDecimal implements Comparable {
    private BigDecimal bigDecimal;
    private boolean isUndecided;

    //Constanta pentru valoare indecisa
    public static final String UNDECIDED_STRING = "<valori diferite>";


    /**
     * Construiesc un big Decimal
     * @param bigDecimal
     */
    public UndecidedBigDecimal(BigDecimal bigDecimal) {
        setValue(bigDecimal);
    }

    /**
     * Construiesc cu un string
     * @param value
     */
    public UndecidedBigDecimal(String value) {
        setValue(value);
    }

    /**~~~~~~~~~~~~~~~~~
     *      Getters
     *~~~~~~~~~~~~~~~~~~*/

    /**
     * Obtin BigDecimalul din spate
     * @return
     */
    public BigDecimal getBigDecimal() {
        return bigDecimal;
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
     * Metoda de comparatie intre doua valori
     * @param o
     * @return
     */
    public int compareTo(Object o) {
        if (o instanceof UndecidedBigDecimal) {
            UndecidedBigDecimal compareBigDecimal = (UndecidedBigDecimal) o;
            if (this.isDecided() && compareBigDecimal.isDecided() && bigDecimal != null)
                return bigDecimal.compareTo(compareBigDecimal.getBigDecimal());
            else return -1;
        } else throw new ClassCastException("Cannot compare");
    }

    /**
     * Compar doua obiecte indecise
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UndecidedBigDecimal) {
            UndecidedBigDecimal compareBigDecimal = (UndecidedBigDecimal) obj;
            if (this.isDecided()) {
                assert bigDecimal != null : "If is decided should not be null";

                if (compareBigDecimal.isDecided()) {
                    return bigDecimal.equals(compareBigDecimal.getBigDecimal());
                }
                else return false;
            } else {
                if (compareBigDecimal.isUndecided())
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
        hash = 29 * hash + (this.bigDecimal != null ? this.bigDecimal.hashCode() : 0);
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
    public void setValue(BigDecimal value) {
        if (value != null) {
            bigDecimal = value;
            isUndecided = false;
        } else {
            bigDecimal = null;
            isUndecided = true;
        }
    }

    /**
     * Setez valoarea ca string
     * @param value
     */
    public void setValue(String value) {
        BigDecimal localBigDecimalValue;
        if (NumberUtils.isValidNumber(value)) {
            localBigDecimalValue = NumberUtils.toBigDecimal(value);
        } else {
            localBigDecimalValue = null;
        }

        //Setez valoare bigDecimal
        setValue(localBigDecimalValue);
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
            BigDecimal auxBigDecimal = bigDecimal.stripTrailingZeros();
            return auxBigDecimal.toPlainString();
        }
    }
}
