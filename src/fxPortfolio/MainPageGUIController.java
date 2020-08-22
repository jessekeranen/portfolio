package fxPortfolio;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.ListChooser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
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
    @FXML private BorderPane pane;
    @FXML private NumberAxis axis;
    @FXML private NumberAxis ayis;
    @FXML private LineChart<Number, Number> chart; 
    @FXML private ComboBoxChooser<String> comboBox;
    
    @FXML private void whichYear() {
        showPortfolio(market);        
    }
    
    @FXML
    private void show() {           
        int number = comboBox.getSelectedIndex();
        if(number != comboBox.getVisibleRowCount()) {
        currentPortfolio = chooserPortfolios.getSelectedObject();
        loadData(currentPortfolio);
        showCompanies(currentPortfolio);  
        }
        else {
            //showPortfolio(market);
            if(chooserPortfolios.getSelectedIndex() != -1) {
                portfolioName.setText(market.years[0][chooserPortfolios.getSelectedIndex()].name); 
                portfolioAveBeMe.setText("");
                portfolioAveMarketValue.setText("");
                portfolioAveReturn.setText(market.periodAverageReturn(chooserPortfolios.getSelectedIndex()));
                }
            else {
                portfolioName.setText(market.years[0][0].name); 
                    portfolioAveBeMe.setText("");
                    portfolioAveMarketValue.setText("");
                    portfolioAveReturn.setText(market.periodAverageReturn(0));
            }
        }      
    }
    
    private Market market;
    private Portfolio currentPortfolio;
    private XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
    
    /**
     * @param market market that contains all the companies of the data
     */
    public void setMarket(Market market) {
        this.market = market;
        //chooserPortfolios.addSelectionListener(e-> showPortfolio(market));
        
        for(int i = 0; i < Company.years; i++) {
            market.constructPortfolios(market.portfolios, i);
        }
        
        comboBox.clear();
        for(int i = 0; i < market.years.length; i++) {
            comboBox.add("Year " + (i+1)); 
        }
        comboBox.add("whole period");
        comboBox.getSelectionModel().select(0);
        showPortfolio(market);     
    }
    
    private void addTextfields(Portfolio portfolio) {
        portfolioName.setText(portfolio.name); 
        portfolioAveBeMe.setText("");
        portfolioAveMarketValue.setText(portfolio.averagePortfolioMarketValueString);
        portfolioAveReturn.setText(portfolio.averagePortfolioReturnString);
    }
    
    private void showPortfolio(Market market) {
        chooserPortfolios.clear();
        int number = comboBox.getSelectedIndex();
        if(market == null) return; 
        
        if(comboBox.getSelectedIndex() != comboBox.getVisibleRowCount()) {
            for(int i = 0; i < market.portfolioCount; i++) {
                chooserPortfolios.add(market.years[number][i].name, market.years[number][i]);
            }
                showCompanies(market.years[number][0]);  
                loadData(market.years[number][0]);
            }
        else {
            for(int i = 0; i < market.portfolioCount; i++) {
                chooserPortfolios.add(market.years[0][i].name, market.years[0][i]);
            }
            //loadData(market.years[number][0]);
        }
    }
    
    
    private void showCompanies(Portfolio portfolio) {
        chooserCompanies.clear();
        if(portfolio == null) return;
        for(int i = 0; i < portfolio.companies.length; i++) {
            chooserCompanies.add(portfolio.companies[i].name, portfolio.companies[i]);
        }
        addTextfields(portfolio);
    }
    
    private void loadData(Portfolio port) {
        pane.getChildren().clear();
        chart.getData().clear();
        series.getData().clear();
        
        NumberAxis xAxis = new NumberAxis(1,12,1);
        xAxis.setLabel("month");
        NumberAxis yAxis = new NumberAxis(port.getMinValue(),port.getMaxValue(),1);
        yAxis.setLabel("return");
        ayis = yAxis;
        axis = xAxis;
        for(int i = 0; i < port.portfolioReturns.length; i++) {
            series.getData().add(new XYChart.Data<>(i, port.portfolioReturns[i]));
        }
        
        chart.getData().add(series);
        pane.getChildren().addAll(chart);
    }   
    
}