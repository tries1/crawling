import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by glenn on 2017. 6. 7..
 */
public class Crawler {
    public static void main(String[] args) {

        try {
            //for(int i = 0; i <= 10000; i = i + 50){
            for(int i = 1; i <= 100; i ++){
                Thread.sleep(500);
                String genre = "fork";
                //String targetUrl = "http://www.genie.co.kr/genre/L0101?genreCode=L0101&pg=" + i;//ballard
                //String targetUrl = "http://www.genie.co.kr/genre/L0102?genreCode=L0102&pg=" + i;//dance
                //String targetUrl = "http://www.genie.co.kr/genre/L0103?genreCode=L0103&pg=" + i;//R&B
                //String targetUrl = "http://www.genie.co.kr/genre/L0104?genreCode=L0104&pg=" + i;//hiphop
                //String targetUrl = "http://www.genie.co.kr/genre/L0105?genreCode=L0105&pg=" + i;//rock
                //String targetUrl = "http://www.genie.co.kr/genre/L0106?genreCode=L0106&pg=" + i;//electronica
                //String targetUrl = "http://www.genie.co.kr/genre/L0109?genreCode=L0109&pg=" + i;//indi
                String targetUrl = "http://www.genie.co.kr/genre/L0108?genreCode=L0108&pg=" + i;//fork

                Document doc = Jsoup.connect(targetUrl).get();
                Elements contents = doc.select("div.list");//원하는 css부분

                List<String> list = contents.stream().map(element -> {
                    //System.out.println(element.html());

                    String title = element.getElementsByClass("title").html();
                    String artist = element.getElementsByClass("artist").html();

                    String[] strings = new String[4];
                    strings[0] = title;
                    strings[1] = artist;
                    strings[2] = genre;
                    strings[3] = String.valueOf((long) (Math.random() * 1000) + 1);//추천수 랜덤으로 생성

                    return Arrays.stream(strings).collect(Collectors.joining("||"));
                })
                .collect(Collectors.toList());

                saveFile(list, genre, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void melonCrawling(){
        try {
            for(int i = 0; i <= 10000; i = i + 50){
                String targetUrl = "http://www.melon.com/genre/song_list.htm#params%5BgnrCode%5D=GN0100&params%5BdtlGnrCode%5D=&params%5BorderBy%5D=NEW&po=pageObj&startIndex=" + i;
                System.out.println("index = " + i);

                Document doc = Jsoup.connect(targetUrl).get();
                Elements contents = doc.select("table tbody tr");//원하는 css부분

                String genre = "ballard";
                List<String> list = contents.stream().map(element -> {
                    String title = element.getElementsByClass("fc_gray").html();
                    String artist = element.getElementsByClass("fc_mgray").get(0).html();

                    String[] strings = new String[3];
                    strings[0] = title;
                    strings[1] = artist;
                    strings[2] = genre;

                    //System.out.println(Arrays.stream(strings).collect(Collectors.joining("||")));
                    return Arrays.stream(strings).collect(Collectors.joining("||"));
                })
                .collect(Collectors.toList());

                saveFile(list, genre, true);
                Thread.sleep(3000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveFile(List<String> list, String genre, boolean isAppend) throws Exception {
        if(list.isEmpty()){
            //Assert.error("size 0");
        }

        //charset을 명시적으로 적어야함
        //플랫폼에따라 동작이 달라짐
        //try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(String.format("/Users/glenn/dev/crawling/doc/%s.csv", genre))))) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(String.format("/Users/glenn/dev/crawling/doc/%s.csv", genre)), isAppend), "UTF-8"))) {
            list.forEach(s -> {
                try {
                    writer.write(s);
                    writer.newLine();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
