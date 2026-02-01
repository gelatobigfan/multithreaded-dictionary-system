# Protocol

The client and server communicate over TCP using a simple, line-based text protocol.

Each request is sent as a single line, and each request receives exactly one response line. The protocol is synchronous: the client sends a request and waits for the response before sending the next one.

## Transport and encoding

- Transport: TCP
- Encoding: UTF-8
- Message framing: one request per line, one response per line
- Field delimiter: `#`

## Message format

All requests follow the same basic format:

COMMAND#param1#param2#...

The first field specifies the operation. Remaining fields are command-specific parameters.

Responses are returned as a single line of human-readable text describing the result of the operation.

## Commands

### QUERY


Returns the meaning or meanings of the given word.

- If the word exists, the server returns the stored meaning string.
- If the word does not exist, the server returns an error message.

---

### ADD


Adds a new word with an initial meaning.

- The operation succeeds only if the word does not already exist.
- If the word already exists, the request is rejected.

---

### DELETE


Deletes a word from the dictionary.

- If the word exists, it is removed.
- If the word does not exist, an error message is returned.

---

### ADD_MEANING


Adds an additional meaning to an existing word.

- The new meaning is appended to the existing meaning list.
- Meanings are stored as a single string separated by semicolons (`;`).
- If the word does not exist, the request is rejected.

---

### UPDATE


Updates a specific meaning of an existing word.

- The server searches for `oldMeaning` and replaces it with `newMeaning`.
- If the word does not exist or the old meaning cannot be found, the request is rejected.

## Response behaviour

- Each request produces exactly one response.
- Responses are plain text messages intended for display in the client UI.
- The protocol does not use structured status codes or machine-readable error formats.

## Constraints and limitations

- Parameters must not contain newline characters.
- The `#` character is reserved as a field delimiter.
- The semicolon (`;`) is used internally to separate multiple meanings.
- Including delimiter characters inside parameters may lead to parsing ambiguity.
