import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task3 {
    /*
     В ТЗ не описано откуда поступает текст, т.к.
     тестовое задание будет поступать из строки.
     Также не указано:
      1) Что делать с числами;
      2) Что делать c пунктуаций.
      3) Как сортировать вывод, если совпадает частота вхождения.
    */

    public static void main(String[] args) {
        String text = "Он говорил на том изысканном французском языке, на котором не только говорили, но и думали наши деды, и с теми, тихими, покровительственными интонациями, которые свойственны состаревшемуся в свете и при дворе значительному человеку. Он подошел к Анне Павловне, поцеловал ее руку, подставив ей свою надушенную и сияющую лысину, и покойно уселся на диване.";

        Pattern pattern =
                Pattern.compile("\\w+", Pattern.UNICODE_CHARACTER_CLASS
                        | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        Long zn;
        Map<String, Long> metaInfoText = new HashMap<String, Long>();

        while (matcher.find()) {
            String word = matcher.group().toLowerCase();
            if (metaInfoText.containsKey(word)) {
                zn = metaInfoText.get(word);
                metaInfoText.put(word, zn + 1);
            } else {
                metaInfoText.put(word, 1L);
            }
        }

        List<WordData> words = new ArrayList<>();
        metaInfoText.forEach((k, v) ->
                words.add(new WordData(k, v)));

        words.stream()
                .sorted(Comparator.comparing(WordData::getFreq).reversed())
                .forEach(System.out::println);
    }
}
