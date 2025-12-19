/**
 * hessian 序列化方式是 dubbo 的默认序列化方式，相对于 protobuf 的学习和实现成本更低。
 * 当然性能上是比 protobuf 差一点，比 json 高很多。
 * 底层利用的是 Java 的反射/序列化机制实现
 *
 * @author DD
 */

package com.xiaozhi.demo.serialization.hessian;