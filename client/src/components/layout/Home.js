import React, { Fragment } from 'react'

import minesweeper from './minesweeper.jpg'

const Home = () => {
  return (
    <Fragment>
      <h2>Welcome to Minesweeper</h2>
      <img src={minesweeper} alt='Minesweeper' title='Minesweeper'/>
    </Fragment>
  )
}

export default Home
