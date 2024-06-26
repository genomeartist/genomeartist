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

package ro.genomeartist.gui.controller.externalcalls.actions;

import ro.genomeartist.components.swingworkers.progressworker.AbstractProgressCallable;
import ro.genomeartist.components.utils.NumberUtils;
import ro.genomeartist.gui.utils.StringUtils;
import java.io.BufferedReader;
import java.util.regex.Pattern;

/**
 *
 * @author iulian
 */
public class TaggedFileConsumerCallable extends AbstractProgressCallable<Boolean> {
    //COnstante pentru taguri
    private static final Pattern REGEX_TAG =
            Pattern.compile("^@(\\w+)[\\s]*:[\\s]*([^:]*)");
    private static final String TAG_INFO = "info";
    private static final String TAG_PROGRESS = "progress";
    private static final String TAG_ERROR = "error";

    //Variabile de parametrii
    private BufferedReader br;
    private String stopWord;

    /**
     * Construiesc un consumator de fisiere taguite
     * @param br
     * @param stopWord
     */
    public TaggedFileConsumerCallable(BufferedReader br, String stopWord) {
        this.br = br;
        this.stopWord = stopWord;
    }


    /**
     * Metoda apelata pentru a face actiunea
     * @return
     * @throws Exception
     */
    public Boolean call() throws Exception {
        String line;
        while ((line = br.readLine()) != null) {
            if (line.startsWith(stopWord))
                break;
            
            //DEBUG, afisez
            //System.out.println(line);

            //Consume content
            String tag = StringUtils.regexFind(REGEX_TAG, line, 1);
            String value = StringUtils.regexFind(REGEX_TAG, line, 2);

            //System.out.println(line);
            if (tag != null && value != null) {
                if (TAG_INFO.equals(tag)) {
                    this.setProgressInfo(value);
                } else
                if (TAG_PROGRESS.equals(tag)) {
                    if (NumberUtils.isValidInteger(value))
                        this.setProgressValue(Integer.valueOf(value));
                } else
                if (TAG_ERROR.equals(tag)) {
                    this.addErrorMessage(value);
                }
            }
        }

        //default value
        return Boolean.TRUE;
    }


}
