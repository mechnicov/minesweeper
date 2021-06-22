import axios from 'axios'

export const doLogin = (email, password) => {
  return axios.post('/api/v1/auth', { email: email, password: password }).
  then(response => {
    if (response.data.token) {
      localStorage.setItem('minesweeper', JSON.stringify(response.data))
    }

    return response.data
  })
}

export const doLogout = () => {
  localStorage.removeItem('minesweeper')
}

export const doRegister = (email, password) => {
  return axios.post('/api/v1/users', { email: email, password: password }).
    then(response => {
      if (response.data.token) {
        localStorage.setItem('minesweeper', JSON.stringify(response.data))
      }
    })
}
