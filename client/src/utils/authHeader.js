export const authHeader = () => {
  let headers = { 'Content-Type': 'application/json' }

  const minesweeper = JSON.parse(localStorage.getItem('minesweeper'))

  if (minesweeper && minesweeper.token) {
    headers = { ...headers, 'Authorization': `Bearer ${minesweeper.token}` }
  }

  return headers
}
