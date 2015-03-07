# deerlet-redis-client
deerlet是一个简单易用的redis for java客户端，支持与spring无缝集成。

# 特性
==========================================================
1，轻量级，jar包只有25kb。

2，简单易用，无侵入性。

3，可以与当前流行的ioc容器spring无缝集成。

# 使用示例

# 1.在spring的配置文件中加入如下bean定义
==========================================================
<![CDATA[
<bean id="deerletRedisClient" class="cn.zxl.deerlet.redis.client.spring.DeerletRedisClientFactoryBean">
		<property name="connectionPool">
			<bean class="cn.zxl.deerlet.redis.client.connection.pool.ConnectionPoolImpl"></bean>
		</property>
</bean>
]]>

# 2.在classpath（比如eclipse中src下）路径下添加以下文件，名为deerlet.properties
==========================================================
address=localhost

port=6379

# 3.使用以下编程式的方式即可使用deerlet
==========================================================
ApplicationContext applicationContext = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");//初始化spring容器

DeerletRedisClient deerletRedisClient = applicationContext.getBean(DeerletRedisClient.class);//获取client对象

deerletRedisClient.set("testKey","testValue");//存储一个键为testKey，值为testValue的键值对

System.out.println(deerletRedisClient.get("testKey"));//获取

System.out.println(deerletRedisClient.dbSize());//查看大小

deerletRedisClient.flushAll();//刷新

System.out.println(deerletRedisClient.dbSize());//查看大小