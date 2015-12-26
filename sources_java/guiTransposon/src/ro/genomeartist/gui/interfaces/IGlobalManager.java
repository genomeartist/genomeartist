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

package ro.genomeartist.gui.interfaces;

import java.awt.Frame;

/**
 * Interfata pentru a accesa anumiti parametrii global
 * @author iulian
 */
public interface IGlobalManager {

    /**
     * Obtine frame-ul de inceput
     * @return
     */
    public Frame getTheRootFrame();

    /**
     * Obtin glasspane-ul ferestrei principale
     * @return
     */
    public void turnOnGlasspane(boolean flag);

    /**
     * Setez mesajul de pe glasspane
     * @param row1
     * @param row2
     */
    public void setGlasspaneMessage(String row1, String row2);

}
