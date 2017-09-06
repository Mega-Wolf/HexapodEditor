/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thread;

import fitnesses.FFarthestMove;
import fitnesses.FFlatRotate;
import fitnesses.FHide;
import fitnesses.FHideExtended;
import fitnesses.FHigh;
import fitnesses.FLateral;
import fitnesses.FRotate;
import fitnesses.IFitness;
import java.util.ArrayList;
import java.util.Collections;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mygame.Population;
import robots.AWalker;
import robots.BetterRobot;
import robots.TripodLoopRobot;

/**
 *
 * @author Tobia
 */
public class Output extends Application {

    private static Output instance;

    public static final AWalker[] WALKERS = {
        new TripodLoopRobot(false),
        new BetterRobot(false)
    };

    public static final IFitness[] FITNESSES = {
        new FFarthestMove(),
        new FFlatRotate(),
        new FHide(),
        new FHideExtended(),
        new FHigh(),
        new FLateral(),
        new FRotate()
    };

    private Stage primaryStage;
    private Population population;

    private XYChart.Series series;
    
    private final Object seriesLock = new Object();

    @Override
    public void start(Stage primaryStage) throws Exception {

        instance = this;

        this.primaryStage = primaryStage;

        Pane startPane = new VBox();

        Pane robotPane = new HBox();
        Label robotLabel = new Label("Choose a Robottype");
        ComboBox<String> chooseRobot = new ComboBox<String>();
        for (AWalker walker : WALKERS) {
            chooseRobot.getItems().add(walker.getName());
        }
        chooseRobot.getSelectionModel().selectFirst();
        robotPane.getChildren().addAll(robotLabel, chooseRobot);

        Pane fitnessPane = new HBox();
        Label fitnessLabel = new Label("Choose a Fitnessfunction");
        ComboBox<String> chooseFitness = new ComboBox<String>();
        for (IFitness fitness : FITNESSES) {
            chooseFitness.getItems().add(fitness.getName());
        }
        chooseFitness.getSelectionModel().selectFirst();
        fitnessPane.getChildren().addAll(fitnessLabel, chooseFitness);

        Button startButton = new Button("Start Simulation");
        startButton.setOnAction((ActionEvent event) -> {
            startSimulation(chooseRobot.getSelectionModel().getSelectedIndex(), chooseFitness.getSelectionModel().getSelectedIndex());
        });

        startPane.getChildren().addAll(robotPane, fitnessPane, startButton);

        Scene scene = new Scene(startPane, 1024, 768);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startSimulation(int robotIndex, int fitnessIndex) {
        Pane firstPane = new VBox();

        LineChart<Number, Number> graph;

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Generation");
        yAxis.setLabel("Fitness");
        
        graph = new LineChart<Number, Number>(xAxis, yAxis) {
            @Override
            protected void layoutPlotChildren() {
                synchronized (seriesLock) {
                    super.layoutPlotChildren(); //To change body of generated methods, choose Tools | Templates.
                }
            }

            @Override
            protected void layoutChildren() {
                synchronized (seriesLock) {
                    super.layoutChildren(); //To change body of generated methods, choose Tools | Templates.
                }
            }

            @Override
            protected void updateAxisRange() {
                synchronized (seriesLock) {
                    super.updateAxisRange(); //To change body of generated methods, choose Tools | Templates.
                }
            }
            
            
            
            
        };
        
        graph.setTitle(FITNESSES[fitnessIndex].getName());
        graph.setCreateSymbols(false);

        series = new XYChart.Series();
        //series.setData(FXCollections.observableList(new java.util.concurrent.CopyOnWriteArrayList<>())); //Collections.synchronizedList(new ArrayList<>())));
        series.setName(WALKERS[robotIndex].getName());

        graph.getData().add(series);

        /*
        ObservableList<AWalker> popList = FXCollections.observableList(new java.util.concurrent.CopyOnWriteArrayList<>());
        popList.addListener((Observable o) -> {
            synchronized (series) {
                int generation = popList.size() - 1;
                series.getData().add(new XYChart.Data(generation, popList.get(generation).getFitness()));
            }
        });
*/

        population = new Population(WALKERS[robotIndex], FITNESSES[fitnessIndex], Collections.synchronizedList(new ArrayList<>()));
        Thread thread = new Thread(() -> {
            //while(true) {
            population.testGA();
            //}
        });
        thread.start();

        Pane graphControl = new VBox();

        Pane rangePane = new HBox();

        TextField fromField = new TextField("0");
        fromField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue.matches("\\d*")) {
                int value = Math.max(0, Math.min(Integer.valueOf(newValue), (int) xAxis.getUpperBound()));
                xAxis.setLowerBound(value);
            } else {
                fromField.setText(oldValue);
            }
        });

        Label toLabel = new Label(" - ");

        TextField toField = new TextField("0");
        toField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue.matches("\\d*")) {
                int value = Math.max((int) xAxis.getLowerBound(), Math.min(Integer.valueOf(newValue), population.getBestRobots().size() - 1));
                xAxis.setUpperBound(value);
            } else {
                toField.setText(oldValue);
            }
        });

        Label ofLabel = new Label(" / " + (population.getBestRobots().size() - 1));

        rangePane.getChildren().addAll(fromField, toLabel, toField, ofLabel);

        graphControl.getChildren().addAll(rangePane);

        //Label robotLabel = new Label(Test.WALKERS[robotIndex].getName());
        //Label fitnessLabel = new Label(Test.FITNESSES[fitnessIndex].getName());
        Pane dnaPane = new VBox();
        for (int i = 0; i < Output.WALKERS[robotIndex].getDNA().length; i++) {
            Pane genePane = new HBox();
            Label geneLabel = new Label(Output.WALKERS[robotIndex].getDNAInfo()[i][0]);
            genePane.getChildren().add(geneLabel);
            dnaPane.getChildren().add(genePane);
        }

        firstPane.getChildren().addAll(graph, graphControl, dnaPane);

        Scene scene = new Scene(firstPane, 1024, 768);
        primaryStage.setScene(scene);
    }

    public static Output getInsatnce() {
        return instance;
    }

    public Population getPopulation() {
        return population;
    }

    public void choose(int index) {
        synchronized (seriesLock) {
            if (index == 0) {
                return;
            }

            System.out.println(series.getData().getClass());

            int oldSize = series.getData().size();
            int newSize = population.getBestRobots().size();

            for (int i = oldSize; i < newSize; i++) {
                series.getData().add(new XYChart.Data(i, Math.max(0, population.getBestRobots().get(i).getFitness())));
            }
        }
    }

}
