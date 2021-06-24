import {
  START_GAME,
  GET_GAME,
  OPEN_CELL,
  MARK_CELL,
  SET_LOADING,
} from '../actions/types'

const initialState = { game: null, loading: false }

const gameReducer = (state = initialState, action) => {
  const { type, payload } = action

  switch (type) {
    case START_GAME:
      return {
        ...state,
        game: payload,
        loading: false,
      }
    case GET_GAME:
      return {
        ...state,
        game: payload,
        loading: false,
      }
    case OPEN_CELL:
      return {
        ...state,
        game: payload,
        loading: false,
      }
    case MARK_CELL:
      return {
        ...state,
        game: payload,
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

export default gameReducer
