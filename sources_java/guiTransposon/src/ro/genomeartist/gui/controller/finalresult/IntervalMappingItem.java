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

package ro.genomeartist.gui.controller.finalresult;

import ro.genomeartist.gui.controller.genes.GeneItem;
import ro.genomeartist.gui.utils.ReadWriteConfiguration;
import ro.genomeartist.gui.controller.genes.GeneItemWrapper;
import ro.genomeartist.gui.controller.genes.GeneVector;
import javax.swing.JComponent;

/**
 * Maparea unui interval pe o anumita secventa
 * @author iulian
 */
public class IntervalMappingItem {
    //Informatii pozitionare
    private String fisierOrigine;    //bratul cromozomal (sau transposonul)
    public int pozitieQuery;         //Pozitia in secventa
    public int pozitieGenom;         //Pozitia in genom (sau transposon)
    public int lengthQuery;         //Lungimea pe query
    public int lengthGenom;         //Lungimea pe genom (sau transposon)
    public int lengthInitialQuery; //Lungimea initiala a secventei cautate

    //Legatura cu genele
    //WARING genele vin din fisierul rezultat fara a tine cont de comlementaritate
    // depi pentru complement, upstream e downstream si viceversa
    private GeneItem closestUpstream;    //Ce mai apropiata gena in amonte
    private GeneItem closestDownstream;  //Ce mai apropiata gena in aval
    private GeneVector insideGenes;      //Vector de gene cu care se intersecteaza intervalul

    //Informatii afisare
    private int outStringOffset;       //Pozitia pe stringul de aliniere
    private int outStringLength;       //Lungimea pe stringul de aliniere
    private int outStringTotalLength;  //Lungimea totala a stringului de aliniere
    
    //Informatii despre tipul de aliniere
    private boolean isComplement;    //Daca rezultatul este pe catena complementara
    private boolean isTransposon;    //Daca rezultatul este transposon

    //###Legatura catre vectorul parinte
    private IntervalMappingSet mappingParinte;

    //###Display offset
    private int offset;

    //###Wrapper afisare gene
    private GeneItemWrapper geneItemWrapper;

    /**
     * Construiesc un interval fara parinte
     */
    public IntervalMappingItem() {
        this(new IntervalMappingSet());
    }


    /**
     * Constructorul
     */
    public IntervalMappingItem(IntervalMappingSet mappingParinte) {
        //Setez parintele
        this.mappingParinte = mappingParinte;

        //Obtin offsetul de afisare a pozitiilor ( 0 sau +1)
        offset = Integer.parseInt((String)ReadWriteConfiguration.get("NUMBERING_OFFSET"));

        //Initializez vectorul de gene
        this.insideGenes = new GeneVector();

        //Initializez wrapperul pentru desenare gene
        this.geneItemWrapper = new GeneItemWrapper(mappingParinte,this);
    }

     /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      *      Getters and setters
      *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    public GeneItem getClosestDownstream() {
        return closestDownstream;
    }

    public void setClosestDownstream(GeneItem closestDownstream) {
        this.closestDownstream = closestDownstream;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public GeneItem getClosestUpstream() {
        return closestUpstream;
    }

    public void setClosestUpstream(GeneItem closestUpstream) {
        this.closestUpstream = closestUpstream;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public String getFisierOrigine() {
        return fisierOrigine;
    }

    public void setFisierOrigine(String fisierOrigine) {
        this.fisierOrigine = fisierOrigine;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public GeneVector getInsideGenes() {
        return insideGenes;
    }

    /**
     * Adauga o gena la sfarsitul vectorului
     * @param insideGene
     */
    public void addInsideGene(GeneItem insideGene) {
        this.insideGenes.add(insideGene);
    }

    /**
     * Adauga o gena la inceputul vectorului
     * @param insideGene
     */
    public void pushInsideGene(GeneItem insideGene) {
        this.insideGenes.insertElementAt(insideGene, 0);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    public GeneItemWrapper getGeneItemWrapper() {
        return geneItemWrapper;
    }

    public void setGeneItemWrapper(GeneItemWrapper geneItemWrapper) {
        this.geneItemWrapper = geneItemWrapper;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public boolean isComplement() {
        return isComplement;
    }

    public void setIsComplement(boolean isComplement) {
        this.isComplement = isComplement;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public boolean isTransposon() {
        return isTransposon;
    }

    public void setIsTransposon(boolean isTransposon) {
        this.isTransposon = isTransposon;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public int getLengthGenom() {
        return lengthGenom;
    }

    public void setLengthGenom(int lengthGenom) {
        this.lengthGenom = lengthGenom;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public int getLengthInitialQuery() {
        return lengthInitialQuery;
    }

    public void setLengthInitialQuery(int lengthInitialQuery) {
        this.lengthInitialQuery = lengthInitialQuery;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public int getLengthQuery() {
        return lengthQuery;
    }

    public void setLengthQuery(int lengthQuery) {
        this.lengthQuery = lengthQuery;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public int getOutStringLength() {
        return outStringLength;
    }

    public void setOutStringLength(int outStringLength) {
        this.outStringLength = outStringLength;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public int getOutStringOffset() {
        return outStringOffset;
    }

    public void setOutStringOffset(int outStringOffset) {
        this.outStringOffset = outStringOffset;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public int getOutStringTotalLength() {
        return outStringTotalLength;
    }

    public void setOutStringTotalLength(int outStringTotalLength) {
        this.outStringTotalLength = outStringTotalLength;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public int getPozitieStartGenom() {
        if(isComplement)
            return pozitieGenom + offset +lengthGenom - 1;
        else return pozitieGenom + offset;
    }

    public void setPozitieStartGenom(int pozitieGenom) {
        this.pozitieGenom = pozitieGenom;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public int getPozitieStartQuery() {
        return pozitieQuery + offset;
    }

    public void setPozitieQuery(int pozitieQuery) {
        this.pozitieQuery = pozitieQuery;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public int getPozitieStopQuery() {
        return pozitieQuery + offset +lengthQuery - 1;
    }

    public int getPozitieStopGenom() {
        if (isComplement)
            return pozitieGenom + offset;
        else return pozitieGenom + offset +lengthGenom - 1;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Get recomended height
     */
    public int getRecommendedHeight(JComponent component) {
        return geneItemWrapper.getAltRecommendedHeight(component);
    }

    /**
     * Get recomended qidth
     */
    public int getRecommendedWidth(JComponent component) {
        return geneItemWrapper.getAltRecommendedWidth(component);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Ontin indexul sau in parinte
     * @return
     */
    public int getIntervalIndex() {
        if (mappingParinte != null)
            return mappingParinte.indexOf(this);
        else return -1;
    }

}
