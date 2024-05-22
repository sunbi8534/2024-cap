package cap.capServer.Repository;

import cap.capServer.Dto.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
        String insertSql = "insert into post(username, mediaTitle, mediaURL, postTitle, postContent, numLikes, numComments) " +
                "values (?, ?, ?, ?, ?, ?, ?);";
        String mediaURL;

        if (url.isEmpty())
            mediaURL = "";
        else
            mediaURL = url.get(0);
        jdbcTemplate.update(insertSql, postDto.getUsername(), postDto.getMediaTitle(), mediaURL
                , postDto.getPostTitle(), postDto.getPostContent(), 0, 0);

        return "true";
    }

    public List<ResponsePostListDto> getPosts(String username) {
        String getSql = "select id, username, mediaTitle, postTitle, numLikes, numComments from post;";
        List<ResponsePostListDto> posts = jdbcTemplate.query(getSql, (rs, rowNum) -> {
            return new ResponsePostListDto(rs.getInt("id"), rs.getString("username"),
                    rs.getString("mediaTitle"), rs.getString("postTitle"), rs.getInt("numLikes"),
                    false, rs.getInt("numComments"));
        });

        String getHasLikedSql = "select post_id from likePost where username = ?;";
        List<Integer> postIds = jdbcTemplate.query(getHasLikedSql, (rs, rowNum) -> {
            return Integer.valueOf(rs.getInt("post_id"));
        }, username);

        for (ResponsePostListDto post : posts) {
            if (postIds.contains(post.getId()))
                post.setHasLiked(true);
        }
        return posts;
    }

    public ResponsePostDto getPost(int id, String username) {
        String getSql = "select username, mediaTitle, postTitle, postContent, mediaURL, numLikes from post where id = ?;";
        List<GetPostInfoDto> post = jdbcTemplate.query(getSql, (rs, rowNum) -> {
            return new GetPostInfoDto(id, rs.getString("username"), rs.getString("mediaTitle"),
                    rs.getString("postTitle"), rs.getString("postContent"), rs.getString("mediaURL"),
                    rs.getInt("numLikes"), false);
        }, id);

        ResponsePostDto responsePostDto = new ResponsePostDto(post.get(0));
        String hasLikedSql = "select id from where username = ? and post_id = ?;";
        List<Integer> hasLikedId = jdbcTemplate.query(hasLikedSql, (rs, rowNum) -> {
            return Integer.valueOf(rs.getInt("id"));
        }, username, responsePostDto.getId());
        if(!hasLikedId.isEmpty()) {
            responsePostDto.setHasLiked(true);
        }

        String getCommentSql = "select id, username, content, numLikes from comment where post_id = ? order by id ASC;";
        List<CommentListDto> comments = jdbcTemplate.query(getCommentSql, (rs, rowNum) -> {
            return new CommentListDto(rs.getInt("id"), rs.getString("username"), rs.getString("content"),
                    rs.getInt("numLikes"), false);
        }, responsePostDto.getId());

        String hasLikedCommentSql = "select id from likeComment where username = ? and post_id = ? and" +
                " comment_id = ?;";
        for(CommentListDto c : comments) {
            List<Integer> ids = jdbcTemplate.query(hasLikedCommentSql, (rs, rowNum) -> {
                return Integer.valueOf(rs.getInt("id"));
            }, responsePostDto.getUsername(), responsePostDto.getId(), c.getId());

            if (!ids.isEmpty())
                c.setHasLiked(true);
        }
        responsePostDto.setCommentList(comments);
        return responsePostDto;
    }

    public boolean likePost(int id, String username) {
        String sql = "insert into likePost(username, post_id) values (?, ?);";
        String updateSql = "update post set numLikes = numLikes + 1 where id = ?;";
        jdbcTemplate.update(sql, username, id);
        jdbcTemplate.update(updateSql, id);
        return true;
    }

    public boolean writeComment(int postId, WriteCommentDto writeCommentDto) {
        String sql = "insert into comment(post_id, username, content, numLikes) values (?, ?, ?, ?);";
        jdbcTemplate.update(sql, postId, writeCommentDto.getUsername(), writeCommentDto.getContent(), 0);
        return true;
    }

    public boolean likeComment(int postId, int commentId, String username) {
        String sql = "insert into likeComment(username, post_id, comment_id) values (?, ?, ?);";
        String updateSql = "update comment set numLikes = numLikes + 1 where id = ?;";
        jdbcTemplate.update(sql, username, postId, commentId);
        jdbcTemplate.update(updateSql, commentId);
        return true;
    }
}
