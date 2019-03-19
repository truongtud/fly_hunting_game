package Model;

import java.util.HashMap;

public class HuntedMsg extends Msg {

    private Fly huntedFly;
    private String name;
    private int score;
    private HashMap<String, Integer> currentScores;

    public HuntedMsg(Fly pos, HashMap<String, Integer> scores) {
        this.huntedFly = pos;
        this.currentScores = scores;
    }

    public Fly getHuntedFly() {
        return this.huntedFly;
    }

    public String getName() {
        return this.name;
    }

    public int getScore() {
        return this.score;
    }

    /**
     * @return the currentScores
     */
    public HashMap<String, Integer> getCurrentScores() {
        return currentScores;
    }

    /**
     * @param currentScores the currentScores to set
     */
    public void setCurrentScores(HashMap<String, Integer> currentScores) {
        this.currentScores = currentScores;
    }
}