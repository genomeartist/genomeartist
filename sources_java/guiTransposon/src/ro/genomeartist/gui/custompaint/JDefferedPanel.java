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

package ro.genomeartist.gui.custompaint;

import ro.genomeartist.gui.interfaces.ICanPaint;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author iulian
 */
public class JDefferedPanel extends JPanel {
    ICanPaint paintObject;

    public JDefferedPanel(ICanPaint paintObject) {
        super();
        this.paintObject = paintObject;

        this.setPreferredSize( new Dimension(
                paintObject.getRecommendedWidth(this),
                paintObject.getRecommendedHeight(this)));
        this.setMinimumSize( new Dimension(
                paintObject.getRecommendedWidth(this),
                paintObject.getRecommendedHeight(this)));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        paintObject.paint(g, this.getWidth(), this.getHeight(), Color.WHITE, Color.BLACK);
    }

}
