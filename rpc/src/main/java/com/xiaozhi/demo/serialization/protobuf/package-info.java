/**
 * @author DD
 */



/*
1.proto 语法：
synax 声明使用的 proto 版本
声明变量：<类型> <字段名> = <字段编号（也可以叫做标识号，此字符编号用作标识并非值）>;

proto关键字：
    - message：定义消息类型，可以包含字段。
    - enum：枚举类型，用于定义枚举值。例如：
        ```proto
        enum Color {
            WHILE = 0;
            BLACK = 1;
        }
        ```
    - option：设置消息或字段的选项，如：是否为必填项、默认值等。
    - package: 定义包名。
    - import：导入其他 proto 文件，可以通过导入其他的 proto 文件来公用类型。
    - reserved：定义被保留的字段编号或字段名。

proto 的数据类型：
基础数据类型：
    - int32：对应Java里的int整数型，默认值0；
    - int64：对应Java里的long长整型，默认值0；
    - float：对应Java里的float浮点型，默认值0；
    - double：对应Java里的double双精度浮点型，默认值0；
    - bool：对应Java里的boolean布尔型，默认值false；
    - string：对应Java里的String字符串，默认值空字符串；
    - bytes：对应Java里的ByteString类型；
    
    整数类型前面有s表示有符号，u表示无符号。
    
- proto 复杂类型：
    - required：必传字段，必须赋值，只能赋单值，多次赋值会覆盖上一次的值；（proto3中不支持required）
    - optional：可选字段，可以不赋值，同样为单值，赋值规则同上；
    - repeated：定义一个消息中必须设置一个字段。类似 java 的 List
    - map：kv键值对。类似 java 的 Map
    例子：
    message Person {
        required string name = 1;
        optional int32 age = 2;
        repeated string email = 3;
        map<string, string> map = 4;
    }

2 protoBuf高级特性
2.1 oneof关键字
在 oneof 关键字包括的变量只能使用其中一个，其他的变量会被自动清除。例如：
```proto
message xxx {
    oneof x {
        unit32 id = 1;
        uint32 serial_number = 2;
    }
    string name = 3;
}
```
上面在 x 中只能使用 id 或 serial_number 一个字段，使用了其中一个字段，另外的一个就会被清除。

2.2 Any类型
在 propt 中不支持很多语言的复杂类型，比如 java 中的 Map<String, List<Map<String, Integer>>> 这样的嵌套复杂类型，
所以为了兼容这些复杂类型，在 proto 中有一个万用类型，就是 Any 类型。无论任何的类型都可以赋给 Any 类型的变量。
例如：
```proto
syntax = "proto3";
// 引入 Any 类型的包
import "google/protobuf/any.proto";
message MyMessage {
  int32 id = 1;
  google.protobuf.Any data = 2;
}
```

2.3 指定可使用字段的范围
在 java 中我们不想别人使用定义的变量可以使用 private 来标识这个变量，那么在 proto 也是支持该操作的，
我们可以通过 reserved 关键字来指定字段的范围，例如：
```proto
syntax = "proto3";

message Test {
  // 表示保留2、4、5、6、7、8、9、10这些标识号 
  reserved 2,4,5 to 10;
  // 表示保留id、name这两个字段名称
  reserved "id","name";
  
  // 会提示：Field name 'id' is reserved
  int32 id = 3;
  // 会提示：Field 'name' uses reserved number 4
  string name = 4;
}
```


3 继承
结构体之间如何进行复用，proto 可以提供继承的特性让结构体之间进行复用。
3.1 继承的语法：
```proto
message parent {
    string name = 0;
    int32 age = 1;
}
message child {
    extends parent {
        // 在父结构的基础上添加新的字段
        int32 sex = 0;
    }
}

```

3.2 组合的方式使用复用
```proto
message parent {
    string name = 0;
    int32 age = 1;
}
message child {
    parent parent = 0;
    int32 sex = 1;
}

```

 */

package com.xiaozhi.demo.serialization.protobuf;