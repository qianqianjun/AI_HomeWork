package bp;

/**
 * @author 高谦
 * 神经元之间的连接线类。
 */
public class Line {
    private Neuron otherNeuron;
    private Double weight;

    public Line(Neuron other, Double weight) {
        this.otherNeuron = other;
        this.weight = weight;
    }

    public Neuron getOtherNeuron() {
        return otherNeuron;
    }

    public void setOtherNeuron(Neuron otherNeuron) {
        this.otherNeuron = otherNeuron;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
