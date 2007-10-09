package org.lastbamboo.common.http.client;

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
     * @return The response code.
     */
    int executeMethod
            (HttpMethod method);
    }
