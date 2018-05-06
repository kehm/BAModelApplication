package no.priv.kehm.bamodelapplication.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
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
import java.util.ArrayList;
import java.util.LinkedList;

public class PlotLogDistributionService extends Service {
    @Override
    protected Task createTask() {
        return new Task<JPanel>() {
            @Override
            protected JPanel call() throws Exception {
                XYSeriesCollection seriesCollection = new XYSeriesCollection();
                ArrayList degreeDistributions = NetworkAnalyzer.getInstance().getDegreeDistributions();
                for (int i = 0; i < degreeDistributions.size(); i++) {
                    String tag = i + 1 + ". Measurement";
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
                for (int i = 0; i < seriesCollection.getSeriesCount(); i++) {
                    renderer.setSeriesShape(i, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                }
                JFreeChart chart = new JFreeChart(plot);
                ChartPanel chartPanel = new ChartPanel(chart, false);
                chartPanel.setPreferredSize(new Dimension(680, 445));
                JPanel jPanel = new JPanel();
                jPanel.add(chartPanel);
                return jPanel;
            }
        };
    }
}