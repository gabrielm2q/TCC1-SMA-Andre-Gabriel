/**
 *  Class responsible to Start the application
 */
package even_or_odd;

import jade.core.AID;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.AgentContainer;
import jade.wrapper.ContainerController;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

/**
 * Class that set the main agent and it's actions
 */
public class App extends Agent{

	private static final long serialVersionUID = 1L;
	
	private static final String START = "START";
	
	private AgentController m1 = null;
	
	Object[] agentArgs = {};
	jade.wrapper.AgentContainer ac = Runtime.instance().createAgentContainer(new ProfileImpl());
	ContainerController cc = Runtime.instance().createMainContainer(new ProfileImpl());
    Throwable e;
	
	protected void setup() {
		
		System.out.println("Ola Mundo! ");
		System.out.println("Meu nome: " + getLocalName());
		
		try {
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			DFService.register(this, dfd);
			System.out.println(getLocalName()+" REGISTERED WITH THE DF");
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		System.out.println("Starting Agents...");
		
		String m1AgentName = "mediator";
		
		try {
			AgentContainer container = (AgentContainer)getContainerController(); // get a container controller for creating new agents
			m1 = container.createNewAgent(m1AgentName, "even_or_odd.Mediator", null);
			m1.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Agents started...");
		
		try {
            System.out.println("The system is paused -- this action is only here to let you activate the sniffer on the agents, if you want (see documentation)");
            System.out.println("Press enter in the console to start the agents");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		System.out.println("Starting system!");
		
		// send them a message demanding start;
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setContent(START);

		msg.addReceiver(new AID(m1AgentName, AID.ISLOCALNAME));
		
		send(msg);
	}
	
	protected void takeDown() {
		// Deregister with the DF
		try {
			DFService.deregister(this);
			System.out.println(getLocalName()+" DEREGISTERED WITH THE DF");
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}
}
