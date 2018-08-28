## 关联本地项目到远程   
	git remote add origin git@github.com:clonegod/project-spider.git
	git push -u origin master

## 从当前分支切换到‘dev’分支：
	git checkout dev

## 建立并切换新分支：
	git checkout -b 'dev'

## 查看当前详细分支信息（可看到当前分支与对应的远程追踪分支）:
	git branch -vv

## 查看当前远程仓库信息
	git remote -vv


## git误删文件找回方法/git版本回退方法
	git log
	git reset --hard versionYouWant

---

## 添加所有文件到本地暂存库
	git add --all .

## 提交文件到本地仓库
	git commit -m'update some file for xxx'

## 推送本地仓库的文件到远程仓库（master/branch分支）
	git push origin master

## git 强制放弃本地所有修改的文件（修改、新增、删除）
	git checkout . && git clean -df

	git clean是从工作目录中移除没有track的文件.
	通常的参数是git clean -df （-d表示同时移除目录,-f表示force）




