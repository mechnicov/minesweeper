import React from 'react'

import mine from './mine.png'
import flag from './flag.png'
import './styles.scss'

const Cell = ({ cell, gameStatus }) => {
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

  const markCell = e => {
    e.preventDefault()

    const { x, y } = e.target.dataset

    console.log(x, y, 'mark')
  }

  const openCell = e => {
    e.preventDefault()

    const { x, y } = e.target.dataset

    console.log(x, y, 'open')
  }

  return (
    <div
      key={cell.id}
      data-x={cell.x}
      data-y={cell.y}
      className={klass}
      onClick={openCell}
      onContextMenu={markCell}
    >
      {content}
    </div>
  )
}

export default Cell
