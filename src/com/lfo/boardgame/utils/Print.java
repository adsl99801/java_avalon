package com.lfo.boardgame.utils;

import java.util.ArrayList;

import com.lfo.boardgame.Camp;
import com.lfo.boardgame.Player;

public class Print {
	public  void printmutiline(int lines) {
		for (int i = 0; i < lines; i++) {
			System.out.println("");
		}
	}
	public  void printNoNamelist(ArrayList<Player> list) {

		for (Player p : list) {
			System.out.println(p.getNo() + "." + p.getName());

		}

	}
	public  void printmissionboard(Camp[] board) {

		System.out.println("戰況公佈!");
		int round = 1;
		for (Camp c : board) {
			if (c != null) {
				System.out.println("第" + round + "局:" + c.toString() + "獲勝");

			}
			round++;
		}
	}

}
