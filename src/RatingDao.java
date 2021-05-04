import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import org.javatuples.Triplet;

public class RatingDao {
    public static Rating readFromFileWithNum(String filePath) throws Exception {
        Rating rating = new Rating();
        BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
        String line = reader.readLine();
        rating.setNumOfUsers(Integer.parseInt(line));
        line = reader.readLine();
        rating.setNumOfItems(Integer.parseInt(line));
        while ((line = reader.readLine()) != null) {
            rating.add(rating.stringToTriplet(line, "\t"));
            //Tuple node = rating.stringToTriplet(line,"\t");
            //rateList.add(node);
        }
        return rating;
    }

    public static  Set<Integer> userSet = new HashSet<>();
    public static  Set<Integer> itemSet = new HashSet<>();
    public static  Map<Integer, Integer> itemMap = new HashMap<>();
    public static  Map<Integer, Integer> userMap = new HashMap<>();

    public static Rating readFromJson(String filePath) throws Exception {
        Rating rating = new Rating();
        JSONArray array = readJsonFile(filePath);

        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            int userId = jsonObject.getInteger("user_id");
            int movieId = jsonObject.getInteger("click_article_id");
            userSet.add(userId);
            itemSet.add(movieId);
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
//        System.out.println("userSize"+ userSet.size());
//        System.out.println("itemSet"+ itemSet.size());
        return rating;
    }

    public static Rating readFromTestJson(String filePath) throws Exception {
        Set<Integer> userSet = new HashSet<>();
        Set<Integer> itemSet = new HashSet<>();
        Map<Integer, Integer> itemMap = new HashMap<>();
        Map<Integer, Integer> userMap = new HashMap<>();

        Rating rating = new Rating();
        JSONArray array = readJsonFile(filePath);

        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            int userId = jsonObject.getInteger("user_id");
            int movieId = jsonObject.getInteger("click_article_id");
            userSet.add(userId);
            itemSet.add(movieId);
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
            rating.add(new Triplet<>(userId, movieId, rating1));
        }


        rating.setNumOfUsers(userSet.size());
        rating.setNumOfItems(itemSet.size());
        System.out.println("userSize"+ userSet.size());
        System.out.println("itemSet"+ itemSet.size());
        return rating;
    }

    public static JSONArray readJsonFile(String filePath){
        BufferedReader reader = null;
        String readJson = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null){
                readJson += tempString;
            }
        }catch (IOException e){
            System.out.println(e);
        }finally {
            if (reader != null){
                try {
                    reader.close();
                }catch (IOException e){
                    System.out.println(e);
                }
            }
        }

        // 获取json
        try {
            JSONArray jsonArray = JSONArray.parseArray(readJson);
            return jsonArray;
        }catch (JSONException e){
            System.out.println(e);
        }
        return null;
    }


    public static Rating readFromFileWithoutID(MapID mapUser,MapID mapItem,String filePath) throws Exception {
        Rating rating = new Rating();
        BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] word = line.split("|");

        }
        return rating;
    }

    public static void testMap(Map map){
        map.put("哈哈",1);
    }
}
