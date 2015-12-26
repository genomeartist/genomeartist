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


/**
 *
 * @author iulian
 */
public class SearchQuery {
    private String queryName;
    private String queryContent;

    /**~~~~~~~~~~~~~~~~~~~
     *    Consructori
     *~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * Constructor
     */
    public SearchQuery() {
        this("", "");
    }
    
    /**
     * Constructor
     */
    public SearchQuery(String queryContent) {
        this("", queryContent);
    }
    
    /**
     * Constructor
     */
    public SearchQuery(String queryName, String queryContent) {
        this.queryName = queryName;
        this.queryContent = queryContent;
    }

    /**~~~~~~~~~~~~~~~~~~~
     *    Getteri
     *~~~~~~~~~~~~~~~~~~~~~*/
    
    /**
     * @return the queryName
     */
    public String getQueryName() {
        return queryName;
    }

    /**
     * @return the queryContent
     */
    public String getQueryContent() {
        return queryContent;
    }

    /**~~~~~~~~~~~~~~~~~~~
     *    Setteri
     *~~~~~~~~~~~~~~~~~~~~~*/
    
    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public void setQueryContent(String queryContent) {
        this.queryContent = queryContent;
    }
}
