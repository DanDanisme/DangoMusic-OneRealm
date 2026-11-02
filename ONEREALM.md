# 与上游的差异

本仓库基于 [Sorihiko/DangoMusic](https://github.com/Sorihiko/DangoMusic) v1.7.2（提交 `e61f461`）修改，按 **GPL-3.0** 发布，原作者的版权与协议文件（`LICENSE`）保持不变。这不是官方版本，改动由一方世界（OneRealm）维护。

首次修改日期：2026-11-03。目前只构建 `modern/versions/1.21.11-neoforge`（Minecraft 1.21.11 + NeoForge），其余版本目录与上游一致。

## 本版本的特点

- 仅保留网易云音乐，必须登录网易云账号后才能使用搜索功能、搜索歌曲。
- 灵动岛集成通知功能，通知文字支持颜色。
- 支持接收并播放在线音频，可同时显示歌词。
- 不包含首页和排行榜；一起听歌功能暂未开放；检查更新默认关闭；歌曲卡片和左上角歌曲信息默认关闭。
- 构建产物不含上游作者的在线服务配置。

## 与上游相比改动的文件

新增：

- `common/core/.../music/BroadcastSource.kt`：在线音频音源
- `common/ui/.../broadcast/BroadcastInbox.kt`：外部消息接收与处理
- `common/ui/.../broadcast/ColorText.kt`：通知文字颜色解析
- `modern/versions/1.21.11-neoforge/.../neoforge/BroadcastPayload.java`：消息载荷

修改：

- `common/core/.../music/MusicPlayer.kt`、`MusicSources.kt`、`SourceHttp.kt`
- `common/core/.../party/ListenParty.kt`、`PartyTrackCodec.kt`
- `common/core/.../i18n/I18n.kt`
- `common/ui/.../module/impls/music/DynamicIslandModule.kt`、`LyricsModule.kt`、`NeteaseMusicModule.kt`
- `common/ui/.../music/MusicScreen.kt`、`CoverCache.kt`
- `common/ui/.../musicui/Library.kt`、`SettingsPage.kt`、`Sidebar.kt`、`LoginDialog.kt`
- `modern/src/main/kotlin/.../ModernDango.kt`
- `modern/versions/1.21.11-neoforge/.../neoforge/DangoMusicNeoForge.java`

删除（本版本不包含 QQ 音乐、酷狗音乐的实现）：

- `common/core/.../music/` 下的 `Qq*.kt`、`Kugou*.kt`、`NativeLogin.kt`、`NativeAccount.kt`、`AuthHttp.kt`

逐行差异请看 `git diff e61f461..HEAD`。
