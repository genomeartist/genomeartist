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
package testing.probability.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

/**
 *
 * @author iulian
 */
public class ProbabilityDrafts {
    
    /**
     * Check http://wangling.me/2008/09/the-probability-of-runs-of-k-consecutive-heads-in-n-coin-tosses/ 
     *  for detailed explanation
     * @param n number of events (coin-tosses)
     * @param k number of consecutive heads
     * @return p the probability of having at least k consecutive heads
     */
    public static double computeCasesOfConsecutiveHeads(int n, int k) {
        if (n == k) {
            return 1;
        } else 
        if (n < k) {
            return 0;
        } else {
            double term1 = 2*computeCasesOfConsecutiveHeads(n-1,k);
            double term2 = Math.pow(2, n-k-1);
            double term3 = computeCasesOfConsecutiveHeads(n-k-1,k);
            return term1 + term2 - term3;
        }
    }
    
    /**
     * Check http://wangling.me/2008/09/the-probability-of-runs-of-k-consecutive-heads-in-n-coin-tosses/ 
     *  for detailed explanation
     * @param n number of events (coin-tosses)
     * @param k number of consecutive heads
     * @return p the probability of having at least k consecutive heads
     */
    public static double computeProbabilityOfConsecutiveHeadsA(int n, int k) {
        double favorable_cases = computeCasesOfConsecutiveHeads(n, k);
        double total_cases = Math.pow(2, n);
        double result = favorable_cases/total_cases;
        return result;
    }
    
    /**
     * http://marknelson.us/2011/01/17/20-heads-in-a-row-what-are-the-odds/
     * K step Fibbonaci
     * @param k number of consecutive heads
     * @param n number of events (coin-tosses)
     * @return 
     */
    public static BigInteger kStepFib( int k, int n ) {
            int arraySize = n + 1;
            if ( arraySize < 3 )
                    arraySize = 3;
            BigInteger fibs[] = new BigInteger[ arraySize ];
            fibs[ 0 ] = new BigInteger( "0" );
            fibs[ 1 ] = new BigInteger( "1" );
            fibs[ 2 ] = new BigInteger( "1" );
            for ( int i = 3 ; i <= n ; i++ ) {
                    BigInteger fib = new BigInteger( "0" );
                    int start = i - k;
                    for ( int j = i - k ; j < i ; j++ )
                            if ( j > 0 )
                                    fib = fib.add( fibs[ j ] );
                    fibs[ i ] = fib;
                    if ( start >= 0 )
                            fibs[ start ] = null;
                    if (( i % 1000)  == 0 )
                                    System.out.print( "i= " + i + "\r" );
            }
            return fibs[ n ];
    }
    
    /**
     * Check http://marknelson.us/2011/01/17/20-heads-in-a-row-what-are-the-odds/
     *  for detailed explanation
     * @param n number of events (coin-tosses)
     * @param k number of consecutive heads
     * @return p the probability of having at least k consecutive heads
     */
    public static BigDecimal computeProbabilityOfConsecutiveHeadsB(int n, int k) {
	final int showdigits = 20;
        BigDecimal f = new BigDecimal( kStepFib( k, n + 2) );
        BigDecimal p = new BigDecimal( (new BigInteger( "2" )).pow( n ) );
        String fs = f.toString();
        //System.out.println( "\nfib(" + k + "," + (n+2) + ") = " + fs.substring(0, showdigits)  + " (" + (fs.length() - showdigits ) + " digits elided)" );
        String ps = p.toString();
        //System.out.println( "2^" + n + " = " + ps.substring(0, showdigits) + " (" + (ps.length() - showdigits ) + " digits elided)" );
        BigDecimal answer = f.divide(p);
        answer = (new BigDecimal(1)).subtract(answer);
        String s = answer.toString();
        //System.out.println( "Div = " + s.substring(0, showdigits ) + " (" + (s.length() - showdigits ) + " digits elided)" );
        return answer;
    }
    
