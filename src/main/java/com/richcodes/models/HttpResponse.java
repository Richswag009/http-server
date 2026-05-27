package com.richcodes.models;

import java.lang.module.ModuleDescriptor;
import java.util.HashMap;
import java.util.Map;

public record HttpResponse(
        int statusCode,
        String statusText,
        Map<String, String> headers,
        String body
) {

    public static Builder builder() {
        return new Builder();
    }

    public String toRawString() {

        StringBuilder builder = new StringBuilder();

        builder.append("HTTP/1.1 ")
                .append(statusCode)
                .append(" ")
                .append(statusText)
                .append("\r\n");

        for (Map.Entry<String, String> header : headers.entrySet()) {
            builder.append(header.getKey())
                    .append(": ")
                    .append(header.getValue())
                    .append("\r\n");
        }
        builder.append("\r\n");

        if (body != null) {
            builder.append(body);
        }

        return builder.toString();
    }

    public static class Builder {
        private int statusCode;
        private String statusText;
        private Map<String, String> headers = new HashMap<>();
        private String body;

        public Builder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder statusText(String statusText) {
            this.statusText = statusText;
            return this;
        }

        public Builder headers(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(statusCode, statusText, headers, body);
        }
    }
}
