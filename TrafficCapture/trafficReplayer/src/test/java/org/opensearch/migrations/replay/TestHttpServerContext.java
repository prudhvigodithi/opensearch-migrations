package org.opensearch.migrations.replay;

import org.opensearch.migrations.testutils.HttpFirstLine;
import org.opensearch.migrations.testutils.SimpleHttpResponse;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

public class TestHttpServerContext {

    static String getUriForIthRequest(int i) {
        return String.format("/%04d", i);
    }

    static String getRequestString(int i) {
        return String.format("GET %s HTTP/1.1\r\n" +
                        "Connection: Keep-Alive\r\n" +
                        "Host: localhost\r\n" +
                        "User-Agent: UnitTest\r\n" +
                        "Connection: Keep-Alive\r\n" +
                        "\r\n",
                getUriForIthRequest(i));
    }

    public static String SERVER_RESPONSE_BODY_PREFIX = "Boring Response to ";
    public static Duration SERVER_RESPONSE_LATENCY = Duration.ofMillis(100);

    public static SimpleHttpResponse makeResponse(HttpFirstLine r) {
        try {
            Thread.sleep(SERVER_RESPONSE_LATENCY.toMillis());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        var headers = Map.of(
                "Content-Type", "text/plain",
                "Funtime", "checkIt!",
                "Content-Transfer-Encoding", "chunked");
        String body = SERVER_RESPONSE_BODY_PREFIX + r.path();
        var payloadBytes = body.getBytes(StandardCharsets.UTF_8);
        return new SimpleHttpResponse(headers, payloadBytes, "OK", 200);
    }

}
