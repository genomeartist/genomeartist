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
package testing.probability;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Clasa face o verificare a algoritmilor de probabilitati
 * @author iulian
 */
public class GeneralProbabilityEvaluator {
    private static final int PRECISION = 10;

    /**
     * Metoda ce face eveluarea unui algoritm de probabilitati
     * @param probabilityAlgorithm
     * @return 
     */
    public static boolean validateAlgorithm(ProbabilityAlgorithm probabilityAlgorithm) throws IOException {
        return validateAlgorithm(probabilityAlgorithm,false);
    }
    
    /**
     * Metoda ce face eveluarea unui algoritm de probabilitati
     * @param probabilityAlgorithm
     * @return 
     */
    public static boolean validateAlgorithm(ProbabilityAlgorithm probabilityAlgorithm, boolean shouldPrint) 
            throws IOException {
        BigDecimal favorableCasesProbability = BigDecimal.ZERO;
        
        FileWriter fileWriter = new FileWriter("probability.txt");
        PrintWriter externalFile = new PrintWriter(fileWriter);
        
        //Construiesc primul eventVector
        EventVector eventVector = new EventVector(probabilityAlgorithm.getNumberOfEvents(), 
                probabilityAlgorithm.getHeadProbability());
        int totalNumberOfCases = (int) Math.pow(2, probabilityAlgorithm.getNumberOfEvents());
        for (int i = 0; i < totalNumberOfCases; i++) {
            boolean isFavorable = probabilityAlgorithm.isFavorableEventVector(eventVector);
            BigDecimal eventVectorProbability = BigDecimal.ZERO;
            if (shouldPrint) {
                eventVectorProbability = eventVector.computeVectorProbability();
                externalFile.println("\""+eventVector+"\";"+
                        eventVectorProbability.setScale(PRECISION, RoundingMode.HALF_EVEN)+";"+
                        isFavorable+";");
            } else {
                if (isFavorable) {
                    eventVectorProbability = eventVector.computeVectorProbability();
                }
            }
            
            //Daca este favorabil adun
            if (isFavorable) {
                favorableCasesProbability = favorableCasesProbability
                        .add(eventVectorProbability);
            }
            
            //Avansez la urmatorul element
            eventVector.increment();
        }
        
        //Close the file
        externalFile.close();
        
        //Testez daca rezultat dat de algorithm este acelasi cu cel calculat din cazuri
        double algorithmProbability = probabilityAlgorithm.computeProbability();
        double favorableProbability = favorableCasesProbability.doubleValue();
        System.out.println("favorableCasesProbability = "+favorableCasesProbability);
        System.out.println("computedProbability = "+algorithmProbability);
        return areEqual(algorithmProbability, favorableProbability, PRECISION);
    }

    
    /**
     * Comapara doua variabile doble rotunjiote la o anumita precizie
     * @param x
     * @param y
     * @param precision
     * @return 
     */
    private static boolean areEqual(double x, double y, int precision) {
        double difference = x - y;
        difference *= Math.pow(10, precision);
        difference = Math.round(difference);
        
        if (difference == 0) {
            return true;
        } else {
            return false;
        }
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Metoda de testare
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Testing area
     * @param args 
     */
    public static void main(String[] args) throws IOException {
        int n = 20;
        int k = 10;
        double p = 0.83;

        System.out.println("~~~~~~ Setarile initiale ~~~~~~~~");
        
        ProbabilityAlgorithm probabilityAlgorithm = 
                new OverlappedWindowsProbability(n, k, p);
//        ProbabilityAlgorithm probabilityAlgorithm = 
//                new NonOverlappedWindowsProbability(n, k, p);
        System.out.println("numberOfEvents = "+probabilityAlgorithm.getNumberOfEvents());
        System.out.println("numberOfConsecutiveHeads = "+
                probabilityAlgorithm.getNumberOfConsecutiveHeads());
        System.out.println("headProbability = "+probabilityAlgorithm.getHeadProbability());
        
        System.out.println("");
        System.out.println("~~~~~~ Testing validateAlgorithm() ~~~~~~~~");
        
        //Log time
        long start_time = System.currentTimeMillis();
        
        boolean result = GeneralProbabilityEvaluator.validateAlgorithm(probabilityAlgorithm);
        long time = System.currentTimeMillis(); 
        
        //Result1
        long res_time = time - start_time;
        System.out.println("validateAlgorithm = "+result);
        System.out.println("time = "+res_time);
        
    }
}
