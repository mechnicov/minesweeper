export const authHeader = () => {
  const minesweeper = JSON.parse(localStorage.getItem('minesweeper'))

  if (minesweeper && minesweeper.token) {
    return { 'Authorization': `Bearer ${minesweeper.token}` }
  } else {
    return {}
  }
}
