package expert;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author  高谦
 * 推理机实现。
 */
public class ReasoningMachine {
    private Fact fact;
    private List<Regular> regulars;
    private String assume;
    private JTextArea log;
    public ReasoningMachine(Fact fact,List<Regular> regulars){
        this.fact=fact;
        this.regulars=regulars;
    }

    public ReasoningMachine(Fact fact,List<Regular> regulars,String condition,JTextArea console){
        this.regulars=regulars;
        this.fact=fact;
        this.assume=condition;
        this.log=console;
    }


    public Boolean isEvidence(String condition) {
        for(Integer i=0;i<this.regulars.size();i++){
            if(this.regulars.get(i).getResult().equals(condition)){
                return false;
            }
        }
        return true;
    }

    public List<Regular> getPreKnowledge(String condition){
        List<Regular> arr=new ArrayList<>();
        for(Integer i=0;i<this.regulars.size();i++){
            if(this.regulars.get(i).getResult().equals(condition)){
                arr.add(this.regulars.get(i));
            }
        }
        return arr;
    }

    /**
     *  正向推理：
     * @return
     */
    public String positiveReasoning(){
        StringBuilder sb=new StringBuilder("开始推理\n");
        Boolean isContinue=true;
        while(isContinue){
            isContinue=false;
            for(Integer i=0;i<this.regulars.size();i++){
                if(this.regulars.get(i).satisfy(fact.getFactItemList())){
                    Boolean success=this.fact.addFactItem(this.regulars.get(i).getResult());
                    if(success) {
                        sb.append("匹配规则 " + this.regulars.get(i).getIndex().toString());
                        sb.append("\n添加结论 " + this.regulars.get(i).getResult());
                        sb.append("\n当前事实：");
                        for (Integer j = 0; j < this.fact.getFactItemList().size(); j++) {
                            sb.append(this.fact.getFactItemList().get(j) + " ");
                        }
                        sb.append("\n============================\n");
                        if (this.regulars.get(i).isTerminal()) {
                            sb.append("\n找到答案：" + this.regulars.get(i).getResult());
                            isContinue = false;
                            break;
                        } else {
                            isContinue = true;
                        }
                    }
                }
            }
        }
        return sb.toString();
    }
    /**
     *  反向推理
     */
    public void reverseReasoning(){
        log.setText("");
        if(isSatisfy(assume)){
            log.append("假设成立！");
        }
        else{
            log.append("假设不成立！");
        }
    }

    public Boolean isSatisfy(String condition){
        if(fact.contains(condition))
            return true;
        else{
            // 判断是不是证据：
            if(isEvidence(condition)){
                Integer type=JOptionPane.showConfirmDialog(null,"判断下面的说法: "+condition+"?","确认",
                        JOptionPane.YES_NO_OPTION);
                if(type==0){
                    //肯定回答，说明证据满足
                    fact.addFactItem(condition);
                    log.append("\n数据库:\n");
                    for(String temp:fact.getFactItemList()){
                        log.append(temp+" ");
                    }
                    return true;
                }
                else{
                    // 否定回答，说明这个证据不满足，返回false；
                    return false;
                }
            }
            else{
                // 如果不是一个证据：
                List<Regular> preKnowledge=getPreKnowledge(condition);
                // 遍历所有可以导出这个条件的知识
                for(Integer i=0;i<preKnowledge.size();i++){
                    // 对于每一个知识，查看他的所有条件是不是满足，可以设置一个标志flag
                    Boolean allSatisfy=true;
                    List<String> conditions=preKnowledge.get(i).getConditions();
                    for(Integer j=0;j<conditions.size();j++){
                        // 如果出现了一个不成立的条件，则不用继续比较了，这条知识推导不出来这个条件。
                        if(!isSatisfy(conditions.get(j))){
                            allSatisfy=false;
                            break;
                        }
                    }
                    // 如果存在一条知识，可以推导出条件，可以不用往下看其他知识了，直接返回就好。
                    if(allSatisfy){
                        fact.addFactItem(condition);
                        log.append("\n数据库:\n");
                        for(String temp:fact.getFactItemList()){
                            log.append(temp+" ");
                        }
                        return true;
                    }
                }
                return false;
            }
        }
    }
}
