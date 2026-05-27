# HTTP Server From Scratch in Java

A learning-focused HTTP server built completely from raw Java sockets without using frameworks like Spring Boot, Netty, or Undertow.

I built this project to deeply understand:
- how HTTP works internally
- how TCP socket communication happens
- how servers accept and manage connections
- how request parsing and routing work
- how frameworks abstract these concepts away

Instead of relying on high-level frameworks, this project manually implements the core parts of an HTTP server using Java networking primitives.

---

# Features

- Raw TCP socket handling using `ServerSocket`
- HTTP request parsing
- HTTP response builder
- Concurrent request handling using Java Virtual Threads
- Request headers parsing
- JSON response support
- Proper HTTP response formatting
- Builder pattern for response creation
- Manual routing foundation

---

# Tech Stack

- Java 21+
- Java Virtual Threads
- Java Sockets
- Builder Pattern
- Raw HTTP protocol handling

---

# Why I Built This

I wanted to stop treating backend frameworks like magic.

Building this project helped me understand:
- what actually happens when a browser sends a request
- how HTTP requests are structured
- why headers like `Content-Length` matter
- how servers handle multiple clients simultaneously
- how frameworks like Spring Boot work internally

This project gave me a much deeper understanding of backend engineering fundamentals.

---

# Project Structure

```text
src/
├── com/
│   └── richcodes/
│       ├── Main.java
│       └── models/
│           ├── HttpRequest.java
│           └── HttpResponse.java
```

---

# How It Works

The server:
1. Opens a TCP socket on a port
2. Waits for incoming client connections
3. Reads raw HTTP requests
4. Parses the request line and headers
5. Builds a valid HTTP response
6. Sends the response back to the client
7. Closes the connection

---

# Example HTTP Request

```http
GET / HTTP/1.1
Host: localhost:4221
User-Agent: curl/8.7.1
Accept: */*
```

---

# Example HTTP Response

```http
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 28

{ "message": "Hello world" }
```

---

# Running The Project

## Clone the repository

```bash
git clone https://github.com/your-username/http-server-java.git
cd http-server-java
```

---

## Compile the project

```bash
javac -d out src/com/richcodes/**/*.java
```

---

## Run the server

```bash
java -cp out com.richcodes.Main
```

The server runs on:

```text
http://localhost:4221
```

---

# Testing The Server

Using curl:

```bash
curl -v http://localhost:4221
```

Expected response:

```json
{ "message": "Hello world" }
```

---

# Example Response Builder Usage

```java
HttpResponse response = HttpResponse.builder()
        .statusCode(200)
        .statusText("OK")
        .header("Content-Type", "application/json")
        .header("Content-Length",
                String.valueOf(body.getBytes().length))
        .body(body)
        .build();
```

---

# What I Learned

## Networking Fundamentals
- TCP/IP basics
- Socket communication
- Ports and connections
- Difference between `ServerSocket` and `Socket`

## HTTP Internals
- HTTP request/response structure
- Headers and body separation
- Status codes
- Content-Type and Content-Length

## Java Networking
- `ServerSocket`
- `Socket`
- `BufferedReader`
- `OutputStream`
- Reading raw requests from sockets

## Backend Architecture
- Request parsing
- Response formatting
- Builder pattern
- Concurrent connection handling

## Concurrency
- Virtual Threads
- Handling multiple simultaneous requests
- Thread-per-request model

---

# Important Concepts Implemented

## Content-Length

The server calculates response body size correctly using:

```java
body.getBytes().length
```

instead of:

```java
body.length()
```

because HTTP counts bytes, not Java characters.

---

## CRLF (`\r\n`)

HTTP requires lines to end with:

```text
\r\n
```

instead of just:

```text
\n
```

This project manually formats valid HTTP responses using the correct protocol formatting.

---

# Future Improvements

Things I plan to add next:

- Router system
- Middleware support
- Dynamic routes
- JSON parsing with Jackson
- Exception handling
- Static file serving
- Keep-Alive support
- Unit and integration tests

---

# Learning Goal

The purpose of this project is not to replace frameworks.

The goal is to understand:
- what backend frameworks do internally
- how requests move through a server
- how HTTP communication actually works

After building this, frameworks like Spring Boot make a lot more sense because I now understand the underlying mechanics they abstract away.

---

# Author

Built by Richcodes as a backend engineering learning project focused on:
- networking
- HTTP internals
- Java concurrency
- server architecture
- low-level backend systems