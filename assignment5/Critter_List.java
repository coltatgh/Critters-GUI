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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/* Holds all critters in the world, and provides means of organizing them throughout the stages of
 * the time steps.
 */
public class Critter_List {
	public static ArrayList<Critter> adults = new ArrayList<Critter>();
	public static ArrayList<Critter> babies = new ArrayList<Critter>();
	//when have 2 in the same spot, just put the first in the array & mark rest conflict = true
	public static Critter[][] positions = new Critter[Params.world_width][Params.world_height];
	public static Critter[][] ghosts = new Critter[Params.world_width][Params.world_height];
	
	/**
	 * Transfer all the babies to the adults list
	 */
	public static void matureBabies(){
		adults.addAll(babies);
		babies.clear();
	}
	
	public static void memorize(){
		for(int i=0; i < Params.world_width; i++){
			for(int j=0; j < Params.world_height; j++){
				ghosts[i][j] = positions[i][j];
			}
		}
	}
}
