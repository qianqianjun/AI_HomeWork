package bp;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;

/**
 * @author 高谦
 * 用于展示各种曲线的panel
 */
public class ResultDisplayPanel extends JPanel {
    private String type;
    private Integer chartWidth;
    private Integer chartHeight;
    public ResultDisplayPanel(String type,Integer preWidth,Integer preHeight){
        this.type=type;
        this.chartHeight=preHeight;
        this.chartWidth=preWidth;
        setChart();
    }
    public void setChart(){
        JFreeChart lineChart = ChartFactory.createXYLineChart(
                type,
                "Years", "Number of Schools",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(chartWidth, chartHeight));
        this.add(chartPanel);
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
}
