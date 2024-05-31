package cap.capServer.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class S3Repository {
    JdbcTemplate jdbcTemplate;

    @Autowired
    public S3Repository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public int saveFileURL(String mediaTitle, String mediaMode, String fileURL, String nickname) {
        String insertSql = "insert into url(mediaTitle, mediaMode, nickname, url, progress) values (?, ?, ?, ?, ?);";
        jdbcTemplate.update(insertSql, mediaTitle, mediaMode, nickname, fileURL, false);
        String getSql = "select id from url where url = ?;";
        List<Integer> id = jdbcTemplate.query(getSql, (rs, rowNum) -> {
            return Integer.valueOf(rs.getInt("id"));
        }, fileURL);

        return id.get(0);
    }

    public void saveGeneratedUrl(int id, String url) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> result = new HashMap<>();
        try {
            result = objectMapper.readValue(url, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String updateSql = "update url set url = ? and progress = true where id = ?;";
        jdbcTemplate.update(updateSql, url, result.get("url"));
    }

    public void saveTags(int id, List<String> tags) {
        StringBuilder tag = new StringBuilder();
        for (String t : tags) {
            tag.append(t).append(",");
        }
        String updateSql = "insert into file_tags(id, tags) values (?,?);";
        jdbcTemplate.update(updateSql, id, tag.toString());
    }
}