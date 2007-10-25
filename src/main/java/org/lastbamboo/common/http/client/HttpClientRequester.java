package org.lastbamboo.common.http.client;

import java.net.URL;
import java.util.Collection;

import org.apache.commons.httpclient.HttpMethod;
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
     * @return The HTTP method instance, including the status code and access 
     * to the response body.
     */
    int request(String baseUrl, 
        Collection<Pair<String,String>> parameters);

    /**
     * Issue a request with no parameters.
     * 
     * @param url The URL to issue the request to.
     * @return The HTTP method instance.
     */
    int request(String url);

    /**
     * Issue a request with no parameters.
     * 
     * @param url The URL to issue the request to.
     * @return The HTTP method instance.
     */
    int request(URL url);
    }
