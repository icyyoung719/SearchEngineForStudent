操作系统配置：
	操作系统: Windows 11家庭中文版64位(10.0，版本22631)
	语言:中文(简体)(区域设置:中文(简体))
	系统制造商:LENOVO
	系统型号:82JW
	BIOS: HHCN29ww
	处理器: AMD Ryzen 7 5800H with Radeon Graphics
	(16 CPUs),~3.2GHz
	内存:16384MB RAM
	页面文件:8483MB已用，10696MB可用DirectX版本: DirectX 12
IDE版本：
	IntelliJ IDEA 2023.1.6 (Community Edition)
	Build #IC-231.9414.13, built on February 14, 2024
	Runtime version: 17.0.10+10-b829.26 amd64
	VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
	Windows 11.0
	GC: G1 Young Generation, G1 Old Generation
	Memory: 2030M
	Cores: 16
	Non-Bundled Plugins:
   		 intellij-theme-relax-your-eyes-green (1.0.3)
    		com.alibaba.p3c.xenoamess (2.1.1.6x-SNAPSHOT)
   		 PlantUML integration (6.5.0-IJ2022.2)
	Kotlin: 231-1.8.21-IJ9414.13
JDK版本：
	openjdk version "17.0.7" 2023-04-18
	OpenJDK Runtime Environment Temurin-17.0.7+7 (build 17.0.7+7)
	OpenJDK 64-Bit Server VM Temurin-17.0.7+7 (build 17.0.7+7, mixed mode, sharing)
使用说明：
1.	用IntellijIDEA打开文件的根目录root_dir，作为项目的根目录。
2.	为项目配置JDK环境，SDK为17，Language Level也为17
3.	运行package hust.cs.javacourse.search.run下的TestBuildIndex类，构建倒排索引。
4.	运行package hust.cs.javacourse.search.run下的TestSearchIndex类，进行搜索。搜索方式如下：
	
	单个单词搜索：直接在输入中输入搜索的单词，
	AND/OR搜索：在输入中输入搜索的单词，中间用空格和and/And/Or（不区分大小写）等分隔开，如”according and people”。
	两个单词构成的短语搜索：输入短语，中间用空格分隔，如”coffee shops”。
	退出程序：输入”q”或”quit“即可退出程序




