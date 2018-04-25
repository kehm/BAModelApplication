package no.priv.kehm.bamodelapplication.network;

/**
 * Class to represent a Node in the network
 */
public class Node {

    private int id;

    /**
     * Constructor for Node object
     *
     * @param id Node identifier
     */
    public Node(int id) {
        this.id = id;
    }

    /**
     * Gets node identifier
     *
     * @return Node identifier
     */
    public int getId() {
        return id;
    }
}
