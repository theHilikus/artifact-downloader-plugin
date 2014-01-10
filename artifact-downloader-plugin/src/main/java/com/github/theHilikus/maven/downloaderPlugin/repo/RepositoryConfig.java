package com.github.theHilikus.maven.downloaderPlugin.repo;

import java.io.File;

public interface RepositoryConfig {

    File getCacheLocation();

    File getIndexLocation();

    String getRepositoryId();

    String getIndexUpdateUrl();

    boolean isSearchable();

    String getRepositoryUrl();

}
