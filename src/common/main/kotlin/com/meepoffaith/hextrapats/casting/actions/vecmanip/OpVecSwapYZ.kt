package com.meepoffaith.hextrapats.casting.actions.vecmanip

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import net.minecraft.world.phys.Vec3

object OpVecSwapYZ : ConstMediaAction{
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val vec = args.toMutableList().getVec3(0)
        return Vec3(vec.x, vec.z, vec.y).asActionResult
    }
}
