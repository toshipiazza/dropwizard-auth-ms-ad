package com.commercehub.dropwizard.authentication

import org.junit.Test

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
/**
 * Created by ghogan on 9/6/14.
 */
class AdConfigurationTest {

    @Test
    void checkTimeouts(){
        def cfg = new AdConfiguration()
        assertEquals("At construction the connection timeout should be set to the default", AdConfiguration.DEFAULT_CONN_TIMEOUT, cfg.getConnectionTimeout())
        assertEquals("At construction the connection timeout should be set to the default", AdConfiguration.DEFAULT_READ_TIMEOUT, cfg.getReadTimeout())

        cfg.setConnectionTimeout(AdConfiguration.DEFAULT_CONN_TIMEOUT * 2)
        cfg.setReadTimeout(AdConfiguration.DEFAULT_CONN_TIMEOUT * 3)

        assertEquals("Setting the connection timeout should change the returned value", AdConfiguration.DEFAULT_CONN_TIMEOUT*2, cfg.getConnectionTimeout())
        assertEquals("Setting the read timeout should change the returned value", AdConfiguration.DEFAULT_READ_TIMEOUT*3, cfg.getReadTimeout())

    }
}
