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

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 *
 * @author iulian
 */
public class ImageUtils {
    
    /**
     * Redimensiuneaza imagini, pastrand totusi aspect ratio
     * @param originalImage
     * @param width
     * @param height
     * @return 
     */
    public static BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
	Graphics2D g2 = resizedImage.createGraphics();

        //Compute the ratio
        double width_ratio = ((float)width)/originalImage.getWidth();
        double height_ratio = ((float)height)/originalImage.getHeight(); 
        double ratio = Math.min(width_ratio, height_ratio);
        double newWidth = ratio * originalImage.getWidth();
        double newHeight = ratio * originalImage.getHeight();

        //Set rendering hints
	g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	g2.setRenderingHint(RenderingHints.KEY_RENDERING,
	RenderingHints.VALUE_RENDER_QUALITY);
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	RenderingHints.VALUE_ANTIALIAS_ON);
        
        //Draw resized image
        int imageWidth = (int) newWidth;
        int imageHeight = (int) newHeight;
        int startX = (width - imageWidth)/2;
        int startY = (height - imageHeight)/2;
        g2.drawImage(originalImage, startX, startY, imageWidth, imageHeight, null);
	g2.dispose();	
	g2.setComposite(AlphaComposite.Src);

        //return the image
        return resizedImage;
    }
    
}
