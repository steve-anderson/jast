/*
  File: MessageService

  Copyright 2018, Steven W. Anderson
*/
package net.swahome.jast.middle.api;

import net.swahome.jast.back.api.CreateMessage;
import net.swahome.jast.back.api.Message;

import java.util.List;

public interface MessageService {
    List<Message> get();

    void add(CreateMessage message);
}
