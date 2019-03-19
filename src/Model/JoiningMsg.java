package Model;

public class JoiningMsg extends Msg {

    private String name;

    public JoiningMsg(String clientName) {
        this.name = clientName;
    }

    public String getPlayerName() {
        return this.name;
    }
}