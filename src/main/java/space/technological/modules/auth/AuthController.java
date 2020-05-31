package space.technological.modules.auth;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import space.technological.Utils;
import space.technological.api_objects.APIObject;
import space.technological.api_objects.Error;
import space.technological.api_objects.Response;
import space.technological.modules.auth.objects.ProfileInfo;
import space.technological.modules.auth.objects.Token;
import space.technological.modules.auth.objects.User;

import java.io.IOException;
import java.util.UUID;

@RestController
public class AuthController {
    @PostMapping("auth/register")
    public APIObject registration(@RequestParam(value = "username") String username,
                                  @RequestParam(value = "email") String email,
                                  @RequestParam(value = "password") String password) {
        if (password.length() > 256) {
            return new Error<String>("API-001-1", "Password is too long. Max: 256");
        }
        String username_lowered = username.toLowerCase();
        if (!username_lowered.matches("^[a-zA-Z0-9_]*$") || username_lowered.toCharArray().length > 16 || username_lowered.toCharArray().length < 4) {
            return new Error<>("API-001-2", "Only alphanumeric characters are allowed. Username must have from 4 to 16 characters.");
        }
        if (!email.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$") || email.toCharArray().length > 64) {
            return new Error<>("API-001-3", "Only valid emails are allowed. Length must be under 64 characters.");
        }
        if (AuthDatabase.checkIfEmailUsed(email)) {
            return new Error<>("API-001-4", "Email is already used.");
        }
        if (AuthDatabase.checkIfUsernameUsed(username_lowered)) {
            return new Error<>("API-001-5", "Username is already used.");
        }

        User user = new User();
        user.email = email;
        user.username = username_lowered;
        user.password_md5 = Utils.md5(password);
        user.password_sha256 = Utils.sha256(password);
        user.storage_quota = 134217728;
        user.avatar = "https://technological.space/assets/images/profile/maksandra.jpg";

        user.ifEmailVerified = false;
        user.emailVerificationCode = Utils.getUnixTime() + Utils.sha256(UUID.randomUUID().toString());
        try {
            Mailer.sendVerificationEmail(user.emailVerificationCode, email, username);
        } catch (IOException a) {
            return new Error<>("API-INT-1", "Internal mailer error. Contact developer: maksandra@asmodeus.app");
        }

        AuthDatabase.insertUser(user);
        return new Response<>(true, "OK");
    }

    @PostMapping("auth/login")
    public APIObject registration(@RequestParam(value = "username") String username,
                                  @RequestParam(value = "password") String password) {

        String username_lowered = username.toLowerCase();

        if (password.length() > 256) {
            return new Error<>("API-001-1", "Password is too long. Max: 256");
        }
        if (!username_lowered.matches("^[a-zA-Z0-9_]*$") || username_lowered.toCharArray().length > 16 || username_lowered.toCharArray().length < 4) {
            return new Error<>("API-001-2", "Only alphanumeric characters are allowed. Username must have from 4 to 16 characters.");
        }

        User user = AuthDatabase.getUserByUsername(username_lowered);
        if (user == null) {
            return new Error<>("API-001-9", "Object does not exist");
        }
        if (user.ifEmailVerified) {
            if (user.password_md5.equals(Utils.md5(password)) && user.password_sha256.equals(Utils.sha256(password))) {

                // TODO: REMOVE TEMPORARY
                if (!user.isBetaTester) {
                    return new Error<>("API-TMP-1", "Only beta-testers have access to service right now. Sorry.");
                }

                Token token = new Token();
                token.expiration_date = Utils.getUnixTime() + 604800;
                token.token = Utils.getUnixTime() + UUID.randomUUID().toString();
                token.user = username_lowered;
                AuthDatabase.insertToken(token);
                return token;
            } else {
                return new Error<>("API-001-6", "Invalid password");
            }
        } else {
            return new Error<>("API-001-7", "User is not verified");
        }
    }

    @PostMapping("auth/logout")
    public APIObject registration(@RequestParam(value = "token") String token) {
        Token token_object = AuthDatabase.getToken(token);
        if (token_object == null) {
            return new Error<>("API-001-8", "Invalid token provided");
        }
        AuthDatabase.deleteToken(token_object);
        return new Response<>(true, "OK");

    }
    @RequestMapping("auth/confirmation")
    public ModelAndView confirmation(@RequestParam(value = "key") String key, ModelMap model) {
        User user = AuthDatabase.getUserByKey(key);
        if (user == null) {
            return new ModelAndView("redirect:https://asmodeus.app/#invalid_link", model);
        }
        if (!user.ifEmailVerified) {
            user.ifEmailVerified = true;
            AuthDatabase.insertUser(user);
        }
        return new ModelAndView("redirect:https://asmodeus.app/#confirmed", model);
    }



    @PostMapping("user/profile_info")
    public APIObject profile_info(@RequestParam(value = "token") String token) {
        if (AuthDatabase.isTokenValid(token)) {
            Token token_object = AuthDatabase.getToken(token);
            User user = AuthDatabase.getUserByToken(token_object);
            ProfileInfo profileInfo = new ProfileInfo();
            profileInfo.avatar = user.avatar;
            profileInfo.username = user.username;
            return profileInfo;
        } else {
            return new Error<>("API-001-8", "Invalid token provided");
        }
    }
}
