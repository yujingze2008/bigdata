# 排序算法

## 定义

假设含有 n 个记录的序列为 {r1, r2, ..., rn}，其相应的关键字分别为 {k1, k2, .., kn}，需确定 1, 2, ...,n 的一种排列 p1, p2, ..., pn，使其相应的关键字满足 kp1 <= kp2 <= ... <= kpn 非递减（或非递增）关系，即使得序列成为一个按关键字有序的序列 {rp1, rp2, ..., rpn}，这样的操作就称为排序。

### 相关属性

**稳定性**

指的是关键字相等的两个记录，在排序后依然保持排序前的相对顺序。

**性能影响因素**

时间性能、算法实现复杂度

## 冒泡排序

两两比较相邻的关键字，如果反序则交换，直到没有反序的记录为止。
