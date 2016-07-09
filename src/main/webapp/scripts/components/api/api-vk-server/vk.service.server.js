angular.module('vktrgtApp')
    .factory('VKCloudService', function ($rootScope, $cookies, $http, $q) {
        return {
            searchGroups: function (q) {
                return $http.get('/api/groups/vk?q=' + q);
            },
            intersectGroups: function (ids, taskInfo, file, minCount) {
                var fd = new FormData();
                fd.append('file', file);
                fd.append('names', ids.join(','))
                fd.append('taskInfo', taskInfo)
                fd.append('minCount', minCount)
                return $http.post('/api/groups/users', fd, {
                    headers: {'Content-Type': undefined},
                    transformRequest: angular.identity
                });
            }, intersectUsers: function (ids, taskInfo, min, file) {
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
            }, groupMembers: function (groupId, taskInfo) {
                return $http.post('/api/groups/members',
                    {
                        'groupId': groupId,
                        'taskInfo': taskInfo
                    }, {
                        headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                        transformRequest: function (data) {
                            return $.param(data);
                        }
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
            }

        }

    }
)
