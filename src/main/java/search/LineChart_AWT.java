package search;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;

public class LineChart_AWT{
    private JFrame frame;
    public LineChart_AWT(String applicationTitle, String chartTitle) {
        this.frame=new JFrame("你好");
        JFreeChart lineChart = ChartFactory.createXYLineChart(
                chartTitle,
                "Years", "Number of Schools",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        frame.setContentPane(chartPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private XYSeriesCollection createDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries standard=new XYSeries("preduction");
        XYSeries output=new XYSeries("neuron output");
        for(Integer i=0;i<1000;i++){
            standard.add(new Double(i),new Double(i+10));
        }

        for(Integer i=0;i<1000;i++){
            output.add(new Double(i),new Double(i));
        }
        dataset.addSeries(standard);
        dataset.addSeries(output);
        return dataset;
    }

    public static void main(String[] args) {
        LineChart_AWT chart = new LineChart_AWT(
                "School Vs Years",
                "Numer of Schools vs years");
    }
}
