import { Fragment } from 'react'
import { Container, Row, Col } from 'react-bootstrap'

import './App.scss'

import Header from './components/layout/Header'

const App = () => {
  return (
    <Fragment>
      <Header/>
      <Container>
        <Row className='mt-4'>
          <Col>
            Minesweeper
          </Col>
        </Row>
      </Container>
    </Fragment>
  )
}

export default App
