package org.lastbamboo.common.http.client;

import java.io.IOException;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.protocol.DefaultProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.SSLProtocolSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for keeping track of <code>HttpClient</code> connections created.  
 * This provides access to the underlying <code>HttpConnection</code> 
 * instances for things such as closing the connection.  This also allows the
 * setting of general properties for connections we create.
 */
public class HttpClientManagerImpl implements HttpClientManager
    {
    
    /**
     * Logger for this class.
     */
    private final Logger m_log = LoggerFactory.getLogger(getClass());

    private final HttpClient m_httpClient; 
        

    /**
     * Creates a new manager.
     */
    public HttpClientManagerImpl()
        {
        this(new MultiThreadedHttpConnectionManager());
        }
    
    /**
     * Creates a new client manager with a custom connection manager.
     * 
     * @param connectionManager The connection manager to use.
     */
    public HttpClientManagerImpl(final HttpConnectionManager connectionManager)
        {
        m_httpClient = new HttpClient(connectionManager);
        }

    public HttpMethod get(final String url) throws HttpException, IOException
        {
        final GetMethod method = new GetMethod(url);
        return execute(method);
        }

    public HttpMethod post(final String url) throws HttpException, IOException
        {
        final PostMethod method = new PostMethod(url);
        return execute(method);
        }

    public int executeMethod(final HttpMethod method) throws HttpException, 
        IOException
        {
        execute(method);
        return method.getStatusCode();
        }
        
    private HttpMethod execute(final HttpMethod method) 
        throws HttpException, IOException
        {
        final HostConfiguration hc = method.getHostConfiguration();
        final Protocol p = hc.getProtocol();
        final String scheme = p == null ? "" : p.getScheme();
        
        if (scheme.equalsIgnoreCase("http"))
            {
            final Protocol prot = 
                new Protocol("http", new DefaultProtocolSocketFactory(), 80);
            hc.setHost(hc.getHost(), hc.getPort(), prot);
            }
        else if (scheme.equalsIgnoreCase("https"))
            {
            final ProtocolSocketFactory sslFactory = 
                new SSLProtocolSocketFactory();
            final Protocol prot = new Protocol("https", sslFactory, 443);
            hc.setHost(hc.getHost(), hc.getPort(), prot);
            }
        
        this.m_httpClient.executeMethod(hc, method);
        return method;
        }

    public HttpConnectionManager getHttpConnectionManager()
        {
        return this.m_httpClient.getHttpConnectionManager();
        }

    public HttpClientParams getParams()
        {
        return this.m_httpClient.getParams();
        }
    }
