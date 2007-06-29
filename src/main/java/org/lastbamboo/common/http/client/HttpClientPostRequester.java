package org.lastbamboo.common.http.client;

import java.io.IOException;
import java.util.Collection;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.StatusLine;
import org.apache.commons.httpclient.methods.PostMethod;
import org.lastbamboo.common.util.Pair;
import org.lastbamboo.common.util.RuntimeIoException;
import org.lastbamboo.common.util.UriUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Issues a post request using HTTP client. 
 */
public class HttpClientPostRequester implements HttpClientRequester
    {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final HttpClient m_httpClient = new HttpClient();
    
    public int request(final String address,
        final Collection<Pair<String, String>> parameters)
        {
        final StringBuilder sb = new StringBuilder ();
        sb.append (address);
        sb.append (UriUtils.getUrlParameters (parameters));
        final String url = sb.toString ();
        
        LOG.debug("Sending URL: {}", url);
        final PostMethod method = new PostMethod(url);
        try
            {
            this.m_httpClient.executeMethod(method);
            final int statusCode = method.getStatusCode();
            final StatusLine statusLine = method.getStatusLine();
            if (statusCode != HttpStatus.SC_OK)
                {
                LOG.warn("ERROR ISSUING POST REQUEST!!" + 
                    statusLine + "\n" + method.getResponseBodyAsString());
                }
            else
                {
                LOG.debug("Successfully wrote request...");
                }
            return statusCode;
            }
        catch (final HttpException e)
            {
            LOG.warn("HTTP error writing request: "+url, e);
            throw new RuntimeHttpException(
                "HTTP error writing request: "+url, e);
            }
        catch (final IOException e)
            {
            LOG.warn("IOException writing request: "+url, e);
            throw new RuntimeIoException(
                "IOException writing request: "+url, e);
            }
        finally
            {
            method.releaseConnection();
            }
        }

    }
