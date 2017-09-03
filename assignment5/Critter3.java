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

import java.util.List;


//Clever strategist
public class Critter3 extends Critter{

	@Override
	public javafx.scene.paint.Color viewFillColor() {return javafx.scene.paint.Color.SLATEGREY;}
	
	@Override
	public javafx.scene.paint.Color viewOutlineColor() {return javafx.scene.paint.Color.BLACK;}
	
	@Override
	public CritterShape viewShape() {return CritterShape.STAR;}
	
	@Override
	public void doTimeStep() {
		
		if(getEnergy()>8){
			
			int bestStep = 0;
			for(int i=0; i<8; i++){
				//make sure not empty cuz null ptr exceptions
				if(look(i,false) == null){
					bestStep = i;
				}else{
					//if not empty prioritize finding algae
					if(look(i,false).equals("@")){
						bestStep = i;
						break;
					}
				}
			}
			//take whatever the best option was
			walk(bestStep);
		}
			
		if((getEnergy() > (Params.min_reproduce_energy + 10))&&(getRandomInt(3) == 2)){
			Critter3 child = new Critter3();
			reproduce(child, 0);
		}
		
	}

	@Override
	public boolean fight(String opponent) {
		boolean answer = true;
		
		//if it's algae or we're feeling tough, fight
		if ((getEnergy() > Params.min_reproduce_energy)||(opponent.equals("@"))){answer = true;}
		//otherwise try to run
		else{
			//if can find an opening to escape, do it
			for(int i=0; i<8; i++){
				if((look(i,true) == null)&&(getEnergy() > Params.run_energy_cost)){
					run(i);
					answer = false;
					break;
				}
			}
			//if break out of loop without escaping fight, default to standing our ground
		}
		return answer;
	}
	
	@Override
	public String toString() {
		return "3";
	}
	
}
