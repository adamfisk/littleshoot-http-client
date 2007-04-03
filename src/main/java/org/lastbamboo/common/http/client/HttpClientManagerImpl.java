package org.lastbamboo.common.http.client;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
    private static final Log LOG = 
        LogFactory.getLog(HttpClientManagerImpl.class);

    private MultiThreadedHttpConnectionManager m_httpConnectionManager;

    /**
     * Creates a new manager.
     */
    public HttpClientManagerImpl()
        {
        this.m_httpConnectionManager = new MultiThreadedHttpConnectionManager();
        this.m_httpConnectionManager.setMaxConnectionsPerHost(20);
        }
    
    /* (non-Javadoc)
     * @see org.lastbamboo.shoot.http.client.HttpClientManager#createClient()
     */
    public HttpClient createClient()
        {
        LOG.trace("Creating new connection..." + 
            this.m_httpConnectionManager.getConnectionsInUse() + " in use...");
        final HttpClient client = new HttpClient(this.m_httpConnectionManager);
        
        // Wait for awhile for the connection to resolve itself.
        client.setConnectionTimeout(30*1000);
        return client;
        }

    /* (non-Javadoc)
     * @see org.lastbamboo.shoot.http.client.HttpClientManager
     * #getConnection(org.apache.commons.httpclient.HostConfiguration)
     */
    public HttpConnection getConnection(final HostConfiguration configuration)
        {
        return this.m_httpConnectionManager.getConnection(configuration);
        }

    }
