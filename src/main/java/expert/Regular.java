package expert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author  高谦
 * 推理规则实现。
 */
public class Regular implements Serializable {
    private List<String> conditions;
    private String result;
    private Integer index;
    private Boolean terminal;

    public void setTerminal(Boolean terminal) {
        this.terminal = terminal;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index=index;
    }

    public Regular(String[] arr){
        this.conditions=new ArrayList<>();
        this.setConditions(arr);
    }
    public List<String> getConditions() {
        return conditions;
    }

    public void setConditions(String[] arr) {
        this.conditions=new ArrayList<>();
        for(String item:arr){
            this.conditions.add(item);
        }
    }

    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public Boolean addCondition(String condition){
        for(String elem:this.conditions){
            if(elem.equals(condition)){
                return false;
            }
        }
        this.conditions.add(condition);
        return true;
    }
    public Boolean satisfy(List<String> factItems){
        for(String item:this.conditions){
            Boolean in=false;
            for(String other:factItems){
                if(other.equals(item)){
                    in=true;
                    break;
                }
            }
            if(!in){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("IF \n");
        sb.append(this.conditions.get(0)+"\n");
        for(Integer i=1;i<this.conditions.size();i++){
            sb.append("AND "+this.conditions.get(i)+"\n");
        }
        sb.append("THEN "+this.result);
        if(this.index!=null){
            sb.append("\n"+index.toString());
        }
        return sb.toString();
    }

    public boolean equals(Regular regular) {
        for(Integer i=0;i<this.conditions.size();i++){
            Boolean flag=false;
            for(String item:regular.getConditions()){
                if(item.equals(this.conditions.get(i))){
                    flag=true;
                    break;
                }
            }
            if(!flag){
                return false;
            }
        }
        if(!result.equals(regular.result)){
            return false;
        }
        return true;
    }

    public String conditionToString() {
        StringBuilder sb=new StringBuilder();

        for(Integer i=0;i<this.conditions.size()-1;i++){
            sb.append("and "+this.conditions.get(i)+"\n");
        }
        sb.append(this.conditions.get(this.conditions.size()-1));
        return sb.toString();
    }

    public boolean isTerminal() {
        return this.terminal;
    }
}