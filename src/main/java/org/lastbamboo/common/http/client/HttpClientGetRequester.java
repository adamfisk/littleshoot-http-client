package org.lastbamboo.common.http.client;

import java.util.Collection;

import org.lastbamboo.common.util.Pair;

/**
 * Issues a post request using HTTP client. 
 */
public class HttpClientGetRequester implements HttpClientRequester
    {

    public int request(final String address,
        final Collection<Pair<String, String>> parameters)
        {
        final BaseHttpClientRequester baseRequester = 
            new BaseHttpClientRequester(address, parameters);
        return baseRequester.get();
        }

    }
