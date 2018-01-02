package mainWindow;

import Inference.InferenceMachine;
import Inference.InferenceProduct;
import Inference.Predicate.Clause;
import Inference.Predicate.KnowledgeBase;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable, Observer {

    private InferenceMachine machine;
    private Strategy selected_strategy;
    private KnowledgeBase knowledge;
    private Clause antithesis;


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

        addClausuleStep("C(x)", "P(f(y) V C(A)", "c", "d","P(f(y) V C(A)", "P(f(y) V C(A)", "P(f(y) V C(A)","P(f(y) V C(A)" );
        addClausuleStep("a", "b", "x");
        addClausuleStep("a", "b", "x");
        addClausuleStep("a", "b", "x");

        for(int i=0; i<100; ++i)
        {
            addClausuleStep("a", "b", "x");
        }

    }

    public void doLoadFile(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter fileChooserTxtExtension = new FileChooser.ExtensionFilter("TXT Files(*.txt)", "*.txt");

        fileChooser.getExtensionFilters().add(fileChooserTxtExtension);
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
        if(file != null) {
            //TODO
            //FileLoader fileLoader = new FileLoader(file);
            //fileLoader.parseFile();

            knowledge = new KnowledgeBase();
            try {
                System.out.println(file.getName());
                knowledge.loadFromFile(file.getName());

            }
            catch(IOException e){
                ConsoleLogger.INSTANCE.LOG(LEVEL.ERROR, "Nie udało się wczytać pliku. Spróbuj jeszcze raz");
                ConsoleLogger.INSTANCE.LOG(LEVEL.ERROR, e.getMessage());
                knowledge = null;
            }
            ConsoleLogger.INSTANCE.LOG(LEVEL.INFO, "Wczytano plik z klauzulami");
            for(int i = 0;i < knowledge.getClauseCount(); ++i)
            {
                ConsoleLogger.INSTANCE.LOG(LEVEL.INFO, "Oto klauzule pobrane z pliku:");
                ConsoleLogger.INSTANCE.LOG(LEVEL.INFO, knowledge.getClause(i).toString());
            }
        }

    }

    private void initializeInference() {
        machine.addObserver(this);
        //TODO dodaj wczytywanie klauzuli - tutaj przypisywana jest na sztywno
        antithesis = new Clause();
        antithesis.parseString("C(A)"); //to antyteza dla zestawu 2
        try {
            machine.notifyObservers();
        }
        catch(Exception e)
        {
            ConsoleLogger.INSTANCE.LOG(LEVEL.ERROR, "Cos sie popsulo przy powiadomieniu obserwatora");
        }
    }
    private InferenceProduct runInference()
    {

        return machine.inference(antithesis);
    }

    /**
     * Jak wszystkie elementy są dane to uruchamia wnioskowanie i wypisuje wynik
     */
    public void startResolution(){
        if(selected_strategy == null){
            ConsoleLogger.INSTANCE.LOG(LEVEL.ERROR,"Wybierz najpierw strategie");
            return;
        }
        if(knowledge == null)
        {
            ConsoleLogger.INSTANCE.LOG(LEVEL.ERROR, "Wczytaj najpierw plik z klauzulami");
            return;
        }
        /* //TODO jak sie zrobi wprowadzanie tezy to odkomentowac
        if(antithesis == null)
        {
            ConsoleLogger.INSTANCE.LOG(LEVEL.ERROR, "Nie ma wprowadzonej tezy/antytezy");
            return;
        }
        */

        machine = new InferenceMachine(knowledge, selected_strategy);
        initializeInference();
        ConsoleLogger.INSTANCE.LOG(LEVEL.INFO,"Uruchomiono");
        InferenceProduct product = runInference();
        ConsoleLogger.INSTANCE.LOG(LEVEL.INFO, "Wynik wnioskowania:" +  product);
    }




    public void doStrategyJustificationSet(){
        selected_strategy = new JustificationSetStrategy();
        ConsoleLogger.INSTANCE.LOG(LEVEL.INFO,"Uruchomiono strategie zbioru uzasadnień");
    }

    public void doStrategyLinear(){
        selected_strategy = new LinearStrategy();
        ConsoleLogger.INSTANCE.LOG(LEVEL.INFO,"Uruchomiono strategie liniową");
    }

    public void doStrategyShortClauses(){
        ConsoleLogger.INSTANCE.LOG(LEVEL.INFO,"Uruchomiono strategie krótkich klauzul(jeszcze nie działa)");
        //TODO dodaj uzycie strategii
    }
    public void doExit(){
        Platform.exit();
    }

    /**
     * Metoda odczytujaca od maszyny wnioskujacej jakie klauzule zostaly wytworzone w danym kroku.
     * Wypisuje ona klauzule na ekran za pomoca metody
     * @param observable Referencja do InferenceMachine przeprowadzajaca wnioskowanie
     * @param o Argumenty przekazywane funkcji update() (tutaj powinny byc nullem)
     */
    @Override
    public void update(Observable observable, Object o)
    {
        InferenceMachine machine = (InferenceMachine)observable;
        ArrayList<Clause> generated_clauses = machine.getActualNewClauses();
        int inference_step = machine.getInferenceStep();

        String[] clauses_in_string = new String[generated_clauses.size()];
        for(int i=0;i<generated_clauses.size();++i)
        {
            clauses_in_string[i] = generated_clauses.get(i).toString();
        }
        addClausuleStep(clauses_in_string);

        ConsoleLogger.INSTANCE.LOG(LEVEL.INFO,"Przechwycilem obserwacje nr " + inference_step );
        //System.out.println("Jestem obserwatorem");// TODO jak Tomasz zrobi GUI to tu bedzie obsluga tego co wywnioskowalo InferenceMachine
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
