import axios from 'axios'

import { authHeader, BASE_API_URL } from './requestsConstants'

export const doGetSettings = () => {
  return axios.get(`${BASE_API_URL}/settings`, { headers: authHeader() })
}

export const doUpdateSettings = settings => {
  return axios.put(`${BASE_API_URL}/settings`, settings, { headers: authHeader() })
}
