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
        String getMediaUrlSql = "select id, mediaMode, url from url where id = ?;";
        List<PostDto> postDtos = jdbcTemplate.query(getMediaUrlSql, (rs, rowNum) -> {
            return new PostDto(rs.getInt("id"), rs.getString("mediaMode"), rs.getString("url"));
        }, postDto.getMediaId());
        String insertSql = "insert into post(username, mediaID, mediaTitle, mediaType, postTitle" +
                ", postContent, mediaURL, numLikes, numComments) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PostDto urlInfo = postDtos.get(0);

        jdbcTemplate.update(insertSql, postDto.getUsername(), urlInfo.getId(), postDto.getMediaTitle(), urlInfo.getMediaMode()
                , postDto.getPostTitle(), postDto.getPostContent(), urlInfo.getUrl(), 0, 0);

        return "true";
    }

    public List<ResponsePostListDto> getPosts(String username) {
        String getSql = "select id, username, mediaTitle, mediaType, postTitle, numLikes, numComments from post;";
        List<ResponsePostListDto> posts = jdbcTemplate.query(getSql, (rs, rowNum) -> {
            return new ResponsePostListDto(rs.getInt("id"), rs.getString("username"),
                    rs.getString("mediaTitle"), rs.getString("mediaType"), rs.getString("postTitle"), rs.getInt("numLikes"),
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
        String getSql = "select username, mediaTitle, postTitle, mediaType, postContent, mediaURL, numLikes from post where id = ?;";
        List<GetPostInfoDto> post = jdbcTemplate.query(getSql, (rs, rowNum) -> {
            return new GetPostInfoDto(id, rs.getString("username"), rs.getString("mediaTitle"),
                    rs.getString("postTitle"), rs.getString("mediaType"), rs.getString("postContent"), rs.getString("mediaURL"),
                    rs.getInt("numLikes"), false);
        }, id);

        ResponsePostDto responsePostDto = new ResponsePostDto(post.get(0));
        String hasLikedSql = "select id from likePost where username = ? and post_id = ?;";
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
        String checkSql = "select id from likePost where username = ? and post_id = ?;";
        String sql = "insert into likePost(username, post_id) values (?, ?);";
        String updateSql = "update post set numLikes = numLikes + 1 where id = ?;";
        String minusSql = "update post set numLikes = numLikes - 1 where id = ?;";
        String deleteSql = "delete from likePost where username = ? and post_id = ?;";
        List<Integer> ids = jdbcTemplate.query(checkSql, (rs, rowNum) -> Integer.valueOf(rs.getInt("id")), username, id);
        if(ids.isEmpty()) {
            jdbcTemplate.update(sql, username, id);
            jdbcTemplate.update(updateSql, id);
        } else {
            jdbcTemplate.update(deleteSql, username, id);
            jdbcTemplate.update(minusSql, id);
        }
        return true;
    }

    public boolean writeComment(int postId, WriteCommentDto writeCommentDto) {
        String sql = "insert into comment(post_id, username, content, numLikes) values (?, ?, ?, ?);";
        jdbcTemplate.update(sql, postId, writeCommentDto.getUsername(), writeCommentDto.getContent(), 0);

        String updateSql = "update post set numComments = numComments + 1 where id = ?;";
        jdbcTemplate.update(updateSql, postId);
        return true;
    }

    public boolean likeComment(int postId, int commentId, String username) {
        String checkSql = "select id from likeComment where username = ? and post_id = ? and comment_id = ?;";
        String sql = "insert into likeComment(username, post_id, comment_id) values (?, ?, ?);";
        String updateSql = "update comment set numLikes = numLikes + 1 where id = ?;";
        String minusSql = "update comment set numLikes = numLikes - 1 where id = ?;";
        String deleteSql = "delete from likeComment where username = ? and post_id = ? and comment_id = ?;";
        List<Integer> ids = jdbcTemplate.query(checkSql, (rs, rowNum) -> {
            return Integer.valueOf(rs.getInt("id"));
        }, username, postId, commentId);
        if(ids.isEmpty()) {
            jdbcTemplate.update(sql, username, postId, commentId);
            jdbcTemplate.update(updateSql, commentId);
        }else {
            jdbcTemplate.update(deleteSql, username, postId, commentId);
            jdbcTemplate.update(minusSql, commentId);
        }
        return true;
    }
}
