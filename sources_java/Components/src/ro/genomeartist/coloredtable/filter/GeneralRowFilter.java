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

package ro.genomeartist.coloredtable.filter;

import javax.swing.RowFilter;

/**
 * Filtru preluat din clasele standard java
 * @author iulian
 */
public abstract class GeneralRowFilter extends RowFilter<Object,Object> {
        private int[] columns;

        public GeneralRowFilter(int[] columns) {
            checkIndices(columns);
            this.columns = columns;
        }

        public boolean include(Entry<? extends Object,? extends Object> value){
            int count = value.getValueCount();
            if (columns.length > 0) {
                for (int i = columns.length - 1; i >= 0; i--) {
                    int index = columns[i];
                    if (index < count) {
                        if (include(value, index)) {
                            return true;
                        }
                    }
                }
            }
            else {
                while (--count >= 0) {
                    if (include(value, count)) {
                        return true;
                    }
                }
            }
            return false;
        }

        protected abstract boolean include(
              Entry<? extends Object,? extends Object> value, int index);


        /**
         * Throws an IllegalArgumentException if any of the values in
         * columns are < 0.
         */
        private static void checkIndices(int[] columns) {
            for (int i = columns.length - 1; i >= 0; i--) {
                if (columns[i] < 0) {
                    throw new IllegalArgumentException("Index must be >= 0");
                }
            }
        }
    }
