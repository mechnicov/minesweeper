export const authHeader = () => {
  let headers = {
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*'
  }

  const minesweeper = JSON.parse(localStorage.getItem('minesweeper'))

  if (minesweeper && minesweeper.token) {
    headers = { ...headers, 'Authorization': `Bearer ${minesweeper.token}` }
  }

  return headers
}

export const BASE_API_URL = process.env.REACT_APP_BASE_API_URL
