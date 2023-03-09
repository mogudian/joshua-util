# joshua-util

工作中总结的各种好用的工具类，均在生产环境中稳定运行

## 工具说明

### bean
#### BeanCopyUtils 用于进行对象属性的拷贝
```java
// 两个已有对象直接拷贝属性
BeanCopyUtils.copyProperties(src, target);

// 拷贝对象的属性到新对象中并返回新对象
X x = BeanCopyUtils.copyPropertiesAndGet(src, new X());

// 用指定的方法构造新对象并拷贝对象的属性到新对象中并返回新对象
X x = BeanCopyUtils.copyPropertiesAndGet(src, X::new);
```

### codec
#### Base58 提供Base58编解码
```java
// 编码
String str = "abc";
String base58 = Base58.encode(str.toBytes());

// 解码
byte[] bytes = Base58.decode(base58);
String str = new String(bytes);
```

#### MD5 提供MD5摘要算法
```java
// 摘要
String str = "abc";
String md5 = MD5.digest(str);
```

### compressor
#### GzipCompressor GZIP压缩器
```java
byte[] src = ...;
Compressor compressor = new GzipCompressor();
// 压缩
byte[] dist = compressor.compress(src);
// 解压
src = compressor.decompress(dist);
```
#### SnappyCompressor google snappy压缩器
```java
byte[] src = ...;
Compressor compressor = new SnappyCompressor();
// 压缩
byte[] dist = compressor.compress(src);
// 解压
src = compressor.decompress(dist);
```
#### ZstdCompressor facebook z-standard压缩器
```java
byte[] src = ...;
Compressor compressor = new ZstdCompressor();
// 压缩
byte[] dist = compressor.compress(src);
// 解压
src = compressor.decompress(dist);
```

### date
#### DateParser 时间字符串解析器，效率高，并且不会抛异常
```java
DateParser dataParser = new DateParser("yyyy-MM-dd");
// 解析
Date date = dateParser.parse("2021-03-21");
// 输出字符串
String str = dateParser.format(date);
```
#### LocalDateUtils JDK8 LocalDate的工具类
```java
Date date = ...;
// 将 Date 对象 转换为 LocalDate 对象
LoacalDate localDate = LocalDateUtils.convertToLocalDate(date);
// 将 Date 对象 转换为 LocalDateTime 对象
LocalDateTime localDateTime = LocalDateUtils.convertToLocalDateTime(date);
// 将 LocalDate 对象 转换为 Date 对象
Date theDate = LocalDateUtils.convert(localDate);
// 将 LocalDateTime 对象 转换为 Date 对象
Date theDateWithTime = LocalDateUtils.convert(localDateTime);
```

### excel
#### ExcelParser Excel解析器
```java
File file = ...;
ExcelParser<XyzExcelDTO> excelParser = new ExcelParser<>(XyzExcelDTO.class);
// 解析Excel
List<XyzExcelDTO> xyzList = excelParser.parse(new FileInputStream(file));
```

### html
#### HTMLUtils HTML工具类
```java
String html = ...;
// 去掉HTML上的标签以及内嵌的css、js代码，将HTML转换为纯文本
String pureText = HTMLUtils.removeTags(html);
// 摘要，取HTML中的前n个字符，保证不会出现标签截断
int n = 50;
String digest = HTMLUtils.summarize(html, n, false);
```

### image
#### ImageUtils 图片工具类
```java
BufferedImage image = ...;
// 缩小图片，最大尺寸是256*256，等比例缩放，如果不足256则不缩放
BufferedImage smallImage = ImageUtils.zoomOut(image, 256, ResizeMode.AUTO resizeMode, false);
```

### io
#### ByteBufferInputStream 包装ByteBuffer的InputStream
#### ByteBufferOutputStream 包装ByteBuffer的OutputStream
#### ByteBufferUtils ByteBuffer的工具类

### javac
#### DynamicCompiler Java动态编译器

### jdbc
#### JdbcUtils JDBC的工具类

