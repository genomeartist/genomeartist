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

package ro.genomeartist.gui.controller.query;

import java.util.Date;

/**
 * Clasa ce retine informatii despre query obtinute din fisierul rezultat
 * @author iulian
 */
public class InfoQuery {
    public String queryName;        //Numele query-ului
    public String query;            //Query-ul initial
    public Date searchDate;         //Daca la care s-a cautat
    public int timpExecutie;        //Timpul de executie in secunde
    public int finalResultNumber;   //Nr de rezultate finale
    public int partialResultNumber; //Nr de rezultate partiale
}
