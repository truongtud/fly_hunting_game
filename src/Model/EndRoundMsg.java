package Model;

import java.util.HashMap;

public class EndRoundMsg extends Msg {

    private String name;
    private int score;
    private HashMap<String, Integer> currentScores;

    public EndRoundMsg(HashMap<String, Integer> scores) {

        this.currentScores = scores;
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