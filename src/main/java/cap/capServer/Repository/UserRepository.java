package cap.capServer.Repository;


import cap.capServer.Dto.GetMusicResponse;
import cap.capServer.Dto.MusicListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<MusicListDto> getMusicProgress(String nickname) {
        String sql = "select id, mediaTitle, url, url2, progress, mediaMode, imageUrl from url where nickname = ?;";
        List<MusicListDto> musicList = jdbcTemplate.query(sql, (rs, rowNum) -> new MusicListDto(rs.getInt("id"), rs.getString("mediaTitle"),
                rs.getBoolean("progress"), null,  rs.getString("url"), rs.getString("url2"), rs.getString("mediaMode"), rs.getString("imageUrl")), nickname);
        String getTagSql = "select tags from file_tags where id = ?;";
        String tagInfo;
        for(MusicListDto dto : musicList) {
            List<String> tags = jdbcTemplate.query(getTagSql, (rs, rowNum) -> {
                return new String(rs.getString("tags"));
            }, dto.getId());

            if (tags.isEmpty())
               tagInfo = null;
            else {

            }
                tagInfo = tags.get(0);
            String[] tag = tagInfo.split(",");
            List<String> tagRInfo = Arrays.asList(tag);
            dto.setTags(tagRInfo);
            if(dto.isProgress()) {
                dto.setMediaUrl(null);
                dto.setMediaUrl2(null);
            }
        }
         return musicList;
    }

    public String getMusicUrl(int id) {
        String sql = "select url from url where id = ?;";
        List<String> urls = jdbcTemplate.query(sql, (rs, rowNum) -> {
            return rs.getString("url");
        }, id);

        return urls.get(0);
    }
}
