import java.util.ArrayList;

public class Command {

	private ArrayList<Integer> item;
	private int commandRest; // 残りコマンド数

	public Command(char time) {
		item = new ArrayList<Integer>();
		if (time == 'D') {
			commandRest = 5;
		} else if (time == 'N') {
			commandRest = 2;
		}
	}
	
	public int getCommand(int i){
		return item.get(i);
	}

	public void add(int lordId) {
		item.add(lordId);
		commandRest--;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < item.size(); i++) {
			sb.append(item.get(i));
			sb.append(" ");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public boolean undone() {
		if (commandRest > 0) {
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) {
		Command command = new Command('N');
		while (command.undone()) {
			command.add(2);
		}
		String s = command.toString();
		System.out.println(s);
	}
}