package com.lfo.boardgame.utils;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import com.lfo.boardgame.Camp;
import com.lfo.boardgame.Part;
import com.lfo.boardgame.Player;
import com.lfo.boardgame.Storage;

public class Utils {
	private Storage  storage;
	public Utils(Storage storage) {
		this.storage = storage;
	}

	public ArrayList<Player> allocateCamAndpart(ArrayList<Player> playerlist) {
		ArrayList<Integer> jlist = ranCampPartNumber(storage.maxjustice);
		ArrayList<Integer> elist = getEvillistBy(storage.playerlist.size(), storage.maxevil, jlist);
	
		/*
		for (Integer i:jlist) {
		
			System.out.println("j:" + i);
		}
		for (Integer i:elist) {
			System.out.println("e:" + i);
		}
		*/
		int merlinIndex=allcfirst(jlist);//merlin
		//System.out.println("merlin:" + merlinIndex);
		int percivalindex=-1;
		int modredindex=-1;
		int maganaindexindex=-1;
		int obindex=-1;
		if(storage.isUsingPercival){
			percivalindex=allcSecondCampPart(jlist, merlinIndex);
			//System.out.println("p:" + percivalindex);
		}
	if(storage.isUsingMogana){
		maganaindexindex=allcfirst(elist);//mogana
		//System.out.println("mmagm:" + maganaindexindex);
	
		if(storage.isUsingMordred){
			modredindex=allcSecondCampPart(elist, maganaindexindex);
			//System.out.println("modered:" + modredindex);
		}
		
	if(storage.isUsingOberyn){
		obindex=allcObPart(elist, maganaindexindex, modredindex);
		//System.out.println("ob" + obindex);
	}
	}
		
		for (Player p:playerlist) {
			if (jlist.contains(p.getNo())) {
				p.setC(Camp.justice);

			
				if (p.getNo() == merlinIndex) {
					p.setPart(Part.merlin);
					//System.out.println(p.getName() + " 你好!" + p.getC().toString() + ":" + p.getPart());
					continue;
				}
				if (storage.isUsingPercival) {
					if (p.getNo() == percivalindex) {
						p.setPart(Part.percival);
						//System.out.println(p.getName() + " 你好!" + p.getC().toString() + ":" + p.getPart());
						continue;
					}
				}
				//jsum++;
			} else {
				p.setC(Camp.evil);
				//sumofevil++;
				if (storage.isUsingMogana) {
					if (p.getNo() == maganaindexindex) {
						p.setPart(Part.morgana);
						//System.out.println(p.getName() + " 你好!" + p.getC().toString() + ":" + p.getPart());
						continue;
					}
				}
				if (storage.isUsingMordred) {
					if (p.getNo() == modredindex) {
						p.setPart(Part.mordred);
						//System.out.println(p.getName() + " 你好!" + p.getC().toString() + ":" + p.getPart());
						continue;
					}
				}
				if (storage.isUsingOberyn) {
					if (p.getNo() == obindex) {
						p.setPart(Part.oberyn);
						//System.out.println(p.getName() + " 你好!" + p.getC().toString() + ":" + p.getPart());
						continue;
					}
				}
			}
			
		}
		return playerlist;

	}
	public ArrayList<Integer> getEvillistBy(int playerlistsize, int maxevils, ArrayList<Integer> jlist) {
		ArrayList<Integer> elist = new ArrayList<>();

		for (int i=0;i < playerlistsize;i++) {
			if (jlist.contains(i)) {
				
				continue;
			}
			if (elist.size() == maxevils) {
				break;
			}
			elist.add(i);
			
		}
		return elist;
	}
	public int allcObPart(ArrayList<Integer> inCamplist, int firstPartIndex, int sePartIndex) {

		for (Integer i:inCamplist) {

			if (i != firstPartIndex && i != sePartIndex) {

				return i;
			}
		}
		return -1;


	}
	public int allcSecondCampPart(ArrayList<Integer> inCamplist, int firstPartIndex) {

		boolean isred=false;

		while (!isred) {

			int r = ThreadLocalRandom.current().nextInt(0, inCamplist.size() - 1);
			if (inCamplist.get(r) != firstPartIndex) {

				return inCamplist.get(r);
			}
		}
		return -1;


	}
	public int allcfirst(ArrayList<Integer> inCamplist) {


		int r = ThreadLocalRandom.current().nextInt(0, inCamplist.size() - 1);
		return inCamplist.get(r);

	}

