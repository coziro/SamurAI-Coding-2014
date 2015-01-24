public class Lord {
	private int id;
	private int strength;
	private double myPosition;
	private int strategy;

	public Lord(int id, int strength) {
		this.id = id;
		this.strength = strength;
	}
	
	public Lord(int id, int strength, double myPosition){
		this.id = id;
		this.strength = strength;
		this.myPosition = myPosition;
	}
	
	public double getMyPosition() {
		return myPosition;
	}
	

	public int getStrength() {
		return strength;
	}

	public int getId() {
		return id;
	}

	public void setMyPosition(double myPosition) {
		this.myPosition = myPosition;
	}

	public void setStrategy(int strategy) {
		this.strategy = strategy;
	}

	public int getStrategy() {
		return this.strategy;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("id: " + id + ", strength: " + strength);
		return sb.toString();
	}
}