### json.fastjson
#### FastJsonUtils FastJSON的工具类
#### JSONArrayBuilder 快速构造JSONArray
```java
JSONArray arr1 = JSONArrayBuilder.build(jsonObject);
JSONArray arr2 = JSONArrayBuilder.build(jsonObject1, jsonObject2);
...
```
#### JSONArrayIterable JSONArray的迭代器
```java
JSONArray jsonArray = ...;
// 原来写法
for (int i = 0; i < jsonArray.size(); i++) {
    JSONObject jsonObject = jsonArray.getJSONObject(i);
    ...
}
// 使用工具后的写法
for (JSONObject jsonObject : new JSONArrayIterable<>(jsonArray, JSONObject.class)) {
    ....
}
```
#### JSONObjectBuilder 快速构造JSONObject
```java
JSONObject o1 = JSONObjectBuilder.build("a", 1);
JSONObject o2 = JSONObjectBuilder.build("a", 1, "b", "2");
...
```
#### JSONPathUtils JSONPath的工具类
#### LongArrayOmitFilter 省略长数组的过滤器
```java
String str = JSON.toJSONString(jsonObject, new LongArrayOmitFilter(x));
```
#### LongStringOmitFilter 省略长字符串的过滤器
```java
String str = JSON.toJSONString(jsonObject, new LongStringOmitFilter(x));
```

### json.jackson
#### BigDecimal2BitsSerializer BigDecimal使用jackson序列化保留两位小数
#### BigDecimal4BitsSerializer BigDecimal使用jackson序列化保留四位小数
#### JacksonUtils Jackson工具类

### map
#### HashMapBuilder 快速构建HashMap
```java
Map<String, Integer> map1 = HashMapBuilder.build("a", 1);
Map<String, String> map2 = HashMapBuilder.build("a", "1", "b", "2");
...
```

### mask
#### MaskUtils 掩码脱敏工具类

### network
#### ClientIpUtils 客户端IP地址获取工具，考虑了反向代理的情况
```java
@Controller
public class UserController {
    @RequestMapping("/user/login")
    public ApiResponse<String> login(HttpServletRequest request) {
        String clientIp = ClientIpUtils.getClientIp(request);
        System.out.println("client real ip is: " + clientIp);
    }
}

```
#### DownloadUtils 文件下载工具
#### IpAddressUtils IP地址的工具类

### random
#### UuidUtils UUID的工具类

### regex
#### RegexUtils 正则工具类

### stream
#### RandomKCollector 实现RandomK的collector
```java
int[] arr = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
List<Integer> list = Arrays.stream(arr).boxed().collect(RandomKCollector.newInstance(6));
System.out.println(list);
```
#### StreamUtils 流的工具类

### validator
#### BaseValidator 基础校验器

### 未分包
#### EnumGetter 枚举工具类
#### ObjectRelationMatcher 对象关系匹配器，特别适合不允许联表时的列表数据组装
```java
// 例如一个评论列表页，不只展示评论内容，还需要展示文章标题和内容
List<CommentDTO> comments = commentService.query(condition);
// 根据评论(CommentDTO)中的文章ID查询文章(ArticleDTO)并设置文章的标题和内容
ObjectRelationMatcher<CommentDTO, Long, List<Long>, ArticleDTO, Long> matcher = new ObjectRelationMatcher<>(); <br/>
matcher.setElements(comments)
       .setElementIdentifierExtractor(CommentDTO::getArticleId)
       .setIdentifierCollectorType(List.class)
       .setBatchQueryMethod(articleService::queryByIds)
       .setDataKeyGenerator(ArticleDTO::getId)
       .setElementToKeyMapping(CommentDTO::getArticleId)
       .match()
       .processOneToOne((comment, article) -> {
           comment.setArticleTitle(article.getTitle());
           comment.setArticleContent(article.getContent());
       }, comment -> {
           comment.setArticleTitle("未知标题");
           comment.setArticleContent("未知内容");
       });
```

## 依赖三方库

| 依赖                   | 版本号            | 说明                    |
|----------------------|----------------|-----------------------|
| spring               | 5.2.12.RELEASE | 只用到了里面集成的cglib        |
| fastjson             | 1.2.73         |                       |
| jackson              | 2.11.3         |                       |
| commons-lang3        | 3.11           |                       |
| commons-collections4 | 4.4            |                       |
| guava                | 29.0-jre       |                       |
| slf4j                | 1.7.30         |                       |
| hibernate-validator  | 6.1.6.Final    |                       |
| tika                 | 2.6.0          | 只用到了预测文件类型            |
| easyexcel            | 2.2.7          | alibaba出品的简易excel解析工具 |
| zstd                 | 1.5.2-4        | google出品的压缩工具         |
| snappy               | 1.1.8.4        | facebook出品的压缩工具       |
| servlet-api          | 3.1.0          | servlet相关依赖           |
| lombok               | 1.18.16        |                       |

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
