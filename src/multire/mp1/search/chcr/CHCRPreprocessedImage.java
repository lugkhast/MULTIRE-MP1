/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package multire.mp1.search.chcr;

import multire.mp1.search.PreprocessedImage;
import java.util.List;

/**
 *
 * @author Earl
 */
public class CHCRPreprocessedImage extends PreprocessedImage {
    private List<Integer> histogramBinsCenter;
    private List<Integer> histogramBinsNonCenter;

    /**
     * @return the histogramBinsCenter
     */
    public List<Integer> getHistogramBinsCenter() {
        return histogramBinsCenter;
    }

    /**
     * @param histogramBinsCenter the histogramBinsCenter to set
     */
    public void setHistogramBinsCenter(List<Integer> histogramBinsCenter) {
        this.histogramBinsCenter = histogramBinsCenter;
    }

    /**
     * @return the histogramBinsNonCenter
     */
    public List<Integer> getHistogramBinsNonCenter() {
        return histogramBinsNonCenter;
    }

    /**
     * @param histogramBinsNonCenter the histogramBinsNonCenter to set
     */
    public void setHistogramBinsNonCenter(List<Integer> histogramBinsNonCenter) {
        this.histogramBinsNonCenter = histogramBinsNonCenter;
    }

    /**
     * @return the histogramBins
     */
    
}