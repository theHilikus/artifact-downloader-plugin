package com.github.theHilikus.maven.downloaderPlugin.download;

import java.io.File;
import java.io.IOException;

import org.apache.maven.index.IteratorSearchResponse;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.github.theHilikus.maven.downloaderPlugin.repo.RepositoryIndex;
import com.github.theHilikus.maven.downloaderPlugin.repo.RepositoryIndexingException;

@Mojo(name = "download", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class DownloadMojo extends AbstractMojo {

    /* ====== Parameters ====== */
    @Parameter(defaultValue = "")
    private File cacheLocation;

    @Parameter
    private File indexLocation;

    /**
     * The id of the repository to use
     * 
     * @see #repositoryUrl
     */
    @Parameter(defaultValue = "central")
    private String repositoryId;

    /**
     * The url of the repository to use
     * 
     * @see #repositoryId
     */
    @Parameter(defaultValue = "http://repo1.maven.org/maven2")
    private String repositoryUrl;

    @Parameter(defaultValue = "false")
    private boolean forceIndexing;

    /**
     * the alternate location of the remote repository indexes (if they are not in default place)
     */
    @Parameter
    private String indexUpdateUrl;

    @Parameter(defaultValue = "true")
    private boolean searchable;

    @Parameter(defaultValue = "${project.build.directory}/downloads")
    private File outputDirectory;

    /* ====== Parameters END ====== */

    private DownloadConfig config = new DownloadConfig();

    public void execute() throws MojoExecutionException, MojoFailureException {
	Log log = getLog();

	RepositoryIndex repo = new RepositoryIndex(log, config);

	try {
	    repo.index(config.isForcedIndexing());
	    IteratorSearchResponse results = repo.search(config.getQuery());

	    // TODO: download artifacts
	} catch (IOException exc) {
	    log.error("[execute] Problem searching for artifacts", exc);
	} catch (RepositoryIndexingException exc) {
	    // TODO Auto-generated catch block
	    log.error("[execute]", exc);
	}

    }

    /**
     * @param cacheLocation the cacheLocation to set
     */
    private void setCacheLocation(File cacheLocation) {
	config.setCacheLocation(cacheLocation);
    }

    /**
     * @param indexLocation the indexLocation to set
     */
    private void setIndexLocation(File indexLocation) {
	config.setIndexLocation(indexLocation);
    }

    /**
     * @param repositoryId the repositoryId to set
     */
    private void setRepositoryId(String repositoryId) {
	config.setRepositoryId(repositoryId);
    }

    /**
     * @param repositoryUrl the repositoryUrl to set
     */
    private void setRepositoryUrl(String repositoryUrl) {
	config.setRepositoryUrl(repositoryUrl);
    }

    /**
     * @param forceIndexing the forceIndexing to set
     */
    private void setForceIndexing(boolean forceIndexing) {
	config.setForcedIndexing(forceIndexing);
    }

    /**
     * @param indexUpdateUrl the indexUpdateUrl to set
     */
    private void setIndexUpdateUrl(String indexUpdateUrl) {
	config.setIndexUpdateUrl(indexUpdateUrl);
    }

    /**
     * @param searchable the searchable to set
     */
    private void setSearchable(boolean searchable) {
	config.setSearchable(searchable);
    }

    /**
     * @param outputDirectory the outputDirectory to set
     */
    private void setOutputDirectory(File outputDirectory) {
	config.setOutputDirectory(outputDirectory);
    }

    public static void main(String[] args) {
	try {
	    new DownloadMojo().execute();
	} catch (MojoExecutionException exc) {
	    // TODO Auto-generated catch block
	} catch (MojoFailureException exc) {
	    // TODO Auto-generated catch block
	}
    }
}
