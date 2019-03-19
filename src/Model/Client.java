package Model;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import Controller.ClientController;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author truongtud
 */
public class Client {

    public HashMap<String, Integer> currentScores;
    public ArrayList<Fly> allFlies;
    public ArrayList<Fly> currentFlies;
    private ClientController clientController;
    public String namePlayer;
    public int score;
    public double[] currentPoint;
    public int round;
    public int numberOfPlayer;
//   public Client(String namePlayer){
//       this.namePlayer=namePlayer;
//       
//   }

    public Client(ClientController controller) {
        this.clientController = controller;
        this.currentScores = new HashMap<String, Integer>();
        this.allFlies = new ArrayList<>();
        this.currentFlies = new ArrayList<>();
        numberOfPlayer = this.score = 0;
        this.round = 1;

    }

    public void setName(String name) {
        this.namePlayer = name;
    }

    public String getName() {
        return this.namePlayer;
    }

    public void huntFly(String playerName) {
    }
}
