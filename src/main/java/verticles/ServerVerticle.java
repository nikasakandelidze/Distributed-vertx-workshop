package verticles;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import shared.Utils;

public class ServerVerticle extends AbstractVerticle {
    private final ConfigRetriever configRetriever;
    public ServerVerticle(ConfigRetriever retriever){
        this.configRetriever =retriever;
    }
    @Override
    public void start(Promise<Void> startPromise) {
        configRetriever.getConfig().onSuccess(res -> {
            int port = res.getInteger("port");
            vertx.createHttpServer()
                    .requestHandler(request -> {
                        System.out.println("received HTTP request");
                        vertx.eventBus().publish(Utils.SERVICE_EVENTBUS_ADDR, new JsonObject().put("data", request.params().get("data")));
                        request.response().end("Thanks for request!");
                    }).listen(port, ar -> {
                        if (ar.succeeded()) {
                            System.out.printf("Started http server on port: %s%n", port);
                        } else {
                            System.out.printf("Failed to start http server on port: %s%n", port);
                        }
                    });
            startPromise.complete();
        });
    }
}
