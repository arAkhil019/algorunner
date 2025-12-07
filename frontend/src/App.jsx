import { useEffect, useState } from 'react'
import { useGameLogic } from './useGameLogic'
import { AuroraBackground } from './components/AuroraBackground'
import { startAmbientMusic, stopAmbientMusic } from './utils/audio'
import './App.css'

function App() {
  const { 
    maze, 
    playerPos, 
    coins, 
    score, 
    timeLeft, 
    gameState, 
    error, 
    fetchMaze, 
    restartGame 
  } = useGameLogic();

  const [cellSize, setCellSize] = useState(40);
  const [gameStarted, setGameStarted] = useState(false);
  const [difficulty, setDifficulty] = useState('easy');
  const [highScore, setHighScore] = useState(() => {
    return parseInt(localStorage.getItem('algoRunnerHighScore') || '0');
  });

  const difficulties = {
    easy: { coinCount: 8, timeLimit: 30, label: 'Easy' },
    medium: { coinCount: 5, timeLimit: 25, label: 'Medium' },
    hard: { coinCount: 5, timeLimit: 19, label: 'Hard' }
  };

  // Start ambient music on page load and keep it playing
  useEffect(() => {
    startAmbientMusic();
  }, []);

  // Handle High Score
  useEffect(() => {
    if (gameState === 'won' || gameState === 'lost') {
      if (score > highScore) {
        setHighScore(score);
        localStorage.setItem('algoRunnerHighScore', score.toString());
      }
    }
  }, [gameState, score, highScore]);

  // Calculate responsive cell size
  useEffect(() => {
    const updateSize = () => {
      if (maze.length === 0) return;
      const rows = maze.length;
      const cols = maze[0].length;
      // Use 60% of width (maze on right) and 90% of height
      const maxWidth = window.innerWidth * 0.60;
      const maxHeight = window.innerHeight * 0.90; 
      const size = Math.floor(Math.min(maxWidth / cols, maxHeight / rows));
      setCellSize(Math.max(20, size)); // Minimum 20px
    };

    updateSize();
    window.addEventListener('resize', updateSize);
    return () => window.removeEventListener('resize', updateSize);
  }, [maze]);

  const handleStartGame = () => {
    setGameStarted(true);
    fetchMaze(difficulties[difficulty]);
  };

  const handleBackToMenu = () => {
    setGameStarted(false);
  };

  return (
    <AuroraBackground>
      {!gameStarted ? (
        <div className="landing-container">
          <h1 className="landing-title">Algo Maze Runner</h1>
          <p className="landing-subtitle">Collect all coins before time runs out.</p>
          <div className="high-score-display">BEST SCORE: {highScore}</div>
          
          <div className="difficulty-selector">
            {Object.keys(difficulties).map((level) => (
              <button 
                key={level}
                className={`difficulty-btn ${level} ${difficulty === level ? 'active' : ''}`}
                onClick={() => setDifficulty(level)}
              >
                {difficulties[level].label}
              </button>
            ))}
          </div>

          <div className="difficulty-info">
            <p>COINS: <span className="highlight">{difficulties[difficulty].coinCount}</span></p>
            <p>TIME: <span className="highlight">{difficulties[difficulty].timeLimit}s</span></p>
          </div>

          <button className="start-button" onClick={handleStartGame}>ENTER THE MAZE</button>
        </div>
      ) : (
        <div className="app-container game-layout">
          <div className="info-panel">
            <h1 className="game-title-side">MAZE<br/>RUNNER</h1>
            
            <div className="timer-container">
              <div className={`timer-huge ${timeLeft <= 10 ? 'urgent' : ''}`}>{timeLeft}</div>
              <div className="timer-label">SECONDS</div>
            </div>

            <div className="score-container">
              <div className="score-label">SCORE</div>
              <div className="score-huge">{score}</div>
            </div>

            <div className="controls-hint-side">
              <p>Use <strong>Arrow Keys</strong> or <strong>WASD</strong> to move</p>
            </div>
          </div>

          <div className="game-board">
            {error && (
              <div className="error-message">
                <p>Error: {error}</p>
                <button onClick={fetchMaze}>Retry</button>
              </div>
            )}

            {maze.length > 0 ? (
              <div 
                className="maze-grid" 
                style={{
                  gridTemplateColumns: `repeat(${maze[0].length}, ${cellSize}px)`
                }}
              >
                {maze.map((row, rowIndex) => (
                  row.map((cell, colIndex) => (
                    <div 
                      key={`${rowIndex}-${colIndex}`} 
                      className="maze-cell"
                      style={{
                        width: `${cellSize}px`,
                        height: `${cellSize}px`,
                        borderTop: cell.walls.top ? '2px solid #00ffff' : '2px solid transparent',
                        borderRight: cell.walls.right ? '2px solid #00ffff' : '2px solid transparent',
                        borderBottom: cell.walls.bottom ? '2px solid #00ffff' : '2px solid transparent',
                        borderLeft: cell.walls.left ? '2px solid #00ffff' : '2px solid transparent',
                      }}
                    >
                      {/* Render Player */}
                      {playerPos.row === rowIndex && playerPos.col === colIndex && (
                        <div className="player">
                          <img src="/mario.svg" alt="player" style={{width: '100%', height: '100%'}} />
                        </div>
                      )}
                      
                      {/* Render Coins */}
                      {coins.some(c => c.row === rowIndex && c.col === colIndex) && (
                        <div className="coin" />
                      )}
                    </div>
                  ))
                ))}
              </div>
            ) : (
              <div style={{color: '#fff'}}>Loading Maze...</div>
            )}

            {/* Game Over / Win Overlay */}
            {(gameState === 'won' || gameState === 'lost') && (
              <div className="overlay">
                <div className="overlay-content">
                  <h2>{gameState === 'won' ? 'Mission Complete' : 'Game Over'}</h2>
                  <p>{gameState === 'won' ? `All coins collected in ${difficulties[difficulty].timeLimit - timeLeft}s` : 'Time Critical Failure'}</p>
                  <p>Final Score: {score}</p>
                  <p style={{fontSize: '0.7rem', color: '#00ffff', marginTop: '-20px', marginBottom: '30px'}}>
                    {score >= highScore && score > 0 ? 'NEW HIGH SCORE!' : `Best: ${highScore}`}
                  </p>
                  <div style={{display: 'flex', gap: '10px', justifyContent: 'center'}}>
                    <button className="restart-btn" onClick={restartGame}>Play Again</button>
                    <button className="restart-btn" style={{background: 'transparent', border: '1px solid #00ffff', color: '#00ffff'}} onClick={handleBackToMenu}>Menu</button>
                  </div>
                </div>
              </div>
            )}
          </div>
        </div>
      )}
    </AuroraBackground>
  )
}

export default App
