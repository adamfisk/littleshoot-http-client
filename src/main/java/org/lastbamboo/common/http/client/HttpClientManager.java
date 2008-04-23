package org.lastbamboo.common.http.client;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.params.HttpClientParams;

/**
 * Class for creating new {@link HttpClient} instances in one centralized
 * location.
 */
public interface HttpClientManager
    {
    
    HttpMethod get(final String url) throws HttpException, IOException;
    
    HttpMethod post(final String url) throws HttpException, IOException;
    
    int executeMethod(HttpMethod method) throws HttpException, IOException;

    HttpConnectionManager getHttpConnectionManager();

    HttpClientParams getParams();

    }
