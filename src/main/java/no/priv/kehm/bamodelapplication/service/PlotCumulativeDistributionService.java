package no.priv.kehm.bamodelapplication.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.swing.*;

public class PlotCumulativeDistributionService extends Service {
    @Override
    protected Task createTask() {
        return new Task<JPanel>() {
            @Override
            protected JPanel call() throws Exception {
                JPanel jPanel = new JPanel();
                return jPanel;
            }
        };
    }
}
