import java.util.*;

public class WeightedClause  {
    List<Integer> clause;
    int weight ;
    public WeightedClause(List<Integer> clause, int weight) {
        this.clause = clause;
        this.weight = weight;
    }


    public List<Integer> clause() {
        return clause;
    }


    public int weight() {
        return weight;
    }

    public void  setWeight(int weight) {
        this.weight = weight;
    }
}
