import { combineReducers } from 'redux'
import alertReducer from './alertReducer'
import authReducer from './authReducer'
import gameReducer from './gameReducer'
import settingsReducer from './settingsReducer'

export default combineReducers({
  alert: alertReducer,
  auth: authReducer,
  games: gameReducer,
  settings: settingsReducer,
})
