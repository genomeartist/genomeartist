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
package ro.genomeartist.components.jtable.rowheader.implementation;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

/**
 * Disables selection in list
 * @author iulian
 */
public class PhonyListSelectionModel implements ListSelectionModel {
        public void setSelectionInterval(int i, int i1) {
            //do nothing
        }

        public void addSelectionInterval(int i, int i1) {
            //do nothing
        }

        public void removeSelectionInterval(int i, int i1) {
            //do nothing
        }

        public int getMinSelectionIndex() {
            return 0;
        }

        public int getMaxSelectionIndex() {
            return 0;
        }

        public boolean isSelectedIndex(int i) {
            return false;
        }

        public int getAnchorSelectionIndex() {
            return 0;
        }

        public void setAnchorSelectionIndex(int i) {
            //do nothing
        }

        public int getLeadSelectionIndex() {
            return 0;
        }

        public void setLeadSelectionIndex(int i) {
            //do nothing
        }

        public void clearSelection() {
            //do nothing
        }

        public boolean isSelectionEmpty() {
            return true;
        }

        public void insertIndexInterval(int i, int i1, boolean bln) {
            //do nothing
        }

        public void removeIndexInterval(int i, int i1) {
            //do nothing
        }

        public void setValueIsAdjusting(boolean bln) {
            //do nothing
        }

        public boolean getValueIsAdjusting() {
            return false;
        }

        public void setSelectionMode(int i) {
            //do nothing
        }

        public int getSelectionMode() {
            return 0;
        }

        public void addListSelectionListener(ListSelectionListener ll) {
            //do nothing
        }

        public void removeListSelectionListener(ListSelectionListener ll) {
            //do nothing
        }
}
