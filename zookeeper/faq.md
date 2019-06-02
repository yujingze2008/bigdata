# Zookeeper 常见问题

#### 1. Zookeeper 集群的各种角色？

##### Leader

- 事务请求的唯一调度和处理者，保证集群事务处理的顺序性
- 集群内部各服务的调度者

##### Follower

- 处理客户端的非事务请求，转发事务请求给 Leader 服务器
- 参与事务请求 Proposal 的投票
- 参与 Leader 选举投票

##### Observer

3.3.0 版本以后引入的一个服务器角色，在不影响集群事务处理能力的基础上提升集群的非事务处理能力。

- 处理客户端的非事务请求，转发事务请求给 Leader 服务器
- 不参与任何形式的投票

#### 2. Zookeeper 节点分类？

##### 持久节点

一旦创建，会一直存在于 Zookeeper 服务器上，直到被主动删除。

##### 临时节点

临时节点的生命周期和客户端的会话是绑定在一起的，如果客户端会话失效，该节点则会被自动清理掉。另外，临时节点不能创建子节点。

##### 顺序节点

**持久节点和临时节点都可以成为顺序节点**，其特性表现在顺序性上。每个父节点会为它的第一级子节点维护一份顺序，用于记录每个节点创建的先后顺序。基于这个顺序特性，在创建子节点的时候，可以设置这个标记，在创建节点的过程中，Zookeeper 会自动为其加上一个数字后缀，作为一个新的、完整的节点名。

#### 3. Watcher 机制的特性？

* 一次性

  一旦一个 Watcher 被触发，Zookeeper 就会将其从相应的存储中移除，所以使用时要反复注册。

* 客户端串行执行

  Watcher 回调的过程是一个串行执行的过程，从而保证了顺序。

* 轻量

  1. Watcher 通知非常简单，只会告诉客户端发生了事件，而不会说明事件的具体内容。

  2. 客户端向服务端注册 Watcher 的时候，并不会把客户端真实的 Watcher 对象实体传递到服务端，仅仅是在客户端请求中使用 boolean 类型属性进行了标记。

#### 4. Zookeeper 的应用场景 

##### 发布/订阅

1. 应用启动时主动去 Zookeeper 获取配置信息，并注册一个 Watcher 监听
2. 配置发生变更时，Zookeeper 服务端会通知所有订阅的客户端
3. 客户端收到通知后，主动到服务端获取最新数据。

##### 全局唯一 ID

利用顺序节点的特性，创建一个顺序节点，根据返回的完整节点名，作为 ID 即可。

##### Master 选举

1. 开始选举，即所有机器都作为 Zookeeper 向 Zookeeper 集群请求创建一个临时节点，成功创建该节点的机器，成为 Master。
2. 其他创建失败的客户端对应的机器，都会在该节点上注册一个 Watcher，用于监控当前的 Master 机器是否存活。
3. 一旦发现当前的 Master 挂了（即收到 Watcher 事件通知节点被删除），则重新选举。

##### 分布式锁

**排他锁**

和 Master 选举类似，利用临时节点的特性。

创建临时节点成功的客户端获得锁，客户端意外挂掉则临时节点被自动删除或者是正常结束业务逻辑然后主动删除节点，此时 Zookeeper 会通知其他客户端再次争夺锁。

**共享锁**

利用顺序节点的特性。

对于读写请求，分别创建临时顺序节点。下面重要的是判断读写顺序——对于读请求，如果有比自己序号小的写请求，则进入等待，否则执行读取逻辑；对于写请求，如果自己不是最小的序号，则进入等待。