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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 *
 * @author iulian
 */
public class RandomDataGenerator {
    private static final String charset = "!0123456789abcdefghijklmnopqrstuvwxyz";
    private static final int TEST_DECIMAL_SCALE = 3;
    private static Random rand = new Random(System.currentTimeMillis());

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      String
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

     /**
      * Generez stringuri random
      */
    public static String getRandomString(int length) {
        return getRandomString(rand, length);
    }

     /**
      * Generez stringuri random
      */
     private static String getRandomString(Random rand,int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int pos = rand.nextInt(charset.length());
            sb.append(charset.charAt(pos));
        }
        return sb.toString();
    }

    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      integer
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

     /**
      * Obtin un intreg
      * @return
      */
     public static int getRandomInt() {
         return rand.nextInt();
     }

     /**
      * Obtin un intreg
      * @return
      */
     public static int getRandomInt(int limit) {
         return rand.nextInt(limit);
     }

     /**
      * Obtin un intreg
      * @return
      */
     public static int getRandomInt(int min, int max) {
         int diff = max-min;
         return rand.nextInt(diff) + min;
     }


    /**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *      BigDecimal
     *~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

     /**
      * Obtin un big decimal de test
      * @param rand
      * @param scale
      * @return
      */
     public static BigDecimal getRandomBigDecimal() {
         return getRandomBigDecimal(rand, BigDecimal.ZERO, BigDecimal.ONE,
                 TEST_DECIMAL_SCALE);
     }

     /**
      * Obtin un big decimal de test
      * @param rand
      * @param scale
      * @return
      */
     public static BigDecimal getRandomBigDecimal(BigDecimal limit) {
         return getRandomBigDecimal(rand, BigDecimal.ZERO, limit,
                 TEST_DECIMAL_SCALE);
     }

     /**
      * Obtin un big decimal de test
      * @param rand
      * @param scale
      * @return
      */
     public static BigDecimal getRandomBigDecimal(BigDecimal min,BigDecimal max) {
         return getRandomBigDecimal(rand, min, max,
                 TEST_DECIMAL_SCALE);
     }

     /**
      * Obtin un big decimal de test
      * @param rand
      * @param scale
      * @return
      */
     private static BigDecimal getRandomBigDecimal(Random rand, 
             BigDecimal min, BigDecimal max, int scale) {
         BigDecimal diff = max.subtract(min);
         BigDecimal bigDecimal = new BigDecimal(rand.nextDouble());
         bigDecimal = bigDecimal.multiply(diff);
         bigDecimal = bigDecimal.add(min);
         bigDecimal = bigDecimal.setScale(scale,RoundingMode.HALF_EVEN);
         return bigDecimal;
     }
}
