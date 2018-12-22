/*
  File: GetMessagesResponse

  Copyright 2018, Steven W. Anderson
*/
package net.swahome.jast.front.api;

import net.swahome.jast.back.api.Message;

import java.util.List;

public class GetMessagesResponse {
    private final String frontHostname;
    private final List<Message> messages;

    public GetMessagesResponse(String frontHostname, List<Message> messages) {
        this.frontHostname = frontHostname;
        this.messages = messages;
    }

    public String getFrontHostname() {
        return frontHostname;
    }

    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public String toString() {
        return "GetMessagesResponse{" +
                "frontHostname='" + frontHostname + '\'' +
                ", messages=" + messages +
                '}';
    }
}
