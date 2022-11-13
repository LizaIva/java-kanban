package http_server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class HttpUtils {
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static Gson gson = new Gson();

    public static String findQueryParam(URI uri, String paramName) {
        String query = uri.getQuery();
        String[] splitStrings = query.split("&");
        for (String split : splitStrings) {
            String[] splits = split.split("=");
            if (splits[0].equals(paramName)) {
                return splits[1];
            }
        }
        return null;
    }

    public static void writeResponse(HttpExchange httpExchange, Object object) throws IOException {
        try (OutputStream os = httpExchange.getResponseBody()) {
            httpExchange.sendResponseHeaders(200, 0);
            os.write(gson.toJson(object).getBytes(DEFAULT_CHARSET));
            httpExchange.close();
        }
    }

    public static void writeResponseWithStatus(HttpExchange httpExchange, int status) throws IOException {
        httpExchange.sendResponseHeaders(status, 0);
        httpExchange.close();
    }

}
