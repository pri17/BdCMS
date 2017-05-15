package com.bidanet.bdcms.velocity;

import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.driver.cache.Cache;
import com.bidanet.bdcms.interceptor.SubmitTokenInterceptor;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

import java.io.IOException;
import java.io.Writer;
import java.util.UUID;

/**
 * Created by Administrator on 2016/7/9.
 */
public class SubmitTokenFunction extends Directive {

    @Override
    public String getName() {
        return "SubmitToken";
    }

    @Override
    public int getType() {
        return LINE;
    }

    @Override
    public boolean render(InternalContextAdapter internalContextAdapter, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        Cache bean = SpringWebTool.getBean(Cache.class);
        String token = UUID.randomUUID().toString();
        String tag = SubmitTokenInterceptor.cacheTag+token;
        bean.set(tag,tag,60*60);
        String res="<input type='hidden' name='"+SubmitTokenInterceptor.inputName+"' value='"+token+"'/>";
        writer.write(res);
        return true;
    }
}
