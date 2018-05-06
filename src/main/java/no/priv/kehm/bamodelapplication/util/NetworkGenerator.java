package no.priv.kehm.bamodelapplication.util;

import no.priv.kehm.bamodelapplication.network.Network;
import no.priv.kehm.bamodelapplication.network.Node;

import java.util.ArrayList;
import java.util.Random;

/**
 * Singleton class to generate networks
 */
public class NetworkGenerator {

    private static NetworkGenerator networkGenerator;
    private static Random rand;

    /**
     * Private constructor for NetworkGenerator singleton
     */
    private NetworkGenerator() {
    }

    /**
     * Gets NetworkGenerator singleton instance
     *
     * @return NetworkGenerator object
     */
    public static synchronized NetworkGenerator getInstance() {
        if (networkGenerator == null) {
            networkGenerator = new NetworkGenerator();
            rand = new Random();
        }
        return networkGenerator;
    }

    /**
     * Generates a network using the Barabási-Albert model
     *
     * @param n Number of nodes
     * @param m Number of links for each node
     * @return Generated network
     */
    public Network generateNetwork(int n, int m) {
        Network network = init(m);
        NetworkAnalyzer.getInstance().initDegreeDynamics(m);
        ArrayList<Node> nodes = createNodes(network.getNodes().size(), n);
        for (Node node : nodes) {
            addNetworkNode(network, node, m);
            // Measure degree distribution at intermediate steps
            if (network.getNodes().size() == 100 || network.getNodes().size() == 1000 || network.getNodes().size() == 10000) {
                NetworkAnalyzer.getInstance().addDegreeDistribution(NetworkAnalyzer.getInstance().getDegreeDistribution(network));
            }
            NetworkAnalyzer.getInstance().measureDegreeDynamics(network);
        }
        return network;
    }

    /**
     * Creates a fully connected network with m nodes
     *
     * @param m Number of nodes
     * @return Fully connected network with m nodes
     */
    private Network init(int m) {
        Network network = new Network(createNodes(0, m));
        for (int i = 0; i < network.getNodes().size(); i++) {
            for (int j = 0; j < network.getNodes().size(); j++) {
                if (i != j) {
                    network.addLink(i, j);
                }
            }
        }
        return network;
    }

    /**
     * Creates the specified number of nodes
     *
     * @param i Start index/node id
     * @param n Number of nodes to create
     * @return List of created nodes
     */
    private ArrayList<Node> createNodes(int i, int n) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (int j = i; j < n + i; j++) {
            Node node = new Node(j);
            nodes.add(node);
        }
        return nodes;
    }

    /**
     * Adds new node to the network according to the Barabási-Albert model
     *
     * @param network Network
     * @param node    Node to add
     * @param m       Number of links to connect
     */
    private void addNetworkNode(Network network, Node node, int m) {
        ArrayList<Integer> linkedNodes = new ArrayList<>();
        while (linkedNodes.size() < m) {
            Node randNode = getRandomNode(network);
            double p = (double) network.getNodeDegree(randNode.getId()) / network.getLinks();
            double r = rand.nextDouble();
            if (p > r && !linkedNodes.contains(randNode.getId())) {
                linkedNodes.add(randNode.getId());
            }
        }
        network.addNode(node);
        for (int linkedNode : linkedNodes) {
            network.addLink(node.getId(), linkedNode);
            network.addLink(linkedNode, node.getId());
        }
    }

    /**
     * Gets a random node in the network
     *
     * @param network Network
     * @return Random node
     */
    private Node getRandomNode(Network network) {
        return (Node) network.getNodes().get(rand.nextInt(network.getNodes().size()));
    }
}