	public ArrayList<Integer> ranCampPartNumber(int max) {
		ArrayList<Integer> randomedList = new ArrayList<>();
		boolean isred=false;

		while (!isred) {
			int r = ThreadLocalRandom.current().nextInt(0, storage.totalplayers - 1);
			if (randomedList.contains(r)) {
				continue;
			}
			if (randomedList.size() == max) {
				isred = true;
			} else {
				randomedList.add(r);
			}
		}

		return randomedList;
	}
	public void printNowMissionCard() {
		System.out.println("成功卡數:" + getMissionSusCardSum() + ",失敗卡數:" + getMissionFailCardSum());
	}
	public void setMissionFailCardSum(int missionFailCardSum) {
		storage.missionFailCardSum = missionFailCardSum;
	}

	public int getMissionFailCardSum() {
		return storage.missionFailCardSum;
	}

	public void setMissionSusCardSum(int missionSusCardSum) {
		storage.missionSusCardSum = missionSusCardSum;
	}

	public int getMissionSusCardSum() {
		return storage.missionSusCardSum;
	}
	public void printVotelist() {

		for (Player p : storage.votelist) {
			System.out.print(p.getName() + ":");
			if (!p.isAgree()) {
				System.out.println("反對");

			} else {
				System.out.println("贊成");

			}
		}
	}
    public void setVotelist(ArrayList<Player> list) {
		storage.votelist = list;
	}
	public  Camp[] getMissionScoreBoard() {
		return storage.missionScoreBoard;
	}
	public ArrayList<Player> getPlayerlist() {
		return storage.playerlist;
	}
	public Player getPlayerBy(int no) {
		return storage.playerlist.get(no);
	}
	public Scanner getScanner() {
		return storage.s5;
	}



	public ArrayList<Player> getAvalibleLakelist(Player usep) {
		ArrayList<Player> list=new  ArrayList<Player>();
		storage.notAvalibleLakelist.add(usep);
		for (Player p:storage.playerlist) {
			if (isInlist(p, storage.notAvalibleLakelist)) {
				continue;
			}
			list.add(p);

		}
		return list;
	}
	public boolean isInlist(Player np, ArrayList<Player> list) {
		for (Player p:list) {
			if (p.getNo() == np.getNo()) {
				return true;
			}
		}
		return false;
	}

	public int getTotalPlayers() {
		return storage.totalplayers;
	}
	public void initUsingPart() {

		Scanner scanner=getScanner();

		System.out.println("是否使用魔甘娜?");
		System.out.println("輸入 1:使用2:不用");
		if (getInput(scanner)) {
			storage.isUsingMogana = true;
		}
		if(storage.totalplayers < 7){
			return;
		}
		if (storage.isUsingMogana) {

			System.out.println("是否使用派希維爾?");
			System.out.println("輸入 1:使用2:不用");
			if (getInput(scanner)) {
				storage.isUsingPercival = true;
			}
		}else{
			return;
		}
		
			System.out.println("是否使用莫德雷德?");
			System.out.println("輸入 1:使用2:不用");
			if (getInput(scanner)) {
				storage.isUsingMordred = true;
			}
		

		
			System.out.println("是否使用奧伯龍?");
			System.out.println("輸入 1:使用2:不用");
			if (getInput(scanner)) {
				storage.isUsingOberyn = true;
			}
		



	}
	private boolean getInput(Scanner scanner) {
		int input =0;
		try {
			input = scanner.nextInt();
		} catch (Exception e) {
			getScanner().nextLine();
			return false;

		}
		if (input != 1) {
			return false;
		}
		return  true;
	}
	public void initTotalPlayers() {
		System.out.println("請輸入遊玩人數:5,6,7,8,9,10人");
		boolean isseted=false;
		int input =0;
		while (!isseted) {
			try {
				input = getScanner().nextInt();
			} catch (Exception e) {
				getScanner().nextLine();
				continue;
			}
			if (input < 5) {
				System.out.println("不符合預期的輸入");
				continue;
			}
			if (input > 10) {
				System.out.println("不符合預期的輸入");
				continue;
			}
			storage.totalplayers = input;
			setMaxJusticeAndEvil(input);
			isseted = true;

		}
	}
	public void setMaxJusticeAndEvil(int totaplayers) {
		switch (totaplayers) {
			case (6):
				storage.maxjustice = 4;
				storage.maxevil = 2;
				break;
			case (7):
				storage.maxjustice = 4;
				storage.maxevil = 3;
				break;
			case (8):
				storage.maxjustice = 5;
				storage.maxevil = 3;
				break;
			case (9):
				storage.maxjustice = 6;
				storage.maxevil = 3;
				break;
			case (10):
				storage.maxjustice = 6;
				storage.maxevil = 4;
				break;


		}

	}
	public void setLakeLadyPlayer(Player p) {
		storage.nowlakeLadyPlayer = p;
	}
	public Player getLakeLadyPlayer(int round , int kingNo) {

		if (round == 2) {
			if (storage.nowlakeLadyPlayer == null) {
				storage.lakeLadyPlayerNo = storage.totalplayers - 1;//給予最後一位玩家0123456 6號		
			}
			storage.nowlakeLadyPlayer = storage.playerlist.get(storage.lakeLadyPlayerNo);
		}

		return storage.nowlakeLadyPlayer;
	}

