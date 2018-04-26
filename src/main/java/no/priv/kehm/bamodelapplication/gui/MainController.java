package no.priv.kehm.bamodelapplication.gui;

import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.text.Text;
import no.priv.kehm.bamodelapplication.network.Network;
import no.priv.kehm.bamodelapplication.service.GenerateNetworkService;
import no.priv.kehm.bamodelapplication.util.NetworkAnalyzer;
import no.priv.kehm.bamodelapplication.util.NetworkGenerator;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class MainController implements Initializable {


    private Network network;

    @FXML
    private Button generateNetworkBtn;
    @FXML
    private Button exitApplicationBtn;
    @FXML
    private Button plotDegreeDistributionBtn;
    @FXML
    private ScatterChart distributionChart;
    @FXML
    private NumberAxis distributionChartX;
    @FXML
    private NumberAxis distributionChartY;
    @FXML
    private Tab mainTab;
    @FXML
    private Tab degreeDistributionTab;
    @FXML
    private Tab cumulativeDegreeDistributionTab;
    @FXML
    private Tab clusteringCoefficientTab;
    @FXML
    private Tab degreeDynamicsTab;
    @FXML
    private ProgressIndicator networkProgress;
    @FXML
    private Text welcomeText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        degreeDistributionTab.setDisable(true);
        cumulativeDegreeDistributionTab.setDisable(true);
        clusteringCoefficientTab.setDisable(true);
        degreeDynamicsTab.setDisable(true);
        networkProgress.setVisible(false);
    }

    @FXML
    private void exitApplication(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void generateNetwork(ActionEvent event) {
        welcomeText.setText("Generating network...");
        generateNetworkBtn.setDisable(true);
        exitApplicationBtn.setDisable(true);
        networkProgress.setVisible(true);
        final GenerateNetworkService generateNetworkService = new GenerateNetworkService();
        generateNetworkService.setOnSucceeded(workerStateEvent -> {
            network = (Network) generateNetworkService.getValue();
            networkProgress.setVisible(false);
            welcomeText.setText("Network generated");
            degreeDistributionTab.setDisable(false);
            cumulativeDegreeDistributionTab.setDisable(false);
            clusteringCoefficientTab.setDisable(false);
            degreeDynamicsTab.setDisable(false);
            exitApplicationBtn.setDisable(false);
        });
        generateNetworkService.setOnFailed(workerStateEvent -> {
            networkProgress.setVisible(false);
            exitApplicationBtn.setDisable(false);
            generateNetworkBtn.setDisable(false);
            welcomeText.setText("Click the button below to generate a network");
            welcomeText.setVisible(true);
            System.out.println("Network generation failed!");
        });
        generateNetworkService.restart(); //here you start your service
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
