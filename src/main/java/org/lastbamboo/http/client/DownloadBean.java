package org.lastbamboo.http.client;

import org.apache.commons.id.uuid.UUID;


/**
 * Interface providing access to data about a downloading file.
 */
public interface DownloadBean 
    {
    /**
     * Code for connecting.  These values SHOULD NOT BE CHANGED because they
     * correspond to equivalent values in the javascript download code.
     */
    public static int CONNECTING = 0;
    
    /**
     * Code for downloading.  These values SHOULD NOT BE CHANGED because they
     * correspond to equivalent values in the javascript download code.
     */
    public static int DOWNLOADING = 1;
    
    /**
     * Code for completed downloads.  These values SHOULD NOT BE CHANGED 
     * because they correspond to equivalent values in the javascript download 
     * code.
     */
    public static int COMPLETED = 2;
    
    /**
     * Code for when we can't connect.  These values SHOULD NOT BE CHANGED 
     * because they correspond to equivalent values in the javascript download 
     * code.
     */
    public static int COULD_NOT_CONNECT = 3;

    /**
     * Code for when the connection for the download was interrupted.  This
     * typcially means the TCP or UDP stream failed/disconnected.
     */
    public static int INTERRUPTED = 4;

    /**
     * Accessor for the name of the file.
     * 
     * @return The name of the file.
     */
    String getName();
    
    /**
     * Accessor for the "status" of the download, such as "downloading", 
     * "stopped", etc.
     * 
     * @return The status of the downloading file.
     */
    String getStatus();

    /**
     * Accessor for the status code
     * @return the status code of the downloading file
     */
    int getStatusCode();

    /**
     * Accessor for the full size of the downloading file.
     * 
     * @return The size of the downloading file.
     */
    long getSize();

    /**
     * Accessor for the amount read 
     * 
     * @return The amount read of the downloading file.
     */
    long getAmountRead();

    /**
     * Accessor for percentage of the file that has completed downloading.
     * @return The percentage complete, between 0 and 100, inclusive.
     */
    int getPercentComplete();
    
    /**
     * Accessor for the speed of the downloading file, in KB/s.
     * 
     * @return The speed of the downloading file, in KB/s.
     */
    float getSpeed();
    
    /**
     * Accessor for the time remaining for the download, in seconds.
     * 
     * @return The time remaining for the download, in seconds.
     */
    int getTimeRemaining();
    
    /**
     * Accessor for the url to use for launching the file.
     * @return The url to use for launching the file.
     */
    String getUrl();
    
    /**
     * Accessor for the <code>UUID</code> for this download.
     * @return The <code>UUID</code> for this download.
     */
    UUID getUuid();
    
    /**
     * Sets the status of the download.
     * 
     * @param status The download status.
     */
    void setStatus(final String status);

    /**
     * Sets the status code of the download.
     * 
     * @param status code The download status.
     */
    void setStatusCode(final int status);

    /**
     * Sets the full size of the downloading content in bytes.
     * @param contentLength The size of the content in bytes.
     */
    void setSize(long contentLength);

    /**
     * Sets the amount read of the downloading file
     * @param amountRead the total amountRead for this downloading file 
     */
    void setAmountRead(long amountRead);

    /**
     * Sets the download speed.
     * 
     * @param speed The speed in KB/S.
     */
    void setSpeed(final float speed);

    /**
     * Sets the estimated time remaining for the download, in seconds.
     * @param seconds The estimated number of seconds left.
     */
    void setTimeRemaining(int seconds);

    /**
     * Sets the percentage of the file that has completed downloading.
     * @param percentComplete The percentage of the file that has completed
     * downloading.
     */
    void setPercentComplete(int percentComplete);
    
    /**
     * Sets the url to use for launching the file.
     * @param url The url to use for launching the file.
     */
    void setUrl(final String url);

    /**
     * Cancels the download.
     */
    void cancel();
    }






