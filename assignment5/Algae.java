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
import assignment5.Critter.TestCritter;

public class Algae extends TestCritter {

	public String toString() { return "@"; } 
	
	public boolean fight(String opponent) {
	
		if (toString().equals(opponent)) { // same species as me!
			/* try to move away */ 
			walk(Critter.getRandomInt(8));
		} 
		return false; 
	} 
	
	public void doTimeStep() {
		setEnergy(getEnergy() + Params.photosynthesis_energy_amount);
	}
	
	public CritterShape viewShape() { return CritterShape.CIRCLE; } 
	
	public javafx.scene.paint.Color viewColor() { 
			return javafx.scene.paint.Color.GREEN; 
			} 
}

/* 
import assignment5.Critter.TestCritter;

public class Algae extends TestCritter {
	
	@Override
	public javafx.scene.paint.Color viewColor() { 
		return javafx.scene.paint.Color.GREEN; 
	}
	
	public String toString() { return "@"; }
	
	public boolean fight(String not_used) { return false; }
	
	public void doTimeStep() {
		setEnergy(getEnergy() + Params.photosynthesis_energy_amount);
	}
}
*/