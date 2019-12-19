
## 背景

随着项目的不断迭代，工程中的组件会越来越多，一个组件对应一个 git 仓库。一个项目中可能有几十个甚至上百个工程组件，如何高效的管理这么多的工程就显得越来越重要。

管理多个 git 仓库也可以使用 `git submodule`，但是这种方式比较繁琐，也容易出现问题，例如：当你执行 `git pull` 后，如果 `submodule` 有修改，你还需要执行 `git submodule update` 命令。

所以 git submodule 的方式，我们并没有采用，而是使用 google repo 作为多仓库管理工具。Android 源码也是通过 repo 工具来管理的，Android 源码的下载需要使用 repo 来下载。


## repo 搭建

- repo 依赖

repo 本身依赖了 git 版本管理工具，所以首先要安装 git。除此之外，还需要安装 Python，然后设置 Python 的环境变量（我安装的版本是 Python2.7）

- repo 安装

根据官方文档 [installing-repo](https://source.android.com/setup/build/downloading#installing-repo) 使用如下命令来安装：

```
mkdir ~/bin
PATH=~/bin:$PATH

curl https://storage.googleapis.com/git-repo-downloads/repo > ~/bin/repo
chmod a+x ~/bin/repo
```

如果使用的是 Windows 系统，上面的方式则不行（上面的下载地址对应的是一个 repo 文件），而且上面的 url 国内可能访问不了。

我们可以使用国内的镜像，把 repo 依赖的所有文件下载下来：

```
git clone https://mirrors.tuna.tsinghua.edu.cn/git/git-repo
```

你也可以使用国外改良之后的 repo（支持 Windows 系统）：

```
https://github.com/esrlabs/git-repo
```


经过上面简单的几步，我们就将 repo 搭建好了。


## 项目中如何使用 repo

### 新建 Manifest

repo 通过一个 manifest 清单文件来列出要管理的仓库，需要新建一个 git 仓库（例如仓库名字叫做 Manifest），里面包含一个 `default.xml` 文件：

```
<?xml version="1.0" encoding="UTF-8"?>
<manifest>
     <remote name="origin" fetch="."/>

     <default revision="master" remote="origin" sync-j="4" />

     <project name="MRouter"             path="MRouter"/>
     <project name="AndroidUpdater"      path="AndroidUpdater"/>
</manifest>
```

`sync-j` 表示线程的数量，`project` 用指定被管理的仓库的名称。关于 manifest 文件格式解析，可以参考：[manifest-format.md](https://gerrit.googlesource.com/git-repo/+/master/docs/manifest-format.md)

> 需要注意的是，上面我们只是指定了仓库的名称，并没有指定仓库的 url，repo 是如何找到这些仓库的呢？repo 会安装 Manifest 对应的路径找，所以被管理的仓库需要和 Manifest 仓库在同一个分组下。

### repo 初始化

新建好了 Manifest 仓库之后，就可以初始化 repo 了：

```
repo init -u https://github.com/chiclaim/Manifest.git
```

执行完命令后，会在当前文件夹下生成 `.repo` 目录，

然后执行 `repo sync` 命令就可以把所有需要管理的仓库都拉下来了：
![repo sync](https://img-blog.csdnimg.cn/20191204113335183.jpg)


除了以上的两个命令，还需要使用其他的命令，接下来介绍下常用的 repo 命令


## 常用的 repo 命令

### repo init

**repo init -u url [options]**

在当前文件初始化 repo ，会生成一个 `.repo` 的文件夹，该文件夹里有个文件 `manifest.xml` ，该文件符号链接（synlink）到我们 Manifest 仓库里的 manifest 文件 default.xml


### 同步代码

**repo sync [project-list]**

如果整个工程是第一次执行该命令，那么该命令就相当于 `git clone` 。如果之前同步过，相当于：

```
git remote update
git rebase origin/branch
```

如果 rebase 过程中出现冲突，先解决冲突，然后 `git rebase --continue`


### 查看所在分支

```
$ repo branch
   default                   | in all projects
*  master                    | in all projects
```

### 切换分支

**repo start branch-name [project-list]**

```
// 将 project-list 中指定的项目切到 master 分支

$ repo start master MRouter AndroidUpdater
```

可以加上 --all 选项，表示所有的仓库切到某个分支上

```
$ repo start master --all
```

> 需要注意的是该命令不会将 manifest 仓库切到某个分支上，所以需要自己手动切。

### 仓库状态

**repo status**

例如我修改下 MRouter 工程里的 README.md 文件，然后执行 repo status 命令：

```
project MRouter/                                project AndroidUpdater/                         branch master
branch master -m        README.md

```

其中 `-m` 两个字符用来描述暂存区域（staging area）和最后一次提交状态（last committed state）的不同，有如下中可能：

|Letter|Meaning|Description|
|-|-|-|
|\-	|No change	|Same in HEAD and index|
|A	|Added	    |Not in HEAD, in index|
|M	|Modified	|In HEAD, modified in index|
|D	|Deleted	|In HEAD, not in index|
|R	|Renamed	|Not in HEAD, path changed in index|
|C	|Copied	    |Not in HEAD, copied from another in index|
|T	|Mode changed	|Same content in HEAD and index, mode changed|
|U	|Unmerged	|Conflict between HEAD and index; resolution required|

第二个字符表示工作区（working directory）与索引（index）之间的差异，有如下可能：

|Letter	|Meaning |Description|
|-|-|-|
|\-	|New/unknown |Not in index, in work tree|
|m	|Modified	 |In index, in work tree, modified|
|d	|Deleted	 |In index, not in work tree|


### repo forall

**repo forall [project-list] -c command**

为每个仓库执行某个命令，例如：

```
repo forall -c git push
```

### 查看 manifest 文件
```
$ repo manifest

<?xml version="1.0" encoding="UTF-8"?>
<manifest>
     <remote name="origin" fetch="."/>

     <default revision="master" remote="origin" sync-j="4" />

     <project name="MRouter"             path="MRouter"/>
     <project name="AndroidUpdater"      path="AndroidUpdater"/>
</manifest>

```

### 查看管理的工程列表

```
$ repo list

MRouter : MRouter
AndroidUpdater : AndroidUpdater 
```

### repo 版本信息

```
$ repo version

repo version v0.4.20
       (from D:\git-repo\.git)
repo launcher version 1.25
       (from D:\git-repo\repo)
git version 2.15.1.windows.2
Python 2.7.14 (v2.7.14:84471935ed, Sep 16 2017, 20:25:58) [MSC v.1500 64 bit (AMD64)]
```

### repo 的帮助命令

如果对有些命令忘记了用法，可以通过 `repo help command` 来查看命令的说明，如：

如 `repo help status`
```
Summary
-------
Show the working tree status

Usage: repo status [<project>...]

Options:
  -h, --help            show this help message and exit
  -j JOBS, --jobs=JOBS  number of projects to check simultaneously
  -o, --orphans         include objects in working directory outside of repo
                        projects

Description
-----------
'repo status' compares the working tree to the staging area (aka index),
and the most recent commit on this branch (HEAD), in each project
specified. A summary is displayed, one line per file where there is a
difference between these three states.

The -j/--jobs option can be used to run multiple status queries in
parallel.

The -o/--orphans option can be used to show objects that are in the
working directory, but not associated with a repo project. This includes
unmanaged top-level files and directories, but also includes deeper
items. For example, if dir/subdir/proj1 and dir/subdir/proj2 are repo
projects, dir/subdir/proj3 will be shown if it is not known to repo.

Status Display
--------------
The status display is organized into three columns of information, for
example if the file 'subcmds/status.py' is modified in the project
'repo' on branch 'devwork':

  project repo/                                   branch devwork
   -m     subcmds/status.py

The first column explains how the staging area (index) differs from the
last commit (HEAD). Its values are always displayed in upper case and
have the following meanings:

 -:  no difference
 A:  added         (not in HEAD,     in index                     )
 M:  modified      (    in HEAD,     in index, different content  )
 D:  deleted       (    in HEAD, not in index                     )
 R:  renamed       (not in HEAD,     in index, path changed       )
 C:  copied        (not in HEAD,     in index, copied from another)
 T:  mode changed  (    in HEAD,     in index, same content       )
 U:  unmerged; conflict resolution required

The second column explains how the working directory differs from the
index. Its values are always displayed in lower case and have the
following meanings:

 -:  new / unknown (not in index,     in work tree                )
 m:  modified      (    in index,     in work tree, modified      )
 d:  deleted       (    in index, not in work tree                )

```


## Reference

- https://source.android.com/setup/build/downloading#installing-repo
- https://source.android.com/setup/develop/repo
- https://segmentfault.com/a/1190000015279330
- https://www.jianshu.com/p/9c57696165f3
