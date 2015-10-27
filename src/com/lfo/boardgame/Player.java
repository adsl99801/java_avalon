package com.lfo.boardgame;

public class Player {

	private int no;
	private String name;
	private int point;
	private Camp c;
	private boolean isVoteAgree = true;
	private boolean isMissionAgree = true;
	private Part part;
	
	public Player(int no, String name) {
		this.no = no;
		this.name = name;
	}

	public void setIsMissionAgree(boolean isMissionAgree) {
		this.isMissionAgree = isMissionAgree;
	}

	public boolean isMissionAgree() {
		return isMissionAgree;
	}

	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	public void setIsAgree(boolean isAgree) {
		this.isVoteAgree = isAgree;
	}

	public boolean isAgree() {
		return isVoteAgree;
	}



	public void setC(Camp c) {
		this.c = c;
	}

	public Camp getC() {
		return c;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getPoint() {
		return point;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getNo() {
		return no;
	}
}
