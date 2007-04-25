package org.lastbamboo.common.http.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.LongRange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lastbamboo.common.util.InputStreamHandler;
import org.springframework.context.ApplicationContext;

/**
 * Performs an HTTP download using HttpClient.
 */
public final class HttpClientRunner implements Runnable
    {

    /**
     * Logger for this class.
     */
    private static final Log LOG = LogFactory.getLog(HttpClientRunner.class);

    private final InputStreamHandler m_inputStreamHandler;

    /**
     * Listener for download events.
     */
    private final HttpListener m_listener;

    /**
     * The application context for retrieving localized messages.
     */
    private ApplicationContext m_applicationContext;

    private final CommonsHttpClient m_httpClient;

    private final HttpMethod m_httpMethod;

    /**
     * Creates a new downloader for the specified resource.
     *
     * @param protocol The protocol to use for establishing the socket to
     * write HTTP messages over.
     * @param id The network ID of the host to download from.
     * @param handler The handler for processing the input stream from the
     * remote host.
     * @param contextPath The context path for the resource.  This could
     * be, for example, "uri-res/N2R?" or "rss/".
     * @param resource The SHA1 <code>URI</code> resource.
     * @param listener The listener for download events.
     * @param ac The context for looking up localized resources.
     */
    public HttpClientRunner (final String protocol, final long id,
        final InputStreamHandler handler, final String contextPath,
        final String resource, final HttpListener listener,
        final ApplicationContext ac)
        {
        if (StringUtils.isBlank(contextPath))
            {
            throw new IllegalArgumentException("blank context path: "+
                contextPath);
            }
        if (StringUtils.isBlank(resource))
            {
            throw new IllegalArgumentException("blank resource"+resource);
            }
        if (ac == null)
            {
            throw new NullPointerException("null context");
            }
        this.m_inputStreamHandler = handler;
        this.m_listener = listener;
        this.m_applicationContext = ac;
        this.m_httpClient = new CommonsHttpClientImpl();
        
        // Override the default of attempting to connect 3 times.
        final HttpMethodRetryHandler retryHandler = 
            new DefaultHttpMethodRetryHandler(0, false);
        this.m_httpClient.getParams().setParameter(
            HttpMethodParams.RETRY_HANDLER, retryHandler);
        this.m_httpClient.getHttpConnectionManager().getParams().
            setConnectionTimeout(300*1000);
        this.m_httpMethod =
            new GetMethod(protocol+"://"+id+"/"+contextPath+resource);
        }

    /**
     * Creates a new HTTP client <code>Runnable</code> with the specified
     * collaborating classes.
     *
     * @param handler The class that should receive the <code>InputStream</code>
     * for the HTTP message body.
     * @param client The <code>HttpClient</code> instance that will send the
     * HTTP request.
     * @param method The HTTP method handler for the request.  This could be,
     * "GET" or "HEAD", for example.
     * @param listener The listener for HTTP events during the download.
     */
    public HttpClientRunner(final InputStreamHandler handler,
        final HttpClient client, final HttpMethod method,
        final HttpListener listener)
        {
        this(handler, client, method, listener, null);
        }
    
    public HttpClientRunner
            (final InputStreamHandler handler,
             final HttpClient client,
             final HttpMethod method,
             final HttpListener listener,
             final ApplicationContext ac)
        {
        this(handler,
             new CommonsHttpClientImpl(client),
             method,
             listener,
             ac);
        }
    
    public HttpClientRunner
            (final InputStreamHandler handler,
             final CommonsHttpClient client,
             final HttpMethod method,
             final HttpListener listener)
        {
        this(handler, client, method, listener, null);
        }
    
    /**
     * Creates a new HTTP client <code>Runnable</code> with the specified
     * collaborating classes.
     *
     * @param handler The class that should receive the <code>InputStream</code>
     * for the HTTP message body.
     * @param client The <code>HttpClient</code> instance that will send the
     * HTTP request.
     * @param method The HTTP method handler for the request.  This could be,
     * "GET" or "HEAD", for example.
     * @param listener The listener for HTTP events during the download.
     * @param ac The <code>ApplicationContext</code> instance for looking up
     * any necessary data.
     */
    public HttpClientRunner(final InputStreamHandler handler,
        final CommonsHttpClient client, final HttpMethod method,
        final HttpListener listener, final ApplicationContext ac)
        {
        this.m_inputStreamHandler = handler;
        this.m_httpClient = client;
        this.m_httpMethod = method;
        this.m_listener = listener;
        this.m_applicationContext = ac;
        }

    /**
     * Performs the download on a separate thread.
     */
    public void run()
        {
        try
            {
            handleDownload();
            }
        catch (final Throwable t)
            {
            LOG.error("Unexpected exception", t);
            }
        }

    /**
     * Handles the download request catching any expected exceptions.
     */
    private void handleDownload()
        {
        this.m_listener.onStatusEvent("Connecting...");
        try
            {
            download();
            }
        catch (final HttpException e)
            {
            LOG.warn("HTTP exception downloading, method: " + 
                this.m_httpMethod.getPath() + " " + e.getReason(), e);
            final int reasonCode = e.getReasonCode();
            final String status;
            if (reasonCode >= 100 && reasonCode < 600)
                {
                status = "Could Not Access User";
                }
            else
                {
                status = "Unknown response";
                }

            this.m_listener.onStatusEvent(status);
            this.m_listener.onHttpException(e);
            }
        catch (final IOException e)
            {
            LOG.warn("Could not connect to user -- method: " + 
                this.m_httpMethod.getPath(), e);
            this.m_listener.onStatusEvent("User Offline");
            this.m_listener.onCouldNotConnect();
            }
        }

    /**
     * Downloads the specified <code>URI</code> resource from the peer
     * with the given user ID.
     *
     * @throws IOException If an I/O (transport) error occurs. Some transport
     *  exceptions can be recovered from.
     * @throws HttpException If a protocol exception occurs. Usually protocol
     *  exceptions cannot be recovered from.
     */
    private void download () throws HttpException, IOException
        {
        LOG.trace ("Sending download request to user...");

        m_listener.onDownloadStarted ();
        try
            {
            executeHttpRequest ();
            LOG.trace ("Finished executing HTTP request...");
            }
        finally
            {
            // Release the connection if no other methods need it.
            m_httpMethod.releaseConnection();

            LOG.trace("Released connection...");
            }
        }

    /**
     * Executes the HTTP request for the download.
     *
     * @throws IOException If an I/O error occurs.
     */
    private void executeHttpRequest () throws IOException
        {
        LOG.trace ("Sending HTTP GET request for: " + 
            this.m_httpMethod.getPath());
        
        final long start = System.currentTimeMillis();
        m_httpClient.executeMethod (m_httpMethod);
        final long connected = System.currentTimeMillis();

        LOG.trace ("Received status code: " + m_httpMethod.getStatusCode ());

        // Notify the listener that we've connected to the user.
        this.m_listener.onConnect(connected-start);
        
        final int statusCode = m_httpMethod.getStatusCode ();
        if (statusCode == HttpStatus.SC_OK)
            {
            onTwoHundredResponse();
            }
        else if (statusCode == HttpStatus.SC_PARTIAL_CONTENT)
            {
            onPartialContent();
            }
        else
            {
            // TODO: We should still read the response body here!!
            noTwoHundredOk();
            }

        }

    /**
     * The method called when no OK response is received when making the HTTP
     * request.
     */
    private void noTwoHundredOk ()
        {
        try
            {
            LOG.warn("Did not receive 200 OK response for request: " +
                this.m_httpMethod.getURI());
            }
        catch (final URIException e)
            {
            LOG.error("Could not resolve URI", e);
            }
        if (m_applicationContext != null)
            {
            final String status =
                m_applicationContext.getMessage("download.response.code." +
                    m_httpMethod.getStatusCode (), null, Locale.getDefault ());
            m_listener.onStatusEvent (status);
            }
        
        m_listener.onNoTwoHundredOk (m_httpMethod.getStatusCode ());
        }

    private void onPartialContent() throws IOException
        {
        final Header rangeHeader = 
            this.m_httpMethod.getResponseHeader ("Content-Range");

        LOG.debug("Received range header: "+rangeHeader);
        if (rangeHeader == null)
            {
            throw new IOException("Received Partial Content response with " +
                "no Content-Range header");
            }
        String rangeString = rangeHeader.getValue().trim();
        if (!rangeString.startsWith("bytes"))
            {
            this.m_listener.onBadHeader(rangeHeader.toString());
            throw new IOException("Could not read header: "+rangeHeader);
            }
        
        rangeString = 
            StringUtils.substringBetween(rangeString, "bytes", "/").trim();
        
        final String minString = 
            StringUtils.substringBefore(rangeString, "-").trim();
        final String maxString = 
            StringUtils.substringAfter(rangeString, "-").trim();
        final long min = Long.parseLong(minString);
        final long max = Long.parseLong(maxString);
        final LongRange range = new LongRange(min, max);
        m_listener.onContentRange(range);
        onTwoHundredResponse();
        }
    
    /**
     * The methed called when an 200-level response is received when making 
     * the HTTP request.
     *
     * @throws IOException If an I/O error occurs.
     */
    private void onTwoHundredResponse () throws IOException
        {
        LOG.trace("Received "+this.m_httpMethod.getStatusCode() + 
            " for request: " +  this.m_httpMethod.getPath());
        
        final Header length = m_httpMethod.getResponseHeader ("Content-Length");

        if (length != null)
            {
            m_listener.onContentLength(
                Long.parseLong (length.getValue ()));
            }
        
//        final byte[] body = m_httpMethod.getResponseBody();

        final InputStream inputStream = m_httpMethod.getResponseBodyAsStream ();
//        final InputStream inputStream = new ByteArrayInputStream(body);

//        LOG.trace("body size: " + body.length);
        LOG.trace("inputStream class: " + inputStream.getClass());
        LOG.trace("Got input stream...");
        LOG.trace("Sending to handler: " + m_inputStreamHandler);
        
        // Uncompress the data if it's gzipped, otherwise just process it.
        final Header contentEncoding =
            m_httpMethod.getResponseHeader("Content-Encoding");

        if (contentEncoding == null)
            {
            m_inputStreamHandler.handleInputStream (inputStream);
            }
        else if (StringUtils.contains (contentEncoding.getValue (), "gzip"))
            {
            LOG.trace("Handling gzipped message body...");
            m_inputStreamHandler.handleInputStream(
                new GZIPInputStream (inputStream));
            }
        else
            {
            LOG.warn("Unrecognized content encoding: "+contentEncoding);
            }

        m_listener.onMessageBodyRead ();
        }
    }
