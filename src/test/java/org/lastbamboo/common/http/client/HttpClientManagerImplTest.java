package org.lastbamboo.common.http.client;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.io.IOUtils;
import org.junit.Test;


public class HttpClientManagerImplTest
    {

    @Test public void testGet() throws IOException
        {
        
        checkUrl("http://www.google.com");
        checkUrl("https://www.google.com");
        }

    private void checkUrl(final String url) throws IOException
        {
        final HttpClientManager manager = new HttpClientManagerImpl();
        HttpMethod method = null;
        try
            {
            method = manager.get(url);
            final int statusCode = method.getStatusCode();
            assertTrue(statusCode > 199 && statusCode < 400);
            final InputStream is = method.getResponseBodyAsStream();
            
            // Just read the response for good measure.
            final String response = IOUtils.toString(is);
            }
        finally
            {
            if (method != null) method.releaseConnection();
            }
        }
    }
