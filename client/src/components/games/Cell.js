import React from 'react'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'

import { markCell, openCell } from '../../actions/gameActions'

import mine from './mine.png'
import flag from './flag.png'
import './styles.scss'

const Cell = ({ markCell, openCell, cell: { id, x, y, status, bombsNear, gameId }, gameStatus }) => {
  const info = () => {
    const klass = `${status === 'empty' ? 'opened' : 'closed'} text-center`

    let content = ''

    if (status === 'marked') {
      content = <img src={flag} alt='!'/>
      if (gameStatus === 'fail') content = <img src={mine} alt='M'/>
    }

    if (status === 'empty' && +bombsNear > 0) content = <span className={`mines-${bombsNear}`}>{bombsNear}</span>

    return { klass: klass, content: content }
  }

  const { klass, content } = info()

  const mark = e => {
    e.preventDefault()

    if (gameStatus !== 'in_progress') return

    markCell(gameId, x, y)
  }

  const open = e => {
    e.preventDefault()

    if (gameStatus !== 'in_progress') return

    openCell(gameId, x, y)
  }

  return (
    <div
      key={id}
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
  openCell: PropTypes.func.isRequired,
}

export default connect(null, { markCell, openCell })(Cell)
