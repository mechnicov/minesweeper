import React from 'react'
import { Table } from 'react-bootstrap'

const GamesList = ({ games }) => {
  return (
    <Table striped bordered hover size='sm'>
      <thead>
        <tr>
          <th>#</th>
          <th>Game</th>
          <th>Status</th>
        </tr>
      </thead>
      <tbody>
        {games.map(game =>
          <tr key={game.id}>
            <td>{game.id}</td>
            <td><a href={`/games/${game.id}`}>See the game</a></td>
            <td>{game.status}</td>
          </tr>
        )}
      </tbody>
    </Table>
  )
}

export default GamesList
