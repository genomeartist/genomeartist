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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author iulian
 */
public class ImageManipulation {

    /**
     * Unesc mai multe imagini intr-o singura imagine mare
     * @param images
     * @return
     */
    public static final BufferedImage joinImages(ImageVector images) {
        int resultWidth;
        int resultHeight;
        Iterator <BufferedImage> imageIterator;

        //     Calculez dimensiuniile imaginii rezultat
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        resultWidth = DrawingConstants.REPORT_WIDTH;
        resultHeight = 0;

        //Calculez dimensiunea totala a imaginii iterand prin imaginile sursa
        imageIterator = images.iterator();
        while (imageIterator.hasNext()) {
            BufferedImage imageItem = imageIterator.next();
            resultHeight += imageItem.getHeight();
        }


        //     Compun imaginea rezultat
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        BufferedImage resultImage = new BufferedImage(resultWidth, resultHeight,
                BufferedImage.TYPE_INT_ARGB);
        //Calculez dimensiunea totala a imaginii iterand prin imaginile sursa
        imageIterator = images.iterator();
        int localY = 0;
        int localHeight;
        while (imageIterator.hasNext()) {
            BufferedImage imageItem = imageIterator.next();
            localHeight = imageItem.getHeight();

            BufferedImage drawingImage = resultImage.getSubimage(0, localY, resultWidth, localHeight);
            Graphics2D g2d = (Graphics2D) drawingImage.getGraphics();
            g2d.drawImage(imageItem, 0, 0, null);

            //Avansez pozitia pe care desenez
            localY += localHeight;
         }

        //Intorc imaginea rezultat
        return resultImage;
    }

    /**
     * Spart o imagine in mai multe imagini mici de dimensiune fiza
     * @param bigImage
     * @return
     */
    public static final ImageVector splitImage(BufferedImage bigImage, int splitSize) {
        ImageVector images = new ImageVector();
        
        //Calculez numarul de bucati care intra
        int cellWidth = bigImage.getWidth();
        int cellHeight = splitSize;
        int pieces = bigImage.getHeight() / cellHeight;

        //compun piesele
        for (int i = 0; i < pieces; i++) {
            BufferedImage cellImage = new BufferedImage(cellWidth, cellHeight, 
                    BufferedImage.TYPE_INT_ARGB);
            cellImage = bigImage.getSubimage(0, i*cellHeight, cellWidth, cellHeight);
            images.add(cellImage);
        }

        //Daca a mai ramas o bucata jos
        int leftover = bigImage.getHeight() % cellHeight;
        if ( leftover != 0) {
            BufferedImage cellImage = new BufferedImage(cellWidth, cellHeight,
                    BufferedImage.TYPE_INT_ARGB);
            cellImage = bigImage.getSubimage(0, pieces*cellHeight, cellWidth, leftover);
            images.add(cellImage);
        }
        
        return images;
    }
}
