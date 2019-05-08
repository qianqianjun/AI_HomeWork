package bp;

/**
 * @author 高谦
 * 激活函数的实现
 */
public class Activation {
    public static Integer SIGMOID = 1;
    public static Integer NOACTIVATION=3;
    public static Integer TANH=2;
    private static Double sigmoid(Double y, Boolean isPositive) {
        if (isPositive) {
            return 1.0 / (1 + Math.exp(-y));
        } else {
            return y * (1 - y);
        }
    }

    private static Double tanh(Double y, Boolean isPositive) {
        if (isPositive) {
            return (Math.exp(y)- Math.exp(-y) )/(Math.exp(-y)+Math.exp(y));
        } else {
            return 1-y*y;
        }
    }

    private static Double none(Double y,Boolean ispositive){
        if(ispositive){
            return y;
        }
        else{
            return new Double(1);
        }
    }
    public static Double activate(Integer type, Double y, Boolean ispositive) {
        if (type == 1) {
            return sigmoid(y, ispositive);
        }
        if (type == 2) {
            return tanh(y,ispositive);
        }
        if(type==3){
            return none(y,ispositive);
        }
        return new Double(0);
    }
}
