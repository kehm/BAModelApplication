package no.priv.kehm.bamodelapplication.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import no.priv.kehm.bamodelapplication.network.Network;
import no.priv.kehm.bamodelapplication.util.NetworkAnalyzer;
import no.priv.kehm.bamodelapplication.util.NetworkGenerator;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private static final int M = 4;
    private static final int N = 10000 - M;
    private Network network;

    @FXML
    private Button generateNetworkBtn;
    @FXML
    private Button plotDegreeDistributionBtn;
    @FXML
    private ScatterChart distributionChart;
    @FXML
    private NumberAxis distributionChartX;
    @FXML
    private NumberAxis distributionChartY;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void exitApplication(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void generateNetwork(ActionEvent event) {
        this.network = NetworkGenerator.getInstance().generateNetwork(N, M);
        distributionChartY.setAutoRanging(false);
        distributionChartY.setLowerBound(-3);
        distributionChartY.setUpperBound(3);
        distributionChartY.setTickUnit(0.1);
        distributionChartX.setAutoRanging(false);
        distributionChartX.setLowerBound(0);
        distributionChartX.setUpperBound(200);
        distributionChartX.setTickUnit(1);
        //printAdjacencyList(network);
        //printDegrees(network);
        //printDegreeDistribution(network);
    }

    @FXML
    private void plotDegreeDistribution(ActionEvent event) {
        ArrayList degreeDistributions = NetworkAnalyzer.getInstance().getDegreeDistributions();
        for (int i = 0; i < degreeDistributions.size(); i++) {
            final XYChart.Series series = new XYChart.Series();
            series.setName("Degree Distribution");
            LinkedList degreeDistribution = (LinkedList) degreeDistributions.get(i);
            for (int j = 0; j < degreeDistribution.size(); j++) {
                series.getData().add(new XYChart.Data<>(j, Math.log10((double) degreeDistribution.get(j))));
            }
            Platform.runLater(() -> distributionChart.getData().addAll(series));
        }
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
            System.out.println("k = " + counter + " distr = " + (double) o);
            counter++;
        }
    }
}
