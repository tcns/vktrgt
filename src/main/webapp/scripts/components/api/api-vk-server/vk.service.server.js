angular.module('vktrgtApp')
    .factory('VKCloudService', function ($rootScope, $cookies, $http, $q) {
        return {
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
            }, intersectUsers: function (ids, taskInfo, min) {
                return $http.post('/api/users/leaders',
                    {
                        'users': ids.join(','),
                        'min': min,
                        'taskInfo': taskInfo
                    }, {
                        headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
                        transformRequest: function (data) {
                            return $.param(data);
                        }
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
            }, analyseUsers: function (model) {
                return $http.post('/api/analyse', model);
            }, filterUsers: function (model) {
                return $http.post('/api/filter', model);
            }

        }

    }
)
