package com.apgroup.auth.http;

import com.apgroup.auth.dto.AuthSuccessResponse;
import com.apgroup.auth.dto.StringErrorResponse;
import com.apgroup.auth.dto.UsernameResponse;

public class ResponseFactory {

    public static StringErrorResponse error(String cause) {
        return new StringErrorResponse(cause);
    }

    public static AuthSuccessResponse success(String username, String token) {
        return new AuthSuccessResponse(username, token);
    }

    public static UsernameResponse currentUser(String username) {
        return new UsernameResponse(username);
    }


}
