package space.technological.modules.map.objects;

public class NPC extends MapObject {
    public String icon;
    public String form;
    public String hp;

    public Coordinates coordinates;

    public NPC() {
        this.type = "npc";
    }
}

