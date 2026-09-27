package com.meepoffaith.hextrapats.casting.actions.queryentity

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.utils.TAU
import kotlin.math.PI

object OpEntityBodyYaw : ConstMediaAction {
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val e = args.getEntity(env.world, 0, argc)
        env.assertEntityInRange(e)

        val yaw = Math.toRadians(e.yRot.toDouble()).mod(TAU)
        return (if (yaw > PI) yaw - TAU else yaw).asActionResult
    }
}
