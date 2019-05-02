package bp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Layer {
    private Integer outputSize;
    private Layer otherLayer;
    private List<Double> output;
    private Integer activation;
    private List<Neuron> neurons;
    private Layer nextLayer;
    private Layer preLayer;

    public static Double randomDouble(Integer precision) {
        String format = "%." + precision.toString() + "f";
        Random random = new Random(System.currentTimeMillis());
        return Double.parseDouble(String.format(format, random.nextDouble()));
    }

    // 第一种个构建方式，输入一个数据，用来建立输入层。
    public Layer(Double[] input) {
        if(input.length==0)
            throw new RuntimeException("请检查您的输入");
        this.output = new ArrayList<>();
        for (Double item : input) {
            this.output.add(item);
        }
        this.outputSize = input.length;
        this.neurons = new ArrayList<>();
        for (Integer i = 0; i < input.length; i++) {
            this.neurons.add(new Neuron());
            this.neurons.get(i).setNeuronOutput(input[i]);
        }
    }

    // 第二种构建方式，输入上一层的数据，用来建立中间层或者输出层。
    public Layer(Layer preLayer, Integer outputSize, Integer activation) {
        preLayer.setNextLayer(this);
        this.setPreLayer(preLayer);
        this.otherLayer = preLayer;
        this.outputSize = outputSize;
        this.activation = activation;
        this.neurons = new ArrayList<>();
        this.output=new ArrayList<>();
        for (Integer i = 0; i < outputSize; i++) {
            this.neurons.add(new Neuron());
            for (Integer j = 0; j < this.otherLayer.getOutputSize(); j++) {
                Line line = new Line(this.otherLayer.neurons.get(j), randomDouble(4));
                this.neurons.get(i).addNeuronInput(line);
            }
        }
    }

    // 得到单层神经网络的输出矩阵
    public List<Double> getOutput() {
        this.output=new ArrayList<>();
        for (Neuron neuron : this.neurons)
            this.output.add(neuron.getNeuronOutput());
        return this.output;
    }

    // 得到单层神经网络的输出个数。
    public Integer getOutputSize() {
        return this.outputSize;
    }

    public void setNextLayer(Layer nextLayer) {
        this.nextLayer = nextLayer;
    }

    public Layer getNextLayer() {
        return nextLayer;
    }

    public boolean hasNextLayer() {
        return this.nextLayer != null;
    }

    public void setOutputSize(Integer outputSize) {
        this.outputSize = outputSize;
    }

    public Layer getOtherLayer() {
        return otherLayer;
    }

    public void setOtherLayer(Layer otherLayer) {
        this.otherLayer = otherLayer;
    }

    public void setOutput(List<Double> output) {
        this.output = output;
    }

    public Integer getActivation() {
        return activation;
    }

    public void setActivation(Integer activation) {
        this.activation = activation;
    }

    public List<Neuron> getNeurons() {
        return neurons;
    }

    public void setNeurons(List<Neuron> neurons) {
        this.neurons = neurons;
    }
    public Layer getPreLayer() {
        return preLayer;
    }

    public void setPreLayer(Layer preLayer) {
        this.preLayer = preLayer;
    }

    public boolean hasPreLayer() {
        return this.getPreLayer()!=null;
    }


    public void showInfo() {
        for(Integer i=0;i<this.neurons.size();i++){
            System.out.println("neuron index of current layer："+i);
            List<Line> lines=this.neurons.get(i).getNeuronInputs();
            for(Line line:lines){
                System.out.println("weight："+line.getWeight()+" v："+line.getOtherNeuron().getNeuronOutput());
            }
        }
        System.out.println("-------------------------------------");
    }
}
