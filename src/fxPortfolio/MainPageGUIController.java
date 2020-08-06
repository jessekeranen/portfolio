package fxPortfolio;

import fi.jyu.mit.fxgui.ListChooser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
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
    
    @FXML private TextField portfolioName;
    @FXML private TextField portfolioAveBeMe;
    @FXML private TextField portfolioAveReturn;
    @FXML private TextField portfolioAveMarketValue;
    
    private Market market;
    private Portfolio currentPortfolio;
    
    /**
     * @param market market that contains all the companies of the data
     */
    public void setMarket(Market market) {
        this.market = market;
        //chooserPortfolios.addSelectionListener(e-> showPortfolio(market));
        
        for(int i = 0; i < Company.years; i++) {
            market.adjustPortfolios();
            market.constructPortfolios(market.portfolios, i);
        }
        showPortfolio(market);     
    }
    
    private void addTextfields(Portfolio portfolio) {
        portfolioName.setText(portfolio.name); 
        portfolioAveBeMe.setText("");
        portfolioAveMarketValue.setText(portfolio.averagePortfolioMarketValueString);
        portfolioAveReturn.setText(portfolio.averagePortfolioReturnString);
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
        addTextfields(portfolio);
    }
    
}