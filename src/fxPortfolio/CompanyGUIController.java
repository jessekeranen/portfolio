package fxPortfolio;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import portfolio.Company;
import portfolio.Market;
import portfolio.Month;

/**
 * Shows more specific information about the chosen company
 * @author jessekeranen
 * @version 23.9.2020
 *
 */
public class CompanyGUIController implements ModalControllerInterface<Company>, Initializable{
    
    @FXML private Text editName;
    @FXML private Text editReturn;
    @FXML private Text editBeMe;
    @FXML private Text editMarketValue;
    @FXML private Text editSharpe;
    @FXML private Text editTreynor;
    @FXML private Text editBeta;
    @FXML private Pane pane2;
    @FXML private NumberAxis axis;
    @FXML private NumberAxis ayis;
    @FXML private LineChart<Number, Number> chart; 
    @FXML private TableView<Month> tableView;
    @FXML private TableColumn<Month, Double> returns;
    @FXML private TableColumn<Month, Double> BeMes;
    @FXML private TableColumn<Month, Double> marketValues;
    @FXML private TableColumn<Month, Double> bookValues;

    /**
     * Constructor
     */
    public CompanyGUIController() {
        //
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
       //alusta();        
    }
    
    private Company currentCompany;
    private XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
    private double averageReturn;
    @SuppressWarnings("unused")
    private Market market;
    
    /**
     * @param modalityStage For what are we modal, if null for the application
     * @param company Company that is being observed
     * @return null
     */
    public static Company askCompany(Stage modalityStage, Company company) {
        return ModalController.showModal(
                CompanyGUIController.class.getResource("CompanyGUIView.fxml"),
                company.getName(),
                modalityStage, company, null 
            );
    }
    
    /**
     * @param market Sets the market
     */
    public void setMarket(Market market) {
        this.market = market;
    }

    
    /**
     * Shows the information about observed company
     * @param company Company which is observed
     */
    public void showCompany(Company company) {
        if( company == null) return;
        editName.setText(company.getName());
        averageReturn = company.average(company.getArray(0));
        editReturn.setText(String.valueOf(averageReturn));
        editBeMe.setText(String.valueOf(company.average(company.getArray(2))));
        editMarketValue.setText(String.valueOf(company.average(company.getArray(1))));
        editSharpe.setText(String.valueOf(company.getDouble(3)));
        editTreynor.setText(String.valueOf(company.getDouble(4)));
        editBeta.setText(String.valueOf(company.getDouble(2)));
        
        pane2.getChildren().clear();
        axis = setAxis("Month", 1, company.getArray(0).length);
        ayis = setAxis("Return", -10, 10);
        series = loadData(company.getArray(0), chart, series);
        chart.getData().add(series);
        pane2.getChildren().addAll(chart);
        chart.autosize();
        
        tableView(company);       
    }
    
    /**
     * Puts needed information from ach mont about the observed company to an observable list 
     * @param company Company that is observed
     * @return Observable list of Months
     */
    public ObservableList<Month> getCompanies(Company company){
        ObservableList<Month> data = FXCollections.observableArrayList();
        for(int i = 0; i < company.getArray(0).length; i++) {
            data.add(new Month(company.getDouble(3, i), company.getDouble(4, i), company.getDouble(1, i), company.getDouble(2, i)));
        }
        return data;
    }
    
    @SuppressWarnings("unchecked")
    private void tableView(Company company) {
        returns = new TableColumn<Month, Double>("Returns2");
        returns.setCellValueFactory(new PropertyValueFactory<>("profit"));
        BeMes = new TableColumn<Month, Double>("Be/Me Ratios");
        BeMes.setCellValueFactory(new PropertyValueFactory<>("beme"));
        marketValues = new TableColumn<Month, Double>("Market Values");
        marketValues.setCellValueFactory(new PropertyValueFactory<>("mv"));
        bookValues = new TableColumn<Month, Double>("Book values");
        bookValues.setCellValueFactory(new PropertyValueFactory<>("bv"));
        
        tableView.setItems(getCompanies(company));
        tableView.getColumns().addAll(returns, BeMes, marketValues, bookValues);  
    }

    @Override
    public Company getResult() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void handleShown() {
        // TODO Auto-generated method stub        
    }

    @Override
    public void setDefault(Company oletus) {
        currentCompany = oletus;
        showCompany(currentCompany);
    }
    
    /**
     * Creates axis for the chart
     * @param name Name of the axis
     * @param begin Min value
     * @param end max Value
     * @return Created axis
     */
    public static NumberAxis setAxis(String name, double begin, double end) {
        NumberAxis axis = new NumberAxis(begin, end , 1);
        axis.setLabel(name);
        return axis;
    }
     
    /**
     * Puts the data to a XYSeries
     * @param array An array of the company returns
     * @param chart Chart where the information is shown
     * @param series Series where the information is put
     * @return XYseiries containing the data needed
     */
    public static XYChart.Series<Number, Number> loadData(double[] array, LineChart<Number, Number>  chart, XYChart.Series<Number, Number> series) {
         chart.getData().clear();
         series.getData().clear();
          
         for(int i = 0; i < array.length; i++) {
             series.getData().add(new XYChart.Data<>(i, array[i]));
         }
         return series;
     }
          
    
}