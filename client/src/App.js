import { Container, Row, Col } from 'react-bootstrap'
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom'

import './App.scss'

import Header from './components/layout/Header'

const App = () => {
  return (
    <Router>
      <Header/>
      <Container>
        <Row className='mt-4'>
          <Col>
            <Switch>
              <Route exact path='/'>Главная</Route>
              <Route exact path='/sign_in'>Войти</Route>
              <Route exact path='/sign_up'>Зарегистрироваться</Route>
              <Route exact path='/games'>Список игр</Route>
              <Route exact path='/games/:id'>Игра</Route>
              <Route exact path='/settings'>Настройки</Route>
              <Route path='*'>404</Route>
            </Switch>
          </Col>
        </Row>
      </Container>
    </Router>
  )
}

export default App
