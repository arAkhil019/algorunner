package com.ar.AlgoRunner;

import com.ar.AlgoRunner.Models.box;
import com.ar.AlgoRunner.Services.MazeGen;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AlgoRunnerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(AlgoRunnerApplication.class, args);

        MazeGen mazeGen = ctx.getBean(MazeGen.class);
        box[][] maze = mazeGen.generateMaze();

//        ctx.close();
    }

}
