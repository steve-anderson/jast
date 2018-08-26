/*
  File: Message

  Copyright 2018, Steven W. Anderson
*/
package net.swahome.jast.back.api;


import java.io.Serializable;

public class CreateMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sender;
    private String receiver;
    private String body;

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
        return "CreateMessage{" +
                "sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
