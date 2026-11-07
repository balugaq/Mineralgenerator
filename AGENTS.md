# AGENTS.md — Mineralgenerator

Slimefun 附属插件：10 台矿物生成器，机器上方挂箱子自动产矿。本文档面向在本仓库工作的 AI 与人类开发者。

## 项目定位

新增 10 台矿物生成器与专属分类，放上箱子即自动产出对应矿物，速率可调，未改动原有机制。

## 技术栈

- Java 21（`compileJava { options.release = 21 }`），Gradle Kotlin DSL 构建，Shadow 打包（minimize + relocation）。
- 硬依赖：Slimefun4（`com.github.SlimefunGuguProject:Slimefun4` 2025.1，JitPack）。
- 打包内置（shaded 并 relocation 到 `io.github.shixinzia.mineralgenerator.*`）：InfinityLib 1.3.12、GuizhanLib 1.1.1。
- 主类继承 InfinityLib 的 `AbstractAddon`（自带 bStats 与 auto-update 机制，读取 `config.yml` 的 `auto-update`）。

## 包地图

```
io.github.shixinzia.mineralgenerator
├── Mineralgenerator.java        主类（AbstractAddon），enable() 里只做 Groups.setup + Materials.setup
├── categories/Groups.java       ItemGroup：MultiGroup 主分类 + SubGroup 子分类「矿物生成器」
└── items/materials/
    ├── Materials.java           10 个 SlimefunItemStack 定义 + setup() 注册全部生成器与配方
    └── Mineralgenerators.java   生成器实现类（SlimefunItem + BlockTicker）
```

## 启动顺序

`enable()` → `Groups.setup()`（注册主分类）→ `Materials.setup()`（依次注册 10 台生成器）。

## 核心机制（改动前必读）

- **产出循环**：`Mineralgenerators.tick()` 每 tick 检查机器正上方（`BlockFace.UP`）是否为箱子；是且箱子未满时，累计进度到 `rate` 值后向箱子 `addItem` 一次。
- **速率设置**：`IntRangeSetting("rate", 1, defaultRate, 1000)`，每台机器注册时传入默认值 30，服主可在指南内调整。
- **进度存储**：`generatorProgress`（`Map<BlockPosition, Integer>`，static），**不持久化**——重启后进度归零，属已知设计。
- `BlockTicker.isSynchronized() == true`，tick 在主线程执行；`PaperLib.getBlockState(targetBlock, false)` 做 snapshot 读取。
- 配方全部是 `RecipeType.ENHANCED_CRAFTING_TABLE`，九宫格中间为 `GEO_MINER`，上下为对应矿物/矿物块，两侧为塑料板/强化板。

## 构建与验收

```bash
./gradlew clean build   # 产物 build/libs/Mineralgenerator-<version>.jar
```

- 版本号唯一来源：`gradle.properties` 的 `version=`；`plugin.yml` 里的 `version: ${version}` 由 `processResources` 注入，**不要**手改 plugin.yml 的版本号或写回 `${project.version}`（那是 Maven 占位符，会导致 Bukkit 加载失败）。
- 新增依赖写入 `gradle/libs.versions.toml`（version catalog），`build.gradle.kts` 只用 `libs.xxx` 引用。
- shaded 库必须配 `relocate`，且 `minimize()` 会裁掉未被引用的类——若 shaded 库走反射加载需注意。

## CI

- `build.yml`：push/PR 触发，JDK 21，跑 test + build，上传产物。
- `release.yml`：`gradle.properties` 的 `version` 变化时自动打 `v<版本>` tag 并发 Release；**升版本只改 gradle.properties**。
- `test-compatibility.yml`：矩阵默认 `1.20.1,26.2`，下载 Slimefun4 后启动 Paper 验证插件 Enabling/Disabling。
- dependabot：`gradle` + `github-actions`，均为 monthly。

## 红线

1. 不要把构建迁回 Maven 或引入 `build.gradle`（Groovy DSL）；构建文件只有 `build.gradle.kts` + `settings.gradle.kts` + version catalog。
2. 不要改 `.github/workflows` 里的 `chmod +x gradlew` 行与 `./gradlew` 调用方式——wrapper 执行位丢失会导致 CI exit 126。
3. `gradle.properties` 禁止写入本机绝对路径（如 `org.gradle.java.home`）。
4. 新增生成器照抄 `Materials.setup()` 里的注册模式（`new Mineralgenerators(...).setItem(...).register(plugin)`），物品 ID 前缀保持 `MINERALGENERATOR_MINERAL_`。

## 已知债务

- `Mineralgenerators.java` 使用旧 API `me.mrCookieSlime.*`（BlockTicker / Config），编译有 deprecation 警告；Slimefun 未来移除该层时需迁移到新 ticker API。
- 产出进度不持久化（重启清零）。
- `plugin.yml` 的 `api-version: 1.16` 与 Java 21 字节码不完全匹配（1.16 服务端跑 Java 16/17，无法加载 major 65 的类）；实际可用底线取决于服务端 JVM 版本，未收紧前先保留。
