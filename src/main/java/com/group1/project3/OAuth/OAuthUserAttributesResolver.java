package com.group1.project3.OAuth;

import com.group1.project3.entity.Permission;
import com.group1.project3.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Component
public class OAuthUserAttributesResolver {

    public OAuthUserAttributesResolver(){

    };

    public String resolveProvider(Map<String, Object> attrs){
        if(attrs.get("sub") !=null){
            return "google";
        }
        if(attrs.get("id") != null || attrs.get("login") != null) {
            return "github";
        }

        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Unsupported OAuth provider");
    }

    public String resolveSubject(String provider, Map<String, Object> attrs){
        Object subject = "google".equals(provider) ? attrs.get("sub") : attrs.get("id");

        if(subject == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expired or invalid");
        }
        return subject.toString();
    }

    public String resolveUsername( String provider, Map<String, Object> attrs) {
        if("google".equals(provider)){
            String email = (String) attrs.get("email");
            if (StringUtils.hasText(email)){
                return email;
            }

            String name = (String) attrs.get("name");
            if(StringUtils.hasText(name)){
                return name;
            }

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Google account is missing a usable username");
        }

        String githubLogin = (String) attrs.get("login");
        if(!StringUtils.hasText(githubLogin)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Github account is missing a login");
        }

        return githubLogin;
    }

    public Permission resolvePermission(User user){
        return user.getPermission();
    }
}
