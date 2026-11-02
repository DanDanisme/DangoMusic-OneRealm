# 一方世界改版说明（OneRealm fork）

本仓库是 [Sorihiko/DangoMusic](https://github.com/Sorihiko/DangoMusic) v1.7.2（提交 `e61f461`）的修改版，按 **GPL-3.0** 发布，原作者版权与协议文件（`LICENSE`）保持不变。**这不是官方版本**，改动由一方世界（OneRealm）自行维护，有问题请不要去打扰原作者。

目前只构建 `modern/versions/1.21.11-neoforge`（Minecraft 1.21.11 + NeoForge），其余版本目录保留上游原样，未改动也未验证。

## 改动概览（相对 v1.7.2，首次修改于 2026-11-03）

1. **服务器推送**：新增 NeoForge 自定义消息通道 `onerealm:broadcast`，服务器可以向客户端发 JSON 消息：
   - `notice`：在灵动岛显示公告（标题、副标题、秒数），支持 `&0-f`、`&#RRGGBB`、`&r` 颜色标记；
   - `prepare` / `play` / `stop`：预加载并播放服务器给的 MP3 链接，可带歌词（LRC）、时长、起点位置。
2. **新音源 `BROADCAST`**：专门承载服务器推送的音乐，不出现在音源选择里，不能进入一起听歌。下载后会检查是否真的是 MP3。
3. **界面精简**：移除首页和排行榜；侧栏默认进“正在播放”；一起听歌入口保留但显示“暂未开放”，不再连接任何云端；检查更新默认关闭并隐藏设置项；歌曲卡片、左上角歌曲信息及封面的设置项隐藏，默认关闭。
4. **不含作者的服务配置**：构建产物里没有 `firebase.properties`。

## 改动文件清单

新增：

- `common/core/.../music/BroadcastSource.kt`：BROADCAST 音源
- `common/ui/.../broadcast/BroadcastInbox.kt`：消息队列与处理
- `common/ui/.../broadcast/ColorText.kt`：颜色标记解析
- `modern/versions/1.21.11-neoforge/.../neoforge/BroadcastPayload.java`：消息载荷

修改：

- `common/core/.../music/MusicPlayer.kt`、`MusicSources.kt`、`SourceHttp.kt`、`party/PartyTrackCodec.kt`
- `common/ui/.../module/impls/music/DynamicIslandModule.kt`、`LyricsModule.kt`、`NeteaseMusicModule.kt`
- `common/ui/.../music/MusicScreen.kt`、`musicui/Library.kt`、`SettingsPage.kt`、`Sidebar.kt`
- `modern/src/main/kotlin/.../ModernDango.kt`
- `modern/versions/1.21.11-neoforge/.../neoforge/DangoMusicNeoForge.java`

逐行差异请看 `git diff e61f461..HEAD`。

## 构建

```
export JAVA_HOME=<JDK 21>
cd modern
sh ./gradlew --no-daemon :1.21.11-neoforge:build
```

第一次构建要下载大量依赖，耗时很长（实测约 46 分钟），之后增量构建 1–3 分钟。产物在 `modern/versions/1.21.11-neoforge/build/libs/`。
