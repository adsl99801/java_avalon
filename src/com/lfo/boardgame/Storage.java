package com.lfo.boardgame;

import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
	

	public boolean isUsingMordred=false;
	public boolean isUsingMogana=false;
	public boolean isUsingOberyn=false;
	public boolean isUsingPercival=false;
	
	public  ArrayList<Player> playerlist = new ArrayList<>();
	public  ArrayList<Player> notAvalibleLakelist = new ArrayList<>();//不可以被使用湖中女神的名單
	public  int maxjustice=3;
	public  int maxevil=2;
	public  int lakeLadyPlayerNo=0;
	public final int totalMissions=5;
	public Player nowlakeLadyPlayer=null;
	public   int denytimes = 0;// 投票否決累積次數
	public  static int mission[] = { 2, 3, 2, 3, 3 };
	//public   int disagreemissioncri = 1;// 若要任務失敗 所需的任務失敗卡個數
	
	public  int totalplayers=5;
	public   Camp missionScoreBoard[] ;//登記得分
	public final static  Scanner[] scanners = new Scanner[5];
	public final  Scanner s5 = new Scanner(System.in);
	public final  Scanner s2 = new Scanner(System.in);
	public final  Scanner s3 = new Scanner(System.in);
	public final  Scanner s1 = new Scanner(System.in);
	public final  Scanner s4 = new Scanner(System.in);
	public Storage(){
		scanners[0] = s1;
		scanners[1] = s2;
		scanners[2] = s3;
		scanners[3] = s4;
		scanners[4] = s5;
		missionScoreBoard = new Camp[totalMissions];
	}
}
