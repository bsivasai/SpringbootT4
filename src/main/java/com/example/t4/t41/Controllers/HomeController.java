package com.example.t4.t41.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.t4.t41.Configuration.JwtUtil;
import com.example.t4.t41.Models.AuthenticationRequest;
import com.example.t4.t41.Models.AuthenticationResponse;
import com.example.t4.t41.Services.MyUserDetailsService;

@RestController
public class HomeController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Autowired
    JwtUtil jwtUtil;
    
    @RequestMapping("/home")
    public String homee()
    {
        return "home";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> generateJwt(@RequestBody AuthenticationRequest authenticationRequest) throws Exception
    {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid username password",e);
        }
        UserDetails userDetails=myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        String jwt=jwtUtil.generateToken(userDetails);
        return new ResponseEntity<>(new AuthenticationResponse(jwt),HttpStatus.OK);
    }
}
