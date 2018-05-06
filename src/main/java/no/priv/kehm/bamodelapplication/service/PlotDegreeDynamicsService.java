package no.priv.kehm.bamodelapplication.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import no.priv.kehm.bamodelapplication.util.NetworkAnalyzer;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class PlotDegreeDynamicsService extends Service {
    @Override
    protected Task createTask() {
        return new Task<JPanel>() {
            @Override
            protected JPanel call() throws Exception {
                XYSeriesCollection seriesCollection = new XYSeriesCollection();
                LinkedHashMap<Integer, LinkedList<Integer>> degreeDynamics = NetworkAnalyzer.getInstance().getDegreeDynamics();
                for (Map.Entry<Integer, LinkedList<Integer>> pair : degreeDynamics.entrySet()) {
                    int id = pair.getKey();
                    int t = id + 1 - NetworkAnalyzer.getInstance().getM();
                    String tag = "Node t=" + t;
                    XYSeries series = new XYSeries(tag);
                    LinkedList<Integer> degrees = pair.getValue();
                    for (int i = 0; i < degrees.size(); i++) {
                        series.add(i, degrees.get(i));
                    }
                    seriesCollection.addSeries(series);
                }
                LogAxis xAxis = new LogAxis("t");
                LogAxis yAxis = new LogAxis("k");
                yAxis.setLowerBound(1);
                yAxis.setUpperBound(100000);
                xAxis.setLowerBound(1);
                xAxis.setUpperBound(10000);
                XYPlot plot = new XYPlot(seriesCollection, xAxis, yAxis, new XYLineAndShapeRenderer(true, false));
                JFreeChart chart = new JFreeChart(plot);
                ChartPanel chartPanel = new ChartPanel(chart, false);
                chartPanel.setPreferredSize(new Dimension(680, 445));
                chartPanel.setPopupMenu(null);
                chartPanel.setDomainZoomable(false);
                chartPanel.setRangeZoomable(false);
                JPanel jPanel = new JPanel();
                jPanel.add(chartPanel);
                return jPanel;
            }
        };
    }
}
