package com.turplespace.renderit.feature.host;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;



class HostSiteRequest {
    private String subdomin;
    private String title;
    private String github;

    public String getSubdomin() {
        return subdomin;
    }
    public void setSubdomin(String subdomin) {
        this.subdomin = subdomin;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getGithub() {
        return github;
    }
    public void setGithub(String github) {
        this.github = github;
    }
    
}

@Controller
public class HostController {
    
    
    @PostMapping("/host")
    @ResponseBody
    public ResponseEntity<String> hostSite(@RequestBody HostSiteRequest request) {
        System.out.println("Subdomain: " + request.getSubdomin());
        System.out.println("Title: " + request.getTitle());
        System.out.println("GitHub: " + request.getGithub());
        return ResponseEntity.ok("Site hosted successfully!");
    }

    
}
