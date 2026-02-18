package utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.NewsItemDto;

import java.io.IOException;

public final class JsonLinesMessageCodec {

        private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonLinesMessageCodec() {}

    public static String encode(NewsItemDto newsItemDto) {
        try{
            return MAPPER.writeValueAsString(newsItemDto) + "\n";
        }

        catch (Exception e) {
            throw new RuntimeException("Failed to encoding new item",e);
        }
    }

    public static NewsItemDto decode(String line) throws IOException {
        return MAPPER.readValue(line, NewsItemDto.class);
    }

}
