## deerlet-redis-client
deerlet是一个简单易用的redis for java客户端，支持与spring无缝集成。

## 特性
==========================================================
1，轻量级，jar包只有25kb。<br/>
2，简单易用，无侵入性。<br/>
3，可以与当前流行的ioc容器spring无缝集成。<br/>
4，API完全与redis命令保持一致，减少学习成本。<br/>
5，支持集群，采用一致性hash策略。<br/>

## 使用示例（直接使用）

### 1.在classpath（比如eclipse中src下）路径下添加以下文件，名为deerlet.properties
==========================================================
\#该属性为redis服务器ip和端口，支持集群，以逗号隔开<br/>
address=localhost:6379<br/>

### 2.使用以下编程式的方式即可使用deerlet
```java
Configuration configuration = ConfigurationFactory.create().loadConfiguration();
ConnectionFactory connectionFactory = new ConnectionFactoryImpl(configuration);
DeerletRedisClient deerletRedisClient = new DeerletRedisClientImpl(connectionFactory);//获取client对象

deerletRedisClient.set("testKey","testValue");//存储一个键为testKey，值为testValue的键值对
System.out.println(deerletRedisClient.get("testKey"));//获取
System.out.println(deerletRedisClient.dbSize());//查看大小
deerletRedisClient.flushAll();//刷新
System.out.println(deerletRedisClient.dbSize());//查看大小
```

## 使用示例（与spring集成）

### 1.在spring的配置文件中加入如下bean定义
==========================================================
\<bean id="deerletRedisClient" class="cn.zxl.deerlet.redis.client.spring.DeerletRedisClientFactoryBean"\><br/>
\</bean\><br/>

### 2.在classpath（比如eclipse中src下）路径下添加以下文件，名为deerlet.properties
==========================================================
\#该属性为redis服务器ip和端口，支持集群，以逗号隔开<br/>
address=localhost:6379<br/>

### 3.使用以下编程式的方式即可使用deerlet
```java
ApplicationContext applicationContext = new FileSystemXmlApplicationContext("
classpath:applicationContext.xml");//初始化spring容器
DeerletRedisClient deerletRedisClient = applicationContext.getBean(DeerletRedisClient.class);//获取client对象

deerletRedisClient.set("testKey","testValue");//存储一个键为testKey，值为testValue的键值对
System.out.println(deerletRedisClient.get("testKey"));//获取
System.out.println(deerletRedisClient.dbSize());//查看大小
deerletRedisClient.flushAll();//刷新
System.out.println(deerletRedisClient.dbSize());//查看大小
```

## deerlet.properties文件属性说明
address:必选，格式为 host1:port1,host2:port2....<br/>
initSize:可选，指定连接池初始连接数,默认为10<br/>
maxSize:可选，指定连接池最大连接数,默认为100<br/>
minIdleSize:可选，指定连接池最小空闲连接数,默认为10<br/>
maxIdleSize:可选，指定连接池最大空闲连接数,默认为20<br/>