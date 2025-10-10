package net.daodao.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class DataQueryServiceServer {
    private Server server;

    public void start() throws IOException {
        int port = 50052;
        server = ServerBuilder.forPort(port)
                .addService(new DataQueryServiceImpl())
                .build()
                .start();
        System.out.println("Server started, listening on " + port);
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    static class DataQueryServiceImpl extends DataQueryServiceGrpc.DataQueryServiceImplBase {
        @Override
        public void queryData(DataQueryRequest request, StreamObserver<DataQueryResponse> responseObserver) {
            String result = "Processed query: " + request.getQuery();
            DataQueryResponse response = DataQueryResponse.newBuilder()
                    .setResult(result)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        DataQueryServiceServer server = new DataQueryServiceServer();
        server.start();
        server.blockUntilShutdown();
    }
}