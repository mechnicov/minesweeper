import React, { useEffect, Fragment } from 'react'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'

import GamesList from './GamesList'
import Preloader from '../layout/Preloader'
import { loadUser } from '../../actions/authActions'

const Games = ({ loadUser, auth: { user, loading }}) => {
  useEffect(() => {
    loadUser()

    // eslint-disable-next-line
  }, [])

  if (loading || !user) {
    return <Preloader/>
  }

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
  auth: PropTypes.object.isRequired,
}

const mapStateToProps = state => ({
  auth: state.auth,
})

export default connect(mapStateToProps, { loadUser })(Games)
