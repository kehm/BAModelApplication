package no.priv.kehm.bamodelapplication.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import no.priv.kehm.bamodelapplication.util.gui.ChartCreator;
import no.priv.kehm.bamodelapplication.util.NetworkAnalyzer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class PlotCumulativeDistributionService extends Service {
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
                        double sum = 0;
                        for (int k = j; k < degreeDistribution.size(); k++) {
                            sum += (double) degreeDistribution.get(k);
                        }
                        series.add(j, sum);
                    }
                    seriesCollection.addSeries(series);
                }
                return ChartCreator.getInstance().getLogChart(seriesCollection, 1, Math.pow(10, (-9)), 10000, 1, "k", "Pk", false, true);
            }
        };
    }
}
