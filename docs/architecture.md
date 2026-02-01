# Architecture

This project is a multithreaded TCP client–server dictionary system.

A server maintains a shared dictionary in memory and persists it to a local file. Multiple clients can connect concurrently and perform dictionary operations through a simple text-based protocol.

## System overview

- **Server**
  - Listens for incoming TCP connections.
  - Creates one handler thread per client.
  - Maintains a shared dictionary.
  - Writes updates back to a file.
  - Provides a GUI for monitoring and exporting data.

- **Client**
  - Connects to the server over TCP.
  - Provides a GUI for dictionary operations.
  - Sends one request and waits for one response.

## Core components

- `Server.java`  
  Loads dictionary data, accepts connections, and tracks active clients.

- `Connection.java`  
  Handles requests from a single client in a dedicated thread.

- `DictionaryLogic.java`  
  Implements all dictionary operations on a shared in-memory map.

- `ServerUI.java`  
  Displays server status and dictionary contents.

- `Client.java` / `ClientUI.java`  
  Manage network communication and user interaction on the client side.

## Concurrency model

- The server uses a thread-per-connection model.
- All client threads share the same dictionary state.

## Persistence

- Dictionary data is loaded from a file at startup.
- After each successful write operation, the dictionary is written back to the file.

## Limitations

- Concurrent writes are not strictly synchronised.
- The protocol uses plain text responses.
- Thread-per-connection may not scale to very large numbers of clients.

These choices were made to keep the system simple and easy to understand.
