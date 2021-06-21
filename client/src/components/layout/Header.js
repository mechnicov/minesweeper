import React, { Fragment } from 'react'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'
import { Navbar, Nav } from 'react-bootstrap'

import { logout } from '../../actions/authActions'

const Header = ({ logout, isLoggedIn }) => {
  const leftGuestLinks = (
    <Fragment>
      <Nav.Link href='/sign_in'>Войти</Nav.Link>
      <Nav.Link href='/sign_up'>Зарегистрироваться</Nav.Link>
    </Fragment>
  )

  const leftUserLinks = (
    <Fragment>
      <Nav.Link href='#'>Новая игра</Nav.Link>
      <Nav.Link href='/games'>Список игр</Nav.Link>
    </Fragment>
  )

  const logoutLink = (
    <Nav>
      <Nav.Link href='#!' onClick={logout}>Выход</Nav.Link>
    </Nav>
  )

  return (
    <Navbar style={{ backgroundColor: '#c5b3e6' }} expand='lg'>
      <Navbar.Collapse id='basic-navbar-nav'>
        <Nav className='me-auto'>
          {isLoggedIn ? leftUserLinks : leftGuestLinks}
        </Nav>

        {isLoggedIn && logoutLink}
      </Navbar.Collapse>
    </Navbar>
  )
}

Header.propTypes = {
  logout: PropTypes.func.isRequired,
  isLoggedIn: PropTypes.bool,
}

const mapStateToProps = state => ({
  isLoggedIn: state.authReducer.isLoggedIn,
})

export default connect(mapStateToProps, { logout })(Header)
