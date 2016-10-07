package logdog;

import java.awt.Color;

public class Player {
	
	private String name;
	private Color color;

	public Player() {
		this("", Color.black);
	}
	
	public Player(String name) {
		this(name, Color.black);
	}
	
	public Player(String name, Color color) {
		this.name = name;
		this.color = color;
	}
	
	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
}
