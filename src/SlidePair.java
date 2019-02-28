public class SlidePair {
    Slide slide1;
    Slide slide2;

    public long cost() {
        return Estimator.cost(slide1, slide2);
    }
}
