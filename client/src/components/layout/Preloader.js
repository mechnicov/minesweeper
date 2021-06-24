import React from 'react'
import { ProgressBar } from 'react-bootstrap'

const Preloader = () => {
  return (
    <ProgressBar animated now={100} variant='secondary' className='mt-4'/>
  )
}

export default Preloader
