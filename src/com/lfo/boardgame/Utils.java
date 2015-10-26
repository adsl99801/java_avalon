package com.lfo.boardgame;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {
	private Storage  storage;


	public Utils(Storage storage) {
		this.storage = storage;
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
			for(Player n:storage.notAvalibleLakelist){
				if(p.getNo()==n.getNo()){
					continue;
				}
				list.add(p);
			}
			
		}
		return list;
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
			isseted=true;
			
		}
	}
	public void setLakeLadyPlayer(Player p){
		storage.nowlakeLadyPlayer=p;
	}
	public Player getLakeLadyPlayer(int round ,int kingNo){
		Player p;
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
			if(storage.totalplayers>5){
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
			int mission []= {2,3,3,4,4};
			return mission;
		}
		if(storage.totalplayers==7){
			int mission []= {3,4,4,5,5};
			return mission;
		}
		int mission []= {2,3,2,3,3};
		return mission;
		
	}
	
	public  void initPlayersNameAndAllocate() {
		
		int sumofjustice = 0;
		int sumofevil = 0;
		for (int i = 0; i < storage.totalplayers; i++) {
			System.out.println("請輸入玩家的名字 第 " + (i + 1) + "位名字是:");
			String name = storage.scanners[4].next();
			Player p = new Player(i, name);
			storage.playerlist.add(p);

			boolean israndomed = false;
			// System.out.println("j"+sumofjustice+",e"+sumofevil);
			while (!israndomed) {
				int r = ThreadLocalRandom.current().nextInt(1, 2 + 1);
				// System.out.println("隨機數字" + r);
				if (r == 1) {
					if (sumofjustice >= 3) {
						if (sumofevil >= 2) {
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
					if (sumofevil >= 2) {
						if (sumofjustice >= 3) {
							break;
						}
						continue;
					}
					p.setC(Camp.evil);
					sumofevil++;
					if (sumofjustice == 1) {
						p.setPart(Part.morgana);
					}

				}

				israndomed = true;

			}
			// System.out.println(name + " 你好!" + p.getC().toString());

		}

		//return list;
	}

}
