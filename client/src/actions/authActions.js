import {
  REGISTER_SUCCESS,
  REGISTER_FAIL,
  LOGIN_SUCCESS,
  LOGIN_FAIL,
  LOGOUT,
  USER_LOAD,
  USER_NOT_LOAD,
} from './types'

import history from '../history'

import { doRegister, doLogin, doLogout, doLoadUser } from '../utils/authServices'
import { setAlert } from './alertActions'

export const register = (email, password) => dispatch => {
  return doRegister(email, password).then(response => {
    dispatch({
      type: REGISTER_SUCCESS,
    })

    dispatch(setAlert('Welcome!'))

    return Promise.resolve()
  }, error => {
    const msg = (error.response && error.response.data && error.response.data.message) ||
                error.message ||
                error.toString()

    dispatch({
      type: REGISTER_FAIL,
    })

    dispatch(setAlert(msg))

    return Promise.resolve()
  })
}

export const login = (email, password) => dispatch => {
  return doLogin(email, password).then(data => {
    dispatch({
      type: LOGIN_SUCCESS,
      payload: { token: data },
    })

    dispatch(setAlert('Welcome!'))

    return Promise.resolve()
  }, error => {
    const msg = (error.response && error.response.data && error.response.data.message) ||
                error.message ||
                error.toString()

    dispatch({
      type: LOGIN_FAIL,
    })

    dispatch(setAlert(msg))

    return Promise.resolve()
  })
}

export const logout = () => dispatch => {
  doLogout()

  dispatch({
    type: LOGOUT,
  })

  dispatch(setAlert('See you soon'))

  history.push('/')
}

export const loadUser = () => dispatch => {
  return doLoadUser().then(data => {
    dispatch({
      type: USER_LOAD,
      payload: data.data,
    })

    return Promise.resolve()
  }, error => {
    const msg = (error.response && error.response.data && error.response.data.message) ||
                error.message ||
                error.toString()

    dispatch({
      type: USER_NOT_LOAD,
    })

    dispatch(setAlert(msg))

    history.push('/')

    return Promise.resolve()
  })
}
