package expert;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class ProfessionalSystem {
    private List<Regular> regulars;
    private JFrame frame;
    private Regular currentRegular;
    private Integer currentRegularIndex;
    private Boolean addToRegulars(Regular regular){
        for(Regular re:regulars){
            if(re.equals(regular)){
                return false;
            }
        }
        this.regulars.add(regular);
        return true;
    }
    private Regular getByIndex(String id) {
        for(Integer i=0;i<this.regulars.size();i++){
            if(this.regulars.get(i).getIndex().toString().equals(id)){
                return this.regulars.get(i);
            }
        }
        return null;
    }
    private void addMenu(){
        this.frame=new JFrame("专家系统");
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }catch (Exception e){
            e.printStackTrace();
        }
        JMenuBar menuBar=new JMenuBar();
        JMenu direction=new JMenu("推理方向");
        JMenuItem item=new JMenuItem("正向推理");
        JMenuItem item0=new JMenuItem("反向推理");
        direction.add(item);
        direction.add(item0);
        menuBar.add(direction);

        JMenu function=new JMenu("功能");
        JMenuItem item1=new JMenuItem("添加规则");
        JMenuItem item2=new JMenuItem("删除规则");
        JMenuItem item3=new JMenuItem("修改规则");
        function.add(item1);
        function.add(item2);
        function.add(item3);
        menuBar.add(function);
        /**
         * 为每一个菜单栏添加事件
         */
        // 正向推理：
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                positive();
            }
        });
        // 反向推理：
        item0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reverse();
            }
        });

        //添加规则
        item1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRegular();
            }
        });
        //删除规则
        item2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeRegular();
            }
        });
        //修改规则
        item3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fixRegular();
            }
        });
        this.frame.setJMenuBar(menuBar);
        this.frame.setLocationRelativeTo(null);
        this.frame.setSize(1000,800);
        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                for(Integer i=0;i<regulars.size();i++){
                    regulars.get(i).setIndex(i);
                }
                try {
                    OutputStream outputStream=new FileOutputStream(new File("./regularSet.dat"));
                    ObjectOutputStream objectOutputStream=new ObjectOutputStream(outputStream);
                    objectOutputStream.writeObject(regulars);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private void positive(){
        this.frame.getContentPane().removeAll();
        positiveUi();
        this.frame.repaint();
    }
    private void reverse(){
        this.frame.getContentPane().removeAll();
        reverseUi();
        this.frame.repaint();
    }
    private void fixRegular(){
        this.frame.getContentPane().removeAll();
        fixUi();
        System.out.println("修改规则");
        this.frame.repaint();
    }
    private void addRegular(){
        this.frame.getContentPane().removeAll();
        addUi();
        System.out.println("添加规则");
        this.frame.repaint();
    }
    private void removeRegular(){
        this.frame.getContentPane().removeAll();
        removeUi();
        System.out.println("删除规则");
        this.frame.repaint();
    }
    private void fixUi(){
        final JPanel inputPanle=new JPanel();
        inputPanle.setPreferredSize(new Dimension(600,100));
        inputPanle.setLayout(null);
        JLabel title=new JLabel("输入关键词搜索知识库");
        title.setFont(new Font("宋体",0,20));
        title.setBounds(10,10,300,30);
        final JTextField keyWord=new JTextField();
        keyWord.setBounds(10,50,400,40);
        keyWord.setFont(new Font("宋体",0,20));
        keyWord.setMargin(new Insets(5,10,5,10));
        JButton searchBtn=new JButton("搜 索");
        searchBtn.setBounds(420,50,90,40);
        searchBtn.setFont(new Font("宋体",0,20));

        final JTextField fixId=new JTextField();
        fixId.setFont(new Font("宋体",0,20));
        fixId.setMargin(new Insets(5,10,5,10));
        fixId.setBounds(540,50,265,40);

        JButton selectBtn=new JButton("选择规则");
        selectBtn.setBounds(820,50,120,40);
        selectBtn.setFont(new Font("宋体",0,20));


        inputPanle.add(selectBtn);
        inputPanle.add(fixId);
        inputPanle.add(keyWord);
        inputPanle.add(searchBtn);
        inputPanle.add(title);

        // 初始化结果展示界面：
        final JTextArea textArea=new JTextArea();
        textArea.setFont(new Font("宋体",0,20));
        textArea.setMargin(new Insets(10,10,10,10));
        textArea.setLineWrap(true);
        JLabel label =new JLabel("查询到的结果如下：");
        label.setBounds(10,5,300,30);
        label.setFont(new Font("宋体",0,20));
        JScrollPane scrollPane=new JScrollPane(textArea);
        scrollPane.setBounds(10,40,500,575);
        scrollPane.setBackground(new Color(0xD9FFDE));

        JPanel searchResult=new JPanel();
        searchResult.setLayout(null);
        searchResult.add(scrollPane);
        searchResult.add(label);
        searchResult.setPreferredSize(new Dimension(520,580));


        JPanel deleteInfo=new JPanel();
        deleteInfo.setLayout(null);
        deleteInfo.setPreferredSize(new Dimension(450,500));
        final JTextArea info=new JTextArea();
        info.setFont(new Font("宋体",0,20));
        info.setMargin(new Insets(10,10,10,10));
        final JScrollPane deleteItemScrollPane=new JScrollPane(info);
        deleteItemScrollPane.setBounds(10,40,400,250);

        JLabel resLabel=new JLabel("您将修改下面规则的结论");
        resLabel.setBounds(10,300,400,40);
        resLabel.setFont(new Font("宋体",0,20));
        final JTextArea res=new JTextArea();
        res.setMargin(new Insets(10,10,10,10));
        res.setFont(new Font("宋体",0,20));
        JScrollPane result=new JScrollPane(res);
        result.setBounds(10,350,400,150);
        JLabel deleteLabel=new JLabel("您将修改下面规则的条件");
        deleteLabel.setFont(new Font("宋体",0,20));
        deleteLabel.setBounds(10,5,300,40);
        JButton submit=new JButton("提 交");
        submit.setFont(new Font("宋体",0,20));
        submit.setBounds(10,510,100,40);
        deleteInfo.add(deleteItemScrollPane);
        deleteInfo.add(submit);
        deleteInfo.add(deleteLabel);
        deleteInfo.add(result);
        deleteInfo.add(resLabel);
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
                String key=keyWord.getText();
                if(key.equals("")){
                    for(Regular re:regulars){
                        textArea.append(re.toString());
                        textArea.append("\n==========================\n");
                    }
                }
                else{
                    for(Regular re:regulars){
                        List<String> conditions=re.getConditions();
                        for(String con:conditions){
                            if(key.equals(con) || key.indexOf(con)!=-1 || con.indexOf(key)!=-1){
                                textArea.append(re.toString());
                                textArea.append("\n==========================\n");
                                break;
                            }
                        }
                        if(re.getResult().indexOf(key)!=-1||
                                key.indexOf(re.getResult())!=-1 ||
                                re.getResult().equals(key)){
                            textArea.append(re.toString());
                            textArea.append("\n==========================\n");
                        }
                    }
                }
            }
        });
        selectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id=fixId.getText();
                Regular re=getByIndex(id);
                if(re==null){
                    JOptionPane.showMessageDialog(null,"当前知识库没有您查询的知识",
                            "提示",JOptionPane.ERROR_MESSAGE);
                }
                else{
                    info.setText(re.conditionToString());
                    res.setText(re.getResult());
                    currentRegularIndex = Integer.parseInt(id);
                }
            }
        });
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String condition=info.getText();
                String result=res.getText();
                if(result.equals("")|| condition.equals("")){
                    JOptionPane.showMessageDialog(null,"请您将内容填写完整","提示",
                            JOptionPane.ERROR_MESSAGE);
                }
                else{
                    Integer type=JOptionPane.showConfirmDialog(null,
                            "这条规则设置为终止规则？","提示",
                            JOptionPane.YES_NO_OPTION);

                    // -1 直接关闭  0 是  1 否
                    if(type==-1){
                        JOptionPane.showMessageDialog(null,"取消修改","提示",
                                JOptionPane.PLAIN_MESSAGE);
                    }
                    else {
                        if(type==0){
                            regulars.get(currentRegularIndex).setTerminal(true);
                        }
                        else{
                            regulars.get(currentRegularIndex).setTerminal(false);
                        }
                        regulars.get(currentRegularIndex).setConditions(condition.split("\n"));
                        regulars.get(currentRegularIndex).setResult(result);
                        JOptionPane.showMessageDialog(null, "修改成功", "提示",
                                JOptionPane.PLAIN_MESSAGE);
                    }
                }
            }
        });

        this.frame.add(inputPanle,BorderLayout.NORTH);
        this.frame.add(searchResult,BorderLayout.WEST);
        this.frame.add(deleteInfo,BorderLayout.EAST);
        this.frame.setVisible(true);
    }
    private void addUi(){
        final JPanel inputPanle=new JPanel();
        inputPanle.setPreferredSize(new Dimension(600,300));
        inputPanle.setLayout(null);
        final JTextArea textAreaLeft=new JTextArea();
        textAreaLeft.setFont(new Font("宋体",0,20));
        textAreaLeft.setMargin(new Insets(10,10,10,10));
        textAreaLeft.setLineWrap(true);

        final JTextArea textAreaRight=new JTextArea();
        textAreaRight.setFont(new Font("宋体",0,20));
        textAreaRight.setMargin(new Insets(10,10,10,10));
        textAreaRight.setLineWrap(true);

        JLabel label =new JLabel("添加规则");
        label.setBounds(10,10,300,30);
        label.setFont(new Font("宋体",0,20));



        JLabel leftTip =new JLabel("输入条件");
        leftTip.setFont(new Font("宋体",0,15));
        leftTip.setBounds(10,40,400,20);
        JScrollPane scrollLeft=new JScrollPane(textAreaLeft);
        scrollLeft.setBounds(10,65,460,150);
        scrollLeft.setBackground(new Color(0xD9FFDE));

        final JLabel rightTip=new JLabel("输入结论，只可以填写一个");
        rightTip.setFont(new Font("宋体",0,15));
        rightTip.setBounds(485,40,400,20);
        JScrollPane scrollRight=new JScrollPane(textAreaRight);
        scrollRight.setBounds(480,65,485,150);
        scrollRight.setBackground(new Color(0xD9FFDE));

        inputPanle.add(scrollLeft);
        inputPanle.add(scrollRight);
        inputPanle.add(leftTip);
        inputPanle.add(rightTip);
        JButton button=new JButton("规则预览");
        inputPanle.add(button);

        button.setBounds(10,240,100,30);
        inputPanle.add(label);
        JPanel displayPanel=new JPanel();
        displayPanel.setLayout(null);

        final JTextArea output=new JTextArea();
        output.setLineWrap(true);
        output.setFont(new Font("宋体",0,20));
        output.setMargin(new Insets(10,10,10,10));
        JScrollPane display=new JScrollPane(output);
        JLabel tip=new JLabel("生成的规则如下：");
        tip.setBounds(10,0,300,30);
        display.setBounds(10,32,960,300);
        tip.setFont(new Font("宋体",0,20));

        JButton submit=new JButton("提交");
        submit.setBounds(10,340,100,30);
        displayPanel.add(submit);

        displayPanel.add(tip);
        displayPanel.add(display);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String regu=textAreaLeft.getText();
                String result=textAreaRight.getText();
                if(regu.equals("")){
                    JOptionPane.showMessageDialog(null,"请输入规则","提示",JOptionPane.ERROR_MESSAGE);
                }
                if(result.equals("")){
                    JOptionPane.showMessageDialog(null,"请您输入结果","提示",JOptionPane.ERROR_MESSAGE);
                }
                if(!result.equals("") && !regu.equals("")){
                    Regular regular=new Regular(regu.split("\n"));
                    regular.setResult(result);
                    currentRegular=regular;
                    output.setText(regular.toString());
                }
            }
        });
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentRegular==null){
                    JOptionPane.showMessageDialog(null,"请你先预览规则",
                            "提示",JOptionPane.ERROR_MESSAGE);
                }
                else {
                    Integer type=JOptionPane.showConfirmDialog(null,"将这条规则设置为终止规则么？","提示",
                            JOptionPane.YES_NO_OPTION);
                    if(type==-1){
                        JOptionPane.showMessageDialog(null,"取消添加","提示",
                                JOptionPane.PLAIN_MESSAGE);
                    }
                    else {
                        if(type==0)
                            currentRegular.setTerminal(true);
                        else
                            currentRegular.setTerminal(false);
                        Boolean ok = addToRegulars(currentRegular);
                        if (ok) {
                            currentRegular.setIndex(regulars.size() - 1);
                            JOptionPane.showMessageDialog(null, "添加成功！",
                                    "提示", JOptionPane.PLAIN_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "添加失败，已经存在",
                                    "提示", JOptionPane.ERROR_MESSAGE);
                        }
                        currentRegular = null;
                    }
                }
            }
        });
        this.frame.add(displayPanel,BorderLayout.CENTER);
        this.frame.add(inputPanle,BorderLayout.NORTH);
        this.frame.setVisible(true);
    }
    private void removeUi(){
        final JPanel inputPanle=new JPanel();
        inputPanle.setPreferredSize(new Dimension(600,100));
        inputPanle.setLayout(null);
        JLabel title=new JLabel("输入关键词搜索知识库");
        title.setFont(new Font("宋体",0,20));
        title.setBounds(10,10,300,30);
        final JTextField keyWord=new JTextField();
        keyWord.setBounds(10,50,400,40);
        keyWord.setFont(new Font("宋体",0,20));
        keyWord.setMargin(new Insets(5,10,5,10));
        JButton searchBtn=new JButton("搜 索");
        searchBtn.setBounds(420,50,90,40);
        searchBtn.setFont(new Font("宋体",0,20));

        final JTextField deleteId=new JTextField();
        deleteId.setFont(new Font("宋体",0,20));
        deleteId.setMargin(new Insets(5,10,5,10));
        deleteId.setBounds(540,50,265,40);

        JButton deleteBtn=new JButton("删除规则");
        deleteBtn.setBounds(820,50,120,40);
        deleteBtn.setFont(new Font("宋体",0,20));


        inputPanle.add(deleteBtn);
        inputPanle.add(deleteId);
        inputPanle.add(keyWord);
        inputPanle.add(searchBtn);
        inputPanle.add(title);

        // 初始化结果展示界面：
        final JTextArea textArea=new JTextArea();
        textArea.setFont(new Font("宋体",0,20));
        textArea.setMargin(new Insets(10,10,10,10));
        textArea.setLineWrap(true);
        JLabel label =new JLabel("查询到的结果如下：");
        label.setBounds(10,5,300,30);
        label.setFont(new Font("宋体",0,20));
        JScrollPane scrollPane=new JScrollPane(textArea);
        scrollPane.setBounds(10,40,500,575);
        scrollPane.setBackground(new Color(0xD9FFDE));

        JPanel searchResult=new JPanel();
        searchResult.setLayout(null);
        searchResult.add(scrollPane);
        searchResult.add(label);
        searchResult.setPreferredSize(new Dimension(520,580));


        JPanel deleteInfo=new JPanel();
        deleteInfo.setLayout(null);
        deleteInfo.setPreferredSize(new Dimension(450,500));
        final JTextArea info=new JTextArea();
        info.setFont(new Font("宋体",0,20));
        info.setMargin(new Insets(10,10,10,10));
        JScrollPane deleteItemScrollPane=new JScrollPane(info);
        deleteItemScrollPane.setBounds(10,40,400,400);
        JLabel deleteLabel=new JLabel("您已将下面的规则删除");
        deleteLabel.setFont(new Font("宋体",0,20));
        deleteLabel.setBounds(10,5,300,40);
        deleteInfo.add(deleteItemScrollPane);
        deleteInfo.add(deleteLabel);
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
                String key=keyWord.getText();
                if(key.equals("")){
                    for(Regular re:regulars){
                        textArea.append(re.toString());
                        textArea.append("\n==========================\n");
                    }
                }
                else{
                    for(Regular re:regulars){
                        List<String> conditions=re.getConditions();
                        for(String con:conditions){
                            if(key.equals(con) || key.indexOf(con)!=-1 || con.indexOf(key)!=-1){
                                textArea.append(re.toString());
                                textArea.append("\n==========================\n");
                                break;
                            }
                        }
                        if(re.getResult().indexOf(key)!=-1||
                                key.indexOf(re.getResult())!=-1 ||
                                re.getResult().equals(key)){
                            textArea.append(re.toString());
                            textArea.append("\n==========================\n");
                        }
                    }
                }
            }
        });
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id =deleteId.getText();
                Regular re=getByIndex(id);
                if(re==null)
                    JOptionPane.showMessageDialog(null,"当前知识库没有您要找的知识",
                            "提示",JOptionPane.ERROR_MESSAGE);
                else {
                    info.setText(re.toString());
                    regulars.remove(re);
                }
            }
        });
        this.frame.add(inputPanle,BorderLayout.NORTH);
        this.frame.add(searchResult,BorderLayout.WEST);
        this.frame.add(deleteInfo,BorderLayout.EAST);
        this.frame.setVisible(true);
    }
    private void reverseUi() {
        final JPanel inputPanle=new JPanel();
        inputPanle.setPreferredSize(new Dimension(600,250));
        inputPanle.setLayout(null);
        final JTextArea textArea=new JTextArea();
        textArea.setFont(new Font("宋体",0,20));
        textArea.setMargin(new Insets(10,10,10,10));
        textArea.setLineWrap(true);
        JLabel label =new JLabel("输入假设，回答问题，逆向推理！");
        label.setBounds(10,10,300,30);
        label.setFont(new Font("宋体",0,20));
        JScrollPane scrollPane=new JScrollPane(textArea);
        scrollPane.setBounds(10,40,960,150);
        scrollPane.setBackground(new Color(0xD9FFDE));
        inputPanle.add(scrollPane);
        JButton button=new JButton("推理");
        button.setBounds(10,200,100,30);

        inputPanle.add(button);
        inputPanle.add(label);
        JPanel displayPanel=new JPanel();
        displayPanel.setLayout(null);

        final JTextArea output=new JTextArea();
        output.setLineWrap(true);
        output.setFont(new Font("宋体",0,20));
        output.setMargin(new Insets(10,10,10,10));
        JScrollPane display=new JScrollPane(output);
        JLabel tip=new JLabel("推理过程和结果如下：");
        tip.setBounds(10,0,300,30);
        display.setBounds(10,32,960,450);
        tip.setFont(new Font("宋体",0,20));

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String factStr=textArea.getText();
                if(factStr.equals("")){
                    JOptionPane.showMessageDialog(null,"请你输入内容",
                            "提示",JOptionPane.ERROR_MESSAGE);
                }
                else{
                    ReasoningMachine machine=new ReasoningMachine(new Fact(),regulars,factStr,output);
                    machine.reverseReasoning();
                }
            }
        });
        displayPanel.add(tip);
        displayPanel.add(display);
        this.frame.add(displayPanel,BorderLayout.CENTER);
        this.frame.add(inputPanle,BorderLayout.NORTH);
        this.frame.setVisible(true);
    }
    private void positiveUi(){
        final JPanel inputPanle=new JPanel();
        inputPanle.setPreferredSize(new Dimension(600,250));
        inputPanle.setLayout(null);
        final JTextArea textArea=new JTextArea();
        textArea.setFont(new Font("宋体",0,20));
        textArea.setMargin(new Insets(10,10,10,10));
        textArea.setLineWrap(true);
        JLabel label =new JLabel("填写事实,（回车分隔）正向推理");
        label.setBounds(10,10,300,30);
        label.setFont(new Font("宋体",0,20));
        JScrollPane scrollPane=new JScrollPane(textArea);
        scrollPane.setBounds(10,40,960,150);
        scrollPane.setBackground(new Color(0xD9FFDE));
        inputPanle.add(scrollPane);
        JButton button=new JButton("推理");
        button.setBounds(10,200,100,30);

        inputPanle.add(button);
        inputPanle.add(label);
        JPanel displayPanel=new JPanel();
        displayPanel.setLayout(null);

        final JTextArea output=new JTextArea();
        output.setLineWrap(true);
        output.setFont(new Font("宋体",0,20));
        output.setMargin(new Insets(10,10,10,10));
        JScrollPane display=new JScrollPane(output);
        JLabel tip=new JLabel("推理过程和结果如下：");
        tip.setBounds(10,0,300,30);
        display.setBounds(10,32,960,450);
        tip.setFont(new Font("宋体",0,20));

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String factStr=textArea.getText();
                if(factStr.equals("")){
                    JOptionPane.showMessageDialog(null,"请你输入内容",
                            "提示",JOptionPane.ERROR_MESSAGE);
                }
                else{
                    Fact fact=new Fact(factStr,"\n");
                    System.out.println(fact.getFactItemList());
                    ReasoningMachine machine=new ReasoningMachine(fact,regulars);
                    String res=machine.positiveReasoning();
                    output.setText(res);
                }
            }
        });
        displayPanel.add(tip);
        displayPanel.add(display);
        this.frame.add(displayPanel,BorderLayout.CENTER);
        this.frame.add(inputPanle,BorderLayout.NORTH);
        this.frame.setVisible(true);
    }
    public ProfessionalSystem(Object object) throws IOException, ClassNotFoundException {
        this.regulars=(List<Regular>) object;
        this.addMenu();
        this.positiveUi();
    }
    public static void main(String[] args) {
        try {
            FileInputStream fileInputStream=new FileInputStream(new File("./regularSet.dat"));
            ObjectInputStream objectInputStream=new ObjectInputStream(fileInputStream);
            ProfessionalSystem pro = new ProfessionalSystem(objectInputStream.readObject());
            objectInputStream.close();
            fileInputStream.close();

        }catch (IOException e){
            System.out.println("找不到知识库文件");
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(new File("./regularSet.dat"));
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(new ArrayList<Regular>());
            }catch (Exception fe){
                fe.printStackTrace();
            }
        }catch (ClassNotFoundException e){
            System.out.println("知识库文件损坏，无法打开知识库文件");
        }
    }
}
