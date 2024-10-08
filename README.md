# joshua-util

工作中总结的各种好用的工具类，均在生产环境中稳定运行

## 引用项目（已发布至中央仓库）

```xml
<dependency>
    <groupId>com.mogudiandian</groupId>
    <artifactId>joshua-util</artifactId>
    <version>LATEST</version>
</dependency>
```

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

// 拷贝集合
List<Entity> list = ...;
Set<Dto> set = BeanCopyUtils.copyCollection(list, HashSet::new, Dto::new);

// 拷贝集合到list
List<Entity> list = ...;
List<Dto> newList = BeanCopyUtils.copyList(list, Dto::new);

// 拷贝集合到set
List<Entity> list = ...;
Set<Dto> newSet = BeanCopyUtils.copySet(list, Dto::new);
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

### function
#### BinaryOperators 封装了常用的BinaryOperator
```java
list.stream().collect(Collectors.toMap(XxxDTO::getId, XxxDTO::getName, BinaryOperators.useFirst()));
list.stream().collect(Collectors.toMap(XxxDTO::getId, XxxDTO::getName, BinaryOperators.useLast()));
list.stream().collect(Collectors.toMap(XxxDTO::getId, XxxDTO::getName, BinaryOperators.useFirstNonNull()));
list.stream().collect(Collectors.toMap(XxxDTO::getId, XxxDTO::getName, BinaryOperators.useLastNonNull()));
```
```
#### FunctionUtils Function的工具
```java
// 例如 有两个现成的函数分别是把A转为B、把B转为C，那么现在需要一个函数是把A转为C
Function<A, B> aToB;
Function<B, C> bToC;
Function<A, C> aToC = FunctionUtils.compose(bToC, aToB);
```
#### TriFunction 三个参数的函数
#### TriPredicate 三个参数的断言
#### TryCatch 使用函数式编程的方式执行try-catch，对于可忽略的异常降低代码量
```java
xxx = tryGet(() -> doSomething());
xxx = tryGet(() -> tryFunction(), () -> catchFunction());
xxx = tryGet(() -> tryFunction(), defaultValue);
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
```java
ByteBuffer byteBuffer = ...;
// 让 ByteBuffer 像 byte[] 一样易读
try (ByteBufferInputStream input = new ByteBufferInputStream(byteBuffer);
     FileOutputStream output = new FileOutputStream("xxx")) {
    byte[] bytes = new byte[4096];
    for (int len; (len = input.read(bytes)) >= 0; ) {
        output.write(bytes)
        output.flush();
    }
}
```
#### ByteBufferOutputStream 包装ByteBuffer的OutputStream
```java
ByteBuffer byteBuffer = ...;
// 让 ByteBuffer 像 byte[] 一样易写
try (FileInputStream input = new FileInputStream("xxx");
     ByteBufferOutputStream output = new ByteBufferOutputStream(byteBuffer)) {
    byte[] bytes = new byte[4096];
    for (int len; (len = input.read(bytes)) >= 0; ) {
        output.write(bytes)
        output.flush();
    }
}
```
#### ByteBufferUtils ByteBuffer的工具类
```java
byte[] bytes = ...;
// 将 byte[] 对象转为 ByteBuffer 对象
ByteBuffer byteBuffer = ByteBufferUtils.newByteBuffer(bytes);
// 将 ByteBuffer 对象转为 byte[] 对象
byte[] byteArray = ByteBufferUtils.toByteArray(byteBuffer);
```

### javac
#### DynamicCompiler Java动态编译器

### jdbc
#### JdbcUtils JDBC的工具类
```java
DataSource dataSource = ...;
String sql = "select * from xxx where create_time between ? and ?";
Date start = ..., end = ...;
List<XxxEntity> xxxList = JdbcUtils.selectList(XxxEntity.class, dataSource, sql, start, end);
```

