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

package ro.genomeartist.components.transfer;

import java.util.Hashtable;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *        Clasa folosita in transfer de date, Reprezinta un vector de Echipamente si
 * doua hashtable-uri.
 *  Hashtable-urile pot fi folosite pentru a cara informatii impreuna ca incarcatura pachetului
 *  Pentru acest
 * @author iulian
 */
public abstract class PackObjects <E> extends Vector <E> {
    Hashtable<E, Integer> nodeHashMap;
    transient Hashtable<E, DefaultMutableTreeNode> nodeMap;

    //Retin hash-ul componentei sursa pentru acest transfer
    int ownerHash;

    //================================
    //      Constructori
    //================================

    public PackObjects() {
        super();

        //initializez hartile de noduri
        initNodeMap();
        initNodeHashMap();

        //initializez ownerul
        ownerHash = 0;
    }

     //================================
    //     Harta de hashuri
    //    - retin nodurile elementelor
    //================================
    private void initNodeMap() {
        nodeMap = new Hashtable<E, DefaultMutableTreeNode>();
    }

    public void setNode(E key,DefaultMutableTreeNode value) {
        nodeMap.put(key, value);
    }

    public DefaultMutableTreeNode getNode(E key) {
        return nodeMap.get(key);
    }
    
     //================================
    //     Harta de hashuri
    //    - retin hasurile parintilor pentru identificare
    //================================
    private void initNodeHashMap() {
        nodeHashMap = new Hashtable<E, Integer>();
    }

    public void setNodeHash(E key,Integer value) {
        nodeHashMap.put(key, value);
    }

    public Integer getNodeHash(E key) {
        return nodeHashMap.get(key);
    }

    //================================
    //   Metode de verificare owner
    //================================
    /**
     * Setez ownerul acestui pachet
     * @param owner
     */
    public void setOwner(Object owner) {
        this.ownerHash = owner.hashCode();
    }

    /**
     * Verific apartenenta unui pachet
     * @param pretendent
     */
    public boolean isOwner(Object pretendent) {
        return ownerHash == pretendent.hashCode();
    }


     //================================
    //     Interfata de obtinere flavor
    //================================

    /**
     * Implementare interfetei de impachetare
     * @return
     */
    public abstract String getFlavorName();
}
