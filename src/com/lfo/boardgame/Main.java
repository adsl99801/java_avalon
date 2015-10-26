package com.lfo.boardgame;

import java.util.*;
import java.util.concurrent.*;
import java.math.*;

public class Main {


	public final static  int informationcommand = 111;
	
	public static Utils utils;
	public static void main(String[] args) {
		
		Storage storage=new Storage();
		utils = new Utils(storage);
		System.out.println("請輸入遊玩人數:5,6,7,8,9,10人");
		utils.initTotalPlayers();
		System.out.println("現在開始分配正義或邪惡角色!");
		utils.initPlayersNameAndAllocate();
		int[] mission=utils.getMissionBoard();
		
		int kingOfIndex = 0;
		boolean isOver = false;
		Player arthurKing = null;
		int round = 1;
		ArrayList<Player> playerlist = storage.playerlist;
		Scanner[] scanners=Storage.scanners;
		System.out.println("開始遊戲!共進行" + mission.length + "個任務。");
		while (!isOver) {
			ArrayList<Player> assignedList = new ArrayList<>();
			arthurKing = playerlist.get(kingOfIndex);

			System.out.println("任務" + round + ",可指派" + mission[round - 1] + "位出任務");
			System.out.println("由玩家:" + arthurKing.getName() + " 指派任務");

			assignedList = kingAssignPlayers(scanners[4], storage.missionScoreBoard,playerlist, arthurKing, mission[round - 1]);

			System.out.println("請每位玩家開始投票:是否贊成此出任務名單?(若否決票達一半則亞瑟王換下一位)");
			boolean cangetmission = voteMissionAssignList(scanners[4],playerlist,assignedList);

			if (!cangetmission) {

				System.out.println("名單被否決!");
				kingOfIndex = nextking(kingOfIndex);
				assignedList.clear();

				storage.denytimes++;
				System.out.println("否決次數:" + storage.denytimes + "/5");
				if (storage.denytimes >= 5) {
					System.out.println("否決達到五次 壞人勝利");
					isOver = true;
				}
				continue;
			}
			boolean ismissionpass = false;

			int disagreemissioncri=utils.getDisagreeMissionCri(round);
			ismissionpass = domission(storage.missionScoreBoard,scanners, assignedList,disagreemissioncri);
			if (ismissionpass) {
				storage.missionScoreBoard[round - 1] = Camp.justice;
				System.out.println("任務成功!");
			} else {
				storage.missionScoreBoard[round - 1] = Camp.evil;
				System.out.println("任務失敗");
			}
			isOver = isoverandprint(storage.missionScoreBoard);
			if (isOver) {
				break;
			}

			
			if (round == storage.totalMissions) {
				isOver = true;
				printmutiline(15);
				System.out.println("滿五局 遊戲結束");
				printmissionboard(storage.missionScoreBoard);

			}
			round++;
			
			afterMission(round,utils,arthurKing);
			kingOfIndex = nextking(kingOfIndex);

		}
		System.out.println("請刺客猜誰是梅林!之後可以輸入111來查看玩家身份");
		afterGame(scanners[4], playerlist);

	}

	private static void afterMission(int round,Utils utils,Player arthurKing) {
		if(utils.getTotalPlayers()<7){
			return;
		}
		if(round>=2){
			Player p=utils.getLakeLadyPlayer(round,arthurKing.getNo());
			useLakelady(utils,p);
		}
		
	}
	private static void useLakelady(Utils utils,Player usep){
		boolean isused=false;
		ArrayList<Player> list=new  ArrayList<Player>();
		Scanner scanner=utils.getScanner();
		int input=0;
		while(!isused){
			list=utils.getAvalibleLakelist(usep);
			System.out.println("請"+usep.getName()+"使用湖中女神的能力!(查看一個玩家的陣營)");
			System.out.println("提示:被查看過的玩家不能再被使用湖中女神能力了!");
			printNoNamelist(list);
			try {
				input=scanner.nextInt();
			} catch (Exception e) {
				scanner.nextLine();
				System.out.println("不符合預期的輸入");
				continue;
			}
			Player picked=utils.getPlayerBy(input);
			System.out.println(picked.getNo()+"."+picked.getName()+"的陣營是"+picked.getC().toString());
			utils.setLakeLadyPlayer(picked);//將湖中女神轉交給被使用的玩家
			isused=true;
		}
		try {
			System.out.println("請任意輸入來洗掉畫面");
			input=scanner.nextInt();
		} catch (Exception e) {
			scanner.nextLine();
		}finally{
			printmutiline(15);
		}
		
	}
	

