import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import javafx.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
/*
 * Pooja Nadkarni
 * Period 1
 * 4/4/18
 * 
 * I spent around 1 hour on this lab.
 * 
 * This program went well for me. I was able to understand how to use the Animation
 * Timer class, Anonymous Inner Classes, and the ToggleButton. Creating the erase
 * method also was simple enough, and I could complete this program fairly quickly.
 * 
 */
public class P1_Nadkarni_Pooja_LifeGUI_2 extends Application implements GenerationListener {
	private P1_Nadkarni_Pooja_LifeModel model;
	private BooleanGridPane view;
	private int gen = 0;
	private Label label3;
	private long previousTime = 0;
	private double delay = 1;
	private AnimationTimer timer = new AnimationTimer() {
		public void handle(long now) {
			if((now - previousTime) * (Math.pow(10, -9)) >= delay) {
				model.nextGeneration();
				previousTime = now;
			}
		}
	};
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Life");
		stage.setResizable(false);
		stage.sizeToScene();
		
		view = new BooleanGridPane();
		Boolean[][] arr = readFile(new File("C://Users/Pooja Nadkarni/Downloads/data.txt"));
		
		model = new P1_Nadkarni_Pooja_LifeModel(arr);
		model.addGenerationListener(this);
		
		view.setModel(model);
		view.setTileSize(30);
		
