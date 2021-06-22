import React from 'react'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'

import { markCell } from '../../actions/gameActions'

import mine from './mine.png'
import flag from './flag.png'
import './styles.scss'

const Cell = ({ markCell, cell, gameStatus }) => {
  const info = () => {
    const klass = `${cell.status === 'empty' ? 'opened' : 'closed'} text-center`

    let content = ''

    if (cell.status === 'marked') {
      content = <img src={flag} alt='!'/>
      if (gameStatus === 'fail') content = <img src={mine} alt='M'/>
    }

    if (cell.status === 'empty' && +cell.bombsNear > 0) content = <span className={`mines-${cell.bombsNear}`}>{cell.bombsNear}</span>

    return { klass: klass, content: content }
  }

  const { klass, content } = info()

  const mark = e => {
    e.preventDefault()

    const { gameId, x, y } = cell

    markCell(gameId, x, y)
  }

  const open = e => {
    e.preventDefault()

    const { x, y } = e.target.dataset

    console.log(x, y, 'open')
  }

  return (
    <div
      key={cell.id}
      className={klass}
      onClick={open}
      onContextMenu={mark}
    >
      {content}
    </div>
  )
}

Cell.propTypes = {
  markCell: PropTypes.func.isRequired,
}

export default connect(null, { markCell })(Cell)
