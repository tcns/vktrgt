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
import ru.tcns.vktrgt.service.export.impl.ExportService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by TIMUR on 29.04.2016.
 */
@Service
public class WallServiceImpl extends AbstractWallService {

    @Inject
    ExportService exportService;

    @Override
    @Async
    public Future<List<WallPost>> getWallPosts(UserTask userTask, Integer ownerId, Integer maxCount) {
        return new AsyncResult<>(getWallPostsSync(userTask, ownerId, maxCount));
    }

    @Override
    @Async
    public Future<List<Integer>> getTopicCommentsWithLikes(UserTask userTask, Integer ownerId, Integer postId) {
        return new AsyncResult<>(getTopicCommentsWithLikesSync(userTask, ownerId, postId));
    }

    @Override
    @Async
    public Future<List<Integer>> getComments(UserTask userTask, Integer ownerId, Integer postId) {
        return new AsyncResult<>(getCommentsSync(userTask, ownerId, postId));
    }

    @Override
    @Async
    public Future<List<Integer>> getReposts(UserTask userTask, Integer ownerId, Integer postId) {
        return new AsyncResult<>(getRepostsSync(userTask, ownerId, postId));
    }

    @Override
    public List<WallPost> getWallPostsSync(UserTask userTask, Integer ownerId, Integer maxDays) {
        userTask = userTask.startWork();
        final List<WallPost> posts;
        try {
            Integer count = getWallPosts(ownerId, 0, 1).getCount();
            if (maxDays == null) {
                maxDays = VKDicts.MAX_DAYS_COUNT_FOR_WALL_PARSING;
            }
            if (count == null) {
                return new ArrayList<>();
            }
            count = Math.min(maxDays, count);
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
            userTask.saveFinal(exportService.getStreamFromObject(posts));
            return posts;
        } catch (JSONException e) {
            userTask.saveFinalError(e);
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Integer> getLikesSync(UserTask userTask, Integer ownerId, Integer postId, String type) {
        final List<Integer> likes;
        userTask = userTask.startWork();
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
            userTask.saveFinal(exportService.getStreamFromObject(likes));
            service.shutdown();
            return likes;
        } catch (JSONException e) {
            userTask.saveFinalError(e);
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Integer> getCommentsSync(UserTask userTask, Integer ownerId, Integer postId) {
        final List<Integer> comments;
        userTask = userTask.startWork();
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
            userTask.saveFinal(exportService.getStreamFromObject(comments));
            return comments;
        } catch (JSONException e) {
            userTask.saveFinalError(e);
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Integer> getTopicCommentsWithLikesSync(UserTask userTask, Integer ownerId, Integer postId) {
        userTask = userTask.startWork();
        final List<Integer> comments;
        try {
            Integer count = getTopicComments(ownerId, postId, 0, 1).getCount();
            userTask = userTask.saveInitial(count);
            comments = new ArrayList<>(count);
            ExecutorService service = Executors.newFixedThreadPool(100);
            List<Future<List<Integer>>> tasks = new ArrayList<>();
            final UserTask userTaskFinal = userTask.copyNoCreate();
            for (int i = 0; i < count; i += 100) {
                final int curId = i;
                tasks.add(service.submit(() -> {
                        List<Comment> items = getTopicComments(ownerId, postId, curId, 100).getItems();
                        List<Integer> cur = items
                            .parallelStream().map(a -> a.getFromId()).collect(Collectors.toList());
                        for (Comment comment : items) {
                            cur.addAll(getLikesSync(userTaskFinal, postId, comment.getId(), "topic_comment"));
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
            userTask.saveFinal(exportService.getStreamFromObject(comments));
            return comments;
        } catch (JSONException e) {
            userTask.saveFinalError(e);
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Integer> getRepostsSync(UserTask userTask, Integer ownerId, Integer postId) {
        List<Integer> reposts = new ArrayList<>();
        userTask = userTask.create();
        try {
            reposts = new ArrayList<>(1000);
            int i = 0;
            //TODO Это пиздец, можно делать запрос сначала на количество репостов
            while (true) {
                RepostResponse repostResponse = getReposts(ownerId, postId, i, 1000);
                if (repostResponse == null || repostResponse.getItems() == null || repostResponse.getItems().isEmpty()) {
                    break;
                }
                List<Integer> cur = repostResponse.getItems()
                    .parallelStream().map(a -> a.getFromId()).collect(Collectors.toList());
                reposts.addAll(cur);
                i += 1000;
                userTask = userTask.saveProgress(i);
            }
            userTask.saveFinal(exportService.getStreamFromObject(reposts));
            return reposts;
        } catch (JSONException e) {
            userTask.saveFinalError(e);
            e.printStackTrace();
        }
        return reposts;
    }

    @Override
    @Async
    public Future<List<Integer>> getLikes(UserTask userTask, Integer ownerId, Integer postId, String type) {
        return new AsyncResult<>(getLikesSync(userTask, ownerId, postId, type));
    }
}
