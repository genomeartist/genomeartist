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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.RowFilter;

/**
 *
 * @author iulian
 */
public class RegexRowFilter extends GeneralRowFilter {
        private Matcher matcher;
        private Pattern regex;

        private RegexRowFilter(String regexString, int[] columns) {
            super(columns);

            regex = Pattern.compile(regexString.toLowerCase());
            if (regex == null) {
                throw new IllegalArgumentException("Pattern must be non-null");
            }
            matcher = regex.matcher("");
        }

        protected boolean include(
                Entry<? extends Object,? extends Object> value, int index) {
            matcher.reset(value.getStringValue(index).toLowerCase());
            return matcher.find();
        }

        /**
         * Metoda statica de obtinere a filtrului
         * @param <M>
         * @param <I>
         * @param regex
         * @param indices
         * @return
         */
        public static <M,I> RowFilter<M,I> getRegexFilter(String regex,
                                                               int... indices) {
                return (RowFilter<M,I>)new RegexRowFilter(regex,
                                                       indices);
        }
    }