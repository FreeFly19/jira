import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Math.min;

public class Estimator {
    public static long test(List<Slide> result) {
        int score = 0;

        for (int i = 0; i < result.size() - 1; i++) {
            score += cost(result.get(i), result.get(i + 1));
        }

        return score;
    }

    public static int cost(Slide slide1, Slide slide2) {
        if (slide2 == null) {
            System.out.println(slide1);
        }
        Set<String> firstUnique = new HashSet<>(slide1.tags());
        firstUnique.removeAll(slide2.tags());

        Set<String> secondUnique = new HashSet<>(slide2.tags());
        secondUnique.removeAll(slide1.tags());


        Set<String> intersection = new HashSet<>(slide1.tags());
        intersection.addAll(slide2.tags());

        intersection.removeAll(firstUnique);
        intersection.removeAll(secondUnique);

        return min(min(firstUnique.size(), secondUnique.size()), intersection.size());
    }
}
