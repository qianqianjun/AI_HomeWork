package search;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
class Route implements Serializable{
    public Double length;
    public Node nextNode;
    public Route(Double length,Node nextNode){
        this.length=length;
        this.nextNode=nextNode;
    }
    public Route(){}
}
class Node implements Serializable {
    private Double x;
    private Double y;
    private Double radius;
    private Color color;
    private Boolean isStart;
    private Boolean isEnd;
    private List<Route> nextNodes;
    private Integer symble;
    public Node(Double x,Double y){
        this.x=x;
        this.y=y;
        this.radius=20.0;
        this.color=Color.BLACK;
        this.nextNodes=new ArrayList<>();
    }
    public void setIsStart(Boolean start){
        this.isStart=start;
        if(this.isStart) this.color=Color.GREEN;
    }
    public void setIsEnd(Boolean end){
        this.isEnd=end;
        if(this.isEnd) this.color=Color.RED;
    }
    public Color getColor(){
        return color;
    }
    public void setColor(Color color1){
        this.color=color1;
    }
    public void setSymble(Integer symble){
        this.symble=symble;
    }

    // 两个连接函数，使用的是这样的方法。
    public void AddLine (Double length,Node nextNode){
        this.nextNodes.add(new Route(length,nextNode));
        nextNode.connect(length,this);
    }
    public void connect(Double length,Node point){
        this.nextNodes.add(new Route(length,point));
    }
    public Ellipse2D getShape(){
        return new Ellipse2D.Double(x,y,radius,radius);
    }

    public boolean equals(Node node) {
        return Math.pow((node.x-this.x-radius/2),2)+Math.pow((node.y-this.y-radius/2),2) <= this.radius;
    }
}

public class Elements{
}