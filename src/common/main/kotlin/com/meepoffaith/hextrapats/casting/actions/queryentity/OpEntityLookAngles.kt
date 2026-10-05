package com.meepoffaith.hextrapats.casting.actions.queryentity

import at.petrak.hexcasting.api.HexAPI
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import kotlin.math.asin

object OpEntityLookAngles : ConstMediaAction {
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val e = args.getEntity(env.world, 0, argc)
        env.assertEntityInRange(e)

        val lookDir = HexAPI.instance().getEntityLookDirSpecial(e)
        return listOf(DoubleIota(asin(lookDir.y)), DoubleIota(Math.toRadians(e.yHeadRot.toDouble())))
    }
}
