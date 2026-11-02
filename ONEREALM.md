# Differences from upstream

This repository is a modified version of [Sorihiko/DangoMusic](https://github.com/Sorihiko/DangoMusic) v1.7.2 (commit `e61f461`), released under the **GPL-3.0**. The original copyright notices and the license file (`LICENSE`) are unchanged. This is not an official release; the changes are maintained by OneRealm.

Date of first modification: 2026-11-03. Only `modern/versions/1.21.11-neoforge` (Minecraft 1.21.11 + NeoForge) is built at the moment; the other version directories are identical to upstream.

## What this edition does

- Keeps NetEase Cloud Music only. Searching for songs requires logging in to a NetEase Cloud Music account first.
- The Dynamic Island integrates notifications, with colored text support.
- Receives and plays online audio, with lyrics shown alongside.
- No home page or charts. Listen Together is not available yet. Update checking is off by default. The song card and the top-left song info are off by default.
- Build output does not contain the upstream author's online service configuration.

## Files changed relative to upstream

Added:

- `common/core/.../music/BroadcastSource.kt`: online audio source
- `common/ui/.../broadcast/BroadcastInbox.kt`: external message intake and handling
- `common/ui/.../broadcast/ColorText.kt`: notification text color parsing
- `modern/versions/1.21.11-neoforge/.../neoforge/BroadcastPayload.java`: message payload

Modified:

- `common/core/.../music/MusicPlayer.kt`, `MusicSources.kt`, `SourceHttp.kt`
- `common/core/.../party/ListenParty.kt`, `PartyTrackCodec.kt`
- `common/core/.../i18n/I18n.kt`
- `common/ui/.../module/impls/music/DynamicIslandModule.kt`, `LyricsModule.kt`, `NeteaseMusicModule.kt`
- `common/ui/.../music/MusicScreen.kt`, `CoverCache.kt`
- `common/ui/.../musicui/Library.kt`, `SettingsPage.kt`, `Sidebar.kt`, `LoginDialog.kt`
- `modern/src/main/kotlin/.../ModernDango.kt`
- `modern/versions/1.21.11-neoforge/.../neoforge/DangoMusicNeoForge.java`

Deleted (this edition does not include the QQ Music or Kugou implementations):

- `Qq*.kt`, `Kugou*.kt`, `NativeLogin.kt`, `NativeAccount.kt` and `AuthHttp.kt` under `common/core/.../music/`

For the line-by-line differences, run `git diff e61f461..HEAD`.

## Building

```
export JAVA_HOME=<JDK 21>
cd modern
sh ./gradlew --no-daemon :1.21.11-neoforge:build
```

The first build downloads many dependencies and takes a long time; later builds are incremental. Output is in `modern/versions/1.21.11-neoforge/build/libs/`.
