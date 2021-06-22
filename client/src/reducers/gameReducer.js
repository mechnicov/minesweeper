import {
  START_GAME,
  GET_GAME,
  OPEN_CELL,
  MARK_CELL,
} from '../actions/types'

const initialState = { game: null }

const gameReducer = (state = initialState, action) => {
  const { type, payload } = action

  switch (type) {
    case START_GAME:
      return {
        ...state,
        game: payload,
      }
    case GET_GAME:
      return {
        ...state,
        game: payload,
      }
    case OPEN_CELL:
      return {
        ...state,
        game: payload,
      }
    case MARK_CELL:
      return {
        ...state,
        game: payload,
      }
    default:
      return state
  }
}

export default gameReducer
