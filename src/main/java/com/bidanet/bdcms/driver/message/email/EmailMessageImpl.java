package com.bidanet.bdcms.driver.message.email;

import com.bidanet.bdcms.driver.message.Message;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * Created by Administrator on 2016/7/28.
 */
public class EmailMessageImpl implements Message {

    @Override
    public boolean send(String msg, String... to) {
        Email email = new SimpleEmail();
        email.setHostName("smtp.googlemail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("username", "password"));
        email.setSSLOnConnect(true);
        try {
            email.setFrom("user@gmail.com");
            email.setSubject("TestMail");
            email.setMsg(msg);
            email.addTo("foo@bar.com");
            email.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }

        return false;
    }
}
