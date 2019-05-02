package bp;
import java.util.List;

/**
 * @author 高谦
 * 训练类，用于神经网络训练的控制。
 */

public class Train {
    private Layer begin;
    private Layer end;
    private Double[] preduction;
    private Double learnRate;
    private Double loss;
    private Integer totalStep;
    private Double minLoss;

    public Train(Layer inputLayer, Layer outputLayer, Double[] preductionOutput) {
        this.begin = inputLayer;
        this.end = outputLayer;
        this.preduction = preductionOutput;
        if (outputLayer.getOutputSize() != preductionOutput.length) {
            throw new RuntimeException("致命错误：输出层的输出个数与预期输出个数不符！");
        }
    }

    // 正向传播函数：
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

    // 反向传播函数：
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

    private void showInfo() {
        Layer currentLayer = this.begin;
        while (currentLayer != null) {
            currentLayer.showInfo();
            currentLayer = currentLayer.getNextLayer();
        }
    }

    public void recall() {
        System.out.println("-----------recall---------------");
        System.out.println("预期输出：");
        for (Double item : this.preduction) {
            System.out.printf("%.5f ", item);
        }
        System.out.println();
        List<Neuron> neurons = this.end.getNeurons();
        for (Neuron neuron : neurons) {
            System.out.printf("%.5f ", neuron.getNeuronOutput());
        }
        System.out.println();
    }

    public Double getLoss() {
        return loss;
    }

    public void Init(int totalstep, double minloss, double learnRate) {
        this.learnRate = learnRate;
        this.totalStep = totalstep;
        this.minLoss = minloss;
    }

    public void train(Integer trainLogInterval, Integer recallInterval) {
        for (Integer i = 0; i < this.totalStep; i++) {
            this.positive();
            this.reverse();
            if ((i + 1) % trainLogInterval == 0) {
                System.out.printf("[训练过程] step：%d  loss : %.10f\n", i, loss);
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