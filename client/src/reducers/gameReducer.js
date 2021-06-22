import {
  GET_GAME,
  OPEN_CELL,
  MARK_CELL,
} from '../actions/types'

const initialState = { game: null }

export default (state = initialState, action) => {
  const { type, payload } = action

  switch (type) {
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
