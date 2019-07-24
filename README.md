# 网页设计——基于安卓平台蓝牙控制的爬楼小车

## 导航栏

1. 首页
2. 项目硬件简介与视频演示
3. 项目软件与代码
4. 小组分工
5. 项目感想与反思
6. 小组个人作业（待定）

## 具体说明

1. 首页放置**照片1**

   ![](images/1.jpg)

   （待定或使用目前所给的），标题：基于安卓平台蓝牙控制的爬楼小车

2. 项目简介页：

- 项目介绍：我们组的小车可以通过蓝牙与手机连接，并通过手机App控制小车的进退状况。小车利用升降的原理进行爬楼。小车共有四组轮子，其中三组为主动轮。

  > 然后先放我们的**演示视频**。（暂时留出视频位置）

- 具体说明如下（**2~8.jpg**）：

- 理想状态下一共有8个阶段，其中1号轮子和4号轮子都是带电机的主动轮，其他都是从动轮，而且升降装置装在2号和4号轮子上

- 阶段1 
  前进

- ![](images/img1.jpg)

- 阶段2 

  车体上升，1号和2号升降装置把1号和3号轮子升起来直到越过楼梯

  ![](images/img2.jpg)

- 阶段3 

  1号和4号轮子推动小车前进

  ![](images/img3.jpg)

- 阶段4 ：1号升降装置把2号轮子收回去

![](images/img4.jpg)

-  阶段5
  1号和4号轮子推动小车前进

![](images/img5.jpg)

- 阶段6：主动轮停止转动，2号升降装置把4号轮子收回去

![](images/img6.jpg)

- 阶段7
  1号轮子推动小车前进 也就是阶段1

![](images/img7.jpg)

- 阶段8
  也就是阶段2，小车继续前进

![](images/img8.jpg)

硬件简介：

- arduino单片机：将多个不同功能的模拟电路、数字电路模块和微处理器集成在一个芯片上，以提供“单片机”解决方案。
- HC-06蓝牙模块：用于接收手机发出的操作信号。
- 减速电机
- 普通电机
- 塑料支架、齿条



3. 软件：

- 手机端程序界面如图所示**（10.png）**，其中电机1、2控制小车平台的升降，其余控制小车的前进。

![](images/10.png)

- 手机端程序使用app inventor开发，图形化编程界面如图（11.png）：

![](images/11.PNG)

- 手机源代码程序下载见下方。

- 单片机源代码：

