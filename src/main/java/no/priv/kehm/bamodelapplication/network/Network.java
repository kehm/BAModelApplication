package no.priv.kehm.bamodelapplication.network;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Class to represent a network of nodes and links
 */
public class Network {

    private ArrayList<Node> nodes; // list of all nodes in the network
    private ArrayList<LinkedList<Integer>> adjacencyList; // list of all links in the network
    private int links; // counter for the total number of links in the network

    /**
     * Constructor for Network object
     *
     * @param nodes List of nodes
     */
    public Network(ArrayList<Node> nodes) {
        this.nodes = nodes;
        links = 0; // set initial number of links to 0
        adjacencyList = new ArrayList<>(); // initialize adjacency list
        for (Node node : nodes) {
            adjacencyList.add(new LinkedList<>());
        }
    }

    /**
     * Adds new node to network
     *
     * @param node Node to add
     */
    public void addNode(Node node) {
        nodes.add(node);
        adjacencyList.add(new LinkedList<>());
    }

    /**
     * Adds a link from i to j
     *
     * @param i Node i
     * @param j Node j
     */
    public void addLink(int i, int j) {
        adjacencyList.get(i).add(j);
        links++;
    }

    /**
     * Gets list of nodes
     *
     * @return List of nodes
     */
    public ArrayList getNodes() {
        return nodes;
    }

    /**
     * Gets adjacency list
     *
     * @return Adjacency list
     */
    public ArrayList<LinkedList<Integer>> getAdjacencyList() {
        return adjacencyList;
    }

    /**
     * Gets the total number of links in the network
     *
     * @return Total number of links
     */
    public int getLinks() {
        return links;
    }

    /**
     * Gets node degree
     *
     * @param id Node id
     * @return Node degree
     */
    public int getNodeDegree(int id) {
        return adjacencyList.get(id).size();
    }
}
