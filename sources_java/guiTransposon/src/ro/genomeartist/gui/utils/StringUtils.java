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
package ro.genomeartist.gui.utils;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author iulian
 */
public class StringUtils {
    //Unitati de masura timp
    private static final int MINUTES_2_SECONDS = 60;
    private static final int HOURS_2_SECONDS = 3600;
    
    
    /**
     * Transform secunde in format afisabil
     * @param seconds
     * @return
     */
    public static String timeToString(int time) {
        String result = "";
        int hours,minutes,seconds;
        int rest;
        String strHour,strMinute,strSecond;
        boolean hasHours = false, hasMinutes = false;
        
        //Calculez timpi
        hours = time / HOURS_2_SECONDS;
        rest = time % HOURS_2_SECONDS;
        minutes = rest / MINUTES_2_SECONDS;
        seconds = rest % MINUTES_2_SECONDS;

        //Conditiile de afisare
        //HOURS
        if (hours > 0) {
            strHour = hours + "";
            hasHours = true;
        } else {
            strHour = "";
            hasHours = false;
        }
        //MINUTES
        if (hasHours) {
            strMinute = minutes + "";
            hasMinutes = true;
        } else {
            if (minutes > 0) {
                strMinute = minutes + "";
                hasMinutes = true;
            } else {
                strMinute = "";
                hasMinutes = false;
            }
        }

        //SECONDS 
        strSecond = seconds+"";

        //Parsez stringurile si le fac padding cu 0
        if (hasHours) strMinute = padWithZero(strMinute, 2);
        if (hasMinutes) strSecond = padWithZero(strSecond, 2);

        if (hasHours) result += strHour + "h ";
        if (hasMinutes) result += strMinute + "m ";
        result += strSecond+"s";

        return result;
    }

    /**
     * Fac padding cu zero la un string
     */
    public static String padWithZero(String string, int count) {
        String padding = "";
        
        for (int i = string.length(); i < count; i++) {
            padding += "0";
        }

        return padding+string;
    }

    /**
     * Cauta si intoarce primul string care s-a potrivit
     * @return Stringul gasit sau null
     */
    public static String regexFind(Pattern pattern,String searchString) {
        return regexFind(pattern, searchString, 0);
    }

    /**
     * Cauta si intoarce primul string care s-a potrivit
     * @return Stringul gasit sau null
     */
    public static String regexFind(Pattern pattern,String searchString,int group) {
        Matcher matcher =
        pattern.matcher(searchString);

        boolean found = false;
        while (matcher.find()) {
            found = true;
            return matcher.group(group);
        }

        return null;
    }

    /**
     * Sanitize a filename replacing unwanted characters
     * @param filename
     * @return 
     */
    public static String sanitizeFilename(String filename) {
        String plainText = sanitizeName(filename);
        return plainText.replaceAll(" ", "_");
    }
    
    /**
     * Elimina spatiile, enterurile dintr-un string
     * @param name
     */
    public static String sanitizeName(String name) {
      StringTokenizer st = new StringTokenizer(name,"/\\\t\n;,><",false);
      String result="";
      while (st.hasMoreElements()) {
            result += st.nextElement();
        }
      return result;
    }

    /**
     * Intorc un string fara spatii
     * @param sequence
     * @return
     */
    public static String squeezeString(String sequence) {
      StringTokenizer st = new StringTokenizer(sequence," \t",false);
      String result="";
      while (st.hasMoreElements()) result += st.nextElement();
      return result;
    }

    /**
     * Compara doua stringuri
     * @param string1
     * @param string2
     * @return 
     */
    public static boolean areEqual(String string1, String string2) {
        return string1 == null ?
                (string2 == null ? true : false) :
                (string2 == null ? false : string1.equals(string2)
                );
    }
}
