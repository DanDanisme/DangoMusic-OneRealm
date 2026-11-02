package me.sorihiko.dango.music

import me.sorihiko.dango.lyrics.LyricLine
import me.sorihiko.dango.lyrics.Lyrics
import me.sorihiko.dango.lyrics.LyricsProvider

import java.util.concurrent.ConcurrentHashMap

/**
 * 服务器推送的音频（OneRealm 改动）：不搜索、不登录，歌曲信息、音频链接和歌词都由服务器消息给出。
 * 只收 MP3（内置解码器只支持 mp3）。歌曲的 nativeId 就是音频链接。
 */
object BroadcastSource : MusicSourceApi {
    override val source = MusicSource.BROADCAST

    /** 歌词按歌曲 key 存放：服务器消息里带的 LRC 文本 */
    private val lyricsByKey = ConcurrentHashMap<String, Lyrics>()
    /** 预加载好的音频：链接 -> 内容，用一次就取走 */
    private val preloaded = ConcurrentHashMap<String, ByteArray>()

    fun track(url: String, title: String, artist: String, durationMs: Long, cover: String): NeteaseTrack =
        NeteaseTrack(MusicSources.stableId(MusicSource.BROADCAST, url), title, if (artist.isEmpty()) emptyList() else listOf(artist),
            durationMs, cover, MusicSource.BROADCAST, url, url)

    fun setLyrics(track: NeteaseTrack, lrc: String) {
        val lines: List<LyricLine> = LyricsProvider.parseLrc(lrc)
        if (lines.isEmpty()) lyricsByKey.remove(track.key) else lyricsByKey[track.key] = Lyrics(lines, "Broadcast")
    }

    fun preload(url: String, bytes: ByteArray) {
        // 最多留 3 首，免得一直占内存
        if (preloaded.size >= 3) preloaded.keys.firstOrNull()?.let { preloaded.remove(it) }
        preloaded[url] = bytes
    }

    fun takePreloaded(url: String): ByteArray? = preloaded.remove(url)

    /** 下载这个链接的音频并留到预加载里（在后台线程调用） */
    fun download(url: String) {
        SourceHttp.bytes(url, "")?.let { preload(url, it) }
    }

    override fun search(query: String): List<NeteaseTrack> = emptyList()
    override fun songUrl(track: NeteaseTrack): NeteaseApi.SongUrl? = NeteaseApi.SongUrl(track.nativeId, false)
    override fun lyrics(track: NeteaseTrack): Lyrics? = lyricsByKey[track.key]
}
