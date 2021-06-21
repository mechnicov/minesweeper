import {
  REGISTER_SUCCESS,
  REGISTER_FAIL,
  LOGIN_SUCCESS,
  LOGIN_FAIL,
  LOGOUT,
} from './types'

import { doRegister, doLogin, doLogout } from '../utils/authServices'

export const register = (email, password) => dispatch => {
  return doRegister(email, password).
    then(response => {
      dispatch({
        type: REGISTER_SUCCESS,
      })

      return Promise.resolve()
    },
    error => {
      const message = (error.response && error.response.data && error.response.data.message) ||
                      error.message ||
                      error.toString()

      alert(message)

      dispatch({
        type: REGISTER_FAIL,
      })
    }
  )
}

export const login = (username, password) => dispatch => {
  return doLogin(username, password).
    then(data => {
      dispatch({
        type: LOGIN_SUCCESS,
        payload: { user: data },
      })

      return Promise.resolve()
    },
    error => {
      const message = (error.response && error.response.data && error.response.data.message) ||
                      error.message ||
                      error.toString()

      alert(message)

      dispatch({
        type: LOGIN_FAIL,
      })
    }
  )
}

export const logout = () => dispatch => {
  doLogout()

  dispatch({
    type: LOGOUT,
  })
}
