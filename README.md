## InterfaceTestframework
### 接口自动化的基础框架
# 使用说明
#### 1、参照test下的example
# 实现
### 1、	接口执行类型</br>
支持接口类型为**post、get**</br>
### 2、	实现功能</br>
####解析json</br>
####解析excel</br>
####解析xmind</br>
######如果xmind升级导致解析失败，在XMind的安装目录\plugins目录搜索org.xmind.core，找到org.xmind.core.****.jar文件，替换src/lib目录ixa的org.xmind.core.****.jar。同时修改pom.xml文件中xmind的jar包名称。
######