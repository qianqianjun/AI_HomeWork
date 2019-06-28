package noguibp.structure;

public class DatasetInfo {
    public Double[] xArr;
    public Double[] yArr;
    public Double[][] xData;
    public Double[][] yData;

    public boolean hasDataset(){
        if(this.xArr!=null || this.xData!=null){
            return true;
        }
        return false;
    }

    public boolean canInitNetwork(){
        if(this.xData!=null){
            return true;
        }
        return false;
    }
}
