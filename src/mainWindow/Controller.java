package mainWindow;

import Inference.InferenceMachine;
import fileManager.FileLoader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import Inference.strategy.*;
import javafx.scene.layout.RowConstraints;

import java.util.Observable;
import java.util.Observer;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable, Observer {

    private InferenceMachine inference;
    private Strategy selected_strategy;


    @FXML
    MenuBar menuBar;
    @FXML
    TextFlow textFlow;
    @FXML
    GridPane clausulesTable;
    int clausulesTableRows;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        ConsoleLogger.INSTANCE.setTextFlow(textFlow);
        ConsoleLogger.INSTANCE.LOG(LEVEL.ERROR, "Hej");
        ConsoleLogger.INSTANCE.LOG(LEVEL.INFO,"Heeej");
        addClausulesHeader();

        addClausuleStep("a", "b", "c", "d");
        addClausuleStep("a", "b", "x");
    }


    public void doLoadFile(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter fileChooserTxtExtension = new FileChooser.ExtensionFilter("TXT Files(*.txt)", "*.txt");

        fileChooser.getExtensionFilters().add(fileChooserTxtExtension);
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
        if(file != null){
            FileLoader fileLoader = new FileLoader(file);
            fileLoader.parseFile();
        }

    }
    public void doStrategyA(){
        selected_strategy = new LinearStrategy();
        ConsoleLogger.INSTANCE.LOG(LEVEL.INFO,"Uruchomiono strategie A");
    }
    public void doStrategyB(){
        ConsoleLogger.INSTANCE.LOG(LEVEL.INFO,"Uruchomiono strategie B");
        //TODO dodaj uzycie strategii
    }
    public void doStrategyC(){
        ConsoleLogger.INSTANCE.LOG(LEVEL.INFO,"Uruchomiono strategie C");
        //TODO dodaj uzycie strategii
    }
    public void doExit(){
        Platform.exit();
    }

    @Override
    public void update(Observable observable, Object o)
    {
        System.out.println("Jestem obserwatorem");// TODO jak Tomasz zrobi GUI to tu bedzie obsluga tego co wywnioskowalo InferenceMachine
    }
    public void addClausulesHeader(){
        Font headerFont = new Font("Arial", 25);
        RowConstraints constraints = new RowConstraints();
        constraints.setPrefHeight(40);
        Label clausuleNumber = new Label("#");
        Label clausulesTitle = new Label("Klauzule");
        clausuleNumber.setFont(headerFont);
        clausulesTitle.setFont(headerFont);
        GridPane.setHgrow(clausuleNumber, Priority.ALWAYS);
        GridPane.setHgrow(clausulesTitle, Priority.ALWAYS);
        clausulesTable.add(clausuleNumber,0, clausulesTableRows);
        clausulesTable.add(clausulesTitle,1, clausulesTableRows);

        clausulesTable.getRowConstraints().add(constraints);

        ++clausulesTableRows;
    }
    public void addClausuleStep(String ...clausules){
        RowConstraints constraints = new RowConstraints();
        constraints.setPrefHeight(20);
        for(int i=0; i < clausules.length; i++){
            Node label = new Label(clausules[i]);
            clausulesTable.add(label,i, clausulesTableRows);
            GridPane.setHgrow(label, Priority.ALWAYS);
        }
        clausulesTable.getRowConstraints().add(constraints);
        ++clausulesTableRows;
    }

}
