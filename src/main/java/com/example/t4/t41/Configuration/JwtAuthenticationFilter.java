package com.example.t4.t41.Configuration;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.t4.t41.Services.MyUserDetailsService;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtil jwtUtil;
    
    @Autowired
    MyUserDetailsService myUserDetailsService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
       String header=request.getHeader("Authorization");
       String username="",jwt="";
       if(header!=null && header.startsWith("Bearer "))
       {
        jwt=header.substring(7);
        username=jwtUtil.extractUsername(jwt);
       }
       if(!username.equals("") && SecurityContextHolder.getContext().getAuthentication()==null)
       {
        UserDetails userDetails=myUserDetailsService.loadUserByUsername(username);
        if(jwtUtil.validateToken(jwt))
        {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                              new UsernamePasswordAuthenticationToken(jwt, userDetails,userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);                  
        }

       }
       filterChain.doFilter(request, response);
    }
}
