package com.ar.AlgoRunner;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping("/")
    public String home() {
        return "Welcome to the AlgoMaze Runner!";
    }

    @GetMapping("/maze")
    public box[][] getMaze() {
        box[][] gameMaze = new MazeGen().generateMaze();
        return gameMaze;
    }
}
