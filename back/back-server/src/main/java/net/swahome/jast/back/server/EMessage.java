/*
  File: Message

  Copyright 2018, Steven W. Anderson
*/
package net.swahome.jast.back.server;

import net.swahome.jast.back.api.CreateMessage;
import net.swahome.jast.back.api.Message;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Message")
public class EMessage implements Serializable {
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "messageId")
    private Long id;

    private String sender;
    private String receiver;
    private String body;

    public EMessage() {
    }

    public EMessage(CreateMessage create) {
        sender = create.getSender();
        receiver = create.getReceiver();
        body = create.getBody();
    }

    public Message toMessage() {
        Message result = new Message();
        result.setId(id.toString());
        result.setSender(sender);
        result.setReceiver(receiver);
        result.setBody(body);
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
