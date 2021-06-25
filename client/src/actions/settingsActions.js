import {
  GET_SETTINGS,
  UPDATE_SETTINGS,
  SET_LOADING,
} from './types'

import { doGetSettings, doUpdateSettings } from '../utils/settingsServices'
import { setAlert } from './alertActions'

export const getSettings = () => dispatch => {
  dispatch({
    type: SET_LOADING,
  })

  return doGetSettings().then(response => {
    dispatch({
      type: GET_SETTINGS,
      payload: response.data
    })
    return Promise.resolve()
  }, error => {
    const msg = (error.response && error.response.data && error.response.data.message) ||
                error.message ||
                error.toString()

    dispatch(setAlert(msg))

    return Promise.resolve()
  })
}

export const updateSettings = settings => dispatch => {
  return doUpdateSettings(settings).then(response => {
    dispatch({
      type: UPDATE_SETTINGS,
      payload: response.data
    })

    dispatch(setAlert('Settings updated success'))

    return Promise.resolve()
  }, error => {
    const msg = (error.response && error.response.data && error.response.data.message) ||
                error.message ||
                error.toString()

    dispatch(setAlert(msg))

    return Promise.resolve()
  })
}
