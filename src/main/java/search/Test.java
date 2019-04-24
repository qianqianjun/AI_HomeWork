package search;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

class Comp extends JComponent{
    private Double x;
    private Double y;
    private Double radius;
    public Comp(){
        this.x=100.0;
        this.y=100.0;
        this.radius=50.0;
        setPreferredSize(new Dimension(500,500));
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D=(Graphics2D) g;
        graphics2D.fill(new Ellipse2D.Double(x,y,radius,radius));
        graphics2D.drawLine(0,100,300,100);
        graphics2D.drawLine(100,0,100,300);
    }
}
public class Test extends JFrame {
    public Test(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Comp comp=new Comp();
        add(comp,BorderLayout.CENTER);
        comp.repaint();
        setVisible(true);
        setLocationRelativeTo(null);
        setSize(500,500);
    }

    public static void main(String[] args) {
        Test test=new Test();
    }
}
