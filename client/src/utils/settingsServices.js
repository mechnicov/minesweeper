import axios from 'axios'

import { authHeader } from './authHeader'

export const doGetSettings = () => {
  return axios.get('/api/v1/settings', { headers: authHeader() })
}

export const doUpdateSettings = settings => {
  return axios.put('/api/v1/settings', settings, { headers: authHeader() })
}
