# Multithreaded Dictionary System

This is a Java client-server dictionary application.

A server runs in the background and keeps a shared dictionary. Multiple clients can connect to the server at the same time and look up or modify dictionary entries through a simple GUI.

---

## Author

Clara Pan  
Master of Information Technology  
University of Melbourne  

---

## Overview

The project implements a basic networked dictionary using a client–server model.

The server stores all dictionary data and listens for incoming client connections. Clients connect over TCP and send requests such as querying a word, adding a new word, or updating meanings. Changes made by one client are visible to all other connected clients.

The goal of this project is to practise networking, concurrency, and basic system design rather than to build a large-scale production system.

---

## Key Features

- Client–server communication over TCP
- Multiple clients supported at the same time
- Shared dictionary state across all clients
- Dictionary data saved to a local file
- Simple graphical interfaces for client and server

---

## How the system works (high level)

- The server is started first and loads the dictionary from a file.
- Clients connect to the server using a TCP socket.
- Each client sends requests and receives responses one at a time.
- The server handles each client independently.
- Dictionary updates are written back to the file.

This keeps the system easy to follow and debug.

---

## Documentation

More detailed documentation is provided separately:

- System structure and design choices: `docs/architecture.md`
- Client–server message format and commands: `docs/protocol.md`

This README intentionally stays at a high level.

---

## Tech Stack

- Java
- TCP sockets
- Multithreading
- Java Swing
- Text file persistence

---

## Notes

The implementation favours simplicity and clarity. Some design trade-offs and limitations are discussed in the documentation.
