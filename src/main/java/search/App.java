package search;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;

/**
 * @author  高谦
 * 人工智能搜索算法实现。
 */
class Canves extends JComponent{
    ArrayList<Node> points;
    public Canves(){
        this.points=new ArrayList<>();
        setBackground(Color.RED);
    }
    public void addPoint(Node node){
        this.points.add(node);
    }
    public Node findNode(Node node){
        for(Integer i=0;i<points.size();i++){
            if(this.points.get(i).equals(node)){
                return this.points.get(i);
            }
        }
        return null;
    }
    public void deleteNode(Node node){
        this.points.remove(node);
    }
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g=(Graphics2D) graphics;
        for(Node node:points){
            g.setColor(node.getColor());
            g.fill(node.getShape());
        }
    }
}
public class App {
    public static Integer DRAW_POINT_MODEL=1;
    public static Integer DRAW_LINE_MODEL=2;
    public static Integer DELETE_POINT_MODEL=3;
    public static Integer DELETE_LINE_MODEL=4;
    private Integer DRAW_LINE_STEP;
    private Integer DELETE_LINE_STEP;
    private JFrame frame;
    private JFileChooser fileChooser;
    private JLabel tips;
    private Integer optionMode;
    private Canves  canves;
    private Node start;
    private Node end;
    private void setMenu(){
        final JMenu file=new JMenu("文件");
        JMenuItem item=new JMenuItem("打开图文件");
        JMenuItem item0=new JMenuItem("保存图文件");
        file.add(item);
        file.add(item0);

        JMenu algorithm=new JMenu("算法");
        JMenuItem item1=new JMenuItem("迪杰斯塔拉算法");
        JMenuItem item2=new JMenuItem("A* 算法");
        JMenuItem item3=new JMenuItem("广度优先算法");
        JMenuItem item4=new JMenuItem("宽度优先算法");
        algorithm.add(item1);
        algorithm.add(item2);
        algorithm.add(item3);
        algorithm.add(item4);

        final JMenu model=new JMenu("模式");
        JMenuItem item5=new JMenuItem("添加点");
        JMenuItem item6=new JMenuItem("添加边");
        JMenuItem item7=new JMenuItem("删除点");
        JMenuItem item8=new JMenuItem("删除边");
        JMenuItem item9=new JMenuItem("清空画板");
        model.add(item5);
        model.add(item6);
        model.add(item7);
        model.add(item8);
        model.add(item9);

        JMenuBar menuBar=new JMenuBar();
        menuBar.add(file);
        menuBar.add(algorithm);
        menuBar.add(model);
        this.frame.setJMenuBar(menuBar);
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        if(f.isDirectory())
                            return true;
                        String name=f.getName().toLowerCase();
                        if(name.endsWith(".dat"))
                            return true;
                        return false;
                    }
                    @Override
                    public String getDescription() {
                        return "二进制文件";
                    }
                });
                fileChooser.setCurrentDirectory(new File("."));
                fileChooser.showOpenDialog(null);
                try {
                    FileInputStream inputStream=new FileInputStream(fileChooser.getSelectedFile());
                    ObjectInputStream objectInputStream=new ObjectInputStream(inputStream);
                    canves.points=(ArrayList<Node>) objectInputStream.readObject();
                    canves.repaint();
                }catch (Exception e2){
                    e2.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "打开文件失败","出错啦！", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        item0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String resval=JOptionPane.showInputDialog(null,"请您输入保存的文件名","保存文件",JOptionPane.OK_OPTION);
                try {
                    FileOutputStream outputStream=new FileOutputStream(new File(resval+".dat"));
                    ObjectOutputStream objectOutputStream=new ObjectOutputStream(outputStream);
                    objectOutputStream.writeObject(canves.points);
                    objectOutputStream.close();
                    outputStream.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        item5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                optionMode=App.DRAW_POINT_MODEL;
                tips.setText("点击屏幕区域即可添加一个点");
            }
        });
        item6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                optionMode=App.DRAW_LINE_MODEL;
                tips.setText("点击一个点作为起点");
            }
        });
        item7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                optionMode=App.DELETE_POINT_MODEL;
                tips.setText("点击屏幕上的一个点进行删除");
            }
        });
        item8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                optionMode=App.DELETE_LINE_MODEL;
                tips.setText("点击一个起点用来选择删除的边");
            }
        });
        item9.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Integer confirm=JOptionPane.showConfirmDialog(null,"您确认要清空画板么，这个操作无法恢复！","提示",
                        JOptionPane.WARNING_MESSAGE);
                if(confirm==0){
                    canves.points=new ArrayList<>();
                    canves.repaint();
                }
            }
        });
        this.tips=new JLabel("就绪");
        JPanel statusBar=new JPanel();
        statusBar.setPreferredSize(new Dimension(1400,30));
        statusBar.setBackground(new Color(0xd1f9e0));
        statusBar.setLayout(null);
        this.tips.setBounds(30,5,500,20);
        statusBar.add(this.tips);
        this.frame.add(statusBar,BorderLayout.SOUTH);
    }
    private void setConvas(){
        this.canves=new Canves();
        this.canves.setPreferredSize(new Dimension(1000,1000));
        this.canves.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Double x = new Double(e.getX());
                Double y = new Double(e.getY());
                Node node = new Node(x, y);
                if(optionMode==DRAW_POINT_MODEL) {
                    canves.addPoint(node);
                    canves.repaint();
                }
                if(optionMode==DELETE_POINT_MODEL){
                    Node res=canves.findNode(node);
                    if(res!=null){
                        canves.repaint();
                        // 0 表示删除， 2 表示不删除。
                        Integer returnVal=JOptionPane.showConfirmDialog(null,
                                "你确定要删除这个点么","提示",JOptionPane.OK_CANCEL_OPTION);
                        if(returnVal==0){
                            canves.deleteNode(res);
                            canves.repaint();
                        }
                    }
                }
                if(optionMode==DRAW_LINE_MODEL){
                    Node res=canves.findNode(node);
                    res.setColor(Color.CYAN);
                    if(DRAW_LINE_STEP%2==0){
                    }
                    else{
                    }
                    DRAW_LINE_STEP+=1;
                }
                if(optionMode==DELETE_LINE_MODEL){

                }
            }
        });
        this.frame.add(this.canves,BorderLayout.WEST);
    }
    public App(){
        this.frame=new JFrame("演示程序");
        this.frame.setLocationRelativeTo(null);
        this.frame.setSize(new Dimension(1400,900));
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMenu();
        setConvas();
        this.fileChooser=new JFileChooser();
        this.frame.setVisible(true);
    }
    public static void main(String[] args) {
        App app=new App();
    }
}
