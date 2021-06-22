import React, { Fragment } from 'react'

import notfound from './404.jpg'

const NotFound = () => {
  return (
    <Fragment>
      <h2>Page not found</h2>
      <img src={notfound} alt='404' title='404'/>
    </Fragment>
  )
}

export default NotFound
