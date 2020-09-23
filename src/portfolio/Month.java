package portfolio;

/**
 * Consist form companys information of one chosen month. Month class is used to put data to TableView in
 * CompanyGUICOntroller
 * @author jessekeranen
 * @version 23.9.2020
 *
 */
public class Month {
    
    private double profit;
    private double beme;
    private double mv;
    private double bv;

    public Month(double profit, double beme, double mv, double bv) {
        this.profit = profit;
        this.beme = beme;
        this.mv = mv;
        this.bv = bv;
    }
    
    public double getProfit() {
        return profit;
    }
    
    public double getBeme() {
        return beme;
    }
    
    public double getMv() {
        return mv;
    }
    
    public double getBv() {
        return bv;
    }
}