```c
#include <Arduino.h>

//1组轮子--4,5接口
//2组轮子--6,7接口
//3组轮子--8,9接口
//4--2,3 
//升降电机1--10,11
//升降电机2--12,13

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  pinMode(2, OUTPUT);  
  pinMode(3, OUTPUT);
  pinMode(4, OUTPUT);  
  pinMode(5, OUTPUT);
  pinMode(6, OUTPUT);
  pinMode(7, OUTPUT);
  pinMode(8, OUTPUT);
  pinMode(9, OUTPUT);
  pinMode(10, OUTPUT);
  pinMode(11, OUTPUT);
  pinMode(12, OUTPUT);
  pinMode(13, OUTPUT);
  digitalWrite(2, LOW);
  digitalWrite(3, LOW);
  digitalWrite(4, LOW);
  digitalWrite(5, LOW);
  digitalWrite(6, LOW);
  digitalWrite(7, LOW);
  digitalWrite(8, LOW);
  digitalWrite(9, LOW);
  digitalWrite(10,LOW);
  digitalWrite(11,LOW);
  digitalWrite(12,LOW);
  digitalWrite(13,LOW);
}

void loop() {
  // put your main code here, to run repeatedly:
  if(Serial.available()>0){
     int ch = Serial.read();
     switch(ch){
          case 101:allforward();break;//前进按压
          case 102:stop();break;//前进松开
          case 103:allbackword();break;//后退按压
          case 104:stop();break;//前进松开
          case 105:open_dianji1();break;//电机1开         
          case 117:fan_dianji1();break;
          case 106:stop();break;//电机1关
          case 107:open_dianji2();break;//电机2开
          case 118:fan_dianji2();break;
          case 108:stop();break;//电机2关
          case 109:forward1();break;//1前进按压
          case 110:stop();break;//1前进松开
          case 111:forward2();break;//2前进按压
          case 112:stop();break;//2前进松开         
          case 113:forward3();break;//3前进按压
          case 114:stop();break;//3前进松开 
          case 115:forward4();break;//4前进按压
          case 116:stop();break;//4前进松开  
                 
          default:break;
        }
  }
}
void allforward(){            //全速前进
  digitalWrite(2, HIGH);
  digitalWrite(3, LOW);
  digitalWrite(4, HIGH);
  digitalWrite(5, LOW);
  digitalWrite(6, HIGH);
  digitalWrite(7, LOW);
  digitalWrite(8, HIGH);
  digitalWrite(9, LOW);
  digitalWrite(10,LOW);
  digitalWrite(11,LOW);
  digitalWrite(12,LOW);
  digitalWrite(13,LOW);
  }
void allbackword(){            //全速后退
  digitalWrite(2, LOW);
  digitalWrite(3, HIGH);
  digitalWrite(4, LOW);
  digitalWrite(5, HIGH);
  digitalWrite(6, LOW);
  digitalWrite(7, HIGH);
  digitalWrite(8, LOW);
  digitalWrite(9, HIGH);
  digitalWrite(10,LOW);
  digitalWrite(11,LOW);
  digitalWrite(12,LOW);
  digitalWrite(13,LOW);
  }
void forward1(){            //1组前退
  digitalWrite(2, LOW);
  digitalWrite(3, LOW);
  digitalWrite(4, HIGH);
  digitalWrite(5, LOW);
  digitalWrite(6, LOW);
  digitalWrite(7, LOW);
  digitalWrite(8, LOW);
  digitalWrite(9, LOW);
  digitalWrite(10,LOW);
  digitalWrite(11,LOW);
  digitalWrite(12,LOW);
  digitalWrite(13,LOW);
  }
  
void forward4(){            //4组前退
  digitalWrite(2, HIGH);
  digitalWrite(3, LOW);
  digitalWrite(4, LOW);
  digitalWrite(5, LOW);
  digitalWrite(6, LOW);
  digitalWrite(7, LOW);
  digitalWrite(8, LOW);
  digitalWrite(9, LOW);
  digitalWrite(10,LOW);
  digitalWrite(11,LOW);
  digitalWrite(12,LOW);
  digitalWrite(13,LOW);
  }
void forward2(){            //2组前进
  digitalWrite(2, LOW);
  digitalWrite(3, LOW);
  digitalWrite(4, LOW);
  digitalWrite(5, LOW);
  digitalWrite(6, HIGH);
  digitalWrite(7, LOW);
  digitalWrite(8, LOW);
  digitalWrite(9, LOW);
  digitalWrite(10,LOW);
  digitalWrite(11,LOW);
  digitalWrite(12,LOW);
  digitalWrite(13,LOW);
  }
void forward3(){            //3组前进
  digitalWrite(2, LOW);
  digitalWrite(3, LOW);
  digitalWrite(4, LOW);
  digitalWrite(5, LOW);
  digitalWrite(6, LOW);
  digitalWrite(7, LOW);
  digitalWrite(8, HIGH);
  digitalWrite(9, LOW);
  digitalWrite(10,LOW);
  digitalWrite(11,LOW);
  digitalWrite(12,LOW);
  digitalWrite(13,LOW);
  }
void open_dianji1(){            //电机1爬升
  digitalWrite(2, LOW);
  digitalWrite(3, LOW);
  digitalWrite(4, LOW);
  digitalWrite(5, LOW);
  digitalWrite(6, LOW);
  digitalWrite(7, LOW);
  digitalWrite(8, LOW);
  digitalWrite(9, LOW);
  digitalWrite(10,HIGH);
  digitalWrite(11,LOW);

  }
void fan_dianji1(){          
  digitalWrite(2, LOW);
  digitalWrite(3, LOW);
  digitalWrite(4, LOW);
  digitalWrite(5, LOW);
  digitalWrite(6, LOW);
  digitalWrite(7, LOW);
  digitalWrite(8, LOW);
  digitalWrite(9, LOW);
  digitalWrite(10,LOW);
  digitalWrite(11,HIGH);

  }
void open_dianji2(){            //电机2爬升
  digitalWrite(2, LOW);
  digitalWrite(3, LOW);
  digitalWrite(4, LOW);
  digitalWrite(5, LOW);
  digitalWrite(6, LOW);
  digitalWrite(7, LOW);
  digitalWrite(8, LOW);
  digitalWrite(9, LOW);
  digitalWrite(12,HIGH);
  digitalWrite(13,LOW);
  }
void fan_dianji2(){            
  digitalWrite(2, LOW);
  digitalWrite(3, LOW);
  digitalWrite(4, LOW);
  digitalWrite(5, LOW);
  digitalWrite(6, LOW);
  digitalWrite(7, LOW);
  digitalWrite(8, LOW);
  digitalWrite(9, LOW);
  digitalWrite(12,LOW);
  digitalWrite(13,HIGH);
  }
void stop(){            //全停
  digitalWrite(2, LOW);
  digitalWrite(3, LOW);
  digitalWrite(4, LOW);
  digitalWrite(5, LOW);
  digitalWrite(6, LOW);
  digitalWrite(7, LOW);
  digitalWrite(8, LOW);
  digitalWrite(9, LOW);
  digitalWrite(10,LOW);
  digitalWrite(11,LOW);
  digitalWrite(12,LOW);
  digitalWrite(13,LOW);
  }
```

> 这里请用markdown编辑嗷，美观一点

- 底部给出下载链接
  - 安卓apk安装文件
  - 单片机源代码txt
  - 手机程序源代码aia

4. 小组分工

- 组长：
  - ![](images/柴子豪.jpg)
  - 柴子豪，电子信息与电气工程学院自动化专业，18级本科生。负责电路连接、程序设计、机械结构组装。

- 组员：
  - ![](images/白振宇.jpg)
  - 白振宇，电子信息与电气工程学院自动化专业，18级本科生。负责机械结构组装。
  - ![](images/王子伦.jpg)
  - 王子伦，电子信息与电气工程学院信息安全专业，18级本科生。负责机械结构组装。
  - ![](images/桑锐.jpg)
  - 桑锐，电子信息与电气工程学院信息工程专业，18级本科生。负责网页设计、机械结构组装。

5. 项目感想：

- 这次制作安卓爬楼小车的过程中，尽管我们设计小车的计划较为周密，但是过程也历经坎坷，最终的小车成品也不尽如人意。小车的不足之处主要表现在：
  - 整体结构臃肿，所占体积大；
  - 爬楼效率低下，且无法攀爬日常所见的楼梯；
  - 走线暴露，未进行封装，故障概率大。
- 以上这些问题的原因有：
  - 所给材料限制，整体刚度低而较为脆弱，易折断损坏；
  - 材料非标准件，材料之间匹配难度大；
  - 对于电路部分的设计考虑不完备。
- 相信在这次的项目设计与制作中，每一位组员都收获满满，吸取了相关经验。

> 附上16~19.jpg

![](images/16.jpg)

![](images/17.jpg)

![](images/18.jpg)

![](images/19.jpg)

![](images/20.jpg)

