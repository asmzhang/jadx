# JADX Smali可编辑模式

## 功能概述

为JADX-GUI添加了Smali代码可编辑模式，允许用户直接在JADX中编辑从APK/DEX反编译的Smali代码。

## 主要功能

### 1. 可编辑模式切换
- 在Smali标签页的右键菜单中添加了"Editable Mode"选项
- 启用后，Smali代码区域变为可编辑状态
- 支持语法高亮、自动缩进、括号匹配等功能

### 2. 保存功能
- 点击"Save"按钮保存当前编辑的Smali代码
- 代码保存到临时文件中
- 保存成功后会显示提示信息

### 3. 导出功能
- 点击"Export"按钮将编辑后的Smali代码导出到文件
- 导出的文件可以用于后续的编译和打包
- 导出后可以使用外部工具（如smali）进行编译

### 4. 重置功能
- 点击"Reset"按钮恢复到原始的Smali代码
- 重置前会弹出确认对话框
- 所有未保存的修改将丢失

## 使用方法

### 启用编辑模式

1. 在JADX-GUI中打开APK/DEX文件
2. 切换到"Smali"标签页
3. 右键点击Smali代码区域
4. 选择"Editable Mode"菜单项
5. Smali代码区域变为可编辑状态

### 编辑Smali代码

1. 启用编辑模式后，可以直接在Smali代码区域进行编辑
2. 支持以下编辑功能：
   - 撤销/重做（Ctrl+Z / Ctrl+Y）
   - 剪切/复制/粘贴
   - 自动缩进
   - 括号匹配
   - 代码折叠

### 保存修改

1. 点击工具栏上的"Save"按钮
2. 修改会保存到临时文件
3. 可以随时继续编辑

### 导出Smali代码

1. 点击工具栏上的"Export"按钮
2. Smali代码会导出到临时文件
3. 系统会显示导出文件的完整路径
4. 使用外部工具（如smali）编译导出的文件

### 重置为原始代码

1. 点击工具栏上的"Reset"按钮
2. 确认重置操作
3. Smali代码恢复到反编译时的原始状态

## 技术实现

### 新增文件

1. **JEditableSmaliClass.java**
   - 包装JClass节点，提供可编辑的Smali代码接口
   - 管理临时文件和代码状态
   - 提供保存、导出、重置功能

### 修改文件

1. **SmaliArea.java**
   - 添加可编辑模式切换功能
   - 实现EditableModel内部类
   - 添加保存、导出、重置方法

2. **ClassCodeContentPanel.java**
   - 添加Save、Export、Reset按钮到工具栏
   - 按钮仅在Smali标签页显示时可用

## 限制说明

1. **编译功能**
   - 由于模块依赖限制，当前版本不支持直接在JADX中编译Smali为DEX
   - 需要导出后使用外部工具（如smali）进行编译

2. **APK重新打包**
   - 当前版本不支持直接重新打包APK
   - 需要使用APKTool等工具进行打包和签名

3. **多类编辑**
   - 每次只能编辑一个类的Smali代码
   - 切换到其他类时，当前编辑状态会保留

## 后续改进方向

1. 集成smali编译功能，支持直接编译为DEX
2. 支持批量编辑多个类的Smali代码
3. 添加Smali语法验证和错误提示
4. 支持直接重新打包APK
5. 添加代码差异对比功能

## 示例工作流

```
1. 在JADX中打开APK文件
   ↓
2. 切换到Smali标签页
   ↓
3. 启用Editable Mode
   ↓
4. 编辑Smali代码
   ↓
5. 点击Save保存修改
   ↓
6. 点击Export导出.smali文件
   ↓
7. 使用smali工具编译: smali MyClass.smali -o classes.dex
   ↓
8. 使用APKTool重新打包APK
   ↓
9. 签名APK
   ↓
10. 安装测试
```

## 注意事项

1. 编辑模式会创建临时文件，退出JADX时会自动清理
2. 建议在编辑前先备份原始APK文件
3. 修改后的代码可能无法编译，请仔细检查语法
4. 复杂的修改可能需要调整多个相关类
5. 导出后的文件路径会显示在对话框中，请及时复制保存

## 故障排除

### 问题：无法启用编辑模式
- 解决：确保已正确反编译类文件
- 检查临时文件权限

### 问题：导出失败
- 解决：检查磁盘空间
- 确保有写入临时目录的权限

### 问题：编译失败
- 解决：使用外部smali工具验证语法
- 检查API级别设置是否正确

## 相关资源

- [Smali语法文档](https://github.com/JesusFreke/smali/wiki)
- [APKTool文档](https://ibotpeaches.github.io/Apktool/)
- [JADX项目](https://github.com/skylot/jadx)
