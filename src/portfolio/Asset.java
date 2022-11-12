package portfolio;

import java.text.DecimalFormat;

/**
 * @author jessekeranen
 * @version 5.10.2020
 *
 */
public class Asset {
    
        /** Name of the asset */
        protected String name;
        
        protected double averageReturn;
        protected double averageMarketValue;
        public double beta;
        protected double treynorRatio;
        protected double sharpeRatio;
        
        protected double[] returns;
        protected double[] cumulativeReturns;
        protected double[] marketValues;
        protected double[] beMeRatios;
        
    
        /**
         * Calculates the Sharpe ratio of the company
         * @param profits array of the assets returns
         * @param rf array of the risk free returns
         * @return Sharpe ratio of the company
         * @example
         * <pre name="test">
         * #TOLERANCE=0.000001
         * exampleCompany();
         * Nokia.sharpeRatio ~~~ -0.112799;
         * </pre>
         */
        public double sharpeRatio(double[] profits, double[] rf) {
            double std = 0;
            double excessReturn = 0;
            for(int i = 1; i < profits.length; i++) {
                excessReturn += profits[i] - rf[i];
            }
            excessReturn = excessReturn/(profits.length-1);
            
            for(int j = 1; j < profits.length; j++) {
                std += Math.pow((profits[j] - rf[j] - excessReturn), 2);
            }
            std = Math.sqrt(std/(profits.length-1));
            
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
         * Calculates cumulative returns of the asset
         */
        public void cumulativeReturn() {
            cumulativeReturns[0] = 100;
            for(int i = 1; i < returns.length; i++) {
                cumulativeReturns[i] = cumulativeReturns[i-1]*(1+returns[i]);
            }
        }
        
        /**
         * Calculates the mean of the given array return
         * There -1 in the denominator of the dididing because first cell of the returns array is always zero
         * @param array An array holding all the information
         * @param returnArray Is the array return array or not
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
        public double average(double[] array, boolean returnArray) {
            if(array.length != 0) {
                double average = 0;
                for(int i = 0; i < array.length; i++) {
                    average += array[i];
                }
                if(returnArray == true) average = average/(array.length-1);
                else average = average/array.length;
                return average;
            }
            return 0;
        }
        
        /**
         * @return Returns the name of the asset
         */
        public String getName() {
            return name;
        }
        
        /**
         * Returns value of requested asset variable
         * @param number Indicates which variable is requested
         * @return Returns value of requested asset variable
         */
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
        
        /**
         * Returns a value of certain cell, indicated by parameter k, from an array holding asset information
         * @param number In which array infromation is located
         * @param k In which cell wanted information is located
         * @return Requested value
         */
        public double getDouble(int number, int k) {
            switch(number) {
            case 0: return returns[k];
            case 1: return marketValues[k];
            case 2: return beMeRatios[k];
            default: return 0;
            }
        }
        
        /**
         * Returns an array of asset information
         * @param number Which array is requested
         * @return Asset information array
         */
        public double[] getArray(int number) {
            switch(number) {
            case 0: return returns;
            case 1: return marketValues;
            case 2: return beMeRatios;
            case 3: return cumulativeReturns;
            default: return null;
            }
        }
        
        /**
         * Returns cumulative return between last moment and the momen indicated by the parameter period
         * @param period Starting point 
         * @return Assets return
         */
        public double getReturn(int period) {
            double profit = 1;
            for(int i = (returns.length-period); i < returns.length; i++) {
                profit = profit*(1+returns[i]);
            }
            return profit-1;
        }
        /**
         * Changes the average portfolio return from double to string
         * @param average double value of the mean
         * @return String value of the average
         */
        public String averageString(double average) {
            return new DecimalFormat("#.#######").format(average);
        }
}
