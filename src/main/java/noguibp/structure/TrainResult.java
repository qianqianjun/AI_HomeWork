package noguibp.structure;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.Serializable;
import java.util.List;

/**
 * @author 高谦
 * 用于存储神经网络训练的结果
 */
public class TrainResult implements Serializable {
    private XYSeriesCollection recallSet;
    private XYSeriesCollection generalizationSet;
    private XYSeriesCollection lossSet;

    public void setRecallSet(List<XYSeries> group) {
        recallSet=new XYSeriesCollection();
        for(Integer i=0;i<group.size();i++){
            recallSet.addSeries(group.get(i));
        }
    }

    public void setGeneralizationSet(List<XYSeries> group) {
        generalizationSet=new XYSeriesCollection();
        for(Integer i=0;i<group.size();i++){
            generalizationSet.addSeries(group.get(i));
        }
    }

    public void setLossSet(XYSeries lossLine) {
        lossSet=new XYSeriesCollection();
        lossSet.addSeries(lossLine);
    }

    public XYSeriesCollection getGeneralizationSet() {
        return generalizationSet;
    }

    public XYSeriesCollection getLossSet() {
        return lossSet;
    }

    public XYSeriesCollection getRecallSet() {
        return recallSet;
    }
}
