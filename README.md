# Mineralgenerator 矿物生成器

一个 Slimefun 附属插件：新增 10 台矿物生成器与专属分类，放上箱子即自动产出对应矿物，速率可调，未改动原有机制。

## 功能

生成器放置后，在**机器正上方放一个普通箱子**，即会按固定节奏向箱子内产出对应物品：

| 生成器 | 产出 |
| --- | --- |
| 煤炭生成器 | 煤炭 |
| 铁锭生成器 | 铁锭 |
| 铜锭生成器 | 铜锭 |
| 金锭生成器 | 金锭 |
| 钻石生成器 | 钻石 |
| 青金石生成器 | 青金石 |
| 红石生成器 | 红石 |
| 绿宝石生成器 | 绿宝石 |
| 石英生成器 | 石英 |
| 下界合金锭生成器 | 下界合金锭 |

- 全部生成器归入 Slimefun 指南中的「矿物生成器」专属分类。
- 配方均为**增强工作台**（Enhanced Crafting Table）合成，核心部件包含地质矿业机（GEO-Miner）、机器人内存核心、高级电路板、强化板等 Slimefun 物料。
- 产出速率默认 30 tick（约 1.5 秒）一个，可在 Slimefun 指南内通过物品设置（1–1000 tick）调整。

## 前置

- Paper / Spigot 服务端（`api-version: 1.16`+）
- [Slimefun4](https://github.com/SlimefunGuguProject/Slimefun4)（硬依赖）

## 安装

1. 将 `Mineralgenerator-<版本>.jar` 放入服务器 `plugins/` 目录；
2. 同时确保 `plugins/` 内已安装 Slimefun4；
3. 重启服务器。

## 构建

```bash
./gradlew clean build
```

产物位于 `build/libs/Mineralgenerator-<版本>.jar`。构建需要 JDK 21+。

## 反馈

问题请提交到 [Issues](https://github.com/balugaq/Mineralgenerator/issues)。

## 许可

见 [LICENSE](LICENSE)。
