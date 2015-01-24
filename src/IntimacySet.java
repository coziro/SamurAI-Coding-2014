import java.util.HashMap;

public class IntimacySet {
	private int daimyoNum = 4;
	private HashMap<Integer, Double> map = new HashMap<Integer, Double>();

	public void putIntimacy(int daimyoId, double intimacy) {
		map.put(daimyoId, intimacy);
	}

	public double getIntimacyByDaimyoId(int daimyoId) {
		return map.get(daimyoId);
	}

	public double getMeanIntimacy() {
		float sum = 0;
		for (int d = 0; d < daimyoNum; d++) {
			double intimacy = map.get(d);
			sum += intimacy;
		}
		float mean = sum / daimyoNum;
		return mean;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int d = 0; d < daimyoNum; d++) {
			double intimacy = map.get(d);
			sb.append(intimacy);
			sb.append(" ");
		}
		return sb.toString();
	}

	public int getRank(int daimyoId) {
		int counter = 0;
		for (int d = 0; d < daimyoNum; d++) {
			if (map.get(daimyoId) < map.get(d)) {
				counter++;
			}
		}
		int rank = 0;
		switch (counter) {
		case 0:
			rank = 1;
			break;
		case 1:
			rank = 2;
			break;
		case 2:
			rank = 3;
			break;
		case 3:
			rank = 4;
			break;
		}
		return rank;
	}

	public int getMyRank() {
		return getRank(0);
	}

	public double getMyIntimacy() {
		return getIntimacyByDaimyoId(0);
	}

	public double getIntimacyByRank(int rank) {
		double result = 0;
		for (int d = 0; d < daimyoNum; d++) {
			if (rank == getRank(d)) {
				result = map.get(d);
			}
		}
		return result;
	}

	public int getNumOfRank(int rank) {
		int result = 0;
		for (int d = 0; d < daimyoNum; d++) {
			if (rank == getRank(d)) {
				result++;
			}
		}
		return result;
	}

	public boolean existRank(int rank) {
		if (this.getNumOfRank(rank) > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public double getLeastIntimacy(){
		if (this.existRank(4)){
			return this.getIntimacyByRank(4);
		} else if (this.existRank(3)){
			return this.getIntimacyByRank(3);
		} else if (this.existRank(2)){
			return this.getIntimacyByRank(2);
		} else {
			return this.getIntimacyByRank(1);
		}
	}

	public static void main(String[] args) {
		IntimacySet set = new IntimacySet();
		set.putIntimacy(0, 1.0);
		set.putIntimacy(1, 1.0);
		set.putIntimacy(2, 0.0);
		set.putIntimacy(3, 0.0);
		//double mean = set.getMeanIntimacy();
		//System.out.println(mean);
		
		System.out.println(set.getRank(0));
		System.out.println(set.getRank(1));
		System.out.println(set.getRank(2));
		System.out.println(set.getRank(3));
		System.out.println(set.getNumOfRank(1));
		System.out.println(set.getNumOfRank(2));
		System.out.println(set.getNumOfRank(3));
		System.out.println(set.getNumOfRank(4));
		
	}
}
