package com.bidanet.bdcms.controller.machine;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by xuejike on 2017/2/23.
 */
@Controller()
@RequestMapping("/mq")
public class PTestController {
    @RequestMapping("/index")
    public void index( HttpRequest request,
                          HttpResponse response,
                          HttpContext context)  {
        try{
            if (request instanceof HttpEntityEnclosingRequest) {
                HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
                InputStream is = entity.getContent();


                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String content = buffer.toString();

                System.out.println("Simplified Notification: \n" + content);

                response.setStatusCode(200);
            }
        }catch (Exception e){

        }

    }
}
