package scripts;

import javafx.scene.input.KeyCode;

public class Testing {
	public static void main(String[] args) {
		System.out.println(KeyCode.getKeyCode(String.valueOf('c').toUpperCase()));

		System.out.println(KeyCode.getKeyCode("C"));
	}
}
