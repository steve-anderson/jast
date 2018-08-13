/*
  File: MessageServiceBean

  Copyright 2018, Steven W. Anderson
*/
package net.swahome.jast.middle.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.swahome.jast.back.messages.CreateMessage;
import net.swahome.jast.back.messages.Message;
import net.swahome.jast.middle.api.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.*;
import javax.ws.rs.InternalServerErrorException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.stream.Collectors.toList;

@Stateless
//@Remote(MessageService.class)
public class MessageServiceBean implements MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageServiceBean.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    @Resource(lookup = "java:/jms/remoteCF")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "java:/jms/queues/createMessageQueue")
    private Queue queue;

    // I know a singleton is thread-safe but I still use the safe ones.
    private static final AtomicLong idSeq = new AtomicLong();
    private static final Map<Long, Message> messageStore = new ConcurrentHashMap<>();

    public List<Message> get() {
        logger.debug("returning list of {} messages", messageStore.size());
        return messageStore.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).map(Map.Entry::getValue).collect(toList());
    }

    public void add(CreateMessage message) {
        validate(message);

        Message result = new Message();
        long id = idSeq.incrementAndGet();
        result.setId(String.valueOf(id));
        result.setSender(message.getSender());
        result.setReceiver(message.getReceiver());
        result.setBody(message.getBody());

        messageStore.put(id, result);
        try {
            sendMessage(message);

            logger.debug("created message: {}", result);
        } catch (JMSException | JsonProcessingException e) {
            throw new InternalServerErrorException("Faile to send message", e);
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
}
