package no.priv.kehm.bamodelapplication.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import no.priv.kehm.bamodelapplication.network.Network;
import no.priv.kehm.bamodelapplication.util.NetworkAnalyzer;
import no.priv.kehm.bamodelapplication.util.NetworkGenerator;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class MainController implements Initializable {

    @FXML
    private Button generateNetworkBtn;
    @FXML
    private Button plotDegreeDistributionBtn;
    @FXML
    private ScatterChart distributionChart;

    private Network network;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void exitApplication(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void generateNetwork(ActionEvent event) {
        int n = 10; // The book specifies N = 10^4
        int m = 4; // The book specifies m = 4 as an initial condition
        this.network = NetworkGenerator.getInstance().generateNetwork(n, m);
        printAdjacencyList(network);
        printDegrees(network);
        printDegreeDistribution(network);
    }

    @FXML
    private void plotDegreeDistribution(ActionEvent event) {
        final XYChart.Series series = new XYChart.Series();
        series.setName("Degree Distribution");
        LinkedList degreeDistribution = NetworkAnalyzer.getInstance().getDegreeDistribution(network);
        Platform.runLater(() -> distributionChart.getData().addAll(series));
    }


    // DEBUG METHODS BELOW
    /**
     * Prints adjacency list
     *
     * @param network Network
     */
    private void printAdjacencyList(Network network) {
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
    private void printDegrees(Network network) {
        for (int i = 0; i < network.getNodes().size(); i++) {
            System.out.println("Node = " + i + " Degree = " + network.getNodeDegree(i));
        }
    }

    /**
     * Prints degree distribution
     *
     * @param network Network
     */
    private void printDegreeDistribution(Network network) {
        LinkedList degreeDistribution = NetworkAnalyzer.getInstance().getDegreeDistribution(network);
        int counter = 0;
        for (Object o : degreeDistribution) {
            AtomicInteger integer = (AtomicInteger) o;
            System.out.println("k = " + counter + " distr = " + integer.toString());
            counter++;
        }
    }
}
