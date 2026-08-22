package com.meepoffaith.hextrapats.registry

import at.petrak.hexcasting.api.casting.castables.SpecialHandler
import at.petrak.hexcasting.common.lib.HexRegistries
import at.petrak.hexcasting.xplat.IXplatAbstractions
import com.meepoffaith.hextrapats.casting.actions.eval.SpecialHandlerIndexedForEach
import com.meepoffaith.hextrapats.casting.handlers.*

object HextraSpecialHandlers : HextrapatsRegistrar<SpecialHandler.Factory<*>>(
    HexRegistries.SPECIAL_HANDLER,
    { IXplatAbstractions.INSTANCE.specialHandlerRegistry }
) {
    val VEC_X = make("vec_x", SpecialHandlerVectorX.Factory())
    val VEC_Y = make("vec_y", SpecialHandlerVectorY.Factory())
    val VEC_Z = make("vec_z", SpecialHandlerVectorZ.Factory())
    val VEC_1 = make("vec_1", SpecialHandlerVector1.Factory())
    val SCI_EXP = make("scientific_exp", SpecialHandlerScientificExponent.Factory())
    val INDEXED_FOR_EACH = make("indexed_for_each", SpecialHandlerIndexedForEach.Factory())
    val DUPLICATE_AT = make("duplicate_at", SpecialHandlerDuplicateAt.Factory())
    val VEC_SWIZZLE = make("vec/swizzle", SpecialHandlerVecSwizzling.Factory())

    private fun make(name: String, handler: SpecialHandler.Factory<*>): SpecialHandler.Factory<*> {
        register(name) { handler }
        return handler
    }
}
