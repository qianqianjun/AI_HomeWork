package bp;
public class Main {
    // 获取测测试数据的静态函数。
    public static Double[] getInputArray(Double begin,Double end,Integer num){
        Double distance=(end-begin)/num;
        Double[] result=new Double[num];
        Double start=begin;
        Integer i=0;
        while (start <end){
            result[i]=start;
            i+=1;
            start+=distance;
        }
        return result;
    }
    public static void main(String[] args) {
        Double[] xData = getInputArray(-Math.PI, Math.PI, 20);
        Double[] yData = new Double[20];
        for (Integer i = 0; i < 20; i++) {
            yData[i] = Math.sin(xData[i]);
        }

        Double[] x_data ={-Math.PI,-1.0/2.0*Math.PI,0.0,1.0/2.0*Math.PI,Math.PI};
        Double[] y_data=new Double[x_data.length];
        for(Integer i=0;i<x_data.length;i++){
            y_data[i]=Math.sin(x_data[i]);
        }

        Layer inputLayer = new Layer(x_data);
        Layer hidden = new Layer(inputLayer, 100, Activation.SIGMOID);
        Layer outputLayer = new Layer(hidden, y_data.length, Activation.NOACTIVATION);


        Train train = new Train(inputLayer, outputLayer, y_data);
        train.Init(10000, 0.0, 0.1);
        train.train(100, 500);
    }
}
