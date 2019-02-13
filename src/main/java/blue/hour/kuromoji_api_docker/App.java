package blue.hour.kuromoji_api_docker;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.util.List;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.JsonProcessingException;

public class App 
{
    protected static class PostRequestBody {
      public String body;
    }

    protected static String convert(ObjectMapper mapper, Tokenizer tokenizer, String body) throws JsonProcessingException
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
        ObjectMapper mapper = new ObjectMapper();
        Tokenizer tokenizer = new Tokenizer();

        Undertow server = Undertow.builder()
                .addHttpListener(3000, "0.0.0.0")
                .setHandler(new HttpHandler() {
                    @Override
                    public void handleRequest(final HttpServerExchange exchange) throws Exception {
                        exchange.getRequestReceiver().receiveFullBytes(
                            (fullByteExchange, data) -> {
                                try {
                                    PostRequestBody requestBody = mapper.readValue(new String(data), PostRequestBody.class);
                                    String outputJson = convert(mapper, tokenizer, requestBody.body);
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