	public int getDisagreeMissionCri(int round) {
		if (round == 4) {
			if (storage.totalplayers > 6) {
				return 2;
			}
		}
		return 1;
	}

	public int [] getMissionBoard() {

		if (storage.totalplayers == 5) {
			int mission []= {2,3,2,3,3};
			return mission;
		}
		if (storage.totalplayers == 6) {
			int mission []= {2,3,4,3,4};
			return mission;
		}
		if (storage.totalplayers == 7) {
			int mission []= {2,3,3,4,4};
			return mission;
		}
		if (storage.totalplayers == 8) {
			int mission []= {3,4,4,5,5};
			return mission;
		}
		if (storage.totalplayers == 9) {
			int mission []= {3,4,4,5,5};
			return mission;
		}

		int mission []= {2,3,2,3,3};
		return mission;

	}

	public  void initPlayersNameAndAllocate() {
		System.out.println("現在開始分配正義或邪惡角色!");

		storage.playerlist.clear();
		for (int i = 0; i < storage.totalplayers; i++) {
			System.out.println("請輸入玩家的名字 第 " + (i + 1) + "位名字是:");
			String name = getScanner().next();
			Player p = new Player(i, name);
			storage.playerlist.add(p);
		}
		/*
		 boolean israndomed = false;
		 // System.out.println("j"+sumofjustice+",e"+sumofevil);
		 while (!israndomed) {
		 int r = ThreadLocalRandom.current().nextInt(1, storage.totalplayers + 1);
		 // System.out.println("隨機數字" + r);
		 if (r == 1) {
		 if (sumofjustice >= storage.maxjustice) {
		 if (sumofevil >= storage.maxevil) {
		 break;
		 }
		 continue;
		 }
		 p.setC(Camp.justice);
		 sumofjustice++;
		 if (sumofjustice == 3) {
		 p.setPart(Part.merlin);
		 }
		 if(storage.isUsingPercival){
		 if (sumofjustice == 2) {
		 p.setPart(Part.percival);
		 }
		 }

		 } else {
		 if (sumofevil >= storage.maxevil) {
		 if (sumofjustice >= storage.maxjustice) {
		 break;
		 }
		 continue;
		 }
		 p.setC(Camp.evil);
		 sumofevil++;
		 if(storage.isUsingMogana){
		 if (sumofevil == 1) {
		 p.setPart(Part.morgana);
		 }
		 }
		 if(storage.isUsingMordred){
		 if (sumofevil == 2) {
		 p.setPart(Part.mordred);
		 }
		 }
		 if(storage.isUsingOberyn&&storage.totalplayers>=7){
		 if (sumofevil == 3) {
		 p.setPart(Part.oberyn);
		 }
		 }


		 }

		 israndomed = true;

		 }*/



		storage.playerlist = allocateCamAndpart(storage.playerlist);

		//return list;
	}

}
