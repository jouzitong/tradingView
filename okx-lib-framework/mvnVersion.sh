#!/usr/bin
# 功能: mvn打版本, 并打tag.
# 注意: 但再次之前需要自行编译并通过
# version: 1.0.0

# 参数: 版本号, 版本说明
jdk21
param_version=$1
param_version_desc=$2
# 如果版本说明为空, 默认给版本号
if [ -z "$param_version_desc" ]; then
    param_version_desc=$param_version
fi

echo "运行参数: version=${param_version}, version_message=${param_version_desc}";

# 操作步骤:
# 切换jdk版本
mvn versions:set -DnewVersion=$param_version
mvn versions:commit

git add .
git commit -m "chore: 升级版本 > ${param_version}"

last_value="${param_version##*.}"
if [[ "$last_value" =~ ^[0-9]+$ ]]; then
    echo "需要打版本: v${param_version}"
    # 打版本
    git tag -a 'v'$param_version -m $param_version_desc
    # 列出刚打的版本
    git tag -l
    # 提交打的版本
    git push origin $param_version
else
    echo "不需要打版本: v${param_version}"
fi
