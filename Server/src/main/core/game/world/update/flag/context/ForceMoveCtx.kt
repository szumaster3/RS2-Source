package core.game.world.update.flag.context

import core.game.world.map.Direction
import core.game.world.map.Location

/**
 * Represents a forced movement update for an entity.
 *
 * @property start The starting location of the movement.
 * @property dest The destination location of the movement.
 * @property startArrive The delay (in ticks) before the movement starts.
 * @property destArrive The duration (in ticks) it takes to reach the destination.
 * @property direction The direction the entity should face during the movement.
 */
data class ForceMoveCtx (val start: Location, val dest: Location, val startArrive: Int, val destArrive: Int, val direction: Direction)