### 阿里云oss上传插件，用于上传cdn文件（js、css、图片等）


example1, 上传源码路径
```xml
<plugin>
    <groupId>com.fengjx.maven.cdn</groupId>
    <artifactId>maven-cdn-alioss-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <configuration>
        <endpoint>http://oss-cn-shenzhen.aliyuncs.com</endpoint>
        <accessKeyId>xxxxx</accessKeyId>
        <accessKeySecret>xxxxxx</accessKeySecret>
        <!-- 上传的Bucket名称 -->
        <bucketName>bucketName</bucketName>
        <!-- oss路径前缀 -->
        <prefix>blog/assets</prefix>
        <!-- false:打印日志, true:隐藏日志 -->
        <quiet>false</quiet>
        <includes>
            <include>**/src/main/webapp/js/**</include>
            <include>**/src/main/webapp/css/**</include>
            <include>**/src/main/webapp/images/**</include>
            <include>**/src/main/webapp/skins/**</include>
        </includes>
        <excludes>
            <exclude>**/src/main/webapp/skins/**/*.ftl</exclude>
            <exclude>**/src/main/webapp/skins/**/*.properties</exclude>
        </excludes>
    </configuration>
</plugin>
```

```bash
mvn cdn-alioss:upload
```



example2, 上传编译路径
```xml
<plugin>
    <groupId>com.fengjx.maven.cdn</groupId>
    <artifactId>maven-cdn-alioss-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <configuration>
        <endpoint>http://oss-cn-shenzhen.aliyuncs.com</endpoint>
        <accessKeyId>xxxx</accessKeyId>
        <accessKeySecret>xxxxx</accessKeySecret>
        <!-- 上传的Bucket名称 -->
        <bucketName>bucketName</bucketName>
        <!-- oss路径前缀 -->
        <prefix>blog/assets</prefix>
        <!-- false:打印日志, true:隐藏日志 -->
        <quiet>false</quiet>
        <baseDir>${project.build.directory}/${project.build.finalName}</baseDir>
        <includes>
            <include>**/js/**</include>
            <include>**/css/**</include>
            <include>**/images/**</include>
            <include>**/skins/**</include>
        </includes>
        <excludes>
            <exclude>**/skins/**/*.ftl</exclude>
            <exclude>**/skins/**/*.properties</exclude>
        </excludes>
    </configuration>
</plugin>
```

```bash
mvn package cdn-alioss:upload
```
