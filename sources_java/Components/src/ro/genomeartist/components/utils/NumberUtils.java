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

package ro.genomeartist.components.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

/**
 * Clasa cu metode statice pentru manipulare numere
 * @author iulian
 */
public class NumberUtils {
    public static final Locale LOCALE_RO = new Locale("ro", "RO");
    private static final String BASE_FORMATER_PATTERN = "##,##0";

    /**
     * Simbolurile asociate formaterului
     */
    private static DecimalFormatSymbols decimalFormatSymbols = null;

    /**
     * Formatez un numar la defaultul local
     * @param bigDecimal
     * @param scale
     * @return
     */
    private static DecimalFormat decimalFormater = null;
    private static DecimalFormat integerFormater = null;
    private static int formaterScale = 0;
    static {
        decimalFormater = new DecimalFormat(BASE_FORMATER_PATTERN);
        integerFormater = new DecimalFormat(BASE_FORMATER_PATTERN);
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Number rendering
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Initializez simbolurile
     */
    private static void initializeFormatSymbols() {
        decimalFormatSymbols = new DecimalFormatSymbols(LOCALE_RO);
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormatSymbols.setGroupingSeparator(',');
        decimalFormatSymbols.setMinusSign('-');
    }

    /**
     * Initializez number formaterul
     * @param scale
     */
    private static void initializeDecimalFormat(int scale) {
        //Verific initializarea simbolurilor
        if (decimalFormatSymbols == null)
            initializeFormatSymbols();

        //Initializez formaterul
        if (formaterScale != scale) {
            String decimals = "";
            if (scale > 0) decimals = ".";
            for (int i = 0; i < scale; i++) {
                decimals += "0";
            }

            //Initializez formaterul
            decimalFormater = new DecimalFormat(BASE_FORMATER_PATTERN+decimals,decimalFormatSymbols);
        }
    }

    /**
     * Obtin un formatter pentru o anumita scara
     * @param scale
     * @return
     */
    public static NumberFormat getDecimalFormatter(int scale) {
        if ( scale != formaterScale)
            initializeDecimalFormat(scale);
        return decimalFormater;
    }

    /**
     * Formate un big decimal
     * @param bigDecimal
     * @param scale
     * @return
     */
    public static String formatBigDecimal(BigDecimal bigDecimal, int scale) {
        if ( scale != formaterScale)
            initializeDecimalFormat(scale);

        return decimalFormater.format(bigDecimal.doubleValue());
    }

    /**
     * Formate un double
     * @param bigDecimal
     * @param scale
     * @return
     */
    public static String formatDouble(Double localDouble, int scale) {
        if ( scale != formaterScale)
            initializeDecimalFormat(scale);

        return decimalFormater.format(localDouble);
    }

    /**
     * Formate un double
     * @param bigDecimal
     * @param scale
     * @return
     */
    public static String formatInteger(Integer integer) {
        return integerFormater.format(integer);
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Number validation
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Verifica daca un string este numar sau nu
     * @param text
     * @return
     */
    public static boolean isValidNumber(String text) {
        ParsePosition p = new ParsePosition(0);
        try {
            getDecimalFormatter(formaterScale).parse(text, p);
            if (text.length() != p.getIndex() || p.getErrorIndex() != -1) {
                return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Verifica daca un string este numar sau nu
     * @param text
     * @return
     */
    public static boolean isValidInteger(String text) {
        try {
            if (isValidNumber(text)) {
                Integer.parseInt(text);
                return true;
            } else return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Converesion utils
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Converteste un vector de Integer in vector de int
     * @param irri Vectorul de Integer
     * @return vector de int
     */
    public static int[] toPrimitive(Integer[] irri) {
        int []array = new int[irri.length];
        for (int i=0;i<array.length;i++) {
            array[i] = irri[i].intValue();
        }
        return array;
    }

    /**
     * Convertesc un string in BigDecimal
     * @param text
     * @return
     */
    public static BigDecimal toBigDecimal(String text) {
        if (isValidNumber(text)) {
            ParsePosition p = new ParsePosition(0);
            Number number = getDecimalFormatter(formaterScale).parse(text, p);

            //Intorc un bigdecimal
            return new BigDecimal(number.toString());
        } else return null;
    }

    /**
     * Convertesc un string in Double
     * @param text
     * @return
     */
    public static Double toDouble(String text) {
        if (isValidNumber(text)) {
            ParsePosition p = new ParsePosition(0);
            Number number = getDecimalFormatter(formaterScale).parse(text, p);

            //Intorc un bigdecimal
            return new Double(number.doubleValue());
        } else return null;
    }

    /**
     * Convertesc un string in Integer
     * @param text
     * @return
     */
    public static Integer toInteger(String text) {
        if (isValidInteger(text)) {
            ParsePosition p = new ParsePosition(0);
            Number number = getDecimalFormatter(formaterScale).parse(text, p);

            //Intorc un bigdecimal
            return new Integer(number.intValue());
        } else return null;
    }


    public static void main(String[] args) {
        BigDecimal bigDecimal = toBigDecimal("100,0,000.20");
        System.out.println(bigDecimal);
    }
}