### json.fastjson
#### FastJsonUtils FastJSON的工具类
```java
XxxDTO xxx = ...;
// 将 Java 对象转为 JSONObject 对象
JSONObject jsonObject = FastJsonUtils.toJSONObject(xxx);

List<XxxDTO> xxxList = ...;
// 将 Java 对象（List 或 array）转为 JSONArray 对象
JSONArray jsonArray = FastJsonUtils.toJSONArray(xxxList);
...
```
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
```java
@JsonSerialize(using = BigDecimal2BitsSerializer.class)
private BigDecimal amount;
```
#### BigDecimal4BitsSerializer BigDecimal使用jackson序列化保留四位小数
```java
@JsonSerialize(using = BigDecimal4BitsSerializer.class)
private BigDecimal amount;
```
#### JacksonUtils Jackson工具类

### lang
#### BooleanUtils 布尔工具类，扩展commons-lang3的BooleanUtils
```java
// 是否为true或null
BooleanUtils.isTrueOrNull(b);
// 是否为flase或null
BooleanUtils.isFalseOrNull(b);
```
#### CompareUtils 比较工具类，支持多参数级联比较，数组内比较，集合内比较
```java
// 是否相同
CompareUtils.isEquivalent(1, 1, 1, 1);
// 是否递增，每一个元素都比前一个大
CompareUtils.isIncreasing(1, 2, 3, 4);
// 是否递增，每一个元素都和前一个元素相同或比前一个元素大
CompareUtils.isIncreasingOrEquivalent(1, 2, 2, 3);
// 是否递减，每一个元素都比前一个小
CompareUtils.isDecreasing(4, 3, 2, 1);
// 是否递增减，每一个元素都和前一个元素相同或比前一个元素小
CompareUtils.isDecreasingOrEquivalent(3, 2, 2, 1);
```
#### EnhancedEqualsUtils 增强型的比较相等工具类
```java
// 下面都会返回true
EnhancedEqualsUtils.equals(1, 1L);
EnhancedEqualsUtils.equals("1", '1');
EnhancedEqualsUtils.equals("1", 1L);
EnhancedEqualsUtils.equals(new int[] {1, 2, 3}, new long[] {2L, 3L, 1L});
EnhancedEqualsUtils.equals(Stream.of(1, 2, 3).collect(Collectors.toList()), Stream.of(3, 2, 1).collect(Collectors.toSet());
EnhancedEqualsUtils.equals(1, 1L, '1', "1");
```
#### EnumGetter 枚举工具类
```java
@Getter
public enum XxxEnum {
    READY(0, "就绪"),
    RUNNING(1, "运行中"),
    FINISH(2, "完成");
    int status;
    String description;
    XxxEnum(int status, String description) {
        this.status = status;
        this.description = description;
    }
}
// 根据 status 获取枚举对象
XxxEnum xxx = EnumGetter.get(XxxEnum.class, XxxEnum::getStatus, 1);
// 返回 RUNNING

// 根据 status 获取 description
String description = EnumGetter.getEnumPropertyValue(XxxEnum.class, XxxEnum::getStatus, 2, XxxEnum::getDescription);
// 返回 完成

// 根据 status 获取 description status不存在
String description = EnumGetter.getEnumPropertyValue(XxxEnum.class, XxxEnum::getStatus, 3, XxxEnum::getDescription)
// 返回 null

// 根据 status 获取 description status不存在 设置默认值
String description = EnumGetter.getEnumPropertyValue(XxxEnum.class, XxxEnum::getStatus, 4, XxxEnum::getDescription, "未知");
// 返回 未知
```
#### LooseEnumGetter 宽松的枚举工具类，和EnumGetter的区别是更宽松，查找key基于toString，解决了某些enum的属性类型是Long，用Integer或String查询不到的情况
```java
@Getter
public enum XxxEnum {
    READY(0L, "就绪"),
    RUNNING(1L, "运行中"),
    FINISH(2L, "完成");
    Long status;
    String description;
    XxxEnum(Long status, String description) {
        this.status = status;
        this.description = description;
    }
}
// 根据 status 获取枚举对象
XxxEnum xxx = EnumGetter.get(XxxEnum.class, LooseEnumGetter::getStatus, 1L);
// 返回 RUNNING

// 根据 status 获取 description
String description = LooseEnumGetter.getEnumPropertyValue(XxxEnum.class, XxxEnum::getStatus, 2, XxxEnum::getDescription);
// 返回 完成

// 根据 status 获取 description status不存在
String description = LooseEnumGetter.getEnumPropertyValue(XxxEnum.class, XxxEnum::getStatus, "3", XxxEnum::getDescription)
// 返回 null

// 根据 status 获取 description status不存在 设置默认值
String description = LooseEnumGetter.getEnumPropertyValue(XxxEnum.class, XxxEnum::getStatus, 4, XxxEnum::getDescription, "未知");
// 返回 未知
```
#### Range 范围
```java
Range<Integer> range = new Range<>(1, true, 10, true);
range.contains(0); // false
range.contains(1); // true
range.contains(5); // true
range.contains(10); // true
range.contains(11); // false
range.hasIntersection(new Range<>(-10, true, 0, true)); // false
range.hasIntersection(new Range<>(-10, true, 1, true)); // true
range.hasIntersection(new Range<>(-10, true, 2, true)); // true
range.hasIntersection(new Range<>(1, true, 10, true)); // true
range.hasIntersection(new Range<>(2, true, 9, true)); // true
range.hasIntersection(new Range<>(9, true, 15, true)); // true
range.hasIntersection(new Range<>(10, true, 15, true)); // true
range.hasIntersection(new Range<>(11, true, 15, true)); // false
range.hasIntersection(new Range<>(-10, true, 1, false)); // false
range.hasIntersection(new Range<>(10, false, 15, false)); // false
range.hasIntersection(new Range<>(-10, true, 15, true)); // true
```
#### WeakTypeUtils 弱类型工具类
```java
WeakTypeUtils.toBoolean(null); // false
WeakTypeUtils.toBoolean(false); // false
WeakTypeUtils.toBoolean(true); // true
WeakTypeUtils.toBoolean(0); // false
WeakTypeUtils.toBoolean(1); // true
WeakTypeUtils.toBoolean(""); // false
WeakTypeUtils.toBoolean("a"); // true
WeakTypeUtils.toBoolean(new Object()); // true
WeakTypeUtils.toBoolean(new int[0]); // true
```

