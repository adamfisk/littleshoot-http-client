package org.lastbamboo.common.http.client;

import java.io.File;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.id.uuid.UUID;
import org.lastbamboo.common.http.client.HttpClientManager;


/**
 * Bean containing download data for a file being downloaded over HTTP.
 */
public final class HttpDownloadBean implements DownloadBean
    {
    
    /**
     * The size of the file we're downloading.  This is -1 by default to 
     * indicate we don't know the lengh of the file.  This will typically be
     * set in the HTTP Content-Length response header.
     */
    private long m_size = -1;
    

    /**
     * amount of the file that has already been read and downloaded
     */
    private long m_amountRead = 0;

    /**
     * The file on disk we're downloading.
     */
    private File m_file;

    /**
     * The status of the download.
     */
    private String m_status;

    private int m_statusCode = DownloadBean.CONNECTING;

    private float m_speed;

    private int m_timeRemaining = -1;

    private int m_percentComplete;

    private HttpClientManager m_httpClientManager;
    
    private HostConfiguration m_hostConfiguration;

    private String m_url;

    private final UUID m_uuid;

    /**
     * Creates a new bean with the specified <code>HttpClient</code> providing
     * access to the download data.
     * @param file The file on disk we're downloading to.
     * @param manager The class that provides access to the underlying http
     * connection
     * @param configuration The <code>HostConfiguration</code> of the host
     * we're downloading from.
     */
    public HttpDownloadBean(final File file, 
        final HttpClientManager manager, 
        final HostConfiguration configuration)
        {
        this.m_file = file;
        this.m_httpClientManager = manager;
        this.m_hostConfiguration = configuration;
        
        // This only works because we download to the friends directory by
        // default.  TODO: This should be a setting.
        this.m_url = "friends:"+file.getName();
        this.m_uuid = UUID.randomUUID();
        }

    /* (non-Javadoc)
     * @see com.bamboo.web.beans.DownloadBean#getName()
     */
    public String getName()
        {
        return this.m_file.getName();
        }

    /* (non-Javadoc)
     * @see com.bamboo.web.beans.DownloadBean#getStatus()
     */
    public String getStatus()
        {
        return this.m_status;
        }

    /* (non-Javadoc)
     * @see com.bamboo.web.beans.DownloadBean#getStatusCode()
     */
    public int getStatusCode()
        {
        return this.m_statusCode;
        }

    /* (non-Javadoc)
     * @see com.bamboo.web.beans.DownloadBean#getSize()
     */
    public long getSize()
        {
        return this.m_size;
        }

    /* (non-Javadoc)
     * @see com.bamboo.web.beans.DownloadBean#getAmountRead()
     */
    public long getAmountRead()
        {
        return this.m_amountRead;
        }

    /* (non-Javadoc)
     * @see com.bamboo.web.beans.DownloadBean#getProgress()
     */
    public int getPercentComplete()
        {
        return this.m_percentComplete;
        }

    /* (non-Javadoc)
     * @see com.bamboo.web.beans.DownloadBean#getSpeed()
     */
    public float getSpeed()
        {
        return this.m_speed;
        }

    /* (non-Javadoc)
     * @see com.bamboo.web.beans.DownloadBean#getTimeRemaining()
     */
    public int getTimeRemaining()
        {
        return this.m_timeRemaining;
        }

    /* (non-Javadoc)
     * @see com.bamboo.web.beans.DownloadBean#setStatus(java.lang.String)
     */
    public void setStatus(final String status)
        {
        this.m_status = status;
        }

    /* (non-Javadoc)
     * @see com.bamboo.web.beans.DownloadBean#setStatusCode(int)
     */
    public void setStatusCode(final int status)
        {
        this.m_statusCode = status;
        }

    /* (non-Javadoc)
     * @see com.bamboo.web.beans.DownloadBean#setSize(long)
     */
    public void setSize(final long size)
        {
        this.m_size = size;
        }

    /* (non-Javadoc)
     * @see com.bamboo.web.beans.DownloadBean#setAmountRead(long)
     */
    public void setAmountRead(final long amountRead)
        {
        this.m_amountRead = amountRead;
        }

    /* (non-Javadoc)
     * @see com.bamboo.web.beans.DownloadBean#setSpeed(float)
     */
    public void setSpeed(final float speed)
        {
        this.m_speed = speed;
        }

    /* (non-Javadoc)
     * @see com.bamboo.web.beans.DownloadBean#setTimeRemaining(int)
     */
    public void setTimeRemaining(final int seconds)
        {
        this.m_timeRemaining = seconds;
        }

    /* (non-Javadoc)
     * @see com.bamboo.web.beans.DownloadBean#setPercentComplete(int)
     */
    public void setPercentComplete(int percentComplete)
        {
        this.m_percentComplete = percentComplete;
        }

    /* (non-Javadoc)
     * @see com.bamboo.web.beans.DownloadBean#cancel()
     */
    public void cancel()
        {
        final HttpConnection connection = 
            this.m_httpClientManager.getConnection(
                this.m_hostConfiguration);
        connection.close();
        }

    /* (non-Javadoc)
     * @see com.bamboo.web.beans.DownloadBean#getUrl()
     */
    public String getUrl()
        {
        return this.m_url;
        }

    /* (non-Javadoc)
     * @see com.bamboo.web.beans.DownloadBean#setUrl(java.lang.String)
     */
    public void setUrl(final String url)
        {
        this.m_url = url;
        }

    /* (non-Javadoc)
     * @see com.bamboo.web.beans.DownloadBean#getUuid()
     */
    public UUID getUuid()
        {
        return this.m_uuid;
        }
    }
