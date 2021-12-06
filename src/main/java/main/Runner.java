package main;

import com.hazelcast.config.Config;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import verticles.ServerVerticle;
import verticles.ServiceVerticle;

public class Runner {
    public static void main(String[] args) {
        Config hazelcastConfig = new Config();
        hazelcastConfig.getNetworkConfig().getJoin().getTcpIpConfig().addMember("127.0.0.1").setEnabled(true);
        hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);
        VertxOptions options = new VertxOptions().setClusterManager(mgr);

        Vertx.clusteredVertx(options, res -> {
            System.out.println("Starting vertx distributed node");
            if (res.succeeded()) {
                Vertx vertx = res.result();
                ConfigStoreOptions sys = new ConfigStoreOptions()
                        .setType("sys")
                        .setConfig(new JsonObject().put("cache", false));
                ConfigRetrieverOptions configOptions = new ConfigRetrieverOptions()
                        .addStore(sys);
                ConfigRetriever retriever = ConfigRetriever.create(vertx, configOptions);

                vertx.deployVerticle(new ServerVerticle(retriever));
                vertx.deployVerticle(new ServiceVerticle());
            }
        });
    }
}
