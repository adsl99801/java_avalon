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


	public  Camp[] getMissionScoreBoard(){
		return storage.missionScoreBoard;
	}
	public ArrayList<Player> getPlayerlist(){
		return storage.playerlist;
	}
	public Player getPlayerBy(int no){
		return storage.playerlist.get(no);
	}
	public Scanner getScanner(){
		return storage.s5;
	}


	
	public ArrayList<Player> getAvalibleLakelist(Player usep){
		ArrayList<Player> list=new  ArrayList<Player>();
		storage.notAvalibleLakelist.add(usep);
		for(Player p:storage.playerlist){
			if(isInlist(p, storage.notAvalibleLakelist)){
				continue;
			}
			list.add(p);
			
		}
		return list;
	}
	public boolean isInlist(Player np,ArrayList<Player> list){
		for(Player p:list){
			if(p.getNo()==np.getNo()){
				return true;
			}
		}
		return false;
	}

	public int getTotalPlayers(){
		return storage.totalplayers;
	}
	public void initTotalPlayers(){
		boolean isseted=false;
		int input =0;
		while(!isseted){
			try {
				input=getScanner().nextInt();
			} catch (Exception e) {
				getScanner().nextLine();
				continue;
			}
			if(input<5){
				System.out.println("不符合預期的輸入");
				continue;
			}
			if(input>10){
				System.out.println("不符合預期的輸入");
				continue;
			}
			storage.totalplayers=input;
			setMaxJusticeAndEvil(input);
			isseted=true;
			
		}
	}
	public void setMaxJusticeAndEvil(int totaplayers){
		switch(totaplayers){
			case (6):
				storage.maxjustice=4;
				storage.maxevil=2;
			break;
			case (7):
				storage.maxjustice=4;
				storage.maxevil=3;
			break;
			case (8):
				storage.maxjustice=5;
				storage.maxevil=3;
			break;
			case (9):
				storage.maxjustice=6;
				storage.maxevil=3;
			break;
			case (10):
				storage.maxjustice=6;
				storage.maxevil=4;
			break;
				
				
		}

	}
	public void setLakeLadyPlayer(Player p){
		storage.nowlakeLadyPlayer=p;
	}
	public Player getLakeLadyPlayer(int round ,int kingNo){

		if(round==2){
			if(storage.nowlakeLadyPlayer==null){
				storage.lakeLadyPlayerNo=storage.totalplayers-1;//給予最後一位玩家0123456 6號		
			}
			storage.nowlakeLadyPlayer=storage.playerlist.get(storage.lakeLadyPlayerNo);
		}
	
		return storage.nowlakeLadyPlayer;
	}

	public int getDisagreeMissionCri(int round){
		if(round==4){
			if(storage.totalplayers>6){
				return 2;
			}
		}
		return 1;
	}

	public int [] getMissionBoard(){
		
		if(storage.totalplayers==5){
			int mission []= {2,3,2,3,3};
			return mission;
		}
		if(storage.totalplayers==6){
			int mission []= {2,3,4,3,4};
			return mission;
		}
		if(storage.totalplayers==7){
			int mission []= {2,3,3,4,4};
			return mission;
		}
		if(storage.totalplayers==8){
			int mission []= {3,4,4,5,5};
			return mission;
		}
		if(storage.totalplayers==9){
			int mission []= {3,4,4,5,5};
			return mission;
		}
	
		int mission []= {2,3,2,3,3};
		return mission;
		
	}
	
	public  void initPlayersNameAndAllocate() {
		
		int sumofjustice = 0;
		int sumofevil = 0;
		storage.playerlist.clear();
		for (int i = 0; i < storage.totalplayers; i++) {
			System.out.println("請輸入玩家的名字 第 " + (i + 1) + "位名字是:");
			String name = getScanner().next();
			Player p = new Player(i, name);
			storage.playerlist.add(p);

			boolean israndomed = false;
			// System.out.println("j"+sumofjustice+",e"+sumofevil);
			while (!israndomed) {
				int r = ThreadLocalRandom.current().nextInt(1, 2 + 1);
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
					if (sumofjustice == 1) {
						p.setPart(Part.merlin);
					}
					if (sumofjustice == 2) {
						p.setPart(Part.percival);
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
					if (sumofevil == 1) {
						p.setPart(Part.morgana);
					}

				}

				israndomed = true;

			}
			 System.out.println(name + " 你好!" + p.getC().toString()+p.getPart());

		}

		//return list;
	}

}
