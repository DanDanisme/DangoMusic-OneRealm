package me.sorihiko.dango.neoforge;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * OneRealm 改动：服务器推送的消息（频道 onerealm:broadcast），内容是一段 JSON 文本，格式见 BroadcastInbox。
 * 线上编码是 Minecraft 的字符串（VarInt 长度 + UTF-8），Paper 插件按同样方式写。
 */
public record BroadcastPayload(String json) implements CustomPacketPayload {

    public static final Type<BroadcastPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath("onerealm", "broadcast"));

    public static final StreamCodec<RegistryFriendlyByteBuf, BroadcastPayload> CODEC =
            StreamCodec.composite(ByteBufCodecs.stringUtf8(262144), BroadcastPayload::json, BroadcastPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
