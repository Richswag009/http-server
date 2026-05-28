# HTTP Server From Scratch in Java

An HTTP server built from raw Java sockets — no Spring Boot, no Netty, no Undertow.
Every layer implemented manually: TCP socket handling, HTTP request parsing, response
formatting, and concurrent connections using Java 25 virtual threads.
 
---

## Why I Built This

I wanted to stop treating backend frameworks like magic.

Most developers use Spring Boot without understanding what happens underneath. Building
this project forced me to understand what a framework actually does — before using one.

After building this, when I see `@RestController` in Spring Boot I know exactly what
it's replacing. That's the point.
 
---

## What It Does

- Accepts raw TCP connections on port 4221
- Reads and parses incoming HTTP requests line by line
- Extracts method, path, and version from the request line
- Builds valid HTTP responses with correct status line, headers, and body
- Handles multiple concurrent connections using Java 21 virtual threads
---

## How It Works

```
Browser/curl sends request
        ↓
ServerSocket accepts TCP connection
        ↓
BufferedReader reads raw HTTP text line by line
        ↓
HttpRequest record parses method, path, version
        ↓
Handler builds HttpResponse using Builder pattern
        ↓
Raw HTTP string written back to socket
        ↓
Connection closed
```
 
---

## Key Concepts Implemented

### Content-Length uses bytes, not characters

```java
// wrong — counts Java characters
"Content-Length: " + body.length()
 
// correct — counts bytes as HTTP requires
"Content-Length: " + body.getBytes().length
```

HTTP measures body size in bytes. For ASCII they're the same.
For Unicode characters they're not. Using `getBytes().length` is correct.

### CRLF line endings

HTTP requires `\r\n` to end every header line, not just `\n`.

```java
builder.append("HTTP/1.1 200 OK\r\n")
       .append("Content-Type: application/json\r\n")
       .append("\r\n") // blank line separates headers from body
       .append(body);
```

A missing `\r` breaks HTTP clients silently. Easy to miss, important to understand.

### Virtual threads for concurrency

```java
var executor = Executors.newVirtualThreadPerTaskExecutor();
while (true) {
    Socket socket = serverSocket.accept();
    executor.submit(() -> handleClientRequest(socket));
}
```

Each connection runs on its own virtual thread. The server handles multiple
simultaneous requests without blocking. This is the same model Spring Boot
uses internally with Java 25.
 
---

## Running the Server

```bash
git clone https://github.com/richswag009/http-server-java.git
cd http-server-java
```

Run `Main.java` from your IDE or with Maven.

Server starts on:
```
http://localhost:4221
```
 
---

## Testing

```bash
# basic request
curl -v http://localhost:4221
 
# different paths
curl -v http://localhost:4221/hello
curl -v http://localhost:4221/users/123
```

Expected response:
```http
HTTP/1.1 200 OK
Content-Type: application/json
Content-Length: 28
 
{ "message": "Hello world" }
```
 
---

## Project Structure

```
src/
└── com/richcodes/
    ├── Main.java               ← server entry point, connection handling
    └── models/
        ├── HttpRequest.java    ← record, parses request line
        └── HttpResponse.java   ← record + Builder, formats raw HTTP response
```
 
---

## Intentionally Omitted

This project stops at Phase 8 of a 14-phase learning roadmap. Routing,
middleware, error handling, and tests were excluded intentionally — the goal
was understanding HTTP and socket fundamentals, not building a complete framework.

The next project (HookRelay) applies these fundamentals in Spring Boot.
 
---

## Tech Stack

- Java 25
- Java Virtual Threads (Project Loom)
- Raw Java Sockets (`ServerSocket`, `Socket`)
- No external dependencies
---

Built by [Riches Metelewawon](https://linkedin.com/in/richesmetelewawon)