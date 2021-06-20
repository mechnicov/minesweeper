import React from 'react'
import { Navbar, Nav } from 'react-bootstrap'

const Header = () => {
  return (
    <Navbar style={{ backgroundColor: '#c5b3e6' }} expand='lg'>
      <Navbar.Collapse id='basic-navbar-nav'>
        <Nav className='me-auto'>
          <Nav.Link href='/sign_in'>Войти</Nav.Link>
          <Nav.Link href='/sign_up'>Зарегистрироваться</Nav.Link>
        </Nav>

        <Nav>
          <Nav.Link href='#'>Настройки</Nav.Link>
          <Nav.Link href='#'>Выход</Nav.Link>
        </Nav>
      </Navbar.Collapse>
    </Navbar>
  )
}

export default Header
