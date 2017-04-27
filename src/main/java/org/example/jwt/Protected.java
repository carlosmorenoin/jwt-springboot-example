package org.example.jwt;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Controller
public class Protected {

    @RequestMapping(value = "/protected", method = RequestMethod.GET)
    @ResponseBody
    public String getProtectedResource(HttpServletRequest request) {

        if (!validateToken(request)) {
            throw new ForbiddenException();
        }
        return "PROTECTED RESOURCE";
    }


    private String retrieveJWT(HttpServletRequest request) {
        return request.getHeader("access_token");
    }

    private boolean validateToken(final HttpServletRequest request) {
        final String jwt = retrieveJWT(request);
        if (jwt == null) {
            return false;
        }
        Claims claims = Jwts.parser().setSigningKey(Secret.getKey()).parseClaimsJws(jwt).getBody();

        return "EXTERNAL".equals(claims.get("externalServiceId"))
                && "demo".equals(claims.getSubject());

    }
}