### map
#### HashMapBuilder 快速构建HashMap
```java
Map<String, Integer> map1 = HashMapBuilder.build("a", 1);
Map<String, String> map2 = HashMapBuilder.build("a", "1", "b", "2");
...
```

### mask
#### MaskUtils 掩码脱敏工具类
```java
String phone = "18511112222";
// 前面保留3位 后面保留4位
int front = 3, rear = 4;
// 用 '*' 打码
String maskPhone = MaskUtils.mask(phone, front, rear, '*');
// 输出 185****2222
```

### math
#### NumberUtils 数值工具类，返回Optional，解析失败不会抛异常，除非越界
```java
Optional<Long> aLong = NumberUtils.parseLong("123456");
Optional<Integer> aInteger = NumberUtils.parseInteger("-123456");
Optional<Integer> aBigInteger = NumberUtils.parseBigInteger("123456");
Optional<Double> aDouble = NumberUtils.parseDouble("123456.78");
Optional<Double> aFloat = NumberUtils.parseFloat("-123456.78");
Optional<Double> aBigDecimal = NumberUtils.parseBigDecimal("123456.78");
```

#### CalculationUtils 计算工具类，支持多个数值、数组、集合，便于对超过2个数的计算，对null友好
```java
// 加法
BigDecimal add = CalculationUtils.add(BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.TEN);
BigDecimal add = CalculationUtils.add(array);
BigDecimal add = CalculationUtils.add(list);

// 减法
BigDecimal substract = CalculationUtils.substract(BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO);
BigDecimal substract = CalculationUtils.substract(array);
BigDecimal substract = CalculationUtils.substract(list);

// 乘法
BigDecimal multiple = CalculationUtils.multiple(BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO);
BigDecimal multiple = CalculationUtils.multiple(array);
BigDecimal multiple = CalculationUtils.multiple(list);

// 除法
BigDecimal divide = CalculationUtils.divide(4, RoundingMode.HALF_UP, BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.TEN);
BigDecimal divide = CalculationUtils.divide(4, RoundingMode.HALF_UP, array);
BigDecimal divide = CalculationUtils.divide(4, RoundingMode.HALF_UP, list);

// 除法 四舍五入保留两位小数
BigDecimal divide = CalculationUtils.divide(BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.TEN);
BigDecimal divide = CalculationUtils.divide(array);
BigDecimal divide = CalculationUtils.divide(list);
```

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
```java
// 下载地址
String url = "https://xxx";
// 超时时间 10秒
int readTimeout = 10 * 1000;
// 是否优先读取原文件名
boolean useSourceName = true;
// 下载 返回的key为文件名 value为内容 其中文件名可以生成并匹配内容的格式
// 比如 http://a.com/pic/001 最终文件名可能是001.png
Map.Entry<String, ByteBuffer> download = DownloadUtils.download(url, readTimeout, useSourceName);
String fileName = download.getKey();
ByteBuffer fileContent = download.getValue();
```
#### IpAddressUtils IP地址的工具类
```java
// 获取本机IP地址，用于对外（比如注册中心）暴露自己的服务
String localIp = IpAddressUtils.getLocalIp();
```

