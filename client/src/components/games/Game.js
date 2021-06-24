import React, { useEffect, Fragment } from 'react'
import { useParams } from 'react-router-dom'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'

import Cell from './Cell'
import Preloader from '../layout/Preloader'
import { getOneGame } from '../../actions/gameActions'

const Game = ({ getOneGame, games: { game, loading }}) => {
  const gameId = useParams().id

  useEffect(() => {
    if (game === null) {
      getOneGame(gameId)
    }

    // eslint-disable-next-line
  }, [game])

  if (loading || !game) {
    return <Preloader/>
  }

  return (
    <Fragment>
      {game &&
        <Fragment>
          <div
            className='game-map'
            style={
              {
                gridTemplateColumns: `repeat(${game.width}, 30px)`,
                gridTemplateRows: `repeat(${game.height}, 30px)`
              }
            }
          >
            {game.cells.map(cell =>
              <Cell key={cell.id} cell={cell} gameStatus={game.status}/>
            )}
          </div>
          {game.status === 'in_progress' && <div className='mt-4'>Left: {game.bombsCount - game.cells.filter(cell => cell.status === 'marked').length}</div>}
          <div className='mt-4'>Status: {game.status}</div>
        </Fragment>
      }
    </Fragment>
  )
}

Game.propTypes = {
  getOneGame: PropTypes.func.isRequired,
  games: PropTypes.object.isRequired,
}

const mapStateToProps = state => ({
  games: state.games,
})

export default connect(mapStateToProps, { getOneGame })(Game)
