package com.lfo.boardgame;

import java.util.*;
import java.util.concurrent.*;

import com.lfo.boardgame.utils.Print;
import com.lfo.boardgame.utils.Utils;

import java.math.*;

public class Main {


	public final static  int informationcommand = 111;
	
	public static Utils utils;
	public static Print print;
	public static void main(String[] args) {
		
		Storage storage=new Storage();
		 print=new Print();
		utils = new Utils(storage);
		
		utils.initTotalPlayers();
		utils.initUsingPart();
		
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
			boolean cangetmission = voteMissionAssignList(utils,assignedList);

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
				print.printmutiline(15);
				System.out.println("滿五局 遊戲結束");
				print.printmissionboard(storage.missionScoreBoard);

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
		if(round>4){
			System.out.println("只能在2 3 4局使用湖中女神");
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
			print.printNoNamelist(list);
			try {
				input=scanner.nextInt();
			} catch (Exception e) {
				scanner.nextLine();
				System.out.println("不符合預期的輸入");
				continue;
			}
			Player picked=utils.getPlayerBy(input);
			if(!utils.isInlist(picked, list)){
				System.out.println("提示:被查看過的玩家不能再被使用湖中女神能力了!");
				continue;
			}
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
			print.printmutiline(15);
		}
		
	}
	

	public static ArrayList<Player> kingAssignPlayers(Scanner s,Camp[] board, ArrayList<Player> playerlist, Player king,
			int totalMissionPlayers) {
		ArrayList<Player> assignedList = new ArrayList<>();

		int choosesum = 0;
		Player np = null;
		boolean isassignover=false;

		while (!isassignover) {

			System.out.println("請亞瑟王:" + king.getName() + " 選擇要指派的玩家(輸入該玩家號碼) " + informationcommand + "可以得到資訊");
			print.printNoNamelist(playerlist);
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
			if(choosesum == totalMissionPlayers){
				isassignover=true;
			}

		}
		System.out.println("出任務名單為:");
		print.printNoNamelist(assignedList);
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
					if(p.getPart()!=null){
						System.out.print(p.getPart().toString());
						if (p.getPart().equals(Part.merlin)) {
							System.out.print(";是梅林");
						}
					}
					
					System.out.println("");

				}
				isbreak = true;

			}

		}

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
		print.printmutiline(15);
		if(board[0]!=null){
			System.out.println("目前戰況:");
		}
		
		
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
		print.printNoNamelist(list);
		int domissionindex = 0;
		boolean ismissionvoted = false;
		int input = 111;
		// System.out.println("size"+list.size());
		for (Player p : list) {
			ismissionvoted = false;
			print.printmutiline(15);
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
		int agreesum = 0;
		for (Player p : list) {
			System.out.print(p.getName() + ":");
			if (!p.isAgree()) {
				System.out.println("反對");
				denysum++;
			} else {
				System.out.println("贊成");
				agreesum++;
			}
		}
		if (denysum >= agreesum) {
			return false;
		}
		return true;
	}

	public static  boolean voteMissionAssignList(Utils utils,ArrayList<Player> assignlist) {
		ArrayList<Player> votelist = new ArrayList<>();
		ArrayList<Player> playerlist=utils.getPlayerlist();
		for (Player p : playerlist) {
			print.printmutiline(15);
			System.out.println("出任務名單為:");
			print.printNoNamelist(assignlist);
			System.out.println("現在，" + p.getName() + "請投票");
			System.out.println("輸入 1:贊成;2:反對  ;" + informationcommand + "可以得到資訊");
			int nextint = -1;
			boolean isvoted = false;
			Scanner scanner =utils.getScanner();
			while (!isvoted) {
				try {
					nextint = scanner.nextInt();
				} catch (Exception e) {
					System.out.println("不符合預期的輸入");
					scanner.nextLine();
					continue;

				}
				if (nextint==informationcommand) {
					lookup(utils.getMissionScoreBoard(), p, playerlist);
					continue;
				}
				isvoted = true;
			}
			votelist = insertvoteto(votelist, p, nextint);

		}

		boolean cangetmission = voteresultandprint(votelist);
		return cangetmission;

	}

	public static void lookup(Camp[] missionboard,Player p,ArrayList<Player> playerlist) {
		print.printmissionboard(missionboard);
		if (p.getC().equals(Camp.justice)) {
			System.out.println("你是好人");
			if(p.getPart()==null){
				return;
			}
			if (p.getPart().equals(Part.merlin)) {
				System.out.println("你是梅林 ;你不可以被壞人猜到身份");
				maylinlookup(p,playerlist);
			}
			if (p.getPart().equals(Part.percival)) {
				System.out.println("你是派希維爾 ;你知道梅林和魔甘娜是誰");
				percivallookup(playerlist);
			}
		} else {
			if (p.getPart()!=null){
				if (p.getPart().equals(Part.oberyn)) {
					System.out.println("你是奧伯倫 ;是壞人。但是不知道其他壞人是誰，也不會給其他壞人知道。");
					return;
				}
				if (p.getPart().equals(Part.mordred)) {
					System.out.println("你是莫德雷德。梅林不知道你");
				}
			}

			evillookup(p,playerlist);
			if(p.getPart()==null){
				return;
			}
			if (p.getPart().equals(Part.morgana)) {
				System.out.println("你是魔甘娜 ;你知道所有人的陣營");
				morganalookup(p,playerlist);
			}
			
			
			
		}
	}

	private static void morganalookup(Player p, ArrayList<Player> playerlist) {
		maganalookup(p,playerlist);
	}

	private static void percivallookup(ArrayList<Player> playerlist) {	
		System.out.println("這兩個人是魔甘娜或梅林? :");
		for (Player np : playerlist) {
			if(np.getPart()==null){
				continue;
			}
			
			if (np.getPart().equals(Part.morgana)) {
				System.out.println(np.getName());
			}
			if (np.getPart().equals(Part.merlin)) {
				System.out.println(np.getName() );
			}
		}
	}

	public static void evillookup(Player evil,ArrayList<Player> playerlist) {
		System.out.println("你是壞人 你的邪惡夥伴是:");
		for (Player p : playerlist) {
			if (p.getC().equals(Camp.evil)) {
				if(p.getPart()!=null){//壞人時看不到oberyn
					if(p.getPart().equals(Part.oberyn)){
						continue;
					}
				}
				System.out.println(p.getName() + ":" + p.getC().toString());
			}
		}
	}

	public static void maylinlookup(Player maylin,ArrayList<Player> playerlist) {
		System.out.println("邪惡現形:");
		for (Player p : playerlist) {
			if(p.getC().equals(Camp.evil)){
				if(p.getPart()!=null){//梅林看壞人時看不到mordred
					if(p.getPart().equals(Part.mordred)){
						continue;
					}
				}
				System.out.println(p.getName() + ":" + p.getC().toString());	
			}
			
		}
	}
	public static void maganalookup(Player maylin,ArrayList<Player> playerlist) {
		System.out.println("好人現形:");
		for (Player p : playerlist) {
			if(p.getC().equals(Camp.justice)){
				System.out.println(p.getName() + ":" + p.getC().toString());	
			}
			
		}
	}

	
}
