package com.security.ansj;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BasicAuthenticationTest {

    @LocalServerPort
    int port;

    RestTemplate client = new RestTemplate();

    private String greetingUrl(){
        return "http://localhost:"+port+"/greeting";
    }

    @DisplayName("1. 인증 실패")
    @Test
    public void authenticationFailureTest(){
//        String response = client.getForObject(greetingUrl(), String.class);//401에러가 발생함
//        System.out.println(response);
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            client.getForObject(greetingUrl(), String.class);
        });
        assertEquals(401, exception.getRawStatusCode());
    }

    @DisplayName("2. 인증 실패")
    @Test
    void authenticationFailureTest2(){
        HttpHeaders headers = new HttpHeaders();
        headers.add(
                HttpHeaders.AUTHORIZATION,
                "Basic "+ Base64.getEncoder().encodeToString("user1:1111".getBytes())
        );
        HttpEntity entity = new HttpEntity(null, headers);
        ResponseEntity<String> resp = client.exchange(greetingUrl(), HttpMethod.GET, entity, String.class);
        assertEquals("hello", resp.getBody());
        System.out.println(resp.getBody());
    }

    @DisplayName("3. 인증성공")
    @Test
    void authenticationSuccessTest(){
        TestRestTemplate testClient = new TestRestTemplate("user1", "1111");//basicAuthentication이 기본임
        String resp = testClient.getForObject(greetingUrl(), String.class);
        assertEquals("hello", resp);
    }

    @DisplayName("4. POST 인증")
    @Test//post 요청은 csrf
    void postAuthentication(){
        System.out.println("port: " + port);
        TestRestTemplate testClient = new TestRestTemplate("user1","1111");
        ResponseEntity<String> resp = testClient.postForEntity(greetingUrl(), "ansj", String.class);
        assertEquals("hello ansj", resp.getBody());
    }
}
