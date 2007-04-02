package org.lastbamboo.http.client;

import org.apache.commons.httpclient.HttpMethod;

/**
 * Our interface wrapper for HTTP clients.
 */
public interface LbHttpClient
    {
    /**
     * Executes a given HTTP method (from Commons HTTP).
     * 
     * @param method
     *      The method to execute.
     */
    void executeMethod
            (HttpMethod method);
    }
