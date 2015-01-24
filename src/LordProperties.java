import java.util.HashMap;

public class LordProperties {

	private HashMap<Integer, Integer> prop = new HashMap<Integer, Integer>();

	public void put(int lordId, int value) {
		prop.put(lordId, value);
	}
	
	public int get(int lordId){
		return prop.get(lordId);
	}
}
