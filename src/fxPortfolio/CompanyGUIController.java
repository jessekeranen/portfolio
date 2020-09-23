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
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import portfolio.Company;
import portfolio.Month;

/**
 * Shows more specific information about the chosen company
 * @author jessekeranen
 * @version 23.9.2020
 *
 */
public class CompanyGUIController implements ModalControllerInterface<Company>, Initializable{
    
    @FXML private TextField editName;
    @FXML private TextField editReturn;
    @FXML private TextField editBeMe;
    @FXML private TextField editMarketValue;
    @FXML private BorderPane pane2;
    @FXML private NumberAxis axis;
    @FXML private NumberAxis ayis;
    @FXML private LineChart<Number, Number> chart; 
    @FXML private TableView<Month> tableView;
    @FXML private TableColumn<Month, Double> returns;
    @FXML private TableColumn<Month, Double> BeMes;
    @FXML private TableColumn<Month, Double> marketValues;
    @FXML private TableColumn<Month, Double> bookValues;

    public CompanyGUIController() {
        //
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
       alusta();        
    }
    
    private Company currentCompany;
    private XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
    
    public static Company askCompany(Stage modalityStage, Company company) {
        return ModalController.showModal(
                CompanyGUIController.class.getResource("CompanyGUIView.fxml"),
                company.name,
                modalityStage, company, null 
            );

    }
    
    public void showCompany(Company company) {
        if( company == null) return;
        editName.setText(company.name);
        editReturn.setText(String.valueOf(company.average(company.returns)));
        editBeMe.setText(String.valueOf(company.average(company.beMeRatios)));
        editMarketValue.setText(String.valueOf(company.average(company.marketValues)));
        setAxis(company.returns);
        series = loadData(company.returns, pane2, chart, series, axis, ayis);
        chart.getData().add(series);
        pane2.getChildren().addAll(chart);
        tableView(company);
        
    }
    
    public ObservableList<Month> getCompanies(Company company){
        ObservableList<Month> data = FXCollections.observableArrayList();
        for(int i = 0; i < company.returns.length; i++) {
            data.add(new Month(company.returns[i], company.beMeRatios[i], company.marketValues[i], company.bookValues[i]));
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
    
    protected void alusta() {
        //
    }

    @Override
    public void setDefault(Company oletus) {
        currentCompany = oletus;
        showCompany(currentCompany);
    }
    
    private void setAxis(double[] array) {
        NumberAxis xAxis = new NumberAxis(1, array.length , 0.5);
        xAxis.setLabel("month");
        NumberAxis yAxis = new NumberAxis(-10,10,1);
        yAxis.setLabel("return");
        ayis = yAxis;
        axis = xAxis;
    }
     
    public static XYChart.Series<Number, Number> loadData(double[] array, BorderPane pane, LineChart<Number, Number>  chart, XYChart.Series<Number, Number> series, NumberAxis axis, NumberAxis ayis) {
         pane.getChildren().clear();
         chart.getData().clear();
         series.getData().clear();
         
          
         for(int i = 0; i < array.length; i++) {
             series.getData().add(new XYChart.Data<>(i, array[i]));
         }
         return series;
     }
          
    
}