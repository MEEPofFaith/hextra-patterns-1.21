package com.meepoffaith.hextrapats.casting.iota

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.utils.gold
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import com.meepoffaith.hextrapats.registry.HextraIotas
import com.mojang.serialization.MapCodec
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

class SetIota(val iotaSet: IotaSet): Iota({ HextraIotas.SET }) {
    constructor(iotaList: List<Iota>, argc: Int = 0) : this(IotaSet(iotaList, argc))

    override fun isTruthy(env: CastingEnvironment?): Boolean = iotaSet.isNotEmpty()

    override fun toleratesOther(that: Iota?): Boolean =
        that is SetIota && that.iotaSet == iotaSet

    override fun display(): Component{
        val out = "{ ".gold

        var first = true
        iotaSet.forEach{
            if(!first) out.append(" | ".gold)
            out.append(it.display())
            first = false
        }
        out.append(" }".gold)

        return out
    }

    override fun hashCode(): Int = iotaSet.hashCode()

    override fun subIotas(): Iterable<Iota>{
        return toList()
    }

    fun toList(): List<Iota>{
        return iotaSet.toList()
    }

    fun asActionResult(): List<Iota>{
        return listOf(this)
    }

    companion object {
        var CODEC: MapCodec<SetIota> = IotaType.TYPED_CODEC.listOf()
            .xmap({ list -> SetIota(list) }, SetIota::toList)
            .fieldOf("set")
        var STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SetIota> =
            IotaType.TYPED_STREAM_CODEC.apply(ByteBufCodecs.list())
                .map({ list -> SetIota(list) }, SetIota::toList)

        var TYPE: IotaType<SetIota> = object : IotaType<SetIota>() {
            override fun codec(): MapCodec<SetIota> = CODEC
            override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, SetIota> = STREAM_CODEC
            override fun color(): Int = 0xff_fadc19.toInt()
        }
    }
}
