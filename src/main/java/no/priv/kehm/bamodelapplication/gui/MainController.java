package no.priv.kehm.bamodelapplication.gui;

import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.text.Text;
import no.priv.kehm.bamodelapplication.network.Network;
import no.priv.kehm.bamodelapplication.service.*;
import no.priv.kehm.bamodelapplication.util.NetworkAnalyzer;

import javax.swing.*;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    private Network network;

    @FXML
    private Button generateNetworkBtn;
    @FXML
    private Button exitApplicationBtn;
    @FXML
    private Tab startTab;
    @FXML
    private Tab degreeDistributionTab;
    @FXML
    private Tab logDistributionTab;
    @FXML
    private Tab cumulativeDegreeDistributionTab;
    @FXML
    private Tab degreeDynamicsTab;
    @FXML
    private ProgressIndicator networkProgress;
    @FXML
    private Text welcomeText;
    @FXML
    private SwingNode distributionChartNode;
    @FXML
    private SwingNode logChartNode;
    @FXML
    private SwingNode cumulativeChartNode;
    @FXML
    private SwingNode dynamicsChartNode;
    @FXML
    private Text clusteringText;
    @FXML
    private Text clusteringCText;
    @FXML
    private Text numberOfNodesText;
    @FXML
    private Text nOfNodesText;
    @FXML
    private Text nClusteringText;
    @FXML
    private Text nClusteringCText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        degreeDistributionTab.setDisable(true);
        logDistributionTab.setDisable(true);
        cumulativeDegreeDistributionTab.setDisable(true);
        degreeDynamicsTab.setDisable(true);
        networkProgress.setVisible(false);
        clusteringText.setVisible(false);
        nClusteringCText.setVisible(false);
        numberOfNodesText.setVisible(false);
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
            logDistributionTab.setDisable(false);
            cumulativeDegreeDistributionTab.setDisable(false);
            degreeDynamicsTab.setDisable(false);
            exitApplicationBtn.setDisable(false);
            numberOfNodesText.setVisible(true);
            nOfNodesText.setText(String.valueOf(network.getNodes().size()));
            nClusteringCText.setVisible(true);
            double lnN = Math.log(network.getNodes().size());
            nClusteringText.setText(String.valueOf(Math.pow(lnN, 2) / network.getNodes().size()));
            clusteringText.setVisible(true);
            clusteringCText.setText(String.valueOf(NetworkAnalyzer.getInstance().getAverageClusteringCoefficient(network)));
            plotLinDegreeDistribution();
            plotLogDegreeDistribution();
            plotCumulativeDistribution();
            plotDegreeDynamics();
        });
        generateNetworkService.setOnFailed(workerStateEvent -> {
            networkProgress.setVisible(false);
            exitApplicationBtn.setDisable(false);
            generateNetworkBtn.setDisable(false);
            welcomeText.setText("");
            welcomeText.setVisible(true);
            System.out.println("Network generation failed!");
        });
        generateNetworkService.restart();
    }

    /**
     * Plots linearly binned degree distribution in the "Degree Distribution (Linear)" tab
     */
    private void plotLinDegreeDistribution() {
        final PlotLinDegreeDistributionService plotLinDegreeDistributionService = new PlotLinDegreeDistributionService();
        plotLinDegreeDistributionService.setOnSucceeded(workerStateEvent -> {
            JPanel jPanel = (JPanel) plotLinDegreeDistributionService.getValue();
            SwingUtilities.invokeLater(() -> distributionChartNode.setContent(jPanel));
        });
        plotLinDegreeDistributionService.setOnFailed(workerStateEvent -> {
            degreeDistributionTab.setDisable(true);
            System.out.println("Linearly binned degree distribution measurement failed!");
        });
        plotLinDegreeDistributionService.restart();
    }

    /**
     * Plots log-binned degree distribution in the "Degree Distribution (Log)" tab
     */
    private void plotLogDegreeDistribution() {
        final PlotLogDegreeDistributionService plotLogDegreeDistributionService = new PlotLogDegreeDistributionService();
        plotLogDegreeDistributionService.setOnSucceeded(workerStateEvent -> {
            JPanel jPanel = (JPanel) plotLogDegreeDistributionService.getValue();
            SwingUtilities.invokeLater(() -> logChartNode.setContent(jPanel));
        });
        plotLogDegreeDistributionService.setOnFailed(workerStateEvent -> {
            logDistributionTab.setDisable(true);
            System.out.println("Log-binned degree distribution measurement failed!");
        });
        plotLogDegreeDistributionService.restart();
    }

    /**
     * Plots cumulative degree distribution in the "Cumulative Distribution" tab
     */
    private void plotCumulativeDistribution() {
        final PlotCumulativeDistributionService plotCumulativeDistributionService = new PlotCumulativeDistributionService();
        plotCumulativeDistributionService.setOnSucceeded(workerStateEvent -> {
            JPanel jPanel = (JPanel) plotCumulativeDistributionService.getValue();
            SwingUtilities.invokeLater(() -> cumulativeChartNode.setContent(jPanel));
        });
        plotCumulativeDistributionService.setOnFailed(workerStateEvent -> {
            logDistributionTab.setDisable(true);
            System.out.println("Cumulative degree distribution measurement failed!");
        });
        plotCumulativeDistributionService.restart();
    }

    /**
     * Plots the degree dynamics in the "Degree Dynamics" tab
     */
    private void plotDegreeDynamics() {
        final PlotDegreeDynamicsService plotDegreeDynamicsService = new PlotDegreeDynamicsService();
        plotDegreeDynamicsService.setOnSucceeded(workerStateEvent -> {
            JPanel jPanel = (JPanel) plotDegreeDynamicsService.getValue();
            SwingUtilities.invokeLater(() -> dynamicsChartNode.setContent(jPanel));
        });
        plotDegreeDynamicsService.setOnFailed(workerStateEvent -> {
            degreeDynamicsTab.setDisable(true);
            System.out.println("Degree dynamics measurement failed!");
        });
        plotDegreeDynamicsService.restart();
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
