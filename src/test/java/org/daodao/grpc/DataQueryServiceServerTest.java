package org.daodao.grpc;

import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataQueryServiceServerTest {
    private Server server;
    private DataQueryServiceGrpc.DataQueryServiceBlockingStub blockingStub;

    @BeforeEach
    public void setUp() throws Exception {
        String serverName = InProcessServerBuilder.generateName();
        server = InProcessServerBuilder.forName(serverName)
                .directExecutor()
                .addService(new DataQueryServiceServer.DataQueryServiceImpl())
                .build()
                .start();

        blockingStub = DataQueryServiceGrpc.newBlockingStub(
                InProcessChannelBuilder.forName(serverName)
                        .directExecutor()
                        .build());
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (server != null) {
            server.shutdownNow().awaitTermination();
        }
    }

    @Test
    public void testQueryData() {
        DataQueryRequest request = DataQueryRequest.newBuilder()
                .setQuery("Test query")
                .build();

        DataQueryResponse response = blockingStub.queryData(request);
        assertEquals("Processed query: Test query", response.getResult());
    }
}