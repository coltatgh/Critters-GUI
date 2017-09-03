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

//zig-zags across the world
//randomly decides whether to fight
public class Critter1 extends Critter {
	
	private int dir;
	private int babyDir;
	
	public Critter1(){dir = 7;}
	
	@Override
	//TODO explain to me why we were using viewColor rather than viewFillColor
	public javafx.scene.paint.Color viewFillColor() {return javafx.scene.paint.Color.SALMON;}
	
	@Override
	public javafx.scene.paint.Color viewOutlineColor() {return javafx.scene.paint.Color.DARKRED;}
	
	@Override
	public CritterShape viewShape() {return CritterShape.DIAMOND;}
	
	@Override
	public String toString() { return "1";}
	
	@Override
	public void doTimeStep() {
		walk(dir);
		if(dir == 7){
			dir = 5;
		}else if(dir == 5){
			dir = 7;
		}
		
		//Added dice-roll to slow reproduction some
		if((this.getEnergy() > (Params.min_reproduce_energy + Params.look_energy_cost))&&(Critter.getRandomInt(5)==3)){
			
			if(look(dir, false) == null){
				babyDir = dir;
			}else if(look(dir,false).equals("@")){
					babyDir = dir;
				}else{
					babyDir = dir-4;
				}
			
			Critter1 child = new Critter1();
			reproduce(child, babyDir);
		}
	}

	@Override
	public boolean fight(String oponent) {
		if(oponent.equals("4")){
			return true;
		}else{
			int num = Critter.getRandomInt(2);
			if(num < 1){
				return false;
			}else{
				return true;
			}
		}
	}
}
