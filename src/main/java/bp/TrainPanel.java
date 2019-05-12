package bp;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 高谦
 *  用于展示训练，回想，结果的 panel
 */
public class TrainPanel extends JPanel {
    private Main mainWindow;
    private Layer inputLayer;
    private Layer outputLayer;
    private Train train;
    private TrainData trainData;
    private Boolean isRun;
    private ResultDisplayPanel recallPanel;
    private ResultDisplayPanel lossPanel;
    private ResultDisplayPanel generalizationPanel;
    public TrainPanel(Main mainWindow){
        this.mainWindow=mainWindow;
        this.isRun=false;
        this.setUi();
    }

    public static Double[][] addDimension(Double[] data){
        Double[][] newData=new Double[data.length][];
        for(Integer i=0;i<data.length;i++) {
            newData[i] = new Double[1];
            newData[i][0]=data[i];
        }
        return newData;
    }
    public void debug(){
        List<Layer> layers=new ArrayList<>();
        DisplayLayer input=mainWindow.networkCanvas.getLayer(0);
        layers.add(new Layer(input.outputsize));
        Integer layerNum=mainWindow.networkCanvas.getLayerNum();
        DisplayLayer currentLayer=input.nextLayer;
        for(Integer i=1;i<layerNum;i++){
            layers.add(new Layer(layers.get(i-1),currentLayer.outputsize,currentLayer.activation));
            currentLayer=currentLayer.nextLayer;
        }
    }
    public void createLayers(){
        List<Layer> layers=new ArrayList<>();
        DisplayLayer current=mainWindow.networkCanvas.inputLayer;
        layers.add(new Layer(current.outputsize));
        Integer j=1;
        while(current.nextLayer!=null){
            layers.add(new Layer(layers.get(j-1),current.nextLayer.outputsize,current.nextLayer.activation));
            j++;
            current=current.nextLayer;
        }

        /** debug **/
        Integer layerNum=mainWindow.networkCanvas.getLayerNum();

        System.out.println(layers.get(0).getOutputSize());
        System.out.println("()()()()()()()()()");
        for(Integer i=1;i<layerNum;i++){
            System.out.print(layers.get(i).getOutputSize());
            System.out.print(" ");
            if(layers.get(i).getActivation()==0){
                System.out.println("sigmoid");
            }
            if(layers.get(i).getActivation()==1){
                System.out.println("tanh");
            }
            if(layers.get(i).getActivation()==2) {
                System.out.println("none");
            }
        }

        this.inputLayer=layers.get(0);
        this.outputLayer=layers.get(layers.size()-1);


        /** change **/
//        inputLayer = new Layer(2);
//        Layer hidden = new Layer(inputLayer, 100, Activation.SIGMOID);
//        outputLayer = new Layer(hidden, 1, Activation.NOACTIVATION);
    }
    public void setDataset(){
        if(mainWindow.dataset!=null){
            if(inputLayer.getOutputSize()!=mainWindow.dataset.size()-1){
                JOptionPane.showMessageDialog(null,"致命错误，神经元无法接收数据集中的数，请修改网络输入层",
                        "提示",JOptionPane.PLAIN_MESSAGE);
            }else{
                /**
                String[] row=mainWindow.dataset.get(0).split(" ");
                Integer trainSize=row.length;
                Integer parmNum=mainWindow.dataset.size()-1;
                String[][] dataStr=new String[parmNum+1][];
                for(Integer i=0;i<dataStr.length;i++){
                    dataStr[i]=mainWindow.dataset.get(i).split(" ");
                }
                Double[][] data=new Double[trainSize][];
                for(Integer i=0;i<data.length;i++){
                    data[i]=new Double[parmNum];
                    for(Integer j=0;j<parmNum;j++){
                        data[i][j]=Double.parseDouble(dataStr[j][i]);
                    }
                }

                Double[] y=new Double[trainSize];
                for(Integer i=0;i<y.length;i++){
                    y[i]=Double.parseDouble(dataStr[parmNum][i]);
                }*/

                Double[][] xdata=new Double[19][];
                Double[][] ydata=new Double[19][];
                for(Integer i=0;i<xdata.length;i++){
                    xdata[i]=new Double[2];
                    ydata[i]=new Double[1];
                    xdata[i][0]=new Double(i);
                    xdata[i][1]=new Double(i+1);
                    ydata[i][0]=new Double(2*i+1);
                }
                this.trainData=new TrainData(xdata,ydata);
                System.out.println("这里执行了");
            }
        }else{
            Integer inputSize=inputLayer.getOutputSize();
            Double[] xData=mainWindow.xData;
            Double[] yData=mainWindow.yData;
            Integer size=xData.length/inputSize;
            if(size==0){
                JOptionPane.showMessageDialog(null,"致命错误：数据集太小！输入层的输入无法满足");
            }else{
                Double[][] x=new Double[size][];
                Double[][] y=new Double[size][];
                for(Integer i=0;i<x.length;i++){
                    x[i]=new Double[inputSize];
                    for(Integer j=0;j<inputSize;j++){
                        x[i][j]=xData[i*inputSize+j];
                    }
                    y[i]=new Double[inputSize];
                    for(Integer j=0;j<inputSize;j++){
                        y[i][j]=yData[i*inputSize+j];
                    }
                }
                this.trainData=new TrainData(x,y);
                System.out.println("哈哈哈啊");
            }
        }
    }

    public void setUi(){
        this.setLayout(null);
        JButton start=new JButton("开始训练");
        start.setBounds(5,10,120,40);
        start.setFont(new Font("雅黑",1,20));
        this.add(start);

        recallPanel=new ResultDisplayPanel("recall result",485,320);
        recallPanel.setBounds(5,60,490,325);
        this.add(recallPanel);

        generalizationPanel=new ResultDisplayPanel("generalization result",465,320);
        generalizationPanel.setBounds(500,60,470,325);
        this.add(generalizationPanel);

        lossPanel=new ResultDisplayPanel("loss result",960,320);
        lossPanel.setBounds(5,390,965,325);
        this.add(lossPanel);

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isRun){
                    JOptionPane.showMessageDialog(null,"请等待当前训练结束","提示",
                            JOptionPane.ERROR_MESSAGE);
                }
                else{
                    isRun=true;
                    createLayers();
                    /** debug **/
                    mainWindow.dataset=new ArrayList<>();
                    setDataset();
                    Train train = new Train(inputLayer, outputLayer, trainData);
                    train.Init(5000, 0.0, 0.01);
                    train.train(100, 500);
                    isRun=false;
                }
            }
        });
    }
}
