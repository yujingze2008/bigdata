# 一致性 hash

## 背景

为了解决简单的 hash 在增删节点时带来的问题——可能要对所有的数据进行重新分配。

## 基本实现

构建环形 hash 值空间，将节点映射在环上，这样在对数据做 hash 运算时，将结果按照顺时针找到最近的节点即可。

![](../img/distribute/consistent-hashing.jpg)

此时，
删除节点时：将属于该节点的数据复制到下一个节点即可，不影响其他数据。
增加节点时：将下一个节点的数据的一部分复制给新节点，不影响其他数据。


## 新问题

由于 hash 算法并不保证平衡，那么如何解决热点问题——大部分数据集中在同一个节点？
**解决**： 引入虚拟节点，这样可以缓解即使节点较少导致的数据倾斜问题，在实际应用中，通常将虚拟节点数设置为32甚至更大。

## 扩展

一致性hash算法提出了在动态变化的Cache环境中，判定哈希算法好坏的四个定义：

### 平衡性(Balance)
平衡性是指哈希的结果能够尽可能分布到所有的缓存中去，这样可以使得所有的缓存空间都得到利用。很多哈希算法都能够满足这一条件。

上面的增加虚拟节点就是解决的平衡性问题。

### 单调性(Monotonicity)
单调性是指如果已经有一些内容通过哈希分派到了相应的缓存中，又有新的缓存加入到系统中。哈希的结果应能够保证原有已分配的内容可以被映射到原有的或者新的缓存中去，而不会被映射到旧的缓存集合中的其他缓存区。 

因此一致性哈希主要解决的是单调性问题。

### 分散性(Spread)
在分布式环境中，终端有可能看不到所有的缓存，而是只能看到其中的一部分。当终端希望通过哈希过程将内容映射到缓存上时，由于不同终端所见的缓存范围有可能不同，从而导致哈希的结果不一致，最终的结果是相同的内容被不同的终端映射到不同的缓存区中。这种情况显然是应该避免的，因为它导致相同内容被存储到不同缓冲中去，降低了系统存储的效率。分散性的定义就是上述情况发生的严重程度。好的哈希算法应能够尽量避免不一致的情况发生，也就是尽量降低分散性。 

### 负载(Load)
负载问题实际上是从另一个角度看待分散性问题。既然不同的终端可能将相同的内容映射到不同的缓冲区中，那么对于一个特定的缓冲区而言，也可能被不同的用户映射为不同 的内容。与分散性一样，这种情况也是应当避免的，因此好的哈希算法应能够尽量降低缓冲的负荷。
