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
package testing.probability.profiles;

import testing.probability.NonOverlappedWindowsProbability;
import testing.probability.OverlappedWindowsProbability;

/**
 * Clasa care realizeaza profilul de sensitivitate
 * @author iulian
 */
public class Query_vs_Probability_Profile {
    /**
     * Metoda in care creez profilul de sensitivitate
     * @param args 
     */
    public static void main(String[] args) {
        //Query size
        int q_start = 10;
        int q_inc = 10;
        int q_stop = 100;
        
        //Probability
        int p_start = 81;
        int p_inc = 2;
        int p_stop = 97;
        
        //Windows
        int window_size = 10;
        
        //Overlapped
        for (int i = p_start; i <= p_stop; i += p_inc ) {
            for (int j = q_start; j <= q_stop; j += q_inc) {
                double headProbability = i / (double)100;
                OverlappedWindowsProbability overlappedWindowsProbability = 
                        new OverlappedWindowsProbability(j, window_size, headProbability);
                double result = overlappedWindowsProbability.computeProbability();
                System.out.print(result);
                System.out.print(";");
            }
            System.out.println("");
        }
        
        System.out.println("====================");
        
        //Non overlapped
        for (int i = p_start; i <= p_stop; i += p_inc ) {
            for (int j = q_start; j <= q_stop; j += q_inc) {
                double headProbability = i / (double)100;
                NonOverlappedWindowsProbability nonOverlappedWindowsProbability = 
                        new NonOverlappedWindowsProbability(j, window_size, headProbability);
                double result = nonOverlappedWindowsProbability.computeProbability();
                System.out.print(result);
                System.out.print(";");
            }
            System.out.println("");
        }
    }
}
