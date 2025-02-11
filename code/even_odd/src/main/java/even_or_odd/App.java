/**
 *  Class responsible to Start the application
 */
package even_or_odd;

import jade.core.Agent;

/**
 * Class that set the main agent and it's actions
 */
public class App extends Agent{

	private static final long serialVersionUID = 1L;
	
	protected void setup() {
		System.out.println("Ola Mundo! ");
		System.out.println("Meu nome: " + getLocalName());
	}

}
