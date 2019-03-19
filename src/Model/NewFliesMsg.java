package Model;

import java.util.ArrayList;

public class NewFliesMsg extends Msg {

    private ArrayList<Fly> newFlyList;
    public String name;

    public NewFliesMsg(ArrayList<Fly> info, String n) {
        this.newFlyList = info;
        this.name = n;
    }

    public ArrayList<Fly> getNewFlyList() {
        return this.newFlyList;
    }
}
