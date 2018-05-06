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

/**
 * Service class to plot log-binned degree distribution
 */
public class PlotLogDegreeDistributionService extends Service {
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
                    int n = 0;
                    int index = 0;
                    for (int j = 0; j < degreeDistribution.size(); j++) {
                        if (j < index) {
                            series.add(j, 0);
                        } else {
                            double entriesInThisBin = Math.pow(2, n);
                            while (entriesInThisBin + index > degreeDistribution.size()) {
                                entriesInThisBin--;
                            }
                            double averageDegreeDistribution = 0;
                            for (int k = 0; k < entriesInThisBin; k++) {
                                averageDegreeDistribution += (double) degreeDistribution.get(k + index);
                            }
                            averageDegreeDistribution /= entriesInThisBin;
                            series.add(index, averageDegreeDistribution);
                            index += entriesInThisBin;
                            n++;
                        }
                    }
                    seriesCollection.addSeries(series);
                }
                seriesCollection.addSeries(ChartCreator.getInstance().getPowerLawPlot(10000));
                return ChartCreator.getInstance().getLogChart(seriesCollection, 1, Math.pow(10, (-9)), 10000, 1, "k", "Pk", false, true);
            }
        };
    }
}