    /**
     * http://www.askamathematician.com/2010/07/q-whats-the-chance-of-getting-a-run-of-k-successes-in-n-bernoulli-trials-why-use-approximations-when-the-exact-answer-is-known/
     * @param n number of events (coin-tosses)
     * @param k number of consecutive heads
     * @param headProbability probability of showing a head
     * @return q the probability of having at least k consecutive heads
     */
    public static double computeProbabilityOfConsecutiveHeadsC(int n, int k,
            double headProbability) {
        double numCoins = Double.valueOf(n);
        double minHeads = Double.valueOf(k);
        double result = 0;
        double intermediateResult;
        double auxDouble;
        double auxProbability;
        double[] computedProbability = new double[n+1];
        
        //Dynamic programmig
        computedProbability[0] = 0;
        for (int i = 1; i <= n; i++) {
            if (k > i) {
                intermediateResult = 0;
            } else {
                intermediateResult = Math.pow(headProbability, minHeads);
                for (int j = 1; j < k+1; j++) {
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
        System.out.println("Vector: "+Arrays.toString(computedProbability));
        return computedProbability[n];
    }
    
    /**
     * http://www.askamathematician.com/2010/07/q-whats-the-chance-of-getting-a-run-of-k-successes-in-n-bernoulli-trials-why-use-approximations-when-the-exact-answer-is-known/
     * @param n number of events (coin-tosses)
     * @param k number of consecutive heads
     * @param headProbability probability of showing a head
     * @return the probability of having at least k consecutive heads
     */
    public static double computeProbabilityOfOverlappedWindows(int n, int k,
            double headProbability) {
        double minHeads = Double.valueOf(k);
        double intermediateResult;
        double auxDouble;
        double auxProbability;
        double[] computedProbability = new double[n+1];
        
        //Dynamic programmig
        computedProbability[0] = 0;
        for (int i = 1; i <= n; i++) {
            if (k > i) {
                intermediateResult = 0;
            } else {
                intermediateResult = Math.pow(headProbability, minHeads);
                for (int j = 1; j < k+1; j++) {
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
        return computedProbability[n];
    }
    
    /**
     * http://www.askamathematician.com/2010/07/q-whats-the-chance-of-getting-a-run-of-k-successes-in-n-bernoulli-trials-why-use-approximations-when-the-exact-answer-is-known/
     * @param n number of events (coin-tosses)
     * @param k number of consecutive heads
     * @param headProbability probability of showing a head
     * @return q the probability of having at least k consecutive heads
     */
    public static double computeProbabilityOfNonOverlappedWindows(int n, int k,
            double headProbability) {
        double result;
        
        double e1 = Math.pow(headProbability, k);
        double e2 = 1-e1;
        int numberOfWindows = (int) Math.floor((double)n/(double)k);
        double e3 = Math.pow(e2, numberOfWindows);
        result = 1-e3;
        
        //Return the result
        //System.out.println("Vector: "+Arrays.toString(computedProbability));
        return result;
    }
    
    
    /**
     * Testing area
     * @param args 
     */
    public static void main(String[] args) {
        int n = 100;
        int k = 14;
        double result1 = 0;
        BigDecimal result2 = BigDecimal.ZERO;
        double result3 = 0;
        
        //Log time
        long start_time = System.currentTimeMillis();
        
        //First run
        //result1 = computeProbabilityOfConsecutiveHeadsA(n, k);
        long time1 = System.currentTimeMillis(); 
        
        //Second run
        //result2 = computeProbabilityOfConsecutiveHeadsB(n, k);
        long time2 = System.currentTimeMillis(); 
        
        //Third run
        result3 = computeProbabilityOfConsecutiveHeadsC(n, k, 0.81);
        long time3 = System.currentTimeMillis(); 
        
        //Result
        long res_time1 = time1 - start_time;
        System.out.println("result1 = "+result1);
        System.out.println("time1 = "+res_time1);
        
        long res_time2 = time2 - time1;
        System.out.println("result2 = "+result2);
        System.out.println("time2 = "+res_time2);
        
        long res_time3 = time3 - time2;
        System.out.println("result3 = "+result3);
        System.out.println("time3 = "+res_time3);
    }
}