	public static ArrayList<Player> kingAssignPlayers(Scanner s,Camp[] board, ArrayList<Player> playerlist, Player king,
			int totalMissionPlayers) {
		ArrayList<Player> assignedList = new ArrayList<>();

		int choosesum = 0;
		Player np = null;

		while (choosesum < totalMissionPlayers) {

			System.out.println("請亞瑟王:" + king.getName() + " 選擇要指派的玩家(輸入該玩家號碼) " + informationcommand + "可以得到資訊");
			printNoNamelist(playerlist);
			System.out.println("正在指派第" + (choosesum + 1) + "/" + totalMissionPlayers + "位");
			int pick = -1;
			try {
				pick = s.nextInt();
			} catch (Exception e) {
				System.out.println("不符合預期的輸入");
				s.nextLine();
				continue;
			}
			if (pick == informationcommand) {
				lookup(board,king,playerlist);
				continue;
			}

			if (pick < 0) {
				System.out.println("不符合預期的輸入");
				continue;
			}
			if (pick > (utils.getTotalPlayers()-1)) {
				System.out.println("不符合預期的輸入");
				continue;
			}
			np = playerlist.get(pick);
			if (assignedList.contains(np)) {
				System.out.println("不可以指派重複的玩家出任務");
				continue;
			} else {
				assignedList.add(np);
				choosesum++;
				System.out.println("你選了:" + np.getName());

			}

		}
		System.out.println("出任務名單為:");
		printNoNamelist(assignedList);
		return assignedList;

	}

	public static void afterGame(Scanner s, ArrayList<Player> playerlist) {
		boolean isbreak = false;
		int input = 0;
		while (!isbreak) {
			try {
				input = s.nextInt();

			} catch (Exception e) {
				s.nextLine();
				System.out.println("不符合預期的輸入");
				continue;
			}
			if (input == 111) {
				System.out.println("");
				for (Player p : playerlist) {
					System.out.print(p.getNo() + "." + p.getName() + "角色:" + p.getC().toString());
					if (p.getPart().equals(Part.merlin)) {
						System.out.print(";是梅林");
					}
					System.out.println("");

				}
				isbreak = true;

			}

		}

	}

	public static boolean voteMissionAssignList(Scanner scanner,ArrayList<Player> assignlist,ArrayList<Player> playerlist) {
		ArrayList<Player> votelist = new ArrayList<>();
		for (Player p : playerlist) {
			printmutiline(15);
			System.out.println("出任務名單為:");
			printNoNamelist(assignlist);
			System.out.println("現在，" + p.getName() + "請投票");
			System.out.println("輸入 1:贊成;2:反對  ;" + informationcommand + "可以得到資訊");
			int nextint = -1;
			boolean isvoted = false;
			while (!isvoted) {
				try {
					nextint = scanner.nextInt();
				} catch (Exception e) {
					System.out.println("不符合預期的輸入");
					scanner.nextLine();
					continue;

				}
				if (lookupwhen(p, nextint,playerlist)) {
					System.out.println("輸入 1:贊成;2:反對  ;" + informationcommand + "可以得到資訊");
					continue;
				}
				isvoted = true;
			}
			votelist = insertvoteto(votelist, p, nextint);

		}

		boolean cangetmission = voteresultandprint(votelist);
		return cangetmission;

	}

	
	public static int nextking(int kingindex) {
		if (kingindex == 4) {
			kingindex = 0;
		} else {
			kingindex++;
		}
		return kingindex;
	}

	public static boolean isoverandprint(Camp[] board) {
		int round = 1;
		int jscore = 0;
		int escore = 0;
		printmutiline(15);
		System.out.println("戰況公佈!");

		for (Camp c : board) {
			if (c == null) {
				break;
			}

			System.out.println("第" + round + "局:" + c.toString() + "獲勝");

			round++;
			if (c.equals(Camp.justice)) {
				jscore++;
			} else {
				escore++;
			}
			if (jscore == 3) {
				System.out.println("好人勝利");
				return true;
			}
			if (escore == 3) {
				System.out.println("壞人勝利");
				return true;
			}
		}
		return false;
	}

	public static void printmissionboard(Camp[] board) {

		System.out.println("戰況公佈!");
		int round = 1;
		for (Camp c : board) {
			if (c != null) {
				System.out.println("第" + round + "局:" + c.toString() + "獲勝");

			}
			round++;
		}
	}

	public static boolean lookupwhen(Player p, int pvote,ArrayList<Player> playerlist) {
		if (pvote != informationcommand) {
			return false;
		}
		if (p.getC().equals(Camp.justice)) {
			System.out.println("你是好人");
			if (p.getPart().equals(Part.merlin)) {
				maylinlookup(p,playerlist);
			}
		}
		if (p.getC().equals(Camp.evil)) {
			evillookup(p,playerlist);
		}
		return true;
	}

