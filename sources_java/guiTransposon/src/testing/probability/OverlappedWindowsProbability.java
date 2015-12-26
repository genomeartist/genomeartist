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

/**
 * Clasa ce incapsuleaza logica calcului probabilitatiilor cand ferestrele sunt overlapped
 * @author iulian
 */
public class OverlappedWindowsProbability extends ProbabilityAlgorithm {

    /**
     * Construiesc o clasa pentru calculul probabilitatiilor ferestrelor overlapped
     * @param numberOfEvents
     * @param numberOfConsecutiveHeads
     * @param headProbability 
     */
    public OverlappedWindowsProbability(int numberOfEvents, 
            int numberOfConsecutiveHeads, double headProbability) {
        super(numberOfEvents, numberOfConsecutiveHeads, headProbability);
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Actiuni publice
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Verific daca event vectorul contine "numberOfConsecutiveHeads" favorabile consecutive
     */
    public boolean isFavorableEventVector(EventVector eventVector) {
        int maxConsecutiveRuns = 0;
        int counter = 0;
        
        //Parcurc vectorul si numar evenimentele
        for (int i = 0; i < eventVector.getNumberOfEvents(); i++) {
            if (eventVector.getEventAt(i)) {
                counter++;
                
                //Verific daca am maxim
                if (counter > maxConsecutiveRuns) {
                    maxConsecutiveRuns = counter;
                }
            } else {
                counter = 0;
            }
        }
        
        //Daca maximul este mai mare sau egal decat "numberOfConsecutiveHeads" atunci e valid
        return maxConsecutiveRuns >= numberOfConsecutiveHeads;
    }
    
    /**
     * Obtine probabilitatea de a avea "numberOfConsecutiveHeads" 
     *  intr-un numar de "numberOfEvents" avand probabilitatea evenimentului pozitiv "headProbability"
     * @return 
     */
    public double computeProbability() {
        double minHeads = Double.valueOf(numberOfConsecutiveHeads);
        double intermediateResult;
        double auxDouble;
        double auxProbability;
        double[] computedProbability = new double[numberOfEvents+1];
        
        //Dynamic programmig
        computedProbability[0] = 0;
        for (int i = 1; i <= numberOfEvents; i++) {
            if (numberOfConsecutiveHeads > i) {
                intermediateResult = 0;
            } else {
                intermediateResult = Math.pow(headProbability, minHeads);
                for (int j = 1; j < numberOfConsecutiveHeads+1; j++) {
                    auxDouble = Math.pow(headProbability, j-1)*(1-headProbability);
                    
                    //Compute probability
                    if (i < j) {
                        auxProbability = 0;
                    } else {
                        auxProbability = computedProbability[i-j];
                    }
                    
                    //Compute result
                    intermediateResult += auxDouble*auxProbability;
                }
            }
            
            //Store the result
            computedProbability[i] = intermediateResult;
        }
        
        //Return the result
        //System.out.println("Vector: "+Arrays.toString(computedProbability));
        return computedProbability[numberOfEvents];
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Metoda de testare
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Testing area
     * @param args 
     */
    public static void main(String[] args) {
        int n = 40;
        int k = 10;
        double p = 0.81;

        System.out.println("~~~~~~ Setarile initiale ~~~~~~~~");
        
        ProbabilityAlgorithm probabilityAlgorithm = 
                new OverlappedWindowsProbability(n, k, p);
        System.out.println("numberOfEvents = "+probabilityAlgorithm.getNumberOfEvents());
        System.out.println("numberOfConsecutiveHeads = "+
                probabilityAlgorithm.getNumberOfConsecutiveHeads());
        System.out.println("headProbability = "+probabilityAlgorithm.getHeadProbability());
        
        System.out.println("");
        System.out.println("~~~~~~ Testing isFavorableEventVector() ~~~~~~~~");
        
        //Creez un event vector
        EventVector eventVector = new EventVector(n, p);
        int offset = 10;
        for (int i = 0; i < k; i++) {
            eventVector.setEventAt(i+offset, true);
        }
        
        boolean isFavorableEventVector = probabilityAlgorithm.isFavorableEventVector(eventVector);
        System.out.println(eventVector);
        System.out.println("isFavorableEventVector = "+isFavorableEventVector);
        
        System.out.println("");
        System.out.println("~~~~~~ Testing computeProbability() ~~~~~~~~");
        double result = 0;
        
        //Log time
        long start_time = System.currentTimeMillis();
        
        //Run
        result = probabilityAlgorithm.computeProbability();
        long time = System.currentTimeMillis(); 
        
        //Result1
        long res_time = time - start_time;
        System.out.println("result = "+result);
        System.out.println("time = "+res_time);
    }
}
