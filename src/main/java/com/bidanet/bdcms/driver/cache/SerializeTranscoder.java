package com.bidanet.bdcms.driver.cache;

import org.apache.commons.fileupload.util.Closeable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * . <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c)
 * <p>
 * Company: 苏州必答网络科技有限公司
 * <p>
 *
 * @author 季照群
 * @version 1.0.0
 */
public abstract class SerializeTranscoder {

    protected static Logger logger =  LoggerFactory.getLogger(SerializeTranscoder.class);

    public abstract byte[] serialize(Object value);

    public abstract Object deserialize(byte[] in);

    public void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                logger.info("Unable to close " + closeable, e);
            }
        }
    }
}
