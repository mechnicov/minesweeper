import React, { Fragment } from 'react'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'
import { Navbar, Nav } from 'react-bootstrap'

import history from '../../history'
import { logout } from '../../actions/authActions'
import { startGame } from '../../actions/gameActions'

const Header = ({ logout, startGame, isLoggedIn, user }) => {
  const logOutAndRedirect = () => {
    logout()
    history.push('/')
  }

  const leftGuestLinks = (
    <Fragment>
      <Nav.Link href='/sign_in'>Sign in</Nav.Link>
      <Nav.Link href='/sign_up'>Sign up</Nav.Link>
    </Fragment>
  )

  const leftUserLinks = (
    <Fragment>
      <Nav.Link href='#!' onClick={startGame}>New game</Nav.Link>
      <Nav.Link href='/games'>My games</Nav.Link>
    </Fragment>
  )

  const rightUserLinks = (
    <Fragment>
      <Nav>
        <Nav.Link href='#!'>{user && user.email}</Nav.Link>
        {user && user.isAdmin && <Nav.Link href='#!'>Settings</Nav.Link>}
        <Nav.Link href='#!' onClick={logOutAndRedirect}>Log out</Nav.Link>
      </Nav>
    </Fragment>
  )

  return (
    <Navbar style={{ backgroundColor: '#c5b3e6' }} expand='lg'>
      <Navbar.Collapse id='basic-navbar-nav'>
        <Nav className='me-auto'>
          {isLoggedIn ? leftUserLinks : leftGuestLinks}
        </Nav>

        {isLoggedIn && rightUserLinks}
      </Navbar.Collapse>
    </Navbar>
  )
}

Header.propTypes = {
  logout: PropTypes.func.isRequired,
  startGame: PropTypes.func.isRequired,
  user: PropTypes.object,
  isLoggedIn: PropTypes.bool.isRequired,
}

const mapStateToProps = state => ({
  isLoggedIn: state.authReducer.isLoggedIn,
  user: state.authReducer.user,
})

export default connect(mapStateToProps, { logout, startGame })(Header)
