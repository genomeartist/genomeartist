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

package ro.genomeartist.gui.controller.genes;

import ro.genomeartist.gui.utils.ReadWriteConfiguration;

/**
 *
 * @author iulian
 */
public class GeneItem implements Comparable {
    private String name;           //Numele genei
    private String fisierOrigine;  //Bratul cromozomal pe care se afla
    public int locationStart;    //Pozitia de inceput
    public int locationEnd;    //Pozitia de sfarsit
    private boolean isComplement;          //Daca gena este pe catena complementare
    private String cyto;           //Regiunea cyto
    private String geneId;      //Id-ul flybase

    //###Display offset
    private int offset;


    /**
     * Constructorul
     */
    public GeneItem() {

        //Obtin offsetul de afisare a pozitiilor ( 0 sau +1)
        offset = Integer.parseInt((String)ReadWriteConfiguration.get("NUMBERING_OFFSET"));
    }

     /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *      Getters and setters
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    
    public String getCyto() {
        return cyto;
    }

    public void setCyto(String cyto) {
        this.cyto = cyto;
    }
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    

    public String getFisierOrigine() {
        return fisierOrigine;
    }

    public void setFisierOrigine(String fisierOrigine) {
        this.fisierOrigine = fisierOrigine;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public String getGeneId() {
        return geneId;
    }

    public void setGeneId(String geneId) {
        this.geneId = geneId;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public boolean isComplement() {
        return isComplement;
    }

    public void setIsComplement(boolean isComplement) {
        this.isComplement = isComplement;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public int getLocationEnd() {
        return locationEnd + offset;
    }

    public void setLocationEnd(int locationEnd) {
        this.locationEnd = locationEnd;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public int getLocationStart() {
        return locationStart + offset;
    }

    public void setLocationStart(int locationStart) {
        this.locationStart = locationStart;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //~~~~~~~~~~~~~~~~~~~~Ordinea naturala~~~~~~~~~~~~~~
    /**
     * Specifica ordinea naturala a obiectelor de tip geneItem
     * @param o
     * @return
     */
    public int compareTo(Object o) {
        if (o instanceof GeneItem) {
            GeneItem geneItem = (GeneItem) o;
            return this.name.compareToIgnoreCase(geneItem.name);
        } else throw new ClassCastException("Cannot compare");
    }

    @Override
    /**
     * Metoda standard de comparare a doua obiecte
     */
    public boolean equals(Object obj) {
        if (obj instanceof GeneItem) {
            int compareResult = this.compareTo(obj);
            if (compareResult == 0)
                return true;
            else return false;
        } else throw new ClassCastException("Cannot compare");
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }
}
