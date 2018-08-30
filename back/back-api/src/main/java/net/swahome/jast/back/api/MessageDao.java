/*
  File: MessageDao

  Copyright 2018, Steven W. Anderson
*/
package net.swahome.jast.back.api;

import java.util.List;

/**
 * Read all the messages from storage.
 */
public interface MessageDao {
    List<Message> list();
}
