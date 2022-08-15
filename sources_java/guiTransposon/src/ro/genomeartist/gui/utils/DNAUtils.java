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

import java.lang.String;
import java.util.LinkedList;
import java.util.List;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author iulian
 */
public class DNAUtils {
    
    private static final int OUTPUT_NAME_TRIM = 30;
    
    /**
     * Fac reverse complement la un string
     * @param sequence
     * @return 
     */
    public static String reverseComplementSequence(String sequence) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            stringBuilder.append(complementNucleotide(c));
        }

        return stringBuilder.reverse().toString();
    }
    
    public static List<String> reverseComplementSequences(List<String> sequences) {
        List<String> reverseComplementedSequences = new LinkedList<String>();
        for (String sequence : sequences) {
            reverseComplementedSequences.add(DNAUtils.reverseComplementSequence(sequence));
        }
        
        return reverseComplementedSequences;
    }
    
    
    /**
     * Elimina spatiile, enterurile dintr-un string
     * @param name
     */
    public static List<String> extractCleanSequences(String content) {
        //Detect format
        String squeezedContent = StringUtils.squeezeString(content).trim();

        //FASTA. begins with ">"
        //GENBANK. ends with "\\" and contains keywork ORIGIN
        //RAW. separated by empty lines
        List<String> sequences;
        if (squeezedContent.startsWith(">") || squeezedContent.startsWith(";")) {
            sequences = getSequencesFromFasta(content);
        } else
        if (squeezedContent.contains("ORIGIN") && squeezedContent.endsWith("//")) {
            sequences = getSequencesFromGenbank(content);
        } else {
            sequences = getSequencesFromRawInput(content);
        }

        //Ultima verificare asupra stringului. pastrez doar nucleotide
        List<String> cleanedSequences = new LinkedList<String>();
        for (String string : sequences) {
            cleanedSequences.add(cleanString(string));
        }

        //Intorc rezultatul
        return cleanedSequences;
    }


    
    /**
     * Obtin secventa dintr-un format fasta
     * FASTA:
     *      liniile ce incep cu > sau ; se ignora
     * @param sequence
     * @return secventa continuta
     */
    private static List<String> getSequencesFromFasta(String content) {
        List<String> sequences = new LinkedList<String>();
        
        String buffer = "";
        StringTokenizer st = new StringTokenizer(content,"\n",false);
        String auxString;
        while (st.hasMoreElements()) {
            auxString = StringUtils.squeezeString(st.nextToken());
            if (auxString.startsWith(">")) {
                //Add previous sequence
                if (!buffer.isEmpty()) {
                    sequences.add(buffer);
                }
                
                //Reset buffer
                buffer = "";
                continue;
            } else 
            if (auxString.startsWith(";")) {
                //Ignore line
                continue;
            } else {
                buffer += auxString;
            }
        }

        //Add the last sequence
        if (!buffer.isEmpty()) {
            sequences.add(buffer);
        }
        
        return sequences;
    }

    /**
     * Obtin secventa din genbank
     * GENBANK:
     *      - secventa incepe dupa keyword-ul ORIGIN
     *      - secventa se termina inainte de linia ce se sfarseste cu "\\"
     * @param sequence
     * @return
     */
    private static List<String> getSequencesFromGenbank(String content) {
        List<String> sequences = new LinkedList<String>();
        
        StringTokenizer st = new StringTokenizer(content,"\n",false);
        String auxString;
        boolean foundOrigin = false;
        String buffer = "";
        while (st.hasMoreElements()) {
            auxString = StringUtils.squeezeString(st.nextToken());
            if (auxString.toLowerCase().startsWith("origin")) {
                //Add previous sequence
                if (!buffer.isEmpty()) {
                    sequences.add(buffer);
                }
                
                //Reset buffer
                foundOrigin = true;
                buffer = auxString.substring("ORIGIN".length());
                continue;
            } else 
            if (auxString.startsWith("//")) {
                //Ignore line
                foundOrigin = false;
                continue;
            }
            
            if (foundOrigin) {
                buffer += auxString;
            }
        }

        //Add the last sequence
        if (!buffer.isEmpty()) {
            sequences.add(buffer);
        }
        
        return sequences;
    }
    
    /**
     * Obtin secventa dintr-un format free form
     * @param content
     * @return 
     */
    private static List<String> getSequencesFromRawInput(String content) {
        List<String> sequences = new LinkedList<String>();
        
        String marker = "#";
        String markedContent = content.replaceAll("\n\\S*\n", "\n#\n");
        StringTokenizer st = new StringTokenizer(markedContent,"\n",false);
        String auxString;
        String buffer = "";
        while (st.hasMoreElements()) {
            auxString = StringUtils.squeezeString(st.nextToken());
            if (auxString.startsWith(marker)) {
                //Add previous sequence
                if (!buffer.isEmpty()) {
                    sequences.add(buffer);
                }
                
                //Reset buffer
                buffer = "";
                continue;
            } else {
                buffer += auxString;
            }
        }

        //Add the last sequence
        if (!buffer.isEmpty()) {
            sequences.add(buffer);
        }
        
        return sequences;
    }
    
    /**
     * Obtine numele secventei
     */
    public static List<String> getSequenceNames(String content) {
        String squeezedContent = StringUtils.squeezeString(content).trim();
        //FASTA. begins with ">"
        //GENBANK. ends with "\\" and contains keywork ORIGIN
        //RAW. separated by empty lines
        List<String> names = new LinkedList<String>();
        if (squeezedContent.startsWith(">") || squeezedContent.startsWith(";")) {
            StringTokenizer st = new StringTokenizer(content,"\n",false);
            String auxString;
            while (st.hasMoreElements()) {
                auxString = StringUtils.squeezeString(st.nextToken());
                if (auxString.startsWith(">")) {
                    names.add(auxString.substring(1, Math.min(auxString.length(), OUTPUT_NAME_TRIM+1)));
                }
            }
        } else if (squeezedContent.contains("ORIGIN") && squeezedContent.endsWith("//")) {
            StringTokenizer st = new StringTokenizer(content,"\n",false);
            String auxString;
            while (st.hasMoreElements()) {
                auxString = StringUtils.squeezeString(st.nextToken());
                if (auxString.toLowerCase().startsWith("accession")) {
                    Pattern pattern = Pattern.compile("accession", Pattern.CASE_INSENSITIVE);
                    Matcher match = pattern.matcher(auxString);
                    auxString = match.replaceAll("");
                    names.add(auxString.substring(0, Math.min(auxString.length(), OUTPUT_NAME_TRIM)));
                }
            }
        } else {
            return null;
        }
        return names;
    }
    
    /**
     * Pastreaza doar nucleotidele dintr-un string dat
     * @param sequence
     * @return
     */
    private static String cleanString(String sequence) {
        String result = new String();

        for (int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);
            if (isNucleotide(c))
                result += toNucleotide(c);
        }

        return result;
    }
    
    /**
     * Verifica daca un caracter este nucleotida sau nu
     * @param c
     * @return true daca c este nucleotida
     */
    public static boolean isNucleotide(char c) {
        switch (c) {
            case 'a':case 'A':
            case 'c':case 'C':
            case 'g':case 'G':
            case 't':case 'T':
            case 'n':case 'N':
            case '.':
                return true;
            default:
                return false;
        }
    }

    /**
     * Verifica daca un caracter este nucleotida sau nu
     * @param c
     * @return true daca c este nucleotida
     */
    public static char toNucleotide(char c) {
        switch (c) {
            case 'a':case 'A':
            case 'c':case 'C':
            case 'g':case 'G':
            case 't':case 'T':
                return c;
            default:
                return 'N';
        }
    }
    
    /**
     * Verifica daca un caracter este nucleotida sau nu
     * @param c
     * @return true daca c este nucleotida
     */
    public static char complementNucleotide(char c) {
        switch (c) {
            case 'a':case 'A':
                return 'T';
            case 'c':case 'C':
                return 'G';
            case 'g':case 'G':
                return 'C';
            case 't':case 'T':
                return 'A';
            case 'n':case 'N':
                return 'N';
            default:
                return c;
        }
    }
}
