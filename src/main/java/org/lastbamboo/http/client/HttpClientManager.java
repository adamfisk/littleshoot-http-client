package org.lastbamboo.http.client;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnection;

/**
 * Class for keeping track of <code>HttpClient</code> connections created for 
 * JXTA.  This provides access to the underlying <code>HttpConnection</code> 
 * instances for things such as closing the connection.
 */
public interface HttpClientManager
    {

    /**
     * Creates a new <code>HttpClient</code>
     * @return A new <code>HttpClient</code> instance associated with this
     * manager.
     */
    HttpClient createClient();

    /**
     * Accessor for the <code>HttpConnection</code> associated with the given
     * <code>HostConfiguration</code>.
     * 
     * @param configuration The <code>HostConfiguration</code> for the 
     * connection to look up.
     * @return The <code>HttpConnection</code> for the given configuration.
     */
    HttpConnection getConnection(final HostConfiguration configuration);
    }
