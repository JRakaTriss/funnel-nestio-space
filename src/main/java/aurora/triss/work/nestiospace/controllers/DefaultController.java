package aurora.triss.work.nestiospace.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

    @GetMapping("/")
    public String defaultContext(){
        return "The Nestio Space Monitoring Server is Online";
    }
}
