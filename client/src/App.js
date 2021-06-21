import { Container, Row, Col } from 'react-bootstrap'
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom'
import { Provider } from 'react-redux'

import './App.scss'

import Header from './components/layout/Header'
import Alerts from './components/layout/Alerts'
import Login from './components/auth/Login'
import Register from './components/auth/Register'
import store from './store'

const App = () => {
  return (
    <Provider store={store}>
      <Router>
        <Header/>
        <Container>
          <Alerts/>
          <Row className='mt-4'>
            <Col>
              <Switch>
                <Route exact path='/'>Главная</Route>
                <Route exact path='/sign_in' component={Login}/>
                <Route exact path='/sign_up' component={Register}/>
                <Route exact path='/games'>Список игр</Route>
                <Route exact path='/games/:id'>Игра</Route>
                <Route exact path='/settings'>Настройки</Route>
                <Route path='*'>404</Route>
              </Switch>
            </Col>
          </Row>
        </Container>
      </Router>
    </Provider>
  )
}

export default App
