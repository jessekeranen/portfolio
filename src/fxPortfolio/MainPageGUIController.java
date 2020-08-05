package fxPortfolio;

import fi.jyu.mit.fxgui.ListChooser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
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
    ListChooser<Portfolio> chooserPortfolios;
    
    @FXML
    ListChooser<Company> chooserCompanies;
    
    private Market market;
    private Portfolio currentPortfolio;
    
    public void setMarket(Market market) {
        this.market = market;
        for(int size = 1; size < 3; size++) {
            for(int value = 1; value < 3; value++) {
                Portfolio portfolio = new Portfolio(size, value);
                market.addPortfolio(portfolio);
            }
        }
        market.constructPortfolios(market.portfolios, 1);
        
        showPortfolio(market);
        //market.beMeBreakPoints(1);
        //market.sizeBreakPoints(1);
        
    }
    
    private void showPortfolio(Market market) {
        if(market == null) return;
        for(int i = 0; i < market.portfolioCount; i++) {
            chooserPortfolios.add(market.portfolios[i].name, market.portfolios[i]);
        }
        showCompanies(market.portfolios[0]);
        
    }
    
    @FXML
    private void show(MouseEvent event) {
        currentPortfolio = chooserPortfolios.getSelectedObject();
        showCompanies(currentPortfolio);
    }
    
    private void showCompanies(Portfolio portfolio) {
        chooserCompanies.clear();
        if(portfolio == null) return;
        for(int i = 0; i < portfolio.companies.length; i++) {
            chooserCompanies.add(portfolio.companies[i].name, portfolio.companies[i]);
        }
    }
    
}