		MyClickHandler handleClick = new MyClickHandler();
		view.setOnMouseClicked(handleClick);
		
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 500, 500);
		stage.setScene(scene);
		VBox mainScreen = new VBox();
		root.getChildren().add(mainScreen);
		
		MenuBar menuBar = new MenuBar();
		Menu menu = new Menu("Options");
		MenuItem open = new MenuItem("Open");
		MenuEventHandler handleOpen = new MenuEventHandler();
		open.setOnAction(handleOpen);
		
		MenuItem save = new MenuItem("Save");
		MenuEventHandler handleOpen2 = new MenuEventHandler();
		save.setOnAction(handleOpen2);
		menu.getItems().addAll(open, save);
		menuBar.getMenus().add(menu);
		mainScreen.getChildren().add(menuBar);
		BorderPane gridScreen = new BorderPane();
		gridScreen.setCenter(view);
		mainScreen.getChildren().add(gridScreen);
		
		HBox buttonScreen = new HBox();
		buttonScreen.setPadding(new Insets(scene.getHeight() / 8, scene.getWidth() / 50, scene.getHeight() / 8, scene.getWidth() / 50));
	    buttonScreen.setSpacing(scene.getWidth() / 10);
		mainScreen.getChildren().add(buttonScreen);
				
		Button clear = new Button("Clear");
		MyMouseHandler clearHandle = new MyMouseHandler();
		clear.setOnMouseClicked(clearHandle);
		buttonScreen.getChildren().addAll(clear);
			
		Button nextGen = new Button("Next Generation");
		MyMouseHandler handleNext = new MyMouseHandler();
		nextGen.setOnMousePressed(handleNext);
		nextGen.setOnMouseDragged(handleNext);
		buttonScreen.getChildren().addAll(nextGen);
		
		ToggleButton tb1 = new ToggleButton("Animate Generations");
		MyToggleListener listenTog = new MyToggleListener();
		tb1.setOnAction(listenTog);
		buttonScreen.getChildren().add(tb1);
		
		
		VBox vbox = new VBox();
		Label label2 = new Label("Generation: ");
		label3 = new Label("" + gen);
		buttonScreen.getChildren().add(vbox);
		vbox.getChildren().addAll(label2, label3);
		
		VBox vbx = new VBox();
		Label label = new Label("Tile Size");
		Slider slider = new Slider();
		slider.setMin(0);
		slider.setMax(100);
		slider.setValue(50);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setMajorTickUnit(10);
		slider.setMinorTickCount(1);
		slider.setBlockIncrement(10);
		MyChangeListener slideHandle = new MyChangeListener();
		slider.valueProperty().addListener(slideHandle);
		vbx.getChildren().addAll(label, slider);
		buttonScreen.getChildren().addAll(vbx);
		
		VBox vbx2 = new VBox();
		Label label5 = new Label("Animation Speed");
		Slider slider2 = new Slider();
		slider2.setMin(.1);
		slider2.setMax(10);
		slider2.setValue(1);
		slider2.setShowTickLabels(true);
		slider2.setShowTickMarks(true);
		slider2.setMajorTickUnit(.5);
		MyChangeListener slideHandle2 = new MyChangeListener() {
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				delay = (double) newValue;
			}
		};
		slider.valueProperty().addListener(slideHandle2);
		vbx.getChildren().addAll(label5, slider2);
		buttonScreen.getChildren().addAll(vbx2);
		
	
		stage.show();
		
	}
	
	private static Boolean[][] readFile(File file){
		Scanner scan = null;
		Boolean[][] grid = new Boolean[0][0];
		try {
			scan = new Scanner(file);
			int gridLength = scan.nextInt();		
			int gridWidth = scan.nextInt();
			grid = new Boolean[gridLength][gridWidth];
			int rowCount = 0;
			int colCount = 0;
			while(scan.hasNext()) {
				String item = scan.next();	
				if(item.equalsIgnoreCase("X")) {
					grid[rowCount][colCount] = true;
					if(colCount == gridWidth - 1) {
						rowCount++;
						colCount = 0;
					} else {
						colCount++;
					}
					
				} else if(item.equalsIgnoreCase("O")){					
					grid[rowCount][colCount] = false;
					if(colCount == gridWidth - 1) {
						rowCount++;
						colCount = 0;
					} else {
						colCount++;
					}
				}			
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return grid;
	}

	
	private class MyChangeListener implements ChangeListener<Number>{
	
		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			view.setTileSize((double) newValue);
		}
		
	}
	
	private class MyToggleListener implements EventHandler <ActionEvent>{

		public void handle(ActionEvent event) {
			if(((ToggleButton) event.getSource()).isSelected()) {
				timer.start();
			} else {
				timer.stop();
			}
		}

	}
	
	private class MenuEventHandler implements EventHandler <ActionEvent>{
		public void handle(ActionEvent event) {
			if(((MenuItem) event.getSource()).getText().equals("Open")){
				Stage stage2 = new Stage();
				FileChooser fileChoose = new FileChooser();			
				fileChoose.setTitle("Open Data File");
				Boolean[][] arr = readFile(fileChoose.showOpenDialog(stage2));
				model = new P1_Nadkarni_Pooja_LifeModel(arr);
				view.setModel(model);
			} else if(((MenuItem) event.getSource()).getText().equals("Save")){
				Stage stage2 = new Stage();
				FileChooser fileChoose = new FileChooser();			
				fileChoose.setTitle("Save Data File");
				File file = fileChoose.showOpenDialog(stage2);
				FileWriter writer = null;
				try {
					writer = new FileWriter(file);
					System.out.println("here");
					writer.write(model.getNumRows() + " " + model.getNumCols());
					writer.write("\r\n");
					for(int i = 0; i < model.getNumRows(); i++){
						for(int x = 0; x < model.getNumCols(); x++){
							if(model.getValueAt(i, x)) {
								writer.write("X ");
							} else {
								writer.write("O ");
							}				
						}
						writer.write("\r\n");
					}
					writer.close();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
	
	private class MyMouseHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			if(((Button) event.getSource()).getText().equals("Clear")){
				for(int i = 0; i < model.getNumRows(); i++) {
					for(int n = 0; n < model.getNumCols(); n++) {
						model.setValueAt(i, n, false);
					}
				}
				model.setGeneration(0);
				label3.setText("" + gen);
			} else if(((Button) event.getSource()).getText().equals("Next Generation")){
				model.nextGeneration();
				label3.setText("" + gen);
			} 
		}
	
	
		
	}
	
	private class MyClickHandler implements EventHandler<MouseEvent>{
		@Override
		public void handle(MouseEvent event) {
			if(event.getButton() == MouseButton.PRIMARY) {
				if(event.getClickCount() == 2) {
					int row = view.rowForYPos(event.getY());
					int col = view.colForXPos(event.getX());
					erase(row, col);
				} else {
					int row = view.rowForYPos(event.getY());
					int col = view.colForXPos(event.getX());
					model.setValueAt(row, col, true);
				}
				
			} else if(event.getButton() == MouseButton.SECONDARY){
				int row = view.rowForYPos(event.getY());
				int col = view.colForXPos(event.getX());
				model.setValueAt(row, col, false);
			}
		}
	}
	
	public void erase(int row, int col){
		if(row >= 0 && row < model.getNumRows() && col >= 0 && col < model.getNumCols() && model.getValueAt(row, col)){
			model.setValueAt(row, col, false);
			erase(row + 1, col);
			erase(row - 1, col);
			erase(row, col + 1);
			erase(row, col - 1);
		}
	}

	
	@Override
	public void generationChanged(int oldVal, int newVal) {
		gen = newVal;
	}

}
