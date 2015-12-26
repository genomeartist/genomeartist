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

package ro.genomeartist.components.tabbedpane;

import java.awt.Component;
import javax.swing.*;

/**
 *
 * @author iulian
 */
public class JClosableTabbedPane extends JTabbedPane {

    /**
     * Constructorul clasei
     */
    public JClosableTabbedPane() {
        super();
        setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        setFocusable(false);
    }

     /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *   Metode publice de adaugare ale clasei
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * SECONDARY
     * Adaug o componenta folosind numele
     * @param component
     * @return
     */
    @Override
    public Component add(Component component) {
        return this.add(component.getName(),component);
    }

    /**
     * SECONDARY
     * Ingnor constrangerile
     * @param component
     * @param constraints
     */
    @Override
    public void add(Component component, Object constraints) {
        this.add(component);
    }

    /**
     * SECONDARY
     * Adauga o componenta la un anumit indez
     * @param component
     * @param index
     * @return
     */
    @Override
    public Component add(Component component, int index) {
        return this.add(component.getName(), component, index);
    }

    /**
     * SECONDARY
     * Ignor constrangerile
     * @param component
     * @param constraints
     * @param index
     */
    @Override
    public void add(Component component, Object constraints, int index) {
        this.add(component, index);
    }

    /**
     * SECONDARY
     * Adaug specificatii pentru tab
     * @param title
     * @param component
     * @param index
     * @return
     */
    public Component add(String title, Component component, int index) {
        TabSpecifications specifications = new TabSpecifications();
        specifications.title = title;

        return this.add(specifications, component, index);
    }

    /**
     * SECONDARY
     * Adaug specificatii pentru tab
     * @param title
     * @param component
     * @return
     */
    @Override
    public Component add(String title, Component component) {
        TabSpecifications specifications = new TabSpecifications();
        specifications.title = title;

        return this.add(specifications, component);
    }

    /**
     * PRIMARY
     * Adaug o componente pe o anumita pozitie
     * @param text
     * @param component
     * @param index
     * @return
     */
    public Component add(TabSpecifications specifications, Component component, int index) {
        int i;

        //Adaug componenta in parinte
        super.add(component,index);
        i = this.indexOfComponent(component);

        //Setez Tabul pentru componenta nou adaugata
        if (i!= -1) {
            JComponentForTab componentForTab = new JComponentForTab(
                    specifications.icon,specifications.title,this,specifications.shouldBeBold);
            this.setTabComponentAt(i, componentForTab );
        }
        return component;
    }

    /**
     * PRIMARY
     * Adauga o componenta la Tabbed Pane
     * @param text  Textul atasat labelului
     * @param componenta Compunenta ce se adauga la Pane
     * @return
     */
    public Component add(TabSpecifications specifications, Component component) {
        int i;

        //Adaug componenta in parinte
        super.add(specifications.title,component);
        i = this.indexOfComponent(component);

        //Setez Tabul pentru componenta nou adaugata
        if (i!= -1) {
            JComponentForTab componentForTab = new JComponentForTab(
                    specifications.icon,specifications.title,this,specifications.shouldBeBold);
            this.setTabComponentAt(i, componentForTab );
        }
        return component;
    }

     /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *   Mecanism de modificate taburi
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Schimba titlul unui tab
     * @param index
     * @param title
     */
    @Override
    public void setTitleAt(int index, String title) {
        //Notific parintele
        super.setTitleAt(index, title);

        JComponentForTab tabComponent = (JComponentForTab) this.getTabComponentAt(index);
        tabComponent.setTitle(title);
    }

    /**
     * Setez iconul pentru o componenta de tab
     * @param index
     * @param icon
     */
    @Override
    public void setIconAt(int index, Icon icon) {
        super.setIconAt(index, icon);

        JComponentForTab tabComponent = (JComponentForTab) this.getTabComponentAt(index);
        tabComponent.setIcon(icon);
    }

    /**
     * Setez o componenta bold
     * @param index
     * @param icon
     */
    public void setTextBoldAt(int index, boolean shouldBeBold) {
        JComponentForTab tabComponent = (JComponentForTab) this.getTabComponentAt(index);
        tabComponent.setTextBold(shouldBeBold);
    }
}
