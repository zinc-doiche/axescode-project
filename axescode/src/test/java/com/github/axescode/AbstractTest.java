package com.github.axescode;

import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

abstract public class AbstractTest {
    private static final Logger log = LoggerFactory.getLogger(AbstractTest.class);

    protected void info( Object obj) {
        log.info(obj::toString);
    }
}
