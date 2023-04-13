package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Map.Entry;
import java.util.*;

import com.example.springboot.common.Result;

import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.service.IStdApplicationService;
import com.example.springboot.entity.StdApplication;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Misaki
 * @since 2023-02-28
 */
@RestController
@RequestMapping("/application")
public class StdApplicationController {

    @Resource
    private IStdApplicationService stdApplicationService;

    // 增加 更新
    @PostMapping
    public Result save(@RequestBody StdApplication stdApplication) {
        // 若数据库中不存在则添加 （根据用户名查询）
        long count = stdApplicationService.count(new QueryWrapper<StdApplication>()
                .eq("name", stdApplication.getName()));
        if (count == 0) {
            return Result.success(stdApplicationService.saveOrUpdate(stdApplication));
        }
        // 若存在则更新
        else {
            UpdateWrapper<StdApplication> updateWrapper = new UpdateWrapper<>();
            updateWrapper
                    .set("score", stdApplication.getScore())
                    .set("avatar", stdApplication.getAvatar())
                    .set("application1", stdApplication.getApplication1())
                    .set("application2", stdApplication.getApplication2())
                    .set("application3", stdApplication.getApplication3())
                    .set("application4", stdApplication.getApplication4())
                    .set("application5", stdApplication.getApplication5())
                    .eq("name", stdApplication.getName());
            return Result.success(stdApplicationService.update(null, updateWrapper));
        }
    }

    // 删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(stdApplicationService.removeById(id));
    }

    // 批量删除
    @DeleteMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(stdApplicationService.removeByIds(ids));
    }

    // 查询所有
    @GetMapping
    public Result findAll(){
        return Result.success(stdApplicationService.list());
    }

    // 查询单个
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(stdApplicationService.getById(id));
    }

    // 查询名称
    @GetMapping("/pageName")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam (defaultValue = "") String name) {
        QueryWrapper<StdApplication> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(name != null, "name", name);
        // 逆序
        queryWrapper.orderByDesc("id");
        return Result.success(stdApplicationService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }


    // 协同过滤推荐算法
    @PostMapping("/Recommend")
    public List<String> findRecommend(@RequestBody List<StdApplication> listAll){
        // 建立用户稀疏矩阵，用于用户相似度计算【相似度矩阵】
        int[][] sparseMatrix = new int[listAll.size()][listAll.size()];
        // 存储每一个用户对应的不同物品总数  eg: A 3
        Map<String, Integer> userItemLength = new HashMap<>();
        // 建立物品到用户的倒排表 eg: a A B
        Map<String, Set<String>> itemUserCollection = new HashMap<>();
        // 辅助存储物品集合
        Set<String> items = new HashSet<>();
        // 辅助存储每一个用户的用户ID映射
        Map<String, Integer> userID = new HashMap<>();
        // 辅助存储每一个ID对应的用户映射
        Map<Integer, String> idUser = new HashMap<>();
        // 存储院校和相似度
        Map<String, Double> list = new HashMap<String, Double>();

        // 建立稀疏矩阵
        for (int i = 0; i < listAll.size(); i++) {
            String[] user_item = new String[]{
                    listAll.get(i).getApplication1(),
                    listAll.get(i).getApplication2(),
                    listAll.get(i).getApplication3(),
                    listAll.get(i).getApplication4(),
                    listAll.get(i).getApplication5()
            };
            // 5个志愿 + 1个名称
            int length = 6;
            String userName = listAll.get(i).getName();
            // 对应不同用户和专业总数
            userItemLength.put(userName, length);
            userID.put(userName, i);
            idUser.put(i, userName);

            // 建立 用户 -- 物品 倒排表
            for (int j = 0; j < length - 1; j++) {
                if (items.contains(user_item[j])) {
                    // 从第一个往后直接添加
                    itemUserCollection.get(user_item[j]).add(userName);
                }
                else {
                    // 添加第一个用户映射
                    items.add(user_item[j]);
                    itemUserCollection.put(user_item[j], new HashSet<String>());
                    itemUserCollection.get(user_item[j]).add(userName);
                }
            }
        }

        System.out.println("\n" + itemUserCollection.toString());

        // 稀疏矩阵 (相似度矩阵计算)
        Set<Entry<String, Set<String>>> entrySet = itemUserCollection.entrySet();
        Iterator<Entry<String, Set<String>>> iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            Set<String> commonUsers = iterator.next().getValue();
            for (String user_u : commonUsers) {
                for (String user_v : commonUsers) {
                    if (user_u.equals(user_v)) {
                        continue;
                    }
                    // 计算共有数据
                    sparseMatrix[userID.get(user_u)][userID.get(user_v)] += 1;
                }
            }
        }
        System.out.println("\n" + userItemLength.toString());

        // 余弦相似度
        String recommendUser = listAll.get(0).getName();
        int recommendUserId = userID.get(recommendUser);
        System.out.println();
        for (int j = 0;j < sparseMatrix.length; j++) {
            if(j != recommendUserId){
                System.out.println(idUser.get(recommendUserId) +
                        "--" +
                        idUser.get(j) +
                        " 相似度:" +
                        sparseMatrix[recommendUserId][j] /
                                Math.sqrt(userItemLength.get(idUser.get(recommendUserId)) *
                                        userItemLength.get(idUser.get(j))));
            }
        }

        // 计算当前用户的院校推荐度
        // 遍历集合
        for(String item : items){
            // 当前院校集合
            Set<String> users = itemUserCollection.get(item);
            // 若当前院校未包含于推荐用户的院校集内
            if(!users.contains(recommendUser)){
                double itemRecommendDegree = 0.0;
                for(String user : users){
                    // 进行物品和用户的相似度叠加 生成院校推荐度
                    itemRecommendDegree +=
                            sparseMatrix[userID.get(recommendUser)][userID.get(user)] /
                                    Math.sqrt(userItemLength.get(recommendUser) * userItemLength.get(user));
                }
                list.put(item, itemRecommendDegree);
//                System.out.println("The item "+item+" for "+recommendUser +"'s recommended degree:"+itemRecommendDegree);
            }
        }

        // 移除空志愿
        list.remove(null);
        List<String> res = new ArrayList<>(sortByValueFloatDesc(list).keySet());
        // 若无推荐 则返回空
        if (res.size() == 0) {
            return null;
        }
        // 返回最多 5个最接近结果
        else {
            if(res.size() < 5) {
                return res;
            }
            else {
                return res.subList(0, 5);
            }
        }
    }

    // 将志愿推荐度从大到小进行排序
    private static Map<String, Double> sortByValueFloatDesc(Map<String, Double> nowPartTwoData) {
        List<Map.Entry<String, Double>> lists = new ArrayList<Map.Entry<String, Double>>(nowPartTwoData.entrySet());
        LinkedHashMap<String, Double> linkedHashMap = new LinkedHashMap<String, Double>();
        lists.sort(new Comparator<Entry<String, Double>>() {
            public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
                Double q1 = Double.parseDouble(o1.getValue() + "");
                Double q2 = Double.parseDouble(o2.getValue() + "");
                double p = q2 - q1;
                if (p > 0) {
                    return 1;
                } else if (p == 0) {
                    return 0;
                } else
                    return -1;
            }
        });
        System.out.println();

        for (Map.Entry<String, Double> set : lists) {
            System.out.println(set.getKey() + ": " + set.getValue());
        }

        // 建立相似度从大到小的新队列
        for (Map.Entry<String,Double> e : lists) {
            linkedHashMap.put(e.getKey(),e.getValue());
        }

        return linkedHashMap;
    }
}

