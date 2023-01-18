# joshua-util

工作中总结的各种好用的工具类，均在生产环境中稳定运行

## 工具说明

`TODO`

## 依赖三方库

 依赖           | 版本号           | 说明  
--------------|---------------|-----
spring | 5.2.12.RELEASE | 只用到了里面集成的cglib 
fastjson | 1.2.73        |  
jackson | 2.11.3 |  
commons-lang3 | 3.11          |  
commons-collections4 | 4.4           |  
guava | 29.0-jre      |  
slf4j | 1.7.30    |  
hibernate-validator | 6.1.6.Final |  
tika | 2.6.0   | 只用到了预测文件类型 
easyexcel | 2.2.7 | alibaba出品的简易excel解析工具 
zstd | 1.5.2-4 | google出品的压缩工具 
snappy | 1.1.8.4 | facebook出品的压缩工具 
lombok | 1.18.16 | 

## 使用前准备

- [Maven](https://maven.apache.org/) (构建/发布当前项目)
- Java 8 ([Download](https://adoptopenjdk.net/releases.html?variant=openjdk8))

## 构建/安装项目

使用以下命令:

`mvn clean install`

## 引用项目

```xml

<dependency>
    <groupId>com.mogudiandian</groupId>
    <artifactId>joshua-util</artifactId>
    <version>LATEST</version>
</dependency>
```

## 发布项目

修改 `pom.xml` 的 `distributionManagement` 节点，替换为自己在 `settings.xml` 中 配置的 `server` 节点，
然后执行 `mvn clean deploy`

举例：

`settings.xml`

```xml
<servers>
    <server>
        <id>snapshots</id>
        <username>yyy</username>
        <password>yyy</password>
    </server>
    <server>
        <id>releases</id>
        <username>xxx</username>
        <password>xxx</password>
    </server>
</servers>
```

`pom.xml`

```xml
<distributionManagement>
    <snapshotRepository>
        <id>snapshots</id>
        <url>http://xxx/snapshots</url>
    </snapshotRepository>
    <repository>
        <id>releases</id>
        <url>http://xxx/releases</url>
    </repository>
</distributionManagement>
```
