import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Scanner;


public class URLwiki {


    public static void main(String[] args) throws Exception {

        /*
        //
        //даётся любое слово: несуществующее, не только город, город
        //
        */

        String city = "зима";
        URL wiki;
        URLConnection wikiCon = null;


        //проверка есть ли связь с Вики
        try {
            wiki = new URL("https://ru.wikipedia.org/wiki/" + city);
            wikiCon = wiki.openConnection();
        } catch (UnknownHostException e) {
            //ошибка подключения
            System.out.println("Ошибка подключения к интернету");
            return;
        }


        String s;
        String strWithInfoCity = "";
        String strWithInfoPopulation = "";


        /*
        //
        //получение содержимого страницы
        //
        */
        InputStream inStream;
        Scanner sc;
        long length;
        try {
            length = wikiCon.getContentLengthLong();
            if (length != 0) {
                inStream = wikiCon.getInputStream();
                sc = new Scanner(inStream);
                while (sc.hasNext()) {
                    s = sc.nextLine();
                    strWithInfoCity = strWithInfoCity + s + "\n";
                }
                inStream.close();
            }
        } catch (SocketException e1) {
            System.out.println("Ошибка подключения к интернету");
            return;

        } catch (FileNotFoundException e3) {
            System.out.println("Такого слова не существует");
            return;
        }

        //в данной строке информация об объекте
        String beginWordForCity = "infobox";
        String endWordForCity = "<span";
        String resultStr = "\nНеобходимая для поиска строка не найдена\n";
        boolean isContain = false;

        /*
        //
        //проверка есть ли в содержимом строка с нужными словами
        //
        */
        try {
            resultStr = strWithInfoCity.substring(strWithInfoCity.indexOf(beginWordForCity));
            resultStr = resultStr.substring(0, resultStr.indexOf(endWordForCity));
        } catch (StringIndexOutOfBoundsException e) {

            //в такой последовательности слов нет строки
            //либо это несуществующее слово, либо не только город
            try {
                wiki = new URL("https://ru.wikipedia.org/wiki/" + city + "_(город)");
                wikiCon = wiki.openConnection();
                length = wikiCon.getContentLengthLong();
                //извлекаем содержимое для варианта если это не только город
                if (length != 0) {
                    inStream = wikiCon.getInputStream();
                    sc = new Scanner(inStream);
                    while (sc.hasNext()) {
                        s = sc.nextLine();
                        strWithInfoCity = strWithInfoCity + s + "\n";
                    }
                    inStream.close();
                }
                resultStr = strWithInfoCity.substring(strWithInfoCity.indexOf(beginWordForCity));
                resultStr = resultStr.substring(0, resultStr.indexOf(endWordForCity));
            } catch (FileNotFoundException e2) {
                System.out.println(isContain);
                return;
            }

        }

        //проверяем есть ли слово "город" или "столица" в данной строке
        isContain = resultStr.contains("Город") || resultStr.contains("Столица");
        System.out.println(isContain);   // нашел - выведет true
        if (isContain) {
            System.out.println("\nЭто ГОРОД!");
            strWithInfoPopulation = strWithInfoCity.substring(strWithInfoCity.indexOf("nowrap"));
            strWithInfoPopulation = strWithInfoPopulation.substring(0,strWithInfoPopulation.indexOf("span"));
            System.out.println(strWithInfoPopulation);
        } else {
            System.out.println("Это ТОЧНО не город!");
        }

    }

}

