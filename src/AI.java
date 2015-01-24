import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AI {
	private static Scanner scanner;
	private static PrintWriter writer;
	private static boolean debug = false;
	private static Logger logger;

	private static int maxTurn, turn, daimyoNum, lordNum;
	private static char time;
	private static LordManager lm = new LordManager();
	private static IntimacyManager im = new IntimacyManager();

	public static void main(String[] args) throws Exception {
		// Log
		if (debug) {
			File logFile = new File("/xxx/log.txt");
			logger = new Logger(logFile);
			logger.println("Log Start");
			logger.flush();
		}
		// READY
		scanner = new Scanner(System.in);
		writer = new PrintWriter(System.out, true);
		writer.println("READY");
		writer.flush();
		// 初期データの読み込み
		readInitialData();
		// ターン実行
		for (int t = 1; t < maxTurn + 1; t++) {
			if (debug) {
				logger.println("");
				logger.println("------ Turn " + t + " ------");
				logger.flush();
			}
			// 入力情報読み込み
			readData();
			// 出力
			Command command = new Command(time);
			if (turn == 1) {
				makeCommand1(command);
			} else {
				makeCommand(command);
			}
			writer.println(command.toString());
			writer.flush();
			if (debug) {
				logger.println("--- Command ---");
				logger.println(command.toString());
				logger.flush();
			}
			if (time == 'N') {
				// 夜の場合、交渉回数から自分が実行した分を差し引く
				im.subtractNegoCount(command);
			}
		}
		if (debug) {
			logger.close();
		}
	}

	private static void readInitialData() throws Exception {
		// 全ターン数
		maxTurn = scanner.nextInt();
		// 大名数
		daimyoNum = scanner.nextInt();
		// 領主数
		lordNum = scanner.nextInt();
		// 各領主の兵力
		for (int l = 0; l < lordNum; l++) {
			int strength = scanner.nextInt();
			Lord lord = new Lord(l, strength);
			lm.addLord(lord);
		}
		lm.sort();
		if (debug) {
			logger.println("------ Read Initial Data ------");
			logger.printKeyValue("Max Turn", maxTurn);
			logger.printKeyValue("Daimyo Num", daimyoNum);
			logger.printKeyValue("Lord Num", lordNum);
			logger.println("- Lord Information -");
			logger.print(lm.toString());
			logger.flush();
		}
	}

	private static void readData() throws Exception {
		if (debug) {
			logger.println("--- Read Data ---");
			logger.flush();
		}
		// 現在のターン数
		turn = scanner.nextInt();
		// D(昼) or N(夜)
		time = scanner.next().charAt(0);
		// 公開親密度
		for (int l = 0; l < lordNum; l++) {
			IntimacySet set = new IntimacySet();
			for (int d = 0; d < daimyoNum; d++) {
				int intimacy = scanner.nextInt();
				set.putIntimacy(d, intimacy);
			}
			im.putPublicIntimacy(l, set);
		}
		// 真の親密度
		ArrayList<Integer> realIntimacy = new ArrayList<Integer>();
		for (int l = 0; l < lordNum; l++) {
			int intimacy = scanner.nextInt();
			realIntimacy.add(intimacy);
		}
		im.updateRealIntimacy(realIntimacy);
		// 夜ターン交渉回数
		if (turn == 6) {
			im.clearNegoCount();
			if (debug) {
				logger.println("- Clear Night Negotiation Count -");
				logger.flush();
			}
		}
		if (time == 'D') {
			ArrayList<Integer> negoCount = new ArrayList<Integer>();
			for (int l = 0; l < lordNum; l++) {
				int count = scanner.nextInt();
				negoCount.add(count);
			}
			im.addNegoCount(negoCount);
			if (debug) {
				logger.println("- Update Night Negotiation Count - ");
				logger.flush();
			}
		}
		if (debug) {
			logger.printKeyValue("Turn", turn);
			logger.printKeyValue("Time", time);
			logger.println("--- Intimacy Information ---");
			logger.println(im.toString());
			logger.flush();
		}
	}

	private static void makeCommand1(Command command) {
		// 戦力の強い領主から順に1回ずつ交渉を実施
		List<Lord> sortedLordList = lm.getSortedLordList();
		int i = 0;
		while (command.undone()) {
			Lord lord = sortedLordList.get(i);
			command.add(lord.getId());
			i++;
		}
	}

	private static void makeCommand(Command command) throws Exception {
		// 戦略の見直し
		updateStrategy();

		// 戦力の高い領主から順に実行
		List<Lord> sortedLordList = lm.getSortedLordList();
		for (int sl = 0; sl < sortedLordList.size(); sl++) {
			Lord lord = sortedLordList.get(sl);
			int strategy = lord.getStrategy();
			IntimacySet set = im.estimateRealIntimacy(lord);
			if (debug) {
				logger.println("--- Operation for " + lord.getId() + " ---");
				logger.printKeyValue("Intimacy Set", set.toString());
				logger.printKeyValue("Strategy", lord.getStrategy());
				logger.flush();
			}
			int myRank = set.getMyRank();
			double myIntimacy = set.getMyIntimacy();
			double competitorIntimacy = 0.0;
			double requiredIntimacy = 0.0;

			// 戦略別に追加すべき親密度を計算
			switch (strategy) {
			case Strategy.AGGRESSIVE:
				// 競合大名の親密度を取得
				if (myRank == 1 && set.getNumOfRank(1) == 1) {
					// 単独1位の場合
					competitorIntimacy = set.getIntimacyByRank(2);
				} else {
					// 同点1位の場合、もしくは2位以下の場合
					competitorIntimacy = set.getIntimacyByRank(1);
				}
				// 追加が必要な親密度を計算
				requiredIntimacy = competitorIntimacy - myIntimacy + 2;
				break;
			case Strategy.PASSIVE:
				// 競合大名の親密度を取得
				if (myRank == 4) {
					// 4位(単独最下位)の場合
					if (set.existRank(3)) {
						competitorIntimacy = set.getIntimacyByRank(3);
					} else if (set.existRank(2)) {
						competitorIntimacy = set.getIntimacyByRank(2);
					} else if (set.existRank(1)) {
						competitorIntimacy = set.getIntimacyByRank(1);
					}
				} else {
					// 単独最下位でない場合
					competitorIntimacy = set.getLeastIntimacy();
				}
				// 追加が必要な親密度を計算
				requiredIntimacy = competitorIntimacy - myIntimacy + 1;
				break;
			case Strategy.NEGLECT:
				break;
			}
			if (debug) {
				logger.printKeyValue("Competitor Intimacy", competitorIntimacy);
				logger.printKeyValue("My Intimacy", myIntimacy);
				logger.printKeyValue("Requred Intimacy", requiredIntimacy);
				logger.flush();
			}

			// 必要な親密度に到達するまで、もしくは残りコマンド数がなくなるまで追加
			while (requiredIntimacy > 0) {
				command.add(lord.getId());
				if (debug) {
					logger.println("Add Command");
				}
				if (!command.undone()) {
					break;
				}
				if (time == 'D') {
					requiredIntimacy -= 1;
				} else if (time == 'N') {
					requiredIntimacy -= 2;
				}
			}
			if (!command.undone()) {
				break;
			}
		}

		int i = 0;
		while (command.undone()) {
			// コマンド数が余っている場合は、戦力の強い領主から順に追加
			Lord lord = sortedLordList.get(i);
			command.add(lord.getId());
			i++;
			if (debug) {
				logger.printKeyValue("Add Spare Command", lord.getId());
				logger.flush();
			}
		}
	}

	private static void updateStrategy() throws Exception {
		if (debug == true) {
			logger.println("--- Update Strategy ---");
			logger.flush();
		}
		for (int l = 0; l < lordNum; l++) {
			// 領主に対する自分の位置づけ(平均からの距離)を計算
			Lord lord = lm.getLord(l);
			IntimacySet set = im.estimateRealIntimacy(lord);
			// IntimacySet set = im.getPublicIntimacy(l);
			double meanIntimacy = set.getMeanIntimacy();
			int myIntimacy = im.getRealIntimacy().get(l);
			double myPosition = myIntimacy - meanIntimacy;
			lord.setMyPosition(myPosition);
		}

		// MyPositionでソート
		List<Lord> sortedLordList = lm.sortLordByMyPosition();
		for (int i = 0; i < lordNum; i++) {
			Lord lord = sortedLordList.get(i);
			if (i == 0 || i == 1 || i == 2) {
				lord.setStrategy(Strategy.AGGRESSIVE);
			} else if (i == 5) {
				lord.setStrategy(Strategy.NEGLECT);
			} else {
				lord.setStrategy(Strategy.PASSIVE);
			}
		}

		if (debug == true) {
			for (int l = 0; l < lordNum; l++) {
				Lord lord = lm.getLord(l);
				StringBuffer sb = new StringBuffer();
				sb.append("Lord: " + l + ", ");
				sb.append("Strength: " + lord.getStrength() + ", ");
				sb.append("Strategy: " + lord.getStrategy() + ", ");
				sb.append("My Position: " + lord.getMyPosition());
				logger.println(sb.toString());
				logger.flush();
			}
		}
	}
}
