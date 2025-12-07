import { useState, useEffect } from 'react'
import './App.css'

function App() {
  const [maze, setMaze] = useState([])
  const [error, setError] = useState(null)

  const fetchMaze = async () => {
    try {
      const response = await fetch('/maze');
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const data = await response.json();
      setMaze(data);
      setError(null);
    } catch (err) {
      console.error("Failed to fetch maze:", err);
      setError(err.message);
    }
  }

  useEffect(() => {
    fetchMaze();
  }, [])

  return (
    <div className="app-container">
      <h1>Maze Viewer</h1>
      
      {error && (
        <div className="error-message">
          <p>Error connecting to backend: {error}</p>
          <p>Ensure backend is running on http://localhost:8080/maze</p>
        </div>
      )}

      {maze.length > 0 ? (
        <div 
          className="maze-grid" 
          style={{
            gridTemplateColumns: `repeat(${maze[0].length}, 50px)`
          }}
        >
          {maze.map((row, rowIndex) => (
            row.map((cell, colIndex) => (
              <div 
                key={`${rowIndex}-${colIndex}`} 
                className="maze-cell"
                style={{
                  // cell structure: object with walls property
                  // true = wall (closed), false = open
                  borderTop: cell.walls.top ? '5px solid #009900' : '5px solid transparent',
                  borderRight: cell.walls.right ? '5px solid #009900' : '5px solid transparent',
                  borderBottom: cell.walls.bottom ? '5px solid #009900' : '5px solid transparent',
                  borderLeft: cell.walls.left ? '5px solid #009900' : '5px solid transparent',
                }}
              />
            ))
          ))}
        </div>
      ) : (
        !error && <p>Loading maze data...</p>
      )}
      
      <div className="controls">
        <button onClick={fetchMaze}>Refresh Maze</button>
      </div>
    </div>
  )
}

export default App
