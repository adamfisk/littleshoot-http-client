package org.lastbamboo.common.http.client;

import org.apache.commons.httpclient.HttpException;
import org.lastbamboo.common.http.client.RuntimeHttpException;
import org.lastbamboo.common.util.RuntimeExceptionPair;
import org.lastbamboo.common.util.RuntimeExceptionWrapperTest;

/**
 * A test for the RuntimeHttpException wrapper.
 */
public final class RuntimeHttpExceptionTest
        extends RuntimeExceptionWrapperTest
    {
    protected RuntimeExceptionPair pair ()
        {
        final HttpException checked = new HttpException ();
        final RuntimeException runtime = new RuntimeHttpException (checked);
        
        return (new RuntimeExceptionPair (checked, runtime));
        }
    }
