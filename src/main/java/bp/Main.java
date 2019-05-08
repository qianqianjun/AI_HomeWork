package bp;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class Main {
    private JFrame frame;
    private JMenuBar menuBar;
    private String function;
    private Double a;
    private Double b;
    private Double begin;
    private Double ends;
    private Integer num;
    private Double[] xData;
    private Double[] yData;
    private Double[] textX;
    private Double[] textY;
    private NetworkPanel networkPanel;
    // 获取测测试数据的静态函数。
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
    public Double getY(Double x){
        if(this.function.equals("sin")){
            return Math.sin(x);
        }
        if(this.function.equals("cos")){
            return Math.cos(x);
        }
        if(this.function.equals("linear")){
            return a*x+b;
        }
        if(this.function.equals("sin+cos")){
            return Math.sin(x)+Math.cos(x);
        }
        return x;
    }
    private void dataSetUi(Boolean isnew){
        JPanel panel=new JPanel();
        panel.setLayout(null);
        JLabel label=null;
        if(isnew) {
            label = new JLabel("创建数据集合");
        }
        else{
            label=new JLabel("修改数据集合");
        }
        label.setBounds(10,20,400,40);
        label.setFont(new Font("雅黑",1,25));
        panel.add(label);

        JLabel type=new JLabel("选择要拟合函数的类型");
        type.setBounds(20,70,400,40);
        type.setFont(new Font("宋体",1,20));
        panel.add(type);
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
        panel.add(funset);
        final JLabel current;
        if(isnew) {
            current = new JLabel("未选择函数");
        }
        else{
            String value="";
            if(this.function!="linear"){
                value="当前函数："+this.function;
            }
            else{
                value="当前函数："+this.a.toString()+"X + "+this.b.toString();
            }
            current=new JLabel(value);
        }
        current.setFont(new Font("宋体",1,20));
        current.setBounds(20,260,500,40);
        panel.add(current);

        sin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                function="sin";
                current.setText("当前函数：正弦函数");
            }
        });
        cos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                function="cos";
                current.setText("当前函数：余弦函数");
            }
        });
        sin_cos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                function="sin+cos";
                current.setText("当前函数：sin + cos 函数");
            }
        });
        linear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                function="linear";
                String inputs=JOptionPane.showInputDialog(null,
                        "请输入自定义线性函数的斜率和偏移\n空格隔开如 2 2 。","输入参数",JOptionPane.OK_CANCEL_OPTION);
                if(inputs!=null) {
                    String[] set = inputs.split(" ");
                    if (set.length != 2) {
                        JOptionPane.showMessageDialog(null, "请您输入完整的信息",
                                "错误", JOptionPane.ERROR_MESSAGE);
                    } else {
                        a = Double.parseDouble(set[0]);
                        b = Double.parseDouble(set[1]);
                        current.setText("当前函数：" + a.toString() + "X + " + b.toString());
                    }
                }
            }
        });

        JLabel tip=new JLabel("初始化输入，输入x 的范围和在这个范围内平均分成的份数，程序将自动创建数据集进行训练");
        tip.setBounds(20,310,800,40);
        panel.add(tip);

        final JLabel xstart=new JLabel("x起始");
        xstart.setFont(new Font("宋体",1,20));
        xstart.setBounds(20,360,60,40);
        panel.add(xstart);
        final JTextField xStartField=new JTextField();
        xStartField.setBounds(80,360,200,40);
        xStartField.setFont(new Font("宋体",1,20));
        if(!isnew){
            xStartField.setText(this.begin.toString());
        }
        panel.add(xStartField);

        final JLabel xend=new JLabel("x终止");
        xend.setFont(new Font("宋体",1,20));
        xend.setBounds(290,360,60,40);
        panel.add(xend);
        final JTextField xEndField=new JTextField();
        xEndField.setBounds(360,360,200,40);
        xEndField.setFont(new Font("宋体",1,20));
        if(!isnew){
            xEndField.setText(this.ends.toString());
        }
        panel.add(xEndField);

        JLabel number=new JLabel("份数");
        number.setFont(new Font("宋体",1,20));
        number.setBounds(570,360,60,40);
        panel.add(number);
        final JTextField numberField=new JTextField();
        numberField.setBounds(640,360,150,40);
        numberField.setFont(new Font("宋体",1,20));
        if(!isnew){
            numberField.setText(this.num.toString());
        }
        panel.add(numberField);

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
                    if(function==null){
                        JOptionPane.showMessageDialog(null,
                                "请先选择要拟合的函数","提示",JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        try {
                            Double startVal = Double.parseDouble(start);
                            Double endVal = Double.parseDouble(end);
                            Integer numVal = Integer.parseInt(number);
                            begin = startVal;
                            ends = endVal;
                            num = numVal;
                            xData = getInputArray(startVal, endVal, numVal);
                            textX = getInputArray(startVal, endVal, numVal + 1);
                            yData = new Double[xData.length];
                            for (Integer i = 0; i < xData.length; i++) {
                                yData[i] = getY(xData[i]);
                            }
                            for(Double item:xData){
                                System.out.printf("%.4f ",item);
                            }
                            System.out.println();
                            for(Double item:yData){
                                System.out.printf("%.4f ",item);
                            }
                            textY = new Double[textX.length];
                            for (Integer i = 0; i < textX.length; i++) {
                                textY[i] = getY(textX[i]);
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
        panel.add(createBtn);
        this.frame.getContentPane().removeAll();
        this.frame.add(panel);
        this.frame.setVisible(true);
        this.frame.repaint();
    }
    private void initDataUi(){
        final JMenu dataset=new JMenu("数据操作");
        menuBar.add(dataset);
        JMenuItem create=new JMenuItem("新建训练集");
        JMenuItem fix=new JMenuItem("修改训练集");
        dataset.add(create);
        dataset.add(fix);

        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataSetUi(true);
            }
        });

        fix.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(function==null){
                    JOptionPane.showMessageDialog(null,
                            "您还没有建立数据集","提示",JOptionPane.ERROR_MESSAGE);
                    dataSetUi(true);
                }
                else{
                    dataSetUi(false);
                }
            }
        });
    }

    private void networkUi(boolean isnew) {
        JPanel panel=new JPanel();
        panel.setLayout(null);
        JLabel label=null;
        if(isnew) {
            this.networkPanel=new NetworkPanel(this.xData.length);
            label = new JLabel("定义网络结构");
        }
        else{
            label=new JLabel("修改网络结构");
        }
        label.setBounds(10,20,400,40);
        label.setFont(new Font("雅黑",1,25));
        panel.add(label);

        final JLabel xstart=new JLabel("输出大小");
        xstart.setFont(new Font("宋体",1,20));
        xstart.setBounds(10,80,100,40);
        panel.add(xstart);
        final JTextField outputSize=new JTextField();
        outputSize.setBounds(100,80,60,40);
        outputSize.setFont(new Font("宋体",1,20));
        panel.add(outputSize);

        final JLabel activation = new JLabel("激活函数");
        activation.setFont(new Font("宋体",1,20));
        activation.setBounds(170,80,100,40);
        panel.add(activation);

        String[] listData = new String[]{"sigmoid", "than", "none"};
        final JComboBox<String> comboBox = new JComboBox<String>(listData);
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // 只处理选中的状态
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    System.out.println("选中: " + comboBox.getSelectedIndex() + " = " + comboBox.getSelectedItem());
                }
            }
        });
        comboBox.setSelectedIndex(2);
        comboBox.setBounds(275,80,90,40);
        panel.add(comboBox);

        JButton createLayer=new JButton("创建新层");
        createLayer.setBounds(370,80,100,40);
        createLayer.setFont(new Font("宋体",1,15));
        createLayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(outputSize.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"请输入该层输出的大小","提示",
                            JOptionPane.ERROR_MESSAGE);
                }
                else {
                    Integer outputsize;
                    try {
                        outputsize = Integer.parseInt(outputSize.getText());
                        networkPanel.addLayer(outputsize, comboBox.getSelectedIndex());
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null,
                                "输入格式不正确", "提示", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        panel.add(createLayer);

        JLabel layerIndex = new JLabel("索引：");
        layerIndex.setFont(new Font("宋体",1,20));
        layerIndex.setBounds(475,80,80,40);
        panel.add(layerIndex);

        final JTextField LayerIndexField=new JTextField();
        LayerIndexField.setBounds(560,80,60,40);
        LayerIndexField.setFont(new Font("宋体",1,20));
        panel.add(LayerIndexField);

        JButton removeBtn=new JButton("删除");
        final JButton fixBtn=new JButton("修改");
        final JButton insertBtn=new JButton("插入");
        JButton searchBtn=new JButton("查看");

        removeBtn.setFont(new Font("宋体",1,20));
        removeBtn.setBounds(625,80,80,40);;
        removeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(LayerIndexField.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"你还没有输入索引","提示",
                            JOptionPane.ERROR_MESSAGE);
                }
                else {
                    try {
                        Integer index = Integer.parseInt(LayerIndexField.getText());
                        if (index >=networkPanel.getLayerNum()) {
                            JOptionPane.showMessageDialog(null, "输入的索引太大！", "提示",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            networkPanel.removeLayer(index);
                        }
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "输入索引的格式不对！", "提示",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        fixBtn.setFont(new Font("宋体",1,20));
        fixBtn.setBounds(710,80,80,40);
        fixBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (LayerIndexField.getText().equals("") || outputSize.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "你还没有输入索引或者输出大小，无法进行修改！", "提示",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        Integer index = Integer.parseInt(LayerIndexField.getText());
                        Integer outputsize = Integer.parseInt(outputSize.getText());
                        if (index >= networkPanel.getLayerNum()) {
                            JOptionPane.showMessageDialog(null, "输入的索引太大！", "提示",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            Boolean success = networkPanel.fixLayer(index, outputsize, comboBox.getSelectedIndex());
                            if (success) {
                                JOptionPane.showMessageDialog(null,
                                        "修改成功！", "提示", JOptionPane.PLAIN_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null,
                                        "修改失败，发生异常！", "提示", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(null, "输入索引或者输出大小的格式不对！", "提示",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        searchBtn.setFont(new Font("宋体",1,20));
        searchBtn.setBounds(795,80,80,40);;
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(LayerIndexField.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"你还没有输入索引","提示",
                            JOptionPane.ERROR_MESSAGE);
                }
                else {
                    try {
                        Integer index = Integer.parseInt(LayerIndexField.getText());
                        if (index >=networkPanel.getLayerNum()) {
                            JOptionPane.showMessageDialog(null, "输入的索引太大！", "提示",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        else {
                            DisplayLayer currentLayer=networkPanel.getLayer(index);
                            outputSize.setText(currentLayer.outputsize.toString());
                            comboBox.setSelectedIndex(currentLayer.activation);
                        }
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "输入索引的格式不对！", "提示",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        insertBtn.setFont(new Font("宋体",1,20));
        insertBtn.setBounds(885,80,80,40);
        insertBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(LayerIndexField.getText().equals("") || outputSize.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"你还没有输入索引或者输出大小","提示",
                            JOptionPane.ERROR_MESSAGE);
                }
                else {
                    try {
                        Integer index = Integer.parseInt(LayerIndexField.getText());
                        Integer outputsize=Integer.parseInt(outputSize.getText());
                        if (index >= networkPanel.getLayerNum()) {
                            JOptionPane.showMessageDialog(null, "输入的索引太大！", "提示",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            System.out.println("执行到了这里");
                            System.out.println(index);
                            System.out.println(comboBox.getSelectedIndex());
                            networkPanel.insertlayer(index,outputsize,comboBox.getSelectedIndex());
                        }
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "输入索引的格式不对！", "提示",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        panel.add(removeBtn);
        panel.add(fixBtn);
        panel.add(insertBtn);
        panel.add(searchBtn);

        JScrollPane scrollPane=new JScrollPane(this.networkPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(10,130,980,600);
        panel.add(scrollPane);


        this.frame.getContentPane().removeAll();
        this.frame.add(panel);
        this.frame.setVisible(true);
        this.frame.repaint();
    }
    private void initNetworkUi(){
        final JMenu network=new JMenu("神经网络");
        menuBar.add(network);
        JMenuItem createNet=new JMenuItem("新建网络");
        JMenuItem fixNet=new JMenuItem("修改网络");
        createNet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(xData==null){
                    JOptionPane.showMessageDialog(null,"您还没有初始化数据集",
                            "提示",JOptionPane.ERROR_MESSAGE);
                    dataSetUi(true);
                }
                else {
                    networkUi(true);
                }
            }
        });
        fixNet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(networkPanel==null){
                    JOptionPane.showMessageDialog(null,"您还没有创建过网络",
                            "提示",JOptionPane.ERROR_MESSAGE);
                    if(xData==null)
                        dataSetUi(true);
                    else
                        networkUi(true);
                }
                else {
                    networkUi(false);
                }
            }
        });
        network.add(createNet);
        network.add(fixNet);
    }

    private void runUi(){
        JPanel panel=new JPanel();
        panel.setLayout(null);
        JLabel label=new JLabel("运行状态");
        label.setBounds(20,20,400,40);
        label.setFont(new Font("雅黑",1,25));
        panel.add(label);



        this.frame.getContentPane().removeAll();
        this.frame.add(panel);
        this.frame.setVisible(true);
        this.frame.repaint();
    }
    private void initRunUi(){
        final JMenu run=new JMenu("运行查看");
        JMenuItem item=new JMenuItem("运行查看");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runUi();
            }
        });
        run.add(item);
        menuBar.add(run);
    }
    public Main(){
        this.frame=new JFrame("BP算法演示：by 高谦 :) ");
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }catch (Exception e){
            e.printStackTrace();
        }
        menuBar=new JMenuBar();
        this.frame.setJMenuBar(menuBar);
        initDataUi();
        initNetworkUi();
        initRunUi();
        dataSetUi(true);
        frame.setSize(1000,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
    }
    public static void main(String[] args) {

//        Main app=new Main();

        Double[] x_data ={-Math.PI,-1.0/2.0*Math.PI,0.0,1.0/2.0*Math.PI,Math.PI};
        Double[] y_data=new Double[x_data.length];
        for(Integer i=0;i<x_data.length;i++){
            y_data[i]=Math.sin(x_data[i]);
        }

//        Double[] x_data={1.0,2.0};
//        Double[] y_data={3.0};

        Layer inputLayer = new Layer(x_data);
        Layer hidden = new Layer(inputLayer, 100, Activation.TANH);
        Layer outputLayer = new Layer(hidden, y_data.length, Activation.NOACTIVATION);


        Train train = new Train(inputLayer, outputLayer, y_data);
        train.Init(1400, 0.0, 0.01);
        train.train(20, 100);
    }
}
