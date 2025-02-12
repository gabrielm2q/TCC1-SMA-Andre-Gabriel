package even_or_odd;

import jade.core.Agent;

public class Player extends Agent {

	private static final long serialVersionUID = 1L;
	int njogador;
	
	protected void setup() {	
		System.out.println("Sou o jogador "+ njogador +"!");
	}

}
