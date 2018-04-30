package no.priv.kehm.bamodelapplication.util;

import no.priv.kehm.bamodelapplication.network.Network;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class to analyze the different properties of a network
 */
public class NetworkAnalyzer {

    private static NetworkAnalyzer networkAnalyzer;
    private ArrayList<LinkedList> degreeDistributions;

    /**
     * Private constructor for NetworkAnalyzer singleton
     */
    private NetworkAnalyzer() {
        degreeDistributions = new ArrayList<>();
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
     * Adds degree distribution to collection
     *
     * @param degreeDistribution Degree distribution
     */
    public void addDegreeDistribution(LinkedList degreeDistribution) {
        degreeDistributions.add(degreeDistribution);
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
            degreeDistribution.add(i, (double) occurrences.get(i).get() / network.getNodes().size());
        }
        return degreeDistribution;
    }

    /**
     * Gets recorded degree distributions
     *
     * @return Degree distributions
     */
    public ArrayList<LinkedList> getDegreeDistributions() {
        return degreeDistributions;
    }

    /**
     * Gets average clustering coefficient for the network
     *
     * @param network Network
     * @return Average clustering coefficient
     */
    public double getAverageClusteringCoefficient(Network network) {
        double sum = 0;
        for (int i = 0; i < network.getNodes().size(); i++) {
            double c = getClusteringCoefficient(network, i);
            sum += c;
        }
        return sum / network.getNodes().size();
    }

    /**
     * Gets clustering coefficient
     *
     * @param network Network
     * @param id      Node id
     * @return Clustering coefficient
     */
    private double getClusteringCoefficient(Network network, int id) {
        int degree = network.getNodeDegree(id);
        int links = 0;
        LinkedList adjacencyList = network.getAdjacencyList().get(id);
        for (int i = 0; i < adjacencyList.size(); i++) {
            LinkedList iAdjacencyList = network.getAdjacencyList().get((Integer) adjacencyList.get(i));
            for (Object anIAdjacencyList : iAdjacencyList) {
                if (adjacencyList.contains(anIAdjacencyList)) {
                    links++;
                }
            }
        }
        return (double) (links) / (degree * (degree - 1));
    }
}
