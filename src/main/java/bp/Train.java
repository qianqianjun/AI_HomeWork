package bp;
import javafx.scene.control.TableRow;

import java.util.List;

/**
 * @author 高谦
 * 训练类，用于神经网络训练的控制。
 */

public class Train {
    private Layer begin;
    private Layer end;
    private TrainData trainData;
    private Double[] preduction;
    private Double learnRate;
    private Double loss;
    private Integer totalStep;
    private Double minLoss;

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

        // 计算损失函数：
        List<Double> output = this.end.getOutput();
        Double loss = new Double(0);
        for (Integer i = 0; i < output.size(); i++) {
            loss += Math.pow(output.get(i) - this.preduction[i], 2);
        }
        loss = 1 / 2.0 * loss;
        this.loss = loss;
    }

    /**
     * 反向传播函数实现
     * 无参数，使用的是神经网络中的参数。
     */
    private void reverse() {
        // 计算输出层的神经元对于数据的输出：
        Layer output = this.end;
        List<Neuron> neurons = output.getNeurons();
        for (Integer i = 0; i < neurons.size(); i++) {
            Neuron currentNeuron = neurons.get(i);
            Double dy = (currentNeuron.getNeuronOutput() - preduction[i]) *
                    Activation.activate(output.getActivation(), currentNeuron.getNeuronOutput(), false);
            currentNeuron.setDy(dy);
        }

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
     *  无特殊作用，用于检查网络的情况
     *  Debug 的时候使用
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
        System.out.println("-----------recall---------------");
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

    public void Init(int totalstep, double minloss, double learnRate) {
        this.learnRate = learnRate;
        this.totalStep = totalstep;
        this.minLoss = minloss;
    }

    /**
     * 每一次的训练过程
     * 首先需要初始化这一次的训练集输入
     * 然后初始化这一次的训练集标准输出
     * 然后进行正向传播和反向传播进行训练。
     */
    private void debug(){
        for(Double[] item:this.trainData.getData()){
            System.out.print(item[0]);
            System.out.print(" ");
            System.out.print(item[1]);
            System.out.print(" | ");
        }
        System.out.println();
        for(Double[] item:this.trainData.getPreductionOutput()){
            System.out.print(item[0]);
            System.out.print(" | ");
        }
        System.out.println();
        throw new RuntimeException("操你妈");
    }
    public void eachTrain(){
        for(Integer i=0;i<this.trainData.getData().length;i++) {
            this.preduction = this.trainData.getPreductionOutput()[i];
            this.begin.setLayerTrainData(this.trainData.getData()[i]);
            this.positive();
            this.reverse();
        }
    }

    /**
     * 真正的训练过程
     * @param trainLogInterval  打印训练日志的间隔
     * @param recallInterval 回想的间隔
     */
    public void train(Integer trainLogInterval, Integer recallInterval) {
        Double Minloss=100000000.0;
        System.out.println(totalStep);
        System.out.println(learnRate);
        System.out.println(minLoss);
        for (Integer i = 0; i < this.totalStep; i++) {
            eachTrain();
            if ((i + 1) % trainLogInterval == 0) {
                System.out.printf("[训练过程] step：%d  loss : %.10f\n", i, loss);
                if(Minloss<loss){
                    this.learnRate=this.learnRate/2;
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
            }
            if ((i + 1) % recallInterval == 0) {
                this.recall();
            }
        }
    }
}