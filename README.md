OAuth
=

利用spring-security实现的权限控制


<!-- TOC -->

- [使用说明](#使用说明)
    - [controller](#controller)
    - [resources](#resources)
    - [application.yml](#applicationyml)
- [依赖](#依赖)
    - [maven dependency](#maven-dependency)
    - [gradle](#gradle)

<!-- /TOC -->


#### 使用说明

##### controller

```java
//在需要进行权限的控制的地方加@PreAuthorize注解
//具体如何使用此注解请自行查阅资料

//并且Mapping最后的结尾为"/kkn"
//则会进行是否授权的校验
@RestController
public class controller
{
    @PreAuthorize("hasRole('user')")
    @GetMapping("/test/kkn")
    public Object test()
    {
        return "test/kkn";
    }

    @GetMapping("/test")
    public Object test1()
    {
        return "test";
    }
}
```

##### resources

```markdown
在maven项目的resources目下要有jwt.jks文件
resources
|---application.yml
└---jwt.jks
```

##### application.yml

```yaml
配置文件如下填写

luomingxu:
  oauth:
    aes-conf:
      aes-pwd: 123456 #不可为null
    jwt-token-expiration: 7 #单位为天; 默认为7天, 可不填
    jwt-pwd: 123456 #不可为null
```

#### 依赖
##### maven dependency
```xml
<dependency>
    <groupId>com.github.luomingxuorg</groupId>
    <artifactId>oauth-boot-starter</artifactId>
     <version>$version</version>
</dependency>
```
##### gradle
```grovvy
compile 'com.github.luomingxuorg:oauth-boot-starter:$version'
```