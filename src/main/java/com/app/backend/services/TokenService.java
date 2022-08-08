package com.app.backend.services;

import com.app.backend.dtos.user.UserLoginDTO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService{

    private static final int TOKEN_EXPIRATION_TIME = 1_200_000;
    private static final String TOKEN_SECRET = "2f9ca687-89e4-4602-b47e-cf3cfc08737f";

    public static String returnToken(UserLoginDTO userLoginDTO) {
        return JWT.create()
                .withSubject(userLoginDTO.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(TOKEN_SECRET));
    }

    public static boolean validToken(String token){
        if(token == null) return false;

        long nowDate = new Date(System.currentTimeMillis()).getTime();
        long tokenDate = JWT.decode(token).getExpiresAt().getTime();

        return nowDate <= tokenDate;
    }
}
