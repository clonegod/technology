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


## 关联本地项目到远程   
	git remote add origin git@github.com:clonegod/project-spider.git
	git push -u origin master

