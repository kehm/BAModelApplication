package no.priv.kehm.bamodelapplication.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import no.priv.kehm.bamodelapplication.util.gui.ChartCreator;
import no.priv.kehm.bamodelapplication.util.NetworkAnalyzer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Service class to plot degree dynamics
 */
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
                return ChartCreator.getInstance().getLogChart(seriesCollection, 1, 1, 10000, 100000, "t", "k", true, false);
            }
        };
    }
}
