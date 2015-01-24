import java.util.ArrayList;
import java.util.List;

public class LordManager {
	private static int lordNum = 6;
	private List<Lord> lordList = new ArrayList<Lord>();
	private List<Lord> sortedLordListOld = new ArrayList<Lord>();

	public static int getLordNum() {
		return lordNum;
	}

	public void addLord(Lord lord) {
		lordList.add(lord);
	}

	public Lord getLord(int lordId) {
		return lordList.get(lordId);
	}

	public void sort() {
		addToSortedList(6, this.sortedLordListOld);
		addToSortedList(5, this.sortedLordListOld);
		addToSortedList(4, this.sortedLordListOld);
		addToSortedList(3, this.sortedLordListOld);
	}

	public List<Lord> getSortedLordList() {
		return this.sortedLordListOld;
	}

	private void addToSortedList(int s, List<Lord> list) {
		for (int l = 0; l < lordNum; l++) {
			Lord lord = lordList.get(l);
			int strength = lord.getStrength();
			if (strength == s) {
				list.add(lord);
			}
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int l = 0; l < lordNum; l++) {
			Lord lord = lordList.get(l);
			sb.append(lord.toString());
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}
	
	public List<Lord> sortLordByMyPosition() {
		// バブルソート
		List<Lord> sortedLordList = LordManager.copyLordList(this.lordList);
		for (int i = 0; i < sortedLordList.size() - 1; i++) {
			for (int j = sortedLordList.size() - 1; j > i; j--) {
				double p1 = sortedLordList.get(j).getMyPosition();
				double p2 = sortedLordList.get(j - 1).getMyPosition();
				if (p1 > p2) {
					Lord temp = sortedLordList.get(j);
					sortedLordList.set(j, sortedLordList.get(j - 1));
					sortedLordList.set(j - 1, temp);
				}
			}
		}
		return sortedLordList;
	}

	public static List<Lord> copyLordList(List<Lord> lordList) {
		List<Lord> result = new ArrayList<Lord>();
		for (int i = 0; i < lordList.size(); i++) {
			Lord lord = lordList.get(i);
			result.add(lord);
		}
		return result;
	}

	public static void main(String[] args) {
		LordManager lm = new LordManager();
		lm.addLord(new Lord(0, 6, 2.0));
		lm.addLord(new Lord(1, 6, 1.0));
		lm.addLord(new Lord(2, 5, 2.0));
		lm.addLord(new Lord(3, 5, 3.0));
		lm.addLord(new Lord(4, 4, 4.0));
		lm.addLord(new Lord(5, 4, 2.0));
		System.out.println("--- Lord Information ---");
		System.out.println(lm.toString());
	}
}
