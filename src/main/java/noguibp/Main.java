package noguibp;
import noguibp.structure.Activation;
import noguibp.structure.Layer;
import noguibp.train.Train;
import noguibp.train.TrainData;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * @author 高谦
 * 主面板，用于启动程序运行
 */
public class Main {
    public static Object[] loadData(String trainName){
        Double[][] xData;
        Double[][] yData;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(trainName));
            ArrayList<String> temp=new ArrayList<>();
            String buf;
            while ((buf=reader.readLine())!=null){
                temp.add(buf);
            }
            reader.close();
            String line=temp.get(0);
            String[] parm=line.split(",");
            Integer inputSize=parm[0].split(" ").length;
            Integer outputSize=parm[1].split(" ").length;

            xData=new Double[temp.size()][];
            yData=new Double[temp.size()][];
            for(Integer i=0;i<xData.length;i++){
                xData[i]=new Double[inputSize];
                yData[i]=new Double[outputSize];
                String[] data=temp.get(i).split(",");
                String[] input=data[0].split(" ");
                String[] output=data[1].split(" ");
                for(Integer j=0;j<inputSize;j++){
                    xData[i][j]=Double.parseDouble(input[j]);
                }
                for(Integer j=0;j<outputSize;j++){
                    yData[i][j]= Double.parseDouble(output[j]);
                }
            }
            Object[] res=new Object[2];
            res[0]=xData;
            res[1]=yData;
            reader.close();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void trainX1plusX2(){
        Double[][] xData=null;
        Double[][] yData=null;
        Double[][] txData=null;
        Double[][] tyData=null;
        Object[] res=loadData("x1plusx2.txt");
        xData=(Double[][]) res[0];
        yData=(Double[][]) res[1];

        Object[] tres=loadData("x1plusx2test.txt");
        txData=(Double[][]) tres[0];
        tyData=(Double[][]) tres[1];

        Layer inputLayer = new Layer(2);
        Layer hidden = new Layer(inputLayer, 10, Activation.SIGMOID);
        Layer outputLayer = new Layer(hidden, 1, Activation.NOACTIVATION);

        TrainData trainData=new TrainData(xData,yData,txData,tyData);
        Train train = new Train(inputLayer, outputLayer, trainData);
        train.Init(50000,100,0.0,0.05);
        train.train();
    }

    public static void trainSin() {
        Double[][] xData=null;
        Double[][] yData=null;
        Double[][] txData=null;
        Double[][] tyData=null;
        Object[] res=loadData("trainSin.txt");
        xData=(Double[][]) res[0];
        yData=(Double[][]) res[1];

        Object[] tres=loadData("testSin.txt");
        txData=(Double[][]) tres[0];
        tyData=(Double[][]) tres[1];

        Layer inputLayer = new Layer(1);
//        Layer hidden = new Layer(inputLayer, 10, Activation.TANH);
//        Layer outputLayer = new Layer(hidden, 1, Activation.TANH);
        Layer hidden = new Layer(inputLayer, 10, Activation.TANH);
        Layer outputLayer = new Layer(hidden, 1, Activation.TANH);
        TrainData trainData=new TrainData(xData,yData,txData,tyData);
        Train train = new Train(inputLayer, outputLayer, trainData);
//        train.Init(50000,100,0.0,0.1);
        train.Init(50000,100,0.0,0.2);
        train.train();
    }

    public static void trainSquare(){
        Double[][] xData=null;
        Double[][] yData=null;
        Double[][] txData=null;
        Double[][] tyData=null;
        Object[] res=loadData("trainSquare.txt");
        xData=(Double[][]) res[0];
        yData=(Double[][]) res[1];

        Object[] tres=loadData("testSquare.txt");
        txData=(Double[][]) tres[0];
        tyData=(Double[][]) tres[1];

        Layer inputLayer = new Layer(50);
        Layer hidden = new Layer(inputLayer, 30, Activation.SIGMOID);
        Layer outputLayer = new Layer(hidden, 50, Activation.SIGMOID);
        TrainData trainData=new TrainData(xData,yData,txData,tyData);
        Train train = new Train(inputLayer, outputLayer, trainData);
//        train.Init(50000,100,0.0,0.1);
        train.Init(50000,100,0.0,0.2);
        train.train();
    }
    public static void main(String[] args) {
//        trainX1plusX2();
//        trainSin();
        trainSquare();
    }
}
