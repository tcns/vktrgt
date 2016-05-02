package ru.tcns.vktrgt.service.external.vk.impl;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.json.JSONException;
import ru.tcns.vktrgt.domain.external.vk.internal.Comment;
import ru.tcns.vktrgt.domain.external.vk.internal.WallPost;
import ru.tcns.vktrgt.domain.external.vk.response.*;
import ru.tcns.vktrgt.domain.util.DateUtils;
import ru.tcns.vktrgt.domain.util.parser.ResponseParser;
import ru.tcns.vktrgt.service.external.vk.intf.WallService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by TIMUR on 29.04.2016.
 */
public class WallServiceImpl implements WallService {

    private WallPostsResponse getWallPosts(int ownerId, int offset, int count) {
        try {
            String url = PREFIX + "get?owner_id=" + ownerId + "&offset=" + offset
                + "&count=" + count + VERSION;
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            WallPostsResponse response = new ResponseParser<>(WallPostsResponse.class).parseResponseString
                (ans, RESPONSE_STRING);
            return response;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new WallPostsResponse();
    }

    @Override
    public List<WallPost> getWallPosts(Integer ownerId, Integer maxDays) {
        List<WallPost> posts = new ArrayList<>();
        try {
            Integer count = getWallPosts(ownerId, 0, 2).getCount();
            if (count == null) {
                count = 0;
            }
            posts = new ArrayList<>(count);
            for (int i = 0; i < count; i += 100) {
                List<WallPost> cur = getWallPosts(ownerId, i, 100).getItems();
                posts.addAll(cur);
                if (!cur.isEmpty()) {
                    WallPost post = cur.get(0);
                    if (isBefore(maxDays, post)) {
                        break;
                    }
                }
            }
            return posts;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return posts;
    }

    private LikesResponse getLikes(Integer ownerId, String type, int wallId, int offset, int count) {
        try {
            String url = LIKES_PREFIX + "getList?owner_id=" + ownerId + "&item_id=" + wallId + "&type=" + type + "&offset=" + offset
                + "&count=" + count + VERSION;
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            LikesResponse response = new ResponseParser<>(LikesResponse.class).parseResponseString
                (ans, RESPONSE_STRING);
            return response;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new LikesResponse();
    }

    @Override
    public List<Integer> getTopicCommentsWithLikes(Integer ownerId, Integer postId) {
        List<Integer> comments = new ArrayList<>();
        try {
            Integer count = getTopicComments(ownerId, postId, 0, 1).getCount();
            comments = new ArrayList<>(count);
            for (int i = 0; i < count; i += 100) {
                List<Comment> items = getTopicComments(ownerId, postId, i, 100).getItems();
                List<Integer> cur = items
                    .stream().map(a -> a.getFromId()).collect(Collectors.toList());
                List<Integer> likes = new ArrayList<>();
                for (Comment comment: items) {
                    likes = getLikes(postId, comment.getId(), "topic_comment");
                }
                comments.addAll(likes);
                comments.addAll(cur);
            }
            return comments;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return comments;
    }
    private CommentsResponse getTopicComments(Integer groupId, int topicId, int offset, int count) {
        try {
            String url = TOPIC_PREFIX + "getComments?group_id=" + Math.abs(groupId) + "&topic_id=" + topicId + "&offset=" + offset
                + "&count=" + count + VERSION;
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            CommentsResponse response = new ResponseParser<>(CommentsResponse.class).parseResponseString
                (ans, RESPONSE_STRING);
            return response;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new CommentsResponse();
    }

    private CommentsResponse getComments(Integer ownerId, int postId, int offset, int count) {
        try {
            String url = PREFIX + "getComments?owner_id=" + ownerId + "&post_id=" + postId + "&offset=" + offset
                + "&count=" + count + VERSION;
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            CommentsResponse response = new ResponseParser<>(CommentsResponse.class).parseResponseString
                (ans, RESPONSE_STRING);
            return response;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new CommentsResponse();
    }

    @Override
    public List<Integer> getComments(Integer ownerId, Integer postId) {
        List<Integer> comments = new ArrayList<>();
        try {
            Integer count = getComments(ownerId, postId, 0, 2).getCount();
            comments = new ArrayList<>(count);
            for (int i = 0; i < count; i += 100) {
                List<Integer> cur = getComments(ownerId, postId, i, 100).getItems()
                    .stream().map(a->a.getFromId()).collect(Collectors.toList());
                comments.addAll(cur);
            }
            return comments;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return comments;
    }

    private RepostResponse getReposts(Integer ownerId, int postId, int offset, int count) {
        try {
            String url = PREFIX + "getReposts?owner_id=" + ownerId + "&post_id=" + postId + "&offset=" + offset
                + "&count=" + count + VERSION;
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            RepostResponse response = new ResponseParser<>(RepostResponse.class).parseResponseString
                (ans, RESPONSE_STRING);
            return response;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new RepostResponse();
    }
    @Override
    public List<Integer> getReposts(Integer ownerId, Integer postId) {
        List<Integer> reposts = new ArrayList<>();
        try {
            reposts = new ArrayList<>(1000);
            int i = 0;
            while (true) {
                RepostResponse repostResponse = getReposts(ownerId, postId, i, 1000);
                if (repostResponse.getItems()==null || repostResponse.getItems().isEmpty()) {
                    break;
                }
                List<Integer> cur = repostResponse.getItems()
                    .stream().map(a->a.getFromId()).collect(Collectors.toList());
                reposts.addAll(cur);
                i+=1000;
            }
            return reposts;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reposts;
    }

    @Override
    public List<Integer> getLikes(Integer ownerId, Integer postId, String type) {
        List<Integer> likes = new ArrayList<>();
        try {
            Integer count = getLikes(ownerId, type, postId, 0, 2).getCount();
            likes = new ArrayList<>(count);
            for (int i = 0; i < count; i += 1000) {
                List<Integer> cur = getLikes(ownerId, type, postId, i, 1000).getItems();
                likes.addAll(cur);
            }
            return likes;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return likes;
    }

    private boolean isBefore(Integer maxDays, WallPost post) {
        return DateUtils.parseUnixTime("" + post.getDate()).before(org.apache.commons.lang.time.DateUtils.addDays(new Date(), -maxDays));
    }
}
