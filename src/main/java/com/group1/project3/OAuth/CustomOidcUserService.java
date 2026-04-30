package com.group1.project3.OAuth;

import com.group1.project3.entity.Permission;
import com.group1.project3.entity.User;
import com.group1.project3.repository.UserRepository;
import com.group1.project3.service.UserProfileService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository;
    private final OAuthUserAttributesResolver oAuthUserAttributesResolver;
    private final UserProfileService userProfileService;


    public CustomOidcUserService(UserRepository userRepository, OAuthUserAttributesResolver oAuthUserAttributesResolver, UserProfileService userProfileService){
        this.userRepository = userRepository;
        this.oAuthUserAttributesResolver = oAuthUserAttributesResolver;
        this.userProfileService = userProfileService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest request) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(request);

        Map<String, Object> attrs = oidcUser.getAttributes();

        String provider = "google";
        String subject = oAuthUserAttributesResolver.resolveSubject(provider, attrs);
        String email = oidcUser.getEmail();
        String username = oAuthUserAttributesResolver.resolveUsername(provider, attrs);

        User user = userRepository.findByAuthProviderAndAuthSubject(provider, subject)
                .map(existingUser -> {
                    existingUser.setUsername(username);
                    existingUser.setEmail(email);
                    existingUser.setOAuthProvider(provider);
                    existingUser.setOauthSubject(subject);
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
                    userProfileService.create(newUser);
                    return savedUser;
                });

        Set<GrantedAuthority> authorities = new HashSet<>(oidcUser.getAuthorities());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getPermission().name()));

        return new DefaultOidcUser(
                authorities,
                oidcUser.getIdToken(),
                oidcUser.getUserInfo(),
                "sub"
        );
    }
}
