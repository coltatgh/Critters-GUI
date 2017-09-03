package assignment5;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Crit_GUI extends Application{
	
	@Override
	public void start(Stage primaryStage){
		primaryStage.setWidth(800);
		primaryStage.setHeight(800);
		
		//The pane with all controls (Consider Vbox)
		Pane controller = new Pane(new Button("Hi"));	//Just something to help visualize the pane rn
		ColumnConstraints controlWidth = new ColumnConstraints();
		controlWidth.setPercentWidth(20);	//column one is 20% and dynamically resizes
		GridPane.setColumnIndex(controller, 0);	//set the controller pane to this column
		GridPane.setRowSpan(controller, 2);		//make it span both rows
		
		//The pane storing all the critters
		GridPane world = new GridPane();		
		world.add(new Button("Ian"), 0, 0);	//again, just for visuals
		ColumnConstraints worldWidth = new ColumnConstraints();
		worldWidth.setPercentWidth(80);		//This gets the remaining 80% of horizontal space
		RowConstraints worldHeight = new RowConstraints();
		worldHeight.setPercentHeight(80);	//same shit but with the rows
		GridPane.setConstraints(world, 1, 0);	//assigns this to 2nd column, first row
		
		//The pane where the stats will be printed
		Pane statsOut = new Pane();
		statsOut.getChildren().add(new Button("Stats"));	//viz
		RowConstraints statsHeight = new RowConstraints();	
		statsHeight.setPercentHeight(20);					//you know the drill
		GridPane.setConstraints(statsOut, 1, 1);			//puts this at bottom row, right column
		
		//Used to organize the 3 panes within our scene
		GridPane layout = new GridPane();
		layout.getChildren().addAll(controller, world, statsOut);	//puts them in their prev. assigned spots
		layout.getColumnConstraints().addAll(controlWidth, worldWidth);	//applies sizing
		layout.getRowConstraints().addAll(worldHeight, statsHeight);
		layout.setGridLinesVisible(true);
		
		//Bring it all together);
		primaryStage.setTitle("Critter_World");
		Scene scene = new Scene(layout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}

}
