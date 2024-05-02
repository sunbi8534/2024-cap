package cap.capServer.Repository;

import cap.capServer.Dto.UserEnrollDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public class LoginRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public LoginRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String checkUser(String id, String pw) {
        String checkSql = "select nickname from user where id = ? and pw = ?;";
        List<String> nickname = jdbcTemplate.query(checkSql, (rs, rowNum) -> {
            return new String(
                    rs.getString("nickname"));
        }, id, pw);

        if(nickname.isEmpty())
            return "false";
        else
            return nickname.get(0);
    }

    public String checkDupNickname(String nickname) {
        String checkSql = "select nickname from user where nickname = ?;";
        List<String> nicknames = jdbcTemplate.query(checkSql, (rs, rowNum) -> {
            return new String(rs.getString("nickname"));
        }, nickname);

        if(nicknames.isEmpty())
            return "ok";
        else
            return "false";
    }

    public String checkDupId(String id) {
        String checkSql = "select id from user where id = ?;";
        List<String> ids = jdbcTemplate.query(checkSql, (rs, rowNum) -> {
            return new String(rs.getString("id"));
        }, id);

        if(ids.isEmpty())
            return "ok";
        else
            return "false";
    }

    public void enrollUser(UserEnrollDto user) {
        String insertSql = "insert into user(id, pw, nickname) values (?, ?, ?);";
        jdbcTemplate.update(insertSql, user.getId(), user.getPw(), user.getNickname());
    }
}

