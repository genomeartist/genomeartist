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

/**
 * Clasa ce reprezinta un big decimal nedecis.
 * @author iulian
 */
public class UndecidedBoolean implements Comparable {
    private Boolean backingBoolean;
    private boolean isUndecided;

    //Constanta pentru valoare indecisa
    public static final String UNDECIDED_STRING = "<valori diferite>";


    /**
     * Construiesc un big Decimal
     * @param integer
     */
    public UndecidedBoolean(Boolean backingBoolean, boolean isUndecided) {
        setValue(backingBoolean, isUndecided);
    }

    /**
     * Construiesc cu un string
     * @param value
     */
    public UndecidedBoolean(Boolean backingBoolean) {
        setValue(backingBoolean,false);
    }

    /**~~~~~~~~~~~~~~~~~
     *      Getters
     *~~~~~~~~~~~~~~~~~~*/

    /**
     * Obtin BigDecimalul din spate
     * @return
     */
    public Boolean getBoolean() {
        return backingBoolean;
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
        if (o instanceof UndecidedBoolean) {
            UndecidedBoolean compareInteger = (UndecidedBoolean) o;
            if (this.isDecided() && compareInteger.isDecided() && backingBoolean != null)
                return backingBoolean.compareTo(compareInteger.getBoolean());
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
        if (obj instanceof UndecidedBoolean) {
            UndecidedBoolean compareInteger = (UndecidedBoolean) obj;
            if (this.isDecided()) {
                assert backingBoolean != null : "Is is decided should not be null";

                if (compareInteger.isDecided()) {
                    return backingBoolean.equals(compareInteger.getBoolean());
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
        hash = 29 * hash + (this.backingBoolean != null ? this.backingBoolean.hashCode() : 0);
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
    public void setValue(Boolean value, boolean isUndecided) {
        backingBoolean = value;
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
            return backingBoolean.toString();
        }
    }
}
