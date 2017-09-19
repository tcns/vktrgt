angular.module('vktrgtApp')
    .factory('VKCloudService', function ($rootScope, $cookies, $http, $q) {
        return {
            exportToFile: function (content, filename) {
                var fd = new FormData();
                fd.append('content', content);
                fd.append('fileName', filename)
                return $http.post('/api/tasks/export/content', fd, {
                    headers: {'Content-Type': undefined},
                    transformRequest: angular.identity
                });
            },
            searchGroups: function (q) {
                return $http.get('/api/groups/vk?q=' + q);
            },
            searchAudio: function (names, audios, taskInfo, file) {
                var fd = new FormData();
                fd.append('file', file);
                fd.append('users', names.join(','))
                fd.append('audios', audios.join(','))
                fd.append('taskInfo', taskInfo)
                return $http.post('/api/users/audio', fd, {
                    headers: {'Content-Type': undefined},
                    transformRequest: angular.identity
                });
            }, intersectGroups: function (ids, taskInfo, file, minCount) {
                var fd = new FormData();
                fd.append('file', file);
                fd.append('names', ids.join(','))
                fd.append('taskInfo', taskInfo)
                fd.append('minCount', minCount)
                return $http.post('/api/groups/users', fd, {
                    headers: {'Content-Type': undefined},
                    transformRequest: angular.identity
                });
            }, opinionLeaders: function (ids, taskInfo, min, file) {
                var fd = new FormData();
                fd.append('file', file);
                fd.append('users', ids.join(','))
                fd.append('taskInfo', taskInfo)
                fd.append('min', min)
                return $http.post('/api/users/leaders', fd, {
                    headers: {'Content-Type': undefined},
                    transformRequest: angular.identity
                });
            }, intersectUsersByGroups: function (ids, taskInfo, min, file) {
                var fd = new FormData();
                fd.append('file', file);
                fd.append('users', ids.join(','))
                fd.append('taskInfo', taskInfo)
                fd.append('min', min)
                return $http.post('/api/users/groups', fd, {
                    headers: {'Content-Type': undefined},
                    transformRequest: angular.identity
                });
            }, listOperation: function (ids1, ids2, file1, file2, type, taskInfo) {
                var fd = new FormData();
                fd.append('file1', file1);
                fd.append('file2', file2);
                fd.append('ids1', ids1.join(','))
                fd.append('ids2', ids2.join(','))
                fd.append('taskInfo', taskInfo)
                fd.append('type', type)
                return $http.post('/api/lists', fd, {
                    headers: {'Content-Type': undefined},
                    transformRequest: angular.identity
                });
            }, groupsInfo: function (ids, taskInfo, file) {
                var fd = new FormData();
                fd.append('file', file);
                fd.append('names', ids.join(','))
                fd.append('taskInfo', taskInfo)
                return $http.post('/api/groups/info/vk', fd, {
                    headers: {'Content-Type': undefined},
                    transformRequest: angular.identity
                });
            }, getGroupActivity: function (activeAuditoryDTO, file) {
                var fd = new FormData();
                fd.append('file', file)
                fd.append('dto', JSON.stringify(activeAuditoryDTO))
                return $http.post('/api/activity', fd, {
                    headers: {'Content-Type': undefined},
                    transformRequest: angular.identity
                });
            }, topicActivity: function (ids, minCount, taskInfo, file) {
                var fd = new FormData();
                fd.append('file', file);
                fd.append('minCount', minCount);
                fd.append('topicUrls', ids.join(','))
                fd.append('taskInfo', taskInfo)
                return $http.post('/api/topics', fd, {
                    headers: {'Content-Type': undefined},
                    transformRequest: angular.identity
                });
            }, groupMembers: function (groupIds, taskInfo, file) {
                var fd = new FormData();
                fd.append('file', file);
                fd.append('taskInfo', taskInfo)
                fd.append('groupIds', groupIds)
                return $http.post('/api/groups/members', fd, {
                    headers: {'Content-Type': undefined},
                    transformRequest: angular.identity
                });
            }, analyseUsers: function (model, file) {
                var fd = new FormData();
                fd.append('file', file)
                fd.append('dto', JSON.stringify(model))
                return $http.post('/api/analyse', fd, {
                    headers: {'Content-Type': undefined},
                    transformRequest: angular.identity
                });
            }, filterUsers: function (model, file) {
                var fd = new FormData();
                fd.append('file', file)
                fd.append('dto', JSON.stringify(model))
                return $http.post('/api/filter', fd, {
                    headers: {'Content-Type': undefined},
                    transformRequest: angular.identity
                });
            }, userIdToURL: function (ids, taskInfo, file) {
                var fd = new FormData();
                fd.append('file', file)
                fd.append('userIds', ids)
                fd.append('taskInfo', taskInfo)
                return $http.post('/api/users/url', fd, {
                    headers: {'Content-Type': undefined},
                    transformRequest: angular.identity
                });
            }, userURLToId: function (ids, taskInfo, file) {
                var fd = new FormData();
                fd.append('file', file)
                fd.append('userUrl', ids)
                fd.append('taskInfo', taskInfo)
                return $http.post('/api/users/urlid', fd, {
                    headers: {'Content-Type': undefined},
                    transformRequest: angular.identity
                });
            }, userInfo: function (ids, taskInfo, file) {
                var fd = new FormData();
                fd.append('file', file)
                fd.append('userIds', ids)
                fd.append('taskInfo', taskInfo)
                return $http.post('/api/users/info', fd, {
                    headers: {'Content-Type': undefined},
                    transformRequest: angular.identity
                });
            }, nearestBirthdays: function (userIds, relativeTypes, genders, fromDays,  nearestDays, taskInfo, file) {
                var fd = new FormData();
                fd.append('file', file)
                fd.append('userIds', userIds)
                fd.append('taskInfo', taskInfo)
                fd.append('relativeTypes', relativeTypes)
                fd.append('genders', genders)
                fd.append('fromDays', fromDays)
                fd.append('nearestDays', nearestDays)
                return $http.post('/api/users/birth', fd, {
                    headers: {'Content-Type': undefined},
                    transformRequest: angular.identity
                });
            },
            usersLikes: function (ids, taskInfo, file) {
                var fd = new FormData();
                fd.append('file', file)
                fd.append('userIds', ids)
                fd.append('taskInfo', taskInfo)
                return $http.post('/api/users/like', fd, {
                    headers: {'Content-Type': undefined},
                    transformRequest: angular.identity
                });
            }

        }

    }
)
