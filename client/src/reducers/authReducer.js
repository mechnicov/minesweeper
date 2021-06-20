const user = JSON.parse(localStorage.getItem('minesweeper'))

const initialState = user ? { isLoggedIn: true, user } : { isLoggedIn: false, user: null }

export default (state = initialState, action) => {
  const { type, payload } = action

  switch (type) {
    default:
      return state
  }
}
