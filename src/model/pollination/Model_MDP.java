package model.pollination;

import java.io.FileNotFoundException;

import java.util.*;
import model.Printer;
import model.MersenneTwisterFast;

public class Model_MDP {

	public static MersenneTwisterFast mt = new MersenneTwisterFast();
	private ArrayList<Plant_MDP> plants;
	private Pollinator_MDP bee_a, bee_b;
	private Printer output;
	
	public Model_MDP(String file, int num_plants) throws FileNotFoundException {
		plants = new ArrayList<Plant_MDP>(0);
		bee_a = new Pollinator_MDP(1);
		bee_b = new Pollinator_MDP(2);
		output = new Printer(file, "time\tpid\tptype\tfit_a\tfit_b\tattract_a\tattract_b");
		output.printHeader();
		initPlants(num_plants);
	}
	
	public void run(int steps) {
		ArrayList<Plant_MDP> new_plants;
		ArrayList<Integer> good_plants;
		ArrayList<Integer> good_plants_1;
		ArrayList<Integer> good_plants_2;
		ArrayList<Integer> plants_1;
		ArrayList<Integer> plants_2;
		Plant_MDP plant_temp;
		Plant_MDP temp;
		
		for(int i = 0; i < steps; i++) {
			for(int j = 0; j < 500; j++) {
				if(mt.nextInt(2)+2 % 2 == 0)
					bee_a.move(plants);
				else
					bee_b.move(plants);
			}
			
			if(i % 10 == 0) { 
				for(int k = 0; k < 100; k++) {
					plant_temp = plants.get(k);
	//				if(plant_temp.id == 1) // change this to getjust one plant's pid in file, or remove for to get all plants
						output.printData(Integer.toString(i) + "\t" + Integer.toString(plant_temp.id) + "\t" + Integer.toString(plant_temp.plant_type) 
							+ "\t" + Integer.toString(plant_temp.fit_a) + "\t" + Integer.toString(plant_temp.fit_b) + "\t"
							+ Integer.toString(plant_temp.attract_a) + "\t" + Integer.toString(plant_temp.attract_b));
				}
			}
		
			good_plants = new ArrayList<Integer>(0);
			good_plants_1 = new ArrayList<Integer>(0);
			good_plants_2 = new ArrayList<Integer>(0);
			plants_1 = new ArrayList<Integer>(0);
			plants_2 = new ArrayList<Integer>(0);
			new_plants = new ArrayList<Plant_MDP>(0);
			int num_plants = 0;
			int num_plants_1 = 0;
			int num_plants_2 = 0;
					
			for(int k = 0;k < 100;k++)
			{ 
				temp = plants.get(k);
				if(temp.plant_type == 1)
				{	
					plants_1.add(temp.id);
				}	
				if(temp.plant_type == 2)
				{	
					plants_2.add(temp.id);
				}	
				for(int j = 0; j < Math.min(3,temp.num_st_pollen_grains_good); j++)
				{					
					if(temp.plant_type == 1)
					{
						good_plants_1.add(temp.id);
						num_plants_1++;
					}
					else if(temp.plant_type == 2)
					{
						good_plants_2.add(temp.id);
						num_plants_2++;
					}
					good_plants.add(temp.id);
					num_plants++;
				}
			}
			
			int num_new_plants = 0;
			int num_new_plants_1 = 0;
			int num_new_plants_2 = 0;
			
		//	System.out.println(i + "\t" + num_plants + "\t" + num_plants_1 + "\t" + num_plants_2);
		/*	
			while(num_new_plants < 100) {
				int rannum = mt.nextInt(num_plants);
				int tempid = good_plants.get(rannum);
			//	System.out.println(num_new_plants + "\t" + rannum + "\t" + tempid );
				Plant_MDP temp = plants.get(tempid);
			//	temp.PrintPlant();
				new_plants.add(plants.get(tempid).reproduce(plants, num_new_plants));
				good_plants.remove(rannum);
				num_plants--;
			//	new_plants.get(num_new_plants).PrintPlant();
				num_new_plants++;
				
			}
			*/
			
			while(num_new_plants_1 < 50 & !good_plants_1.isEmpty()) 
			{
				int rannum = mt.nextInt(num_plants_1);
				int tempid = good_plants_1.get(rannum);
				temp = plants.get(tempid);
				new_plants.add(plants.get(tempid).reproduce(plants, num_new_plants));
				good_plants_1.remove(rannum);
				num_plants_1--;
				num_new_plants_1++;
				num_new_plants++;
			}
			
			int num_self_1 = 50 - num_new_plants_1;
			while(num_self_1 > 0)
			{
				int rannum = mt.nextInt(50);
				int tempid = plants_1.get(rannum);
				System.out.println(tempid);
				temp = 	new Plant_MDP(plants.get(tempid).attract_a, plants.get(tempid).attract_b,plants.get(tempid).fit_a,plants.get(tempid).fit_b, num_new_plants, 1);
				new_plants.add(temp);
				num_self_1--;
				num_new_plants_1++;
				num_new_plants++;
			}
			
			while(num_new_plants_2 < 50 & !good_plants_2.isEmpty()) 
			{
				int rannum = mt.nextInt(num_plants_2);
				int tempid = good_plants_2.get(rannum);
				temp = plants.get(tempid);
				new_plants.add(plants.get(tempid).reproduce(plants, num_new_plants));
				good_plants_2.remove(rannum);
				num_plants_2--;
				num_new_plants_2++;
				num_new_plants++;
			}
	
			int num_self_2 = 50 - num_new_plants_2;
			while(num_self_2 > 0)
			{
				int rannum = mt.nextInt(50);
				int tempid = plants_2.get(rannum);
				temp = 	new Plant_MDP(plants.get(tempid).attract_a, plants.get(tempid).attract_b,plants.get(tempid).fit_a,plants.get(tempid).fit_b, num_new_plants, 2);
				new_plants.add(temp);
				num_self_2--;
				num_new_plants_2++;
				num_new_plants++;
			}
			
			System.out.println(i + "\t" + num_new_plants_1 + "\t" + num_self_1 + "\t" + num_new_plants_2 + "\t" + num_self_2);
			
			
			this.plants = new_plants;
		}
	}
	
	private void initPlants(int num_plants) {
		for(int i = 0; i < num_plants/2; i++) 
			{
				plants.add(new Plant_MDP(mt.nextInt(11), mt.nextInt(11), mt.nextInt(11),mt.nextInt(11), i, 1));
			}
		for(int i = num_plants/2; i < num_plants; i++) 
			{
				plants.add(new Plant_MDP(mt.nextInt(11), mt.nextInt(11), mt.nextInt(11),mt.nextInt(11), i, 2));
			}
			
	}
	
	
}
