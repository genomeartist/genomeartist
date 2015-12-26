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

import java.util.Arrays;

/**
 *
 * @author iulian
 */
public class StringUtilities {
    
    /** Test whether a given string is a valid Java identifier.
    * @param id string which should be checked
    * @return <code>true</code> if a valid identifier
    */
    public static boolean isJavaIdentifier(String id) {
        if (id == null) return false;
        if (id.equals("")) return false; // NOI18N
        if (!(java.lang.Character.isJavaIdentifierStart(id.charAt(0))) )
            return false;
        for (int i = 1; i < id.length(); i++) {
            if (!(java.lang.Character.isJavaIdentifierPart(id.charAt(i))) )
                return false;
        }

        return Arrays.binarySearch(keywords, id) < 0;
    }
    
    /**
     * Vector cu identificatorii java
     */
    private static final String[] keywords = new String[] {
        //If adding to this, insert in alphabetical order!
        "abstract","assert","boolean","break","byte","case", //NOI18N
        "catch","char","class","const","continue","default", //NOI18N
        "do","double","else","extends","false","final", //NOI18N
        "finally","float","for","goto","if","implements", //NOI18N
        "import","instanceof","int","interface","long", //NOI18N
        "native","new","null","package","private", //NOI18N
        "protected","public","return","short","static", //NOI18N
        "strictfp","super","switch","synchronized","this", //NOI18N
        "throw","throws","transient","true","try","void", //NOI18N
        "volatile","while" //NOI18N
    };    
    
    /**
     * Fac escape la un string pentru a-l afisa in XML
     * @param text
     * @return
     */
    public static String removeWindowsCarriageReturn(String text){
        return text.replaceAll("\\r", "");
    }
}
