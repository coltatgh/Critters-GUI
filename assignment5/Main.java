/* CRITTERS Main.java
 * EE422C Project 5 submission by
 * Ian Melendez
 * iem254
 * 16225
 * Colton Lewis
 * ctl492
 * Slip days used: <0>
 * GIT URL: https://github.com/ianmelendez95/ee422c_iem254_assignment5.git
 * Spring 2017
 */

package assignment5;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
	
	boolean printToConsole = true;
	TextArea statsT = new TextArea();
	private Timeline animation;
	private GridPane layout;
	private GridPane world;
	private ArrayList<String> statsCrits = new ArrayList<String>();
	
	private static String myPackage;
	
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	public void start(Stage primaryStage){
		
		
		primaryStage.setWidth(1040);
		primaryStage.setHeight(1040);
		
		ColumnConstraints controlWidth = new ColumnConstraints();
		controlWidth.setPercentWidth(20);	//column one is 20% and dynamically resizes
		ColumnConstraints worldWidth = new ColumnConstraints();
		RowConstraints worldHeight = new RowConstraints();
		RowConstraints statsHeight = new RowConstraints();	
		statsHeight.setPercentHeight(20);					//you know the drill
				
				
		// Stats Scene
		StackPane stats = makeStatsPane();
		GridPane.setConstraints(stats, 1, 1);			//puts this at bottom row, right column

		//Stats Control Scene
		VBox statsController = statsControlVBox();
		GridPane.setConstraints(statsController, 0, 1);
		
						
		worldHeight.setFillHeight(true);
		worldWidth.setFillWidth(true);
		worldHeight.setVgrow(Priority.ALWAYS);
		worldWidth.setHgrow(Priority.ALWAYS);
		worldHeight.setPercentHeight(80);	//same shit but with the rows
		worldWidth.setPercentWidth(80);		//This gets the remaining 80% of horizontal space
		
		
		
		//Used to organize the 3 panes within our scene
		layout = new GridPane();
		layout.getColumnConstraints().addAll(controlWidth, worldWidth);	//applies sizing
		layout.getRowConstraints().addAll(worldHeight, statsHeight);
		layout.setGridLinesVisible(true);
		
		// World
		//The pane storing all the critters
		//world.setPadding(new Insets(10,10,10,10));
		world = makeWorldPane(layout);
		GridPane.setConstraints(world, 1, 0);	//assigns this to 2nd column, first row
		//world.setGridLinesVisible(true);
		
		// Control Pane
		VBox controls = ControlsVBox(layout, world);			// Put all of the code in a helper method to make the controller there
		GridPane.setConstraints(controls, 0, 0);
		GridPane.setRowSpan(controls, 1);		//make it span both rows		
		
		layout.getChildren().addAll(controls, world, statsController, stats);	//puts them in their prev. assigned spots
		
		//Bring it all together);
		primaryStage.setTitle("Critter_World");
		Scene scene = new Scene(layout);
		primaryStage.setScene(scene);
		
		runStats();
		// Requirement, IDK why
		Critter.displayWorld(primaryStage);
		
	}
	
	public void runStats(){
		ArrayList<String> stats = getStats();
		
		StringBuilder fOut = new StringBuilder(stats.size());
		for(String str : stats){
			fOut.append(str);
		}
		
		statsT.setText(fOut.toString());
	}
	
	private ArrayList<String> getStats(){
		
		//Get list of critters
		ArrayList<String> critNames = statsCrits;
		ArrayList<Critter> critList = new ArrayList<Critter>();
		
		//System.out.println(">---------------- STATS --------------<");
		
		ArrayList<String> output = new ArrayList<String>();
		
		for(String name : critNames){
			critList.clear();
			
			try{critList.addAll(Critter.getInstances(name));}
			catch(InvalidCritterException ice){
				critList = null;
				// add a boolean hadError as a flag?
			}
			
			Class<?> critterType;
			try{
				critterType = Class.forName(myPackage + "." + name);
				Method method = critterType.getMethod("runStats", List.class);
				output.add((String)method.invoke(critterType.newInstance(), critList));
			}
			catch(Exception e){
				//Error
			}
			
			
		}
		
		
		
		//System.out.println(">-------------------------------------<");
		return output;
	}
	
	private VBox ControlsVBox(GridPane layout, GridPane world){
		VBox controls = new VBox(50);
		controls.setAlignment(Pos.TOP_LEFT);
		controls.setPadding(new Insets(30, 15, 15, 15));
		
		VBox critterControl = createCritterControl(layout, world);
		VBox discreteStepControl = createDiscreteStepControl();
		VBox animationControl = createAnimationControl();
		VBox seedControl = createSeedControl();
		HBox statsControl = createStatsControl();
		VBox metaControl = createMetaControl();
		
		controls.getChildren().addAll(
				critterControl, 
				discreteStepControl, 
				animationControl, 
				seedControl,
				metaControl);
		
		return controls;
	}
	
	private VBox createCritterControl(GridPane layout, GridPane world){
		VBox critCont = new VBox();
		//critCont.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.FULL)));
		
		Label title = new Label("Make Critter Control");
		title.setUnderline(true);
		title.setFont(new Font(15));
		title.setPadding(new Insets(0,0,10,0));
		
		ArrayList<String> names = getNames();
		
		
		HBox menuAndField = new HBox(20);
		
		TextField num = new TextField("1");
		num.setMaxWidth(50);
		
		ObservableList<String> namesO = FXCollections.observableArrayList();
		namesO.addAll(names);
		
		// Combo Box
		ComboBox<String> comboBox = new ComboBox<String>();
		comboBox.getItems().setAll(names);
		comboBox.getSelectionModel().selectFirst();
		
		// Button: Make Critter
		Button btMC = new Button("Make");
		btMC.setOnAction(event -> {
			if(!(animation.getStatus() == Animation.Status.RUNNING)){
				if(Integer.parseInt(num.getText()) > 0){	
					for(int i = 0; i < Integer.parseInt(num.getText()); i++){
						make(comboBox.getValue().toString());
					}
				} else {
					make(comboBox.getValue().toString());
				}
				resetWorld();
				//runStats();
			} else {
				System.out.println("Can't Make Right Now");
			}
		});
		
		menuAndField.getChildren().addAll(comboBox, num);
		critCont.getChildren().addAll(title, menuAndField, btMC);
		
		return critCont;
	}
	
	private VBox createAnimationControl() {
		VBox animationP = new VBox();
		
		Label title = new Label("Animation Control");
		title.setUnderline(true);
		title.setFont(new Font(15));
		title.setPadding(new Insets(0,0,10,0));
/* ---------------- IMPLEMENT ANIMATION ------------ */
		
		Label sliderInfo = new Label("Frames/Second");
		Slider slider = new Slider();
		slider.setSnapToTicks(true);
		slider.setMin(1);
		slider.setMax(5);
		slider.setValue(3);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setMajorTickUnit(1);
		slider.setMinorTickCount(0);
		
		
		
		EventHandler<ActionEvent> runP = event -> {
			Critter.worldTimeStep();
			resetWorld();
		};
		
		animation = new Timeline(new KeyFrame(Duration.millis(500), runP));
		animation.setCycleCount(Timeline.INDEFINITE);
		
		
		Button btPlay = new Button("Play/Stop");
		btPlay.setOnAction(event -> {
			if(animation.getStatus() == Animation.Status.PAUSED || animation.getStatus() == Animation.Status.STOPPED) {
				animation = new Timeline(new KeyFrame(Duration.millis((int)(1000/slider.getValue())), runP));
				animation.setCycleCount(Timeline.INDEFINITE);
				
				animation.play();
			} else {
				animation.pause();
			}
		});
		
		animationP.getChildren().addAll(title, sliderInfo, slider, btPlay);
		return animationP;
	}
	
	private VBox createDiscreteStepControl(){
		VBox stepControl = new VBox();
		
		Label title = new Label("Step Control");
		title.setPadding(new Insets(0,0,10,0));
		title.setUnderline(true);
		title.setFont(new Font(15));
		title.setPadding(new Insets(0,0,10,0));
		
		TextField num = new TextField("1");
		num.setMaxWidth(50);
		
		// Button: Step
		Button btStep = new Button("Step");
		btStep.setOnAction(event -> {
			if(animation.getStatus() != Animation.Status.RUNNING){
				for(int i = 0; i < Integer.parseInt(num.getText()); i++){
					Critter.worldTimeStep();
				}
				resetWorld();
			}
		});
		
		HBox stepH = new HBox(20);
		stepH.getChildren().addAll(num, btStep);
		
		stepControl.getChildren().addAll(title, stepH);
		
		return stepControl;
	}
	
	private VBox createSeedControl() {
		Label title = new Label("Seed Control");
		title.setPadding(new Insets(0,0,10,0));
		title.setUnderline(true);
		title.setFont(new Font(15));
		title.setPadding(new Insets(0,0,10,0));
		
		VBox seedControl = new VBox();
		
		Label seedStat = new Label("");
		
		TextField seedN = new TextField("1");
		seedN.setMaxWidth(50);
		
		Button btSeed = new Button("Set Seed");
		btSeed.setOnAction(e -> {
			long val = Long.parseLong(seedN.getText());
			if(val > 0){
				Critter.setSeed(val);
				seedStat.setText("Seed set to: " + val);
			}
		});
		
		HBox hBox = new HBox(10);
		
		hBox.getChildren().addAll(seedN, btSeed);
		seedControl.getChildren().addAll(title, hBox, seedStat);
		
		return seedControl;
	}
	
	private VBox statsControlVBox(){
		VBox statControls = new VBox(0);
		statControls.setAlignment(Pos.CENTER_LEFT);
		statControls.setPadding(new Insets(15, 15, 15, 15));
		
		Label title = new Label("Stats Control");
		title.setPadding(new Insets(0,0,10,0));
		title.setUnderline(true);
		title.setFont(new Font(15));
//		title.setPadding(new Insets(0,0,10,0));
		
		HBox critterChecks = createStatsControl();	
		statControls.getChildren().addAll(
				title,
				critterChecks);
		
		return statControls;
	}
	
	private HBox createStatsControl() {
		HBox statsControl = new HBox(20);
		
		//CheckBox
		ArrayList<String> allCrits = getNames();
		CheckBox[] critterChecks = new CheckBox[allCrits.size()];
		for (int i = 0; i < allCrits.size(); i++) {
		    final CheckBox cb = critterChecks[i] = new CheckBox(allCrits.get(i));
//		    cb.selectedProperty().addListener(new ChangeListener<Boolean>() {
//		        public void changed(ObservableValue<? extends Boolean> ov,
//		            Boolean old_val, Boolean new_val) {
//		                
//		        }
//		    });
		}
		
		// Button: Run Stats
		VBox butBox = new VBox();
		butBox.setSpacing(10);
		butBox.setPadding(new Insets(2));
		Button btStats = new Button("Update");
		btStats.setOnAction(event -> checkCheckedCrits(critterChecks));
		butBox.getChildren().add(btStats);
		
		VBox statChecks = new VBox();
		statChecks.setSpacing(1);
		statChecks.setPadding(new Insets(2));
		statChecks.getChildren().addAll(critterChecks);
		statsControl.getChildren().addAll(statChecks, butBox);
		
		return statsControl;
	}
	
	private void checkCheckedCrits(CheckBox[] myBoxes){
		ArrayList<String> updatedStatsCrits = new ArrayList<String>();
		for(CheckBox cb : myBoxes){
			if(cb.isSelected()){
				updatedStatsCrits.add(cb.getText());
			}
		}
		statsCrits = updatedStatsCrits;
		runStats();
	}
	
	private VBox createMetaControl() {
		VBox metaControl = new VBox(5);
		
		Label title = new Label("Meta Control");
		title.setPadding(new Insets(0,0,10,0));
		title.setUnderline(true);
		title.setFont(new Font(15));
		title.setPadding(new Insets(0,0,10,0));
		
		Button btRefresh = new Button("Refresh");
		btRefresh.setOnAction(e -> resetWorld());
		
		Button btReset = new Button("Clear");
		btReset.setOnAction(e -> {
			Critter.clearWorld();
			
			resetWorld();
		});
		
		Button btTerm = new Button("Terminate");
		btTerm.setOnAction(e -> System.exit(0));
		
		metaControl.getChildren().addAll(title, btRefresh, btReset, btTerm);
		
		return metaControl;
	}
	
	private void resetWorld(){
		world.getChildren().clear();
		
		int numCols = Params.world_width;
		int numRows = Params.world_height;
		
		for(int x = 0 ; x < numCols ; x++){
			for(int y = 0 ; y < numRows ; y++){
				//worldPane.add(createCell(layout), x, y);
				//worldPane.add(new Rectangle(20, 20, Color.BLACK), x, y);
				if(Critter_List.positions[x][y] == null){
					world.add(createCell(layout), x, y);
				}else{
					world.add(createCritter(layout, Critter_List.positions[x][y]), x, y);
					//System.out.print(Critter_List.positions[j][i].toString());
				}
			}
		}
		runStats();
	}
	
	private StackPane createCritter(GridPane worldPane, Critter critter){
		StackPane critP = new StackPane();
		
		Critter.CritterShape critShape = critter.viewShape();
		double scaleW = 0.75 * (1.0 / (double)Params.world_width);
		double scaleH = 0.75 * (1.0 / (double)Params.world_height);
		
		Rectangle back = new Rectangle();
		back.widthProperty().bind(worldPane.widthProperty().multiply(scaleW));
		back.heightProperty().bind(worldPane.heightProperty().multiply(scaleH));
		back.setStroke(Color.BLACK);
		back.setFill(Color.WHITE);
		
		critP.getChildren().add(back);
		
		switch(critShape){
		case CIRCLE:
			Circle circle = new Circle();
			
			if(worldPane.getWidth() > worldPane.getHeight()){
				circle.radiusProperty().bind(worldPane.heightProperty().multiply(scaleW*0.45));
			} else {
				circle.radiusProperty().bind(worldPane.widthProperty().multiply(scaleW*0.45));
			}
			
			circle.centerXProperty().bind(critP.widthProperty().divide(2));
			circle.centerYProperty().bind(critP.heightProperty().divide(2));
			
			circle.setStroke(critter.viewOutlineColor());
			circle.setFill(critter.viewFillColor());
			
			critP.getChildren().add(circle);
			critP.getStyleClass().add("cell");
			
			break;
		case SQUARE:
			Rectangle square = new Rectangle();
			
			if(worldPane.getWidth() > worldPane.getHeight()){
				square.widthProperty().bind(worldPane.heightProperty().multiply(0.9*scaleW));
				square.heightProperty().bind(worldPane.heightProperty().multiply(0.9*scaleH));
			} else {
				square.widthProperty().bind(worldPane.widthProperty().multiply(0.9*scaleW));
				square.heightProperty().bind(worldPane.widthProperty().multiply(0.9*scaleH));
			}
			
			square.setStroke(critter.viewOutlineColor());
			square.setFill(critter.viewFillColor());
			
			critP.getChildren().add(square);
			critP.getStyleClass().add("cell");
			
			break;
		case DIAMOND:
			Polygon diam = new Polygon();
			diam.getPoints().addAll(new Double[]{
					worldPane.getWidth()*(0.45*scaleW), 0.0, 
				    0.0, worldPane.getHeight()*(0.45*scaleH), 
				    worldPane.getWidth()*(0.45*scaleW), worldPane.getHeight()*(0.9*scaleH), 
				    worldPane.getWidth()*(0.9*scaleW), worldPane.getHeight()*(0.45*scaleH) });
			diam.setStroke(critter.viewOutlineColor());
			diam.setFill(critter.viewFillColor());

			critP.getChildren().add(diam);
			critP.getStyleClass().add("cell");
			break;
		case TRIANGLE:
			Polygon tri = new Polygon();
			tri.getPoints().addAll(new Double[]{
					worldPane.getWidth()*(0.45*scaleW), 0.0, 
				    0.0, worldPane.getHeight()*(0.9*scaleH), 
				    worldPane.getWidth()*(0.9*scaleW), worldPane.getHeight()*(0.9*scaleH) });
			tri.setStroke(critter.viewOutlineColor());
			tri.setFill(critter.viewFillColor());

			critP.getChildren().add(tri);
			critP.getStyleClass().add("cell");
			break;
		case STAR:
			Polygon stah = new Polygon();
			stah.getPoints().addAll(new Double[]{
					
					worldPane.getWidth()*(0.45*scaleW), 0.0,
					worldPane.getWidth()*(0.9*0.375*scaleW), worldPane.getHeight()*(0.9*0.4*scaleH),
					0.0, worldPane.getHeight()*(0.9*0.4*scaleH),
					worldPane.getWidth()*(0.9*0.31*scaleW), worldPane.getHeight()*(0.9*0.625*scaleH), 
					worldPane.getWidth()*(0.9*0.2*scaleW), worldPane.getHeight()*(0.9*1*scaleH),
					worldPane.getWidth()*(0.9*0.5*scaleW), worldPane.getHeight()*(0.9*0.775*scaleH),
					worldPane.getWidth()*(0.9*0.8*scaleW), worldPane.getHeight()*(0.9*1*scaleH),
					worldPane.getWidth()*(0.9*0.69*scaleW), worldPane.getHeight()*(0.9*0.625*scaleH),
					worldPane.getWidth()*(0.9*1*scaleW), worldPane.getHeight()*(0.9*0.4*scaleH),
					worldPane.getWidth()*(0.9*0.625*scaleW), worldPane.getHeight()*(0.9*0.4*scaleH)});
					//Please god let it end
			
			stah.setStroke(critter.viewOutlineColor());
			stah.setFill(critter.viewFillColor());

			critP.getChildren().add(stah);
			critP.getStyleClass().add("cell");
			break;
		default:
			Rectangle def = new Rectangle();
			def.widthProperty().bind(worldPane.widthProperty().multiply(scaleW));
			def.heightProperty().bind(worldPane.heightProperty().multiply(scaleH));
			
			def.setStroke(critter.viewOutlineColor());
			def.setFill(critter.viewFillColor());
			
			critP.getChildren().add(def);
			critP.getStyleClass().add("cell");
			break;
		}

		
		//Rectangle rect = new Rectangle();

		
		
		
		return critP;
	}
	
	private StackPane createCell(GridPane worldPane){
		StackPane cell = new StackPane();
		Rectangle rect = new Rectangle();
		
		
		double scaleW = 0.75 * (1.0 / (double)Params.world_width);
		double scaleH = 0.75 * (1.0 / (double)Params.world_height);
		
		rect.widthProperty().bind(worldPane.widthProperty().multiply(scaleW));
		rect.heightProperty().bind(worldPane.heightProperty().multiply(scaleH));
		rect.setStroke(Color.BLACK);
		rect.setFill(Color.WHITE);
		cell.getChildren().add(rect);
		cell.getStyleClass().add("cell");
		
		return cell;
	}
	
	private StackPane makeStatsPane(){
		StackPane stats = new StackPane();
		
		statsT.setWrapText(true);
		statsT.setEditable(false);
		/*
	
		if(printToConsole == false){
			// Prepping the string for textfield
			Console console = new Console(statsT);
			PrintStream ps = new PrintStream(console, true);
			System.setOut(ps);
			System.setErr(ps);
		}
		*/
		stats.getChildren().addAll(statsT);
		
		
		return stats;
	}
	
	private GridPane makeWorldPane(GridPane layout){
		int numCols = Params.world_width;
		int numRows = Params.world_height;
		
		GridPane worldPane = new GridPane();
		
		for(int x = 0; x < numCols; x++){
			ColumnConstraints cc = new ColumnConstraints();
			cc.setFillWidth(true);
			cc.setHgrow(Priority.ALWAYS);
			cc.setPercentWidth(100/Params.world_width);
			worldPane.getColumnConstraints().add(cc);
		}
		
		for(int y = 0; y < numRows; y++){
			RowConstraints rc = new RowConstraints();
			rc.setFillHeight(true);
			rc.setVgrow(Priority.ALWAYS);
			rc.setPercentHeight(100/Params.world_height);
			worldPane.getRowConstraints().add(rc);
		}
		
		for(int x = 0 ; x < numCols ; x++){
			for(int y = 0 ; y < numRows ; y++){
				worldPane.add(createCell(layout), x, y);
				//worldPane.add(new Rectangle(20, 20, Color.BLACK), x, y);
			}
		}
		
		//worldPane.setGridLinesVisible(true);
		worldPane.getStyleClass().add("cell");
		
		return worldPane;
	}
	
	public class Console extends OutputStream {
		private TextArea output;
		
		public Console(TextArea ta){
			this.output = ta;
		}
		
		@Override
		public void write(int i) throws IOException
		{
			output.appendText(String.valueOf((char) i));
		}
	}
	
	private void make(String critName){
		//System.out.println(critName);
		try{
			Critter.makeCritter(critName);
			//System.out.println(critName + " made successfully");
		} catch (InvalidCritterException ice){
			System.out.println("Not gonna work");
		}
		
		// TODO: Update the world
	}
	

	
	
	private ArrayList<String> getNames(){
		/*
		 * ComboBox idea:
		 * Get all the file names, cut off ".java", and then check the name with reflection with Class.isAssignableFrom(<Critter>)
		 */
		
		// Get list of files
		File file = new File("./src/assignment5");
		String[] list = file.list();
		
		// 1) Cut off "java"
		ArrayList<String> rawNames = new ArrayList<String>();
		ArrayList<Character> cutArray = new ArrayList<Character>();	// cutArray: Temp storage of the cut string
		
		
		for(String item : list){
			char[] arrayToCut = item.toCharArray();			// arrayToCut: Char array rep. of original string
			
			// Make sure its even a .java file
			if(arrayToCut[arrayToCut.length - 1] != 'a') continue;
			cutArray.clear();								// Prep char array for receiving
			for(char charA : arrayToCut){
				if(charA == '.'){
					break;
				} else {
					cutArray.add(charA);
				}
			}
			StringBuilder stringB = new StringBuilder(cutArray.size());
			for(char next : cutArray){
				stringB.append(next);
			}
			
			if(stringB.toString().equals("Critter")) continue;
			
			String fileName = myPackage + "." + stringB.toString();
			rawNames.add(stringB.toString());
		} 
		
		// 2) Get classes from the name list
		//		Rmr: Use fileNames array
		
		
		ArrayList<Class<?>> myPotentialCritters = new ArrayList<Class<?>>();
		//Object instanceOfMyCritter = null;
		
		for(int i = 0 ; i < rawNames.size() ; i++){
			//System.out.println("Trying " + rawNames.get(i));
			
			try{
				myPotentialCritters.add(Class.forName(myPackage + "." + rawNames.get(i)));
			} catch (ClassNotFoundException e) {
				rawNames.remove(i);
				i--;
				continue;
			}
		}
		
		// 3) Try isAssignable on the classes, keep on the list if successful
		
		// Simply prep a Critter class to check
		Class<?> critterC = null;
		try{
			critterC = Class.forName(myPackage + "." + "Critter");
		} catch (ClassNotFoundException e) {
			System.out.println("Failure");
		}
		
		// Parse through the classes and check that they are critters
		for(int i = 0; i < myPotentialCritters.size() ; i++){
		
			// If its not a Critter
			if(!critterC.isAssignableFrom(myPotentialCritters.get(i))){
				myPotentialCritters.remove(i);
				rawNames.remove(i);
				i--;	// Iteration needs to be adapted 
			} 
			
		}
		
		
		return rawNames;
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}

}
