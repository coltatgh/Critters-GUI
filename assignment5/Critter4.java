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

//Venus fly trap
public class Critter4 extends Critter {
	
	@Override
	public javafx.scene.paint.Color viewFillColor() {return javafx.scene.paint.Color.CYAN;}
	
	@Override
	public javafx.scene.paint.Color viewOutlineColor() {return javafx.scene.paint.Color.DARKCYAN;}
	
	@Override
	public CritterShape viewShape() {return CritterShape.CIRCLE;}
	
	@Override
	public String toString() { return "4"; }
	
	@Override
	public void doTimeStep() {
		if((getEnergy() > Params.min_reproduce_energy + 8)&&(getRandomInt(6) == 2)){
			
			int babyPos = getRandomInt(8);
			for(int i = 0; i<5; i++){
				if(look(babyPos, false) == null){
					break;
				}else{
					if(look(babyPos,false).equals("@")){
						break;
					}else{
						babyPos = getRandomInt(8);
					}
				}
			}
			
			Critter4 child = new Critter4();
			reproduce(child, babyPos);
		}

	}

	@Override
	public boolean fight(String oponent) {
		int decision = getRandomInt(5);
		if(decision > 2) return true;
		else return false;
	}

}
