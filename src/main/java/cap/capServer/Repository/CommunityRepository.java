package cap.capServer.Repository;

import cap.capServer.Dto.RequestPostDto;
import cap.capServer.Dto.ResponsePostDto;
import cap.capServer.Dto.ResponsePostListDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommunityRepository {
    JdbcTemplate jdbcTemplate;

    public CommunityRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String post(RequestPostDto postDto) {
        String getMediaUrlSql = "select url from url where nickname = ? and filename = ?;";
        List<String> url = jdbcTemplate.query(getMediaUrlSql, (rs, rowNum) -> {
            return new String(rs.getString("url"));
        }, postDto.getUsername(), postDto.getMediaTitle());
        String insertSql = "insert into post(username, mediaTitle, mediaURL, postTitle, postContent) " +
                "values (?, ?, ?, ?, ?);";
        jdbcTemplate.update(insertSql, postDto.getUsername(), postDto.getMediaTitle(), url.get(0), postDto.getPostTitle(), postDto.getPostContent());

        return "true";
    }

    public List<ResponsePostListDto> getPosts() {
        String getSql = "select id, username, mediaTitle, postTitle from post;";
        List<ResponsePostListDto> posts = jdbcTemplate.query(getSql, (rs, rowNum) -> {
            return new ResponsePostListDto(rs.getInt("id"), rs.getString("username"),
                    rs.getString("mediaTitle"), rs.getString("postTitle"));
        });

        return posts;
    }

    public ResponsePostDto getPost(int id) {
        String getSql = "select username, mediaTitle, postTitle, postContent, mediaURL" +
                "from post where id = ?;";
        List<ResponsePostDto> post = jdbcTemplate.query(getSql, (rs, rowNum) -> {
            return new ResponsePostDto(id, rs.getString("username"), rs.getString("mediaTitle"),
                    rs.getString("postTitle"), rs.getString("postContent"), rs.getString("mediaURL"));
        }, id);

        return post.get(0);
    }
}
