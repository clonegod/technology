###	配置git
	git config --global user.name "Your Name"
	git config --global user.email "email@example.com"
	
	ssh-keygen -t rsa -C "youremail@example.com"

### 基本操作
	
	添加1个文件到暂存区
	git add <file>

	添加所有文件到暂存区
	git add --all .
	
	提交暂存区文件到本地仓库
	git commit -m'update some file for xxx'
	

### 远程仓库///添加远程仓库
	关联本地项目到远程项目   
	git remote add origin git@github.com:clonegod/project-spider.git

	把本地库的所有内容推送到远程库上（第一次推送时要使用-u参数将本地与远程仓库进行关联）
	git push -u origin master

	删除本地项目与远程origin仓库的关联	
	git remote rm origin
	
	》》》一个项目关联多个远程仓库
	远程库的名称叫github，不叫origin了
	git remote add github git@github.com:michaelliao/learngit.git

	远程库的名称叫gitee，不叫origin。
	git remote add gitee git@gitee.com:liaoxuefeng/learngit.git

	用git remote -v查看远程库信息，可以看到两个远程库
	git remote -v	

	git push github master	// 推送到github

	git push gitee master	// 推送到gitee

### 远程仓库///克隆远程仓库
	git clone git@github.com:michaelliao/bootstrap.git


---
### 分支管理/创建分支
	创建并切换到dev分支
	git checkout -b dev

	相当于以下两条命令
		git branch dev
		git checkout dev
	
	切换回master分支
	git checkout master

	合并指定分支到当前分支
	git merge dev
	
	删除本地分支
	git branch -d dev

	
	强制删除本地分支
	git branch -D dev


### 分支管理/多人协作
	origin/master
		1、基于master分支创建新的分支；
		2、将其它分支合并到master分支；
		3、在master分支打标签 && 打包发布系统；

	origin/dev
		开发人员基于远程dev进行开发;
		开发完成后提交到远程dev分支;
		最后将origin/dev分支合并到origin/master分支上；
#	
	克隆远程仓库到本地	
	git clone git@github.com:michaelliao/learngit.git	

	> 创建远程origin库的dev分支到本地，得到本地的dev分支（本地和远程分支的名称最好一致）
	git check -b dev origin/dev	

	添加文件到暂存区
	git add test.txt
	
	提交暂存区文件到本地仓库
	git commit -m'add test file'

	> 推送本地dev分支到远程dev分支
	git push origin dev

	> A handy way to push the current branch to the same name on the remote.
	git push origin HEAD
		
	-------
	> 建立本地分支和远程分支的关联（本地分支不是直接从远程分支创建出来的时候）
	git branch --set-upstream-to=origin/dev dev
	
	从当前分支所关联的远程分支更新代码
	git pull


#
	查看远程库
	git remote

	查看远程库的地址
	git remote -v

	查看所有分支（本地分支、远程分支）
	git branch -a

	查看远程分支
	git branch -r

	删除远程experimental分支
	git push origin :experimental

	查看本地分支
	git branch

	> 查看当前详细分支信息（可看到当前分支与对应的远程追踪分支）:
	git branch -vv

	删除本地dev分支
	git branch -d dev
	
	> 合并某分支到当前分支
	git merge <name>


--- 
### 标签管理 tag 

	查看所有标签
	git tag

	查看某个标签的详细信息（该标签打在哪个commit上，commit的注释是什么）
	git show <tagname>

	切换到master分支
	git checkout master

	打一个新标签（默认标签是打在最新提交的commit上的）
	git tag v1.0
	
	给指定的commit id打标签（对历史提交打标签）
	git tag v0.9 f52c633

	打一个新标签，并编写注释
	git tag -a v0.1 -m "version 0.1 released" 1094adb

	推送一个本地标签
	git push origin v1.0

	推送全部未推送过的本地标签
	git push origin --tags

	删除一个本地标签（标签打错了）
	git tag -d v0.9
	
	删除一个远程标签（先从本地删除，再推送）
	git push origin :refs/tags/v0.9
	


---

### git fetch 和 git pull 的差别
git fetch相当于是从远程获取最新版本到本地，但不会自动merge。

如果需要有选择的合并git fetch是更好的选择。

如果不需要选择合并，则 git pull将更为快捷。

###### git fetch 相当于是从远程获取最新到本地，但不会自动merge
	git fetch origin master:tmp //从远程仓库master分支拉取最新代码，在本地建立tmp分支

	git diff tmp //將當前分支和tmp進行對比
	
	git merge tmp //合并tmp分支到当前分支
	

###### git pull 相当于从远程获取最新版本并merge到本地
	git pull origin master


---
### 版本回退

	强制放弃本地所有修改的文件（修改、新增、删除）
	git clean是从工作目录中移除没有track的文件，git clean -df （-d表示同时移除目录,-f表示force）
	git checkout . && git clean -df

	
---
### log日志
	git log
	git log --pretty=oneline
	git log --pretty=oneline --abbrev-commit




