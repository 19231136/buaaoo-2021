[TOC]

# oo第三单元总结

## 一、JML理论基础

### 简介

ML(Java Modeling Language) 是用于对 Java 程序进行规格化设计的一种表示语言。JML 是一种行为接口规格语言（Behavior Interface Specification Language，BISL），基于 Larch 方法构建。BISL 提供了对方法和类型的规格定义手段。所谓接口即一个方法或类型外部可见的内容。JML 主要由 Leavens 教授在 Larch 上的工作，并融入了 Betrand Meyer， John Guttag 等人关于 Design by Contract 的研究成果。近年来，JML 持续受到关注，为严格的程序设计提供了一套行之有效的方法。通过 JML 及其支持工具，不仅可以基于规格自动构造测试用例，并整合了 SMT Solver 等工具以静态方式来检查代码实现对规格的满足情况。

### 原子表达式

- `\result`：表示一个非 void 类型的方法执行所获得的结果，即方法执行后的返回值
- `\old(expr)`：表示一个表达式`expr`在相应方法执行前的取值，该表达式涉及到评估`expr`中的对象是否发生变化。
- `\nonnullelements(container)`：表示container对象中存储的对象不会有null

### 量化表达式

- `\forall`：全称量词修饰的表达式，表示对于给定范围内的元素，每个元素都满足相应的约束
- `\exists`：存在量词修饰的表达式，表示对于给定范围内的元素，存在某个元素满足相应的约束
- `\sum`：返回给定范围内的表达式的和

### 操作符

- `b_expr1==>b_expr2`或`b_expr1<==b_expr2`推理操作符：相当于离散的->，只有（1，0）是false
- `\nothing`或`\everthing`变量引用操作符：表示当前作用域访问的所有变量

### 前置条件

- `requires`

### 后置条件

- `ensures`

### 其他

- `public normal_behavior`和`public exception_behavior`为了有效地区分方法的正常功能行为和异常行为。
- `(/*@ pure @ */)`指不会对对象的状态进行任何改变，也不需要提供输入参数，这样的方法无需描述前置条件，也不会有任何副作用，且执行一定会正常结束。有些前置条件可以引用`pure`方法的返回结果
- 关键词`assignable`（表示可赋值）或者`modifiable`（可修改）

## 二、架构设计

### 第一次作业

​	第一次作业只需要根据给出的JML规格完成两个接口和四种异常类。

​	其中值得注意的是以下几点：

​	1.我最初采用了`ArrayList`存储数据。但是后来，在学长学姐们的博客启发下，发现作业中许多的方法涉及查找，而`Hashmap`的查找速度远远快于`ArrayList`的循环遍历，因此使用了`Hashmap`的数据结构

​	2.我的异常计数，是在异常类中使用static变量记录异常数量：

- `static int count`：记录该类异常总发生次数
- `static HashMap<Integer,Integer> hashMap`：以id为key，以该id发生该异常的次数为value

​	3.注意到前边的问题后，我顺利通过强测，但是在互测中由于CPU超时问题被同屋大佬严重针对。问题根源在于`queryBlockSum()`方法以及其中调用的`isCircle()`方法实现比较笨拙。

​		在bug修复阶段，我学习使用了**并查集**，以此降低了`isCircle()`方法的复杂度。

​		实现方法如下：

- `MyNetwork`类中，定义了`Hashmap`类型的`father`，有记忆地记录了每个用户以及其对应的祖先。

- 在`addPerson()`方法中需要新增键值对，此时新用户的祖先为自身

- 在`addRelation()`方法中，需要合并两个人的祖先

- 实现`findFather()`方法，返回祖先

  至此，成功解决了CPU超时的问题，为后边的作业打下了良好的基础。

### 第二次作业

​	第二次作业中新增了`Group`以及`Message`接口，和四个新的异常类。

​	总体而言，第二次作业是最轻松的，这次平安度过了强测和互测。

​	可能值得注意的问题有这些：

- 针对`getValueSum()`,`getAgeVar()`,`getAgeMean()`这些方法，我选择在`MyGroup`类中定义相关属性`valueSum`,`ageSum`,`ageSqSum`，在`addPerson()`中直接实现相关计算并记录
- 其中需要高度警惕，`MyNetwork`中的`addRelation()`方法会对`valueSum`产生影响，需要有相关方法在此时更改`valueSum`                                                                                                                   

### 第三次作业

​	第三次作业新增了三个接口，和两个异常类。

​	第三次作业很好的考察了继承这一面向对象的思想。正是考虑到继承的合理性，大家很快发现了JML规格中的问题。

​	值得注意的问题如下：

- 最短路径：堆优化的Dijkstra

  ```java
  int personCount = 0;
  HashMap<Integer,Path> paths = new HashMap();
  PriorityQueue<Path> candidates = new PriorityQueue<>();
  candidates.add(new Path(0,person1,person1));
  
  while (personCount < queryPeopleSum() && !candidates.isEmpty()) {
      Path path = candidates.poll();
      int distance = path.getDistance();
      MyPerson min = (MyPerson) path.getEnd();
      if (paths.get(min.getId()) != null) {
          continue;
      }
      paths.put(min.getId(), path);
      if (min.getId() == person2.getId()) {
          break;
      }
  
      Iterator<Person> p = min.getAcquaintance().keySet().iterator();
      while (p.hasNext()) {
          Person next = p.next();
          Path path1 = paths.get(next.getId());
          int value = min.queryValue(next);
          if (path1 == null) {
              candidates.add(new Path(distance + value,min,next));
          }
      }
      personCount++;
  }
  ```

- 一个不太机灵的小错误：emojiId和Id傻傻分不清除

## 三、测试

- ### 黑盒测试

  ​	第三次作业由于不太机灵的小错误迟迟没法过中测，在这里真诚感谢zsm大佬的评测姬，帮助心态崩溃的孩子快速定位了问题所在。

- ### 白盒测试

  ​	感谢ljy同学，在研讨课上教会了我Junit的使用方法，但也在这里引用一句他的名言：“IDEA的debug已经很好用了”（手动狗头）

  ​	当然，也自己构造了些特殊的数据，检查了一下自己的程序。

## 四、心得体会

​	诚如老师所言，第三单元比起前两单元难度骤降，看似友好。但友好的同时，也需要自己不能轻敌，否则也是惨不忍睹。

​	在课上首次接触JML时，以为这一单元需要自己编写JML规格。但事实上，只有实验课接触了根据代码编写JML，比想象中要轻松。

​	只要能读懂JML规格，根据给定的JML编写代码其实并不是难事。但是在完成架构实现功能的基础上，也需要进一步思考如何使用更合理的数据结构存储数据，如何使用更好的算法优化功能。就比如使用并查集代替JML文档中的双重循环，如果一味使用JML给定的方法，就算万幸度过强测，也是很难逃过互测房中狼人们的掌心。
