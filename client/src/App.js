import { Container, Row, Col } from 'react-bootstrap'
import { Router, Route, Switch } from 'react-router-dom'
import { Provider } from 'react-redux'

import './App.scss'

import Header from './components/layout/Header'
import Alerts from './components/layout/Alerts'
import Home from './components/layout/Home'
import NotFound from './components/layout/NotFound'
import Login from './components/auth/Login'
import Register from './components/auth/Register'
import Games from './components/games/Games'
import Game from './components/games/Game'
import history from './history'
import store from './store'

const App = () => {
  return (
    <Provider store={store}>
      <Router history={history}>
        <Header/>
        <Container>
          <Alerts/>
          <Row className='mt-4'>
            <Col>
              <Switch>
                <Route exact path='/' component={Home}/>
                <Route exact path='/sign_in' component={Login}/>
                <Route exact path='/sign_up' component={Register}/>
                <Route exact path='/games' component={Games}/>
                <Route exact path='/games/:id' component={Game}></Route>
                <Route exact path='/settings'>Настройки</Route>
                <Route path='*' component={NotFound}/>
              </Switch>
            </Col>
          </Row>
        </Container>
      </Router>
    </Provider>
  )
}

export default App
