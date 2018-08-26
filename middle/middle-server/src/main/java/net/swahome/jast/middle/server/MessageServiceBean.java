/*
  File: MessageServiceBean

  Copyright 2018, Steven W. Anderson
*/
package net.swahome.jast.middle.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.swahome.jast.back.api.CreateMessage;
import net.swahome.jast.back.api.Message;
import net.swahome.jast.back.api.MessageDao;
import net.swahome.jast.middle.api.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.WebApplicationException;
import java.lang.IllegalStateException;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

@Stateless
@Remote(MessageService.class)
public class MessageServiceBean implements MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageServiceBean.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Resource(lookup = "java:/jms/remoteCF")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "java:/jms/queues/createMessageQueue")
    private Queue queue;

    public List<Message> get() {
        try {
            MessageDao dao = getMessageDao();
            List<Message> result = dao.list();

            Optional<Message> userTestException = result.stream().filter(x -> x.getReceiver().equals("middle-get-exception")).findFirst();
            userTestException.ifPresent(m -> {
                throw new WebApplicationException("User test failure: " + m.getBody());
            });

            logger.debug("returning list of {} api", result.size());
            return result;
        } catch (NamingException e) {
            throw new InternalServerErrorException("Failed to get messages", e);
        }
    }

    public void add(CreateMessage message) {
        validate(message);

        try {
            if (message.getReceiver().equals("middle-add-exception")) {
                throw new WebApplicationException("User test failure: " + message.getBody());
            }

            sendMessage(message);
            logger.debug("sent message: {}", message);
        } catch (JMSException | JsonProcessingException e) {
            throw new InternalServerErrorException("Failed to send message", e);
        }
    }

    private void sendMessage(CreateMessage message) throws JMSException, JsonProcessingException {
        try (Connection connection = connectionFactory.createConnection()) {
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(queue);
            String json = mapper.writeValueAsString(message);
            logger.debug("send create message to queue: {}", json);
            TextMessage textMessage = session.createTextMessage(json);
            producer.send(textMessage);
        }
    }

    private void validate(CreateMessage message) {
        ensureValue("sender", message.getSender());
        ensureValue("receiver", message.getReceiver());
        ensureValue("body", message.getBody());
    }

    private static void ensureValue(String name, String value) {
        if (isNullOrBlank(value)) {
            throw new IllegalArgumentException("The " + name + " requires a value.");
        }
    }

    private static boolean isNullOrBlank(String x) {
        return x == null || x.trim().isEmpty();
    }

    private static MessageDao getMessageDao() throws NamingException {
        InitialContext ic = getContext();
        Object proxy = ic.lookup("ejb:/back-server/MessageDaoBean!" + MessageDao.class.getName());
        if (proxy == null) {
            throw new IllegalStateException("Failed to create proxy for MessageDao");
        }
        return (MessageDao)proxy;
    }

    private static InitialContext getContext() throws NamingException {
        Hashtable<String, String> props = new Hashtable<>();
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        return new InitialContext(props);
    }
}
