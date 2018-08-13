/*
  File: ClientMain

  Copyright 2018, Steven W. Anderson
*/
package net.swahome.jast.middle.client;

import net.swahome.jast.back.messages.CreateMessage;
import net.swahome.jast.back.messages.Message;
import net.swahome.jast.middle.api.MessageService;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.List;
import java.util.Properties;

public class ClientMain {
    public static void main(String[] args) {
        try {
            Properties jndiProps = new Properties();
            jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
            jndiProps.put(Context.PROVIDER_URL, "http-remoting://localhost:8081");
            jndiProps.put(Context.SECURITY_PRINCIPAL, "ejbclient");
            jndiProps.put(Context.SECURITY_CREDENTIALS, "letMeInN0w=");
//            jndiProps.put(Context.SECURITY_CREDENTIALS, "bad");
            jndiProps.put("jboss.naming.client.ejb.context", true);
            // create a context passing these properties
            Context ic = new InitialContext(jndiProps);
            try {
                Object proxy = ic.lookup("middle-server/MessageServiceBean!net.swahome.jast.middle.api.MessageService");
                if (proxy == null) {
                    throw new IllegalStateException("Failed to create proxy for MessageService");
                }
                MessageService service = (MessageService) proxy;

                CreateMessage createMessage = new CreateMessage();
                createMessage.setSender("steve");
                createMessage.setReceiver("tom");
                createMessage.setBody("test complete");
                service.add(createMessage);

                List<Message> messages = service.get();
                System.out.println(messages.toString());
            } finally {
                ic.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
