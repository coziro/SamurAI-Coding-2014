import java.util.ArrayList;
import java.util.HashMap;

public class IntimacyManager {
	private HashMap<Integer, IntimacySet> publicIntimacySet = new HashMap<Integer, IntimacySet>();
	private ArrayList<Integer> nightNegoCount = new ArrayList<Integer>();
	private ArrayList<Integer> realIntimacy;

	public ArrayList<Integer> getRealIntimacy() {
		return realIntimacy;
	}

	public void putPublicIntimacy(int lordId, IntimacySet set) {
		publicIntimacySet.put(lordId, set);
	}

	public IntimacySet getPublicIntimacy(int lordId) {
		return publicIntimacySet.get(lordId);
	}

	public IntimacySet getPublicIntimacy(Lord lord) {
		return getPublicIntimacy(lord.getId());
	}

	public void addNegoCount(ArrayList<Integer> negoCount) {
		if (nightNegoCount.size() == 0) {
			this.nightNegoCount = negoCount;
		} else {
			for (int l = 0; l < LordManager.getLordNum(); l++) {
				// for (int l = 0; l < nightNegoCount.size(); l++) {
				int newCount = nightNegoCount.get(l) + negoCount.get(l);
				nightNegoCount.set(l, newCount);
			}
		}
	}

	public void clearNegoCount() {
		for (int l = 0; l < nightNegoCount.size(); l++) {
			nightNegoCount.set(l, 0);
		}
	}
	
	public void subtractNegoCount(Command command){
		for (int c = 0; c < 2; c++){
			int lordId = command.getCommand(c);
			int newCount = nightNegoCount.get(lordId) - 1;
			nightNegoCount.set(lordId, newCount);
		}
	}
	
	public IntimacySet estimateRealIntimacy(Lord lord) {

		IntimacySet result = new IntimacySet();
		// 自分への親密度
		int intimacy = realIntimacy.get(lord.getId());
		result.putIntimacy(0, intimacy);
		// 自分以外の大名への親密度
		IntimacySet publicIntimacy = this.getPublicIntimacy(lord);
		// IntimacySet publicIntimacy = publicIntimacySet.get(lord);
		for (int d = 1; d < 4; d++) {
			double estimatedIntimacy = publicIntimacy.getIntimacyByDaimyoId(d);
			int negoCount = nightNegoCount.get(lord.getId());
			// 交渉回数のうち20%がこの領主からだったという想定
			// TODO: 20%を引数で指定する
			estimatedIntimacy += negoCount * 2 * 0.6;
			result.putIntimacy(d, estimatedIntimacy);
		}
		return result;
	}

	public void updateRealIntimacy(ArrayList<Integer> realIntimacy) {
		this.realIntimacy = realIntimacy;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("- Public Intimacy -");
		sb.append(System.getProperty("line.separator"));
		for (int l = 0; l < publicIntimacySet.size(); l++) {
			IntimacySet set = publicIntimacySet.get(l);
			sb.append(set.toString());
			sb.append(System.getProperty("line.separator"));
		}
		sb.append("- Real Intimacy -");
		sb.append(System.getProperty("line.separator"));
		for (int l = 0; l < realIntimacy.size(); l++) {
			sb.append(realIntimacy.get(l));
			sb.append(" ");
		}
		sb.append(System.getProperty("line.separator"));
		sb.append("- Night Negotiation Count -");
		sb.append(System.getProperty("line.separator"));
		for (int l = 0; l < nightNegoCount.size(); l++) {
			sb.append(nightNegoCount.get(l));
			sb.append(" ");
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println("test");

		IntimacyManager im = new IntimacyManager();
		for (int l = 0; l < 6; l++) {
			IntimacySet set = new IntimacySet();
			set.putIntimacy(0, 0.0 * l);
			set.putIntimacy(1, 1.0 * l);
			set.putIntimacy(2, 2.0 * l);
			set.putIntimacy(3, 1.0 * l);
			im.putPublicIntimacy(l, set);
		}
		
		IntimacySet set2 = im.getPublicIntimacy(2);
		System.out.println(set2.toString());

	}
}