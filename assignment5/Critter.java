/* CRITTERS Main.java
 * EE422C Project 5 submission by=
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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javafx.stage.Stage;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {
	/* NEW FOR PROJECT 5 */
	public enum CritterShape {
		CIRCLE,
		SQUARE,
		TRIANGLE,
		DIAMOND,
		STAR
	}
	
	public javafx.scene.paint.Color viewColor() {return javafx.scene.paint.Color.WHITE;}
	public javafx.scene.paint.Color viewOutlineColor() { return viewColor(); }
	public javafx.scene.paint.Color viewFillColor() { return viewColor(); }
	
	public abstract CritterShape viewShape(); 
	
	private boolean conflict = false;
	private boolean hasMoved = false;
	private static boolean timeSteppin = false;
	
	private static String myPackage;

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	
	protected final String look(int direction, boolean steps) {
		//if steps = true distance is 2; if steps = false distance is 1
		int distance = (steps)? 2:1;	
		String neighbor = null;
		int posX = this.x_coord; 		//X position we are looking at
		int posY = this.y_coord;		//Y position we are looking at
		
		//TODO Extract this switch into a getPosition method cuz we've retyped it 4 times...
		//For now utilize the switch from walk and run with some modifications
		switch(direction){
		case 0: 
			posX = (x_coord + distance) % Params.world_width;
			break;
		case 1:
			posX = (x_coord + distance) % Params.world_width;
			posY = (y_coord - distance) % Params.world_height;
			break;
		case 2: 
			posY = (y_coord - distance) % Params.world_height;
			break;
		case 3:
			posX = (x_coord - distance) % Params.world_width;
			posY = (y_coord - distance) % Params.world_height;
			break;
		case 4:
			posX = (x_coord - distance) % Params.world_width;
			break;
		case 5:
			posX = (x_coord - distance) % Params.world_width;
			posY = (y_coord + distance) % Params.world_height;
			break;
		case 6:
			posY = (y_coord + distance) % Params.world_height;
			break;
		case 7:
			posX = (x_coord + distance) % Params.world_width;
			posY = (y_coord + distance) % Params.world_height;
			break;
		}
		
		if(posX < 0){
			posX = Params.world_width + posX;
		}
		if(posY < 0){
			posY = Params.world_height + posY;
		}
		
		//If we're in time step, have to use ghost grid
		if(timeSteppin){
			if(Critter_List.ghosts[posX][posY] == null){
				neighbor = null;
			}else{
				neighbor = Critter_List.ghosts[posX][posY].toString();
			}
		}else{
			//otherwise use updated grid
			if(Critter_List.positions[posX][posY] == null){
				neighbor = null;
			}else{
				neighbor = Critter_List.positions[posX][posY].toString();
			}
		}
		
		//pay your dues
		energy -= Params.look_energy_cost;
		return neighbor;
	}
	
	//Old Code from here on out?
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	/**
	 * Set the random seed to a given value
	 * @param new_seed - value to set seed
	 */
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;
	
	/**
	 * Make critter walk in given direction if it is appropriate to do so
	 * @param direction - direction to walk
	 */
	protected final void walk(int direction) {
		
		/*
		 * 1) Change x and y coord 
		 * 2) Check if x and y coord are taken in world
		 * 		if true Set conflict flag
		 * 
		 * 3) Set hasMoved flag
		 * 4) Fix energy
		 */
		
		
		
		// Adjust "placeholder" x and y coords (NOT technically moved yet) [Make sure it can even literally move]
		if(!hasMoved){
			// Remove critter from its current position IF it is in that position in the world// Remove critter from its current position 
			if(Critter_List.positions[x_coord][y_coord] == this){
				Critter_List.positions[x_coord][y_coord] = null;
			} 
			
			
			switch(direction){
			case 0: 
				x_coord = (x_coord + 1) % Params.world_width;
				break;
			case 1:
				x_coord = (x_coord + 1) % Params.world_width;
				y_coord = (y_coord - 1) % Params.world_height;
				break;
			case 2: 
				y_coord = (y_coord - 1) % Params.world_height;
				break;
			case 3:
				x_coord = (x_coord - 1) % Params.world_width;
				y_coord = (y_coord - 1) % Params.world_height;
				break;
			case 4:
				x_coord = (x_coord - 1) % Params.world_width;
				break;
			case 5:
				x_coord = (x_coord - 1) % Params.world_width;
				y_coord = (y_coord + 1) % Params.world_height;
				break;
			case 6:
				y_coord = (y_coord + 1) % Params.world_height;
				break;
			case 7:
				x_coord = (x_coord + 1) % Params.world_width;
				y_coord = (y_coord + 1) % Params.world_height;
				break;
			}
			
			if(x_coord < 0){
				x_coord = Params.world_width + x_coord;
			}
			if(y_coord < 0){
				y_coord = Params.world_height + y_coord;
			}
		}
		
		// If position is taken in the world, flag for POTENTIAL conflict
		try{
		if(Critter_List.positions[x_coord][y_coord] != null && Critter_List.positions[x_coord][y_coord] != this && !Critter_List.babies.contains(Critter_List.positions[x_coord][y_coord])){
			conflict = true;
		} else {
			Critter_List.positions[x_coord][y_coord] = this;
			conflict = false;				// Successful move
		}
		} 
		catch (ArrayIndexOutOfBoundsException e){
			System.out.println("Here");
		}
		
		hasMoved = true;								// Self explanatory
		energy = energy - Params.walk_energy_cost;		// Calling always drains energy
		
		
		
	}
	
	/**
	 * Make critter run (move 2 spaces) in a given direction if appropriate to do so
	 * @param direction - direction to move
	 */
	protected final void run(int direction) {
		
		// Adjust "placeholder" x and y coords (NOT technically moved yet) [Make sure it can even literally move]
		if(!hasMoved){
			if(Critter_List.positions[x_coord][y_coord] == this){
				Critter_List.positions[x_coord][y_coord] = null;
			} 
			
			switch(direction){
			case 0: 
				x_coord = (x_coord + 2) % Params.world_width;
				break;
			case 1:
				x_coord = (x_coord + 2) % Params.world_width;
				y_coord = (y_coord - 2) % Params.world_height;
				break;
			case 2: 
				y_coord = (y_coord - 2) % Params.world_height;
				break;
			case 3:
				x_coord = (x_coord - 2) % Params.world_width;
				y_coord = (y_coord - 2) % Params.world_height;
				break;
			case 4:
				x_coord = (x_coord - 2) % Params.world_width;
				break;
			case 5:
				x_coord = (x_coord - 2) % Params.world_width;
				y_coord = (y_coord + 2) % Params.world_height;
				break;
			case 6:
				y_coord = (y_coord + 2) % Params.world_height;
				break;
			case 7:
				x_coord = (x_coord + 2) % Params.world_width;
				y_coord = (y_coord + 2) % Params.world_height;
				break;
			}
			
			if(x_coord < 0){
				x_coord = Params.world_width + x_coord;
			}
			if(y_coord < 0){
				y_coord = Params.world_height + y_coord;
			}
		}
		
		// If position is taken in the world, flag for POTENTIAL conflict
		if(Critter_List.positions[x_coord][y_coord] != null && Critter_List.positions[x_coord][y_coord] != this && !Critter_List.babies.contains(Critter_List.positions[x_coord][y_coord])){
			conflict = true;
		} else {
			Critter_List.positions[x_coord][y_coord] = this;
			conflict = false; 		// Successful move
		}
		
		hasMoved = true;								// Self explanatory
		energy = energy - Params.run_energy_cost;		// Calling always drains energy
		
	}
	
	/**
	 * Realize a new critter of the given type in the direction given
	 * @param offspring - critter to be realized 
	 * @param direction - direction to realize from parent
	 */
	protected final void reproduce(Critter offspring, int direction) {
		if(getEnergy() < Params.min_reproduce_energy) return;
		
		// Account for energy
		offspring.energy = energy/2;
		energy = (energy + 1)/2;
		
		// Create offset
		switch(direction){
		case 0: 
			offspring.x_coord = (x_coord + 1) % Params.world_width;
			offspring.y_coord = y_coord;
			break;
		case 1:
			offspring.x_coord = (x_coord + 1) % Params.world_width;
			offspring.y_coord = (y_coord - 1) % Params.world_height;
			break;
		case 2: 
			offspring.y_coord = (y_coord - 1) % Params.world_height;
			offspring.x_coord = x_coord;
			break;
		case 3:
			offspring.x_coord = (x_coord - 1) % Params.world_width;
			offspring.y_coord = (y_coord - 1) % Params.world_height;
			break;
		case 4:
			offspring.x_coord = (x_coord - 1) % Params.world_width;
			offspring.y_coord = y_coord;
			break;
		case 5:
			offspring.x_coord = (x_coord - 1) % Params.world_width;
			offspring.y_coord = (y_coord + 1) % Params.world_height;
			break;
		case 6:
			offspring.y_coord = (y_coord + 1) % Params.world_height;
			offspring.x_coord = x_coord;
			break;
		case 7:
			offspring.x_coord = (x_coord + 1) % Params.world_width;
			offspring.y_coord = (y_coord + 1) % Params.world_height;
			break;
		}
		
		if(offspring.x_coord < 0){
			offspring.x_coord = Params.world_width + offspring.x_coord;
		}
		if(offspring.y_coord < 0){
			offspring.y_coord = Params.world_height + offspring.y_coord;
		}
		
		// Baby is ready, add to array
		Critter_List.babies.add(offspring);
		
		// Realize baby in world if appropriate
		if(Critter_List.positions[offspring.x_coord][offspring.y_coord] == null){
			Critter_List.positions[offspring.x_coord][offspring.y_coord] = offspring;
		}
		
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		critter_class_name = myPackage + "." + critter_class_name;
		makeBabies(critter_class_name);
		Critter_List.matureBabies();
	}
	
	/**
	 * Create a new instance of the given class name, and add to grid if appropriate
	 * @param critter_class_name - name of the class to be instantiated
	 * @throws InvalidCritterException - if critter_class_name does not correspond to an existing class name, throw this exception
	 */
	public static void makeBabies(String critter_class_name) throws InvalidCritterException {
		// Code from example on Piazza
		Class<?> myCritter = null;
		Constructor<?> constructor = null;
		Object instanceOfMyCritter = null;

		try {
			myCritter = Class.forName(critter_class_name);
		} catch (ClassNotFoundException e) {
			throw new InvalidCritterException(critter_class_name);
		}
		try {
			constructor = myCritter.getConstructor();		// No-parameter constructor object
			instanceOfMyCritter = constructor.newInstance();	// Create new object using constructor
		} catch (Exception e) {
			// Do whatever is needed to handle the various exceptions here -- e.g. rethrow Exception
		}
		Critter noob = (Critter)instanceOfMyCritter;		// Cast to Critter
		noob.x_coord = getRandomInt(Params.world_width);
		noob.y_coord = getRandomInt(Params.world_height);
		noob.energy = Params.start_energy;
		
		Critter_List.babies.add(noob);
		if(Critter_List.positions[noob.x_coord][noob.y_coord] == null){
			try{
			Critter_List.positions[noob.x_coord][noob.y_coord] = noob;
			} catch (IndexOutOfBoundsException e){
				System.out.println("X: " + noob.x_coord + "Y: " + noob.y_coord);
			}
		}
	}
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
		Class<?> critterType;
		critter_class_name = myPackage + "." + critter_class_name;
		try{critterType = Class.forName(critter_class_name);}
		catch(ClassNotFoundException e){
			throw new InvalidCritterException(critter_class_name);
		}
		//add all instances of the given class to the list
		for(Critter c : Critter_List.adults){
			if(critterType.isInstance(c)){
				result.add(c);
			}
		}
		
		return result;
	}
	
	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static String runStats(List<Critter> critters) {
		String output;
		
		//System.out.print("" + critters.size() + " critters as follows -- ");
		
		output = critters.size() + " critters as follows -- ";
		
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			output = output + prefix + s + ":" + critter_count.get(s);
			//System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		//System.out.println();
		
		output = output + "\n";
		
		
		return output;
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return Critter_List.adults;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return Critter_List.babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		Critter_List.adults.clear();
		Critter_List.babies.clear();
		Critter_List.positions = new Critter[Params.world_width][Params.world_height];
		Critter_List.ghosts = new Critter[Params.world_width][Params.world_height];
		
	}
	
	
	/**
	 * Make all adult critters do timestep, handle encounters/fights, and account for deaths and births
	 */
	public static void worldTimeStep() {
		
		Critter_List.memorize();
		timeSteppin = true;
		
		//doTimeStep for each critter
		for(Critter c : Critter_List.adults){

			c.energy -= Params.rest_energy_cost;
			c.doTimeStep();
		}
		
		timeSteppin = false;
		
		// 'Sweep' for dead critters
		Critter current;
		for(int i = 0; Critter_List.adults.size() > i; i++){
			current = Critter_List.adults.get(i);
			if(Critter_List.adults.get(i).energy <= 0){
				if(Critter_List.positions[current.x_coord][current.y_coord] == current){
					Critter_List.positions[current.x_coord][current.y_coord] = null;
				}
				Critter_List.adults.remove(i);
				i--;
			}
		}
		
		//handle movement on grid (Critter_List.position) done in walk or run?
		//handle encounters (I'm thinking conflict field in Critter)
		for(Critter c : Critter_List.adults){
			if(c.hasMoved == false && Critter_List.positions[c.x_coord][c.y_coord] != c && !Critter_List.babies.contains(Critter_List.positions[c.x_coord][c.y_coord])){	
				c.conflict = true;
			}
			
			if(c.conflict){
				//false alarm, the position has opened up!
				if(Critter_List.positions[c.x_coord][c.y_coord] == null){
					Critter_List.positions[c.x_coord][c.y_coord] = c;
				}else{
					//gotta fight
					resolveEncounters(c);
				}
				c.conflict = false;
			}
		}
		
		// All critters lose rest energy regardless of what they have done
		for(Critter c : Critter_List.adults){
			c.hasMoved = false;
		}
		
		// Handle births and deaths
		for(int i = 0; Critter_List.adults.size() > i; i++){
			current = Critter_List.adults.get(i);
			if(Critter_List.adults.get(i).energy <= 0){
				if(Critter_List.positions[current.x_coord][current.y_coord] == current){
					Critter_List.positions[current.x_coord][current.y_coord] = null;
				}
				Critter_List.adults.remove(i);
				i--;
			}
		}
		Critter_List.matureBabies();
		
		try{
			for(int i = 0; i < Params.refresh_algae_count; i++) makeCritter("Algae");
		}catch(InvalidCritterException e){
			//fuxk
		}	
	}
	
	/**Called during time step for all Critters that moved to an occupied space
	 * @param c Critter marked for conflict
	 */
	private static void resolveEncounters(Critter c){
		Critter winner;
		Critter loser;
		
		Critter foe = Critter_List.positions[c.x_coord][c.y_coord];
		
		// Battlefield
		int bx_coord = c.x_coord;
		int by_coord = c.y_coord;
		//Decide who wants to fight
		boolean foeAgro = foe.fight(c.toString());
		boolean cAgro = c.fight(foe.toString());
		
		int foePow;
		int cPow;
		
		//Check for unsuccessful escapes
		if(c.x_coord != bx_coord || c.y_coord != by_coord){
			//If moved unsuccessfully
			if(c.conflict == true){
				c.x_coord = bx_coord;
				c.y_coord = by_coord;
			}
		}
		if(foe.x_coord != bx_coord || foe.y_coord != by_coord){
			//If moved unsuccessfully
			if(foe.conflict == true){
				foe.x_coord = bx_coord;
				foe.y_coord = by_coord;
			} else if(c.x_coord == bx_coord && c.y_coord == by_coord){
				//If foe moved but c stayed
				Critter_List.positions[c.x_coord][c.y_coord] = c; 
				return;
			}
		}
		
		//if foe dies
		if(foe.energy <= 0){
			//remove it from the grid
			if(Critter_List.positions[foe.x_coord][foe.y_coord] == foe) Critter_List.positions[foe.x_coord][foe.y_coord] = null;

			if((c.energy <= 0)){
				if(Critter_List.positions[c.x_coord][c.y_coord] == c) Critter_List.positions[c.x_coord][c.y_coord] = null;
				return;
			}
		}
		
		//if c dies
		if(c.energy<=0){
			//remove it from the grid
			if(Critter_List.positions[c.x_coord][c.y_coord] == c) Critter_List.positions[c.x_coord][c.y_coord] = null;
			
			if(foe.energy <= 0){
				if(Critter_List.positions[foe.x_coord][foe.y_coord] == foe) Critter_List.positions[foe.x_coord][foe.y_coord] = null;
				return;
			}
		}
		
		if((c.x_coord == foe.x_coord) && (c.y_coord == foe.y_coord)){
			
			
			

			if(foe.energy <= 0){
				foePow = -1;
				foe.energy = 0;
			} else if(!foeAgro){
				foePow = 0;
			} else {
				foePow = getRandomInt(foe.energy);
			}
			
	
			if(c.energy <= 0){
				cPow = -1;
				c.energy = 0;
			} else if(!cAgro) {
				cPow = 0;
			} else {
				cPow = getRandomInt(c.energy);
			}

			
			if(foePow >= cPow){
				winner = foe;
				loser = c;
			}else{
				winner = c;
				loser = foe;
			}
			winner.energy += loser.energy/2;	//round down
			loser.energy = 0;
			//Assign the winner to the contested position on the grid
			Critter_List.positions[bx_coord][by_coord] = winner;
			
		}
		
		
				
	}
	
	/**
	 * Print out a visual representation of the current world state
	 */
	public static void displayWorld(Stage primaryStage) {
		
		primaryStage.show();
		
		/*// Realize critters that have a chance now
		for(Critter c: Critter_List.adults){
			if(Critter_List.positions[c.x_coord][c.y_coord] == null){
				Critter_List.positions[c.x_coord][c.y_coord] = c;
			}
		}
		
		// -1 to n+1 to include borders (NOT NECESSARY)
		for(int i = -1; i<Params.world_height+1; i++){
			for(int j = -1; j<Params.world_width+1; j++){
				
				//Print corners (P5 NOT NECESSARY
				if(((i==-1)&&(j==-1))||((i==-1)&&(j==Params.world_width))||((i==Params.world_height)&&(j==-1))||((i==Params.world_height)&&(j==Params.world_width))){
					System.out.print("+");
				}else{
					//Print top and bottom edges (NOT NECESSARY)
					if((i==-1)||(i==Params.world_height)){
						System.out.print("-");
					}else{
						//Print left and right edges (NOT NECESSARY)
						if((j==-1)||(j==Params.world_width)){
							System.out.print("|");
						}else{
							
							//----------------------------------
							//Print the grid
							if(Critter_List.positions[j][i] == null){
								System.out.print(" ");
							}else{
								System.out.print(Critter_List.positions[j][i].toString());
							}l
							//----------------------------------
						}
					}
				}
			}
			//Reached the end of a line so
			System.out.println("");
		}*/
		
	}

}
