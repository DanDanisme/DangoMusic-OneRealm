package me.sorihiko.dango.broadcast

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.sorihiko.dango.DangoLogger
import me.sorihiko.dango.DangoMusic
import me.sorihiko.dango.module.impls.music.DynamicIslandModule
import me.sorihiko.dango.music.BroadcastSource
import me.sorihiko.dango.music.MusicPlayer
import me.sorihiko.dango.music.MusicSource

import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors

/**
 * 服务器推送的消息（OneRealm 改动）。平台层把收到的 JSON 文本交给 [push]，游戏线程每个 tick 调 [process] 处理。
 *
 * 消息格式（JSON，v = 1）：
 *   {"v":1,"type":"notice","title":"新年快乐","sub":"服务器发来的祝福","seconds":6}
 *   {"v":1,"type":"prepare","url":"https://…/x.mp3"}                              预加载音频，不播放
 *   {"v":1,"type":"play","url":"…","title":"…","artist":"…","cover":"…","lrc":"[00:01.00]…","position":0}
 *                                                                              开始播放（position 是毫秒，晚进服时用来跳到当前位置）
 *   {"v":1,"type":"stop"}                                                       停止服务器推送的音乐
 */
object BroadcastInbox {

    private val queue = ConcurrentLinkedQueue<String>()
    private val worker = Executors.newSingleThreadExecutor { r -> Thread(r, "Dango-Broadcast").apply { isDaemon = true } }

    /** 网络线程调用：只入队，不做任何事 */
    @JvmStatic
    fun push(json: String) {
        if (queue.size < 32) queue.add(json)
    }

    /** 游戏线程每个 tick 调用 */
    @JvmStatic
    fun process() {
        while (true) {
            val raw = queue.poll() ?: return
            try {
                handle(JsonParser().parse(raw).asJsonObject)
            } catch (e: Exception) {
                DangoLogger.warn("Broadcast message ignored: {}", e.toString())
            }
        }
    }

    private fun JsonObject.str(k: String): String = get(k)?.takeIf { it.isJsonPrimitive }?.asString ?: ""
    private fun JsonObject.num(k: String): Long = get(k)?.takeIf { it.isJsonPrimitive }?.asLong ?: 0L

    private fun handle(o: JsonObject) {
        if (o.num("v") != 1L) return
        when (o.str("type")) {
            "notice" -> {
                val title = o.str("title").take(200)
                if (title.isEmpty()) return
                val secs = (o.get("seconds")?.takeIf { it.isJsonPrimitive }?.asFloat ?: 6f)
                DangoMusic.instance.moduleManager.get(DynamicIslandModule::class.java).showNotice(title, o.str("sub").take(200), secs)
            }
            "prepare" -> {
                val url = checkedUrl(o.str("url")) ?: return
                worker.execute {
                    try {
                        BroadcastSource.download(url)
                    } catch (e: Exception) {
                        DangoLogger.warn("Broadcast preload failed: {}", e.toString())
                    }
                }
            }
            "play" -> {
                val url = checkedUrl(o.str("url")) ?: return
                val track = BroadcastSource.track(url, o.str("title").take(200).ifEmpty { "Broadcast" }, o.str("artist").take(200),
                    o.num("duration").coerceIn(0L, 24 * 60 * 60 * 1000L), o.str("cover").take(4096))
                o.str("lrc").take(200_000).takeIf { it.isNotEmpty() }?.let { BroadcastSource.setLyrics(track, it) }
                val base = o.num("position").coerceAtLeast(0L)
                val received = System.currentTimeMillis()
                // 起始位置在下载完成后才算：下载花的时间也算进去，大家才能对齐
                MusicPlayer.playTrackAt(track, { base + (System.currentTimeMillis() - received) }, false)
            }
            "stop" -> if (MusicPlayer.current?.source == MusicSource.BROADCAST) MusicPlayer.stop()
        }
    }

    /** 只收 http(s) 链接，长度有限制 */
    private fun checkedUrl(u: String): String? =
        u.takeIf { it.length in 8..2000 && (it.startsWith("https://") || it.startsWith("http://")) }
}
