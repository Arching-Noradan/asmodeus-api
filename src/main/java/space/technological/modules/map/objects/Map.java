package space.technological.modules.map.objects;

import space.technological.api_objects.APIObject;

import java.util.ArrayList;
import java.util.List;

public class Map extends APIObject {

    // Basic Info
    public String name;
    public String uid;
    public String owner;
    public String file;
    public int pixel_per_square;

    // Map Components
    public ArrayList<MapObject> map_objects = new ArrayList<>();
}
