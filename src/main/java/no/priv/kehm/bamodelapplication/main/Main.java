package no.priv.kehm.bamodelapplication.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import no.priv.kehm.bamodelapplication.gui.MainController;
import no.priv.kehm.bamodelapplication.network.Network;
import no.priv.kehm.bamodelapplication.util.NetworkAnalyzer;
import no.priv.kehm.bamodelapplication.util.NetworkGenerator;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Main class for launching the Barabási-Albert Model Application (BAModelApplication)
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
        Parent root = (Parent) loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/main.css");
        primaryStage.setTitle("Barabási-Albert Model Application");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
        MainController controller = loader.getController();
        printToConsole();
    }

    /**
     * Main method to launch application
     *
     * @param args args
     */
    public static void main(String[] args) {
        launch(args);

    }

    private static void printToConsole() {
        int n = 10; // The book specifies N = 10^4
        int m = 4; // The book specifies m = 4 as an initial condition
        Network network = NetworkGenerator.getInstance().generateNetwork(n, m);
        printAdjacencyList(network);
        printDegrees(network);
        printDegreeDistribution(network);
    }

    /**
     * Prints adjacency list
     *
     * @param network Network
     */
    private static void printAdjacencyList(Network network) {
        for (int i = 0; i < network.getNodes().size(); i++) {
            System.out.print("Node " + i + " -> ");
            LinkedList links = network.getAdjacencyList().get(i);
            for (Object o : links) {
                System.out.print((int) o + " ");
            }
            System.out.println();
        }
        System.out.println("Total number of links = " + network.getLinks());
    }

    /**
     * Prints node degrees
     *
     * @param network Network
     */
    private static void printDegrees(Network network) {
        for (int i = 0; i < network.getNodes().size(); i++) {
            System.out.println("Node = " + i + " Degree = " + network.getNodeDegree(i));
        }
    }

    /**
     * Prints degree distribution
     *
     * @param network Network
     */
    private static void printDegreeDistribution(Network network) {
        LinkedList degreeDistribution = NetworkAnalyzer.getInstance().getDegreeDistribution(network);
        int counter = 0;
        for (Object o : degreeDistribution) {
            AtomicInteger integer = (AtomicInteger) o;
            System.out.println("k = " + counter + " distr = " + integer.toString());
            counter++;
        }
    }
}
