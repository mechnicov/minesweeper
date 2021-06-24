import {
  REGISTER_SUCCESS,
  REGISTER_FAIL,
  LOGIN_SUCCESS,
  LOGIN_FAIL,
  LOGOUT,
  USER_LOAD,
  USER_NOT_LOAD,
  SET_LOADING,
} from '../actions/types'

const token = JSON.parse(localStorage.getItem('minesweeper'))

const initialState =
  token ?
  { isLoggedIn: true, token, user: null, loading: false } :
  { isLoggedIn: false, token: null, user: null, loading: false }

const authReducer = (state = initialState, action) => {
  const { type, payload } = action

  switch (type) {
    case REGISTER_SUCCESS:
      return {
        ...state,
        isLoggedIn: true,
        loading: false,
      }
    case REGISTER_FAIL:
      return {
        ...state,
        isLoggedIn: false,
        loading: false,
      }
    case LOGIN_SUCCESS:
      return {
        ...state,
        isLoggedIn: true,
        token: payload.token,
        loading: false,
      }
    case LOGIN_FAIL:
      return {
        ...state,
        isLoggedIn: false,
        token: null,
        loading: false,
      }
    case LOGOUT:
      return {
        ...state,
        isLoggedIn: false,
        token: null,
        loading: false,
      }
    case USER_LOAD:
      return {
        ...state,
        isLoggedIn: true,
        user: payload,
        loading: false,
      }
    case USER_NOT_LOAD:
      return {
        ...state,
        isLoggedIn: false,
        user: null,
        token: null,
        loading: false,
      }
    case SET_LOADING:
      return {
        ...state,
        loading: true,
      }
    default:
      return state
  }
}

export default authReducer
