package fxPortfolio;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author jessekeranen
 * @version 22.7.2020
 *
 */
public class PortfolioConstructorMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader ldr = new FXMLLoader(getClass().getResource("PortfolioConstructorGUIView.fxml"));
            final Pane root = ldr.load();
            //final PortfolioConstructorGUIController portfolioconstructorCtrl = (PortfolioConstructorGUIController) ldr.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("portfolioconstructor.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("PortfolioConstructor");
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args Ei käytössä
     */
    public static void main(String[] args) {
        launch(args);
    }
}