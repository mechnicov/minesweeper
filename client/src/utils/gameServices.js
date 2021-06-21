import axios from 'axios'

import authHeader from './authHeader'

export const getAllGames = () => {
  return axios.get('/api/v1/games', { headers: authHeader() })
}
