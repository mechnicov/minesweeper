import React, { useEffect, Fragment } from 'react'
import { useHistory } from 'react-router'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'

import GamesList from './GamesList'
import { loadUser } from '../../actions/authActions'

const Games = ({ loadUser, user }) => {
  let history = useHistory()

  useEffect(() => {
    loadUser()

    !user && history.push('/')

    // eslint-disable-next-line
  }, [])

  return (
    <Fragment>
      {user && user.games && user.games.length > 0 ?
        <GamesList games={user.games}/> :
        'Games list is blank'
      }
    </Fragment>
  )
}

Games.propTypes = {
  loadUser: PropTypes.func.isRequired,
  user: PropTypes.object,
}

const mapStateToProps = state => ({
  user: state.authReducer.user,
})

export default connect(mapStateToProps, { loadUser })(Games)
