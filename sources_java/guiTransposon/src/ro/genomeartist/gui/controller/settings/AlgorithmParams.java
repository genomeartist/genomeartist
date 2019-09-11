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

package ro.genomeartist.gui.controller.settings;

import ro.genomeartist.gui.utils.ReadOnlyConfiguration;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author iulian
 */
public class AlgorithmParams {
    //Enumetatie pentru tipul de extindere
    public enum ExpansionType {
        SHORT,
        MEDIUM,
        LONG,
        CUSTOM;

        /**
         * Implements toString-ul
         * @return 
         */
        @Override
        public String toString() {
            switch (this) {
                case SHORT:
                    return "Short";
                case MEDIUM:
                    return "Medium";
                case LONG:
                    return "Long";
                case CUSTOM:
                    return "Custom";
                default:
                    throw new AssertionError();
            }
        }
    }
    
    //Constante pentru identificare variabilelor
    private static final String NAME_OFFSET = "EXPANSION_OFFSET_ZERO";
    private static final String NAME_MATCH = "EXPANSION_SCORE_MATCH";
    private static final String NAME_MISMATCH = "EXPANSION_SCORE_MISMATCH";
    private static final String NAME_LENGTH_MOD = "EXPANSION_LENGTH_MODIFIER";
    private static final String NAME_PICK_DEPTH = "PICKING_SORT_DEPTH";
    private static final String NAME_NUCLEU = "COMPUNERE_LUNGIME_MINIMA_NUCLEU";
    private static final String NAME_SOLUTII = "COMPUNERE_NUMAR_MAXIM_SOLUTII";
    private static final String NAME_BONUS_COMPUNERE = "COMPUNERE_GENOM_TRANSPOSON_BONUS";

    //Constante pentru valorile standard ale parametrilor
    private static final ExpansionParameters EXPANSION_SHORT = 
            new ExpansionParameters(-5, 4, -2, 2);
    private static final ExpansionParameters EXPANSION_MEDIUM = 
            new ExpansionParameters(-4, 2, 0, 2);
    private static final ExpansionParameters EXPANSION_LONG = 
            new ExpansionParameters(-3, 2, 0, 2);
    
    //Constante boolean
    private static final int BOOL_FALSE = 0;
    private static final int BOOL_TRUE = 1;

    //Expansion params
    private int offsetZero;
    private int scoreMatch;
    private int scoreMismatch;
    private int lengthModifier;
    //Picking params
    private int pickingDepth;
    private int nucleu;
    private int solutii;
    private boolean bonusCompunere;

    /**~~~~~~~~~~~~~~~~~~~~~
     *       Getteri 
     *~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Get the type of expansion
     * @return 
     */
    public ExpansionType getExpansionType() {
        ExpansionParameters currentParameters = 
                new ExpansionParameters(offsetZero, scoreMatch, 
                scoreMismatch, lengthModifier);
        return getExpansionType(currentParameters);
    }
    
    /**
     * Getter
     * @return 
     */
    public int getOffsetZero() {
        return offsetZero;
    }

    /**
     * Getter
     * @return 
     */
    public int getScoreMatch() {
        return scoreMatch;
    }

    /**
     * Getter
     * @return 
     */
    public int getScoreMismatch() {
        return scoreMismatch;
    }    
    
    /**
     * Getter
     * @return 
     */
    public int getLengthModifier() {
        return lengthModifier;
    }    
    
    /**
     * Getter
     * @return 
     */
    public int getPickingDepth() {
        return pickingDepth;
    }    
    
    /**
     * Getter
     * @return 
     */
    public int getNucleu() {
        return nucleu;
    }    
    
    /**
     * Getter
     * @return 
     */
    public int getSolutii() {
        return solutii;
    }    
    
    /**
     * Getter
     * @return 
     */
    public boolean isBonusCompunere() {
        return bonusCompunere;
    }   
    

    /**~~~~~~~~~~~~~~~~~~~~~
     *       Setteri 
     *~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Setter
     * @param offsetZero 
     */
    public boolean setOffsetZero(int offsetZero) {
        if (this.offsetZero != offsetZero) {
            this.offsetZero = offsetZero;
            return true;
        } else {
            return false;
        }
    }    
    
