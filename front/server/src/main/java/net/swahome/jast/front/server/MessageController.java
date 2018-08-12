/*
  File: MessageService

  Copyright 2018, Steven W. Anderson
*/
package net.swahome.jast.front.server;

import net.swahome.jast.back.messages.CreateMessage;
import net.swahome.jast.back.messages.Message;
import net.swahome.jast.middle.api.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Singleton;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Hashtable;
import java.util.List;

@Singleton
@Path("/messages")
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Message> get() {

        try {
            MessageService service = getMessageService();
            List<Message> messages = service.get();

            logger.debug("returning list of {} messages", messages.size());

            return messages;
        } catch (Exception e) {
            logger.error("Failed to get messages", e);

            if (e instanceof WebApplicationException) {
                throw (WebApplicationException)e;
            } else {
                throw new WebApplicationException("Failed to create message", e);
            }
        }
    }

    @PUT
    public void add(CreateMessage message) {
        try {
            MessageService service = getMessageService();
            service.add(message);
            logger.debug("created message: {}", message);
        } catch (Exception e) {
            logger.error("Failed to create message: {}", message, e);

            if (e instanceof WebApplicationException) {
                throw (WebApplicationException)e;
            } else {
                throw new WebApplicationException("Failed to create message", e);
            }
        }
    }

    private static MessageService getMessageService() throws NamingException {
        InitialContext ic = getContext();
        Object proxy = ic.lookup("ejb:/middle-server/MessageServiceBean!net.swahome.jast.middle.api.MessageService");
        if (proxy == null) {
            throw new IllegalStateException("Failed to create proxy for MessageService");
        }
        return (MessageService)proxy;
    }

    private static InitialContext getContext() throws NamingException {
        Hashtable<String, String> props = new Hashtable<>();
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        return new InitialContext(props);
    }
}
