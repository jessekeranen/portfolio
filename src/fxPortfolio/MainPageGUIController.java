package fxPortfolio;

import fi.jyu.mit.fxgui.ListChooser;
import javafx.fxml.FXML;
import portfolio.Company;
import portfolio.Market;
import portfolio.Portfolio;

/**
 * @author jessekeranen
 * @version 23.7.2020
 *
 */
public class MainPageGUIController {
    
    @FXML
    ListChooser<Company> chooserCompanies;
    
    private Market market;
    
    public void setMarket(Market market) {
        this.market = market;
        for(int size = 1; size < 4; size++) {
            for(int value = 1; value < 4; value++) {
                Portfolio portfolio = new Portfolio();
                market.constructPortfolio(portfolio, size, value,1);
                market.addPortfolio(portfolio);
            }
        }
        
        
        showCompany(market);
        //market.beMeBreakPoints(1);
        //market.sizeBreakPoints(1);
        
    }
    
    protected void showCompany(Market market) {
        if(market == null) return;
        for(int i = 0; i < market.companyCount; i++) {
            chooserCompanies.add(market.companies[i].name, market.companies[i]);
        }
        
    }
}