import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.javatuples.Tuple;

import java.util.ArrayList;
import java.util.List;

public class FactorizationTest {
    public static void main(String[] args) throws Exception {

//        String trainFilePath = "data\\u1_1.base";//training文件路径
        String trainFilePath = "data\\Train.json";//training文件路径
        String testFilePath = "data\\Test.json"; //test文件路径
        Rating rating = RatingDao.readFromJson(trainFilePath);

        System.out.println("梯度下降：");
        GradientDescent gd = new GradientDescent();
        gd.train(rating, 1,1,0.02,0.01);
        gd.rmseOfTestFile(testFilePath);

    }
}
