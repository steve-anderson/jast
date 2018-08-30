/*
  File: Message

  Copyright 2018, Steven W. Anderson
*/
package net.swahome.jast.back.api;


import java.io.Serializable;

/**
 * A message sent to someone.
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String sender;
    private String receiver;
    private String body;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
                "id='" + id + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
