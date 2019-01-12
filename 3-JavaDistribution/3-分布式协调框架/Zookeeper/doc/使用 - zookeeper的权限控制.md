###ACL--保障数据的安全
ACL全称为Access Control List（访问控制列表），用于控制资源的访问权限。zk利用ACL策略控制节点的访问权限，如节点数据读写、节点创建、节点删除、读取子节点列表、设置节点权限等。

znode的ACL是没有继承关系的，每个znode的权限都是独立控制的，只有客户端满足znode设置的权限要求时，才能完成相应的操作。

Zookeeper的ACL，分为三个维度：**scheme、id、permission**

通常表示为：scheme:id:permission，schema代表授权策略，id代表用户，permission代表权限。

id是验证模式，不同的scheme，id的值也不一样。


### scheme
scheme即采取的授权策略，每种授权策略对应不同的权限校验方式。

下面是zk常用的几种scheme：
#####1. digest
语法：digest:username:BASE64(SHA1(password)):cdrwa 
	digest：是授权方式 
	username:BASE64(SHA1(password))：是id部分 
	cdrwa：权限部份 

用户名+密码授权访问方式，也是常用的一种授权策略。

id部份是用户名和密码做sha1加密再做BASE64加密后的组合。

比如，

	## 创建节点/node_05
	shell> create /node_05 data
	Created /node_05
	## 设置权限
	shell> setAcl /node_05 digest:yangxin:ACFm5rWnnKn9K9RN/Oc8qEYGYDs=:cdrwa
	cZxid = 0x8e
	ctime = Mon Nov 14 21:38:52 CST 2016
	mZxid = 0x8e
	mtime = Mon Nov 14 21:38:52 CST 2016
	pZxid = 0x8e
	cversion = 0
	dataVersion = 0
	aclVersion = 1
	ephemeralOwner = 0x0
	dataLength = 3
	numChildren = 0
	## 获取节点刚刚设置的权限
	shell> getAcl /node_05
	'digest,'yangxin:ACFm5rWnnKn9K9RN/Oc8qEYGYDs=
	: cdrwa
	 
	## 没有授权，创建节点失败
	shell> create /node_05/node_05_01 data
	Authentication is not valid : /node_05/node_05_01
	 
	## 添加授权信息
	shell> addauth digest yangxin:123456
	 
	## 添加授权信息后，就可以正常操作了
	shell> create /node_05/node_05_01 data
	Created /node_05/node_05_01


#####2. IP

基于客户端IP地址校验，限制只允许指定的客户端能操作znode。 
比如，设置某个节点只允许IP为192.168.1.100的客户端能读写该写节点的数据：ip:192.168.1.100:rw

#####3. world
语法：world:anyone:cdrwa 

创建节点默认的scheme，所有人都可以访问。

上面主要介绍了平时常用的三种scheme，除此之外，还有host、super（管理员超级用户）、auth授权策略。

#### permission
在介绍scheme的时候，提到了acl的权限，
如：digest:username:BASE64(SHA1(password)):cdrwa中的cdrwa即是permission。 

	1> CREATE(c)：创建子节点的权限 
	2> DELETE(d)：删除节点的权限 
	3> READ(r)：读取节点数据的权限 
	4> WRITE(w)：修改节点数据的权限 
	5> ADMIN(a)：设置子节点权限的权限

注意：cd权限用于控制子节点，rwa权限用于控制节点本身