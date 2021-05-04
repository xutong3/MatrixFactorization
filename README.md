## 环境配置

编程语言java

jdk1.8

添加依赖

fastjson-1.2.76.jar
Jama-1.0.3.jar
javatuples-1.2.jar



## 文件描述



FactorizationTest矩阵分解的测试函数

GradientDescent使用梯度下降方法做矩阵分解

Rating评分的数据结构：用户，物品，评分

RatingDao对Rating的操作



## 数据预处理

```java
Rating rating = new Rating();
JSONArray array = readJsonFile(filePath);

for (int i = 0; i < array.size(); i++) {
    JSONObject jsonObject = array.getJSONObject(i);
    int userId = jsonObject.getInteger("user_id");
    int movieId = jsonObject.getInteger("click_article_id");
    userSet.add(userId);  //保存训练用户集
    itemSet.add(movieId); //保存训练物品集
}

List<Integer> itemMapSet = itemSet.stream().sorted().collect(Collectors.toList());
List<Integer> userMapSet = userSet.stream().sorted().collect(Collectors.toList());

for (int i = 0; i < itemMapSet.size(); i++) {
    itemMap.put(itemMapSet.get(i), i);
}

for (int i = 0; i < userSet.size(); i++) {
    userMap.put(userMapSet.get(i), i);
}


for (int i = 0; i < array.size(); i++) {
    JSONObject jsonObject = array.getJSONObject(i);
    int userId = jsonObject.getInteger("user_id");
    int movieId = jsonObject.getInteger("click_article_id");

    double rating1 = 1D;
    rating.add(new Triplet<>(userMap.get(userId), itemMap.get(movieId), rating1));
}


rating.setNumOfUsers(userSet.size());
rating.setNumOfItems(itemSet.size());

return rating;
```



## 梯度下降

### 参数定义

```java
private int factorNum = 1;       //分解因子数（或者叫 特征值数量），即分解后小矩阵的宽度。
private int maxOfIter = 20;      //迭代次数
private double alpha = 0.01;      //学习速率
private double lambda = 0.05;     //正则化参数
private int numOfUsers = 6400;    //最大用户数量
private int numOfItems = 2305;    //最大物品数量
private double[][] users;    //分解后的用户矩阵
private double[][] items;    //分解后的物品矩阵
private double[] userBias;   //针对用户的打分偏见，用于优化
private double[] itemBias;   //针对物品的打分偏见，用于优化
private double sumRateScore = 0.0;  //评分总和，用于求平均
private double avgRateScore = 0.0;  //平均评分，用于优化
```



### 初始化分解矩阵

```java
double divisor = Math.sqrt(factorNum);
userBias = new double[numOfUsers];
users = new double[numOfUsers][factorNum];
for (int i = 0; i < numOfUsers; i++) {
    userBias[i] = 0.0;
    for (int j = 0; j < factorNum; j++) {
        users[i][j] = Math.random() / divisor;
    }
}
items = new double[numOfItems][factorNum];
itemBias = new double[numOfItems];
for (int i = 0; i < numOfItems; i++) {
    itemBias[i] = 0.0;
    for (int j = 0; j < factorNum; j++) {
        items[i][j] = Math.random() / divisor;
    }
}
for (int i = 0; i < rating.size(); i++) {
    int userId = rating.getUserId(i);
    int itemId = rating.getItemId(i);
    double rateScore = rating.getRateScore(i);
    if (userId > numOfUsers) numOfUsers = userId;
    if (itemId > numOfItems) numOfItems = itemId;
    sumRateScore += rateScore;
}
if (rating.size() > 0) avgRateScore = sumRateScore / rating.size();
```

### 矩阵分解

```java
for (int iter = 0; iter < maxOfIter; iter++) {
    for (int i = 0; i < rating.size(); i++) {
        int userId = rating.getUserId(i);
        int itemId = rating.getItemId(i);
        double RateScore = rating.getRateScore(i);
        double predictScore = predict(userId, itemId);
        double error = RateScore - predictScore;
        userBias[userId] += alpha * (error - lambda * userBias[userId]);
        itemBias[itemId] += alpha * (error - lambda * itemBias[itemId]);
        for (int f = 0; f < factorNum; f++) {
            users[userId][f] += alpha * (items[itemId][f] * error - lambda * users[userId][f]);
            items[itemId][f] += alpha * (users[userId][f] * error - lambda * items[itemId][f]);
        }
    }
    alpha *= 0.9;
}
```

## 预测

### 基本流程

```java
Rating rating = RatingDao.readFromTestJson(testFilePath);
for(int i=0;i<rating.size();i++){
    int userId = rating.getUserId(i);
    int itemId = rating.getItemId(i);
    if(!RatingDao.userSet.contains(userId) || !RatingDao.itemSet.contains(itemId)){ //如果记录不存在直接为空
        System.out.println(0);
        continue;
    }
    double predictScore = predict(RatingDao.userMap.get(userId), RatingDao.itemMap.get(itemId));
    System.out.println(predictScore > 1 ? 1 : 0);
}
```



### 预测方法

```java
private double predict(int userId, int itemId) {
    double ret = avgRateScore + userBias[userId] + itemBias[itemId];
    for (int f = 0; f < factorNum; f++) {
            ret += users[userId][f] * items[itemId][f];
        }
        return ret;
}
```





