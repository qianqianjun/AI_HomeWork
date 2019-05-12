package bp;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class CreateDataSet {
    public static void createX1PlusX2() throws Exception{
        PrintWriter printWriter=new PrintWriter(new File("./x1plusx2.txt"));
        Scanner scanner=new Scanner(System.in);
        System.out.println("输入训练集个数");
        Integer num=scanner.nextInt();
        Double interval=new Double(0.5)/new Double(num);
        Double interval2=new Double(0.4)/new Double(num);
        for(Integer i=0;i<num;i++){
            Double a=interval*i;
            Double b=interval2*i;
            Double res=a+b;
            printWriter.printf("%.4f %.4f %.4f\n",a,b,res);
        }
        printWriter.close();

    }

    public static void main(String[] args) {
        try {
            createX1PlusX2();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
