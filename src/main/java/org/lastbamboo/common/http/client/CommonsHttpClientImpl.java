package org.lastbamboo.common.http.client;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.lastbamboo.common.util.RuntimeIoException;

/**
 * An implementation of the Apache Commons HTTP client interface.
 */
public final class CommonsHttpClientImpl implements CommonsHttpClient
    {
    /**
     * The underlying Apache Commons client to which we delegate.
     */
    private final HttpClientManager m_commonsClient;
    
    /**
     * Constructs a new Apache Commons HTTP client.
     * 
     * @param connectionManager
     *      The Commons connection manager used to manage connections used by
     *      the underlying Commons HTTP client.
     */
    public CommonsHttpClientImpl(final HttpConnectionManager connectionManager)
        {
        this(new HttpClientManagerImpl(connectionManager));
        }
    
    /**
     * Constructs a new Apache Commons HTTP client.
     * 
     * @param commonsClient The underlying Apache Commons client to which to 
     * delegate.
     */
    private CommonsHttpClientImpl(final HttpClientManager commonsClient)
        {
        m_commonsClient = commonsClient;
        }

    /**
     * {@inheritDoc}
     */
    public int executeMethod(final HttpMethod method)
        {
        try
            {
            return m_commonsClient.executeMethod(method);
            }
        catch(final HttpException e)
            {
            throw new RuntimeHttpException(e);
            }
        catch(final IOException e)
            {
            throw new RuntimeIoException(e);
            }
        }

    /**
     * {@inheritDoc}
     */
    public HttpClientParams getParams ()
        {
        return m_commonsClient.getParams();
        }

    /**
     * {@inheritDoc}
     */
    public HttpConnectionManager getHttpConnectionManager
            ()
        {
        return m_commonsClient.getHttpConnectionManager();
        }
    }
