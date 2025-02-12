/**
 *  Class responsible to Start the application
 */
package even_or_odd;

import jade.core.Agent;
import jade.core.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.core.ProfileImpl;
import jade.core.Runtime;

import java.io.IOException;

/**
 * Class that set the main agent and it's actions
 */
public class App extends Agent{

	private static final long serialVersionUID = 1L;
	
	Object[] agentArgs = {};
	jade.wrapper.AgentContainer ac = Runtime.instance().createAgentContainer(new ProfileImpl());
	ContainerController cc = Runtime.instance().createMainContainer(new ProfileImpl());
    Throwable e;
	
	protected void setup() {
		
		try {
            System.out.println("The system is paused -- this action is only here to let you activate the sniffer on the agents, if you want (see documentation)");
            System.out.println("Press enter in the console to start the agents");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		System.out.println("Ola Mundo! ");
		System.out.println("Meu nome: " + getLocalName());
		System.out.println("iniciando agentes...");
		
		try {
			launchAgent(0, "even_or_odd.Player");
			launchAgent(1, "even_or_odd.Player");
			// TODO Auto-generated catch block
			launchAgent(2, "even_or_odd.Mediator");
			e.printStackTrace();
		} catch (Exception e) {
		}
		
		System.out.println("Agentes iniciados...");
	}
	
	void launchAgent(int id, String type) throws Exception {
		AgentController ag = ac.createNewAgent("fulano "+id, type, agentArgs);
		ag.start();
	}
}
