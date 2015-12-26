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

package ro.genomeartist.gui.controller.settings;

import java.io.File;

/**
 *
 * @author iulian
 */
public class SearchFolder implements Comparable {
    public File folderLocation;
    public String fileTitle;
    public boolean isTransposon;

    /**
     * Metoda de a compara dou obiecte de acest tip
     * @param o
     * @return
     */
    public int compareTo(Object o) {
        if (o instanceof SearchFolder) {
            SearchFolder searchFile = (SearchFolder) o;
            return this.fileTitle.compareTo(searchFile.fileTitle);
        } else throw new ClassCastException();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SearchFolder) {
            SearchFolder searchFile = (SearchFolder) obj;
            return this.fileTitle.equals(searchFile.fileTitle);
        } else throw new ClassCastException();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.fileTitle != null ? this.fileTitle.hashCode() : 0);
        return hash;
    }
}
