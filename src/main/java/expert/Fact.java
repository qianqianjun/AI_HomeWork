package expert;
import java.util.ArrayList;
import java.util.List;

/**
 * @author  高谦
 * 综合数据记录实现。
 */
public class Fact{
    private List<String> factItemList;
    private List<String> originItemList;
    public Fact(String input,String regex){
        this.factItemList=new ArrayList<>();
        this.originItemList=new ArrayList<>();
        String[] arr=input.split(regex);
        for(Integer i=0;i<arr.length;i++){
            this.factItemList.add(arr[i]);
            this.originItemList.add(arr[i]);
        }
    }

    public Fact(){
        this.factItemList=new ArrayList<>();
    }

    public List<String> getFactItemList() {
        return factItemList;
    }

    public void setFactItemList(List<String> factItemList) {
        this.factItemList = factItemList;
    }

    public Boolean addFactItem(String result) {
        for(Integer i=0;i<this.factItemList.size();i++){
            if(this.factItemList.get(i).equals(result)){
                return false;
            }
        }
        this.factItemList.add(result);
        return true;
    }

    public boolean contains(String condition) {
        for(Integer i=0;i<this.factItemList.size();i++){
            if(this.factItemList.get(i).equals(condition))
                return true;
        }
        return false;
    }
}
