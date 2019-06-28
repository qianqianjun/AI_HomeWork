package noguibp.train;
import noguibp.structure.*;
import org.jfree.data.xy.XYSeries;
import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 高谦
 * 训练类，用于神经网络训练的控制。
 */

public class Train{
    private Layer begin;
    private Layer end;
    private TrainData trainData;
    private Double[] preduction;
    private Double learnRate;
    private Double loss;
    private Integer totalStep;
    private Double minLoss;
    private Integer trainLogInterval;
    private Integer recallInterval;
    private TrainResult trainResult;

    /**
     * 构造函数
     * @param inputLayer 需要进行训练的神经网络的输入层
     * @param outputLayer  需要进行训练的神经网络的输出层
     * @param trainData 这里是训练数据类。
     */
    public Train(Layer inputLayer, Layer outputLayer, TrainData trainData) {
        this.begin = inputLayer;
        this.end = outputLayer;
        this.trainData=trainData;
        if (outputLayer.getOutputSize() != trainData.getEachPreductionOutputSize()) {
            throw new RuntimeException("致命错误：输出层的输出个数与预期输出个数不符！");
        }
        this.trainResult=new TrainResult();
    }

    /**
     * 正向传播函数实现
     * 没有参数，参数使用的是网络中的数据。
     */
    private void positive() {
        Layer currentLayer = this.begin;
        while (currentLayer.hasNextLayer()) {
            Layer nextLayer = currentLayer.getNextLayer();
            List<Neuron> nextLayerNeurons = nextLayer.getNeurons();
            for (Integer i = 0; i < nextLayerNeurons.size(); i++) {
                Neuron currentNeuron = nextLayerNeurons.get(i);
                List<Line> lines = currentNeuron.getNeuronInputs();
                Double result = new Double(0);
                for (Line line : lines) {
                    Double output = line.getOtherNeuron().getNeuronOutput();
                    Double weight = line.getWeight();
                    result += weight * output;
                }
                currentNeuron.setNeuronOutput(Activation.activate(nextLayer.getActivation(), result, true));
            }
            currentLayer = currentLayer.getNextLayer();
        }
//        List<Double> output = this.end.getOutput();
//        Double los = new Double(0);
//        for (Integer i = 0; i < output.size(); i++) {
//            los += Math.pow(output.get(i) - this.preduction[i], 2);
//        }
//        loss = 0.5 * los;
//        loss = los;
    }

    /**
     * 反向传播函数实现
     * 无参数，使用的是神经网络中的参数。
     */
    private void reverse(Double[] dys) {
        // 计算输出层的神经元对于数据的输出：
        Layer output = this.end;
        List<Neuron> neurons = output.getNeurons();
        for (Integer i = 0; i < neurons.size(); i++) {
            Neuron currentNeuron = neurons.get(i);
            Double dy = (currentNeuron.getNeuronOutput() - preduction[i]) *
                    Activation.activate(output.getActivation(), currentNeuron.getNeuronOutput(), false);
            currentNeuron.setDy(dy);
        }

//        Layer output = this.end;
//        List<Neuron> neurons = output.getNeurons();
//        for (Integer i = 0; i < neurons.size(); i++) {
//            Neuron currentNeuron = neurons.get(i);
//            currentNeuron.setDy(dys[i]);
//        }

        Layer currentLayer = this.end.getPreLayer();
        while (currentLayer != null) {
            Layer nextLayer = currentLayer.getNextLayer();
            List<Neuron> nextNeurons = nextLayer.getNeurons();
            for (Integer n = 0; n < currentLayer.getNeurons().size(); n++) {
                Double result = new Double(0);
                for (Integer i = 0; i < nextNeurons.size(); i++) {
                    result += nextNeurons.get(i).getDy() * nextNeurons.get(i).getNeuronInputs().get(n).getWeight();
                }
                Neuron currentNeuron = currentLayer.getNeurons().get(n);
                currentNeuron.setDy(result * Activation.activate(1, currentNeuron.getNeuronOutput(), false));
            }
            for (Integer n = 0; n < nextNeurons.size(); n++) {
                for (int i = 0; i < nextNeurons.get(n).getNeuronInputs().size(); i++) {
                    Line currentLine = nextNeurons.get(n).getNeuronInputs().get(i);
                    Double weight = currentLine.getWeight();
                    Double dw = currentLine.getOtherNeuron().getNeuronOutput() * nextNeurons.get(n).getDy();
                    weight = weight - this.learnRate * dw;
                    currentLine.setWeight(weight);
                }
            }
            currentLayer = currentLayer.getPreLayer();
        }
    }

    /**
     * 无特殊作用，用于检查网络的情况
     * Debug 的时候使用
     */
    private void showInfo() {
        Layer currentLayer = this.begin;
        while (currentLayer != null) {
            currentLayer.showInfo();
            currentLayer = currentLayer.getNextLayer();
        }
    }

    /**
     * 回想过程
     * 这里主要对训练集进行测试，如果测试效果变差，那就说明网络是错误的。
     * 如果测试的效果很好，也有可能是过拟合了，测试集上不一定就好。
     */

    public void recall() {
        System.out.println("--------------------------");
        System.out.println("预期输出：");
        for(Integer i=0;i<this.trainData.getData().length;i++){
            this.preduction=this.trainData.getPreductionOutput()[i];
            this.begin.setLayerTrainData(this.trainData.getData()[i]);
            this.positive();
            for (Double item : this.preduction) {
                System.out.printf("%.5f ", item);
            }
            System.out.println();
            List<Neuron> neurons = this.end.getNeurons();
            for (Neuron neuron : neurons) {
                System.out.printf("%.5f ", neuron.getNeuronOutput());
            }
            System.out.println("\n===========");
        }

    }

    public Double getLoss() {
        return loss;
    }

