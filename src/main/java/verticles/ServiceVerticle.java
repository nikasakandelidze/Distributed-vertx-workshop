package verticles;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import shared.Utils;

import java.util.Map;

public class ServiceVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise){
        System.out.println("deploying ServiceVerticle");
        vertx.eventBus().<JsonObject>consumer(Utils.SERVICE_EVENTBUS_ADDR, req -> {
            JsonObject body = req.body();
            String data = body.getString("data");
            System.out.println("Data recieved is: "+ data);
            System.out.println("Got eventbus request on ServiceVerticle on address: "+Utils.SERVICE_EVENTBUS_ADDR);
            req.reply("Hello world");
        });
        startPromise.complete();
    }
}
