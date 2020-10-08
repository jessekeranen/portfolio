package portfolio;

/**
 * Consist from companys information of one chosen month. Month class is used to put data to TableView in
 * CompanyGUIController
 * @author jessekeranen
 * @version 23.9.2020
 *
 */
public class Month {
    
    private double profit;
    private double beme;
    private double mv;
    private double bv;

    /**
     * Constructor
     * @param profit Companys return on this spefici month
     * @param beme Companys Be/Me-ratio on this spefici month
     * @param mv Companys market value on this spefici month
     * @param bv Companys book value on this spefici month
     */
    public Month(double profit, double beme, double mv, double bv) {
        this.profit = profit;
        this.beme = beme;
        this.mv = mv;
        this.bv = bv;
    }
    
    /**
     * @return Companys return on spefic month
     */
    public double getProfit() {
        return profit;
    }
    
    /**
     * @return Companys Be/Me-ratio on spefic month
     */
    public double getBeme() {
        return beme;
    }
    
    /**
     * @return Companys market value on spefic month
     */
    public double getMv() {
        return mv;
    }
    
    /**
     * @return  Companys book value on spefic month
     */
    public double getBv() {
        return bv;
    }
}
