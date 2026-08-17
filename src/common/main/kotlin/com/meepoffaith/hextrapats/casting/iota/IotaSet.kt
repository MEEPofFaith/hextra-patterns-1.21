package com.meepoffaith.hextrapats.casting.iota

import at.petrak.hexcasting.api.casting.iota.*
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import com.meepoffaith.hextrapats.util.MathUtils
import com.meepoffaith.hextrapats.util.MathUtils.roundToTolerance

class IotaSet : HashSet<Iota> {
    constructor(): super()
    constructor(map: IotaSet) : super(map)
    constructor(list: List<Iota>, argc: Int = 0) : super(){
        for(i in 0..list.lastIndex){
            add(list[i], argc - (i + 1))
        }
    }

    fun copy(): IotaSet{
        return IotaSet(this)
    }

    override fun add(iota: Iota): Boolean{
        return super.add(coerceIota(iota))
    }

    fun add(iota: Iota, reversedIdx: Int): Boolean{
        if(!checkType(iota)) {
            throw MishapInvalidIota.of(iota, reversedIdx, "hextrapats:set_item")
        }
        return add(iota)
    }

    override fun contains(iota: Iota): Boolean{
        return super.contains(coerceIota(iota))
    }

    override fun remove(iota: Iota): Boolean{
        return super.remove(coerceIota(iota))
    }

    fun asActionResult(): List<Iota>{
        return listOf(SetIota(this))
    }

    override fun hashCode(): Int {
        var hashCode = 2 // Hopefully, starting on 2 instead of 1 is enough to differentiate from a list.
        for(iota in this){
            hashCode *= 31
            hashCode += iota.hashCode()
        }
        return hashCode
    }

    companion object {
        /** Iota types disallowed from being input into an iota set. */
        val DISALLOWED_TYPES = mutableSetOf<IotaType<*>>(ListIota.TYPE, SetIota.TYPE, ContinuationIota.TYPE)

        fun checkType(iota: Iota): Boolean{
            return !DISALLOWED_TYPES.contains(iota.type)
        }

        fun coerceIota(iota: Iota): Iota{
            // There's probably a better option than hardcoding this.
            // TODO: Do something to make it easier for other addons to add their own iotas to this.
            return when(iota){
                is DoubleIota -> DoubleIota(iota.double.roundToTolerance())
                is Vec3Iota -> Vec3Iota(iota.vec3.roundToTolerance())
                else -> iota
            }
        }
    }
}
