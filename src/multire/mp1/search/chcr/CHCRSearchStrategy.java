/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package multire.mp1.search.chcr;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import multire.mp1.cie.cieConvert;
import multire.mp1.search.PreprocessedImage;
import multire.mp1.search.SearchStrategy;

/**
 *
 * @author lugkhast
 */
public class CHCRSearchStrategy implements SearchStrategy {

    public final int BIN_COUNT = 159;

    @Override
    public double compareImageFiles(PreprocessedImage queryImage, PreprocessedImage datasetImage) {
        int totalDifferenceCenter = 0, totalDifferenceNonCenter = 0, totalDifference;
        List<Integer> queryBinsCenter, dsBinsCenter, queryBinsNonCenter, dsBinsNonCenter;
        CHCRPreprocessedImage chQueryImg = (CHCRPreprocessedImage) queryImage;
        CHCRPreprocessedImage chDatasetImg = (CHCRPreprocessedImage) datasetImage;
        
        queryBinsCenter = chQueryImg.getHistogramBinsCenter();
        dsBinsCenter = chDatasetImg.getHistogramBinsCenter();
        queryBinsNonCenter = chQueryImg.getHistogramBinsNonCenter();
        dsBinsNonCenter = chDatasetImg.getHistogramBinsNonCenter();
        
        for (int i = 0; i < BIN_COUNT; i++) {
            totalDifferenceCenter += Math.abs(
                    queryBinsCenter.get(i) - dsBinsCenter.get(i)
            );
            totalDifferenceNonCenter += Math.abs(
                    queryBinsNonCenter.get(i) - dsBinsNonCenter.get(i)
            );
        }
        
        totalDifference = totalDifferenceCenter + totalDifferenceNonCenter;
        
        return totalDifference;
    }

    private int getBinFromRGB(int rgb) {
        int red, green, blue;
        Color color = new Color(rgb);
        cieConvert converter = new cieConvert();

        red = color.getRed();
        green = color.getGreen();
        blue = color.getBlue();

        converter.setValues(red / 255.0, green / 255.0, blue / 255.0);
        return converter.IndexOf();
    }

    @Override
    public PreprocessedImage preprocessImage(File imageFile) throws IOException {
        CHCRPreprocessedImage preppedImage = new CHCRPreprocessedImage();
        BufferedImage image = ImageIO.read(imageFile);
        Integer[] colorBinsCenter = new Integer[BIN_COUNT];
        Integer[] colorBinsNonCenter = new Integer[BIN_COUNT];
        int bin, rgb;
        int cenlimitHeight, cenlimitWidth;
        
        Arrays.fill(colorBinsCenter, 0);
        Arrays.fill(colorBinsNonCenter, 0);
        preppedImage.setImageFile(imageFile);
        preppedImage.setImage(image);
        cenlimitHeight = (int) Math.ceil(image.getHeight()/.125);
        cenlimitWidth = (int) Math.ceil(image.getWidth()/.125);

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                rgb = image.getRGB(i, j);
                bin = getBinFromRGB(rgb);
                if((j >= cenlimitHeight || j <= image.getHeight() - cenlimitHeight) && 
                  ((i >= cenlimitWidth || i <= image.getWidth() - cenlimitWidth))){
                        colorBinsCenter[bin] += 1;
                }
                else { colorBinsNonCenter[bin] += 1; }
            }
        }
        
        preppedImage.setHistogramBinsCenter(Arrays.asList(colorBinsCenter));
        preppedImage.setHistogramBinsNonCenter(Arrays.asList(colorBinsNonCenter));

        return preppedImage;
    }
    
    @Override
    public String toString() {
        return "Color Histogram with Centering Refinement";
    }

}
