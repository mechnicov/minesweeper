import {
  START_GAME,
  GET_GAME,
  OPEN_CELL,
  MARK_CELL,
  SET_LOADING,
} from './types'

import history from '../history'

import { doStartGame, doGetOneGame, doMarkCell, doOpenCell } from '../utils/gameServices'
import { setAlert } from './alertActions'

export const startGame = () => dispatch => {
  dispatch({
    type: SET_LOADING,
  })

  return doStartGame().then(response => {
    const game = response.data

    dispatch({
      type: START_GAME,
      payload: game
    })

    history.push(`/games/${game.id}`)

    return Promise.resolve()
  }, error => {
    const msg = (error.response && error.response.data && error.response.data.message) ||
                error.message ||
                error.toString()

    dispatch(setAlert(msg))

    history.push('/')

    return Promise.resolve()
  })
}

export const getOneGame = gameId => dispatch => {
  dispatch({
    type: SET_LOADING,
  })

  return doGetOneGame(gameId).then(response => {
    dispatch({
      type: GET_GAME,
      payload: response.data
    })
    return Promise.resolve()
  }, error => {
    const msg = (error.response && error.response.data && error.response.data.message) ||
                error.message ||
                error.toString()

    dispatch(setAlert(msg))

    history.push('/')

    return Promise.resolve()
  })
}

export const markCell = (gameId, x, y) => dispatch => {
  return doMarkCell(gameId, x, y).then(response => {
    dispatch({
      type: MARK_CELL,
      payload: response.data
    })

    return Promise.resolve()
  }, error => {
    const msg = (error.response && error.response.data && error.response.data.message) ||
                error.message ||
                error.toString()

    dispatch(setAlert(msg))

    history.push('/')

    return Promise.resolve()
  })
}

export const openCell = (gameId, x, y) => dispatch => {
  return doOpenCell(gameId, x, y).then(response => {
    dispatch({
      type: OPEN_CELL,
      payload: response.data
    })

    return Promise.resolve()
  }, error => {
    const msg = (error.response && error.response.data && error.response.data.message) ||
                error.message ||
                error.toString()

    dispatch(setAlert(msg))

    history.push('/')

    return Promise.resolve()
  })
}
