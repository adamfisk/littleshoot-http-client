package org.lastbamboo.common.http.client;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.apache.commons.httpclient.HttpMethod;
import org.lastbamboo.common.util.Pair;

/**
 * Issues a post request using HTTP client. 
 */
public class HttpClientPostRequester implements HttpClientRequester
    {
    
    private static final Collection<Pair<String, String>> EMPTY_PARAMS =
        Collections.unmodifiableList(new LinkedList<Pair<String,String>>());

    public int request(final String address,
        final Collection<Pair<String, String>> parameters)
        {
        final BaseHttpClientRequester baseRequester = 
            new BaseHttpClientRequester(address, parameters);
        return baseRequester.post();
        }

    public int request(final String url)
        {
        return request(url, EMPTY_PARAMS);
        }

    public int request(final URL url)
        {
        return request(url.toExternalForm());
        }
    }
