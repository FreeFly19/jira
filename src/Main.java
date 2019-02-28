import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    static Map<String, Set<Photo>> tags;
    static List<Photo> photos;
    static List<Slide> slides;
    static List<Slide> result;


    static Set<Slide> usedSlides = new HashSet();

    static void alg() {
        usedSlides.add(slides.get(0));
        result.add(slides.get(0));

        for (int i = 0; i < slides.size() - 1; i++) {
            Slide currentSlide = slides.get(i);

            Slide bestMatch = null;
            long bestMatchScore = -1;

            for (int j = i + 1; j < slides.size(); j++) {
                if (usedSlides.contains(slides.get(j))) continue;
                if (Estimator.cost(currentSlide, slides.get(j)) > bestMatchScore) {
                    bestMatch = slides.get(j);
                    bestMatchScore = Estimator.cost(currentSlide, slides.get(j));
                }
            }
            if (bestMatch != null) {
                usedSlides.add(bestMatch);
                result.add(bestMatch);
            }
        }
    }



    public static void main(String[] args) {
        Set.of(
//                "a_example.txt"
//                ,
//                "b_lovely_landscapes.txt",
//                "c_memorable_moments.txt"
//                ,
                "d_pet_pictures.txt"
//                "e_shiny_selfies.txt"
        ).forEach(Main::run);

    }

    static void run(String file) {
        tags = new HashMap<>();
        photos = new ArrayList<>();
        slides = new ArrayList<>();
        result = new ArrayList<>();
        Scanner sc = new Scanner(Main.class.getResourceAsStream(file));
        int amountOfSlides = sc.nextInt();


        for (int i = 0; i < amountOfSlides; i++) {
            Photo photo = new Photo();
            photo.id = i;
            photo.vertical = sc.next().equals("V");
            int tagsAmount = sc.nextInt();
            for (int j = 0; j < tagsAmount; j++) {
                photo.tags.add(sc.next());
            }
            photos.add(photo);
        }


        for (Photo photo : photos) {
            for (String tag : photo.tags) {
                tags.put(tag, new HashSet<>());
            }
        }


        int slot = -1;
        for (Photo photo : photos) {
            if (!photo.vertical) {
                slides.add(new Slide(photo));
                continue;
            }
            if(slot == -1) {
                slides.add(new Slide(photo));
                slot = slides.size() - 1;
            } else {
                slides.get(slot).photo2 = photo;
                slot = -1;
            }
        }

        alg();

        System.out.println(file);
        System.out.println(Estimator.test(result));
        System.out.println();
        //save(file);
    }

    static void save(String pathname) {
        try {
            File file = new File("result/" + pathname);
            file.createNewFile();
            PrintWriter w = new PrintWriter(file);
            w.println(slides.size());
            for (int i = 0; i < slides.size(); i++) {
                w.print(slides.get(i).photo1.id);
                if(slides.get(i).photo2 != null) {
                    w.print(' ');
                    w.print(slides.get(i).photo2.id);
                }
                w.println();
            }
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Slide {
    Photo photo1;
    Photo photo2;

    public Slide(Photo photo1) {
        this.photo1 = photo1;
    }

    public Slide(Photo photo1, Photo photo2) {
        this.photo1 = photo1;
        this.photo2 = photo2;
    }

    public Set<String> tags() {
        HashSet<String> tag = new HashSet<>(photo1.tags);
        if(photo2 != null)  tag.addAll(photo2.tags);
        return tag;
    }
}

class Photo {
    long id;
    boolean vertical;
    Set<String> tags = new HashSet<>();

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", vertical=" + vertical +
                ", tags=" + tags +
                "}\n";
    }
}