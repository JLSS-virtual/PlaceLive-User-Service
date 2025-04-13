package com.jlss.placelive.userservice.client;

import com.jlss.placelive.userservice.dto.UserDto;
import com.jlss.placelive.userservice.entity.User;
import org.apache.catalina.UserDatabase;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SearchServiceUserClient {

    //instantiating the sigletone restTemplate
    private final RestTemplate restTemplate;

    public SearchServiceUserClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * The Search and suer service internal calls are here
     * 1.for indexing.
     * 2.for updating(similar to indexing but with id too)
     * 3.for deletion
     * **/
    public void  postUserToSearchService(UserDto userDto){
        String url = "http://localhost:8084/placelive-search-service/v1/api/elasticSearch/user";

        restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(userDto),
                new ParameterizedTypeReference<Void>(){}
        );
    }
    public void putUserToSearchService(Long userId, UserDto userDto){
        String url = "http://localhost:8084/placelive-search-service/v1/api/elasticSearch/userupdate/{id}";

        restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(userDto),
                new ParameterizedTypeReference<Void>() {},
                userId // 👈 this maps to {id} in the URL
        );
    }

    //TODO use atleast resposentity to track that the services are what returing
    public  void deleteUserToSearchService(Long userId){
        String url = "http://localhost:8084/placelive-search-service/v1/api/elasticSearch/user/{userId}";
        restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Void>(){},
                userId
        );
    }
}
