/*
  File: MessageService

  Copyright 2018, Steven W. Anderson
*/
package net.swahome.jast.middle.api;

import net.swahome.jast.back.messages.CreateMessage;
import net.swahome.jast.back.messages.Message;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface MessageService {
    List<Message> get();

    void add(CreateMessage message);
}
