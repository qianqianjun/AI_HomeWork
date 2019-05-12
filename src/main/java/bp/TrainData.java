package bp;

/**
 * @author 高谦
 * 实现简单的算法。
 */
public class TrainData {
    private Integer batchSize;
    private Double[][] data;
    private Double[][] preductionOutput;
    public TrainData(Double[][] x,Double[][] y){
        this.data=x;
        this.preductionOutput=y;
        this.batchSize=x.length;
        if(this.batchSize==0){
            throw new RuntimeException("请您输入训练数据");
        }
        if(this.data.length!=this.preductionOutput.length){
            throw new RuntimeException("致命错误：输入与输出的个数不一致！");
        }
    }
    public Double[][] getData() {
        return data;
    }

    public Double[][] getPreductionOutput() {
        return preductionOutput;
    }

    public Integer getEachDataInputSize(){
        return this.data[0].length;
    }
    public Integer getEachPreductionOutputSize() {
        return this.preductionOutput[0].length;
    }
}