    /**
     * Setter
     * @param offsetZero 
     */
    public boolean setScoreMatch(int scoreMatch) {
        if (this.scoreMatch != scoreMatch) {
            this.scoreMatch = scoreMatch;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Setter
     * @param offsetZero 
     */
    public boolean setScoreMismatch(int scoreMismatch) {
        if (this.scoreMismatch != scoreMismatch) {
            this.scoreMismatch = scoreMismatch;
            return true;
        } else {
            return false;
        }
    }    
    
    /**
     * Setter
     * @param offsetZero 
     */
    public boolean setLengthModifier(int lengthModifier) {
        if (this.lengthModifier != lengthModifier) {
            this.lengthModifier = lengthModifier;
            return true;
        } else {
            return false;
        }
    }    
    
    /**
     * Setter
     * @param offsetZero 
     */
    public boolean setPickingDepth(int pickingDepth) {
        if (this.pickingDepth != pickingDepth) {
            this.pickingDepth = pickingDepth;
            return true;
        } else {
            return false;
        }
    }    
    
    /**
     * Setter
     * @param offsetZero 
     */
    public boolean setNucleu(int nucleu) {
        if (this.nucleu != nucleu) {
            this.nucleu = nucleu;
            return true;
        } else {
            return false;
        }
    }    
    
    /**
     * Setter
     * @param offsetZero 
     */
    public boolean setSolutii(int solutii) {
        if (this.solutii != solutii) {
            this.solutii = solutii;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Setter
     * @param offsetZero 
     */
    public boolean setBonusCompunere(boolean bonusCompunere) {
        if (this.bonusCompunere != bonusCompunere) {
            this.bonusCompunere = bonusCompunere;
            return true;
        } else {
            return false;
        }
    }
    

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Metode de persistenta
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Incarc setarile dintr-un fisier
     */
    public void loadFromFile() {
        try {
            String paramfile = ReadOnlyConfiguration.getString("PARAM_SERVER");
            BufferedReader fileReader = new BufferedReader(new FileReader(paramfile));
            String line;            //O linie din fisier
            StringTokenizer st,aux; //Tokenizeri
            String leftToken,rightToken;  //Valoare din stanga si dreapta egalului
            String key,value;             //Valorile ce vor fi retinute
            while ((line = fileReader.readLine()) != null) {
                //Linia ce incepe cu "#" nu va fi interpretata
                if (!line.startsWith("#") && (line.indexOf("=")!=-1)){
                    st = new StringTokenizer(line,"=");
                    leftToken = st.nextToken();
                    rightToken = st.nextToken();

                    //Pregatesc cheia
                    key=new String();
                    aux = new StringTokenizer(leftToken, " \t");
                    if (aux.hasMoreTokens()) key += aux.nextToken();
                    while (aux.hasMoreTokens()) {
                        key += " "+aux.nextToken();
                    }

                    //Pregatesc valoare
                    value=new String();
                    aux = new StringTokenizer(rightToken, " \t");
                    if (aux.hasMoreTokens()) value += aux.nextToken();
                    while (aux.hasMoreTokens()) {
                        value += " "+aux.nextToken();
                    }

                    //Verific cu valorile deja stocate
                    if (NAME_OFFSET.equals(key)) {
                        offsetZero = Integer.parseInt(value);
                    } else
                    if (NAME_MATCH.equals(key)) {
                        scoreMatch = Integer.parseInt(value);
                    } else
                    if (NAME_MISMATCH.equals(key)) {
                        scoreMismatch = Integer.parseInt(value);
                    } else
                    if (NAME_LENGTH_MOD.equals(key)) {
                        lengthModifier = Integer.parseInt(value);
                    } else
                    if (NAME_PICK_DEPTH.equals(key)) {
                        pickingDepth = Integer.parseInt(value);
                    } else
                    if (NAME_NUCLEU.equals(key)) {
                        nucleu = Integer.parseInt(value);
                    } else
                    if (NAME_SOLUTII.equals(key)) {
                        solutii = Integer.parseInt(value);
                    } else
                    if (NAME_BONUS_COMPUNERE.equals(key)) {
                        int bool = Integer.parseInt(value);
                        if (bool == BOOL_FALSE)
                            bonusCompunere = false;
                        else bonusCompunere = true;
                    }
                }
            }
            fileReader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ResultSet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (java.io.IOException ex) {
            Logger.getLogger(ResultSet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Salveaza configuratia in fisier
     * @param configFile
     */
    public void saveToFile() {
        try {
            String paramfile = ReadOnlyConfiguration.getString("PARAM_SERVER");
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(paramfile, false));
            //entry
            fileWriter.write(NAME_OFFSET);
            fileWriter.write(" = ");
            fileWriter.write(offsetZero+"");
            fileWriter.newLine();
            //entry
            fileWriter.write(NAME_MATCH);
            fileWriter.write(" = ");
            fileWriter.write(scoreMatch+"");
            fileWriter.newLine();
            //entry
            fileWriter.write(NAME_MISMATCH);
            fileWriter.write(" = ");
            fileWriter.write(scoreMismatch+"");
            fileWriter.newLine();
            //entry
            fileWriter.write(NAME_LENGTH_MOD);
            fileWriter.write(" = ");
            fileWriter.write(lengthModifier+"");
            fileWriter.newLine();
            //entry
            fileWriter.write(NAME_PICK_DEPTH);
            fileWriter.write(" = ");
            fileWriter.write(pickingDepth+"");
            fileWriter.newLine();
            //entry
            fileWriter.write(NAME_NUCLEU);
            fileWriter.write(" = ");
            fileWriter.write(nucleu+"");
            fileWriter.newLine();
            //entry
            fileWriter.write(NAME_SOLUTII);
            fileWriter.write(" = ");
            fileWriter.write(solutii+"");
            fileWriter.newLine();
            //entry
            fileWriter.write(NAME_BONUS_COMPUNERE);
            fileWriter.write(" = ");
            if (bonusCompunere)
                fileWriter.write(BOOL_TRUE+"");
            else
                fileWriter.write(BOOL_FALSE+"");
            fileWriter.newLine();
            //END
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(ReadOnlyConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Utils
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Get the type of expansion
     * @return 
     */
    public static ExpansionType getExpansionType(ExpansionParameters currentParameters) {
        if (EXPANSION_SHORT.equals(currentParameters)) {
            return ExpansionType.SHORT; 
        } else
        if (EXPANSION_MEDIUM.equals(currentParameters)) {
            return ExpansionType.MEDIUM; 
        } else
        if (EXPANSION_LONG.equals(currentParameters)) {
            return ExpansionType.LONG; 
        } else {
            return ExpansionType.CUSTOM; 
        }
    }  
    
    /**
     * Get the type of expansion
     * @return 
     */
    public ExpansionParameters getExpansionParameters(ExpansionType expansionType) {
        switch (expansionType) {
            case SHORT:
                return EXPANSION_SHORT;
            case MEDIUM:
                return EXPANSION_MEDIUM;
            case LONG:
                return EXPANSION_LONG;
            case CUSTOM:
                return new ExpansionParameters(offsetZero, scoreMatch, 
                        scoreMismatch, lengthModifier);
            default:
                throw new AssertionError();
        }
    }   
    
    /**
     * Clasa ce incapsuleaza parametrii de extindere
     */
    public static class ExpansionParameters {
        private int offsetZero;
        private int scoreMatch;
        private int scoreMismatch;
        private int lengthModifier;

        /**
         * Constructor cu toti parametrii
         * @param offsetZero
         * @param scoreMatch
         * @param scoreMismatch
         * @param lengthModifier 
         */
        public ExpansionParameters(int offsetZero, int scoreMatch, 
                int scoreMismatch, int lengthModifier) {
            this.offsetZero = offsetZero;
            this.scoreMatch = scoreMatch;
            this.scoreMismatch = scoreMismatch;
            this.lengthModifier = lengthModifier;
        }

        /**
         * Getteri
         * @return 
         */
        public int getLengthModifier() {
            return lengthModifier;
        }

        /**
         * Getteri
         * @return 
         */
        public int getOffsetZero() {
            return offsetZero;
        }

        /**
         * Getteri
         * @return 
         */
        public int getScoreMatch() {
            return scoreMatch;
        }

        /**
         * Getteri
         * @return 
         */
        public int getScoreMismatch() {
            return scoreMismatch;
        }
        
        /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
         *      Comparare
         *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
        
        /**
         * Metoda de testare egalitate
         * @param obj
         * @return 
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ExpansionParameters other = (ExpansionParameters) obj;
            if (this.offsetZero != other.offsetZero) {
                return false;
            }
            if (this.scoreMatch != other.scoreMatch) {
                return false;
            }
            if (this.scoreMismatch != other.scoreMismatch) {
                return false;
            }
            if (this.lengthModifier != other.lengthModifier) {
                return false;
            }
            return true;
        }

        /**
         * Functia de hash
         * @param obj
         * @return 
         */
        @Override
        public int hashCode() {
            int hash = 5;
            hash = 59 * hash + this.offsetZero;
            hash = 59 * hash + this.scoreMatch;
            hash = 59 * hash + this.scoreMismatch;
            hash = 59 * hash + this.lengthModifier;
            return hash;
        }
    }
    
}
