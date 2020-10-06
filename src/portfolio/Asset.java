package portfolio;

/**
 * @author jessekeranen
 * @version 5.10.2020
 *
 */
public class Asset {
    
        protected String name;
        
        protected double averageReturn;
        protected double averageMarketValue;
        public double beta;
        protected double treynorRatio;
        protected double sharpeRatio;
        
        protected double[] returns;
        protected double[] marketValues;
        protected double[] beMeRatios;
        
    
        /**
         * Calculates the Sharpe ratio of the company
         * @param returns array of the assets returns
         * @param rf array of the risk free returns
         * @return Sharpe ratio of the company
         * @example
         * <pre name="test">
         * #TOLERANCE=0.000001
         * exampleCompany();
         * Nokia.sharpeRatio ~~~ -0.112799;
         * </pre>
         */
        public double sharpeRatio(double[] returns, double[] rf) {
            double std = 0;
            double excessReturn = 0;
            for(int i = 1; i < returns.length; i++) {
                excessReturn += returns[i] - rf[i];
            }
            excessReturn = excessReturn/(returns.length-1);
            
            for(int j = 1; j < returns.length; j++) {
                std += Math.pow((returns[j] - rf[j] - excessReturn), 2);
            }
            std = Math.sqrt(std/(returns.length-1));
            
            return excessReturn/std;
        }
        
        /**
         * Calculates the treynor ratio of the asset
         * @param marketReturns An array of the market returns
         * @param aveMarketReturn Average market return
         * @param profits An array of the assets returns
         * @param prices An array of the assets prices
         * @param aveReturn Average return of the asset
         * @param company Is asset a company or portfolio
         * @return Treynor ratio of the company
         */
        public double treynorRatio(double[] marketReturns, double aveMarketReturn, double[] profits, double [] prices, double aveReturn, boolean company) {
            
            /* number needs to be conditional because portfolios returns array length is only 12 but we want to use longer sample to calculate treynor ratio for companies */
            int number = 0;
            if(company == true) number = 60;
            else number = 11;
            
            double covariance = 0;
            int rows = profits.length;
            
            if(profits.length < number || prices[rows-2] == 0) return 0;
            for(int i = rows-number; i <rows-1; i++) {
                covariance += (profits[i]-aveReturn)*(marketReturns[i]-aveMarketReturn);
            }
            covariance = covariance/number;
            
            double variance = 0;
            for(int j = rows-number; j < profits.length; j++) {
                variance += Math.pow((profits[j]-aveReturn), 2);
            }
            variance = variance/number;
            this.beta = covariance/variance;
            double lastYearProfit = ((prices[prices.length-1]-prices[prices.length-12])/prices[prices.length-12]);
            return (lastYearProfit/beta);
        }
        
        /**
         * Calculates the mean of the given array return
         * @param array An array holding all the information
         * @return average value
         * @example
         * <pre name="test">
         * #TOLERANCE=0.001
         * exampleCompany();
         * Nokia.average(Nokia.returns) ~~~ 0.012;
         * double[] array = new double[]{1.5, 0, -1.5, -1.6};
         * double[] array2 = new double[]{};
         * double[] array3 = new double[]{1.6, 5.9, 99, 6.45, 1.88};
         * Nokia.average(array) ~~~ -0.4;
         * Nokia.average(array2) ~~~ 0.0;
         * Nokia.average(array3) ~~~ 22.966;
         * </pre>
         */
        public double average(double[] array) {
            if(array.length != 0) {
                double average = 0;
                for(int i = 0; i < array.length; i++) {
                    average += array[i];
                }
                average =  average/array.length;
                return average;
            }
            return 0;
        }
        
        public String getName() {
            return name;
        }
        
        public double getDouble(int number) {
            switch(number) {
            case 0: return averageReturn;
            case 1: return averageMarketValue;
            case 2: return beta;
            case 3: return sharpeRatio;
            case 4: return treynorRatio;
            default: return 0;
            }
        }
        
        public double getDouble(int number, int k) {
            switch(number) {
            case 0: return returns[k];
            case 1: return marketValues[k];
            case 2: return beMeRatios[k];
            default: return 0;
            }
        }
        
        public double[] getArray(int number) {
            switch(number) {
            case 0: return returns;
            case 1: return marketValues;
            case 2: return beMeRatios;
            default: return null;
            }
        }
}
