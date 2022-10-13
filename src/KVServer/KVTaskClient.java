package KVServer;

import Managers.Managers;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    protected final long API_TOKEN;
    protected HttpClient client;
    protected Gson gson = Managers.getGson();

    public KVTaskClient(String url) throws IOException, InterruptedException { //Конструктор клиента с регистрацией
        client = HttpClient.newHttpClient();
        URI uri = URI.create(String.valueOf(url));
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        API_TOKEN = Long.parseLong(gson.fromJson(response.body(), String.class));
    }

    public void put(String key, String json) throws IOException, InterruptedException { //Метод для сохранения данных на KVServer
        URI uri = URI.create("http://localhost:8078/save/" + key + "?API_TOKEN=" + API_TOKEN);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public String load(String key) throws IOException, InterruptedException {  //Метод для выгрузки данных с KVServer
        URI uri = URI.create("http://localhost:8078/load/" + key + "?API_TOKEN=" + API_TOKEN);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), String.class);
    }
}