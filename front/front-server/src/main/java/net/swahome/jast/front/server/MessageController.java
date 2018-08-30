/*
  File: MessageService

  Copyright 2018, Steven W. Anderson
*/
package net.swahome.jast.front.server;

import net.swahome.jast.back.api.CreateMessage;
import net.swahome.jast.back.api.Message;
import net.swahome.jast.middle.api.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

/**
 * The MessageController is a JAX-RS EJB for saving and listing messages.
 *
 * It does remote EJB calls to MessageService.
 */
@Stateless
@Path("/messages")
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    /**
     * Get the list of saved messages.
     *
     * @return a List of Messages
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Message> get() {

        try {
            MessageService service = getMessageService();
            List<Message> messages = service.get();

            Optional<Message> userTestException = messages.stream().filter(x -> x.getReceiver().equals("front-get-exception")).findFirst();
            userTestException.ifPresent(m -> {
                throw new WebApplicationException("User test failure: " + m.getBody());
            });

            logger.debug("returning list of {} api", messages.size());

            return messages;
        } catch (Exception e) {
            logger.error("Failed to get api", e);

            if (e instanceof WebApplicationException) {
                throw (WebApplicationException)e;
            } else {
                throw new WebApplicationException("Failed to get message", e);
            }
        }
    }

    /**
     * Save a message.
     *
     * @param message The data for creating a message.
     */
    @PUT
    public void add(CreateMessage message) {
        try {
            if (message.getReceiver().equals("front-add-exception")) {
                throw new WebApplicationException("User test failure: " + message.getBody());
            }

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
        Object proxy = ic.lookup("ejb:/middle-server/MessageServiceBean!" + MessageService.class.getName());
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
