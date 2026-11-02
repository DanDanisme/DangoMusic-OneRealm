package me.sorihiko.dango.party

import com.google.gson.JsonObject
import me.sorihiko.dango.music.*

/** Only public song metadata goes into rooms. Each member resolves URLs with their own login. */
internal object PartyTrackCodec {
    fun write(track: NeteaseTrack): JsonObject = JsonObject().apply {
        addProperty("trackId", if (track.source == MusicSource.NETEASE) track.id else 0L)
        addProperty("source", track.source.key); addProperty("nativeId", track.nativeId)
        addProperty("mediaId", track.mediaId); addProperty("albumId", track.albumId)
        addProperty("name", track.name); addProperty("artists", track.artists.joinToString(" / "))
        addProperty("cover", track.coverUrl); addProperty("duration", track.durationMs)
    }
    fun read(data: JsonObject): NeteaseTrack? {
        val key = data.text("source").ifEmpty { "netease" }
        val source = MusicSource.values().firstOrNull { it.key == key } ?: return null
        val nativeRaw = data.text("nativeId").ifEmpty { data.text("trackId") }
        val native = nativeRaw
        val id = when (source) {
            MusicSource.NETEASE -> native.toLongOrNull()?.takeIf { it > 0 } ?: return null
            MusicSource.BROADCAST -> return null   // 服务器推送的歌不进一起听歌房间
        }
        val media = data.text("mediaId").ifEmpty { native }
        val album = data.text("albumId")
        return NeteaseTrack(id, data.text("name").take(1024), data.text("artists").take(2048).split(" / ").filter { it.isNotEmpty() },
            data.number("duration").coerceIn(0,24*60*60*1000L), data.text("cover").take(4096), source,native,media,album)
    }
}
