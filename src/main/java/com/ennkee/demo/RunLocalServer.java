package com.ennkee.demo;

import com.hsbc.cranker.connector.CrankerConnector;
import com.hsbc.cranker.connector.CrankerConnectorBuilder;
import com.hsbc.cranker.connector.RouterEventListener;
import com.hsbc.cranker.connector.RouterRegistration;
import io.muserver.ContentTypes;
import io.muserver.Method;
import io.muserver.MuServer;
import io.muserver.MuServerBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.util.List;

public class RunLocalServer {


    public static void main(String[] args) {

//        System.getProperties().put("jdk.internal.httpclient.disableHostnameVerification", "true");

        MuServer server = MuServerBuilder
                .httpServer()
                .withHttpPort(0)
                .addHandler(Method.GET, "/users", (muRequest, muResponse, map) -> {
                    muResponse.status(200);
                    muResponse.contentType(ContentTypes.APPLICATION_JSON);
                    muResponse.write(new JSONArray(List.of(
                            aUser("Jay", 28),
                            aUser("Jack", 22)
                    )).toString());
                })
                .start();

        System.out.println("server started at " + server.uri());

        CrankerConnector connector = CrankerConnectorBuilder.connector()
                .withRouterUris(() -> List.of(URI.create("wss://localhost:12002")))
                .withComponentName("user-services")
                .withRoute("users")
                .withTarget(server.uri())
                .withHttpClient(CrankerConnectorBuilder.createHttpClient(true).build())
                .withRouterRegistrationListener(new RouterEventListener() {
                    @Override
                    public void onSocketConnectionError(RouterRegistration router, Throwable exception) {
                        System.out.println("failed to registered to cranker, error=" + exception.getMessage());
                    }
                })
                .start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            connector.stop();
            server.stop();
        }));
    }

    private static JSONObject aUser(String name, int age) {
        return new JSONObject()
                .put("name", name)
                .put("age", age);
    }
}
