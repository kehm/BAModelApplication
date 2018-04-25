package no.priv.kehm.bamodelapplication.util;

import no.priv.kehm.bamodelapplication.network.Network;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class to analyze the different properties of a network
 */
public class NetworkAnalyzer {

    private static NetworkAnalyzer networkAnalyzer;

    /**
     * Private constructor for NetworkAnalyzer singleton
     */
    public NetworkAnalyzer() {
    }

    /**
     * Gets NetworkAnalyzer singleton instance
     *
     * @return NetworkAnalyzer object
     */
    public static synchronized NetworkAnalyzer getInstance() {
        if (networkAnalyzer == null) {
            networkAnalyzer = new NetworkAnalyzer();
        }
        return networkAnalyzer;
    }

    /**
     * Gets degree distribution
     *
     * @param network Network
     * @return Degree distribution list
     */
    public LinkedList getDegreeDistribution(Network network) {
        LinkedList<AtomicInteger> occurrences = new LinkedList<>();
        for (int i = 0; i < network.getNodes().size(); i++) {
            occurrences.add(new AtomicInteger(0));
        }
        for (int i = 0; i < network.getNodes().size(); i++) {
            int degree = network.getNodeDegree(i);
            occurrences.get(degree).incrementAndGet();
        }
        LinkedList<Double> degreeDistribution = new LinkedList<>();
        for (int i = 0; i < occurrences.size(); i++) {
            degreeDistribution.add(i,(double) occurrences.get(i).get() / network.getNodes().size());
        }
        return degreeDistribution;
    }
}
