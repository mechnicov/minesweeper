import React, { useEffect, Fragment } from 'react'
import { useParams } from 'react-router-dom'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'

import Cell from './Cell'
import { getOneGame } from '../../actions/gameActions'

const Game = ({ getOneGame, game }) => {
  const gameId = useParams().id

  useEffect(() => {
    if (game === null) {
      getOneGame(gameId)
    }
  }, [game])

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
          <div className='mt-4'>Status: {game.status}</div>
        </Fragment>
      }
    </Fragment>
  )
}

Game.propTypes = {
  getOneGame: PropTypes.func.isRequired,
  game: PropTypes.object,
}

const mapStateToProps = state => ({
  game: state.gameReducer.game,
})

export default connect(mapStateToProps, { getOneGame })(Game)
