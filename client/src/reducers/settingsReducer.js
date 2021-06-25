import {
  GET_SETTINGS,
  UPDATE_SETTINGS,
  SET_LOADING,
} from '../actions/types'

const initialState = { settings: null, loading: false }

const settingsReducer = (state = initialState, action) => {
  const { type, payload } = action

  switch (type) {
    case GET_SETTINGS:
      return {
        ...state,
        settings: payload,
        loading: false,
      }
    case UPDATE_SETTINGS:
      return {
        ...state,
        settings: payload,
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

export default settingsReducer
