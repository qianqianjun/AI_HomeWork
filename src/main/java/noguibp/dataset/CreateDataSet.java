package noguibp.dataset;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class CreateDataSet {
    public static void createX1PlusX2() throws Exception{
        Scanner scanner=new Scanner(System.in);
        System.out.println("输入文件名称");
        String fileName=null;
        fileName=scanner.next();
        PrintWriter printWriter=new PrintWriter(new File(fileName));
        System.out.println("输入数据集个数");
        Integer num=scanner.nextInt();
        Double interval=new Double(0.5)/new Double(num);
        Double interval2=new Double(0.4)/new Double(num);
        for(Integer i=0;i<num;i++){
            Double a=interval*i;
            Double b=interval2*i;
            Double res=a+b;
            printWriter.printf("%.4f %.4f,%.4f\n",a,b,res);
        }
        printWriter.close();

    }

    public static void createSin() throws Exception{
        Scanner scanner=new Scanner(System.in);
        System.out.println("请输入文件名称");
        String fileName=null;
        fileName=scanner.next();
        PrintWriter printWriter=new PrintWriter(fileName);
        System.out.println("请输入数据集的大小");
        Integer num=scanner.nextInt();
        Double interval=0.5*Math.PI/new Double(num);
        /**  distance 用来表示输入的数量  **/
        Integer distance=1;
        List<Double> x=new ArrayList<>();
        List<Double> y=new ArrayList<>();
        for(Integer i=0;i<num;i++){
            Double a = interval * i+0.5*Math.PI;
            Double res = 0.5*Math.sin(a)+0.5;
            x.add(a);
            y.add(res);
            if((i+1)%distance==0) {
                for(int j=0;j<x.size()-1;j++){
                    printWriter.printf("%.4f ",x.get(j));
                }
                printWriter.printf("%.4f,",x.get(x.size()-1));
                for(int j=0;j<y.size()-1;j++){
                    printWriter.printf("%.4f ",y.get(j));
                }
                printWriter.printf("%.4f\n",y.get(y.size()-1));
                x.clear();
                y.clear();
            }
        }
        printWriter.close();
    }

    public static void createSquareX() throws Exception{
        Scanner scanner=new Scanner(System.in);
        System.out.println("请输入文件名称");
        String fileName=null;
        fileName=scanner.next();
        PrintWriter printWriter=new PrintWriter(fileName);
        System.out.println("请输入数据集的大小");
        Integer num=scanner.nextInt();
        Double interval=new Double(1)/new Double(num);
        Integer distance=50;
        List<Double> x=new ArrayList<>();
        List<Double> y=new ArrayList<>();
        for(Integer i=0;i<num*2;i++){
            Double a=interval*i-1;
            Double res=Math.pow(a,2);
            x.add(a);
            y.add(res);
            if((i+1)%distance==0) {
                for(int j=0;j<x.size()-1;j++){
                    printWriter.printf("%.4f ",x.get(j));
                }
                printWriter.printf("%.4f,",x.get(x.size()-1));
                for(int j=0;j<y.size()-1;j++){
                    printWriter.printf("%.4f ",y.get(j));
                }
                printWriter.printf("%.4f\n",y.get(y.size()-1));
                x.clear();
                y.clear();
            }
        }
        printWriter.close();
    }


    public static void main(String[] args) {
        try {
//            createX1PlusX2();
//            createSin();
            createSquareX();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
