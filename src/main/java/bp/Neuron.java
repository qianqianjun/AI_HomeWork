package bp;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 高谦
 * 对于神经元定义类。
 */
public class Neuron {
    private List<Line> neuronInputs;
    private Double neuronOutput;
    // 表示 d（loss）/neuronOutput
    private Double dy;
    public Neuron(){
        this.neuronInputs=new ArrayList<>();
    }

    public List<Line> getNeuronInputs() {
        return neuronInputs;
    }

    public void setNeuronInputs(List<Line> neuronInputs) {
        this.neuronInputs = neuronInputs;
    }

    public Double getNeuronOutput() {
        return neuronOutput;
    }

    public void setNeuronOutput(Double neuronOutput) {
        this.neuronOutput = neuronOutput;
    }

    public void addNeuronInput(Line line) {
        this.neuronInputs.add(line);
    }

    public void setDy(Double dy) {
        this.dy=dy;
    }

    public Double getDy() {
        return dy;
    }
}
