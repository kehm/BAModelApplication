package no.priv.kehm.bamodelapplication.util;

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

/**
 * Singleton utility class to create charts
 */
public class ChartCreator {
    private static ChartCreator chartCreator;

    /**
     * Private constructor for NetworkGenerator singleton
     */
    private ChartCreator() {
    }

    /**
     * Gets NetworkGenerator singleton instance
     *
     * @return NetworkGenerator object
     */
    public static synchronized ChartCreator getInstance() {
        if (chartCreator == null) {
            chartCreator = new ChartCreator();
        }
        return chartCreator;
    }

    /**
     * Gets chart with log axis'
     *
     * @param seriesCollection Data set
     * @param xAxisLowerBound  Lower bound for x-axis
     * @param yAxisLowerBound  Lower bound for y-axis
     * @param xAxisUpperBound  Upper bound for x-axis
     * @param yAxisUpperBound  Upper bound for y-axis
     * @param xLabel           Label for x-axis
     * @param yLabel           Label for y-axis
     * @param lines            True if create chart with lines
     * @param shapes           True if create chart with shapes
     * @return JPanel with chart
     */
    public JPanel getLogChart(XYSeriesCollection seriesCollection, double xAxisLowerBound, double yAxisLowerBound, double xAxisUpperBound, double yAxisUpperBound, String xLabel, String yLabel, boolean lines, boolean shapes) {
        LogAxis xAxis = new LogAxis(xLabel);
        LogAxis yAxis = new LogAxis(yLabel);
        yAxis.setBase(10);
        xAxis.setBase(10);
        yAxis.setLowerBound(yAxisLowerBound);
        yAxis.setUpperBound(yAxisUpperBound);
        xAxis.setLowerBound(xAxisLowerBound);
        xAxis.setUpperBound(xAxisUpperBound);
        XYPlot plot = new XYPlot(seriesCollection, xAxis, yAxis, new XYLineAndShapeRenderer(lines, shapes));
        XYItemRenderer renderer = plot.getRenderer();
        for (int i = 0; i < seriesCollection.getSeriesCount(); i++) {
            renderer.setSeriesShape(i, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
        }
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

    /**
     * Gets power law plot
     *
     * @return Power law plot
     */
    public XYSeries getPowerLawPlot(int size) {
        XYSeries series = new XYSeries("Power law");
        for (int i = 0; i < size; i++) {
            series.add(i, Math.pow(i, -3));
        }
        return series;
    }
}
