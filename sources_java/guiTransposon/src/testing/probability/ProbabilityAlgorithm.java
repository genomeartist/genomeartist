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
 *
 * @author iulian
 */
public abstract class ProbabilityAlgorithm {
    //Variabile de clasa
    protected int numberOfEvents;
    protected int numberOfConsecutiveHeads;
    protected double headProbability;

    /**
     * Construiesc o clasa pentru calculul probabilitatiilor
     * @param numberOfEvents
     * @param numberOfConsecutiveHeads
     * @param headProbability 
     */
    public ProbabilityAlgorithm(int numberOfEvents, 
            int numberOfConsecutiveHeads, double headProbability) {
        this.numberOfEvents = numberOfEvents;
        this.numberOfConsecutiveHeads = numberOfConsecutiveHeads;
        this.headProbability = headProbability;
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Getteri publici
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Numarul de evenimente
     */
    public int getNumberOfEvents() {
        return numberOfEvents;
    }    
    
    /**
     * Obtine numarul de evenimente consecutive cautat
     * @return 
     */
    public int getNumberOfConsecutiveHeads() {
        return numberOfConsecutiveHeads;
    }    
    
    /**
     * Obtine probabilitatea evenimetului pozitiv
     * @return 
     */
    public double getHeadProbability() {
        return headProbability;
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Setteri publici
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Seteaza numarul total de evenimente
     * @param numberOfEvents 
     */
    public void setNumberOfEvents(int numberOfEvents) {
        this.numberOfEvents = numberOfEvents;
    }    
    
    /**
     * Seteaza numarul de evenimente favorabile cautat
     * @param numberOfConsecutiveHeads 
     */
    public void setNumberOfConsecutiveHeads(int numberOfConsecutiveHeads) {
        this.numberOfConsecutiveHeads = numberOfConsecutiveHeads;
    }    
    
    /**
     * Seteaza probabilitatea evenimentului favorabil
     * @param headProbability 
     */
    public void setHeadProbability(double headProbability) {
        this.headProbability = headProbability;
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Actiuni publice
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Specifica daca o anumita configuratie este valida pentru algoritm
     * @param caseRepresentation
     * @return 
     */
    public abstract boolean isFavorableEventVector(EventVector eventVector);    
    
    /**
     * Calculeaza probabilitate conforma algoritmului propriu
     * @return 
     */
    public abstract double computeProbability();
}
