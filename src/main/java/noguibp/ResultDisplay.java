package noguibp;
import javax.swing.*;

import noguibp.structure.TrainResult;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * @author 高谦
 * 用于展示各种曲线的panel
 */
class ResultDisplayPanel extends JPanel {
    private String type;
    private Integer chartWidth;
    private Integer chartHeight;
    private String xInfo;
    private String yInfo;
    private XYSeriesCollection dataset;
    public ResultDisplayPanel(String type,Integer preWidth,Integer preHeight,
                              String xAxisLabel,String yAxisLabel,XYSeriesCollection dataset){
        this.type=type;
        this.chartHeight=preHeight;
        this.chartWidth=preWidth;
        this.xInfo=xAxisLabel;
        this.yInfo=yAxisLabel;
        this.dataset=dataset;
        setChart();
    }
    public void setChart(){
        JFreeChart lineChart = ChartFactory.createXYLineChart(
                type,
                xInfo, yInfo,
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(chartWidth, chartHeight));
        this.add(chartPanel);
    }
}

/**
 * @author 高谦
 *  用于展示训练，回想，结果的 panel
 */
class TrainPanel extends JPanel {
    public ResultDisplayPanel recallPanel;
    public ResultDisplayPanel lossPanel;
    public ResultDisplayPanel generalizationPanel;
    private Double learnRate=0.1;
    public TrainPanel(){
        this.setLayout(null);
        ObjectInputStream objectReader=null;
        TrainResult trainResult=null;
        try {
            objectReader = new ObjectInputStream(new FileInputStream(new File("trainResult.dat")));
            trainResult=(TrainResult) objectReader.readObject();
            objectReader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        recallPanel=new ResultDisplayPanel("recall result",485,320,
                "x","y",trainResult.getRecallSet());
        recallPanel.setBounds(5,60,490,325);
        this.add(recallPanel);
        generalizationPanel=new ResultDisplayPanel("generalization result",465,320,
                "x","y",trainResult.getGeneralizationSet());
        generalizationPanel.setBounds(500,60,470,325);
        this.add(generalizationPanel);
        lossPanel=new ResultDisplayPanel("loss result",960,320,
                "step","loss value",trainResult.getLossSet());
        lossPanel.setBounds(5,390,965,325);
        this.add(lossPanel);
    }
}
/**
 * @author 展示页面的结果
 */
public class ResultDisplay{
    public static void main(String[] args) {
        JFrame frame=new JFrame("BP算法演示：by 高谦 :) ");
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }catch (Exception e){
            e.printStackTrace();
        }
        frame.setSize(1000,800);
        JPanel trainPanel=new TrainPanel();
        frame.add(trainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
