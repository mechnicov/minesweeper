import React, { Fragment } from 'react'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'
import { Navbar, Nav } from 'react-bootstrap'

import { logout } from '../../actions/authActions'
import { startGame } from '../../actions/gameActions'

const Header = ({ logout, startGame, auth: { isLoggedIn, user }}) => {
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
        {user && user.isAdmin && <Nav.Link href='/settings'>Settings</Nav.Link>}
        <Nav.Link href='#!' onClick={logout}>Log out</Nav.Link>
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
  auth: PropTypes.object.isRequired,
}

const mapStateToProps = state => ({
  auth: state.auth,
})

export default connect(mapStateToProps, { logout, startGame })(Header)
