package Controller;


//import Model.IGameServer;

import Model.*;
import View.GameMainFrame;
import View.LoginFrame;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientController {

    static String activeMqUrl = ActiveMQConnection.DEFAULT_BROKER_URL;
    static String topicName = "FlyHunted4";
    public Client client;
    private GameMainFrame gameMainFrame;
    private LoginFrame loginFrame;
    final String nameServer = "GameHuntFlyServer";
    public int round = 1;
    private ImageIcon image;
    static Connection connection;
    static ConnectionFactory connectionfactory;
    static Session subcriberSession;
    static TopicSubscriber subscriber;

    public ClientController() {
        try {

            this.client = new Client(this);

            this.loginFrame = new LoginFrame();
            this.loginFrame.setVisible(true);

            this.gameMainFrame = new GameMainFrame(this, this.client);
            this.loginFrame.loginButton
                    .addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            loginButtonClicked(evt);
                        }
                    });
            this.gameMainFrame.logoutButton
                    .addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            logoutButtonClicked(evt);
                        }
                    });

            //	image = new ImageIcon("/client/View/fliege.jpg");

        } catch (Exception ex) {
            Logger.getLogger(ClientController.class.getName()).log(
                    Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param evt
     */
    private void loginButtonClicked(java.awt.event.MouseEvent evt) {
        try {
            //Registry registry = LocateRegistry.getRegistry();
            //	server = (IGameServer) registry.lookup(nameServer);
            String namePlayer = this.loginFrame.userNameTextField.getText();
            client.setName(namePlayer);
            //  client.currentScores.put(playerName, 0);
            //this.publisher(namePlayer);
            //this.subscriber(namePlayer);           
            //playerName = server.login(playerName, client);
            //	System.out.println(playerName);
            JoiningMsg joiningMsg = new JoiningMsg(namePlayer);
            subscriber(namePlayer);
            publisher(joiningMsg);

            this.loginFrame.setVisible(false);
            this.gameMainFrame.setVisible(true);
            this.gameMainFrame.userNameLabel.setText(namePlayer);
            this.loginFrame.dispose();
        } catch (Exception ex) {

            System.err.println(ex.getMessage());
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE,
                    null, ex);
        }

    }

    public void publisher(Msg msg) {
        connectionfactory = new ActiveMQConnectionFactory(activeMqUrl);
        try {
            connection = connectionfactory.createConnection();
            //connection.setClientID(namePlayer);
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic(topicName);
            MessageProducer messageProducer = session.createProducer(destination);
            ObjectMessage objectMessage = session.createObjectMessage(msg);
            // System.out.println("send mess" + msg);
            messageProducer.send(objectMessage);
            session.close();
            connection.close();
        } catch (JMSException ex) {

            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void subscriber(String uniqueID) {
        connectionfactory = new ActiveMQConnectionFactory(activeMqUrl);
        try {
            connection = connectionfactory.createConnection();
            connection.setClientID(uniqueID);
            connection.start();
            subcriberSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = subcriberSession.createTopic(topicName);

            subscriber = subcriberSession.createDurableSubscriber(topic, "Subscriber");

            subscriber.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message msg) {
                    try {
                        if (msg instanceof ObjectMessage) {

                            Object objMessage = ((ObjectMessage) msg).getObject();
                            if (objMessage instanceof JoiningMsg) {

                                String newPlayerName = ((JoiningMsg) objMessage).getPlayerName();
                                //  System.out.println("receive new player" + newPlayerName);
                                client.currentScores.put(newPlayerName, 0);
                                gameMainFrame.updatePlayersPanel(client.currentScores);
                                CurrentInformation ci = new CurrentInformation(client.getName(), client.currentFlies, client.score, client.round, client.currentScores.size());
                                publisher(ci);


                            }
                            if (objMessage instanceof CurrentInformation) {
                                CurrentInformation ci = (CurrentInformation) objMessage;
                                // System.out.println("receive ci " + ci.getName());
                                // System.out.println("current sco " + client.currentScores.toString());
                                if (!client.currentScores.containsKey(ci.getName()) && !client.namePlayer.equals(ci.getName())) {
                                    client.currentScores.put(ci.getName(), ci.getCurrentScore());
                                    client.allFlies.addAll(ci.getCurrentFlyList());
                                    client.currentFlies.addAll(ci.getCurrentFlyList());
                                    gameMainFrame.addFly(ci.getCurrentFlyList());
                                    for (Fly flyIamge : ci.getCurrentFlyList()) {
                                        flyIamge.addMouseListener(new java.awt.event.MouseAdapter() {
                                            public void mouseClicked(java.awt.event.MouseEvent evt) {
                                                // System.out.println("click");
                                                flyImageMouseClicked(evt);
                                            }
                                        });
                                    }
                                    if (client.currentScores.size() == ci.getNumberOfPlayer() + 1) {
                                        ArrayList<Fly> newList = new ArrayList<>();
                                        int size = ci.getCurrentFlyList().size();
                                        //   System.out.println("receive ci fly" + size);
                                        for (int i = 0; i < size; i++) {
                                            double[] ds = setPositionRandom();
                                            Fly f = new Fly(ds);
                                            newList.add(f);
                                        }
                                        NewFliesMsg fliesMsg = new NewFliesMsg(newList, client.namePlayer);
                                        publisher(fliesMsg);
                                    }
//                                    client.currentFlies.addAll(client.currentFlies);
//                                    //gameMainFrame.removeAllFly();
//                                    gameMainFrame.flyImageList = client.currentFlies;
//                                    gameMainFrame.addFly(gameMainFrame.flyImageList);
                                } else if (ci.getNumberOfPlayer() == 1 && ci.getName().equals(client.namePlayer)) {
                                    //  System.out.println("receive 1st ci");
//                                    String newPlayerName = ((JoiningMsg) objMessage).getPlayerName();
                                    client.currentScores.put(client.namePlayer, 0);
                                    gameMainFrame.updatePlayersPanel(client.currentScores);
                                    double[] ds = setPositionRandom();
                                    Fly f = new Fly(ds);
                                    ArrayList arr = new ArrayList<Fly>();
                                    arr.add(f);
                                    NewFliesMsg fliesMsg = new NewFliesMsg(arr, client.getName());
                                    publisher(fliesMsg);
                                }
                            }
                            if (objMessage instanceof NewFliesMsg) {
                                NewFliesMsg nf = (NewFliesMsg) objMessage;
                                // System.out.println("receive nf" + nf.name + "-" + nf.getNewFlyList().size());

                                client.currentFlies.addAll(nf.getNewFlyList());
                                client.allFlies.addAll(client.currentFlies);
                                //   System.out.println(client.currentFlies.size());
                                gameMainFrame.addFly(nf.getNewFlyList());
                                for (Fly flyIamge : nf.getNewFlyList()) {
                                    flyIamge.addMouseListener(new java.awt.event.MouseAdapter() {
                                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                            //  System.out.println("click");
                                            flyImageMouseClicked(evt);
                                        }
                                    });
                                }
                            }
                            if (objMessage instanceof HuntedMsg) {
                                HuntedMsg objMsg = (HuntedMsg) objMessage;
                                //  System.out.println("receive hm" + objMsg.getHuntedFly().x + "-" + objMsg.getHuntedFly().y);
                                Fly f = objMsg.getHuntedFly();
                                for (Fly fly : client.allFlies) {
                                    //  System.out.println(fly.x + "-" + fly.y);
                                    if (fly.x == f.x && fly.y == f.y) {
                                        //      System.out.println(fly);
                                        fly.setVisible(false);
                                        fly.repaint();
                                    }
                                }
                                client.currentScores = objMsg.getCurrentScores();
                                gameMainFrame.updatePlayersPanel(client.currentScores);
                            }
                            if (objMessage instanceof EndRoundMsg) {
                                EndRoundMsg objMsg = (EndRoundMsg) objMessage;
                                // System.out.println("receive er" + objMsg.getCurrentScores());
                                client.allFlies.removeAll(client.allFlies);
                                client.currentFlies.removeAll(client.currentFlies);
                                client.round += 1;
                                ArrayList<Fly> newList = new ArrayList<>();
                                for (int i = 0; i < Math.pow(2, client.round - 1); i++) {
                                    double[] ds = setPositionRandom();
                                    Fly f = new Fly(ds);
                                    newList.add(f);
                                }
                                NewFliesMsg fliesMsg = new NewFliesMsg(newList, client.getName());
                                publisher(fliesMsg);
                            }
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.out.println();
                    }

                }
            });

        } catch (JMSException ex) {
            //System.out.println("Loi cho nao nhi");
            System.out.println(ex);
        }


    }

    public void unsubscriber(String uniqueID) {
        ConnectionFactory connectionfactory = new ActiveMQConnectionFactory(activeMqUrl);
        try {
            Connection connection = connectionfactory.createConnection();
            connection.setClientID(uniqueID);
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            session.unsubscribe(uniqueID);
        } catch (JMSException ex) {
            System.out.println(ex);
        }
    }

    public double[] setPositionRandom() {

        return new double[]{Math.random(), Math.random()};
    }

    private void logoutButtonClicked(java.awt.event.MouseEvent evt) {
        logout();
    }

    public void logout() {
        try {
        	
         /*   client.currentScores.remove(client.namePlayer);
            CurrentInformation ci = new CurrentInformation(client.getName(), client.currentFlies, client.score, client.round, client.currentScores.size());
            publisher(ci);
            unsubscriber(client.namePlayer);*/
            unsubscriber(client.namePlayer);
            subscriber.close();
            subcriberSession.close();
            connection.close();

            this.gameMainFrame.dispose();
            System.exit(0);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    private void flyImageMouseClicked(java.awt.event.MouseEvent evt) {
        try {
            Fly f = (Fly) evt.getComponent();
            f.setVisible(false);
            int playerScore = (int) client.currentScores.get(client.namePlayer) + 1;
            client.currentScores.remove(client.namePlayer);
            client.currentScores.put(client.namePlayer, playerScore);
            HuntedMsg hm = new HuntedMsg(f, client.currentScores);
            publisher(hm);
            boolean allDied = true;
            //   System.out.println(client.allFlies.size());
            for (Fly f1 : client.allFlies) {
                //  System.out.println(f1.x + "-" + f1.y + f1.isVisible());
                if (f1.isVisible()) {
                    allDied = false;
                    break;
                }
            }
            if (allDied) {
                // System.out.println("all died");
                EndRoundMsg endRoundMsg = new EndRoundMsg(client.currentScores);
                publisher(endRoundMsg);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    public void UpdateCurrentScore() {
        gameMainFrame.updatePlayersPanel(this.client.currentScores);
    }

    public void UpdateFlyPositon() {
        for (Fly flyImage : client.allFlies) {
            flyImage
                    .setLocation(
                            (int) ((gameMainFrame.jPanel1.getWidth() - flyImage.getWidth()) * flyImage.x) + gameMainFrame.jPanel1.getX(),
                            (int) ((gameMainFrame.jPanel1.getHeight() - flyImage.getHeight()) * flyImage.y) + gameMainFrame.jPanel1.getY());
        }
        //System.out.println((int) ((gameMainFrame.jPanel1.getWidth()- gameMainFrame.flyImage.getWidth())*  this.client.currentPoint[0] )+gameMainFrame.jPanel1.getX());
    }

    public static void main(String args[]) {
        new ClientController();
    }
}
