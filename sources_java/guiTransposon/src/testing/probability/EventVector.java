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

import java.math.BigDecimal;

/**
 * Clasa ce reprezinta configuratia unor serii de evenimente
 * @author iulian
 */
public class EventVector {
    //Variabile de clasa
    private int numberOfEvents;
    private double headProbability;
    
    //Vectorul de evenimente
    private boolean[] eventVector;

    /**
     * Consruiesc un vector de evenimente
     * @param numberOfEvents
     * @param headProbability 
     */
    public EventVector(int numberOfEvents, double headProbability) {
        this.numberOfEvents = numberOfEvents;
        this.headProbability = headProbability;
        
        //Construiesc un vector de evenimente
        this.eventVector = new boolean[numberOfEvents];
        
        //Initializez vectorul la configuratia cel mai putin favorabila
        resetToStartEvent();
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Getteri publici
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Obtin numarul de evenimente din vector
     * @return 
     */
    public int getNumberOfEvents() {
        return numberOfEvents;
    }    
    
    /**
     * Obtin probabilitatea evenimentului pozitiv
     * @return 
     */
    public double getHeadProbability() {
        return headProbability;
    }

    /**
     * Obtine daca evenimentul de la pozitia i este favorabil
     * @param index
     * @return evenimentul de la pozitia index
     * @throws ArrayIndexOutOfBoundsException if the index is out of range
     *	       ({@code index < 0 || index >= size()})
     */
    public boolean getEventAt(int index) {
        if (index >= 0 && index < numberOfEvents) {
            return eventVector[index];
        } else {
	    throw new ArrayIndexOutOfBoundsException(index + " >= " + numberOfEvents);
	}
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Setteri publici
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Seteaza probabilitatea evenimentului pozitiv
     * @param headProbability 
     */
    public void setHeadProbability(double headProbability) {
        this.headProbability = headProbability;
    }
    
    /**
     * Obtine daca evenimentul de la pozitia i este favorabil
     * @param index
     * @throws ArrayIndexOutOfBoundsException if the index is out of range
     *	       ({@code index < 0 || index >= size()})
     */
    public void setEventAt(int index, boolean newValue) {
        if (index >= 0 && index < numberOfEvents) {
            eventVector[index] = newValue;
        } else {
	    throw new ArrayIndexOutOfBoundsException(index + " >= " + numberOfEvents);
	}
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Actiuni publice
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Calculeaza probabilitatea acestui eveniment
     * @return 
     */
    public BigDecimal computeVectorProbability() {
        BigDecimal result = BigDecimal.ONE;
        BigDecimal favorableProbability = new BigDecimal(headProbability);
        BigDecimal unfavorableProbability = new BigDecimal(1-headProbability);
        
        //Calculez probabilitatea
        for (int i = 0; i < eventVector.length; i++) {
            if (eventVector[i] == true) {
                result = result.multiply(favorableProbability);
            } else {
                result = result.multiply(unfavorableProbability);
            }
        }
        
        return result;
    }

    /**
     * Incrementeaza vectorul de evenimente
     * @return 
     */
    public void increment() {
        //Adun 1 la vectorul de evenimente
        incrementEventAt(0);
    }
    
    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *    Utils
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Reseteaza event vectorul la starea initiala
     */
    private void resetToStartEvent() {
        //Initializez vectorul la configuratia cel mai putin favorabila
        for (int i = 0; i < eventVector.length; i++) {
            eventVector[i] = false;
        }
    }
    
    /**
     * Operatie de adunare pe starea unui eveniment
     * @param index
     * @param state 
     */
    private void incrementEventAt(int index) {
        boolean baseEvent = this.getEventAt(index);
        
        if (baseEvent == true) {
            //Overflow
            this.setEventAt(index, false);
            if (index < this.getNumberOfEvents() - 1) {
                incrementEventAt(index+1);
            } else {
                resetToStartEvent();
            }
        } else {
            //no overflow
            this.setEventAt(index, true);
        }
    }
    
    /**
     * Metoda preferate de afisare
     * @return 
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        
        //Iterez prin vector
        for (int i = 0; i < eventVector.length; i++) {
            if (eventVector[i]) {
                stringBuilder.append("1");
            } else {
                stringBuilder.append("0");
            }
        }
        
        return stringBuilder.toString();
    }
}
