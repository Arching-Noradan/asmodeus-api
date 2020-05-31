package space.technological.modules.file.objects;

import space.technological.api_objects.APIObject;

public class LocalFileStorage extends APIObject {
    public String localPath;
    public String baseURL;
    public long partitionSize;
    public long partitionUsed;
}
