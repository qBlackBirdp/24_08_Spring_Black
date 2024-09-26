package com.example.blackbirdlofi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final OAuth2AuthorizedClientManager authorizedClientManager;

    @Autowired
    public TokenService(OAuth2AuthorizedClientManager authorizedClientManager) {
        this.authorizedClientManager = authorizedClientManager;
    }

    public String callProtectedApi(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId("google")
                .principal(authentication)
                .build();

        OAuth2AuthorizedClient authorizedClient = this.authorizedClientManager.authorize(authorizeRequest);

        if (authorizedClient != null) {
            // 액세스 토큰을 얻어 API 호출 가능
            String accessToken = authorizedClient.getAccessToken().getTokenValue();

            // 이 액세스 토큰으로 API 호출 (예: RestTemplate 사용)
            return "API 호출 성공";
        }

        return "API 호출 실패";
    }
}
