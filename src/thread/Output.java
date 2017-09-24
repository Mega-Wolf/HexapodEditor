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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mygame.InternalMovement;
import static mygame.InternalMovement.MOVEMENTS;
import static mygame.InternalMovement.MOVEMENT_ENDING;
import mygame.Population;
import robots.AWalker;
import robots.RotatableRobot;
import robots.TripodLoopRobot;

/**
 *
 * @author Tobias
 */
public class Output extends Application {

    private static Output instance;

    public static final AWalker[] WALKERS = {
        new TripodLoopRobot(false),
        new RotatableRobot(false)
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

    
    /**
     * Starts the simulation and builds up the GUI for that
     * @param robotIndex the index of the robot in Output.WALKERS
     * @param fitnessIndex the index of the fitness in Output.FITNESSES
     */
    private void startSimulation(int robotIndex, int fitnessIndex) {
        //TODO: Definitely break up this big chunk of code
        
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
                    super.layoutPlotChildren();
                }
            }

            @Override
            protected void layoutChildren() {
                synchronized (seriesLock) {
                    super.layoutChildren();
                }
            }

            @Override
            protected void updateAxisRange() {
                synchronized (seriesLock) {
                    super.updateAxisRange();
                }
            }
        };

        graph.setTitle(FITNESSES[fitnessIndex].getName() + " (" + FITNESSES[fitnessIndex].getDescription() + ")");
        graph.setCreateSymbols(false);
        
        //set this to 50-100 to get a graph for comparing parameters; however only "1" allows to stop and restart the simulation
        //Don't forget to uncomment the little if cluase in Population (line 121)
        int testSize = 1;
        
        XYChart.Series seriesArray[] = new XYChart.Series[testSize];
        
        for (int i = 0; i < testSize; i++) {
            series = new XYChart.Series();
            series.setName(WALKERS[robotIndex].getName());
            graph.getData().add(series);
            seriesArray[i] = series;
        }
        
        
        Thread t = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < testSize; i++) {
                    //System.out.println(i);
                    series = seriesArray[testSize];

                    population = new Population(WALKERS[robotIndex], FITNESSES[fitnessIndex], Collections.synchronizedList(new ArrayList<>()));      
                    population.testGA();
                    
                    choose(499);
                }
            }
        };
        
        t.start();

        TextField populationIndex = new TextField("0");

        Label ofLabel = new Label(" / " + (population.getBestRobots().size() - 1));

        Label fitnessLabel = new Label();
        Pane fitnessPane = new StackPane();
        fitnessPane.getChildren().add(fitnessLabel);

        fitnessLabel.setContentDisplay(ContentDisplay.CENTER);

        Button saveButton = new Button("Save Movement");

        Pane popPane = new HBox();
        popPane.getChildren().addAll(populationIndex, ofLabel);

        //Label robotLabel = new Label(Test.WALKERS[robotIndex].getName());
        //Label fitnessLabel = new Label(Test.FITNESSES[fitnessIndex].getName());
        TextField[][] geneFields;
        ToggleButton[][] mutationButtons;

        double[][] dna;
        boolean[][] mutations;

        GridPane dnaPane = new GridPane();
        dnaPane.setVgap(5);
        dnaPane.setHgap(5);
        geneFields = new TextField[Output.WALKERS[robotIndex].getDNA().length][];
        mutationButtons = new ToggleButton[Output.WALKERS[robotIndex].getDNA().length][];
        dna = new double[Output.WALKERS[robotIndex].getDNA().length][];
        mutations = new boolean[Output.WALKERS[robotIndex].getDNA().length][];
        for (int i = 0; i < Output.WALKERS[robotIndex].getDNA().length; i++) {
            Label geneLabel = new Label(Output.WALKERS[robotIndex].getDNAInfo()[i][0]);
            dnaPane.add(geneLabel, 0, i);

            geneFields[i] = new TextField[Output.WALKERS[robotIndex].getDNA()[i].length];
            mutationButtons[i] = new ToggleButton[Output.WALKERS[robotIndex].getDNA()[i].length];
            dna[i] = new double[Output.WALKERS[robotIndex].getDNA()[i].length];
            mutations[i] = new boolean[Output.WALKERS[robotIndex].getDNA()[i].length];
            for (int ii = 0; ii < Output.WALKERS[robotIndex].getDNA()[i].length; ii++) {
                //Label geneLabelInner = new Label((Output.WALKERS[robotIndex].getDNA()[i][ii]) + "");
                //dnaPane.add(geneLabelInner, ii + 1, i);

                Pane innerPane = new HBox();

                TextField geneEdit = new TextField((Output.WALKERS[robotIndex].getDNA()[i][ii]) + "");
                ToggleButton geneToggle = new ToggleButton("Keep");

                innerPane.getChildren().addAll(geneEdit, geneToggle);
                dnaPane.add(innerPane, ii + 1, i);

                geneEdit.setId(i + "," + ii);

                geneEdit.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (oldValue && !newValue) {
                        String id = geneEdit.getId();
                        String[] coords = id.split(",");

                        if (geneEdit.getText().matches("[+-]?((\\d+(\\.\\d*)?)|(\\.\\d+))")) {
                            dna[Integer.valueOf(coords[0])][Integer.valueOf(coords[1])] = Double.valueOf(geneEdit.getText());

                            AWalker walker = Output.WALKERS[robotIndex].newInstance(dna);

                            if (walker.initialFitnessChecks(Output.FITNESSES[fitnessIndex]) == 0) {
                                Output.FITNESSES[fitnessIndex].calcFitnessValue(walker);
                            }

                            fitnessLabel.setText("Fitness: " + walker.getFitness());

                            if (walker.getFitness() > 0) {
                                saveButton.setDisable(false);
                            } else {
                                saveButton.setDisable(true);
                            }

                        } else {
                            //geneEdit.setText(oldValue);
                            geneEdit.setText(dna[Integer.valueOf(coords[0])][Integer.valueOf(coords[1])] + "");
                        }
                    }
                });

                geneToggle.setId(i + "," + ii);

                geneToggle.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    String id = geneToggle.getId();
                    String[] coords = id.split(",");

                    mutations[Integer.valueOf(coords[0])][Integer.valueOf(coords[1])] = !newValue;
                });

                /*
                geneEdit.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    if (!geneEdit.focusedProperty().get()) {
                        String id = geneEdit.getId();
                        String[] coords = id.split(",");
                        
                        if (newValue.matches("[+-]?((\\d+(\\.\\d*)?)|(\\.\\d+))")) {
                            fitnessLabel.setText("Fitness: ???");
                            saveButton.setDisable(true);
                            
                            dna[Integer.valueOf(coords[0])][Integer.valueOf(coords[1])] = Double.valueOf(newValue);
                        } else {
                            //geneEdit.setText(oldValue);
                            geneEdit.setText(dna[Integer.valueOf(coords[0])][Integer.valueOf(coords[1])] + "");
                        }
                    }
                });
                 */
                geneFields[i][ii] = geneEdit;
                mutationButtons[i][ii] = geneToggle;

                dna[i][ii] = (Output.WALKERS[robotIndex].getDNA()[i][ii]);
                mutations[i][ii] = (Output.WALKERS[robotIndex].getShallMutate()[i][ii]);
            }
        }

        populationIndex.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue.matches("\\d*")) {
                int value = Math.max(0, Math.min(Integer.valueOf(newValue), population.getBestRobots().size() - 1));

                for (int i = 0; i < Output.WALKERS[robotIndex].getDNA().length; i++) {
                    for (int ii = 0; ii < Output.WALKERS[robotIndex].getDNA()[i].length; ii++) {
                        geneFields[i][ii].setText(population.getBestRobots().get(value).getDNA()[i][ii] + "");
                        dna[i][ii] = population.getBestRobots().get(value).getDNA()[i][ii];
                    }
                }

                ofLabel.setText(" / " + (population.getBestRobots().size() - 1));

                fitnessLabel.setText("Fitness: " + population.getBestRobots().get(value).getFitness() + "");
                saveButton.setDisable(false);
            } else {
                populationIndex.setText(oldValue);
            }
        });

        Pane IOPane = new HBox();
        Button stopSimulation = new Button("Stop Simulation");
        Button restartSimulation = new Button("Restart Simiulation");

        stopSimulation.setOnAction(e -> {
            IOPane.getChildren().remove(stopSimulation);
            IOPane.getChildren().addAll(restartSimulation, fitnessPane, saveButton);

            choose(population.getBestRobots().size() - 1);

            for (int i = 0; i < Output.WALKERS[robotIndex].getDNA().length; i++) {
                for (int ii = 0; ii < Output.WALKERS[robotIndex].getDNA()[i].length; ii++) {
                    geneFields[i][ii].setText(population.getLast().getDNA()[i][ii] + "");
                }
            }

            populationIndex.setText(population.getBestRobots().size() - 1 + "");
            ofLabel.setText(" / " + (population.getBestRobots().size() - 1));

            fitnessLabel.setText("Fitness: " + population.getBestRobots().get(population.getBestRobots().size() - 1).getFitness() + "");
            saveButton.setDisable(false);

            firstPane.getChildren().add(1, popPane);
            firstPane.getChildren().add(2, dnaPane);

            population.stopSim();
        });

        restartSimulation.setOnAction(e -> {
            IOPane.getChildren().removeAll(restartSimulation, fitnessPane, saveButton);
            IOPane.getChildren().add(stopSimulation);

            firstPane.getChildren().remove(popPane);
            firstPane.getChildren().remove(dnaPane);

            population = new Population(Output.WALKERS[robotIndex].newInstance(dna, mutations), FITNESSES[fitnessIndex], population.getBestRobots());
            Thread thread_ = new Thread(() -> {
                population.testGA();
            });

            thread_.start();
        });

        saveButton.setDisable(true);
        saveButton.setOnAction(e -> {
            InternalMovement im = new InternalMovement(Output.WALKERS[robotIndex].newInstance(dna));

            int i = 0;
            for (; new File(MOVEMENTS + System.getProperty("file.separator") + i + MOVEMENT_ENDING).isFile(); i++);

            try {
                im.export(i);
            } catch (IOException ex) {
                Logger.getLogger(Output.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        IOPane.getChildren().addAll(stopSimulation);

        firstPane.getChildren().addAll(graph, IOPane);

        Scene scene = new Scene(firstPane, 1024, 768);
        primaryStage.setScene(scene);
    }
    
    /**
     * Update the chart
     * @param index the number to which the graph shall be updated
     */
    public void choose(int index) {
        synchronized (seriesLock) {
            if (index == 0) {
                return;
            }

            int oldSize = series.getData().size();
            int newSize = population.getBestRobots().size();

            for (int i = oldSize; i < newSize; i++) {
                series.getData().add(new XYChart.Data(i, Math.max(0, population.getBestRobots().get(i).getFitness())));
            }
        }
    }

    /**
     * Returns the only instance of Output in the program
     * @return a pseudo singleton of Output
     */
    public static Output getInstance() {
        return instance;
    }
    
    /* Getter */
    
    /**
     * Returns the curret population
     * @return the current population
     */
    public Population getPopulation() {
        return population;
    }
    
    @Override
    public void stop() throws Exception {
        population.stopSim();
        super.stop();
    }

}
