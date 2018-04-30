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
import no.priv.kehm.bamodelapplication.service.GenerateNetworkService;
import no.priv.kehm.bamodelapplication.util.NetworkAnalyzer;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
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
    private Tab startTab;
    @FXML
    private Tab degreeDistributionTab;
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
    private Text clusteringText;
    @FXML
    private Text clusteringCText;
    @FXML
    private Text numberOfNodesText;
    @FXML
    private Text nOfNodesText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        degreeDistributionTab.setDisable(true);
        cumulativeDegreeDistributionTab.setDisable(true);
        degreeDynamicsTab.setDisable(true);
        networkProgress.setVisible(false);
        clusteringText.setVisible(false);
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
            cumulativeDegreeDistributionTab.setDisable(false);
            degreeDynamicsTab.setDisable(false);
            exitApplicationBtn.setDisable(false);
            numberOfNodesText.setVisible(true);
            nOfNodesText.setText(String.valueOf(network.getNodes().size()));
            clusteringText.setVisible(true);
            clusteringCText.setText(String.valueOf(NetworkAnalyzer.getInstance().getAverageClusteringCoefficient(network)));
            plotDegreeDistribution();
        });
        generateNetworkService.setOnFailed(workerStateEvent -> {
            networkProgress.setVisible(false);
            exitApplicationBtn.setDisable(false);
            generateNetworkBtn.setDisable(false);
            welcomeText.setText("Click the button below to generate a network");
            welcomeText.setVisible(true);
            System.out.println("Network generation failed!");
        });
        generateNetworkService.restart();
    }

    private void plotDegreeDistribution() {
        XYSeriesCollection seriesCollection = new XYSeriesCollection();
        ArrayList degreeDistributions = NetworkAnalyzer.getInstance().getDegreeDistributions();
        for (int i = 0; i < degreeDistributions.size(); i++) {
            String tag = i+1 + ". Measurement";
            XYSeries series = new XYSeries(tag);
            LinkedList degreeDistribution = (LinkedList) degreeDistributions.get(i);
            for (int j = 0; j < degreeDistribution.size(); j++) {
                series.add(j, (double) degreeDistribution.get(j));
            }
            seriesCollection.addSeries(series);
        }
        LogAxis xAxis = new LogAxis("k");
        LogAxis yAxis = new LogAxis("Pk");
        yAxis.setBase(10);
        xAxis.setBase(10);
        yAxis.setLowerBound(Math.pow(10, (-9)));
        yAxis.setUpperBound(1);
        xAxis.setLowerBound(1);
        xAxis.setUpperBound(10000);
        XYPlot plot = new XYPlot(seriesCollection, xAxis, yAxis, new XYLineAndShapeRenderer(false, true));
        XYItemRenderer renderer = plot.getRenderer();
        for(int i = 0; i < seriesCollection.getSeriesCount(); i++) {
            renderer.setSeriesShape(i, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
        }
        JFreeChart chart = new JFreeChart(plot);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(680,445));
        JPanel jPanel = new JPanel();
        jPanel.add(chartPanel);
        SwingUtilities.invokeLater(() -> distributionChartNode.setContent(jPanel));
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
