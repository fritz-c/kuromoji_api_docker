package com.example.kuromoji_api_docker;

import com.atilika.kuromoji.TokenizerBase;
import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.util.List;
import java.util.Objects;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.JsonProcessingException;

public class App 
{
    private static class PostRequestBody {
      public String body;
      public String mode;
    }

    private static Tokenizer normalTokenizer = new Tokenizer.Builder().mode(TokenizerBase.Mode.NORMAL).build();
    private static Tokenizer searchTokenizer = new Tokenizer.Builder().mode(TokenizerBase.Mode.SEARCH).build();
    private static Tokenizer extendedTokenizer = new Tokenizer.Builder().mode(TokenizerBase.Mode.EXTENDED).build();
    private static ObjectMapper mapper = new ObjectMapper();

    private static Tokenizer getTokenizer(String mode) {
        switch (Objects.toString(mode, "").toLowerCase()) {
            case "extended":
                return extendedTokenizer;
            case "search":
                return searchTokenizer;
            default:
                return normalTokenizer;
        }
    }

    protected static String convert(Tokenizer tokenizer, String body) throws JsonProcessingException
    {
        List<Token> tokens = tokenizer.tokenize(body);

        ArrayNode tokenArrayNode = mapper.createArrayNode();
        for (Token token : tokens) {
            ArrayNode featuresNode = mapper.createArrayNode();
            for (String featureString : token.getAllFeaturesArray()) {
                featuresNode.add(featureString);
            }

            ObjectNode tokenNode = mapper.createObjectNode();
            tokenNode.put("surface", token.getSurface());
            tokenNode.put("position", token.getPosition());
            tokenNode.set("features", featuresNode);

            tokenArrayNode.add(tokenNode);
        }

        ObjectNode result = mapper.createObjectNode();
        result.set("tokens", tokenArrayNode);

        return mapper.writeValueAsString(result);
    }

    public static void main( String[] args )
    {
        String portString = System.getenv("PORT");
        int port;
        try {
            port = Integer.parseInt(portString);
        } catch (NumberFormatException e) {
            port = 9696;
        }

        System.out.println("Starting server at 0.0.0.0:" + port);
        Undertow server = Undertow.builder()
                .addHttpListener(port, "0.0.0.0")
                .setHandler(new HttpHandler() {
                    @Override
                    public void handleRequest(final HttpServerExchange exchange) throws Exception {
                        exchange.getRequestReceiver().receiveFullBytes(
                            (fullByteExchange, data) -> {
                                try {
                                    PostRequestBody requestBody = mapper.readValue(new String(data), PostRequestBody.class);
                                    String outputJson = convert(getTokenizer(requestBody.mode), requestBody.body);
                                    exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
                                    exchange.getResponseSender().send(outputJson);
                                } catch (IOException e) {
                                    exchange.setStatusCode(400);
                                    exchange.getResponseSender().send("Invalid Request");
                                    exchange.endExchange();
                                }
                            }
                        );
                    }
                }).build();
        server.start();
    }
}
