package com.github.theHilikus.maven.downloaderPlugin.download;

import java.io.File;

import org.apache.lucene.search.Query;

import com.github.theHilikus.maven.downloaderPlugin.repo.RepositoryConfig;


public class DownloadConfig implements RepositoryConfig {

    
    private File cacheLocation;
    private File indexLocation;
    private String repositoryId;
    private String indexUpdateUrl;
    private boolean searchable;
    private boolean forcedIndexing;
    /**
     * @param forcedIndexing the forcedIndexing to set
     */
    public void setForcedIndexing(boolean forcedIndexing) {
        this.forcedIndexing = forcedIndexing;
    }

    private String repositoryUrl;
    private File outputDirectory;

    public File getCacheLocation() {
	return cacheLocation;
    }

    public File getIndexLocation() {
	return indexLocation;
    }

    public String getRepositoryId() {
	return repositoryId;
    }

    public String getIndexUpdateUrl() {
	return indexUpdateUrl;
    }

    public boolean isSearchable() {
	return searchable;
    }

    public String getRepositoryUrl() {
	return repositoryUrl;
    }

    public Query getQuery() {
	// TODO Auto-generated method stub
	return null;
    }


    public boolean isForcedIndexing() {
	return forcedIndexing;
    }

    /**
     * @param cacheLocation the cacheLocation to set
     */
    public void setCacheLocation(File cacheLocation) {
        this.cacheLocation = cacheLocation;
    }

    /**
     * @param indexLocation the indexLocation to set
     */
    public void setIndexLocation(File indexLocation) {
        this.indexLocation = indexLocation;
    }

    /**
     * @param repositoryId the repositoryId to set
     */
    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    /**
     * @param indexUpdateUrl the indexUpdateUrl to set
     */
    public void setIndexUpdateUrl(String indexUpdateUrl) {
        this.indexUpdateUrl = indexUpdateUrl;
    }

    /**
     * @param searchable the searchable to set
     */
    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

    /**
     * @param repositoryUrl the repositoryUrl to set
     */
    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public void setOutputDirectory(File outputDirectory) {
	this.outputDirectory = outputDirectory;
	
    }

}