	public static ArrayList<Player> insertvoteto(ArrayList<Player> list, Player p, int pvote) {
		if (pvote == 1) {
			p.setIsAgree(true);
		} else {

			p.setIsAgree(false);
		}
		list.add(p);
		return list;
	}

	public static boolean domission(Camp[] missionboard,Scanner[] scanners, ArrayList<Player> list,int disagreemissioncri) {
		System.out.println("任務開始了!");
		System.out.println("人員為:");
		printNoNamelist(list);
		int domissionindex = 0;
		boolean ismissionvoted = false;
		int input = 111;
		// System.out.println("size"+list.size());
		for (Player p : list) {
			ismissionvoted = false;
			printmutiline(15);
			System.out.println("現在,輪到" + p.getName());
			System.out.println("請選擇 1:任務成功  2:任務失敗 或111查看資訊");
			while (!ismissionvoted) {
				try {

					input = scanners[domissionindex].nextInt();
				} catch (Exception e) {
					e.printStackTrace();
					scanners[domissionindex].nextLine();
					System.out.println("請再輸入一次");
					continue;
				}

				if (!(input == 1 || input == 2 || input == informationcommand)) {
					System.out.println("只能輸入1或2");
					continue;
				}
				if (input == informationcommand) {
					lookup(missionboard,p,list);

					continue;
				}
				if (p.getC().equals(Camp.evil)) {
					if (input == 1) {
						p.setIsAgree(true);
					} else {
						p.setIsAgree(false);
					}

				} else {
					p.setIsAgree(true);
				}
				ismissionvoted = true;
				System.out.println("已選擇");

			}
			domissionindex++;
		}
		if (isdisagreeovercri(list, disagreemissioncri)) {
			return false;
		}

		return true;
	}

	public static boolean isdisagreeovercri(ArrayList<Player> list, int cri) {
		int sumofdisagree = 0;
		for (Player p : list) {
			if (!p.isAgree()) {
				sumofdisagree++;
			}
		}
		if (sumofdisagree >= 1) {
			return true;
		}
		return false;
	}

	public static boolean voteresultandprint(ArrayList<Player> list) {
		int denysum = 0;
		for (Player p : list) {
			System.out.print(p.getName() + ":");
			if (!p.isAgree()) {
				System.out.println("反對");
				denysum++;
			} else {
				System.out.println("贊成");
			}
		}
		if (denysum >= 3) {
			return false;
		}
		return true;
	}

	public static void printNoNamelist(ArrayList<Player> list) {

		for (Player p : list) {
			System.out.println(p.getNo() + "." + p.getName());

		}

	}

	public static void lookup(Camp[] missionboard,Player p,ArrayList<Player> playerlist) {
		printmissionboard(missionboard);
		if (p.getC().equals(Camp.justice)) {
			System.out.println("你是好人");
			if (p.getPart().equals(Part.merlin)) {
				maylinlookup(p,playerlist);
			}
			if (p.getPart().equals(Part.percival)) {
				percivallookup(playerlist);
			}
		} else {
			if (p.getPart().equals(Part.morgana)) {
				morganalookup(p,playerlist);
			}
			evillookup(p,playerlist);
			
			
		}
	}

	private static void morganalookup(Player p, ArrayList<Player> playerlist) {
		maylinlookup(p,playerlist);
	}

	private static void percivallookup(ArrayList<Player> playerlist) {	
		for (Player np : playerlist) {
			if (np.getPart().equals(Part.morgana)) {
				System.out.println(np.getName() + ":" +Part.morgana.toString());
			}
			if (np.getPart().equals(Part.merlin)) {
				System.out.println(np.getName() + ":" +Part.merlin.toString());
			}
		}
	}

	public static void evillookup(Player evil,ArrayList<Player> playerlist) {
		System.out.println("你是壞人 你的邪惡夥伴是:");
		for (Player p : playerlist) {
			if (p.getC().equals(Camp.evil)) {

				System.out.println(p.getName() + ":" + p.getC().toString());
			}
		}
	}

	public static void maylinlookup(Player maylin,ArrayList<Player> playerlist) {
		System.out.println("你是梅林 ;你知道所有人的身份但是你不可以被壞人猜到身份");
		for (Player p : playerlist) {
			System.out.println(p.getName() + ":" + p.getC().toString());
		}
	}

	public static void printmutiline(int lines) {
		for (int i = 0; i < lines; i++) {
			System.out.println("");
		}
	}
}
