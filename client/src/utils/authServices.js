import axios from 'axios'

import { authHeader, BASE_API_URL } from './requestsConstants'

export const doLogin = (email, password) => {
  return axios.post(`${BASE_API_URL}/auth`, { email: email, password: password }).then(response => {
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
  return axios.post(`${BASE_API_URL}/users`, { email: email, password: password }).then(response => {
    if (response.data.token) {
      localStorage.setItem('minesweeper', JSON.stringify(response.data))
    }

    return response.data
  })
}

export const doLoadUser = () => {
  return axios.get(`${BASE_API_URL}/auth`, { headers: authHeader() })
}
