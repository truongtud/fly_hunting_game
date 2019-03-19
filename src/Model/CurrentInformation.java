package Model;

import java.util.ArrayList;

public class CurrentInformation extends Msg {

    private String name;
    private ArrayList<Fly> currentInfo;
    private int currentScore, numberOfPlayer;
    int currentRound;

    public CurrentInformation(String clientName, ArrayList<Fly> info, int score, int round, int numberOfPlayer) {
        this.name = clientName;
        this.currentInfo = info;
        this.currentScore = score;
        this.currentRound = round;
        this.numberOfPlayer = numberOfPlayer;
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<Fly> getCurrentFlyList() {
        return this.currentInfo;
    }

    public int getCurrentScore() {
        return this.currentScore;
    }

    public int getCurrentRound() {
        return this.currentRound;
    }

    /**
     * @return the numberOfPlayer
     */
    public int getNumberOfPlayer() {
        return numberOfPlayer;
    }

    /**
     * @param numberOfPlayer the numberOfPlayer to set
     */
    public void setNumberOfPlayer(int numberOfPlayer) {
        this.numberOfPlayer = numberOfPlayer;
    }
}