package com.group1.project3.OAuth;

import com.group1.project3.entity.Permission;
import com.group1.project3.entity.User;
import com.group1.project3.repository.UserRepository;
import com.group1.project3.service.UserProfileService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService{

    private final UserRepository userRepository;
    private final OAuthUserAttributesResolver oAuthUserAttributesResolver;
    private final UserProfileService userProfileService;

    public CustomOAuth2UserService(
            UserRepository userRepository,
            OAuthUserAttributesResolver oAuthUserAttributesResolver,
            UserProfileService userProfileService
    ){
        this.userRepository = userRepository;
        this.oAuthUserAttributesResolver = oAuthUserAttributesResolver;
        this.userProfileService = userProfileService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oauthUser = fetchOAuthUser(request);
        Map<String, Object> attrs = oauthUser.getAttributes();
        String provider = oAuthUserAttributesResolver.resolveProvider(attrs);
        String subject = oAuthUserAttributesResolver.resolveSubject(provider, attrs);
        String username = oAuthUserAttributesResolver.resolveUsername(provider, attrs);
        String email = oauthUser.getAttribute("email");
        String nameAttributeKey = request.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        if(!StringUtils.hasText(nameAttributeKey)){
            nameAttributeKey = "id";
        }

        User user = userRepository.findByAuthProviderAndAuthSubject(provider, subject)
                .map(existingUser -> {
                    Permission desiredPermission = oAuthUserAttributesResolver.resolvePermission(existingUser);
                    if(existingUser.getPermission() != desiredPermission){
                        existingUser.setPermission(desiredPermission);
                    }
                    existingUser.setUsername(username);
                    existingUser.setEmail(email);
                    existingUser.setOAuthProvider(provider);
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(username);
                    newUser.setEmail(email);
                    newUser.setOAuthProvider(provider);
                    newUser.setOauthSubject(subject);
                    newUser.setPermission(Permission.USER);
                    User savedUser = userRepository.save(newUser);
                    userProfileService.create(savedUser);
                    return savedUser;
                });

        Set<GrantedAuthority> authorities = new HashSet<>(oauthUser.getAuthorities());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getPermission().name()));

        return new DefaultOAuth2User(authorities, oauthUser.getAttributes(), nameAttributeKey);
    }

    protected OAuth2User fetchOAuthUser(OAuth2UserRequest request){ return super.loadUser(request);}
}
