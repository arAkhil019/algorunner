package com.ar.AlgoRunner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import java.util.Arrays;

@SpringBootApplication
public class AlgoRunnerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(AlgoRunnerApplication.class, args);

        MazeGen mazeGen = ctx.getBean(MazeGen.class);
        box[][] maze = mazeGen.generateMaze();

//        ctx.close();
    }

}
