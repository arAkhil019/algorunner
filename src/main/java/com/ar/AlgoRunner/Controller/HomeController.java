package com.ar.AlgoRunner.Controller;

import com.ar.AlgoRunner.Services.MazeGen;
import com.ar.AlgoRunner.Models.box;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    @PostMapping("/saveScore")
    public void saveScore(int score){

    }
}
