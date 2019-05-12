package bp;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author 高谦
 * 主界面上用于创建和修改数据集的 panel
 */
public class DatasetPanel extends JPanel {
    private Main mainWindow;
    public static Double[] getInputArray(Double begin,Double end,Integer num){
        Double distance=(end-begin)/num;
        Double[] result=new Double[num+1];
        Double start=begin;

        for(Integer i=0;i<result.length;i++){
            result[i]=start;
            start+=distance;
        }
        return result;
    }
    public DatasetPanel(Main mainWindow,Boolean isnew){
        this.mainWindow=mainWindow;
        this.setUi(isnew);
    }
    public Double getY(Double x){
        if(mainWindow.function.equals("sin")){
            return Math.sin(x);
        }
        if(mainWindow.function.equals("cos")){
            return Math.cos(x);
        }
        if(mainWindow.function.equals("linear")){
            return mainWindow.a*x+mainWindow.b;
        }
        if(mainWindow.function.equals("sin+cos")){
            return Math.sin(x)+Math.cos(x);
        }
        return x;
    }
    public void setUi(Boolean isnew){
        this.setLayout(null);
        JLabel label=null;
        if(isnew) {
            label = new JLabel("创建数据集合");
        }
        else{
            label=new JLabel("修改数据集合");
        }
        label.setBounds(10,20,400,40);
        label.setFont(new Font("雅黑",1,25));
        this.add(label);

        JLabel type=new JLabel("选择要拟合的单输入函数类型：");
        type.setBounds(20,70,400,40);
        type.setFont(new Font("宋体",1,20));
        this.add(type);
        final JPanel funset=new JPanel();
        final JButton sin=new JButton();
        sin.setPreferredSize(new Dimension(200,130));
        JButton cos=new JButton();
        cos.setPreferredSize(new Dimension(200,130));
        JButton sin_cos=new JButton();
        sin_cos.setPreferredSize(new Dimension(200,130));
        JButton linear=new JButton();
        linear.setPreferredSize(new Dimension(200,130));
        sin.setIcon(new ImageIcon("sin.png"));
        cos.setIcon(new ImageIcon("cos.png"));
        sin_cos.setIcon(new ImageIcon("sin+cos.png"));
        linear.setIcon(new ImageIcon("line.png"));
        funset.setBounds(0,120,1000,150);

        funset.add(sin);
        funset.add(cos);
        funset.add(sin_cos);
        funset.add(linear);
        this.add(funset);
        final JLabel current;
        if(isnew) {
            current = new JLabel("未选择函数");
        }
        else{
            String value="";
            if(mainWindow.function!="linear"){
                value="当前函数："+mainWindow.function;
            }
            else{
                value="当前函数："+mainWindow.a.toString()+"X + "+mainWindow.b.toString();
            }
            current=new JLabel(value);
        }
        current.setFont(new Font("宋体",1,20));
        current.setBounds(20,260,500,40);
        this.add(current);

        sin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.function="sin";
                current.setText("当前函数：正弦函数");
            }
        });
        cos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.function="cos";
                current.setText("当前函数：余弦函数");
            }
        });
        sin_cos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.function="sin+cos";
                current.setText("当前函数：sin + cos 函数");
            }
        });
        linear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.function="linear";
                String inputs=JOptionPane.showInputDialog(null,
                        "请输入自定义线性函数的斜率和偏移\n空格隔开如 2 2 。","输入参数",JOptionPane.OK_CANCEL_OPTION);
                if(inputs!=null) {
                    String[] set = inputs.split(" ");
                    if (set.length != 2) {
                        JOptionPane.showMessageDialog(null, "请您输入完整的信息",
                                "错误", JOptionPane.ERROR_MESSAGE);
                    } else {
                        mainWindow.a = Double.parseDouble(set[0]);
                        mainWindow.b = Double.parseDouble(set[1]);
                        current.setText("当前函数：" + mainWindow.a.toString() + "X + " + mainWindow.b.toString());
                    }
                }
            }
        });

        JLabel tip=new JLabel("初始化输入，输入x 的范围和在这个范围内平均分成的份数，程序将自动创建数据集进行训练");
        tip.setBounds(20,310,800,40);
        this.add(tip);

        final JLabel xstart=new JLabel("x起始");
        xstart.setFont(new Font("宋体",1,20));
        xstart.setBounds(20,360,60,40);
        this.add(xstart);
        final JTextField xStartField=new JTextField();
        xStartField.setBounds(80,360,200,40);
        xStartField.setFont(new Font("宋体",1,20));
        if(!isnew){
            xStartField.setText(mainWindow.begin.toString());
        }
        this.add(xStartField);

        final JLabel xend=new JLabel("x终止");
        xend.setFont(new Font("宋体",1,20));
        xend.setBounds(290,360,60,40);
        this.add(xend);
        final JTextField xEndField=new JTextField();
        xEndField.setBounds(360,360,200,40);
        xEndField.setFont(new Font("宋体",1,20));
        if(!isnew){
            xEndField.setText(mainWindow.ends.toString());
        }
        this.add(xEndField);

        final JLabel number=new JLabel("份数");
        number.setFont(new Font("宋体",1,20));
        number.setBounds(570,360,60,40);
        this.add(number);
        final JTextField numberField=new JTextField();
        numberField.setBounds(640,360,150,40);
        numberField.setFont(new Font("宋体",1,20));
        if(!isnew){
            numberField.setText(mainWindow.num.toString());
        }
        this.add(numberField);

        JButton createBtn=new JButton("创建");
        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String start=xStartField.getText();
                String end=xEndField.getText();
                String number=numberField.getText();
                if(start.equals("")||end.equals("")||number.equals("")){
                    JOptionPane.showMessageDialog(null,"请填写完整信息",
                            "提示",JOptionPane.ERROR_MESSAGE);
                }
                else{
                    if(mainWindow.function==null){
                        JOptionPane.showMessageDialog(null,
                                "请先选择要拟合的函数","提示",JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        try {
                            Double startVal = Double.parseDouble(start);
                            Double endVal = Double.parseDouble(end);
                            Integer numVal = Integer.parseInt(number);
                            mainWindow.begin = startVal;
                            mainWindow.ends = endVal;
                            mainWindow.num = numVal;
                            mainWindow.xData = getInputArray(startVal, endVal, numVal);
                            mainWindow.dataset=null;
                            mainWindow.textX = getInputArray(startVal, endVal, numVal - 1);
                            mainWindow.yData = new Double[mainWindow.xData.length];
                            for (Integer i = 0; i < mainWindow.xData.length; i++) {
                                mainWindow.yData[i] = getY(mainWindow.xData[i]);
                            }
                            mainWindow.textY = new Double[mainWindow.textX.length];
                            for (Integer i = 0; i < mainWindow.textX.length; i++) {
                                mainWindow.textY[i] = getY(mainWindow.textX[i]);
                            }

                            JOptionPane.showMessageDialog(null,"创建成功",
                                    "提示",JOptionPane.PLAIN_MESSAGE);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                            JOptionPane.showMessageDialog(null, "程序出现异常,检查输入格式是否正确！",
                                    "异常提示", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        createBtn.setBounds(800,360,150,40);
        createBtn.setFont(new Font("宋体",1,20));
        this.add(createBtn);

        JLabel other=new JLabel("其他函数：y= x1 + x2  （多输入，单输出）");
        other.setBounds(20,420,800,40);
        other.setFont(new Font("宋体",1,20));
        this.add(other);

        JButton importBtn=new JButton("导入数据集");
        importBtn.setBounds(20,480,200,40);
        importBtn.setFont(new Font("宋体",1,20));
        importBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser=new JFileChooser();
                fileChooser.showOpenDialog(null);
                fileChooser.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        if(f.isDirectory())
                            return true;
                        else{
                            if(f.getName().endsWith(".txt")){
                                return true;
                            }
                        }
                        return false;
                    }

                    @Override
                    public String getDescription() {
                        return "文本文件";
                    }
                });
                mainWindow.dataset=new ArrayList<>();
                File file=fileChooser.getSelectedFile();
                if(file!=null) {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(file);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
                        while(reader.ready()){
                            String temp=reader.readLine();
                            mainWindow.dataset.add(temp);
                        }
                        mainWindow.xData=null;
                        JOptionPane.showMessageDialog(null,"导入数据集成功！",
                                "提示",JOptionPane.PLAIN_MESSAGE);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        this.add(importBtn);
    }
}
