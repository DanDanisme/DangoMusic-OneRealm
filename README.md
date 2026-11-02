<p align="center"><img src="docs/logo.png" alt="Dango Music" width="220"></p>

<h1 align="center">Dango Music · 一方世界版</h1>

<p align="center"><em>Minecraft 里的纯净网易云音乐播放器：液态玻璃界面、3D 歌词、灵动岛。</em></p>

> **这是修改版。** 本项目基于 [Sorihiko/DangoMusic](https://github.com/Sorihiko/DangoMusic) v1.7.2 修改而来，按 **GPL-3.0** 发布，保留原作者的版权与协议。它不是官方版本，使用中遇到问题请不要打扰原作者。与上游的差异见 [ONEREALM.md](ONEREALM.md)。

## 功能

- **仅保留网易云音乐**，在游戏内扫码登录
- **登录后才能搜索**歌曲，并使用自己账号里的歌单
- **液态玻璃播放器**（默认按 `M`）：歌单列表 / 封面墙两种视图，设置都在侧栏的 ⚙ 里
- **3D 歌词**：逐字歌词漂浮在世界里，带发光、抖动、文闪等效果
- **灵动岛**：显示正在播放的歌曲，并集成通知功能
- **在线播放**：接收并播放在线音频，同步显示歌词
- 一起听歌：暂未开放

## 支持版本

目前只构建 Minecraft **1.21.11 + NeoForge**。仓库里其余版本目录保留上游原样，未改动也未验证。

## 构建

```
export JAVA_HOME=<JDK 21>
cd modern
sh ./gradlew --no-daemon :1.21.11-neoforge:build
```

第一次构建要下载大量依赖，耗时很长（实测约 46 分钟），之后增量构建 1–3 分钟。产物在 `modern/versions/1.21.11-neoforge/build/libs/`。

## 协议

GPL-3.0，见 [LICENSE](LICENSE)。原作者：Sorihiko。
