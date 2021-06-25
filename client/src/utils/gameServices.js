import axios from 'axios'

import { authHeader, BASE_API_URL } from './requestsConstants'

export const doGetAllGames = () => {
  return axios.get(`${BASE_API_URL}/games`, { headers: authHeader() })
}

export const doStartGame = () => {
  return axios.post(`${BASE_API_URL}/games`, {}, { headers: authHeader() })
}

export const doGetOneGame = gameId => {
  return axios.get(`${BASE_API_URL}/games/${gameId}`, { headers: authHeader() })
}

export const doMarkCell = (gameId, x, y) => {
  return axios.post(`${BASE_API_URL}/games/${gameId}/mark`, { x: x, y: y } , { headers: authHeader() })
}

export const doOpenCell = (gameId, x, y) => {
  return axios.post(`${BASE_API_URL}/games/${gameId}/open`, { x: x, y: y } , { headers: authHeader() })
}
