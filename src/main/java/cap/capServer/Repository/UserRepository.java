package cap.capServer.Repository;


import cap.capServer.Dto.GetMusicResponse;
import cap.capServer.Dto.MusicListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
        String sql = "select id, mediaTitle, url, progress, mediaMode from url where nickname = ?;";
        List<MusicListDto> musicList = jdbcTemplate.query(sql, (rs, rowNum) -> new MusicListDto(rs.getInt("id"), rs.getString("mediaTitle"),
                rs.getBoolean("progress"), rs.getString("url"), rs.getString("mediaMode")), nickname);

        for(MusicListDto dto : musicList) {
            if(!dto.isProgress())
                dto.setUrl(null);
        }
         return musicList;
    }

    public String getMusicUrl(int id) {
        String sql = "select url from url where id = ?;";
        List<String> urls = jdbcTemplate.query(sql, (rs, rowNum) -> {
            return rs.getString("url");
        }, id);
        System.out.println(id);
        System.out.println(urls.get(0));

        return urls.get(0);
    }
}
