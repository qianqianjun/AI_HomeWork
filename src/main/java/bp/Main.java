package bp;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author 高谦
 * 主面板，用于启动程序运行
 */
public class Main {
    private JFrame frame;
    private JMenuBar menuBar;
    public String function;
    public Double a;
    public Double b;
    public Double begin;
    public Double ends;
    public Integer num;
    public Double[] xData;
    public Double[] yData;
    public Double[] textX;
    public Double[] textY;
    public NetworkCanvas networkCanvas;
    public List<String> dataset;
    public TrainPanel trainPanel;
    // 获取测测试数据的静态函数。
    private void dataSetUi(Boolean isnew){
        JPanel panel=new DatasetPanel(this,isnew);
        this.frame.getContentPane().removeAll();
        this.frame.add(panel);
        this.frame.setVisible(true);
        this.frame.repaint();
    }
    private void initDataUi(){
        final JMenu dataset=new JMenu("数据操作");
        menuBar.add(dataset);
        JMenuItem create=new JMenuItem("新建训练集");
        JMenuItem fix=new JMenuItem("修改训练集");
        dataset.add(create);
        dataset.add(fix);
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataSetUi(true);
            }
        });
        fix.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(function==null){
                    JOptionPane.showMessageDialog(null,
                            "您还没有建立数据集","提示",JOptionPane.ERROR_MESSAGE);
                    dataSetUi(true);
                }
                else{
                    dataSetUi(false);
                }
            }
        });
    }

    private void networkUi(boolean isnew) {
        JPanel panel=new NetworkPanel(this,isnew);
        this.frame.getContentPane().removeAll();
        this.frame.add(panel);
        this.frame.setVisible(true);
        this.frame.repaint();
    }
    private void initNetworkUi(){
        final JMenu network=new JMenu("神经网络");
        menuBar.add(network);
        JMenuItem createNet=new JMenuItem("新建网络");
        JMenuItem fixNet=new JMenuItem("修改网络");
        createNet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(xData!=null || dataset!=null){
                    networkUi(true);
                }
                else{
                    JOptionPane.showMessageDialog(null,"您还没有初始化数据集",
                            "提示",JOptionPane.ERROR_MESSAGE);
                    dataSetUi(true);
                }
            }
        });
        fixNet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(networkCanvas ==null){
                    JOptionPane.showMessageDialog(null,"您还没有创建过网络",
                            "提示",JOptionPane.ERROR_MESSAGE);
                    if(xData!=null|| dataset!=null)
                        networkUi(true);
                    else
                        dataSetUi(true);
                }
                else {
                    networkUi(false);
                }
            }
        });
        network.add(createNet);
        network.add(fixNet);
    }

    private void runUi(Boolean isnew){
        if(isnew) {
            trainPanel = new TrainPanel(this);
        }
        this.frame.getContentPane().removeAll();
        this.frame.add(trainPanel);
        this.frame.setVisible(true);
        this.frame.repaint();
    }
    private void initRunUi(){
        final JMenu run=new JMenu("运行查看");
        JMenuItem item=new JMenuItem("运行查看");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(trainPanel==null)
                    runUi(true);
                else
                    runUi(false);
            }
        });
        run.add(item);
        menuBar.add(run);
    }
    public Main(){
        this.frame=new JFrame("BP算法演示：by 高谦 :) ");
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }catch (Exception e){
            e.printStackTrace();
        }
        menuBar=new JMenuBar();
        this.frame.setJMenuBar(menuBar);
        initDataUi();
        initNetworkUi();
        initRunUi();
        dataSetUi(true);
        frame.setSize(1000,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
    }
    public static Double[][] addDe(Double[] xData){
        Double[][] data=new Double[1][];
        data[0]=xData;
        return data;
    }
    public static void main(String[] args) {
        Main app=new Main();
//        noGui();
    }
    public static void noGui(){
        Double[][] xdata=new Double[19][];
        Double[][] ydata=new Double[19][];
        for(Integer i=0;i<xdata.length;i++){
            xdata[i]=new Double[2];
            ydata[i]=new Double[1];
            xdata[i][0]=new Double(i);
            xdata[i][1]=new Double(i+1);
            ydata[i][0]=new Double(2*i+1);
        }
        // 构造数据集

        // 定义网络结构：
//        Layer inputLayer = new Layer(trainData.getEachDataInputSize());
        Layer inputLayer = new Layer(2);
        Layer hidden = new Layer(inputLayer, 100, Activation.SIGMOID);
        Layer outputLayer = new Layer(hidden, 1, Activation.NOACTIVATION);

        TrainData trainData=new TrainData(xdata,ydata);
        Train train = new Train(inputLayer, outputLayer, trainData);
        train.Init(5000, 0.0, 0.01);
        train.train(100, 500);
    }
}
