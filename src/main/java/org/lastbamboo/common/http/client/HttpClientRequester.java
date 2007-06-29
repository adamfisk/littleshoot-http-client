package org.lastbamboo.common.http.client;

import java.util.Collection;

import org.lastbamboo.common.util.Pair;

/**
 * Interface for utility classes for issuing HTTP client requests.
 */
public interface HttpClientRequester
    {

    /**
     * Writes a request to the specified address.
     * 
     * @param baseUrl The base URL to send the request to.
     * @param parameters The request parameters.
     * @return The HTTP status code.
     */
    int request(String baseUrl, Collection<Pair<String,String>> parameters);
    }
