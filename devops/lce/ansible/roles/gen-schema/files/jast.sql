\connect postgres

CREATE TABLE IF NOT EXISTS Message (
  messageId BIGSERIAL NOT NULL PRIMARY KEY,
  sender VARCHAR(256) NOT NULL,
  receiver VARCHAR(256)  NOT NULL,
  body TEXT NOT NULL
);
