  自认为对计算机很有天赋，然后并没有什么卵用，不管Java也好，C++也好，PHP也好，做过或者积累的东西能丢的基本都丢了，该从现在开始把这些东西记录下来了，一来是方便自己平时开发，二来可供小伙伴们学习参考。

# nirvana 涅槃
叫涅槃呢有两层意思，一是引里边讲的自个该涅槃重生了，二是，这个项目里会放所有基础的东西，包括Java自身，还有其他各种中间件的代码，会不断的完善。

# 目前有的功能
1. logback  通过Spring初始化logback，而不是web.xml中的listener
   优势：单元测试logback正常输出；不同环境可根据maven编译，取不同环境的配置文件
2. mybatis  主要是自动生成Mybatis-generator-plugin的Example查询类，省去手动编码
3. page 主要是分页的东西
4. parametertype 主要是java Type接口相关内容的例子，包含一个工具类，获取最终的泛型
5. reflect  反射相关工具类，反射是个好东西，可以简化很多重复性劳动
6. type 自个实现的简单Java类型转换等功能，深入学习Spring时，才知道Spring-core中的converter貌似很强大的样子，不管了，自个写得用着舒服
7. verify 校验工具类，基于自定义注解，功能灵活，学习Spring时，才知道原来Java已经定义了一套标准校验接口，也不管了，自个写的用着舒服
8. web  主要是对Web请求参数、返回值处理的东西。包括ServletWrapper、Spring ArgumentResolver等等
100. 目前还没到这呢，再补充
