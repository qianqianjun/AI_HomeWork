package bp;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

class DisplayLayer{
    public Integer outputsize;
    public Integer activation;
    public DisplayLayer nextLayer;
    public List<Double> x;
    public List<Double> y;
}
public class NetworkPanel extends JPanel {
    public DisplayLayer inputLayer;
    public DisplayLayer currentLayer;
    public ArrayList<Double> xVals;
    public Boolean canDraw;
    public NetworkPanel(Integer outputSize){
        this.inputLayer=new DisplayLayer();
        this.inputLayer.outputsize=outputSize;
        this.inputLayer.activation=2;
        this.currentLayer=this.inputLayer;
        this.setPreferredSize(new Dimension(2000,574));
        if(this.inputLayer.outputsize<15) {
            this.canDraw = true;
        }
        else {
            this.canDraw=false;
        }
    }
    public Integer getLayerNum(){
        Integer i=0;
        DisplayLayer head=this.inputLayer;
        while(head!=null){
            head=head.nextLayer;
            i++;
        }
        return i;
    }
    public void setCanDraw(){
        DisplayLayer head=this.inputLayer;
        while(head!=null){
            if(head.outputsize>15){
                this.canDraw= false;
                return;
            }
            head=head.nextLayer;
        }
        this.canDraw=true;
    }
    public void addLayer(Integer outputSize,Integer activation){
        DisplayLayer newLayer=new DisplayLayer();
        newLayer.outputsize=outputSize;
        newLayer.activation=activation;
        this.currentLayer.nextLayer=newLayer;
        this.currentLayer=newLayer;
        this.setCanDraw();
        this.removeAll();
        this.repaint();
        System.out.println(this.getLayerNum());
    }
    public Boolean removeLayer(Integer index){
        Integer i=0;
        if(index>this.getLayerNum()){
            return false;
        }
        else{
            DisplayLayer head=this.inputLayer;
            while(head.nextLayer!=null){
                if(i==index-1){
                    head.nextLayer=head.nextLayer.nextLayer;
                    break;
                }
                head=head.nextLayer;
                i++;
            }
            this.setCanDraw();
            this.removeAll();
            this.repaint();
            return true;
        }
    }
    public Boolean fixLayer(Integer index,Integer outputSize,Integer activation){
        Integer i=0;
        if(index>this.getLayerNum()){
            return false;
        }
        else{
            DisplayLayer head=this.inputLayer;
            while(head!=null){
                if(i==index){
                    head.outputsize=outputSize;
                    head.activation=activation;
                    break;
                }
                head=head.nextLayer;
                i++;
            }
            this.repaint();
            this.setCanDraw();
            return true;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(canDraw) {
            Graphics2D graphics2D = (Graphics2D) g;
            DisplayLayer head = this.inputLayer;
            Double x = 0.0, y = 0.0;
            Double width = 40.0;
            Double height = 574.0;
            Double radius = width / 2;
            Double offset = width / 4;

            List<Point> points = new ArrayList<>();
            while (head != null) {
                Integer num = head.outputsize;
                Double interval = 1.0 * height / num;
                Double addy = interval / 2;
                head.x=new ArrayList<>();
                head.y=new ArrayList<>();
                for (Integer i = 0; i < num; i++) {
                    Ellipse2D ellipse2D = new Ellipse2D.Double(x + offset, y + addy + i * interval - offset, radius, radius);
                    graphics2D.fill(ellipse2D);
                    head.x.add(x + radius);
                    head.y.add(y + addy + i * interval);
                }
                x += width + 50;
                head = head.nextLayer;
            }
            head = this.inputLayer;
            while (head.nextLayer != null) {
                for (Integer i = 0; i < head.x.size(); i++) {
                    for (Integer j = 0; j < head.nextLayer.x.size(); j++) {
                        graphics2D.drawLine(head.x.get(i).intValue(), head.y.get(i).intValue(),
                                head.nextLayer.x.get(j).intValue(), head.nextLayer.y.get(j).intValue());
                    }
                }
                head = head.nextLayer;
            }
        }
        else{
            g.drawString("因为某一层的神经元数目太多，超过了可以展示的最大值：15 \n" +
                    "（这个值和窗口大小有关） 所以无法展示神经元的结构",30,30);
        }
    }

    public DisplayLayer getLayer(Integer index) {
        DisplayLayer head=this.inputLayer;
        Integer i=0;
        while(head!=null){
            if(index==i){
                return head;
            }
            head=head.nextLayer;
            i=i+1;
        }
        return null;
    }

    public void insertlayer(Integer index, Integer outputSize, int selectedIndex) {
        DisplayLayer head=this.inputLayer;
        DisplayLayer newLayer=new DisplayLayer();
        newLayer.outputsize=outputSize;
        newLayer.activation=selectedIndex;
        Integer i=0;
        while(head!=null){
            if(i==index){
                newLayer.nextLayer=head.nextLayer;
                head.nextLayer=newLayer;
                this.setCanDraw();
                this.removeAll();
                this.repaint();
                break;
            }
            i++;
            head=head.nextLayer;
        }
    }
}


