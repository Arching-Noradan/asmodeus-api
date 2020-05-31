package space.technological.modules.file;

import org.springframework.web.multipart.MultipartFile;
import space.technological.Utils;
import space.technological.modules.auth.AuthDatabase;
import space.technological.modules.auth.objects.User;
import space.technological.modules.file.objects.File;
import space.technological.modules.file.objects.LocalFileStorage;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Pattern;

public class FileUtils {

    public static File uploadFile(MultipartFile multipartFile, User user, String filename) throws IOException {

        if (multipartFile.isEmpty()) {
            return new File();
        }

        Utils.info(multipartFile.getContentType());

        long free_for_user = user.storage_quota - user.used_storage;
        if (free_for_user < multipartFile.getSize()) {
            return new File();
        }

        ArrayList<LocalFileStorage> storages = FileDatabase.getFileStorageUnits();
        LocalFileStorage suitableLocalFileStorage = new LocalFileStorage();
        boolean isStorageSuitable = false;

        for (int i = 0; i != storages.size(); i++) {
            if (storages.get(i).partitionUsed + multipartFile.getSize() < storages.get(i).partitionSize) {
                suitableLocalFileStorage = storages.get(i);
                isStorageSuitable = true;
                break;
            }
        }

        if (isStorageSuitable) {

            String uuid = Utils.getUnixTime() + UUID.randomUUID().toString();
            String file = multipartFile.getOriginalFilename();
            String[] file_splitted = file.split(Pattern.quote("."));

            Path filepath = Paths.get(suitableLocalFileStorage.localPath + "/usercontent/", uuid + "." + file_splitted[file_splitted.length - 1]);
            multipartFile.transferTo(filepath);

            File file_object = new File();
            file_object.owner = user.username;
            file_object.size = multipartFile.getSize();
            file_object.name = filename;
            file_object.URL = suitableLocalFileStorage.baseURL + "/usercontent/" + uuid + "." + file_splitted[file_splitted.length - 1];

            LocalFileStorage updated = suitableLocalFileStorage;
            updated.partitionUsed = updated.partitionUsed + multipartFile.getSize();
            FileDatabase.updateLocalFileStorageUnit(updated);
            FileDatabase.saveFile(file_object);

            User updated_user = user;
            updated_user.used_storage = updated_user.used_storage + multipartFile.getSize();
            AuthDatabase.insertUser(updated_user);

            return file_object;
        } else {
            return null;
        }
    }

}
