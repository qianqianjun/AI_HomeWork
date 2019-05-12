package bp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class NetworkPanel extends JPanel{
    private Main mainWindow;
    private Boolean isnew;
    public NetworkPanel(Main mainWindow,Boolean isnew){
        this.mainWindow=mainWindow;
        this.isnew=isnew;
        this.setUi(isnew);
    }
    public void setUi(Boolean isnew){
        this.setLayout(null);
        JLabel label=null;
        if(isnew) {
            mainWindow.networkCanvas =new NetworkCanvas(1);
            label = new JLabel("定义网络结构");
        }
        else{
            label=new JLabel("修改网络结构");
        }
        label.setBounds(10,20,400,40);
        label.setFont(new Font("雅黑",1,25));
        this.add(label);

        final JLabel xstart=new JLabel("输出大小");
        xstart.setFont(new Font("宋体",1,20));
        xstart.setBounds(10,80,100,40);
        this.add(xstart);
        final JTextField outputSize=new JTextField();
        outputSize.setBounds(100,80,60,40);
        outputSize.setFont(new Font("宋体",1,20));
        this.add(outputSize);

        final JLabel activation = new JLabel("激活函数");
        activation.setFont(new Font("宋体",1,20));
        activation.setBounds(170,80,100,40);
        this.add(activation);

        String[] listData = new String[]{"sigmoid", "than", "none"};
        final JComboBox<String> comboBox = new JComboBox<String>(listData);
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

            }
        });
        comboBox.setSelectedIndex(2);
        comboBox.setBounds(275,80,90,40);
        this.add(comboBox);

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
                        mainWindow.networkCanvas.addLayer(outputsize, comboBox.getSelectedIndex());
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null,
                                "输入格式不正确", "提示", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        this.add(createLayer);

        JLabel layerIndex = new JLabel("索引：");
        layerIndex.setFont(new Font("宋体",1,20));
        layerIndex.setBounds(475,80,80,40);
        this.add(layerIndex);

        final JTextField LayerIndexField=new JTextField();
        LayerIndexField.setBounds(560,80,60,40);
        LayerIndexField.setFont(new Font("宋体",1,20));
        this.add(LayerIndexField);

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
                        if (index >= mainWindow.networkCanvas.getLayerNum()) {
                            JOptionPane.showMessageDialog(null, "输入的索引太大！", "提示",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            mainWindow.networkCanvas.removeLayer(index);
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
                        if (index >= mainWindow.networkCanvas.getLayerNum()) {
                            JOptionPane.showMessageDialog(null, "输入的索引太大！", "提示",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            Boolean success = mainWindow.networkCanvas.fixLayer(index, outputsize, comboBox.getSelectedIndex());
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
                        if (index >= mainWindow.networkCanvas.getLayerNum()) {
                            JOptionPane.showMessageDialog(null, "输入的索引太大！", "提示",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        else {
                            DisplayLayer currentLayer= mainWindow.networkCanvas.getLayer(index);
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
                        if (index >= mainWindow.networkCanvas.getLayerNum()) {
                            JOptionPane.showMessageDialog(null, "输入的索引太大！", "提示",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            mainWindow.networkCanvas.insertlayer(index,outputsize,comboBox.getSelectedIndex());
                        }
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, "输入索引的格式不对！", "提示",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        this.add(removeBtn);
        this.add(fixBtn);
        this.add(insertBtn);
        this.add(searchBtn);

        JScrollPane scrollPane=new JScrollPane(mainWindow.networkCanvas);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(10,130,980,600);
        this.add(scrollPane);
    }
}
