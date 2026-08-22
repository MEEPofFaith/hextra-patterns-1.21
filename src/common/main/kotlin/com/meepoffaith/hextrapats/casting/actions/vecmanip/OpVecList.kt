package com.meepoffaith.hextrapats.casting.actions.vecmanip

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import com.meepoffaith.hextrapats.util.HextraUtils.getVecOrList
import net.minecraft.world.phys.Vec3

object OpVecList : ConstMediaAction {
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val iota = args.getVecOrList(0)
        return iota.map({ vec ->
            listOf(DoubleIota(vec.x), DoubleIota(vec.y), DoubleIota(vec.z)).asActionResult
        }, { list ->
            if(list.size != 3){
                throw MishapInvalidIota.of(args[0], 0, "hextrapats:vec_list")
            }
            val x = list[0]
            val y = list[1]
            val z = list[2]
            if(x is DoubleIota && y is DoubleIota && z is DoubleIota){
                Vec3(x.double, y.double, z.double).asActionResult
            }else{
                throw MishapInvalidIota.of(args[0], 0, "hextrapats:vec_list")
            }
        })
    }
}
