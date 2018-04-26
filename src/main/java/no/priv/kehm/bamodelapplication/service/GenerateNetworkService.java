package no.priv.kehm.bamodelapplication.service;


import javafx.concurrent.Service;
import javafx.concurrent.Task;
import no.priv.kehm.bamodelapplication.network.Network;
import no.priv.kehm.bamodelapplication.util.NetworkGenerator;

/**
 * Service class to generate a network
 */
public class GenerateNetworkService extends Service {

    private static final int M = 4;
    private static final int N = 10000 - M;

    @Override
    protected Task createTask() {
        return new Task<Network>() {
            @Override
            protected Network call() throws Exception {
                return NetworkGenerator.getInstance().generateNetwork(N, M);
            }
        };
    }
}
