import {
  REGISTER_SUCCESS,
  REGISTER_FAIL,
  LOGIN_SUCCESS,
  LOGIN_FAIL,
  LOGOUT,
  USER_LOAD,
  USER_NOT_LOAD,
} from '../actions/types'

const token = JSON.parse(localStorage.getItem('minesweeper'))

const initialState =
  token ? { isLoggedIn: true, token, user: {} } : { isLoggedIn: false, token: null, user: null }

export default (state = initialState, action) => {
  const { type, payload } = action

  switch (type) {
    case REGISTER_SUCCESS:
      return {
        ...state,
        isLoggedIn: true,
      }
    case REGISTER_FAIL:
      return {
        ...state,
        isLoggedIn: false,
      }
    case LOGIN_SUCCESS:
      return {
        ...state,
        isLoggedIn: true,
        token: payload.token,
      }
    case LOGIN_FAIL:
      return {
        ...state,
        isLoggedIn: false,
        token: null,
      }
    case LOGOUT:
      return {
        ...state,
        isLoggedIn: false,
        token: null,
      }
    case USER_LOAD:
      return {
        ...state,
        isLoggedIn: true,
        user: payload,
      }
    case USER_NOT_LOAD:
      return {
        ...state,
        isLoggedIn: false,
        user: null,
        token: null,
      }
    default:
      return state
  }
}
