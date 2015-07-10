/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multire.mp1.search.hrcc;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import multire.mp1.search.PreprocessedImage;

/**
 *
 * @author lugkhast
 */
public class HRCCPreprocessedImage extends PreprocessedImage {

    private BufferedImage blurredImage;

    private int computeBlurredPixelRGB(BufferedImage image, int x, int y) {
        Color pixelColor, tmpColor;
        ColorAverager colorAverager = new ColorAverager();

        if (x > 0) {
            if (y > 0) {
                colorAverager.add(image.getRGB(x - 1, y - 1));
            }

            colorAverager.add(image.getRGB(x - 1, y));

            if (y < image.getHeight() - 1) {
                colorAverager.add(image.getRGB(x - 1, y + 1));
            }
        }

        if (y > 0) {
            colorAverager.add(image.getRGB(x, y - 1));
        }

        if (y < image.getHeight() - 1) {
            colorAverager.add(image.getRGB(x, y + 1));
        }

        if (x < image.getWidth() - 1) {
            if (y > 0) {
                colorAverager.add(image.getRGB(x + 1, y - 1));
            }
            colorAverager.add(image.getRGB(x + 1, y));
            if (y < image.getHeight() - 1) {
                colorAverager.add(image.getRGB(x + 1, y + 1));
            }
        }

        Color averageColor = colorAverager.getAverage();
        return averageColor.getRGB();
    }

    private BufferedImage blurImage(BufferedImage image) {
        int imgWidth = image.getWidth();
        int imgHeight = image.getHeight();
        BufferedImage newImage = new BufferedImage(imgWidth, imgHeight, image.getType());
        int blurredRGB;

        for (int i = 0; i < imgWidth; i++) {
            for (int j = 0; j < imgHeight; j++) {
                blurredRGB = this.computeBlurredPixelRGB(image, i, j);
                newImage.setRGB(i, j, blurredRGB);
            }
        }

        return newImage;
    }

    @Override
    public void setImage(BufferedImage image) {
        super.setImage(image);

        BufferedImage newImage = this.blurImage(this.getImage());
    }

    public static void main(String[] args) {
        System.out.println("Starting!");
        File file = new File("/home/lugkhast/Desktop/calmdown.jpg");
        File outFile = new File("/home/lugkhast/Desktop/calmdownblurred.jpg");
        BufferedImage srcImg, blurredImg;
        HRCCPreprocessedImage prepImg = new HRCCPreprocessedImage();

        try {
            srcImg = ImageIO.read(file);
            System.out.println("Blurring...");
            blurredImg = prepImg.blurImage(srcImg);

            System.out.println("Saving...");
            ImageIO.write(blurredImg, "JPG", outFile);
        } catch (IOException ex) {
            Logger.getLogger(HRCCPreprocessedImage.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Done!");
    }
}
