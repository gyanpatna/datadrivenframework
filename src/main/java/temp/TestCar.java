package temp;

import java.util.Date;

public class TestCar {

	public static void main(String[] args) {
		MarutiCar m = new MarutiCar(); //child class
		m.start();
		m.refuel();
		m.musicSystem();
		
		Car c = new Car(); //parent class
		c.start();
		c.refuel();
		
		Date d = new Date();
		System.out.println(d);
	}

}
