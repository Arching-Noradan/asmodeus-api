package space.technological.modules.map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import space.technological.api_objects.APIObject;
import space.technological.api_objects.Error;
import space.technological.api_objects.Response;
import space.technological.modules.auth.AuthDatabase;
import space.technological.modules.auth.objects.Token;
import space.technological.modules.auth.objects.User;
import space.technological.modules.file.FileUtils;
import space.technological.modules.file.objects.LocalFileStorage;
import space.technological.modules.map.objects.Map;

@RestController
public class MapController {
    @RequestMapping("maps/getMaps")
    public APIObject getMaps(@RequestParam(value = "token") String token) {
        if (AuthDatabase.isTokenValid(token)) {
            Token token_object = AuthDatabase.getToken(token);
            User user = AuthDatabase.getUserByToken(token_object);
            return new Response<>(MapDatabase.getMaps(user.username));

        } else {
            return new Error<>("API-001-8", "Invalid token provided");
        }
    }

    @RequestMapping("maps/getMap")
    public APIObject getMap(@RequestParam(value = "token") String token,
                            @RequestParam(value = "map-id") String uid) {
        if (AuthDatabase.isTokenValid(token)) {
            Token token_object = AuthDatabase.getToken(token);
            User user = AuthDatabase.getUserByToken(token_object);
            Map map = MapDatabase.getMap(user.username, uid);
            if (map == null) {
                return new Error<>("API-001-9", "Object does not exist");
            } else {
                return map;
            }

        } else {
            return new Error<>("API-001-8", "Invalid token provided");
        }
    }

    @PostMapping("maps/createMap")
    public APIObject createMap(@RequestParam(value = "token") String token,
                               @RequestParam("file") MultipartFile file,
                               @RequestParam(value = "map_name") String map_name) {

        if (AuthDatabase.isTokenValid(token)) {
            Token token_object = AuthDatabase.getToken(token);
            User user = AuthDatabase.getUserByToken(token_object);
            try {
                return FileUtils.uploadFile(file, user, map_name);
            } catch (Exception a) {
                a.printStackTrace();
                return new Response<>(false);
            }
        } else {
            return new Error<>("API-001-8", "Invalid token provided");
        }
    }
}
