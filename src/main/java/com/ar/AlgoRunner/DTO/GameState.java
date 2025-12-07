package com.ar.AlgoRunner.DTO;

import com.ar.AlgoRunner.Models.Point;

// 2. Outgoing Response: The current state of the board
public record GameState(Point player, Point bot, boolean gameOver) {}
