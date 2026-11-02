package me.sorihiko.dango.broadcast

import me.sorihiko.dango.utils.Color

/**
 * 公告文字里的颜色标记（OneRealm 改动），写法和 Minecraft 一致：
 *   &0～&f 或 §0～§f：16 种标准颜色；&#RRGGBB：任意颜色；&r：回到默认颜色。
 * 例：`&c新年&6快乐 &#00ccff一方世界`。不认识的标记原样显示。
 */
object ColorText {

    class Seg(val text: String, val color: Color?)

    private val palette = mapOf(
        '0' to 0x000000, '1' to 0x0000AA, '2' to 0x00AA00, '3' to 0x00AAAA,
        '4' to 0xAA0000, '5' to 0xAA00AA, '6' to 0xFFAA00, '7' to 0xAAAAAA,
        '8' to 0x555555, '9' to 0x5555FF, 'a' to 0x55FF55, 'b' to 0x55FFFF,
        'c' to 0xFF5555, 'd' to 0xFF55FF, 'e' to 0xFFFF55, 'f' to 0xFFFFFF,
    )

    /** 拆成一段一段，color 为 null 表示用默认颜色 */
    fun parse(s: String): List<Seg> {
        val out = mutableListOf<Seg>()
        val cur = StringBuilder()
        var color: Color? = null
        fun flush() {
            if (cur.isNotEmpty()) out += Seg(cur.toString(), color)
            cur.setLength(0)
        }
        var i = 0
        while (i < s.length) {
            val c = s[i]
            if ((c == '&' || c == '§') && i + 1 < s.length) {
                val n = s[i + 1]
                if (n == '#' && i + 7 < s.length + 0 && s.substring(i + 2, minOf(i + 8, s.length)).matches(Regex("[0-9a-fA-F]{6}"))) {
                    flush()
                    color = Color.fromRGB(s.substring(i + 2, i + 8).toInt(16))
                    i += 8
                    continue
                }
                val rgb = palette[n.lowercaseChar()]
                if (rgb != null) {
                    flush(); color = Color.fromRGB(rgb); i += 2; continue
                }
                if (n == 'r' || n == 'R') {
                    flush(); color = null; i += 2; continue
                }
            }
            cur.append(c)
            i++
        }
        flush()
        return out
    }

    fun plain(segs: List<Seg>): String = segs.joinToString("") { it.text }
}
