import React, { useState, useEffect } from 'react'
import { useHistory } from 'react-router'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'
import { Form, Button } from 'react-bootstrap'

import { register } from '../../actions/authActions'

const Register = ({ register, isLoggedIn }) => {
  let history = useHistory()

  useEffect(() => {
    if (isLoggedIn) {
      history.push('/')
    }

    // eslint-disable-next-line
  }, [isLoggedIn])

  const [user, setUser] = useState({
    email: '',
    password: '',
  })

  const { email, password } = user

  const onChange = e => setUser({ ...user, [e.target.name]: e.target.value })

  const onSubmit = e => {
    e.preventDefault()
    register(email, password)
  }

  return (
    <Form onSubmit={onSubmit}>
      <Form.Group className='mb-3' controlId='email'>
        <Form.Label>Email address</Form.Label>
        <Form.Control
          name='email'
          type='email'
          placeholder='Enter email'
          required
          onChange={onChange}
        />
      </Form.Group>

      <Form.Group className='mb-3' controlId='password'>
        <Form.Label>Password</Form.Label>
        <Form.Control
          name='password'
          type='password'
          placeholder='Password'
          required
          onChange={onChange}
        />
        <Form.Text className='text-muted'>
          Minimum 6 characters
        </Form.Text>
      </Form.Group>
      <Button variant='primary' type='submit'>
        Submit
      </Button>
    </Form>
  )
}

Register.propTypes = {
  register: PropTypes.func.isRequired,
  isLoggedIn: PropTypes.bool,
}

const mapStateToProps = state => ({
  isLoggedIn: state.authReducer.isLoggedIn,
})

export default connect(mapStateToProps, { register })(Register)
