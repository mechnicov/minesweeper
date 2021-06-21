import React from 'react'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'
import { Alert } from 'react-bootstrap'

const Alerts = ({ alerts }) => {
  return (
    alerts.length > 0 && alerts.map(alert => (
      <Alert key={alert.id} variant='dark' className='mt-3'>
        {alert.msg}
      </Alert>
    ))
  )
}

Alerts.propTypes = {
  alerts: PropTypes.array.isRequired,
}

const mapStateToProps = state => ({
  alerts: state.alertReducer.alerts,
})

export default connect(mapStateToProps, {})(Alerts)
