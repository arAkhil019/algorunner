import { useState, useEffect, useCallback } from 'react';
import { 
  playMoveSound, 
  playCoinSound, 
  playTimerWarningSound, 
  playGameStartSound, 
  playGameOverSound 
} from './utils/audio';

export const useGameLogic = () => {
  const [maze, setMaze] = useState([]);
  const [playerPos, setPlayerPos] = useState({ row: 0, col: 0 });
  const [coins, setCoins] = useState([]);
  const [score, setScore] = useState(0);
  const [timeLeft, setTimeLeft] = useState(30); // 30 seconds time limit
  const [gameState, setGameState] = useState('loading'); // loading, playing, won, lost
  const [error, setError] = useState(null);
  const [gameConfig, setGameConfig] = useState({ coinCount: 5, timeLimit: 30 });

  const fetchMaze = useCallback(async (config) => {
    try {
      setGameState('loading');
      if (config) {
        setGameConfig(config);
      }
      const response = await fetch('/api/maze');
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const data = await response.json();
      setMaze(data);
      resetGame(data, config || gameConfig);
    } catch (err) {
      console.error("Failed to fetch maze:", err);
      setError(err.message);
      setGameState('error');
    }
  }, [gameConfig]);

  const generateCoins = (mazeData, count = 5) => {
    const newCoins = [];
    const rows = mazeData.length;
    const cols = mazeData[0].length;
    
    while (newCoins.length < count) {
      const r = Math.floor(Math.random() * rows);
      const c = Math.floor(Math.random() * cols);
      
      // Don't spawn on player (0,0) or existing coins
      if ((r === 0 && c === 0) || newCoins.some(coin => coin.row === r && coin.col === c)) {
        continue;
      }
      
      newCoins.push({ row: r, col: c, id: `${r}-${c}` });
    }
    return newCoins;
  };

  const resetGame = (mazeData, config) => {
    const currentConfig = config || gameConfig;
    setPlayerPos({ row: 0, col: 0 });
    setScore(0);
    setTimeLeft(currentConfig.timeLimit);
    setCoins(generateCoins(mazeData, currentConfig.coinCount));
    setGameState('playing');
    setError(null);
    playGameStartSound();
  };

  // Timer
  useEffect(() => {
    if (gameState !== 'playing') return;

    const timer = setInterval(() => {
      setTimeLeft((prev) => {
        if (prev <= 11 && prev > 1) {
            playTimerWarningSound();
        }
        if (prev <= 1) {
          setGameState('lost');
          playGameOverSound(false);
          return 0;
        }
        return prev - 1;
      });
    }, 1000);

    return () => clearInterval(timer);
  }, [gameState]);

  // Check Win Condition
  useEffect(() => {
    if (gameState === 'playing' && coins.length === 0) {
      setGameState('won');
      playGameOverSound(true);
    }
  }, [coins, gameState]);

  // Movement Handler
  const handleMove = useCallback((direction) => {
    if (gameState !== 'playing' || maze.length === 0) return;

    setPlayerPos((prev) => {
      const currentCell = maze[prev.row][prev.col];
      let newRow = prev.row;
      let newCol = prev.col;

      if (direction === 'up' && !currentCell.walls.top) newRow--;
      if (direction === 'right' && !currentCell.walls.right) newCol++;
      if (direction === 'down' && !currentCell.walls.bottom) newRow++;
      if (direction === 'left' && !currentCell.walls.left) newCol--;

      // Check bounds (though walls should prevent this)
      if (newRow < 0 || newRow >= maze.length || newCol < 0 || newCol >= maze[0].length) {
        return prev;
      }

      // Check Coin Collection
      setCoins((currentCoins) => {
        const coinIndex = currentCoins.findIndex(c => c.row === newRow && c.col === newCol);
        if (coinIndex !== -1) {
          const newCoins = [...currentCoins];
          newCoins.splice(coinIndex, 1);
          setScore(s => s + 1);
          playCoinSound();
          return newCoins;
        }
        return currentCoins;
      });

      if (newRow !== prev.row || newCol !== prev.col) {
        playMoveSound();
      }

      return { row: newRow, col: newCol };
    });
  }, [maze, gameState]);

  // Keyboard Listeners
  useEffect(() => {
    const handleKeyDown = (e) => {
      if (["ArrowUp", "ArrowDown", "ArrowLeft", "ArrowRight", " "].includes(e.key)) {
        e.preventDefault();
      }

      switch (e.key) {
        case 'ArrowUp':
        case 'w':
          handleMove('up');
          break;
        case 'ArrowRight':
        case 'd':
          handleMove('right');
          break;
        case 'ArrowDown':
        case 's':
          handleMove('down');
          break;
        case 'ArrowLeft':
        case 'a':
          handleMove('left');
          break;
        default:
          break;
      }
    };

    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [handleMove]);

  return {
    maze,
    playerPos,
    coins,
    score,
    timeLeft,
    gameState,
    error,
    fetchMaze,
    restartGame: () => resetGame(maze)
  };
};
