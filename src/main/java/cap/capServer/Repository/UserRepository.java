package cap.capServer.Repository;


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
        String sql = "select filname, progress from url where nickname = ?;";
        List<MusicListDto> musicList = jdbcTemplate.query(sql, (rs, rowNum) -> {
            return new MusicListDto(rs.getString("filename"), rs.getBoolean("progress"));
        }, nickname);

        return musicList;
    }

    public String getMusicUrl(String nickname, String musicName) {
        String sql = "select url from url where nickname = ? and filename = ?;";
        List<String> urls = jdbcTemplate.query(sql, (rs, rowNum) -> {
            return new String(rs.getString("url"));
        });

        return urls.get(0);
    }
}
