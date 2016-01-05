## deerlet-redis-client
deerlet是一个简单易用的redis for java客户端，支持与spring无缝集成。

## 特性
==========================================================
1，轻量级，jar包只有25kb。<br/>
2，简单易用，无侵入性。<br/>
3，可以与当前流行的ioc容器spring无缝集成。<br/>
4，API完全与redis命令保持一致，减少学习成本。<br/>
5，支持集群，采用一致性hash策略。<br/>

## maven依赖坐标
\<dependency\><br/>
    \<groupId\>com.zuoxiaolong\</groupId\><br/>
    \<artifactId\>deerlet-redis-client\</artifactId\><br/>
    \<version\>1.1\</version\><br/>
\</dependency\><br/>

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
\<bean id="deerletRedisClient" class="com.zuoxiaolong.deerlet.redis.client.spring.DeerletRedisClientFactoryBean"\><br/>
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

## 目前支持的命令
del,dump,exists,expire,expireat,keys,migrate,move,object,persist,pexpire<br/>
,pexpireat,pttl,randomkey,rename,renamenx,restore,sort,ttl,type,scan<br/>
,append,bitcount,bitop,decr,decrby,get,getbit,getrange,getset,incr,incrby<br/>
,incrbyfloat,mget,mset,msetnx,psetex,set,setbit,setex,setnx,setrange,strlen<br/>
,lindex,linsert,llen,lpop,lpush,lpushx,lrange,lrem,lset,ltrim<br/>
,select,bgrewriteaof,bgsave,dbsize,flushall,flushdb<br/>
...正在陆续添加中...欢迎您来一起加入

## 如何加入我们
1, 首先安装一下git，并将代码clone到本地，切换到1.X分支<br/>
    git clone git@github.com:xiaolongzuo/deerlet-redis-client.git   //下载源码<br/>
    git -c core.quotepath=false checkout -b branch-1.x  　　//切换到1.x分支<br/>
2, 在本地电脑上下载并安装maven，或使用eclipse的maven插件都可以，因为deerlet的构建工具是maven<br/>
3, 在本地电脑上安装redis，并启动三个redis实例，端口分别为6379,6479,6666<br/>
4, redis启动以后，运行mvn test命令看是否能够通过单元测试<br/>
