package com.bidanet.bdcms.velocity;

import com.bidanet.bdcms.common.SpringWebTool;
import com.bidanet.bdcms.service.ConfigService;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.Writer;

/**
 * 读取Config信息
 */
public class ConfigFunction extends Directive {
    @Override
    public String getName() {
        return "C";
    }

    @Override
    public int getType() {
        return LINE;
    }

    @Override
    public boolean render(InternalContextAdapter internalContextAdapter, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        int i = node.jjtGetNumChildren();
        if (i>0) {
            String configName = (String) node.jjtGetChild(0).value(internalContextAdapter);
            WebApplicationContext springContext = ContextLoader.getCurrentWebApplicationContext();
            ConfigService configService =  springContext.getBean(ConfigService.class);
            String config = configService.getConfig(configName);
            writer.write(config);

        }
        return true;
    }
}
