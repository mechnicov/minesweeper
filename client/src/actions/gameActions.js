import {
  GET_GAME,
  OPEN_CELL,
  MARK_CELL,
} from './types'

import { doGetOneGame, doMarkCell, doOpenCell } from '../utils/gameServices'
import { setAlert } from './alertActions'

export const getOneGame = gameId => dispatch => {
  return doGetOneGame(gameId).
    then(response => {
      dispatch({
        type: GET_GAME,
        payload: response.data
      })
      return Promise.resolve()
    },
    error => {
      const msg = (error.response && error.response.data && error.response.data.message) ||
                  error.message ||
                  error.toString()

      dispatch(setAlert(msg))

      return Promise.resolve()
    }
  )
}

export const markCell = (gameId, x, y) => dispatch => {
  return doMarkCell(gameId, x, y).
    then(response => {
      dispatch({
        type: MARK_CELL,
        payload: response.data
      })

      return Promise.resolve()
    },
    error => {
      const msg = (error.response && error.response.data && error.response.data.message) ||
                  error.message ||
                  error.toString()

      dispatch(setAlert(msg))

      return Promise.resolve()
    }
  )
}
