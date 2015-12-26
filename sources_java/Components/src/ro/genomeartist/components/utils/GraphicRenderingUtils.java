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

package ro.genomeartist.components.utils;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Clasa ce ofera anumite metode statice de desenare celule
 * @author iulian
 */
public class GraphicRenderingUtils {

    private static final int OFFSET_COLOR = 10;


     /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      Color settings and backrounds
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    /**
     * Desenez fundalul unei celule editabile
     */
    public static void drawEditableBackground(Graphics2D g2d, Color leftColor,
            Color rightColor, int width, int height, int iconWidth, boolean isReversed) {
        //FUnctia nu trebuie sa altereze culoarea de fundal la final
        Color oldColor = g2d.getColor();
        
        //Umplu panoul cu culoarea din dreapta
        g2d.setColor(rightColor);
        g2d.fillRect(0, 0, width, height);

        //Desenez un shield de culoare intermediara
        Color intermediateColor = colorAverage(leftColor, rightColor);
        g2d.setColor(intermediateColor);
        if (isReversed) {
            g2d.fillRect(width-iconWidth, 0, iconWidth, height);
        } else {
            g2d.fillRect(0, 0, iconWidth, height);
        }
        
        //Refac setarile de culoare initiale
        g2d.setColor(oldColor);
    }

    /**
     * Inchide o culoare, scazand din fiecare componenta offset-ul standard
     * @param color
     * @return
     */
    public static Color darkenColor(Color color) {
        return darkenColor(color, OFFSET_COLOR);
    }

    /**
     * Inchide o culoare, scazand din fiecare componenta offset-ul
     * @param color
     * @return
     */
    public static Color darkenColor(Color color, int offset) {
	return new Color(Math.max((int)(color.getRed()   -offset), 0),
			 Math.max((int)(color.getGreen() -offset), 0),
			 Math.max((int)(color.getBlue()  -offset), 0));
    }

    /**
     * Deschide o culoare, adunand la fiecare componenta offsetul standard
     * @param color
     * @return
     */
    public static Color lightenColor(Color color) {
        return lightenColor(color, OFFSET_COLOR);
    }

    /**
     * Deschide o culoare, adunand la fiecare componenta offsetul
     * @param color
     * @return
     */
    public static Color lightenColor(Color color, int offset) {
	return new Color(Math.min((int)(color.getRed()   +offset), 255),
			 Math.min((int)(color.getGreen() +offset), 255),
			 Math.min((int)(color.getBlue()  +offset), 255));
    }

    /**
     * Returns the average of two colors
     * @param color1
     * @param color2
     * @return
     */
    public static Color colorAverage(Color color1, Color color2) {
        int redValue = (color1.getRed()+color2.getRed())/2;
        int greenValue = (color1.getGreen()+color2.getGreen())/2;
        int blueValue = (color1.getBlue()+color2.getBlue())/2;
        return new Color(redValue, greenValue, blueValue);
    }

    /**
     * Shift a color towards another color
     * @param colorSource
     * @param colorDestination
     * @return
     */
    public static Color colorShift(Color colorSource, Color colorDestination) {
        int colorIntensity1 = getColorIntensity(colorSource);
        int colorIntensity2 = getColorIntensity(colorDestination);

        //Comparam intensitatiile
        if (colorIntensity1 > colorIntensity2)
            return darkenColor(colorSource, 40);
        else return lightenColor(colorSource, 40);
    }

    /**
     * Shift a color towards another color
     * @param colorSource
     * @param colorDestination
     * @return
     */
    public static Color colorShift(Color colorSource, Color colorDestination, int offset) {
        int colorIntensity1 = getColorIntensity(colorSource);
        int colorIntensity2 = getColorIntensity(colorDestination);

        //Comparam intensitatiile
        if (colorIntensity1 > colorIntensity2)
            return darkenColor(colorSource, offset);
        else return lightenColor(colorSource, offset);
    }

    /**
     * Obtain the intensity of a color
     *  (sum up the components)
     * @param color
     * @return
     */
    public static int getColorIntensity(Color color) {
        return color.getRed()+color.getGreen()+color.getBlue();
    }
}
