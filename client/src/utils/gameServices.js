import axios from 'axios'

import { authHeader } from './authHeader'

export const doGetAllGames = () => {
  return axios.get('/api/v1/games', { headers: authHeader() })
}

export const doGetOneGame = gameId => {
  return axios.get(`/api/v1/games/${gameId}`, { headers: authHeader() })
}

export const doMarkCell = (gameId, x, y) => {
  return axios.post(`/api/v1/games/${gameId}/mark`, { x: x, y: y } , { headers: authHeader() })
}

export const doOpenCell = (gameId, x, y) => {
  return axios.post(`/api/v1/games/${gameId}/open`, { x: x, y: y } , { headers: authHeader() })
}
