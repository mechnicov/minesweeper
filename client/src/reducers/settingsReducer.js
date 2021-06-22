import {
  GET_SETTINGS,
  UPDATE_SETTINGS,
} from '../actions/types'

const initialState = { settings: null }

const settingsReducer = (state = initialState, action) => {
  const { type, payload } = action

  switch (type) {
    case GET_SETTINGS:
      return {
        ...state,
        settings: payload,
      }
    case UPDATE_SETTINGS:
      return {
        ...state,
        settings: payload,
      }
    default:
      return state
  }
}

export default settingsReducer
