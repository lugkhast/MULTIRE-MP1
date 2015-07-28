/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multire.mp1.search;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import multire.mp1.search.ch.CHSearchStrategy;
import multire.mp1.search.chcr.CHCRSearchStrategy;
import multire.mp1.search.hrcc.HRCCSearchStrategy;

/**
 *
 * @author lugkhast
 */
public class ImageSearchEngine {

    private SearchStrategy searchStrategy = null;
    private File directoryToSearch = null;
    private List<PreprocessedImage> preprocessedImages = null;

    public ImageSearchEngine() {
    }

    public List<SearchResult> runImageSearch(File queryImageFile) throws IOException {
        if (searchStrategy == null || directoryToSearch == null) {
            return null;
        }

        List<SearchResult> results = new ArrayList<>();
        SearchResult result;
        PreprocessedImage prepQueryImg = searchStrategy.preprocessImage(queryImageFile);

        for (PreprocessedImage prepDbImg : preprocessedImages) {
            result = new SearchResult();
            result.setScore(
                    searchStrategy.compareImageFiles(prepQueryImg, prepDbImg)
            );
            result.setPreprocessedImage(prepDbImg);
            results.add(result);
        }

        Collections.sort(results);

        return results;
    }

    /**
     * @return the searchStrategy
     */
    public SearchStrategy getSearchStrategy() {
        return searchStrategy;
    }

    /**
     * @param searchStrategy the searchStrategy to set
     */
    public void setSearchStrategy(SearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
        this.preprocessedImages = null;
    }

    /**
     * @return the directoryToSearch
     */
    public File getDirectoryToSearch() {
        return directoryToSearch;
    }

    /**
     * @param directoryToSearch the directoryToSearch to set
     */
    public void setDirectoryToSearch(File directoryToSearch) {
        this.directoryToSearch = directoryToSearch;
        this.preprocessedImages = null;
    }

    public void preprocessImages() throws IOException {
        File[] imageFiles = directoryToSearch.listFiles(new ImageFileFilter());
        PreprocessedImage prepImage;
        preprocessedImages = new ArrayList<>();

        int prepCount = 0;
        for (File imageFile : imageFiles) {
            prepImage = searchStrategy.preprocessImage(imageFile);
            preprocessedImages.add(prepImage);

            System.out.printf(
                    "Preprocessed %d/%d\n",
                    ++prepCount, imageFiles.length
            );
        }
    }

    public boolean hasPreprocessedImages() {
        return preprocessedImages != null;
    }

    public static void main(String[] args) {
        File directory = new File("/home/lugkhast/Desktop/MULTIRE/");
        File query = new File("/home/lugkhast/Desktop/MULTIRE/calmdown.jpg");
        ImageSearchEngine searchEngine = new ImageSearchEngine();
        SearchStrategy strategy = new HRCCSearchStrategy();
        List<SearchResult> results;

        try {
            System.out.println("Starting...");
            searchEngine.setSearchStrategy(strategy);
            searchEngine.setDirectoryToSearch(directory);
            searchEngine.preprocessImages();

            System.out.println("Running image search...");
            results = searchEngine.runImageSearch(query);

            System.out.println("RESULTS:");
            for (SearchResult result : results) {
                System.out.printf(
                        "[%10.2f] %s\n",
                        result.getScore(),
                        result.getPreprocessedImage().getImageFile().getName()
                );
            }
        } catch (IOException ex) {
            Logger.getLogger(ImageSearchEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public SearchStrategy[] getImplementedSearchStrategies() {
        // Instantiate implemented SearchStrategies here!
        return new SearchStrategy[] {
            new CHSearchStrategy(),
            new HRCCSearchStrategy(),
            new CHCRSearchStrategy()
        };
    }

}
