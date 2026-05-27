package com.richcodes.models;

import java.util.Arrays;
import java.util.Map;

public record HttpRequest(
        String method,
        String path,
        String version,
        Map<String, String> headers,
        String body
) {}
