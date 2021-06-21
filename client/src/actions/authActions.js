import {
  REGISTER_SUCCESS,
  REGISTER_FAIL,
  LOGIN_SUCCESS,
  LOGIN_FAIL,
  LOGOUT,
} from './types'

import { doRegister, doLogin, doLogout } from '../utils/authServices'
import { setAlert } from './alertActions'

export const register = (email, password) => dispatch => {
  return doRegister(email, password).
    then(response => {
      dispatch({
        type: REGISTER_SUCCESS,
      })

      dispatch(setAlert('Welcome!'))

      return Promise.resolve()
    },
    error => {
      const msg = (error.response && error.response.data && error.response.data.message) ||
                  error.message ||
                  error.toString()

      dispatch({
        type: REGISTER_FAIL,
      })

      dispatch(setAlert(msg))

      return Promise.resolve()
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

      dispatch(setAlert('Welcome!'))

      return Promise.resolve()
    },
    error => {
      const msg = (error.response && error.response.data && error.response.data.message) ||
                  error.message ||
                  error.toString()

      dispatch({
        type: LOGIN_FAIL,
      })

      dispatch(setAlert(msg))

      return Promise.resolve()
    }
  )
}

export const logout = () => dispatch => {
  doLogout()

  dispatch({
    type: LOGOUT,
  })

  dispatch(setAlert('See you soon'))
}
