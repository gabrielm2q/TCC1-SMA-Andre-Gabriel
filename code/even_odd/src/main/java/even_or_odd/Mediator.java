package even_or_odd;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
// import jade.wrapper.StaleProxyException;

public class Mediator extends Agent {

	private static final long serialVersionUID = 1L;
	
	public final static String ODD = "ODD";
	public final static String EVEN = "EVEN";
	public final static String REQUEST = "REQUEST";
	public final static String ANSWER = "ANSWER";
	public final static String THANKS = "THANKS";
	public final static String START = "START";
	
	private int answersCnt = 0;
	
	private int inpA, inpB;
	
	private AgentController p1 = null;
	private AgentController p2 = null;
	private AID initiator = null;
	
	protected void setup() {
		
		System.out.println("Sou o mediador!");
	
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			initiator = new AID((String) args[0], AID.ISLOCALNAME);
		}
		
		try {
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			DFService.register(this, dfd);
			System.out.println(getLocalName()+" REGISTERED WITH THE DF");
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		// create another two ThanksAgent
		String p1AgentName = "player_1";
		String p2AgentName = "player_2";

		try {
			// create agents t1 and t2 on the same container of the creator agent
			AgentContainer container = (AgentContainer)getContainerController(); // get a container controller for creating new agents
			p1 = container.createNewAgent(p1AgentName, "even_or_odd.Player", null);
			p1.start();
			p2 = container.createNewAgent(p2AgentName, "even_or_odd.Player", null);
			p2.start();
			System.out.println(getLocalName()+" CREATED AND STARTED NEW PLAYERS: "+p1AgentName + " AND " + p2AgentName + " ON CONTAINER "+container.getContainerName());
		} catch (Exception any) {
			any.printStackTrace();
		}
		
		addBehaviour(new CyclicBehaviour(this) {
			public void action() {
				// listen if a greetings message arrives
				ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
				if (msg != null) {
					if(ANSWER.equalsIgnoreCase(msg.getContent().split(" ")[0])) {
						// if an ANSWER to a greetings message is arrived 
						// then send a THANKS message
						System.out.println(myAgent.getLocalName()+" RECEIVED ANSWER MESSAGE FROM "+msg.getSender().getLocalName()); 
						// System.out.println(myAgent.getLocalName()+" " + msg.getContent()); 
						ACLMessage replyT = msg.createReply();
						replyT.setContent(THANKS);
						myAgent.send(replyT);
						System.out.println(myAgent.getLocalName()+" SENT THANKS MESSAGE"); 
						
						if(msg.getSender().getName() == p1AgentName) {
							inpA = Integer.parseInt(msg.getContent().split(" ")[1]);
						}else {
							inpB = Integer.parseInt(msg.getContent().split(" ")[1]);
						}
						
						answersCnt++;
						if (answersCnt == 2) {
							
							ACLMessage replyW = msg.createReply();
							replyW.setContent((((inpA + inpB) % 2 == 0)? ODD + " " + p1AgentName: EVEN + " " + p2AgentName)+ " WINNER!");
							replyW.addReceiver(new AID(p1AgentName, AID.ISLOCALNAME));
							myAgent.send(replyW);
							System.out.println(myAgent.getLocalName()+" SENT WINNER MESSAGE"); 
							// System.out.println(myAgent.getLocalName()+" " + replyW.getContent()); 
							
							// All answers have been received. 
							// Wait a bit to be sure the other Thanks agents gets the Thank message,
							// then kill everybody
							// try {
							// 	Thread.sleep(1000);
							// } 
							// catch (InterruptedException ie) {}
							// try {
								// Kill Agents
							//	p1.kill();
							//	p2.kill();
								
								// Notify the initiator if any
							//	if (initiator != null) {
							//		ACLMessage notification = new ACLMessage(ACLMessage.INFORM);
							//		notification.addReceiver(initiator);
							//		send(notification);
							//	}	
							// } 
							// catch (StaleProxyException any) {
							//	any.printStackTrace();
							// }
						}
					} else if (START.equalsIgnoreCase(msg.getContent())) {
						// send them a message requesting for a number;
						ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
						msg2.setContent(REQUEST);

						msg2.addReceiver(new AID(p1AgentName, AID.ISLOCALNAME));
						msg2.addReceiver(new AID(p2AgentName, AID.ISLOCALNAME));

						send(msg2);
						System.out.println(getLocalName()+" SENT REQUEST MESSAGE  TO "+p1AgentName+" AND "+p2AgentName); 
					}
					else {
						System.out.println(myAgent.getLocalName()+" Unexpected message received from "+msg.getSender().getLocalName()); 
					}					
				}
				else {
					// if no message is arrived, block the behaviour
					block();
				}
			}
		});
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
