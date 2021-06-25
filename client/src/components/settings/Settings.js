import React, { useState, useEffect } from 'react'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'
import { Form, Button } from 'react-bootstrap'

import Preloader from '../layout/Preloader'
import { loadUser } from '../../actions/authActions'
import { getSettings, updateSettings } from '../../actions/settingsActions'

const Settings = ({ loadUser, getSettings, updateSettings, auth: { user }, settings: { settings }}) => {
  useEffect(() => {
    if (user === null) loadUser()

    if (settings !== null) {
      setSettings(settings)
    } else {
      getSettings()
    }

    // eslint-disable-next-line
  }, [settings])

  const [updatedSettings, setSettings] = useState({
    width: '',
    height: '',
    bombsCount: '',
  })

  const { width, height, bombsCount } = updatedSettings

  if (user === null || !user.isAdmin) return <Preloader/>

  const onSubmit = e => {
    e.preventDefault()
    console.log(user)

    updateSettings(updatedSettings)
  }
// eslint-disable-next-line
  const onChange = e => setSettings({ ...updatedSettings, [e.target.name]: e.target.value })

  return (
    <Form onSubmit={onSubmit}>
      <Form.Group className='mb-3' controlId='width'>
        <Form.Label>Map width</Form.Label>
        <Form.Control
          name='width'
          value={width}
          type='number'
          step='1'
          min='2'
          placeholder='Enter width'
          required
          onChange={onChange}
        />
      </Form.Group>

      <Form.Group className='mb-3' controlId='height'>
        <Form.Label>Map height</Form.Label>
        <Form.Control
          name='height'
          value={height}
          type='number'
          step='1'
          min='2'
          placeholder='Enter height'
          required
          onChange={onChange}
        />
      </Form.Group>

      <Form.Group className='mb-3' controlId='bombsCount'>
        <Form.Label>Bombs count</Form.Label>
        <Form.Control
          name='bombsCount'
          value={bombsCount}
          type='number'
          step='1'
          min='1'
          placeholder='Enter bombs count'
          required
          onChange={onChange}
        />
      </Form.Group>

      <Button variant='primary' type='submit'>
        Submit
      </Button>
    </Form>
  )
}

Settings.propTypes = {
  loadUser: PropTypes.func.isRequired,
  getSettings: PropTypes.func.isRequired,
  updateSettings: PropTypes.func.isRequired,
  auth: PropTypes.object.isRequired,
  settings: PropTypes.object.isRequired,
}

const mapStateToProps = state => ({
  auth: state.auth,
  settings: state.settings,
})

export default connect(mapStateToProps, { loadUser, getSettings, updateSettings })(Settings)
