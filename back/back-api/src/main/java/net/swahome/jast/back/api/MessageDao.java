/*
  File: MessageDao

  Copyright 2018, Steven W. Anderson
*/
package net.swahome.jast.back.api;

import java.util.List;

public interface MessageDao {
    List<Message> list();
}
