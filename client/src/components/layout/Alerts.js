import React from 'react'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'
import { Alert } from 'react-bootstrap'

const Alerts = ({ alert: { alerts }}) => {
  return (
    alerts.length > 0 && alerts.map(alert => (
      <Alert key={alert.id} variant='dark' className='mt-3'>
        {alert.msg}
      </Alert>
    ))
  )
}

Alerts.propTypes = {
  alert: PropTypes.object.isRequired,
}

const mapStateToProps = state => ({
  alert: state.alert,
})

export default connect(mapStateToProps, {})(Alerts)
