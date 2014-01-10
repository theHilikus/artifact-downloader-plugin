package com.github.theHilikus.maven.downloaderPlugin.repo;

import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

public class RepositoryIndexingException extends Exception {

    public RepositoryIndexingException(Throwable exc) {
	this("", exc);
    }

    public RepositoryIndexingException(String msg, Throwable exc) {
	super(msg, exc);
    }

}
