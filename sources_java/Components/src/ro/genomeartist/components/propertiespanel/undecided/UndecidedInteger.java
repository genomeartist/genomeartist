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

/**
 * Clasa ce reprezinta un big decimal nedecis.
 * @author iulian
 */
public class UndecidedInteger implements Comparable {
    private Integer integer;
    private boolean isUndecided;

    //Constanta pentru valoare indecisa
    public static final String UNDECIDED_STRING = "<valori diferite>";


    /**
     * Construiesc un big Decimal
     * @param integer
     */
    public UndecidedInteger(Integer integer) {
        setValue(integer);
    }

    /**
     * Construiesc cu un string
     * @param value
     */
    public UndecidedInteger(String value) {
        setValue(value);
    }

    /**~~~~~~~~~~~~~~~~~
     *      Getters
     *~~~~~~~~~~~~~~~~~~*/

    /**
     * Obtin BigDecimalul din spate
     * @return
     */
    public Integer getInteger() {
        return integer;
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
        if (o instanceof UndecidedInteger) {
            UndecidedInteger compareInteger = (UndecidedInteger) o;
            if (this.isDecided() && compareInteger.isDecided() && integer != null)
                return integer.compareTo(compareInteger.getInteger());
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
        if (obj instanceof UndecidedInteger) {
            UndecidedInteger compareInteger = (UndecidedInteger) obj;
            if (this.isDecided()) {
                assert integer != null : "Is is decided should not be null";

                if (compareInteger.isDecided()) {
                    return integer.equals(compareInteger.getInteger());
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
        hash = 29 * hash + (this.integer != null ? this.integer.hashCode() : 0);
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
    public void setValue(Integer value) {
        if (value != null) {
            integer = value;
            isUndecided = false;
        } else {
            integer = null;
            isUndecided = true;
        }
    }

    /**
     * Setez valoarea ca string
     * @param value
     */
    public void setValue(String value) {
        Integer localIntegerValue;
        if (NumberUtils.isValidInteger(value)) {
            localIntegerValue = NumberUtils.toInteger(value);
        } else {
            localIntegerValue = null;
        }

        //Setez valoare bigDecimal
        setValue(localIntegerValue);
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
            return integer.toString();
        }
    }
}
