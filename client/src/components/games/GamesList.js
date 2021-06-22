import React from 'react'
import { Table } from 'react-bootstrap'

const GamesList = ({ games }) => {
  const parseDateTime = isoDateTime => {
    const fn = (i) => {
      return (i < 10) ? '0' + i : '' + i
    }

    const d = new Date(isoDateTime)

    return `${fn(d.getDate())}.${fn(d.getMonth() + 1)}.${fn(d.getFullYear())} ${fn(d.getHours())}:${fn(d.getMinutes())}`
  }

  return (
    <Table striped bordered hover size='sm'>
      <thead>
        <tr>
          <th>#</th>
          <th>Game</th>
          <th>Started At</th>
          <th>Status</th>
        </tr>
      </thead>
      <tbody>
        {games.map(game =>
          <tr key={game.id}>
            <td>{game.id}</td>
            <td><a href={`/games/${game.id}`}>See the game</a></td>
            <td>{parseDateTime(game.createdAt)}</td>
            <td>{game.status}</td>
          </tr>
        )}
      </tbody>
    </Table>
  )
}

export default GamesList
