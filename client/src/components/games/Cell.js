import React from 'react'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'

import { markCell, openCell } from '../../actions/gameActions'

import mine from './mine.png'
import flag from './flag.png'
import './styles.scss'

const Cell = ({ markCell, openCell, cell: { id, x, y, status, bombsNear, gameId }, gameStatus }) => {
  const info = () => {
    let klass

    switch (status) {
      case 'empty':
        klass = 'opened'
        break
      case 'exploded':
        klass = 'exploded'
        break
      default:
        klass = 'closed'
        break
    }

    klass = `${klass} text-center`

    let content = ''

    switch (status) {
      case 'marked':
        content = <img src={flag} alt='!'/>
        break
      case 'question':
        content = <span>?</span>
        break
      case 'exploded':
      case 'bomb':
        content = <img src={mine} alt='M'/>
        break
      default:
        break
    }

    if (status === 'empty' && +bombsNear > 0) content = <span className={`mines-${bombsNear}`}>{bombsNear}</span>

    return { klass: klass, content: content }
  }

  const { klass, content } = info()

  const mark = e => {
    e.preventDefault()

    if (gameStatus !== 'in_progress') return
    if (status === 'empty') return

    markCell(gameId, x, y)
  }

  const open = e => {
    e.preventDefault()

    if (gameStatus !== 'in_progress') return
    if (status !== 'closed') return

    openCell(gameId, x, y)
  }

  const preventSelection = e => {
    e.preventDefault()
  }

  return (
    <div
      key={id}
      className={klass}
      onClick={open}
      onContextMenu={mark}
      onMouseDown={preventSelection}
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
