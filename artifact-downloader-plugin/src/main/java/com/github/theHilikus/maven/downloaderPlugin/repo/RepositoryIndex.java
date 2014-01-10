package com.github.theHilikus.maven.downloaderPlugin.repo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.search.Query;
import org.apache.maven.index.Indexer;
import org.apache.maven.index.IteratorSearchRequest;
import org.apache.maven.index.IteratorSearchResponse;
import org.apache.maven.index.context.ExistingLuceneIndexMismatchException;
import org.apache.maven.index.context.IndexCreator;
import org.apache.maven.index.context.IndexingContext;
import org.apache.maven.index.updater.IndexUpdateRequest;
import org.apache.maven.index.updater.IndexUpdateResult;
import org.apache.maven.index.updater.IndexUpdater;
import org.apache.maven.index.updater.ResourceFetcher;
import org.apache.maven.index.updater.WagonHelper;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.wagon.Wagon;
import org.apache.maven.wagon.events.TransferListener;
import org.apache.maven.wagon.observers.AbstractTransferListener;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.sonatype.aether.transfer.TransferEvent;

public class RepositoryIndex {

    private String contextId;
    private Log log;
    private RepositoryConfig config;
    private static DefaultPlexusContainer plexusContainer;
    private IndexingContext context;
    private Indexer indexer;

    static {
	try {
	    plexusContainer = new DefaultPlexusContainer();
	} catch (PlexusContainerException exc) {
	    System.err.println("Error instantiating Plexus Container"); // can't use log since we
									// don't have it yet
	}
    }

    public RepositoryIndex(Log pLog, RepositoryConfig pConfig) {
	log = pLog;
	config = pConfig;
	contextId = config.getRepositoryId() + "-context";

    }

/*    public void index() throws RepositoryIndexingException {
	index(createDefaultResourceFetcher());
    }*/

    private static ResourceFetcher createDefaultResourceFetcher() throws RepositoryIndexingException {
	if (plexusContainer == null) {
	    throw new IllegalStateException("Could not instantiate Plexus Container");
	}

	try {
	    Wagon httpWagon = plexusContainer.lookup(Wagon.class, "http");

	    TransferListener listener = new AbstractTransferListener() {
		public void transferStarted(TransferEvent transferEvent) {
		    System.out.print("  Downloading " + transferEvent.getResource());
		}

		public void transferProgress(TransferEvent transferEvent, byte[] buffer, int length) {
		    System.out.println("Progress");
		}

		public void transferCompleted(TransferEvent transferEvent) {
		    System.out.println(" - Done");
		}
	    };

	    return new WagonHelper.WagonFetcher(httpWagon, listener, null, null);
	} catch (ComponentLookupException exc) {
	    throw new RepositoryIndexingException("Default Resource Fetcher creation failed", exc);
	}
    }

   public void index(boolean force) throws RepositoryIndexingException {
	index(createDefaultResourceFetcher(), force);
    }
/*
    public void index(ResourceFetcher resourceFetcher) {
	index(resourceFetcher, false);
    }*/

    public void index(ResourceFetcher resourceFetcher, boolean force) {
	if (plexusContainer == null) {
	    throw new IllegalStateException("Could not instantiate Plexus Container");
	}

	try {

	    indexer = plexusContainer.lookup(Indexer.class);

	    IndexUpdater indexUpdater = plexusContainer.lookup(IndexUpdater.class);
	    File localCache = config.getCacheLocation();
	    File indexDir = config.getIndexLocation();

	    // Creators we want to use (search for fields it defines)
	    List<IndexCreator> indexers = createIndexCreators(plexusContainer);

	    context = indexer.createIndexingContext(contextId, config.getRepositoryId(), localCache,
		    indexDir, config.getRepositoryUrl(), config.getIndexUpdateUrl(), config.isSearchable(), true,
		    indexers);

	    if (true) {
		System.out.println("Updating Index...");
		System.out.println("This might take a while on first run, so please be patient!");
		// Create ResourceFetcher implementation to be used with IndexUpdateRequest
		// Here, we use Wagon based one as shorthand, but all we need is a ResourceFetcher
		// implementation

		Date centralContextCurrentTimestamp = context.getTimestamp();
		IndexUpdateRequest updateRequest = new IndexUpdateRequest(context, resourceFetcher);
		IndexUpdateResult updateResult = indexUpdater.fetchAndUpdateIndex(updateRequest);
		if (updateResult.isFullUpdate()) {
		    System.out.println("Full update happened!");
		} else if (updateResult.getTimestamp().equals(centralContextCurrentTimestamp)) {
		    System.out.println("No update needed, index is up to date!");
		} else {
		    System.out.println("Incremental update happened, change covered " + centralContextCurrentTimestamp
			    + " - " + updateResult.getTimestamp() + " period.");
		}

		System.out.println();
	    }
	} catch (ComponentLookupException exc) {
	    // TODO Auto-generated catch block
	    log.error("[execute]", exc);
	} catch (ExistingLuceneIndexMismatchException exc) {
	    // TODO Auto-generated catch block
	    log.error("[execute]", exc);
	} catch (IllegalArgumentException exc) {
	    // TODO Auto-generated catch block
	    log.error("[execute]", exc);
	} catch (IOException exc) {
	    // TODO Auto-generated catch block
	    log.error("[execute]", exc);
	}
    }

    private List<IndexCreator> createIndexCreators(DefaultPlexusContainer plexusContainer)
	    throws ComponentLookupException {
	List<IndexCreator> indexers = new ArrayList<IndexCreator>();
	indexers.add(plexusContainer.lookup(IndexCreator.class, "min"));
	indexers.add(plexusContainer.lookup(IndexCreator.class, "jarContent"));
	indexers.add(plexusContainer.lookup(IndexCreator.class, "maven-plugin"));
	return indexers;
    }

    public IteratorSearchResponse search(Query query) throws IOException {
	if (context == null) {
	    throw new IllegalStateException("Repository needs to be indexed first");
	    //TODO: instead of exception, trigger index??
	}
	IteratorSearchRequest request = new IteratorSearchRequest(query, context);
	return indexer.searchIterator(request);
    }

}