    public void Init(int totalstep,int trainLogInterval,double minloss, double learnRate) {
        this.learnRate = learnRate;
        this.totalStep = totalstep;
        this.minLoss = minloss;
        this.trainLogInterval=trainLogInterval;
    }

    /** 为recall 曲线准备数据  **/
    public void setRecallGroup(){
        List<XYSeries> group;
        group=new ArrayList<>();
        List<Double> recallOut=new ArrayList<>();
        List<Double> preductionOut=new ArrayList<>();
        List<Double> input=new ArrayList<>();
        for(Integer i=0;i<this.trainData.getData().length;i++) {
            this.preduction = this.trainData.getPreductionOutput()[i];
            this.begin.setLayerTrainData(this.trainData.getData()[i]);
            this.positive();
            List<Double> temp=this.end.getOutput();
            for(Double t:temp){
                recallOut.add(t);
            }
            for(Double t:this.preduction){
                preductionOut.add(t);
            }
            for(Double t:this.trainData.getData()[i]){
                input.add(t);
            }
        }
        XYSeries xySeriesProductionOutput=new XYSeries("preduction output");
        XYSeries xySeriesNeuronOutput=new XYSeries("neuron output");

        if(input.size()!=recallOut.size()) {
            for (Integer i = 0; i < recallOut.size(); i++) {
                xySeriesNeuronOutput.add(i, recallOut.get(i));
                xySeriesProductionOutput.add(i, preductionOut.get(i));
            }
        }else{
            for (Integer i = 0; i < recallOut.size(); i++) {
                xySeriesNeuronOutput.add(input.get(i), recallOut.get(i));
                xySeriesProductionOutput.add(input.get(i), preductionOut.get(i));
            }
        }
        group.add(xySeriesNeuronOutput);
        group.add(xySeriesProductionOutput);
        this.trainResult.setRecallSet(group);
    }

    /** 为泛化做准备  **/
    private void setGeneralizationGroup() {
        List<XYSeries> group;
        group=new ArrayList<>();
        List<Double> generation=new ArrayList<>();
        List<Double> preductionOut=new ArrayList<>();
        List<Double> input=new ArrayList<>();
        for(Integer i=0;i<this.trainData.testxData.length;i++) {
            this.preduction = this.trainData.testyData[i];
            this.begin.setLayerTrainData(this.trainData.testxData[i]);
            this.positive();
            List<Double> temp=this.end.getOutput();
            for(Double t:temp){
                generation.add(t);
            }
            for(Double t:this.preduction){
                preductionOut.add(t);
            }
            for(Double t:this.trainData.getData()[i]){
                input.add(t);
            }
        }
        XYSeries xySeriesProductionOutput=new XYSeries("preduction output");
        XYSeries xySeriesNeuronOutput=new XYSeries("neuron output");

        if(input.size()!=generation.size()) {
            for (Integer i = 0; i < generation.size(); i++) {
                xySeriesNeuronOutput.add(i, generation.get(i));
                xySeriesProductionOutput.add(i, preductionOut.get(i));
            }
        }else{
            for (Integer i = 0; i < generation.size(); i++) {
                xySeriesNeuronOutput.add(input.get(i), generation.get(i));
                xySeriesProductionOutput.add(input.get(i), preductionOut.get(i));
            }
        }
        group.add(xySeriesNeuronOutput);
        group.add(xySeriesProductionOutput);
        this.trainResult.setGeneralizationSet(group);
    }

    /** 为loss 曲线做准备 **/
    private void setLossGroup(XYSeries lossLine) {
        this.trainResult.setLossSet(lossLine);
    }


    /** 训练函数  **/
    public void train() {
        Double Minloss=Double.MAX_VALUE;
        XYSeries lossLine=new XYSeries("loss value");
        for (Integer k = 0; k < this.totalStep; k++) {
            Double lossTemp=new Double(0);
            Double[] dys=new Double[this.trainData.getData()[0].length];
            for(int j=0;j<dys.length;j++){
                dys[j]=new Double(0);
            }
            for(Integer i=0;i<this.trainData.getData().length;i++) {
                this.preduction = this.trainData.getPreductionOutput()[i];
                this.begin.setLayerTrainData(this.trainData.getData()[i]);
                this.positive();

                List<Double> output = this.end.getOutput();
                for (Integer j = 0; j < output.size(); j++) {
                    lossTemp += Math.pow(output.get(j) - this.preduction[j], 2);
                }

                for(int j=0;j<output.size();j++){
                    dys[j]+=(output.get(j) - preduction[j]) *
                            Activation.activate(this.end.getActivation(), output.get(j), false);
                }
                this.reverse(dys);
            }
//            this.reverse(dys);
            loss=0.5*lossTemp;
            if(Minloss<loss){
                this.learnRate=this.learnRate/5;
                Minloss=loss;
            }
            else{
                Minloss=loss;
            }
            if (this.getLoss() < minLoss) {
                this.recall();
                System.out.println("达到最小精度 ，训练结束！");
                break;
            }
            if(k%10==0){
                lossLine.add(k,loss);
            }
            if ((k + 1) % trainLogInterval == 0) {
                System.out.printf("[训练过程] step：%10d  loss : %.10f\n", k, loss);
            }
        }
        setRecallGroup();
        setLossGroup(lossLine);
        setGeneralizationGroup();
        storeTrainResult();
    }

    private void storeTrainResult() {
        try {
            FileOutputStream outputStream = new FileOutputStream(new File("trainResult.dat"));
            ObjectOutputStream writer=new ObjectOutputStream(outputStream);
            writer.writeObject(this.trainResult);
            writer.close();
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"训练结果无法保存！请查看日志！",
                    "提示",JOptionPane.ERROR_MESSAGE);
        }
    }
}