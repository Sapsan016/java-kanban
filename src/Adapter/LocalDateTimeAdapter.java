package Adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Objects;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDate) throws IOException {

        LocalDateTime value = Objects.nonNull(localDate) ? localDate : LocalDateTime.now();
        jsonWriter.value(value.format(DateTimeFormatter.ofPattern("dd.MM.yyyy.HH:mm")));
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(),DateTimeFormatter.ofPattern("dd.MM.yyyy.HH:mm"));
    }
}
