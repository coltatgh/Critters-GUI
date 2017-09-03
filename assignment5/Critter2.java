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

//Walks in circles until it encounters a critter, reverses course if it's not algae
//Eats algae, attempts to run from all else
public class Critter2 extends Critter {

	@Override
	public javafx.scene.paint.Color viewFillColor() {return javafx.scene.paint.Color.ORCHID;}
	
	@Override
	public javafx.scene.paint.Color viewOutlineColor() {return javafx.scene.paint.Color.DARKMAGENTA;}
	
	@Override
	public CritterShape viewShape() {return CritterShape.TRIANGLE;}
	
	int dir;
	int dirChange;
	
	public Critter2(){
		dir = Critter.getRandomInt(7);
		dirChange = 1;
		}
	
	@Override
	public String toString(){return "2";}
	
	@Override
	public void doTimeStep() {
		
		//if path holds enemy
		if(look(dir,false) == null){
			//continue, this is to eliminate null ptr exceptions
		}else if((!look(dir,false).equals("@"))&&(!look(dir,false).equals("4"))){
			//This means we have a critter and it's not algae
			dir = (dir-4)%8;			//reverses direction
			dirChange = dirChange*(-1);	//will circle this opposite direction now
			}else{
				//otherwise continue as expected
			}
		
		walk(dir);
		dir = (dir + dirChange)%8;
		
		// 1 in 3 chance of reproduction
		if((this.getEnergy() > Params.min_reproduce_energy)&&(getRandomInt(5) == 1)){
			Critter2 child = new Critter2();
			reproduce(child, dir);
		}
		
	}

	@Override
	public boolean fight(String oponent) {
		if((oponent.equals("@"))||(oponent.equals("4"))){return true;}
		else{
			run(dir);
			return false;
			}
	}

}
