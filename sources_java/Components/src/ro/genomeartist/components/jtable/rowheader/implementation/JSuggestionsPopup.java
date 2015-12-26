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

import ro.genomeartist.components.jtable.rowheader.RunnableSuggestion;
import ro.genomeartist.components.jtable.rowheader.RunnableSuggestion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author iulian
 */
public class JSuggestionsPopup extends JPopupMenu implements ActionListener {
    private Vector<RunnableSuggestion> suggestions;
    
    /**
     * Constructorul principal
     * @param suggestions 
     */
    public JSuggestionsPopup(Vector<RunnableSuggestion> suggestions) {
        super();
        
        //Pastrez variabila de clasa
        this.suggestions = suggestions;
        
        //Compun popup-ul
        for (int i = 0; i < suggestions.size(); i++) {
            RunnableSuggestion runnableSuggestion = suggestions.elementAt(i);
            JMenuItem menuItem;
            menuItem = new JMenuItem(runnableSuggestion.getName());
            menuItem.setActionCommand(i+"");
            menuItem.addActionListener(this);
            this.add(menuItem);
        }
    }

    
    /**
     * Metoda apelata la selectia in 
     * @param e 
     */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        Integer index = Integer.parseInt(cmd);
        
        //Rulez sugestia
        RunnableSuggestion runnableSuggestion = 
                suggestions.elementAt(index);
        if (runnableSuggestion != null)
            runnableSuggestion.run();
    }
    
    
    
}
