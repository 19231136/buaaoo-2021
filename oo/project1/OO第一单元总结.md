# OO第一单元总结

## 第一次作业

### 一、程序结构分析

​	第一次作业只有幂函数和常数的组合

​	第一次作业只使用MainClass,Term和Poly三个类

![image-20210324143253491](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20210324143253491.png)

​	Term类中有coef和degree两个属性，分别是幂函数的系数和指数。方法Term（String s）是构造方法，getCoef（）和getDegree（）用来获取该项的系数指数。

​	Poly类中属性只有map，以degree为key，以coef为value。其中方法add（）添加Term对象，derivation（）对map中的每一项求导并以字符串输出。

​	第一次作业在Term类尝试使用面向对象的思想，但Poly类本质上仍是面向过程。因此在第二次作业中不得不进行重构。

### 二、遇到的bug

​	强测和互测都是同质错误，由于试图追求表达式简洁，忽略了*1省略1时的乘号出错，强测翻车。

​	hack到的bug：

​	正则表达式爆栈

​	根据形式化表达，表达式、项、带符号整数前最多可出现三个符号，直接使用replaceAll（）替换两个符号导致错误

### 三、心得

​	通过中测不是目的，一定要考虑到每种情况构造样例自己测试。第一次作业出现的bug并非是难以构想的极端情况，只是自己测试不够认真。

## 第二次作业

### 一、程序结构分析

​	![image-20210324181744859](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20210324181744859.png)

​	按层次创建类有：Poly、Term、Factor,其中Factor作为父类，是一个抽象类，其中有抽象方法toString(),derivation()。Factor的子类有Constant,Cos,Sin,Power,Poly,分别重写了抽象方法。

​	Term的求导方法即乘法求导法则，因此Multi类中有方法derivation（），但返回值是String，为后续优化带来很大不便

### 二、遇到的bug

​	强测和互测中都没有被发现bug

​	hack到的bug：

### 三、心得

​	为了首先保证正确性，并没有进行太多优化，性能分比较低。

​	第一次到第二次的跨度是第一单元中最大的。初步接触面向对象，将思维由面向过程转向面向对象的过程是痛苦而挣扎的，但是在第二次作业的时候就意识到，面向对象让我的代码可移植性变强了，面对一些新增的需求只需要对几个类进行微调就可以实现相应的功能。

## 第三次作业

### 一、程序结构分析

![image-20210324191055375](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20210324191055375.png)

​	在第二次作业的基础上 ，为Cos，Sin类引入了factor属性，更新了求导方法

​	试图对求导结果进行优化，使Term类的better()方法返回Hashmap，但优化程度有限，性能依然较弱

### 二、遇到的bug

​	在两处WF判断时出现问题，在强测中挂掉两个点，互测中没有被hack，也没有成功hack别人

​	表达式因子的判断：最后一个字符需要判断")"

​	cos()，sin()内只能出现五种因子中的一种

### 三、心得

​	面向对象的思维让第二次到第三次的过渡轻松了很多