### random
#### UuidUtils UUID的工具类
```java
// 生成一个 UUID 并去掉其中的 '-'
String uuid = UuidUtils.uuid();
// 生成一个 UUID 并进行 base58 优化
String base58Uuid = UuidUtils.base58Uuid();
```

### regex
#### RegexUtils 正则工具类
```java
// 转义正则
Strint str = RegexUtils.escape(".*");
// 返回 \.\*
```

### stream
#### ForceToMapCollector 实现将流强制收集为map
```java
int[] arr = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
Map<Integer, Boolean> map = Arrays.stream(arr).boxed().collect(ForceToMapCollector.collect(Functional.identity(), x -> x % 2 == 0));
System.out.println(map);

Stream<String> stream = Arrays.stream("a", "bc", "def", "ghij", "klmno");
Map<Integer, String> countMap = stream.collect(ForceToMapCollector.collect(String::length));
System.out.println(countMap);
```
#### RandomKCollector 实现RandomK的collector
```java
int[] arr = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
List<Integer> list = Arrays.stream(arr).boxed().collect(RandomKCollector.collect(6));
System.out.println(list);
```
#### StreamUtils 流的工具类
```java
List<String> list1 = Arrays.asList("1", "2");
List<String> list2 = Arrays.asList("a", "b", "c");
// 支持多个流的笛卡尔积
Stream<String> stream = StreamUtils.cartesianProduct((String s1, String s2) -> s1 + s2, list1::stream, list2::stream)
List<String> list = stream.collect(Collectors.toList());
// list = ["1a", "1b", "1c", "2a", "2b", "2c"]
```

### string
#### SplitUtils 字符串分割工具类
```java
String str = "1!2@3#4$5%6^7&8*9(0)";
List<Integer> list = SplitUtils.splitToIntegerList(str);
// list = [1, 2, 3, 4, 5, 6, 7, 8, 9, 0]
```

### validator
#### BaseValidator 基础校验器
```java
XxxAddDTO dto = ...;
// 校验对象
List<String> messages = BaseValidator.validate(dto);
if (!messages.isEmpty()) {
    // 这里表示校验失败了，messages就是所有注解上的错误信息
}
```

### 未分包
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
| spring               | 5.2.25.RELEASE | 只用到了里面集成的cglib        |
| fastjson             | 1.2.83         |                       |
| jackson              | 2.14.3         |                       |
| commons-lang3        | 3.11           |                       |
| commons-collections4 | 4.4            |                       |
| guava                | 32.0.1.0-jre   |                       |
| slf4j                | 1.7.30         |                       |
| hibernate-validator  | 6.1.6.Final    |                       |
| tika                 | 2.6.0          | 只用到了预测文件类型            |
| easyexcel            | 2.2.7          | alibaba出品的简易excel解析工具 |
| zstd                 | 1.5.2-4        | google出品的压缩工具         |
| snappy               | 1.1.10.1       | facebook出品的压缩工具       |
| servlet-api          | 3.1.0          | servlet相关依赖           |
| lombok               | 1.18.16        |                       |

