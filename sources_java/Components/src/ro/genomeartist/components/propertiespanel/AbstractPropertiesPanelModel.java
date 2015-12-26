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

package ro.genomeartist.components.propertiespanel;

import java.util.Hashtable;

/**
 * Modelul din spatele unui panou de proprietati
 * @author iulian
 */
public abstract class AbstractPropertiesPanelModel {
    public static final int AUTO_HEIGHT = 0;
    
    //Hashtable cu mapare rand componenta de editare
    private Hashtable<Integer,IPropertiesEditor> hashtableComponente;
    private Hashtable<Integer,IPropertiesHeader> hashtableHeaders;

    /**
     * Contruiesc un model de proprietati
     */
    public AbstractPropertiesPanelModel() {
        this.hashtableComponente = new Hashtable<Integer, IPropertiesEditor>();
        this.hashtableHeaders = new Hashtable<Integer, IPropertiesHeader>();
    }

    /**
     * Obtin numarul de randuri ce se vor afisa
     * @return
     */
    public abstract int getRowCount();

    /**
     * Obtin dimensiunea  labelului
     * @return
     */
    public abstract int getLabelWidth();

    /**
     * Obtin clasa valorii ce se afiseaza
     * @return
     */
    public abstract Class getClassAt(int row);

    /**
     * Obtin textul ce se va afisa pe label
     * @return
     */
    public abstract String getDescriptionAt(int row);

    /**
     * Obtin tipul componentei ce va fi
     * @return
     */
    public abstract IPropertiesEditor createEditorComponentAt(int row);

    /**
     * Obtin tipul componentei ce va fi
     * @return
     */
    public IPropertiesEditor getEditorComponentAt(int row) {
        IPropertiesEditor editor = hashtableComponente.get(Integer.valueOf(row));
        if (editor == null) {
            editor = createEditorComponentAt(row);
            
            if (editor != null)
                hashtableComponente.put(Integer.valueOf(row), editor);
        }

        //Intorc editorul de componente
        return editor;
    }

    /**
     * Obtin tipul componentei ce va fi
     * @return
     */
    public abstract IPropertiesHeader createHeaderComponentAt(int row);

    /**
     * 
     * @param row
     * @return
     */
    public IPropertiesHeader getHeaderComponentAt(int row){
        IPropertiesHeader header = hashtableHeaders.get(Integer.valueOf(row));
        if (header == null) {
            header = createHeaderComponentAt(row);

            if (header != null)
                hashtableHeaders.put(Integer.valueOf(row), header);
        }

        //Intorc editorul de componente
        return header;
    }

    /**
     * Inregistreaza o actiune ce se va rula la modificarea valorilor de pe
     * respectivul rand
     * @param row   Randul ce s-a modificat
     */
    public abstract void fireActionRowChanged(int row);

    /**
     * Specifica daca se incepe rand nou pentru acest rand
     * @param row
     * @return
     */
    public abstract boolean isRowOnNewline(int row);

    /**
     * Obtin dimensiunea  randului
     * @return
     */
    public abstract int getRowHeightAt(int row);

    /**
     * Obtin valoare proprietatilor default de pe anumita pozitie
     * @return null if no default
     */
    public Object getDefaultValueAt(int row) {
        return null;
    }
    
    /**
     * Obtin valoare proprietatii de pe anumita pozitie
     * @return
     */
    public abstract Object getValueAt(int row);

    /**
     * Setez o noua valoare pentru proprietate
     * @return
     */
    public abstract boolean setValueAt(int row, Object newValue);
 }
