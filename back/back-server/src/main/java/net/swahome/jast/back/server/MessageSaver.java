/*
  File: MessageSaver

  Copyright 2018, Steven W. Anderson
*/
package net.swahome.jast.back.server;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.swahome.jast.back.api.CreateMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.IOException;

/**
 * Read CreateMessage JSON from queue, createMessageQueue, and call MessageDaoBean to save it.
 */
@MessageDriven(
    activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "createMessageQueue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
    }
)
public class MessageSaver implements MessageListener {
    private static final Logger logger = LoggerFactory.getLogger(MessageSaver.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @EJB
    private MessageDaoBean dao;

    @Override
    public void onMessage(Message jeeMessage) {
        try {
            if (jeeMessage instanceof TextMessage) {
                TextMessage message = (TextMessage) jeeMessage;
                String json = message.getText();

                logger.debug("Processing message: {}", json);
                CreateMessage createMessage = mapper.readValue(json, CreateMessage.class);
                logger.info("Create message: {}", createMessage);
                dao.create(createMessage);
            }
        } catch (JMSException e) {
            throw new IllegalStateException("Failed to get JMS message.", e);
        } catch (JsonParseException e) {
            throw new IllegalStateException("Failed to parse the JMS message.", e);
        } catch (JsonMappingException e) {
            throw new IllegalStateException("CreateMessage JSON mapping failure.", e);
        } catch (IOException e) {
            throw new IllegalStateException("Failed message processing.", e);
        }
    }
}
