package application;

import com.fasterxml.jackson.databind.ObjectMapper;
import items.File;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class NASAAppl {
    private static final String REMOTE_SERVICE_URI =
            "https://api.nasa.gov/planetary/apod?api_key=......";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static String fileURL;
    private static String fileName;


    public static void main(String[] args) {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();

        HttpGet request = new HttpGet(REMOTE_SERVICE_URI);

        try {
            CloseableHttpResponse response = httpClient.execute(request);
            String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
            File file = mapper.readValue(body, items.File.class);
            // Извлекаем URL файла и его имя
            fileURL = file.getUrl();
            fileName = getFileNameFromURL(fileURL);
            // формируем новый запрос
            request = new HttpGet(fileURL);
            // получаем ответ
            response = httpClient.execute(request);
            // помещаем ответ в массив байт
            byte downloadedFile[] = response.getEntity().getContent().readAllBytes();
            // записываем массив байт в файл
            saveFile(fileName, downloadedFile);
        } catch (IOException e) {
            System.out.println("Ошибка чтения ответа! " + e.getMessage());
        }
    }

    /**
     * Получение имени файла из URL
     * @param url Заданный URL-адрес
     * @return Строка, содержащая имя файла. Если из URL не удалось извлечь информацию о расширении файла
     * (например, адрес вида https://www.youtube.com/embed/2SnbMTQwDKM?rel=0), то считаем
     * что это html-страница и дописываем к имени файла соответствующее расширение
     */
    private static String getFileNameFromURL(String url) {
        // Ищем часть адреса от последнего знака / до последнего символа
        String fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
        // Заменяем все не символьные, не числовые, не знаки падчеркивания и точки на символ подчеркивания.
        // Иначе имя файла содержит недопустимые символы
        fileName = fileName.replaceAll("[^a-zA-Z0-9_.]", "_");
        // Если в имени файла не было точки, то считаем, что это html-страница
        if (!fileName.contains(".")) {
            fileName = fileName + ".html";
        }
        return fileName;
    }

    /**
     * Сохранение массива байт в указанный файл
     * @param fileName Имя файла
     * @param data Массив байт
     */
    private static void saveFile(String fileName, byte data[]) {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(data);
        } catch (FileNotFoundException e) {
            System.out.println("Неверно указано имя файла! " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл! " + e.getMessage());
        }
    }

}
