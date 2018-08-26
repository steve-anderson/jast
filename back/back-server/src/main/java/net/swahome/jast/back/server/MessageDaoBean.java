/*
  File: MessageDaoBean

  Copyright 2018, Steven W. Anderson
*/
package net.swahome.jast.back.server;

import net.swahome.jast.back.api.CreateMessage;
import net.swahome.jast.back.api.Message;
import net.swahome.jast.back.api.MessageDao;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.WebApplicationException;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Stateless
@Remote(MessageDao.class)
@LocalBean
public class MessageDaoBean implements MessageDao {
    @PersistenceContext(unitName = "default")
    private EntityManager em;

    public void create(CreateMessage createMessage) {
        if (createMessage.getReceiver().equals("back-add-exception")) {
            throw new WebApplicationException("User test failure: " + createMessage.getBody());
        }

        EMessage save = new EMessage(createMessage);
        em.persist(save);
    }

    @Override
    public List<Message> list() {
        Query query = em.createQuery("select m from EMessage m");
        List<Message> result = cast(query.getResultList()).stream().map(EMessage::toMessage).collect(toList());

        Optional<Message> userTestException = result.stream().filter(x -> x.getReceiver().equals("back-get-exception")).findFirst();
        userTestException.ifPresent(m -> {
            throw new WebApplicationException("User test failure: " + m.getBody());
        });

        return result;
    }

    private static final List<EMessage> cast(List list) {
        return (List<EMessage>)list;
    }
}
