package ru.tcns.vktrgt.service.external.vk.impl;

import org.json.JSONException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import ru.tcns.vktrgt.domain.UserTask;
import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.dict.VKDicts;
import ru.tcns.vktrgt.domain.external.vk.internal.Comment;
import ru.tcns.vktrgt.domain.external.vk.internal.WallPost;
import ru.tcns.vktrgt.domain.external.vk.response.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by TIMUR on 29.04.2016.
 */
@Service
public class WallServiceImpl extends AbstractWallService {

    @Override
    @Async
    public Future<List<WallPost>> getWallPosts(UserTaskSettings settings, Integer ownerId, Integer maxCount) {
        UserTask userTask = new UserTask(WALL_POSTS, settings, userTaskRepository);
        final List<WallPost> posts;
        try {
            Integer count = getWallPosts(ownerId, 0, 1).getCount();
            if (maxCount == null) {
                maxCount = VKDicts.MAX_DAYS_COUNT_FOR_WALL_PARSING;
            }
            if (count == null) {
                return new AsyncResult<>(new ArrayList<>());
            }
            count = Math.min(maxCount, count);
            userTask = userTask.saveInitial(count);
            ExecutorService service = Executors.newFixedThreadPool(50);
            List<Future<List<WallPost>>> tasks = new ArrayList<>();
            posts = new ArrayList<>(count);
            for (int i = 0; i < count; i += 100) {
                final int cur = i;
                tasks.add(service.submit(() -> getWallPosts(ownerId, cur, 100).getItems()));
            }
            for (Future<List<WallPost>> a : tasks) {
                try {
                    List<WallPost> list = a.get();
                    userTask = userTask.saveProgress(list.size());
                    posts.addAll(list);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            service.shutdown();
            userTask.saveFinal(posts);
            return new AsyncResult<>(posts);
        } catch (JSONException e) {
            userTask.saveFinalError(e);
            e.printStackTrace();
        }
        return new AsyncResult<>(new ArrayList<>());
    }

    @Override
    @Async
    public Future<List<Integer>> getTopicCommentsWithLikes(UserTaskSettings settings, Integer ownerId, Integer postId) {
        UserTask userTask = new UserTask(TOPIC_COMMENTS, settings, userTaskRepository);
        final List<Integer> comments;
        try {
            Integer count = getTopicComments(ownerId, postId, 0, 1).getCount();
            userTask = userTask.saveInitial(count);
            comments = new ArrayList<>(count);
            ExecutorService service = Executors.newFixedThreadPool(100);
            List<Future<List<Integer>>> tasks = new ArrayList<>();
            for (int i = 0; i < count; i += 100) {
                final int curId = i;
                tasks.add(service.submit(() -> {
                        List<Comment> items = getTopicComments(ownerId, postId, curId, 100).getItems();
                        List<Integer> cur = items
                            .parallelStream().map(a -> a.getFromId()).collect(Collectors.toList());
                        for (Comment comment : items) {
                            cur.addAll(getLikes(settings, postId, comment.getId(), "topic_comment").get());
                        }
                        return cur;
                    }
                ));
            }
            for (Future<List<Integer>> task : tasks) {
                try {
                    List<Integer> resp = task.get();
                    userTask = userTask.saveProgress(resp.size());
                    comments.addAll(resp);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            service.shutdown();
            userTask.saveFinal(comments);
            return new AsyncResult<>(comments);
        } catch (JSONException e) {
            userTask.saveFinalError(e);
            e.printStackTrace();
        }
        return new AsyncResult<>(new ArrayList<>());
    }

    @Override
    @Async
    public Future<List<Integer>> getComments(UserTaskSettings settings, Integer ownerId, Integer postId) {
        final List<Integer> comments;
        UserTask userTask = new UserTask(COMMENTS, settings, userTaskRepository);
        try {
            Integer count = getComments(ownerId, postId, 0, 1).getCount();
            userTask = userTask.saveInitial(count);
            comments = new ArrayList<>(count);
            ExecutorService service = Executors.newFixedThreadPool(100);
            List<Future<List<Integer>>> tasks = new ArrayList<>();
            for (int i = 0; i < count; i += 100) {
                final int cur = i;
                tasks.add(service.submit(() -> getComments(ownerId, postId, cur, 100).getItems()
                    .parallelStream().map(a -> a.getFromId()).collect(Collectors.toList())));
            }
            for (Future<List<Integer>> a : tasks) {
                try {
                    List<Integer> list = a.get();
                    userTask = userTask.saveProgress(list.size());
                    comments.addAll(list);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            service.shutdown();
            userTask.saveFinal(comments);
            return new AsyncResult<>(comments);
        } catch (JSONException e) {
            userTask.saveFinalError(e);
            e.printStackTrace();
        }
        return new AsyncResult<>(new ArrayList<>());
    }

    @Override
    @Async
    public Future<List<Integer>> getReposts(UserTaskSettings settings, Integer ownerId, Integer postId) {
        List<Integer> reposts = new ArrayList<>();
        UserTask userTask = new UserTask(REPOSTS, settings, userTaskRepository);
        try {
            reposts = new ArrayList<>(1000);
            int i = 0;
            //TODO Это пиздец, можно делать запрос сначала на количество репостов
            while (true) {
                RepostResponse repostResponse = getReposts(ownerId, postId, i, 1000);
                if (repostResponse.getItems() == null || repostResponse.getItems().isEmpty()) {
                    break;
                }
                List<Integer> cur = repostResponse.getItems()
                    .parallelStream().map(a -> a.getFromId()).collect(Collectors.toList());
                reposts.addAll(cur);
                i += 1000;
                userTask = userTask.saveProgress(i);
            }
            userTask.saveFinal(reposts);
            return new AsyncResult<>(reposts);
        } catch (JSONException e) {
            userTask.saveFinalError(e);
            e.printStackTrace();
        }
        return new AsyncResult<>(reposts);
    }

    @Override
    @Async
    public Future<List<Integer>> getLikes(UserTaskSettings settings, Integer ownerId, Integer postId, String type) {
        final List<Integer> likes;
        UserTask userTask = new UserTask(LIKES, settings, userTaskRepository);
        try {
            Integer count = getLikes(ownerId, type, postId, 0, 1).getCount();
            userTask = userTask.saveInitial(count);
            likes = new ArrayList<>(count);
            ExecutorService service = Executors.newFixedThreadPool(10);
            List<Future<List<Integer>>> tasks = new ArrayList<>();
            for (int i = 0; i < count; i += 1000) {
                final int cur = i;
                tasks.add(service.submit(() -> getLikes(ownerId, type, postId, cur, 1000).getItems()));
            }
            for (Future<List<Integer>> a : tasks) {
                try {
                    List<Integer> list = a.get();
                    userTask = userTask.saveProgress(list.size());
                    likes.addAll(list);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            userTask.saveFinal(likes);
            service.shutdown();
            return new AsyncResult<>(likes);
        } catch (JSONException e) {
            userTask.saveFinalError(e);
            e.printStackTrace();
        }
        return new AsyncResult<>(new ArrayList<>());
    }
}
